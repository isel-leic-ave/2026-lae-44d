package pt.isel

import java.io.File
import java.io.FileOutputStream
import java.lang.classfile.ClassBuilder
import java.lang.classfile.ClassFile
import java.lang.classfile.ClassFile.ACC_BRIDGE
import java.lang.classfile.ClassFile.ACC_PUBLIC
import java.lang.classfile.ClassFile.ACC_SYNTHETIC
import java.lang.classfile.CodeBuilder
import java.lang.classfile.Interfaces
import java.lang.constant.ClassDesc
import java.lang.constant.ConstantDescs.CD_Object
import java.lang.constant.ConstantDescs.CD_boolean
import java.lang.constant.ConstantDescs.CD_char
import java.lang.constant.ConstantDescs.CD_double
import java.lang.constant.ConstantDescs.CD_float
import java.lang.constant.ConstantDescs.CD_int
import java.lang.constant.ConstantDescs.CD_long
import java.lang.constant.ConstantDescs.CD_short
import java.lang.constant.ConstantDescs.CD_void
import java.lang.constant.ConstantDescs.INIT_NAME
import java.lang.constant.ConstantDescs.MTD_void
import java.lang.constant.MethodTypeDesc
import java.net.URLClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.javaGetter

object DynamicMapper {

    private const val PACKAGE_NAME = "pt.isel"
    private val packageFolder = PACKAGE_NAME.replace(".", "/")

    private val currentDir: String = System.getProperty("user.dir") + "/"

    private val root =
        Unit::class.java
            .getResource("/")
            ?.toURI()
            ?.path

    /**
     * A new ClassLoader is required when the existing one loads classes from a JAR
     * and its resource path is null. In such cases, we create a ClassLoader that uses
     * the current working directory, as specified by the 'user.dir' system property.
     */
    private val rootLoader = URLClassLoader(arrayOf(File(root ?: currentDir).toURI().toURL()))

    /**
     * Cache of dynamically generated mappers keyed by the domain class.
     * Prevents repeated code generation and loading.
     */
    private val mappers = mutableMapOf<Pair<KClass<*>, KClass<*>>, Mapper<*, *>>()

    /**
     * Loads a dynamic mapper instance for the given domain class using its Java `Class`
     * representation. Delegates to the Kotlin version of `loadDynamicMapper`.
     * The @JvmStatic annotation is necessary because the loadDynamicMapper is called
     * using invokestatic (must be a static method).
     */
    @JvmStatic
    fun <T : Any, R : Any> loadDynamicMapper(
        srcType: Class<T>,
        destType: Class<R>,
    ) = loadDynamicMapper(srcType.kotlin, destType.kotlin)

    /**
     * Loads or creates a dynamic mapper instance for the given domain class.
     * If not already cached, it generates the class using a builder, loads it, and instantiates it.
     */
    fun <T : Any, R : Any> loadDynamicMapper(
        srcType: KClass<T>,
        destType: KClass<R>,
    ) = mappers.getOrPut(srcType to destType) {
        buildMapperClassFile(srcType, destType)
            .createInstance() as Mapper<*, *>
    } as Mapper<T, R>

    /**
     * Generates the class file for a mapper based on the structure of the given domain classes.
     * Uses code generation techniques (e.g., Class-File API) to build the repository implementation at runtime.
     *
     * @param src the Kotlin class of the source domain type.
     * @param dest the Kotlin class of the destination domain type.
     * @return the runtime-generated class implementing the repository logic.
     */
    private fun <T : Any, R : Any> buildMapperClassFile(
        src: KClass<T>,
        dest: KClass<R>,
    ): KClass<out Any> {
        val className = "${src.simpleName}2${dest.simpleName}"
        println("Generating Class: $className")
        buildMapperByteArray(className, src, dest)
        return rootLoader
            .loadClass("$PACKAGE_NAME.$className")
            .kotlin
    }

