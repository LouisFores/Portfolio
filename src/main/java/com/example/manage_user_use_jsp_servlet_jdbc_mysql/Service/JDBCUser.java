package com.example.manage_user_use_jsp_servlet_jdbc_mysql.Service;

import com.example.manage_user_use_jsp_servlet_jdbc_mysql.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUser implements IUser{
    private static final String  url = "jdbc:mysql://localhost:3306/quan_li_nguoi_dung";
    private static final String username = "root";
    private static final String password = "mySQL7122023@";
    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    public JDBCUser() {}
    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  connection;
    }
    public void insertUser(User user) throws SQLException {
        System.out.printf(INSERT_USERS_SQL);
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(INSERT_USERS_SQL)){
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public User selectUser(int id) {
        User user = null;
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID);) {
             statement.setInt(1, id);
             System.out.println(statement);
             ResultSet resultSet = statement.executeQuery();

             while (resultSet.next()) {
                 String name = resultSet.getString("name");
                 String email = resultSet.getString("email");
                 String country = resultSet.getString("country");
                 user = new User(id, name, email, country);
             }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = connect();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id, name, email, country));
                System.out.println(id + name +  email +  country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);){
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);){
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.out.println("SQLState: " + ((SQLException) e).getSQLState());
                System.out.println("Error Code: " + e.getMessage());
                System.out.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


}
