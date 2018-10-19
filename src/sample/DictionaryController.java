package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class DictionaryController implements Initializable {
    @FXML private AnchorPane dictionaryAnchorPane= new AnchorPane();
    @FXML private ListView<String> listWords= new ListView<>();
    @FXML private WebView displayWordDefination = new WebView();
    @FXML private Button combackMain;
    public void pressButton(ActionEvent e) {

    }
    public void initialize(URL url , ResourceBundle rb){

    }
    public void createDictionaryLayout(ActionEvent event) throws Exception{
        try{
            Parent mainLayout = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene mainView = new Scene(mainLayout);
            Stage Window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Window.setScene(mainView);
            Window.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
