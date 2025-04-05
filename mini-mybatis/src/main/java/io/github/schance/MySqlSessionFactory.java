package io.github.schance;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MySqlSessionFactory {

    public static final String JDBCURL = "jdbc:mysql://localhost:3306/blog";
    public static final String DBUSER = "admin";
    public static final String PASSWORD = "12345";

    @SuppressWarnings("all")
    public <T> T getMapper(Class<T> mapperClass) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperClass}, new MapperInvocationHandler());
    }

    static class MapperInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().startsWith("select")) {
                return invokeSelect(proxy, method, args);
            }
            return null;
        }

        private Object invokeSelect(Object proxy, Method method, Object[] args) {
            String sql = createSelectSql(method);
            System.out.println(sql);
//            List<String> selectCols = getSelectCols(method.getReturnType());
//            String sql = "select " + String.join(",", selectCols) + " from user where id = ?";
            try (Connection conn = DriverManager.getConnection(JDBCURL, DBUSER, PASSWORD)) {
                PreparedStatement statement = conn.prepareStatement(sql);
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Integer) {
                        statement.setInt(i + 1, (int) arg);
                    } else if (arg instanceof String) {
                        statement.setString(i + 1, arg.toString());
                    }
                }
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return parseResultSet(rs, method.getReturnType());
                }
            } catch (Exception e) {
            }
            return null;
        }

        private Object parseResultSet(ResultSet rs, Class<?> returnType) throws Exception {
            Constructor<?> constructor = returnType.getConstructor();
            Object result = constructor.newInstance();
            Field[] declaredFields = returnType.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Object column = null;
                String name = declaredField.getName();
                if (declaredField.getType() == String.class) {
                    column = rs.getString(name);
                } else if (declaredField.getType() == Integer.class) {
                    column = rs.getInt(name);
                }
                declaredField.setAccessible(true);
                declaredField.set(result, column);
            }
            return result;
        }

        private String createSelectSql(Method method) {
            StringBuilder sqlStringBuilder = new StringBuilder();
            sqlStringBuilder.append("select ");
            List<String> selectCols = getSelectCols(method.getReturnType());
            sqlStringBuilder.append(String.join(",", selectCols));
            sqlStringBuilder.append(" from ");
            String tableName = getSelectTableName(method.getReturnType());
            sqlStringBuilder.append(tableName);
            sqlStringBuilder.append(" where ");
            String param = getSelectParam(method);
            sqlStringBuilder.append(param);
            return sqlStringBuilder.toString();
        }

        private String getSelectParam(Method method) {
            return Arrays.stream(method.getParameters())
                    .map((parameter) -> {
                        Param param = parameter.getAnnotation(Param.class);
                        String column = param.value();
                        return column + " = ?";
                    }).collect(Collectors.joining(" and "));
        }

        private String getSelectTableName(Class<?> returnType) {
            Table table = returnType.getAnnotation(Table.class);
            if (table == null) {
                throw new RuntimeException("@Table is required");
            }
            return table.tableName();
        }

        private List<String> getSelectCols(Class<?> returnType) {
            Field[] declaredFields = returnType.getDeclaredFields();
            return Arrays.stream(declaredFields).map(Field::getName).toList();
        }
    }
}
