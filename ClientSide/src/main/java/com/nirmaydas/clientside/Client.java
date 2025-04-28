package com.nirmaydas.clientside;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        loader.setController(new Controller());
        Scene loginScene = new Scene(loader.load(), 175,238);
        primaryStage.setTitle("Library Client - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}