package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Sound.Audio;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {

    int xBomb;
    int yBomb;
    private List<Bomb> _bombs;
    protected Keyboard _input;
    protected ArrayList<Item> bomberItem = new ArrayList<>();
    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
        this.Sound = new Audio();
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO: Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 30
        /**
         * khiểm tra input từ bàn phím nếu là nút đặt bom và số lượng bom còn có thể đặt mà lớn hơn 0
         * và khoảng thời gian giữa 2 lần đặt bom <0 thì tiền hành đặt bom
         * xt và yt là tọa độ của quả bom được hiệu chỉnh để chính xác thêm
         * tiến hành gọi hàm placeBomb và trừ số lượng bom có thể dặt hiện tại đi 1
         * đặt lại thời gian giữa 2 lần dặt bom là 30ms
         */
        if( _input.space == true && Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {

            int xBomb = Coordinates.pixelToTile(_x + _sprite.getSize()/2);
            int yBomb = Coordinates.pixelToTile( (_y + _sprite.getSize() / 2) - _sprite.getSize() );

            placeBomb(xBomb,yBomb);
            Game.addBombRate(-1);

            _timeBetweenPutBombs = 30;
        }
    }

    /**
     * phương thức tạo đối tượng bomb và thêm vào trên bản đồ
     * @param x hoàng đọ của quả bom
     * @param y tung độ của quả bom
     */
    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb b = new Bomb(x, y, _board);
        _board.addBomb(b);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        this.Sound.PlayerDead();
        _alive = false;
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    /**
     * Phương thức tính toán di chuyển
     * nhận tín hiệu từ bàn phím nếu là xuống ya = 1, lên ya = -1, sang trái xa = -1, sang phải xa = 1
     * ta sẽ di chuyển đến tọa độ mới (cần nhân thêm với giá trị tốc độ của Bomber)
     * đặt lại biến _moving = true nếu di chuyển và = false nếu như không
     */
    @Override
    protected void calculateMove() {
        int xMove = 0, yMove = 0;
        if(_input.up) yMove--;
        if(_input.down) yMove++;
        if(_input.left) xMove--;
        if(_input.right) xMove++;

        if(xMove != 0 || yMove != 0)  {
            move(xMove * Game.getBomberSpeed(), yMove * Game.getBomberSpeed());
            _moving = true;
        } else {
            _moving = false;
        }

    }

    /**
     * phương thức xem xét có thể di chuyển tới tọa độ mới hay không
     * @param x hoàng độ vị trí mới
     * @param y tung độ vị trí mới
     * @return true nếu có thể và false nếu không
     */
    @Override
    public boolean canMove(double x, double y) {
        /**
         * xét 4 góc xung quanh nhân vật ( có thực hiện hiệu chỉnh để tăng độ chính xác)
         * kiểm tra các thực thể ở 4 vi trí xung quanh bomber nếu các thực thể đố không cho phép Bomber đi qua
         * thì ta trả về false ngược lại ta tra về true
         */
        for (int i = 0; i < 4; i++) {
            double xt = ((_x + x) + i % 2 * 11) / Game.TILES_SIZE;
            double yt = ((_y + y) + i / 2 * 12 - 13) / Game.TILES_SIZE;

            Entity a = _board.getEntity(xt, yt, this);

            if(!a.collide(this))
                return false;
        }
        return true;
    }

    @Override
    public void move(double xa, double ya) {
        if(xa > 0) _direction = 1;
        if(xa < 0) _direction = 3;
        if(ya > 0) _direction = 2;
        if(ya < 0) _direction = 0;

        if(canMove(0, ya)) { //separate the moves for the player can slide when is colliding
            _y += ya;
        }

        if(canMove(xa, 0)) {
            _x += xa;
        }
    }

    /**
     * phương thức kiểm tra va chạm giữa bomber với các thực thể khác
     * @param e thực thể cần được kiểm tra
     * @return  trong trường hơp Bomber và chạm với Flame hoặc Enemy Bomber sẽ chết ta gọi hàm Kill()
     */
    @Override
    public boolean collide(Entity e) {
        if(e instanceof Flame) {
            kill();
            return false;
        }

        if(e instanceof Enemy) {
            kill();
            return true;
        }

        return true;
    }

    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }

    /**
     * phương thức kích hoạt item cho Bomber
     * @param e là Item mà Bomber đã ăn được
     */
    public void powerUp(Item e){
       if(e.isRemoved())    return;
       bomberItem.add(e);
       e.setValue();
    }
}
