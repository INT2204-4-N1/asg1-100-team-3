package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {

    private static FXMLLoader loader = new FXMLLoader();
    public static FXMLLoader getLoader(){
        return loader;
    }
    public static void setLoader(FXMLLoader temp){
        loader = temp;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader mloader= new FXMLLoader();
        mloader.setLocation(getClass().getResource("sample.fxml"));
        mloader.load();
        loader = mloader;
        Parent root = loader.getRoot();
        primaryStage.setTitle("Dictionary");
        Scene scene = new Scene(root,1000,700);
        scene.getStylesheets().add("css/dic.css");
        primaryStage.setScene(scene);
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
