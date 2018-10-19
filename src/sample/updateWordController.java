package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class updateWordController implements Initializable {
    @FXML private TextField oldWord = new TextField();
    @FXML private TextField newWord = new TextField();
    private boolean checkSearchWord = false;
    @FXML Button searchButton = new Button();
    @FXML Button editButton = new Button();
    @FXML HTMLEditor editDefination = new HTMLEditor();
    Controller controller = new Controller();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            controller = Main.getLoader().getController();
            if(controller.getSelectingWordOnListView()!=null){
                oldWord.setText(controller.getSelectingWordOnListView());
                searchButton.setDisable(true);
                oldWord.setEditable(false);
                checkSearchWord = true;
                editDefination.setHtmlText(controller.getDictionary().getWordDefination(controller.getSelectingWordOnListView()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void pressButton(ActionEvent e){
        if(e.getSource()==searchButton){
            String Word = oldWord.getText();
            Word = Word.toLowerCase();
            if(Word.equals("") || controller.getDictionary().getWordDefination(Word)==null){
                controller.createAlertBoxes();
                return;
            }
            else{
                String Defination = controller.getDictionary().getWordDefination(Word);
                editDefination.setHtmlText(Defination);
                checkSearchWord = true;
            }
        }
        else if(e.getSource()==editButton){
            if(checkSearchWord==false || editDefination.getHtmlText().indexOf("font face")==-1){
                controller.createAlertBoxes();
                return;
            }
            else{
                String Word1 = oldWord.getText();
                String Word2 = newWord.getText();
                String newDefination = editDefination.getHtmlText();
                controller.editWord(Word1,Word2,newDefination);
                Stage stage = (Stage)editButton.getScene().getWindow();
                stage.close();
            }
        }
    }
}
