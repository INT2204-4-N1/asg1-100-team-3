package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Sound.Audio;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

public class Bomb extends AnimatedEntitiy {


	protected double _timeToExplode = 120; //2 seconds
	public int _timeAfter = 20;
	protected Board _board;
	protected Flame[] _flames;
	protected boolean _exploded = false;
	protected boolean _allowedToPassThru = true;

	/**
	 * phương thức khởi tạo cho class bom
	 * @param x	hoành độ
	 * @param y	tung dộ
	 * @param board lớp board dùng để lưu thông tin của game
	 */
	public Bomb(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;
		this.Sound = new Audio();
	}

	/**
	 *  phương thức cập nhật trạng thái
	 *  nếu thòi gian nổ còn lớn hơn 0 thì sẽ giảm nó xuống
	 *  còn không kiểm tra biến _exploded nếu false thì sẽ gọi hàm nổ bom
	 *	ngược lại nếu bom đã nổ thì ta gọi hàm update các flame
	 *	time_after là biến lưu thời gian ảnh hưởng sau nổ của bom
	 *	nếu nó lớn hơn 0 thì ta giảm nó xuống, ngược lại ta xóa bỏ đối tượng bom hiện tại
	 */
	@Override
	public void update() {
		if(_timeToExplode > 0)
			_timeToExplode--;
		else {
			if(!_exploded)
				explode();
			else
				updateFlames();

			if(_timeAfter > 0)
				_timeAfter--;
			else
				remove();
		}

		animate();
	}

	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite =  Sprite.bomb_exploded2;
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);

		int xt = (int)_x << 4;
		int yt = (int)_y << 4;

		screen.renderEntity(xt, yt , this);
	}

	/**
	 * phương thức render các flame của bom
	 * @param screen Screen là màn hình chơi game
	 */
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}

	/**
	 * phương thức cập nhật các Flame của bom
	 */
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}

    /**
     * Xử lý Bomb nổ
	 * ta gán giá trị _allowedToPassThru = true và _exploded = true;
	 * nếu có nhân vật nào(Bomber,Enemy) đang ở vị trí hiện tại thì nhân vật đó sẽ bị chết
	 * tạo các Flame cho quả bom với thông tin gồm bán kính nổ, tọa độ,v...v
     */
    public void explode() {
		_allowedToPassThru = true;
		_exploded = true;
		this.Sound.Explosion();
		Character a = _board.getCharacterAtExcluding(_x, _y);
		if(a != null)  {
			a.kill();
		}

		_flames = new Flame[4];

		for (int i = 0; i < _flames.length; i++) {
			_flames[i] = new Flame((int)_x, (int)_y, i, Game.getBombRadius(), _board);
		}
	}

	/**
	 * phương thức tạo mảnh Flame tại vị trí x,y
	 * @param x hoàng độ
	 * @param y	tung độ
	 * @return mảnh flame cần tạo
	 */
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;

		for (int i = 0; i < _flames.length; i++) {
			if (_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if (e != null) return e;
		}
		return null;
	}

	/**
	 * phuonwg thức xử lý va chạm
	 * @param e	thực thể cần được xét va chạm
	 * @return	biến _allowedToPassThru cho biết Bomber liệu còn có thể đi qua quả bom hay không(true nếu có và flase nếu không)
	 *
	 */
	@Override
	public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
        // TODO: xử lý va chạm với Flame của Bomb khác
		if(e instanceof Bomber) {
			double diffX = e.getX() - Coordinates.tileToPixel(getX());
			double diffY = e.getY() - Coordinates.tileToPixel(getY());

			if(!(diffX >= -10 && diffX < 16 && diffY >= 1 && diffY <= 28)) { // differences to see if the player has moved out of the bomb, tested values
				_allowedToPassThru = false;
			}

			return _allowedToPassThru;
		}

		if(e instanceof Flame) {
			return true;
		}
		return false;
	}
}
