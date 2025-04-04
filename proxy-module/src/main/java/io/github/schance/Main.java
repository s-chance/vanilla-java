package io.github.schance;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws Exception {
        MyInterface proxyObject = MyInterfaceFactory.createProxyObject(new PrintFunctionName());
        proxyObject.fn1();
        proxyObject.fn2();
        proxyObject.fn3();

        System.out.println("====================");

        proxyObject = MyInterfaceFactory.createProxyObject(new PrintFunctionName1());
        proxyObject.fn1();
        proxyObject.fn2();
        proxyObject.fn3();

        System.out.println("====================");

        proxyObject = MyInterfaceFactory.createProxyObject(new LogHandler(proxyObject));
        proxyObject.fn1();
        proxyObject.fn2();
        proxyObject.fn3();

    }

    static class PrintFunctionName implements MyHandler {

        @Override
        public String functionBody(String methodName) {
            return "System.out.println(\"" + methodName + "\");";
        }
    }

    static class PrintFunctionName1 implements MyHandler {
        @Override
        public String functionBody(String methodName) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("System.out.println(1);")
                    .append("System.out.println(\"" + methodName + "\");");
            return stringBuilder.toString();
        }
    }

    static class LogHandler implements MyHandler {

        MyInterface myInterface;

        public LogHandler(MyInterface myInterface) {
            this.myInterface = myInterface;
        }

        @Override
        public void setProxy(MyInterface proxy) {
            Class<? extends MyInterface> aClass = proxy.getClass();
            Field field = null;
            try {
                field = aClass.getDeclaredField("myInterface");
                field.setAccessible(true);
                field.set(proxy, myInterface);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String functionBody(String methodName) {
            return """
                    System.out.println("before");
                    myInterface.%s();
                    System.out.println("after");
                    """.formatted(methodName);
        }

    }
}