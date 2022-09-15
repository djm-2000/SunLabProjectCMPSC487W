package com.example.insurancedatabase;

import java.sql.*;

import oracle.jdbc.pool.OracleDataSource;

import java.util.Scanner;

public class DatabaseAccessObject {
    static Scanner scn = new Scanner(System.in);
    static String username, password;

    // TODO: refine data types for table attributes
    public static void initializeDatabase(Statement s) {
        String dropCustomerTable = "drop table Customer";
        String dropCarInsuranceTable = "drop table car_insurance";
        String dropHomeInsuranceTable = "drop table home_insurance";
        String dropLifeInsuranceTable = "drop table life_insurance";

        String createCustomerTable = "create table customer ( "
                + "customer_id numeric (14, 0), "
                + "first_name varchar (20), "
                + "last_name varchar (20), "
                + "primary key (customer_id) "
                + " )";

        String createCarInsuranceTable = "create table car_insurance ( "
                + "car_insurance_id numeric (14, 0), "
                + "make varchar (20), "
                + "year numeric (4, 0), "
                + "VIN varchar (30), "
                + "mileage_per_year varchar (30), "
                + "coverage varchar (30), "
                + "monthly_payment numeric (10, 2), "
                + "start_date varchar (10), "
                + "end_date varchar (10), "
                + "customer_id numeric (14, 0), "
                + "primary key (car_insurance_id), "
                + "foreign key (customer_id) references customer)";

        String createHomeInsuranceTable = "create table home_insurance ( "
                + "home_insurance_id numeric (14, 0), "
                + "house_address varchar (40), "
                + "house_area numeric (5, 0), "
                + "num_bedrooms numeric (2, 0), "
                + "num_bathrooms numeric (2, 0), "
                + "house_price numeric (10, 2), "
                + "coverage varchar (40), "
                + "monthly_payment numeric (10, 2), "
                + "start_date varchar (10), "
                + "end_date varchar (10), "
                + "customer_id numeric (14, 0), "
                + "primary key (home_insurance_id), "
                + "foreign key (customer_id) references customer)";

        String createLifeInsuranceTable = "create table life_insurance ( "
                + "life_insurance_id numeric (14, 0), "
                + "existing_conditions varchar (200), "
                + "coverage varchar (100), "
                + "beneficiary varchar (100), "
                + "monthly_payment numeric (10, 2), "
                + "start_date varchar (10), "
                + "customer_id numeric (14, 0), "
                + "primary key (life_insurance_id), "
                + "foreign key (customer_id) references customer)";

        try {
            s.execute(createCustomerTable);
            s.execute(createCarInsuranceTable);
            s.execute(createHomeInsuranceTable);
            s.execute(createLifeInsuranceTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static Connection getDatabaseConnection(String username, String password) {
        Connection connection = null;
        OracleDataSource ods = null;
        try {
            ods = new OracleDataSource();

            ods.setServiceName("orclpdb.hbg.psu.edu");
            ods.setURL("jdbc:oracle:thin:@h3oracle.ad.psu.edu:1521/orclpdb.ad.psu.edu");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ods.setUser(username);
        ods.setPassword(password);

        try {
            connection = ods.getConnection();
        } catch (SQLException e) {

        }

        return connection;

    }

    public static String[] getPolicies(Statement s) {
        int numCarPolicies = getNumberOfCarPolicies(s);
        int numHomePolicies = getNumberOfHomePolicies(s);
        int numLifePolicies = getNumberOfLifePolicies(s);

        String[] policyInfo = new String[numCarPolicies + numHomePolicies + numLifePolicies];

        String selectCar = "select * from car_insurance";
        String selectHome = "select * from home_insurance";
        String selectLife = "select * from life_insurance";

        ResultSet resultSet = null;
        try {
            // add car policies
            resultSet = s.executeQuery(selectCar);

            for (int i = 0; i < numCarPolicies; i++) {
                resultSet.next();

            }


        } catch (SQLException e) {

        }

        return policyInfo;
    }

    public static String[] getCustomers(Statement s) {
        int numberOfCustomers = getNumberOfCustomers(s);
        String[] customerInfo = new String[numberOfCustomers];

        String query = "select * from Customer";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            for (int i = 0; i < numberOfCustomers; i++) {
                resultSet.next();
                customerInfo[i] = resultSet.getString("first_name")
                        + " " + resultSet.getString("last_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerInfo;
    }

    public static boolean customerExists(Statement s, String firstName, String lastName) {

        String query = "select customer_id from customer where first_name = " + "\'" + firstName + "\'" + " and last_name = "
                + "\'" + lastName + "\'";
        ResultSet resultSet;
        try {
            resultSet = s.executeQuery(query);
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void addCustomer(Statement s, String firstName, String lastName) {
        int id = getNumberOfCustomers(s) + 1;
        String query = "insert into Customer values (" + id + ", \'" + firstName + "\'" + "," + "\'" + lastName + "\'" + ")";

        try {
            s.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getNumberOfCustomers(Statement s) {
        int numberOfCustomers = -1;
        String query = "select count(customer_id) from Customer";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            resultSet.next();
            numberOfCustomers = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfCustomers;
    }

    public static int getNumberOfLifePolicies(Statement s) {
        int numLifePolicies = -1;
        String query = "select count(life_insurance_id) from life_insurance";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            resultSet.next();
            numLifePolicies = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numLifePolicies;
    }

    public static int getNumberOfHomePolicies(Statement s) {
        int numHomePolicies = -1;
        String query = "select count(home_insurance_id) from home_insurance";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            resultSet.next();
            numHomePolicies = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numHomePolicies;
    }

    public static int getNumberOfCarPolicies(Statement s) {
        int numCarPolicies = -1;
        String query = "select count(car_insurance_id) from car_insurance";
        ResultSet resultSet = null;
        try {
            resultSet = s.executeQuery(query);

            resultSet.next();
            numCarPolicies = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numCarPolicies;
    }


}
