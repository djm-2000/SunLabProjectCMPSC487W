package com.example.sunlab;

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

import java.util.Scanner;

public class DatabaseAccessObject
{
    static Scanner scn = new Scanner(System.in);
    public static void initializeDatabase(Statement s)
    {
        String dropTable = "drop table student_login";
        String createTable = "create table student_login ( "
                + "student_id varchar (9), "
                + "timestamp varchar (26),"
                + "primary key (timestamp) "
                + " )";
        try
        {
            s.execute(dropTable);
           s.execute(createTable);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


    }
    public static Connection getDatabaseConnection(String username, String password)
    {
        Connection connection = null;
        OracleDataSource ods = null;
        try
        {
            ods = new OracleDataSource();

            ods.setServiceName("orcl");
            ods.setURL("jdbc:oracle:thin:@localhost:1521");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        ods.setUser(username);
        ods.setPassword(password);

        try
        {
            connection = ods.getConnection();
        }
        catch (SQLException e)
        {

        }

        return connection;

    }




    public static void addLogin(Statement s, String studentID, Timestamp timestamp)
    {
        PreparedStatement ps;
        int id = getNumberOfLogins(s) + 1;
      //  String query = "insert into student_login values (" + studentID + ", " + timestamp.toString().split(" ")[1] +  ")";
        String query = "insert into student_login values (?,?)";
        // TODO: don't hardcode username and password
        try {
            ps = getDatabaseConnection("system", "oracle").prepareStatement(query);
            ps.setString(1, studentID);
            ps.setString(2, timestamp.toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try
        {
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public static String[] getLogins(Statement s) {

        int numberOfLogins = getNumberOfLogins(s);

        String[] loginInfo = new String[numberOfLogins];

        String query = "select * from student_login";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            for (int i = 0; i < numberOfLogins; i++) {
                resultSet.next();
                loginInfo[i] = resultSet.getString("student_id")
                        + " " + resultSet.getString("timestamp");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return loginInfo;
    }

    public static String[] getLoginsByID(Statement s) {

        int numberOfLogins = getNumberOfLogins(s);

        String[] loginInfo = new String[numberOfLogins];

        String query = "select * from student_login ORDER BY student_id ASC";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            for (int i = 0; i < numberOfLogins; i++) {
                resultSet.next();
                loginInfo[i] = resultSet.getString("student_id")
                        + " " + resultSet.getString("timestamp");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return loginInfo;
    }

    public static String[] getLoginsByTime(Statement s) {

        int numberOfLogins = getNumberOfLogins(s);

        String[] loginInfo = new String[numberOfLogins];

        String query = "select * from student_login ORDER BY timestamp ASC";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            for (int i = 0; i < numberOfLogins; i++) {
                resultSet.next();
                loginInfo[i] = resultSet.getString("student_id")
                        + " " + resultSet.getString("timestamp");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return loginInfo;
    }

    public static int getNumberOfLogins(Statement s) {

        int numberOfLogins = -1;
        String query = "select count(student_id) from student_login";

        ResultSet rs = null;

        try {
            rs = s.executeQuery(query);

            rs.next();
            numberOfLogins = rs.getInt(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfLogins;
    }

}
