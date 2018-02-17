package com.nikulitsa.notesapp.processing;

import com.nikulitsa.notesapp.MainClass;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataBaseSaverRunnable implements Runnable{

    private String noteText;
    private LocalDateTime noteTime;

    private Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private String dbName;
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    private PreparedStatement stmt;

    private MainClass MainApp;
    public void setMainApp(MainClass mainApp) {
        MainApp = mainApp;
    }

    public DataBaseSaverRunnable(){
        this.noteText=null;
        this.noteTime=null;
    }

    public DataBaseSaverRunnable(String noteText, LocalDateTime noteTime){
        this.noteText=noteText;
        this.noteTime=noteTime;
    }

    @Override
    public void run() {
        if (noteText==null || noteTime==null){
            return; // TODO: bad design
        }
            String query = "INSERT INTO paytravel."+dbName+" (noteText, noteDate) VALUES (?, ?)";
            try {
                stmt = connection.prepareStatement(query);
                stmt.setString(1, noteText);
                //TODO: date in database is string, that is really bad
                stmt.setString(2, noteTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")));
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                MainApp.getNotesData().clear(); // TODO: that is bad design
                Platform.runLater(() -> MainApp.showErr());
            } finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    MainApp.getNotesData().clear();
                    Platform.runLater(() -> MainApp.showErr());
                }
            }
    }
}
