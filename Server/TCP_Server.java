import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_Server {

	public static ServerSocket server_socket;
	public static int _port;
	public static Semaphore database;
	public static Semaphore game_search;
	
	public static String db_address = "PuzzleDB/users.txt";
	public static String folder_address = "PuzzleDB";

	public static void main(String args[]) throws Exception {
		database = new Semaphore(1);
		game_search = new Semaphore(1);
		_port = 60015;
		start_server();
	}

	public static void start_server() {

		Thread server_thread = new Thread() {
			@Override
			public void run() {
				
				try {
					loadDB();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					server_socket = new ServerSocket(_port);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				while (true) {
					try {
						Socket connectionSocket = server_socket.accept();
					//	System.out.println("GELDİ");
						new ConnectionHandler(connectionSocket);
					} catch (IOException e) {
						System.err.println("Server aborted:" + e);
					}
				}
			}

		};

		server_thread.start();
	}

	public static void loadDB() throws IOException {
		// String path =
		// FileSystems.getDefault().getPath("").toAbsolutePath().toString();
		File theDir = new File(folder_address);
		if (!theDir.exists()) {
			boolean result = false;
			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				return;
			}
		}
		File users = new File(db_address);
		if (!users.exists()) {
			users.createNewFile();
			BufferedWriter dbWriter = new BufferedWriter(new FileWriter(db_address));
			dbWriter.write("{}");
			dbWriter.close();
		}


	}

}
