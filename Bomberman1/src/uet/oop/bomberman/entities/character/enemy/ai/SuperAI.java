package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.level.Coordinates;
public class SuperAI extends AIMedium {
    private Bomb bomb;
    public SuperAI(Bomber bomber, Enemy e, Board board) {
        super(bomber,e,board);
    }
    @Override
    public int calculateDirection() {
        // TODO: cài đặt thuật toán tìm đường đi
        bodyFlame();
        return super.calculateDirection();
    }
    public void explodeBody(){
        int xBomb = Coordinates.pixelToTile(this._e.getX() + (this._e.getSprite().getSize()) / 2);
        int yBomb = Coordinates.pixelToTile((this._e.getY() + this._e.getSprite().getSize() / 2) - this._e.getSprite().getSize());
        bomb = new Bomb(xBomb, yBomb, this._board);
        this._board.addBomb(bomb);
        bomb.explode();

    }
    public void bodyFlame(){
        int desRow = Coordinates.pixelToTile(this._bomber.getX());
        int desCol = Coordinates.pixelToTile(this._bomber.getY());
        int srcRow = Coordinates.pixelToTile(this._e.getX());
        int srcCol = Coordinates.pixelToTile(this._e.getY());
        if(desCol==srcCol){
            if(Math.abs(desRow-srcRow)<=1){
                explodeBody();
            }
        }
        else if(desRow==srcRow){
            if(Math.abs(desCol-srcCol)<=1){
                explodeBody();
            }
        }
    }

}
