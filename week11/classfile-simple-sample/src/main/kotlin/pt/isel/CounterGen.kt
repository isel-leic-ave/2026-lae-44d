package pt.isel

import java.io.File
import java.io.FileOutputStream
import java.lang.classfile.ClassBuilder
import java.lang.classfile.ClassFile
import java.lang.classfile.ClassFile.ACC_FINAL
import java.lang.classfile.ClassFile.ACC_PRIVATE
import java.lang.classfile.ClassFile.ACC_PUBLIC
import java.lang.classfile.Interfaces
import java.lang.constant.ClassDesc
import java.lang.constant.ConstantDescs.CD_Object
import java.lang.constant.ConstantDescs.CD_int
import java.lang.constant.ConstantDescs.CD_void
import java.lang.constant.ConstantDescs.INIT_NAME
import java.lang.constant.ConstantDescs.MTD_void
import java.lang.constant.MethodTypeDesc

object CounterGen {

    const val FULLY_QUALIFIED_NAME = "pt.isel.Counter"

    // Class descriptors (metadata) for the class being generated and its interface
    private val counterClassDesc = ClassDesc.of(FULLY_QUALIFIED_NAME)
    private val sumClassDesc = ClassDesc.of("pt.isel.Sum")

    // Path logic to determine where the .class file should be saved
    private val classFileName = FULLY_QUALIFIED_NAME.replace(".", "/") + ".class"

    // Dynamically locate the root output directory where the compiled .class files should be stored
    private val resourcePath =
        Unit::class.java
            .getResource("/") // build/classes/kotlin/main/
            ?.toURI()
            ?.path

    /**
     * Dynamically generates a class similar to:
     * <pre>
     * package pt.isel;
     *
     * class Counter implements Sum {
     *     private final int nr;
     *
     *     public Counter(int nr) {
     *         this.nr = nr;
     *     }
     *
     *     public int add(int other) {
     *         return this.nr + other;
     *     }
     * }
     * </pre>
     */
    fun buildAndSave() {
        // println(resourcePath)
        val bytes =
            ClassFile.of().build(counterClassDesc) { clb: ClassBuilder ->
                clb
                    // Set class as: public final class Counter
                    .withFlags(ACC_FINAL or ACC_PUBLIC)
                    // Set interface: implements Sum
                    .withInterfaces(Interfaces.ofSymbols(sumClassDesc).interfaces())
                    // Define field: private int nr;
                    .withField("nr", CD_int, ACC_PRIVATE)
                    // Define Constructor: public Counter(int nr)
                    .withMethod(INIT_NAME, MethodTypeDesc.of(CD_void, CD_int), ACC_PUBLIC) { mb ->
                        mb.withCode { cob ->
                            cob
                                .aload(0)
                                .invokespecial(CD_Object, INIT_NAME, MTD_void)
                                .aload(0)
                                .iload(1)
                                .putfield(counterClassDesc, "nr", CD_int)
                                .return_()
                        }
                    }
                    // Define Method: public int add(int other)
                    .withMethod("add", MethodTypeDesc.of(CD_int, CD_int), ACC_PUBLIC) { mb ->
                        mb.withCode { cob ->
                            cob
                                .aload(0)
                                .getfield(counterClassDesc, "nr", CD_int)
                                .iload(1)
                                .iadd()
                                .ireturn()
                        }
                    }
            }

            // Create the output directory if it does not exist
            val outputFile = File(resourcePath, classFileName)
            outputFile.parentFile.mkdirs()

            // Save the bytes to the file system
            FileOutputStream(outputFile).use { fos ->
                fos.write(bytes)
            }
    }

}