package Dictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import sample.Controller;
import sample.Main;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Dictionary {
    private ArrayList<String> list = new ArrayList<String>();
    private HashMap<String,String> dictionary = new HashMap<String,String>();
    private SQLiteDataBase sqlData = new SQLiteDataBase();
    private Controller controller = Main.getLoader().getController();
    public ArrayList<String> getListWordsFormDictionary(){
        ArrayList<String> Temp = new ArrayList<String>();
        for(Map.Entry<String,String> entry : dictionary.entrySet()){
            Temp.add(entry.getKey());
        }
        return Temp;
    }
    public void readDataFormSQLFile(){
        sqlData.ConnectionSQLite();
        dictionary = sqlData.inputData();
    }
    public String getWordDefination(String Word){
        if(dictionary.containsKey(Word)==true){
            String Defination = dictionary.get(Word);
            return Defination;
        }
        return null;
    }
    public void addWord(String Word,String Defination){
        String query = "INSERT INTO av (word, html) VALUES(?,?)";
        try{
            PreparedStatement preparedStatement = sqlData.getConnection().prepareStatement(query);
            preparedStatement.setString(1,Word);
            preparedStatement.setString(2,Defination);
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteWord(String Word){
        String query = "delete from av  where word=?";
        try{
            PreparedStatement preparedStatement = sqlData.getConnection().prepareStatement(query);
            preparedStatement.setString(1,Word);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public HashMap<String, String> getDictionary() {
        return dictionary;
    }
    public void setDictionary(HashMap<String, String> dictionary) {
        this.dictionary = dictionary;
    }
    public ArrayList<String> getlistWord(){
        return this.list;
    }
}
