package pt.isel

import java.io.File
import java.io.FileOutputStream
import java.lang.classfile.ClassFile
import java.lang.classfile.ClassBuilder
import java.lang.classfile.ClassFile.ACC_PUBLIC
import java.lang.classfile.MethodBuilder
import java.lang.classfile.CodeBuilder
import java.lang.constant.ClassDesc
import java.lang.constant.MethodTypeDesc
import java.lang.constant.ConstantDescs.INIT_NAME
import java.lang.constant.ConstantDescs.MTD_void
import java.lang.constant.ConstantDescs.CD_Object
import java.lang.constant.ConstantDescs.CD_int

object BarGen {

    const val FULLY_QUALIFIED_NAME = "isel.pt.Bar"

    // Path logic to determine where the .class file should be saved
    private val classFileName = FULLY_QUALIFIED_NAME.replace(".", "/") + ".class"

    // Dynamically locate the root output directory where the compiled .class files should be stored
    private val resourcePath =
        Unit::class.java
            .getResource("/") // build/classes/kotlin/main/
            ?.toURI()
            ?.path

    /**
     * Generates a .class file for the 'isel.pt.Bar' class containing a 'foo()' method
     * that returns the integer value 67895.
     */
    fun buildAndSave() {

        // Define the class structure using the new Class-File API
        val bytes = ClassFile.of().build(ClassDesc.of(FULLY_QUALIFIED_NAME)) { clb: ClassBuilder ->
            clb.withFlags(ACC_PUBLIC) // public class Bar

                // Generating the Default Constructor: public Bar() { super(); }
                .withMethod(INIT_NAME, MTD_void, ACC_PUBLIC) { mb: MethodBuilder ->
                    mb.withCode { cob: CodeBuilder ->
                        cob.aload(0)
                            .invokespecial(CD_Object, INIT_NAME, MTD_void)
                            .return_()
                    }
                }

                // Generating the method: public int foo() { return 67895; }
                .withMethod("foo", MethodTypeDesc.of(CD_int), ACC_PUBLIC) { mb: MethodBuilder ->
                    mb.withCode { cob: CodeBuilder ->
                        cob
                            .ldc(clb.constantPool().intEntry(67895)) // Load the constant onto the stack
                            .ireturn()  // Return the integer
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