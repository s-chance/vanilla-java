package io.github.schance;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Compiler {
    public static void compile(File javaFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try (StandardJavaFileManager fileManager =
                        compiler.getStandardFileManager(null, null, null)) {

            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromFiles(List.of(javaFile));

            List<String> options = Arrays.asList("-d", "./target/classes");

            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    null,
                    options,
                    null,
                    compilationUnits
            );

            Boolean success = task.call();

            if (success) {
                System.out.println("Compilation successful");
            } else {
                System.out.println("Compilation failed");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
