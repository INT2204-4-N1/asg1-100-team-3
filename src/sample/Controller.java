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
import javafx.scene.image.Image;
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

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {
    ObservableList<String> items = FXCollections.observableArrayList();
    FilteredList<String> filteredData = new FilteredList<>(items, e -> true);
    private static Dictionary myDictionary = new Dictionary();
    @FXML private ImageView imageSearch = new ImageView();
    @FXML private ImageView imageSpeaker = new ImageView();
    @FXML private ImageView imageGoogle = new ImageView();
    @FXML private ImageView imageDelete = new ImageView();
    @FXML private ImageView imageAdd = new ImageView();
    @FXML private ImageView imageHistory = new ImageView();
    @FXML private ImageView imageEdit = new ImageView();
    @FXML private ImageView imageDictionary = new ImageView();
    @FXML private AnchorPane myAnchorPanel = new AnchorPane();
    @FXML private ImageView imageWiki = new ImageView();
    @FXML private ListView<String> myListView1 = new ListView<>();
    @FXML private Label myLabel1 = new Label();
    @FXML private WebView displayDefination = new WebView();
    @FXML private Button button1 = new Button();
    @FXML private TextField searchTextField = new TextField();
    @FXML private Button buttonDelete = new Button();
    @FXML private Button buttonGoogle = new Button();
    @FXML private Button buttonWiki = new Button();
    @FXML private Button buttonAdd = new Button();
    @FXML private Button buttonSpeaker = new Button();
    @FXML private Label myLabel2 = new Label();
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
        createLayoutMain();
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
    public void createLayoutMain(){
        myAnchorPanel.setStyle("-fx-background-color:#E0FFFF;-fx-font-family:monospace;-fx-font-size:16px;-fx-font-weight:bold");
        /**
         * Load Image Form File
         */
        Image ImageSearch = new Image("image/search1.png");
        Image ImageDictionary = new Image("image/DictionaryImage.png");
        Image Edit = new Image("image/pencil.png");
        Image ImageDelete = new Image("image/close-window.png");
        Image ImageAdd = new Image("image/add.png");
        Image googleTran = new Image("image/google_translate.png");
        Image ImageHistory = new Image("image/history1.png");
        Image Speaker = new Image("image/voice.png");
        Image Wiki = new Image("image/wiki2.png");
        /**
         * Create Button
         */
        button1.setGraphic(imageSearch);
        buttonDelete.setGraphic(imageDelete);
        buttonAdd.setGraphic(imageAdd);
        imageSearch.setImage(ImageSearch);
        imageGoogle.setImage(googleTran);
        imageDelete.setImage(ImageDelete);
        imageWiki.setImage(Wiki);
        imageAdd.setImage(ImageAdd);
        imageGoogle.setImage(googleTran);
        imageHistory.setImage(ImageHistory);
        imageSpeaker.setImage(Speaker);
        imageEdit.setImage(Edit);
        buttonGoogle.setGraphic(imageGoogle);
        historyButton.setGraphic(imageHistory);
        editButton.setGraphic(imageEdit);
        buttonSpeaker.setGraphic(imageSpeaker);
        buttonWiki.setGraphic(imageWiki);
        buttonWiki.setStyle("-fx-background-color:#00FFFF; -fx-box-shadow:40;");
        buttonGoogle.setStyle("-fx-background-color:#00FFFF; -fx-box-shadow:40;");

        /**
         * Initalize Default ListView
         */
        myListView1.setEditable(true);
        myListView1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        /**


        /**
         *
         */
        myLabel1.setStyle("-fx-background-color:#219ecc; -fx-font-family:monospace");
        myLabel2.setStyle("-fx-background-color:#219ecc; -fx-font-family:monospace");
        searchTextField.setStyle("-fx-border-color:#219ecc;-fx-border-size:15");
    }
    public void createDictionaryLayout(ActionEvent event) throws Exception{
        try{
            Parent dictionaryLayout = FXMLLoader.load(getClass().getResource("GoogleTranslate.fxml"));
            Scene dictionaryView = new Scene(dictionaryLayout);
            Stage Window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Window.setScene(dictionaryView);
            Window.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
    }
    public void historySearchWord(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Lịch Sử Tìm Kiếm");
        window.setWidth(700);
        window.setHeight(700);
        ListView<String> historySearch = new ListView<String>();
        historySearch.getItems().addAll(ReadRecentWordFormFile());
        Button closeButton = new Button("Đóng");
        closeButton.setOnAction(event -> window.close());
        WebView wordDefination = new WebView();
        historySearch.setOnMouseClicked(event -> {
            try{
                String Word = historySearch.getSelectionModel().getSelectedItem();
                String Defination = myDictionary.getWordDefination(Word);
                wordDefination.getEngine().loadContent(Defination);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        VBox layout = new VBox();
        layout.getChildren().addAll(historySearch,closeButton,wordDefination);
        layout.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(layout);
        window.setScene(newScene);
        window.show();
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