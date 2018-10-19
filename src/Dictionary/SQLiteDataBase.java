package Dictionary;
import javafx.scene.control.ListView;

import javax.print.attribute.HashAttributeSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class SQLiteDataBase {
    private static String Url = "jdbc:sqlite:src/DataBase/dict_hh.db";
    private Connection connection = null;
    private Statement statement;
    public void ConnectionSQLite(){
        try{
            connection = DriverManager.getConnection(Url);
            System.out.println("Connection to Sqlite Succesfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public HashMap<String,String> inputData(){
        HashMap<String,String> myList = new HashMap<String,String>();
        String query = "select * from av";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                myList.put(rs.getString("word").trim(),rs.getString("html"));
            }
            preparedStatement.close();
            rs.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return myList;
    }
    public void closeConnection(){
        try{
            connection.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void excuteQuery(String Query){
        try{
            this.statement = connection.createStatement();
            this.statement.executeQuery(Query);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
