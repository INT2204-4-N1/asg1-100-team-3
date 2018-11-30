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
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.level.FileLevelLoader;
import uet.oop.bomberman.level.LevelLoader;

import java.io.File;
import java.util.*;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	QItem qItemPlayer;
	QItem qItemEnemy;
	boolean[][] visited = new boolean[14][32];

	public AIMedium(Bomber bomber, Enemy e,Board board) {
		_bomber = bomber;
		_e = e;
		this._board = board;
	}
	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi
		QItem[][] pre = new QItem[100][100];
		int desCol = Coordinates.pixelToTile(_e.getX());
		int desRow = Coordinates.pixelToTile(_e.getY());
		int tmpCol = desCol;
		int tmpRow = desRow;
		int _height = 13;
		int _width = 31;
		for (int y = 2; y <= _height-3; y++) {
			for (int x = 1; x <= _width-2; x++) {
				if(y%2!=0 && x%2==0 ) this.visited[y][x] = true;
				else if(x==16 && y==10) this.visited[y][x] = true;
				else this.visited[y][x] = false;
			}
		}
		List<Bomb> _bombs = new ArrayList<>();
		_bombs = _board.getBombs();
		Iterator<Bomb> bs = _bombs.iterator();
		int col = 0;
		int row = 0;
		if(bs.hasNext()){
			Bomb b = bs.next();
			col =  (int)(b.getX());
			row =  (int)(b.getY());
		}
		this.visited[row][col] = true;
		int step = BreadthFirstSearch(pre);
		if(step!=-1) {
			System.out.println(step);
			QItem previous = pre[tmpRow][tmpCol];
			if(previous.dic==0)	return -1;
			if (previous.row == tmpRow) {
				if (previous.col < tmpCol) {
					return 3;
				} else if (previous.col >= tmpCol) {
					return 1;
				}
			} else if (previous.col == tmpCol) {
				if (previous.row < tmpRow) {
					return 0;
				} else if (previous.row >= tmpRow) {
					return 2;
				}
			}
		}
		return this.random.nextInt(4);
	}
	private int BreadthFirstSearch(QItem[][] pre) {
		int desCol = Coordinates.pixelToTile(_e.getX());
		int desRow = Coordinates.pixelToTile(_e.getY());
		int srcCol = Coordinates.pixelToTile(_bomber.getX());
		int srcRow = Coordinates.pixelToTile(_bomber.getY());
		qItemPlayer = new QItem(srcRow, srcCol, 0);
		qItemEnemy = new QItem(desRow, desCol, 0);
		Queue<QItem> queue = new LinkedList<>();
		queue.add(qItemPlayer);
		this.visited[qItemPlayer.row][qItemPlayer.col] = true;
		int step = -1;
		while (!queue.isEmpty()) {
			QItem temp = queue.remove();
			if (Coordinates.pixelToTile(_e.getX())== temp.col && Coordinates.pixelToTile(_e.getY()) == temp.row) {
				step = temp.dic;
				break;
			}
			if (temp.row - 1 >= 2 && visited[temp.row - 1][temp.col] == false) {
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
