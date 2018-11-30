package uet.oop.bomberman.level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Kondoria;
import uet.oop.bomberman.entities.character.enemy.Minvo;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import java.io.*;
import java.net.URL;
import java.util.StringTokenizer;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;

	public FileLevelLoader(Board board, int level) throws LoadLevelException, IOException {
		super(board, level);
	}

	@Override
	public void loadLevel(int level) throws IOException {
		// TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
		// TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map
		level = 4;
		String path = "levels/Level"+level+".txt";
		URL absPath = FileLevelLoader.class.getResource("/" + path);

		BufferedReader in = new BufferedReader(new InputStreamReader(absPath.openStream()));

		String data = in.readLine();
		StringTokenizer tokens = new StringTokenizer(data);

		_level = Integer.parseInt(tokens.nextToken());
		_height = Integer.parseInt(tokens.nextToken());
		_width = Integer.parseInt(tokens.nextToken());

		char[][] map = new char[_height][_width];
		for (int x = 0; x < _height; x++) {
			data = in.readLine();
			for (int y = 0; y < _width; y++) {
				map[x][y] = data.charAt(y);
			}
		}
		in.close();
		_map = map;
	}

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, gọi _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình

		for (int y = 0; y < _height; y++) {
			for (int x = 0; x < _width; x++) {
				int pos = x + y * _width;
				switch (_map[y][x]) {
					case '#':
						_board.addEntity(pos, new Wall(x, y, Sprite.wall));

						break;
					case '*':
						_board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new Brick(x, y, Sprite.brick)));

						break;
					case 'x':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new Portal(x, y, Sprite.portal),
										new Brick(x, y, Sprite.brick)
								)
						);

						break;
					case 'p':
						_board.addCharacter(new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						Screen.setOffset(0, 0);
						_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));

						break;
					case '1':
						_board.addCharacter(new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));

						break;
					case '2':
						_board.addCharacter(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));

						break;
					case '3':
						_board.addCharacter(new Kondoria(Coordinates.tileToPixel(x),Coordinates.tileToPixel(y)+Game.TILES_SIZE,_board));
						_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));

						break;
					case '4':
						_board.addCharacter(new Minvo(Coordinates.tileToPixel(x),Coordinates.tileToPixel(y)+Game.TILES_SIZE,_board));
						_board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));

						break;
					case 'b':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new BombItem(x, y, Sprite.powerup_bombs),
										new Brick(x, y, Sprite.brick)
								)
						);

						break;
					case 'f':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new FlameItem(x, y, Sprite.powerup_flames),
										new Brick(x, y, Sprite.brick)
								)
						);

						break;
					case 's':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new SpeedItem(x, y, Sprite.powerup_speed),
										new Brick(x, y, Sprite.brick)
								)
						);

						break;
					default:
						_board.addEntity(pos, new Grass(x, y, Sprite.grass) );

						break;
				}
			}
		}
	}

}