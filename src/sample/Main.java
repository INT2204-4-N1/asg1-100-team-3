package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
        loader= mloader;
        Parent root = loader.getRoot();
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
