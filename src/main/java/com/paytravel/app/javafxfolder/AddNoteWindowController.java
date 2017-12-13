package com.paytravel.app.javafxfolder;

import com.paytravel.app.MainClass;
import com.paytravel.app.datamodel.Note;
import com.paytravel.app.processing.DataBaseSaverRunnable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNoteWindowController {

    public void setMainApp(MainClass mainApp) {
        MainApp = mainApp;
    }

    private MainClass MainApp;

    @FXML
    private Label dateTimeTextField;
    @FXML
    private TextField noteTextField;

    private Stage addNoteStage;

    public void setAddNoteStage(Stage addNoteStage) {
        this.addNoteStage = addNoteStage;
    }

    @FXML
    private void initialize(){
        dateTimeTextField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")));
    }

    @FXML
    private void addHandler(){
        if (MainApp.getConnectionStatus()) {
            dateTimeTextField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")));
            ObservableList<Note> notesData = MainApp.getNotesData();


            String noteText = noteTextField.getText();
            LocalDateTime noteTime = LocalDateTime.now();

            if (noteText!=null && !noteText.equals("")) {
                notesData.add(new Note(noteText, noteTime));

                DataBaseSaverRunnable assyncTaskSaveToDataBase = new DataBaseSaverRunnable(noteText, noteTime);
                assyncTaskSaveToDataBase.setMainApp(MainApp); // TODO: that is bad design
                assyncTaskSaveToDataBase.setConnection(MainApp.getConnection());
                assyncTaskSaveToDataBase.setDbName(MainApp.getDbName());
                new Thread(assyncTaskSaveToDataBase).start();
                addNoteStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Текст заметки не может быть пустым");

                alert.showAndWait();
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Нет подключения к базе данных");

            alert.showAndWait();
        }
    }

    @FXML
    private void cancelHandler(){
        addNoteStage.close();
    }


}
