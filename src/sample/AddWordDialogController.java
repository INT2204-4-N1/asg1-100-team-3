package sample;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddWordDialogController implements Initializable {
    @FXML private TextField wordTextField = new TextField();
    @FXML private HTMLEditor definationTextArea = new HTMLEditor();
    @FXML private Button buttonAdd = new Button();
    private Controller controller = new Controller();
    @Override
    public void initialize(URL url , ResourceBundle rb){
        controller = Main.getLoader().getController();
    }
    public void pressButton(ActionEvent event){
        if(event.getSource()==buttonAdd){
            String Word = wordTextField.getText();
            String Defination = definationTextArea.getHtmlText();
            if(Word.equals("") || Defination.indexOf("font face")==-1)  controller.createAlertBoxes();
            else{
            controller.addNewWord(Word,Defination);
            Stage stage = (Stage)buttonAdd.getScene().getWindow();
            stage.close();
            }
        }
    }
}
