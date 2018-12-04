package uet.oop.bomberman.entities.character.enemy.ai;

/**
 * class phục vụ cho thuật toán tìm đường cao cấp
 */
public class QItem {
    protected int row;
    protected int col;
    protected int dic;
    public QItem(int row,int col, int dic){
        this.row = row;
        this.col = col;
        this.dic = dic;
    }
}