    fun <T : Any, R : Any> buildMapperByteArray(
        className: String,
        src: KClass<T>,
        dest: KClass<R>,
    ) {
        val destInit: KFunction<R> = MapperReflectUtils.findConstructor(src, dest)
        val props: Map<KParameter, KProperty<*>> = MapperReflectUtils.findMatchingProperties(src, destInit)

        val classDesc = ClassDesc.of(PACKAGE_NAME, className)
        val mapperInterface = Interfaces.ofSymbols(Mapper::class.descriptor())
        val bytes = ClassFile.of()
            .build(classDesc) { clb: ClassBuilder ->
                clb.withInterfaces(mapperInterface.interfaces())
                clb.withMethod(INIT_NAME, MethodTypeDesc.of(CD_void), ACC_PUBLIC) { mb ->
                    mb.withCode { genInitCode(it) }
                }
                clb.withMethod(
                    "mapFrom",
                    MethodTypeDesc.of(CD_Object, CD_Object),
                    ACC_PUBLIC or ACC_BRIDGE or ACC_SYNTHETIC
                ) { mb ->
                    mb.withCode { cob ->
                        cob.aload(0)
                        cob.aload(1)
                        cob.checkcast(src.descriptor())
                        // 1st argument: The class that owns the property (e.g., ArtistSpotify)
                        cob.invokevirtual(classDesc, "mapFrom", MethodTypeDesc.of(dest.descriptor(), src.descriptor()))
                        cob.areturn()
                    }
                }
                clb.withMethod("mapFrom", MethodTypeDesc.of(dest.descriptor(), src.descriptor()), ACC_PUBLIC) { mb ->
                    mb.withCode { cob ->
                        cob.new_(dest.descriptor())
                        cob.dup()
                        props.forEach { destParam, srcProp ->
                            // Primitive or String case:
                            if (srcProp.returnType.toKClass().isPrimitiveOrString()) {
                                cob.aload(1)
                                cob.invokevirtual(
                                    src.descriptor(),
                                    srcProp.javaGetter?.name,
                                    MethodTypeDesc.of(srcProp.returnType.descriptor())
                                )
                            }
                            // List case:
                            else if (srcProp.returnType.classifier == List::class && destParam.type.classifier == List::class) {
                                val elemSrcType = srcProp.returnType.arguments[0].type
                                val elemDestType = destParam.type.arguments[0].type
                                cob.ldc(cob.constantPool().classEntry(elemSrcType?.descriptor()))
                                cob.ldc(cob.constantPool().classEntry(elemDestType?.descriptor()))
                                cob.invokestatic(
                                    DynamicMapper::class.descriptor(),
                                    "loadDynamicMapper",
                                    MethodTypeDesc.of(
                                        Mapper::class.descriptor(),
                                        Class::class.descriptor(),
                                        Class::class.descriptor()
                                    ),
                                )
                                cob.aload(1)
                                cob.invokevirtual(
                                    src.descriptor(),
                                    srcProp.javaGetter?.name,
                                    MethodTypeDesc.of(srcProp.returnType.descriptor())
                                )
                                cob.invokeinterface(
                                    Mapper::class.descriptor(),
                                    "mapFromList",
                                    MethodTypeDesc.of(List::class.descriptor(), List::class.descriptor()),
                                )
                            }
                            else {
                                // Object Case
                                cob.ldc(cob.constantPool().classEntry(srcProp.returnType.descriptor()))
                                cob.ldc(cob.constantPool().classEntry(destParam.type.descriptor()))
                                cob.invokestatic(
                                    DynamicMapper::class.descriptor(),
                                    "loadDynamicMapper",
                                    MethodTypeDesc.of(
                                        Mapper::class.descriptor(),
                                        Class::class.descriptor(),
                                        Class::class.descriptor()
                                    ),
                                )
                                cob.aload(1)
                                cob.invokevirtual(
                                    src.descriptor(),
                                    srcProp.javaGetter?.name,
                                    MethodTypeDesc.of(srcProp.returnType.descriptor())
                                )
                                cob.invokeinterface(
                                    Mapper::class.descriptor(),
                                    "mapFrom",
                                    MethodTypeDesc.of(CD_Object, CD_Object),
                                )
                                cob.checkcast(destParam.type.descriptor())
                            }
                        }

                        cob.invokespecial(
                            dest.descriptor(),
                            INIT_NAME,
                            MethodTypeDesc.of(CD_void, destInit.parameters.map { it.type.descriptor() }),
                        )
                        cob.areturn()
                    }
                }
            }

            val outputFile = File("$root", "${packageFolder}/$className.class")
            outputFile.parentFile.mkdirs()

            // Save the bytes to the file system
            FileOutputStream(outputFile).use { fos ->
                fos.write(bytes)
        }
    }

    /**
     * Returns a ClassDesc of the type descriptor of the given KClass.
     */
    fun KClass<*>.descriptor(): ClassDesc {
        return if (this.java.isPrimitive) {
            when (this) {
                Char::class -> CD_char
                Short::class -> CD_short
                Int::class -> CD_int
                Long::class -> CD_long
                Float::class -> CD_float
                Double::class -> CD_double
                Boolean::class -> CD_boolean
                else -> {
                    throw IllegalStateException("No primitive type for ${this.qualifiedName}!")
                }
            }
        } else {
            ClassDesc.of(this.java.name)
        }
    }

    /**
     * Returns a ClassDesc of the type descriptor of the given KType.
     */
    private fun KType.descriptor(): ClassDesc {
        return this.toKClass().descriptor()
    }

    /**
     * Returns the KClass of the type.
     */
    private fun KType.toKClass(): KClass<*> {
        return this.classifier as KClass<*>
    }

    /**
     * Returns if the classifier is a primitive or String type.
     */
    private fun KClass<*>.isPrimitiveOrString(): Boolean {
        return this.java.isPrimitive || this == String::class
    }

    /**
     * Returns a CodeBuilder with the bytecode for the <init>()V
     * (constructor) method.
     */
    private fun genInitCode(cob: CodeBuilder): CodeBuilder {
        return cob
            .aload(0)
            .invokespecial(CD_Object, INIT_NAME, MTD_void)
            .return_()
    }

}