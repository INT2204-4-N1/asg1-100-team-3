package sample;
import Dictionary.Dictionary;
import GoogleAPI.Audio;
import GoogleAPI.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {
    ObservableList<String> items = FXCollections.observableArrayList();
    FilteredList<String> filteredData = new FilteredList<>(items, e -> true);
    private static Dictionary myDictionary = new Dictionary();
    @FXML private Button buttonDic = new Button();
    @FXML private AnchorPane myAnchorPanel = new AnchorPane();
    @FXML private ImageView imageDic = new ImageView();
    @FXML private HBox myHBox = new HBox();
    @FXML private VBox myVBox = new VBox();
    @FXML private ListView<String> myListView1 = new ListView<>();
    @FXML private WebView displayDefination = new WebView();
    @FXML private Button button1 = new Button();
    @FXML private TextField searchTextField = new TextField();
    @FXML private Button buttonDelete = new Button();
    @FXML private Button buttonGoogle = new Button();
    @FXML private Button buttonWiki = new Button();
    @FXML private Button buttonAdd = new Button();
    @FXML private Button buttonSpeaker = new Button();
    @FXML private Button historyButton= new Button();
    @FXML private Button editButton = new Button();
    @FXML private ListView<String> historyListView = new ListView<String>();
    public void pressButton(ActionEvent e){
        if(e.getSource()==button1){
            searchWord();
        }
        else if(e.getSource()==buttonAdd){
            loadAddWordDialog(e);
        }
        else if(e.getSource()==historyButton){
            historySearchWord();
        }
        else if(e.getSource()==buttonDelete){
            loadDeleteWordDialog();
        }
        else if(e.getSource()==editButton){
            loadUpdateWordDialog();
        }
        else if(e.getSource()==buttonSpeaker){
            try{
                speak();
            }
            catch (Exception ev){
                ev.printStackTrace();
            }
        }
        else if(e.getSource()==buttonGoogle){
            createGoogleTranslateLayout();
        }
        else if(e.getSource()==historyButton){
            historySearchWord();
        }
    }
    public void pressKeybroad(){
        searchTextField.setOnKeyPressed(event -> {
            if(event.getCode()== KeyCode.ENTER){
                searchWord();
            }
        });
    }
    public void pressKeyBroadListView(){
       myListView1.setOnKeyPressed(event -> {
           try{
           if(event.getCode()==KeyCode.ENTER){
               displayDefination.getEngine().reload();
               String Word = myListView1.getSelectionModel().getSelectedItem();
               String Defination = myDictionary.getWordDefination(Word);
               displayDefination.getEngine().loadContent(Defination);
           }
           }
           catch (Exception e){
               e.printStackTrace();
           }
       });
   }
    public void initialize(URL url , ResourceBundle rb){
        myDictionary.readDataFormSQLFile();
        getSelectingWordOnListView();
        items.addAll(myDictionary.getListWordsFormDictionary());
        suggestionWord();
        pressKeybroad();
        pressKeyBroadListView();
        getSelectedWord();
    }
    public void getSelectedWord(){
        myListView1.setOnMouseClicked(event -> {
            try{
                displayDefination.getEngine().reload();
                String Word = myListView1.getSelectionModel().getSelectedItem();
                if(Word!=null) {
                    String Defination = myDictionary.getWordDefination(Word);
                    writeRecentWordTofile(Word);
                    displayDefination.getEngine().loadContent(Defination);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });
    }
    public String getSelectingWordOnListView(){
        return myListView1.getSelectionModel().getSelectedItem();
    }

    public Set<String> ReadRecentWordFormFile(){
        HashSet<String> Temp = new HashSet<String>();
        FileInputStream fileIn = null;
        try{
            fileIn = new FileInputStream("src/DataBase/RecentWord.txt");
            BufferedReader Read = new BufferedReader(new InputStreamReader(fileIn,"utf-8"));
            String line;
            while((line=Read.readLine())!=null){
                Temp.add(line);
            }
            Read.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return Temp;
    }
    public void suggestionWord(){
         myListView1.setItems(filteredData);
         searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate(word -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (word.toLowerCase().startsWith(lowerCaseFilter)) { // contains
                    return true;
                }

                return false;
            });
            myListView1.setItems(filteredData);
        });
    }
    public void writeRecentWordTofile(String Word){
        try{
            File fileOut = new File("src/DataBase/RecentWord.txt");
            if(fileOut.exists()!=true){
                fileOut.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(fileOut.getAbsoluteFile(),true);
            BufferedWriter BufferWrite = new BufferedWriter(fileWriter);
            BufferWrite.write(Word);
            BufferWrite.write("\n");
            BufferWrite.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createGoogleTranslateLayout(){
        FXMLLoader loadAddWord = new FXMLLoader();
        loadAddWord.setLocation(getClass().getResource("GoogleTranslate.fxml"));
        try{
            loadAddWord.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Parent parent = loadAddWord.getRoot();
        parent.getStylesheets().add("Css/dic.css");
        Stage stage = new Stage();
        stage.resizableProperty().setValue(false);
        stage.setTitle("Google Dịch");
        stage.setScene(new Scene(parent));
        stage.show();
    }
    public void addNewWord(String Word, String Defination){
        myDictionary.addWord(Word,Defination);
        myDictionary.readDataFormSQLFile();
        refreshListView();
    }
    public void deleteWord(String Word){
        myDictionary.deleteWord(Word);
        myDictionary.readDataFormSQLFile();
        displayDefination.getEngine().loadContent("");
        refreshListView();
    }
    public void editWord(String oldWord,String newWord,String newDefination){
        if(newWord.equals("") || oldWord.equals(newWord)){
            if(newDefination.equals(getDictionary().getWordDefination(oldWord)))    return;
            else{
                deleteWord(oldWord);
                addNewWord(oldWord,newDefination);
            }
        }
        else{
            deleteWord(oldWord);
            addNewWord(newWord,newDefination);
        }
    }
    public void createWikipediaLayout(){

    }
    public void loadAddWordDialog(ActionEvent event){

        FXMLLoader loadAddWord = new FXMLLoader();
        loadAddWord.setLocation(getClass().getResource("addWordDialog.fxml"));
        try{
            loadAddWord.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Parent parent = loadAddWord.getRoot();
        Stage stage = new Stage();
        stage.setTitle("THÊM TỪ MỚI");
        stage.setScene(new Scene(parent));
        stage.show();
    }
    public void searchWord(){
        String Word = searchTextField.getText();
        if(checkWord(Word)==false)  createAlertBoxes();
        else{
        Word = Word.toLowerCase();
        String Defination = myDictionary.getWordDefination(Word);
        if(Defination==null)    return;
        String Temp = Defination;
        displayDefination.getEngine().loadContent(Temp);
        writeRecentWordTofile(Word);
        }
    }
    public void createAlertBoxes(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Lỗi Nhập Từ");
        window.setWidth(400);
        window.setHeight(250);
        Label Temp = new Label();
        Temp.setText("Từ bạn nhập không hợp lệ hoặc không tồn tại");
        Temp.setStyle("-fx-font-size:16;-fx-font-weight:bold");
        Button closeButton = new Button("Đóng");
        closeButton.setOnAction(event -> window.close());
        VBox layout = new VBox();
        layout.setOnKeyPressed(event -> {
            if(event.getCode()==KeyCode.ENTER){
                window.close();
            }
        });
        layout.getChildren().addAll(Temp,closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(layout);
        window.setScene(newScene);
        window.show();
    }
    public boolean checkWord(String Word){
        if(Word.isEmpty() || myDictionary.getWordDefination(Word)== null){
            return false;
        }
        return true;
    }
    public void speak () throws IOException, InterruptedException {
        if (IsConnected.IsConnecting() && getSelectingWordOnListView()!=null ) {
            try {
                InputStream sound = null;
                Audio audio = Audio.getInstance();
                sound = audio.getAudio(getSelectingWordOnListView(), Language.ENGLISH);
                audio.play(sound);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(IsConnected.IsConnecting() && !searchTextField.getText().equals("")){
            try {
                InputStream sound = null;
                Audio audio = Audio.getInstance();
                sound = audio.getAudio(searchTextField.getText(), Language.ENGLISH);
                audio.play(sound);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error ");
            alert.setHeaderText(null);
            alert.setContentText("No internet connection!");
            alert.showAndWait();
        }
    }
    public void historySearchWord(){
        FXMLLoader loadHistoryWord = new FXMLLoader();
        loadHistoryWord.setLocation(getClass().getResource("historyWordDialog.fxml"));
        try{
            loadHistoryWord.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Parent parent = loadHistoryWord.getRoot();
        Stage stage = new Stage();
        stage.resizableProperty().setValue(false);
        stage.setTitle("Lịch Sử Tìm Kiếm");
        stage.setScene(new Scene(parent));
        stage.show();
    }
    public void loadDeleteWordDialog(){
        FXMLLoader loadAddWord = new FXMLLoader();
        loadAddWord.setLocation(getClass().getResource("deleteWordDialog.fxml"));
        try{
            loadAddWord.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Parent parent = loadAddWord.getRoot();
        Stage stage = new Stage();
        stage.resizableProperty().setValue(false);
        stage.setTitle("XÓA TỪ");
        stage.setScene(new Scene(parent));
        stage.show();
    }
    public void loadUpdateWordDialog(){
        FXMLLoader loadUpdateWord = new FXMLLoader();
        loadUpdateWord.setLocation(getClass().getResource("updateWordDialog.fxml"));
        try{
            loadUpdateWord.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Parent parent = loadUpdateWord.getRoot();
        Stage stage = new Stage();
        stage.resizableProperty().setValue(false);
        stage.setTitle("Chỉnh Sửa");
        stage.setScene(new Scene(parent));
        stage.show();
    }
    public void refreshListView(){
        ObservableList<String> Temp = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(Temp,e->true);
        Temp.addAll(myDictionary.getListWordsFormDictionary());
        myListView1.setItems(Temp);
    }

    public Dictionary getDictionary(){
        return myDictionary;
    }

    public ObservableList<String> getItems() {
        return items;
    }

    public void setItems(ObservableList<String> items) {
        this.items = items;
    }

    public FilteredList<String> getFilteredData() {
        return filteredData;
    }

    public void setFilteredData(FilteredList<String> filteredData) {
        this.filteredData = filteredData;
    }

    public ListView<String> getMyListView1() {
        return myListView1;
    }

    public void setMyListView1(ListView<String> myListView1) {
        this.myListView1 = myListView1;
    }
}