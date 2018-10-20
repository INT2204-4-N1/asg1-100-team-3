package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class wikipediaController implements Initializable {
    @FXML private TextField wikiTextfield = new TextField();
    @FXML private AnchorPane wikiAnchorPanel = new AnchorPane();
    @FXML private Button wikiSearch = new Button();
    @FXML private Button closeButton = new Button();
    @FXML private WebView wikiWeb = new WebView();
    Controller controller = Main.getLoader().getController();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void pressButton(ActionEvent e){
        if(e.getSource()==closeButton){
            Stage stage = (Stage)closeButton.getScene().getWindow();
            stage.close();
        }
        else if(e.getSource()==wikiSearch){
            wikiWordSearch(wikiTextfield.getText());
        }
    }
    public void wikiWordSearch(String Word){
        try {
            if (IsConnected.IsConnecting() == false) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error ");
                alert.setHeaderText(null);
                alert.setContentText("No internet connection!");
                alert.showAndWait();
            }
            else if(Word.equals("")){
                controller.createAlertBoxes();
            }
            else {
                String Url = "https://en.wiktionary.org/wiki/"+Word;
                wikiWeb.getEngine().load(Url);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
