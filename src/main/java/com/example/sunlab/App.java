package com.example.sunlab;

import com.example.sunlab.DatabaseAccessObject;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class App extends Application  {
    private StackPane root = new StackPane();
    private Stage stage;

    static DatabaseAccessObject dao;
    static Connection connection;
    static Statement statement;

    static Insets defaultInsets = new Insets(10, 10, 10, 10);

    // login credentials for database
    static String username = "system";
    static String password = "oracle";

    static Scanner sc = new Scanner(System.in);

    String cardInfo, studentID;
    @Override
    public void init() {

        // initialize connection object
        connection = dao.getDatabaseConnection(username, password);
        // attempt to log into database (running locally on virtual machine)
        try
        {
            statement = connection.createStatement();
            dao.initializeDatabase(statement);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        VBox vBox = new VBox();

        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10,10,10,10));
        Button browseButton = new Button("Browse");
        Button submitCardButton = new Button("Swipe card");
        Text waitingText = new Text();
        Text successText = new Text();
        TextField cardField = new TextField();


        waitingText.setText("Waiting for card...");
        successText.setText("");

        vBox.getChildren().addAll(
                waitingText,
                successText,
                cardField,
                submitCardButton,
                browseButton);
        root.getChildren().addAll(vBox);

        browseButton.setOnAction(actionEvent -> {

            Stage stage = (Stage) browseButton.getScene().getWindow();
            stage.hide();
            showBrowseScreen(0);
        });

        submitCardButton.setOnAction(actionEvent -> {
            cardInfo = cardField.getText();
            try {

                if (cardInfo.substring(0, 2).equals("%A") & cardInfo.length() == 11) {
                    studentID = cardInfo.substring(2, 11);

                    successText.setText("Student with ID: " + studentID + " allowed access!");

                    // add login to database
                    dao.addLogin(statement, studentID, Timestamp.valueOf(LocalDateTime.now()));

                }
                else {
                    successText.setText("Invalid access attempt");
                }

            }
            catch (Exception e) {

                successText.setText("Invalid access attempt");
            }
        });

    }

    public void showCardReaderScreen() {

        Stage showCardReaderStage = new Stage();
        StackPane showCardReaderStackPane = new StackPane();


        VBox vBox = new VBox();

        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10,10,10,10));
        Button browseButton = new Button("Browse");
        Button submitCardButton = new Button("Swipe card");
        Text waitingText = new Text();
        Text successText = new Text();
        TextField cardField = new TextField();


        waitingText.setText("Waiting for card...");
        successText.setText("");

        vBox.getChildren().addAll(
                waitingText,
                successText,
                cardField,
                submitCardButton,
                browseButton);
        showCardReaderStackPane.getChildren().addAll(vBox);
        Scene showCardReaderScene = new Scene(showCardReaderStackPane, 600, 600);
        showCardReaderStage.setScene(showCardReaderScene);
        showCardReaderStage.setTitle("Card Reader");
        showCardReaderStage.show();

        browseButton.setOnAction(actionEvent -> {

            Stage stage = (Stage) browseButton.getScene().getWindow();
            stage.hide();
            showBrowseScreen(0);
        });

        submitCardButton.setOnAction(actionEvent -> {
            cardInfo = cardField.getText();
            try {

                if (cardInfo.substring(0, 2).equals("%A") & cardInfo.length() == 11) {
                    studentID = cardInfo.substring(2, 11);

                    successText.setText("Student with ID: " + studentID + " allowed access!");

                    // add login to database
                    dao.addLogin(statement, studentID, Timestamp.valueOf(LocalDateTime.now()));

                }
                else {
                    successText.setText("Invalid access attempt");
                }

            }
            catch (Exception e) {

                successText.setText("Invalid access attempt");
            }
        });
    }

    public void showBrowseScreen(int sortMode) {

        Stage browseStage = new Stage();
        StackPane browseStackPane = new StackPane();

        Button backButton = new Button("Back");
        Button sortByIDButton = new Button("Sort by ID");
        Button sortByDateButton = new Button("Sort by date");

        String[] logins = null;
        if (sortMode == 0) {
            logins = dao.getLoginsByTime(statement);
        }
        else if (sortMode == 1) {
            logins = dao.getLoginsByID(statement);
        }

        ListView<String> loginList = new ListView<>();

        for (int i = 0; i < logins.length; i++) {

            loginList.getItems().add(logins[i]);
        }

        VBox box = new VBox(loginList, sortByIDButton, sortByDateButton, backButton);

        box.setSpacing(8);

        box.setPadding(defaultInsets);

        browseStackPane.getChildren().addAll(box);
        Scene browseScene = new Scene(browseStackPane, 600, 600);
        browseStage.setScene(browseScene);
        browseStage.setTitle("Browse");
        browseStage.show();

        sortByIDButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) sortByIDButton.getScene().getWindow();
            stage.hide();
            showBrowseScreen(1);
        });

        sortByDateButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) sortByDateButton.getScene().getWindow();
            stage.hide();
            showBrowseScreen(0);
        });

        backButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.hide();
            showCardReaderScreen();
        });

    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Card Reader");
        primaryStage.setAlwaysOnTop(true);
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}