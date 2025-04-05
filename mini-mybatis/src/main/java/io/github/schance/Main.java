package io.github.schance;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactory();
        UserMapper mapper = mySqlSessionFactory.getMapper(UserMapper.class);
        User user = mapper.selectById(1);
        System.out.println(user);
        System.out.println(mapper.selectByName("Alice"));
        System.out.println(mapper.selectByNameAndAge("Bob", 25));
//        System.out.println(mapper.toString());
//        System.out.println(mapper.hashCode());
    }

    private static User jdbcSelectById(int id) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/blog";
        String dbUser = "admin";
        String password = "12345";

        String sql = "select * from user where id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        } catch (SQLException throwables) {
        }
        return null;
    }
}