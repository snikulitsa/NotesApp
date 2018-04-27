package com.nikulitsa.notesapp.fx;

import com.nikulitsa.notesapp.MainClass;
import com.nikulitsa.notesapp.datamodel.Note;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConnectToDatabaseWindowController {
    private MainClass MainApp;

    private static String urlToDatabase;
    private static String login;
    private static String password;

    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;

    public void setMainApp(MainClass MainApp){
        this.MainApp=MainApp;
    }

    @FXML
    private TextField databaseUrlField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    private Stage dialogStage;

    private  ObservableList<Note> notesData;
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    @FXML
    private void connectToDatabaseHandler(){
        this.notesData=MainApp.getNotesData();
        urlToDatabase="jdbc:mysql://"+databaseUrlField.getText();
        login=loginField.getText();
        password=passwordField.getText();

        String query = "select * from notes";
        if (MainApp.getConnectionStatus()==false) {
            try {
                connection = DriverManager.getConnection(urlToDatabase, login, password);
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
                while (rs.next()) {
                    String testNoteText = rs.getString(1);
                    String testNoteDate = rs.getString(2);
                    LocalDateTime dateTime = LocalDateTime.parse(testNoteDate, formatter);
                    notesData.add(new Note(testNoteText, dateTime));
                }

                MainApp.setConnectionStatus(true);
                MainApp.setConnection(connection);
            } catch (SQLException sqlEx) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Во время подключения к базе данных произошла ошибка:");
                alert.setContentText(sqlEx.getMessage());

                alert.showAndWait();
            } finally {
                try {
                    stmt.close();
                } catch (SQLException se) {
                }
                try {
                    rs.close();
                } catch (SQLException se) {
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Вы уже подключены к базе данных");
            alert.showAndWait();
        }

        dialogStage.close();
    }

    @FXML
    private void cancelHandler(){
        dialogStage.close();
    }

}
