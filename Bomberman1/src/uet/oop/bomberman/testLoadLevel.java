package uet.oop.bomberman;

import com.sun.glass.ui.Size;

import java.io.FileInputStream;

public class testLoadLevel {
    public static void main(String args[]){
        String path ="res/levels/level" + 1 + ".txt";
        try{
            FileInputStream fileInputStream = new FileInputStream(path);
            int read = 0;
            String line1="";
            while((char)(read=fileInputStream.read())!='\n'){
                line1+= (char)read;
            }
            System.out.println(line1);
            String[] SizeMap = line1.split(" ");
            int _height = Integer.parseInt(SizeMap[1].trim());
            int _width = Integer.parseInt(SizeMap[2].trim()) + 1;
            char[][] _map = new char[_height][_width];
            String[] line = new String[_height];
            for(int i=0;i<_height;i++){
                line[i]="";
                while((char)(read=fileInputStream.read())!='\n'){
                    line[i]+=(char)read;
                }
            }
            for(int i=0;i<_height;i++){
                for(int j=0;j<line[i].length();j++){
                    _map[i][j]= line[i].charAt(j);
                }
            }
            for(int i=0;i<_height;i++){
                for(int j=0;j<_width;j++){
                    System.out.print(_map[i][j]);
                }
                System.out.println("");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
