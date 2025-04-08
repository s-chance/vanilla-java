package io.github.schance.debug;

import io.github.schance.debug.compiler.JavaSourceFromString;
import io.github.schance.debug.compiler.MemoryFileManager;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.List;
import java.util.Map;

public class StringPoolDebugger {
    public static void main(String[] args) {
        // 1. 初始化编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // 2. 定义待编译的源码（测试final与非final拼接）
        String sourceCode =
                "public class Test {\n" +
                        "    private static final String FINAL_BASE = \"hello\";\n" +
                        "    private static final String FINAL_STR = FINAL_BASE + 2;\n" +  // 预期优化为"hello2"
                        "    private static String normalBase = \"world\";\n" +
                        "    private static String normalStr = normalBase + 2;\n" +       // 预期生成StringBuilder
                        "}";

        // 3. 创建内存中的源码对象
        JavaFileObject source = new JavaSourceFromString("Test", sourceCode);

        // 4. 配置自定义文件管理器
        try (MemoryFileManager fileManager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null))) {
            // 5. 执行编译
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    null,
                    null,
                    List.of(source)
            );
            boolean success = task.call();

            // 6. 输出诊断信息
            System.out.println("===== 编译结果 =====");
            System.out.println("编译是否成功? " + (success ? "是" : "否"));
            diagnostics.getDiagnostics().forEach(d ->
                    System.out.printf("Line %d: %s\n", d.getLineNumber(), d.getMessage(null))
            );

            // 7. 反编译验证优化行为
            if (success) {
                System.out.println("\n===== 字节码验证 =====");
                Map<String, byte[]> classBytes = fileManager.getClassBytes();
                classBytes.forEach((className, bytes) -> {
                    System.out.println("类名: " + className);
                    System.out.println("字节码长度: " + bytes.length + " bytes");
                    // 此处可调用ASM或javap进一步反编译
                });
            }
        } catch (Exception e) {
        }
    }
}
