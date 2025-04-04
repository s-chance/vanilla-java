package io.github.schance;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public class MyInterfaceFactory {

    private static AtomicInteger counter = new AtomicInteger();

    private static File createJavaFile(String className, MyHandler handler) throws IOException {
        String fn1Body = handler.functionBody("fn1");
        String fn2Body = handler.functionBody("fn2");
        String fn3Body = handler.functionBody("fn3");
        String context = """
                package io.github.schance;
                
                public class %s implements MyInterface {
                
                    MyInterface myInterface;
                
                    @Override
                    public void fn1() {
                        %s
                    }
                
                    @Override
                    public void fn2() {
                        %s
                    }
                
                    @Override
                    public void fn3() {
                        %s
                    }
                }
                """.formatted(className, fn1Body, fn2Body, fn3Body);
        File javaFile = new File(className + ".java");
        Files.writeString(javaFile.toPath(), context);
        return javaFile;
    }

    private static String getClassName() {
        return "MyInterface$proxy" + counter.incrementAndGet();
    }

    private static MyInterface newInstance(String className, MyHandler handler) throws Exception {
        Class<?> aClass = MyInterfaceFactory.class.getClassLoader().loadClass(className);
        Constructor<?> constructor = aClass.getConstructor();
        MyInterface proxy = (MyInterface) constructor.newInstance();
        handler.setProxy(proxy);
        return proxy;
    }

    public static MyInterface createProxyObject(MyHandler myHandler) throws Exception {
        String className = getClassName();
        File javaFile = createJavaFile(className, myHandler);
        Compiler.compile(javaFile);
        return newInstance("io.github.schance." + className, myHandler);

    }
}
