package HashTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class HashTable {
    public Vector< Vector<String> > hashTable = new Vector<Vector<String>>();
    public HashTable(){
        hashTable.setSize(139007);
    }
    public int hashFunction(String Word){
        int key = 0;
        for(int i =0;i<Word.length();i++){
            key += (Word.charAt(i) - '0') + key<<6 + key<<10 - key;
        }
        return Math.abs(key) % 139007;
    }
    public void insertWord(String Word){
        try{
            int key = hashFunction(Word);
            if(hashTable.get(key)==null){
            Vector<String> Temp = new Vector<String>();
            Temp.addElement(Word);
            hashTable.set(key,Temp);
            }
            else{
                Vector<String> Temp = hashTable.get(key);
                Temp.addElement(Word);
                hashTable.set(key,Temp);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean findWord(String Word){
        int key = hashFunction(Word);
        if(hashTable.get(key) == null)  return false;
        else if(hashTable.get(key).indexOf(Word)==-1)    return false;
        return true;
    }
    public static void main(String Args[]){
        HashTable tb = new HashTable();
        System.out.println(tb.hashFunction("dog"));
        tb.insertWord("dog");
        tb.insertWord("god");
        for(String str: tb.hashTable.get(tb.hashFunction("dog"))){
            System.out.println(str);
        }
        System.out.println(tb.findWord("dog"));
        System.out.println(tb.hashFunction("god"));
        System.out.println(tb.hashFunction("dog"));

    }
}
