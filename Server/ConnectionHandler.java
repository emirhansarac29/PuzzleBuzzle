import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionHandler extends Thread {

	Socket connectionSocket;

	BufferedReader inFromClient;
	BufferedWriter outToClient;

	PuzzleBuzzle GAME;
	boolean closeSocket = true;

	static int PUZZLE_ID = 0;

	public ConnectionHandler(Socket _connectionSocket) {
		connectionSocket = _connectionSocket;

		try {
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
		} catch (IOException e) {
			return;
		}

		this.start();
	}

	public void run() {
		String request = "";
		try {
			request = inFromClient.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(request);
		try {
			JSONObject req = new JSONObject(request);
			handle_request(req);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			if (closeSocket) {
				inFromClient.close();
				outToClient.close();
				connectionSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handle_request(JSONObject request) throws JSONException, IOException, InterruptedException {
		String function = request.getString("function");
		if (function.equals("log_in")) {
			log_in(request);
		} else if (function.equals("register")) {
			register(request);
		} else if (function.equals("waiting")) {
			waiting(request);
		} else if (function.equals("delete")) {
			deleteID(request);
		} else if (function.equals("setConnection")) {
			setGameSocket(request);
			closeSocket = false;
		} else if (function.equals("putpiece")) {
			setPiece(request);
		} else if (function.equals("updateScore")) {
			updateDB(request);
			deleteID(request);
		}
	}

	
	//SORUN
	public void deleteID(JSONObject req) throws JSONException, InterruptedException {
		int id = req.getInt("id");
		if (req.getString("function").equals("updateScore")) {
			TCP_Server.game_search.request();
			for (int i = 0; i < PuzzleBuzzle.games.size(); i++) {
				if (PuzzleBuzzle.games.get(i).GAME_ID == id) {
					PuzzleBuzzle.games.remove(i);
					break;
				}
			}
			TCP_Server.game_search.release();
		} else {
			TCP_Server.game_search.request();
			for (int i = 0; i < PuzzleBuzzle.games.size(); i++) {
				if (PuzzleBuzzle.games.get(i).GAME_ID == id && PuzzleBuzzle.games.get(i).numberOfPlayers != 2) {
					PuzzleBuzzle.games.remove(i);
					break;
				}
			}
			TCP_Server.game_search.release();
		}
	}

	// SORUN
	public void setPiece(JSONObject req) throws JSONException, InterruptedException, IOException {
		String name = req.getString("name");
		int id = req.getInt("id");
		int hint = req.getInt("hint");
		boolean inserted = false;
		TCP_Server.game_search.request();
		for (int i = 0; i < PuzzleBuzzle.games.size(); i++) {
			if (PuzzleBuzzle.games.get(i).GAME_ID == id) {
				PuzzleBuzzle.games.get(i).mutex.request();
				if (PuzzleBuzzle.games.get(i).pieces[hint] == false) {
					PuzzleBuzzle.games.get(i).pieces[hint] = true;
					JSONObject t = new JSONObject();
					t.put("success", "1");
					inserted = true;
					send_reply(t);

					send_notification(PuzzleBuzzle.games.get(i), hint, name);
				}
				PuzzleBuzzle.games.get(i).mutex.release();
				break;
			}
		}
		TCP_Server.game_search.release();
		if (!inserted) {
			JSONObject k = new JSONObject();
			k.put("success", "0");
			send_reply(k);
		}
	}

	

// SORUN
	public void setGameSocket(JSONObject pla) throws JSONException, IOException, InterruptedException {
		String name = "";
		String enemy = "";
		int id = 0;
		try {
			name = pla.getString("name");
			id = pla.getInt("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TCP_Server.game_search.request();
		for (int i = 0; i < PuzzleBuzzle.games.size(); i++) {
			if (PuzzleBuzzle.games.get(i).GAME_ID == id) {
				if (PuzzleBuzzle.games.get(i).player1.get("name").equals(name)) {
					PuzzleBuzzle.games.get(i).socket_player1 = connectionSocket;
					PuzzleBuzzle.games.get(i).b_p1 = inFromClient;
					PuzzleBuzzle.games.get(i).w_p1 = outToClient;
					enemy = PuzzleBuzzle.games.get(i).player2.getString("name");
				} else if (PuzzleBuzzle.games.get(i).player2.get("name").equals(name)) {
					PuzzleBuzzle.games.get(i).socket_player2 = connectionSocket;
					PuzzleBuzzle.games.get(i).b_p2 = inFromClient;
					PuzzleBuzzle.games.get(i).w_p2 = outToClient;
					enemy = PuzzleBuzzle.games.get(i).player1.getString("name");
				}
				break;
			}
		}
		TCP_Server.game_search.release();

		JSONObject p = new JSONObject();
		p.put("enemy", enemy);
		p.put("success", "1");
		send_reply(p);

	}

	// SORUN
	public void waiting(JSONObject a) throws JSONException, IOException, InterruptedException {

		join_game(a);

		JSONObject ad = new JSONObject();
		ad.put("success", "1");
		ad.put("id", GAME.GAME_ID);
		send_reply(ad);
	}

	// SORUN
	public void join_game(JSONObject a) throws JSONException, InterruptedException, SocketException {
		JSONObject player = new JSONObject();
		String name = a.getString("name");
		int point = a.getInt("point");
		int map = a.getInt("map");
		player.put("name", name);
		player.put("point", point);
		player.put("map", map);

		TCP_Server.game_search.request();
		boolean find = false;
		for (int i = 0; i < PuzzleBuzzle.games.size(); i++) {
			if (PuzzleBuzzle.games.get(i).map == map && PuzzleBuzzle.games.get(i).numberOfPlayers != 2) {
				GAME = PuzzleBuzzle.games.get(i);
				GAME.addPlayer(player);
				// while (checkAlive() && GAME.numberOfPlayers != 2) {
				// sleep(1000);
				// }
				JSONObject lit = new JSONObject();
				lit.put("id", PUZZLE_ID);
				send_reply(lit);
				find = true;
				break;
			}
		}
		if (!find) {
			PUZZLE_ID++;
			GAME = new PuzzleBuzzle(map, PUZZLE_ID);
			PuzzleBuzzle.games.add(GAME);
			GAME.addPlayer(player);

			JSONObject lit = new JSONObject();
			lit.put("id", PUZZLE_ID);
			send_reply(lit);
		}
		TCP_Server.game_search.release();

		while (checkAlive() && GAME.numberOfPlayers != 2) {
			sleep(1000);
		}
		if (!checkAlive()) {
			TCP_Server.game_search.request();
			PuzzleBuzzle.games.remove(GAME);
			TCP_Server.game_search.release();
			return;
		}

	}

	public boolean checkAlive() {
		JSONObject k = new JSONObject();
		try {
			k.put("success", 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outToClient.write(k.toString());
			outToClient.newLine();
			outToClient.flush();
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public void send_reply(JSONObject ret_f) {
		try {
			outToClient.write(ret_f.toString());
			outToClient.newLine();
			outToClient.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void log_in(JSONObject user) throws JSONException, IOException, InterruptedException {
		TCP_Server.database.request();
		BufferedReader userData = new BufferedReader(
				new FileReader(TCP_Server.db_address));
		JSONObject database = new JSONObject(userData.readLine());
		TCP_Server.database.release();
		userData.close();

		String username = user.getString("name");
		String password = user.getString("password");

		JSONObject return_function = new JSONObject();
		return_function.put("success", "0");
		return_function.put("point", 0);
		return_function.put("name", username);

		if (database.has(username) && database.getJSONObject(username).getString("password").equals(password)) {
			return_function.put("success", "1");
			return_function.put("point", database.getJSONObject(username).getInt("point"));
		}

		send_reply(return_function);
	}

	public void register(JSONObject user) throws JSONException, IOException, InterruptedException {
		TCP_Server.database.request();
		BufferedReader userData = new BufferedReader(
				new FileReader(TCP_Server.db_address));
		JSONObject database = new JSONObject(userData.readLine());
		userData.close();

		JSONObject return_function = new JSONObject();
		return_function.put("success", "1");

		String username = user.getString("name");
		String password = user.getString("password");

		if (database.has(username)) {
			return_function.put("success", "0");
		} else {
			JSONObject info_about_user = new JSONObject();
			info_about_user.put("password", password);
			info_about_user.put("point", 0);
			database.put(username, info_about_user);

			BufferedWriter dbWriter = new BufferedWriter(
					new FileWriter(TCP_Server.db_address));
			dbWriter.write(database.toString());
			dbWriter.close();
		}
		send_reply(return_function);
		TCP_Server.database.release();
	}

	public void updateDB(JSONObject req) throws InterruptedException, JSONException, IOException {
		TCP_Server.database.request();
		BufferedReader userData = new BufferedReader(
				new FileReader(TCP_Server.db_address));
		JSONObject database = new JSONObject(userData.readLine());
		TCP_Server.database.release();
		userData.close();

		String username = req.getString("name");
		int point = req.getInt("point");
		int id = req.getInt("id");

		if (database.has(username)) {
			database.getJSONObject(username).put("point", point);

		}
		TCP_Server.database.request();
		BufferedWriter dbWriter = new BufferedWriter(
				new FileWriter(TCP_Server.db_address));
		dbWriter.write(database.toString());
		dbWriter.close();
		TCP_Server.database.release();

	}
	
	public void send_notification(PuzzleBuzzle game, int hint, String name) {
		Socket s = null;
		BufferedReader r = null;
		BufferedWriter w = null;
		try {
			if (name.equals(game.player1.getString("name"))) {
				s = game.socket_player2;
				r = game.b_p2;
				w = game.w_p2;
			} else {
				s = game.socket_player1;
				r = game.b_p1;
				w = game.w_p1;
			}
		} catch (JSONException e) {
			return;
		}
		try {
			JSONObject ret = new JSONObject();
			ret.put("hint", hint);
			w.write(ret.toString());
			w.newLine();
			w.flush();

		} catch (IOException e) {
			return;
		} catch (JSONException e) {
			return;
		}
	}
}
