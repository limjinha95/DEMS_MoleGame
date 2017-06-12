import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class ServerPC {

	public static void main(String[] args) throws IOException {

		String url = "jdbc:mysql://localhost:3306/Molegame";
		String id = "root";
		String password = "sqljinha9413";
		Connection con;
		dbconn d1 = new dbconn();

		ServerSocket serverSock = null;
		Socket clientSock = null;
		PrintWriter out = null;
		BufferedReader in = null;
		serverSock = new ServerSocket(9555);

		try {
			System.out.println("서버시작");
			clientSock = serverSock.accept();
			System.out.println("클라이언트 연결");
			out = new PrintWriter(clientSock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));

			String inputdata;
			String[] strarr;
			String username;
			int userscore = 0;

			inputdata = in.readLine();
			strarr = inputdata.split(",");
			username = strarr[0];
			userscore = Integer.parseInt(strarr[1]);

			con = (Connection) DriverManager.getConnection(url,id, password);
			d1.insertData(con, url, id, password, username, userscore);
			d1.getData(con, url, id, password);

			out.close();
			in.close();
			clientSock.close();
			serverSock.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

class dbconn{
	public void insertData(Connection con, String url, String id, String pass,String name, int score){

		try{
			PreparedStatement st2 = (PreparedStatement)con.prepareStatement("insert into ranking values(?, ?)");
			st2.setString(1,name);
			st2.setInt(2,score);
			st2.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getData(Connection con, String url, String id, String pass){

		try{
			int rank = 1;
			con = (Connection) DriverManager.getConnection(url,id, pass);
			PreparedStatement st = (PreparedStatement)con.prepareStatement("select * from ranking order by score desc");
			ResultSet set = st.executeQuery();

			System.out.printf("|  RANK  |  USERNAME  |  SCORE  |\n");
			System.out.printf("---------------------------------\n");

			while(set.next()){
				System.out.printf("|   %2d   | %10s |  %5d  |\n", rank, set.getString("name"), set.getInt("score"));
				rank++;
			}
			System.out.printf("---------------------------------\n");

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
