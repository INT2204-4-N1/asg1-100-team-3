package uet.oop.bomberman.Sound;

import javafx.scene.layout.Background;
import javazoom.jl.player.Player;

public class Audio {
    private static String BACKGROUND_SOUND = "src/source/Background.mp3";
    private static String BOMB_EXPLESION = "src/source/Explosion.mp3";
    private static String PLAYER_DEAD = "src/source/PlayerDead.mp3";
    private static String ENDING_GAME = "src/source/GameOver.mp3";
    private static Thread BackGround;
    private static Thread Explosion;
    private static Thread PlayerDead;
    private static Thread GameOver;

    /**
     * phương thức gọi luồng nhạc nền
     */
    public static void BackgroundSound(){
        if(BackGround==null || ! BackGround.isAlive()) {
            BackGround = new Thread(new ThreadSound(BACKGROUND_SOUND));
            BackGround.start();
        }
    }

    /**
     * phương thức gọi luồng nhạc bom nổ
     */
    public static void Explosion(){
        Explosion = new Thread(new ThreadSound(BOMB_EXPLESION));
        Explosion.start();
    }

    /**
     * phương thưc gọi luồng nhạc khi Bomber chết
     */
    public static void PlayerDead(){
        stopThread(BackGround);
        PlayerDead = new Thread(new ThreadSound(PLAYER_DEAD));
        PlayerDead.start();
    }

    /**
     * phương thức dùng một luồng
     * @param thread luồng cần dừng
     */
    public static void stopThread(Thread thread) {
        thread.stop();
    }

}
