package com.example.hrmanagementmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AnnounceMessageController implements Initializable {

    @FXML
    private TableView<Message> messageTable;  // Use <Message> instead of <Object>

    @FXML
    private TableColumn<Message, Integer> colMessageId;

    @FXML
    private TableColumn<Message, String> colMessageText;

    @FXML
    private TableColumn<Message, Timestamp> colCreatedAt;

    private ObservableList<Message> messageList = FXCollections.observableArrayList();

    private Connection connectDB() {
        // Update your DB details here
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMessages();
    }

    private void loadMessages() {
        Connection conn = connectDB();
        if (conn == null) {
            System.out.println("Database connection failed.");
            return;
        }

        String query = "SELECT message_id, message_text, created_at FROM messages";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            messageList.clear();

            while (rs.next()) {
                messageList.add(new Message(
                        rs.getInt("message_id"),
                        rs.getString("message_text"),
                        rs.getTimestamp("created_at")
                ));
            }

            colMessageId.setCellValueFactory(new PropertyValueFactory<>("messageId"));
            colMessageText.setCellValueFactory(new PropertyValueFactory<>("messageText"));
            colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            // Optional: wrap in SortedList if you want sorting
            SortedList<Message> sortedList = new SortedList<>(messageList);
            messageTable.setItems(sortedList);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
