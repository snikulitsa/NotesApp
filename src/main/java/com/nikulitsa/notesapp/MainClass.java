package com.nikulitsa.notesapp;

import com.nikulitsa.notesapp.datamodel.Note;
import com.nikulitsa.notesapp.javafxfolder.AddNoteWindowController;
import com.nikulitsa.notesapp.javafxfolder.ConnectToDatabaseWindowController;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class MainClass extends Application{

    private Stage rootStage;

    private ObservableList<Note> notesData = FXCollections.observableArrayList();
    public ObservableList<Note> getNotesData() {
        return notesData;
    }

    private boolean connectionStatus = false;
    public boolean getConnectionStatus(){
        return this.connectionStatus;
    }
    public void setConnectionStatus(boolean connectionStatus){
        this.connectionStatus=connectionStatus;
    }

    public String getDbName() {
        return "notes"; //TODO: that is very very bad :)
    }

    private Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public Connection getConnection() {
        return connection;
    }



    @FXML
    private TableView<Note> notesTable;
    @FXML
    private TableColumn<Note, String> noteText;
    @FXML
    private TableColumn<Note, String> noteDate;

    @FXML
    private void initialize(){
        noteText.setCellValueFactory(text -> text.getValue().noteTextProperty());
        noteDate.setCellValueFactory(date -> new ReadOnlyStringWrapper(date.getValue().getNoteDate()
                  .format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss"))));

        notesTable.setItems(notesData);
    }

    @Override
    public void start(Stage rootStage) {
        this.rootStage=rootStage;
        this.rootStage.setTitle("Notes with MySQL storage");

        initRootLayer();
    }

    private void initRootLayer(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("javafxfolder/rootLayer.fxml"));
            BorderPane rootLayer = loader.load();
            Scene scene = new Scene(rootLayer);
            rootStage.setScene(scene);

            rootStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err in initRootLayer");
        }
    }

    @FXML
    private void addNoteHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("javafxfolder/addNoteWindow.fxml"));
            AnchorPane addNoteAnchorPane = loader.load();

            Stage addNoteStage = new Stage();
            addNoteStage.setResizable(false);
            addNoteStage.setTitle("Новая заметка");
            addNoteStage.initModality(Modality.WINDOW_MODAL);
            addNoteStage.initOwner(rootStage);
            Scene addNoteScene = new Scene(addNoteAnchorPane);
            addNoteStage.setScene(addNoteScene);

            AddNoteWindowController addNoteWindowController = loader.getController();
            addNoteWindowController.setAddNoteStage(addNoteStage);
            addNoteWindowController.setMainApp(this);

            addNoteStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadNotesFromDatabaseHandler(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("javafxfolder/connectToDatabaseWindow.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle("Edit person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            ConnectToDatabaseWindowController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitHandler(){
        System.exit(0);
    }

    @FXML
    private void aboutHandler(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О приложении");
        alert.setHeaderText("Автор: Сергей Анатольевич Никулица");
        alert.setContentText("Приложение - Блокнот для заметок.\n" +
                "Заметки хранятся в базе данных MySQL (тестировалось с версией 5.7).\n" +
                "БД: paytravel, таблица (захардкожена): notes. Поля таблицы строковые: noteText, noteDate.\n" +
                "Если на момент добавления заметки БД будет недоступна," +
                " приложение выдаст критическую ошибку и потребуется перезапуск.");

        alert.showAndWait();
    }

    public void showErr(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Критическая ошибка");
        alert.setHeaderText("Во время сохранения данных в БД возникла критическая ошибка.");
        alert.setContentText("Необходимо проверить доступность Базы Данных и перезапустить приложение.");

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
