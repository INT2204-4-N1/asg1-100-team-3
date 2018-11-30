package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;

public class AILow extends AI {

	public AILow(){
	}
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi
		int step = this.random.nextInt(4);
		return step;
	}

}
