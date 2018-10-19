package sample;

import Dictionary.Dictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class deleteWordDialogController implements Initializable {
    Controller mainController = new Controller();
    private boolean checkSearchWord = false;
    @FXML private TextField deleteTextField;
    @FXML private HTMLEditor deleteHtml;
    @FXML private Button searchButton;
    @FXML private Button deleteButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            mainController= Main.getLoader().getController();
            if(mainController.getSelectingWordOnListView()!=null){
                deleteTextField.setText(mainController.getSelectingWordOnListView());
                deleteTextField.setEditable(false);
                searchButton.setDisable(true);
                deleteHtml.setHtmlText(mainController.getDictionary().getWordDefination(mainController.getSelectingWordOnListView()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void pressButtonInDeleteDialog(ActionEvent event){
        if(event.getSource()==searchButton){
            String Word = deleteTextField.getText();
            if(mainController.checkWord(Word)==false)   mainController.createAlertBoxes();
            else{
                Word = Word.toLowerCase();
                String Defination = mainController.getDictionary().getWordDefination(Word);
                deleteHtml.setHtmlText(Defination);
                checkSearchWord = true;
            }
        }
        else if(event.getSource()==deleteButton){
            if(checkSearchWord || !deleteTextField.getText().equals("")){
                String Word = deleteTextField.getText();
                mainController.deleteWord(Word);
                Stage stage = (Stage)deleteButton.getScene().getWindow();
                stage.close();
            }
            else mainController.createAlertBoxes();
        }
    }
}