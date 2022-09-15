package com.example.insurancedatabase;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

public class App extends Application {
    private StackPane root = new StackPane();
    private Stage stage;

    static DatabaseAccessObject dao;
    static Connection connection;
    static Statement statement;

    static Insets defaultInsets = new Insets(10, 10, 10, 10);

    @Override
    public void init() {


        Button button = new Button("OPEN");
        VBox vBox = new VBox();

        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Text loginText = new Text();
        vBox.getChildren().addAll(
                new Label("Username"),
                usernameField,
                new Label("Password"),
                passwordField,
                loginButton,
                loginText);
        root.getChildren().addAll(vBox);
/*
        button.setOnAction(actionEvent-> {
            if(stage!=null){
                stage.requestFocus();
                return;
            }


            stage = new Stage();
            StackPane stackPane = new StackPane();
            Scene loginScene = new Scene(stackPane, 200, 200);
            stage.setScene(loginScene);
            stage.show();
        });
        */


        loginButton.setOnAction(actionEvent -> {

            loginText.setText("Connecting...");
            //System.out.println(usernameField.getText() + passwordField.getText());
            connection = dao.getDatabaseConnection(usernameField.getText(), passwordField.getText());
            if (connection != null) {
                loginText.setText("Login successful");
                try {

                    statement = connection.createStatement();
                    dao.initializeDatabase(statement);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.hide();
                showMenuScreen();

            } else {
                loginText.setText("Invalid login details - please try again");
            }

        });

    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Database Login");
        primaryStage.setAlwaysOnTop(true);


    }

    public static void showMenuScreen() {
        Stage menuStage = new Stage();
        StackPane menuStackPane = new StackPane();
        Label title = new Label("Select an option below");
        Button browseCustomersButton = new Button("Browse Customers");
        Button searchCustomersButton = new Button("Search Customers");
        Button addCustomerButton = new Button("Add Customer");
        Button editCustomerButton = new Button("Edit Customer");
        Button removeCustomerButton = new Button("Remove Customer");
        Button addPolicyButton = new Button("Add Policy");
        Button editPolicyButton = new Button("Edit Policy");
        Button removePolicyButton = new Button("Remove Policy");
        VBox container = new VBox(title, searchCustomersButton, browseCustomersButton, addCustomerButton, editCustomerButton, removeCustomerButton
                , addPolicyButton, editPolicyButton, removePolicyButton);
        container.setSpacing(8);
        container.setPadding(new Insets(10, 10, 10, 10));
        menuStackPane.getChildren().addAll(container);
        Scene menuScene = new Scene(menuStackPane, 600, 600);
        menuStage.setScene(menuScene);
        menuStage.setTitle("Menu");

        menuStage.show();

        Statement s = null;
        try {
            s = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //    dao.initializeDatabase(s);

        browseCustomersButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) browseCustomersButton.getScene().getWindow();
            stage.hide();
            browseCustomersScreen();
        });
        searchCustomersButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) searchCustomersButton.getScene().getWindow();
            stage.hide();
            searchCustomersScreen();
        });
        addCustomerButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) addCustomerButton.getScene().getWindow();
            stage.hide();
            addCustomerScreen();

        });

        addPolicyButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) addPolicyButton.getScene().getWindow();
            stage.hide();
            addPolicyScreen();
        });


    }

    public static void searchCustomersScreen() {
        Stage searchCustomersStage = new Stage();
        StackPane searchCustomersStackPane = new StackPane();
        Label message = new Label("Enter customer information below: ");
        Label firstName = new Label("First name: ");
        Label lastName = new Label("Last name: ");
        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");

        Label isCustomer = new Label("");
        Label carPolicies = new Label("");
        Label homePolicies = new Label("");
        Label lifePolicies = new Label("");

        VBox container = new VBox(message, firstName, firstNameTextField, lastName, lastNameTextField, searchButton,
                isCustomer, carPolicies, homePolicies, lifePolicies, backButton);
        container.setSpacing(8);
        container.setPadding(new Insets(10, 10, 10, 10));

        searchCustomersStackPane.getChildren().addAll(container);
        Scene searchCustomersScene = new Scene(searchCustomersStackPane, 600, 600);
        searchCustomersStage.setScene(searchCustomersScene);
        searchCustomersStage.setTitle("Add Customer");
        searchCustomersStage.show();

        searchButton.setOnAction(actionEvent ->
        {
            if (dao.customerExists(statement, firstNameTextField.getText(), lastNameTextField.getText())) {
                isCustomer.setText(firstNameTextField.getText() + " " + lastNameTextField.getText() + " is a customer");
                carPolicies.setText(firstNameTextField.getText() + " " + lastNameTextField.getText() + " has x car insurance policies");
                homePolicies.setText(firstNameTextField.getText() + " " + lastNameTextField.getText() + " has x home insurance policies");
                lifePolicies.setText(firstNameTextField.getText() + " " + lastNameTextField.getText() + " has x life insurance policies");
            } else {
                isCustomer.setText(firstNameTextField.getText() + " " + lastNameTextField.getText() + " is not a customer");
                carPolicies.setText("");
                homePolicies.setText("");
                lifePolicies.setText("");
            }
        });

        backButton.setOnAction(actionEvent ->
        {
            backButtonActionEvent(backButton);
        });
    }

    public static void addCustomerScreen() {
        Stage addCustomerStage = new Stage();
        StackPane addCustomerStackPane = new StackPane();
        Label message = new Label("Enter customer information below: ");
        Label firstName = new Label("First name: ");
        Label lastName = new Label("Last name: ");
        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        Button addButton = new Button("Add");

        VBox container = new VBox(message, firstName, firstNameTextField, lastName, lastNameTextField, addButton);
        container.setSpacing(8);
        container.setPadding(new Insets(10, 10, 10, 10));

        addCustomerStackPane.getChildren().addAll(container);
        Scene addCustomerScene = new Scene(addCustomerStackPane, 600, 600);
        addCustomerStage.setScene(addCustomerScene);
        addCustomerStage.setTitle("Add Customer");
        addCustomerStage.show();

        addButton.setOnAction(actionEvent ->
        {
            dao.addCustomer(statement, firstNameTextField.getText(), lastNameTextField.getText());
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.hide();
            showMenuScreen();
        });
    }

    static String selectedCustomerPast = " "; // need name of the last customer selected to open customer view

    // when same name is clicked twice
    public static void browseCustomersScreen() {
        Stage browseCustomersStage = new Stage();
        StackPane browseCustomersStackPane = new StackPane();

        Button backButton = new Button("Back");
        Button selectButton = new Button("Select");

        String[] customers = dao.getCustomers(statement);

        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button editButton = new Button("Edit");
        Button removeButton = new Button("Remove");
        Button addButton = new Button("Add");

        ListView<String> customerList = new ListView<>();
        for (int i = 0; i < customers.length; i++) {
            customerList.getItems().add(customers[i]);
        }
        VBox box = new VBox(searchField, searchButton, customerList, backButton, selectButton, editButton,
                removeButton, addButton);
        box.setSpacing(8);
        box.setPadding(defaultInsets);
        browseCustomersStackPane.getChildren().addAll(box);
        Scene browseCustomersScene = new Scene(browseCustomersStackPane, 600, 600);
        browseCustomersStage.setScene(browseCustomersScene);
        browseCustomersStage.setTitle("Browse Customers");
        browseCustomersStage.show();

        backButton.setOnAction(actionEvent ->
        {
            backButtonActionEvent(backButton);
        });

        selectButton.setOnAction(actionEvent ->
        {
            System.out.println(customerList.getSelectionModel().getSelectedItem());


            editCustomerScreen("", "");
        });

        searchButton.setOnAction(actionEvent ->
        {
            String[] name = searchField.getText().split(" ");
            String firstName = name[0];
            String lastName = name[1];

            box.getChildren().remove(2);
        });

        customerList.setOnMouseClicked(e ->
        {
            String selectedCustomerCurrent = customerList.getSelectionModel().getSelectedItem().toString();
            if (selectedCustomerCurrent.equals(selectedCustomerPast)) {
                Stage stage = (Stage) customerList.getScene().getWindow();
                stage.hide();
                String[] customerNames = selectedCustomerCurrent.split(" ");
                editCustomerScreen(customerNames[0], customerNames[1]);
            } else {
                selectedCustomerPast = selectedCustomerCurrent;
            }

            System.out.println(customerList.getSelectionModel().getSelectedItem().toString());
        });
    }

    public static void editCustomerScreen(String firstName, String lastName) {
        System.out.println("edit customer screen");
        Stage editCustomerStage = new Stage();
        StackPane editCustomerStackPane = new StackPane();

        Label customerLabel = new Label(firstName + " " + lastName);

        Button backButton = new Button("Back");
        Button removeButton = new Button("Remove");
        Button saveButton = new Button("Save");

        String[] policies = dao.getPolicies(statement);

        ListView<String> policyView = new ListView<>();

        for (int i = 0; i < policies.length; i++) {
            policyView.getItems().add(policies[i]);
        }

        VBox box = new VBox(customerLabel, policyView, backButton, removeButton, saveButton);
        box.setSpacing(8);
        box.setPadding(defaultInsets);
        editCustomerStackPane.getChildren().addAll(box);
        Scene editCustomerScene = new Scene(editCustomerStackPane, 600, 600);
        editCustomerStage.setScene(editCustomerScene);
        editCustomerStage.setTitle("Edit Customer");
        editCustomerStage.show();
    }

    public static void addPolicyScreen() {
        Stage addPolicyStage = new Stage();
        StackPane addPolicyStackPane = new StackPane();

        Label choosePolicy = new Label("Select a type of policy to create");
        Button carPolicyButton = new Button("Car Insurance");
        Button homePolicyButton = new Button("Home Insurance");
        Button lifePolicyButton = new Button("Life Insurance");
        Button backButton = new Button("Back");

        VBox box = new VBox(choosePolicy, carPolicyButton, homePolicyButton, lifePolicyButton, backButton);
        box.setSpacing(8);
        box.setPadding(defaultInsets);

        addPolicyStackPane.getChildren().addAll(box);


        Scene addPolicyScene = new Scene(addPolicyStackPane, 600, 600);

        addPolicyStage.setScene(addPolicyScene);
        addPolicyStage.setTitle("Select a type of policy");
        addPolicyStage.show();

        carPolicyButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) carPolicyButton.getScene().getWindow();
            stage.hide();
            addCarPolicy();
        });
        homePolicyButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) homePolicyButton.getScene().getWindow();
            stage.hide();
            addHomePolicy();
        });
        lifePolicyButton.setOnAction(actionEvent ->
        {
            Stage stage = (Stage) lifePolicyButton.getScene().getWindow();
            stage.hide();
            addLifePolicy();
        });
        backButton.setOnAction(actionEvent ->
        {
            backButtonActionEvent(backButton);
        });
    }

    public static void addCarPolicy() {
        Stage addCarPolicyStage = new Stage();
        StackPane addCarPolicyStackPane = new StackPane();

        TextField makeTextField = new TextField("Make:");
        TextField modelTextField = new TextField("Model:");
        TextField yearTextField = new TextField("Year:");
        TextField VINTextField = new TextField("VIN:");
        TextField mileageTextField = new TextField("Mileage  per year:");
        TextField coverageTextField = new TextField("Coverage:");

        VBox box = new VBox();
        box.setSpacing(8);
        box.setPadding(defaultInsets);

        addCarPolicyStackPane.getChildren().addAll(box);
        Scene addCarPolicyScene = new Scene(addCarPolicyStackPane);

        addCarPolicyStage.setScene(addCarPolicyScene);
        addCarPolicyStage.setTitle("Add a car insurance policy");
        addCarPolicyStage.show();
    }

    public static void addHomePolicy() {
        Stage addHomePolicyStage = new Stage();
        StackPane addHomePolicyStackPane = new StackPane();

        VBox box = new VBox();
        box.setSpacing(8);
        box.setPadding(defaultInsets);
        addHomePolicyStackPane.getChildren().addAll(box);
        Scene addHomePolicyScene = new Scene(addHomePolicyStackPane);

        addHomePolicyStage.setScene(addHomePolicyScene);
        addHomePolicyStage.setTitle("Add a home insurance policy");
        addHomePolicyStage.show();

    }

    public static void addLifePolicy() {
        Stage addLifePolicyStage = new Stage();
        StackPane addLifePolicyStackPane = new StackPane();

        VBox box = new VBox();
        box.setSpacing(8);
        box.setPadding(defaultInsets);
        addLifePolicyStackPane.getChildren().addAll(box);
        Scene addLifePolicyScene = new Scene(addLifePolicyStackPane);

        addLifePolicyStage.setScene(addLifePolicyScene);
        addLifePolicyStage.setTitle("Add a life insurance policy");
        addLifePolicyStage.show();

    }


    public static void backButtonActionEvent(Button backButton) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.hide();
        showMenuScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }


}