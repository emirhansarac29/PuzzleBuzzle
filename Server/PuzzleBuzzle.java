import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

public class PuzzleBuzzle {

	public static ArrayList<PuzzleBuzzle> games = new ArrayList<PuzzleBuzzle>();

	Semaphore mutex;
	JSONObject player1;
	Socket socket_player1 = null;
	BufferedReader b_p1 = null;
	BufferedWriter w_p1 = null;

	JSONObject player2;
	Socket socket_player2 = null;
	BufferedReader b_p2 = null;
	BufferedWriter w_p2 = null;

	int numberOfPlayers;
	int map;

	boolean[] pieces = new boolean[16];
	int GAME_ID;

	protected PuzzleBuzzle(int map_, int id_) {
		numberOfPlayers = 0;
		this.map = map_;
		mutex = new Semaphore(1);
		GAME_ID = id_;
		for (int i = 0; i < 16; i++) {
			pieces[i] = false;
		}
	}

	protected void addPlayer(JSONObject player) throws InterruptedException {

		if (numberOfPlayers == 0) {
			player1 = player;
			numberOfPlayers++;
		} else if (numberOfPlayers == 1) {
			player2 = player;
			numberOfPlayers++;
		}
	}

	protected void putPiece(JSONObject movement) {

	}

	protected void gameOver() {

	}
}
