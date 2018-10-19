package sample;

import GoogleAPI.Audio;
import GoogleAPI.GoogleTranslate;
import GoogleAPI.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleTranslateController implements Initializable {
    private HashMap<String,String> map = new HashMap<>();
    Language language = new Language();
    @FXML private Button closeButton = new Button();
    @FXML private ComboBox<String> srcLang = new ComboBox<String>();
    @FXML private ComboBox<String> desLang = new ComboBox<String>();
    @FXML private Button tranButton = new Button();
    @FXML private TextArea inputText = new TextArea();
    @FXML private TextArea outputText = new TextArea();
    @FXML private Button speakButton = new Button();
    @FXML private Controller controller = new Controller();
    public void initialize(URL url , ResourceBundle rb){
            map.put("Vietnamese","vi");
            map.put("English","en");
            map.put("Korean","ko");
            map.put("Japanese","ja");
            map.put("Chinese","zh");
            map.put("Thailand","th");
            ObservableList<String> languages = FXCollections.observableArrayList("Vietnamese","English","Korean","Chinese","Thailand","Japanese");
            srcLang.setItems(languages);
            desLang.setItems(languages);
            desLang.setValue("Vietnamese");
            srcLang.setValue("English");
        }
        public void createDictionaryLayout(ActionEvent event) throws Exception{
            if(event.getSource()==closeButton){
            try{
                Parent mainLayout = Main.getLoader().load(getClass().getResource("sample.fxml"));
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
        public void pressButton(ActionEvent e){
            if(e.getSource()==tranButton){
               translateText();
            }
            else if(e.getSource()==speakButton){
                try{
                    speak();
                }
                catch (Exception ev){
                    ev.printStackTrace();
                }
            }
        }
        public void pressKeyBoard(){
            inputText.setOnKeyPressed(event -> {
                if(event.getCode()== KeyCode.ENTER){
                    translateText();
                }
            });
        }
        public void translateText(){
            String src = srcLang.getValue();
            String des = desLang.getValue();
            String text = inputText.getText();
            try{
                if(IsConnected.IsConnecting()){
                    outputText.setText(GoogleTranslate.translate(map.get(src),map.get(des),text));
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error ");
                    alert.setHeaderText(null);
                    alert.setContentText("No internet connection!");
                    alert.showAndWait();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        public void speak()throws IOException, InterruptedException{
            if (IsConnected.IsConnecting() && !outputText.getText().equals("") ) {
                try {
                    InputStream sound = null;
                    Audio audio = Audio.getInstance();
                    sound = audio.getAudio(outputText.getText(), Language.VIETNAMESE);
                    audio.play(sound);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
}
