package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
public class historyController implements Initializable {
    @FXML private ListView<String> historySearch = new ListView<String>();
    @FXML private Button closeButton = new Button("Đóng");
    @FXML private WebView wordDefination = new WebView();
    @FXML private AnchorPane historyAnchorPanel = new AnchorPane();
    private Controller controller = Main.getLoader().getController();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        historySearch.getItems().addAll(controller.ReadRecentWordFormFile());
        historySearch.setOnMouseClicked(event -> {
            try{
                String Word = historySearch.getSelectionModel().getSelectedItem();
                String Defination = controller.getDictionary().getWordDefination(Word);
                if(Defination==null){
                    createAlertBoxes();
                    return;
                }
                wordDefination.getEngine().loadContent(Defination);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public void pressButton(ActionEvent e){
        if(e.getSource()==closeButton){
            Stage stage = (Stage)closeButton.getScene().getWindow();
            stage.close();
        }
    }
    public void createAlertBoxes(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Lỗi Tìm Từ");
        window.setWidth(400);
        window.setHeight(250);
        Label Temp = new Label();
        Temp.setText("Từ bạn tìm không còn tồn tại");
        Temp.setStyle("-fx-font-size:16;-fx-font-weight:bold");
        Button closeButton = new Button("Đóng");
        closeButton.setOnAction(event -> window.close());
        VBox layout = new VBox();
        layout.setOnKeyPressed(event -> {
            if(event.getCode()== KeyCode.ENTER){
                window.close();
            }
        });
        layout.getChildren().addAll(Temp,closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(layout);
        window.setScene(newScene);
        window.show();
    }
}
