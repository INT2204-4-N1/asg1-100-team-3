package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.level.FileLevelLoader;
import uet.oop.bomberman.level.LevelLoader;

import java.io.File;
import java.util.*;

public class AIMedium extends AI {
	static int count = 0;
	Bomber _bomber;
	Enemy _e;
	QItem qItemPlayer;
	QItem qItemEnemy;
	boolean[][] visited = new boolean[30][32];

	public AIMedium(Bomber bomber, Enemy e,Board board) {
		_bomber = bomber;
		_e = e;
		this._board = board;
	}

	/**
	 * thuật toán tìm đường cao cấp
	 * @return các giá trị cho phép enemy có thể đuổi theo vị trí của Bomber
	 */
	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi
		count++;
		QItem[][] pre = new QItem[100][100];
		int desCol = Coordinates.pixelToTile(_e.getX());
		int desRow = Coordinates.pixelToTile(_e.getY());
		int tmpCol = desCol;
		int tmpRow = desRow;
		int _height = 13;
		int _width = 31;
		for (int y = 0; y < _height; y++) {
			for (int x = 0; x < _width; x++) {
				Entity a = _board.getEntityAt(x,y);
				if(a!=null){
					if (a instanceof Wall){
						this.visited[y+1][x] = true;
					}
					else if(a instanceof Brick){
						this.visited[y][x] = true;
					}
					else if(a instanceof Portal){
						this.visited[y][x] =true;
					}
					else if(a instanceof Item){
						this.visited[y][x] = true;
					}
					else this.visited[y+1][x] = false;
				}
			}
		}
		/*for (int y = 0; y <_height; y++) {
			for (int x = 0; x < _width; x++) {
				System.out.print(this.visited[y][x]+" ");
			}
			System.out.println("");
		}*/
		List<Bomb> _bombs = new ArrayList<>();
		_bombs = _board.getBombs();
		Iterator<Bomb> bs = _bombs.iterator();
		int col = 0;
		int row = 0;
		while(bs.hasNext()){
			Bomb b = bs.next();
			col =  (int)(b.getX());
			row =  (int)(b.getY());
			this.visited[row][col] = true;
			this.visited[row+1][col-1] = true;
		}
		int step = BreadthFirstSearch(pre);
		System.out.println(step);
		if(step!=-1) {
			QItem previous = pre[tmpRow][tmpCol];
			if(previous!=null) {
				/*
				System.out.println(tmpRow+" "+tmpCol);
				System.out.println(previous.row +" "+previous.col);*/
				if (previous.row == tmpRow) {
					if (previous.col < tmpCol) {
						return 3;
					} else if (previous.col >= tmpCol) {
						return 1;
					}
				}
				else if (previous.col == tmpCol) {
					if (previous.row < tmpRow) {
						return 0;
					} else if (previous.row >= tmpRow) {
						return 2;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * thuật toán duyệt chiều rộng
	 * @param pre ma trận lưu vết đường đi từ enemy tới Bomber
	 * @return khoảng cách gần nhất giữa Enemy và Bomber
	 */
	private int BreadthFirstSearch(QItem[][] pre) {
		int desCol = Coordinates.pixelToTile(_e.getX());
		int desRow = Coordinates.pixelToTile(_e.getY());
		int srcCol = Coordinates.pixelToTile(_bomber.getX());
		int srcRow = Coordinates.pixelToTile(_bomber.getY());
		System.out.println(desRow+" "+desCol);
		System.out.println(srcRow+" "+srcCol);
		qItemPlayer = new QItem(srcRow, srcCol, 0);
		qItemEnemy = new QItem(desRow, desCol, 0);
		Queue<QItem> queue = new LinkedList<>();
		queue.add(qItemPlayer);
		this.visited[qItemPlayer.row][qItemPlayer.col] = true;
		int step = -1;
		while (!queue.isEmpty()) {
			QItem temp = queue.remove();
			if (desCol== temp.col && desRow == temp.row) {
				step = temp.dic;
				break;
			}
			if (temp.row - 1 >= 1 && visited[temp.row - 1][temp.col] == false) {
				queue.add(new QItem(temp.row - 1, temp.col, temp.dic + 1));
				this.visited[temp.row - 1][temp.col] = true;
				pre[temp.row - 1][temp.col] = temp;
			}
			if (temp.row + 1 <= 12 && visited[temp.row + 1][temp.col] == false) {
				queue.add(new QItem(temp.row + 1, temp.col, temp.dic + 1));
				this.visited[temp.row + 1][temp.col] = true;
				pre[temp.row + 1][temp.col] = temp;
			}
			if (temp.col - 1 >= 1 && visited[temp.row][temp.col - 1] == false) {
				queue.add(new QItem(temp.row, temp.col - 1, temp.dic + 1));
				this.visited[temp.row][temp.col - 1] = true;
				pre[temp.row][temp.col - 1] = temp;
			}
			if (temp.col + 1 <= 30 && visited[temp.row][temp.col + 1] == false) {
				queue.add(new QItem(temp.row, temp.col + 1, temp.dic + 1));
				this.visited[temp.row][temp.col + 1] = true;
				pre[temp.row][temp.col + 1] = temp;
			}
		}
		return step;
	}
}
