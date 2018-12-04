package uet.oop.bomberman.Sound;
import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class ThreadSound extends Thread{

    private String audioPath;
    private boolean isRunning= true;
    public ThreadSound(String Path,boolean check){
        this.audioPath = Path;
        isRunning = check;
    }
    public ThreadSound(String Path){
        this.audioPath = Path;
    }
    @Override
    public void run(){
        FileInputStream soundIn;
            try {
                while(isRunning) {
                    soundIn = new FileInputStream(audioPath);
                    Player player = new Player(soundIn);
                    player.play();
                    Stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public void Stop(){
        isRunning = false;
    }
}
