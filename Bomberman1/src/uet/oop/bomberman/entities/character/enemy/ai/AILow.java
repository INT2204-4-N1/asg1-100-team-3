package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;

public class AILow extends AI {

	public AILow(){
	}
	/**
	 * thuật toám tìm đường mức thấp
	 * @return giá trị random trong khoảng từ 0 đến 3
	 */
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi
		int step = this.random.nextInt(4);
		return step;
	}

}
