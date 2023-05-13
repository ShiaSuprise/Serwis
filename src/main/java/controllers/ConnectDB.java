package controllers;


import java.sql.*;




public class ConnectDB {

    private Connection conn = null;


    public Connection connect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No MySQL JDBC Driver found." + "\n");
            e.printStackTrace();
            throw e;
        }

        try {
            conn = DriverManager.getConnection(createURL());
        } catch (SQLException e) {
            System.out.println("Connection Failed! Try again." + "\n");
            e.printStackTrace();
            throw e;
        }
        return conn;
    }

    private String createURL() {

        StringBuilder urlSB = new StringBuilder("jdbc:mysql://");
        urlSB.append("localhost:3306/");
        urlSB.append("pro?");
        urlSB.append("useUnicode=true&characterEncoding=utf");
        urlSB.append("-8&user=root");
        urlSB.append("&password=opli999");
        urlSB.append("&serverTimezone=CET");

        return urlSB.toString();
    }



}
