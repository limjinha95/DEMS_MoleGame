package com.example.dems12_molegame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import com.example.molegamejni.MolegameJNI;

import android.util.Log;

/* 세그먼트, 도트매트릭스와 같이 지속적으로 출력해야 하는 입출력장치를 처리하는 기능 
 * 서버와 통신 기능*/
public class DisplayThread extends Thread {
	private final static int DOT_TIME = 100;
	private Result result;
	private MolegameJNI molegameJNI;

	final String serverIP = "172.20.10.2";
	final int serverPort = 12000;
	
	
	//도트매트릭스와 세그먼트장치를 사용하기 위한 JNI객체를 받음
	public DisplayThread(MolegameJNI molegameJNI) {
		this.molegameJNI = molegameJNI;
	}

	//지속적으로 값을 주어야하는 장치들에 대해 지속적인 출력을 수행
	@Override
	public void run() {
		int i, j;
		molegameJNI.dotmatrixOpen();

		molegameJNI.piezoWrite((char)0x11);
		try { Thread.sleep(300); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x13);
		try { Thread.sleep(300); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x15);
		try { Thread.sleep(300); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x21);
		try { Thread.sleep(500); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x00);
		
		while(Share.gameCount > 0) {
			result = makeMole();
			for(j = 0; j < DOT_TIME; j++) {
				if(Share.gameCount == -1) break;
				molegameJNI.dotmatrixControl(result.getDotShape());
				molegameJNI.segmentPrint(Share.score);
				if (result.ansPosition == Share.inputNum) {	//두더지를 잡을 경우의 수행
					result.setDotShape("00003f212121213f0000");
					result.setAnsPosition(-1);
					Share.score += 100;
					Share.check = 1;
					Share.inputNum = 0;
				} else if(Share.inputNum != 0) {	//어떤 입력도 없을 경우의 수행
					Share.check = 2;
					Share.inputNum = 0;
				}
			}
			if(Share.gameCount != -1) {
				molegameJNI.textlcdClear();
				molegameJNI.textlcdPrint1Line("Player : " + Share.name);
				molegameJNI.textlcdPrint2Line("Playing.. " + --Share.gameCount);
			}
		}
		//모든 게임이 끝났을 경우
		molegameJNI.textlcdClear();
		if(Share.gameCount == 0) {
			molegameJNI.textlcdPrint1Line("Total score : ");
			molegameJNI.textlcdPrint2Line(""+Share.score);
			
			//서버에 기록을 저장하기 위함
			try {
				Socket sock = new Socket(serverIP, serverPort);
				PrintWriter sock_out = new PrintWriter(sock.getOutputStream(), true);
				BufferedReader sock_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				sock_out.println(Share.name +","+ Share.score);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(Share.gameCount == -1)	{//8번 이상 잘못 눌렀을 경우
			molegameJNI.textlcdPrint2Line("Game Over");
		}
		

		molegameJNI.piezoWrite((char)0x21);
		try { Thread.sleep(300); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x15);
		try { Thread.sleep(300); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x13);
		try { Thread.sleep(300); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x11);
		try { Thread.sleep(500); } catch(Exception e) {}
		molegameJNI.piezoWrite((char)0x00);
		
		molegameJNI.dotmatrixClose();
		
		
		
	}

	//두더지를 만들기 위해 도트매트릭스에 출력할 값을 생성
	private Result makeMole() {
		Result result;
		String str = null;

		int hex_base[] = { 0x3f, 0x21, 0x21, 0x21, 0x21, 0x3f };
		int mod_col[] = { 0x23, 0x25, 0x29, 0x31 };
		int row, col;

		Random rand = new Random();
		col = rand.nextInt(4) + 1;
		row = rand.nextInt(4);

		hex_base[col] = mod_col[row];
		for(int i = 0; i < 6; i++) {
			String tmp = new String();
			tmp = Integer.toHexString(hex_base[i]);
			if(tmp.length() < 2) tmp += "0";
			if(i == 0) str = tmp;
			else str += tmp;
		}

		result = new Result(row*4+col, "0000" + str + "0000");

		return result;
	}

	//도트매트릭스에 출력할 값과 두더지의 위치를 저장하기 위한 클래스
	private class Result {
		private int ansPosition;
		private String dotShape;

		public Result(int ansPos, String dotShape) {
			this.ansPosition = ansPos;
			this.dotShape= dotShape;
		}
		public void setDotShape(String dotShape) {
			this.dotShape = dotShape;
		}
		public void setAnsPosition(int ansPosition) {
			this.ansPosition = ansPosition;
		}
		public int getAnsPosition() {
			return ansPosition;
		}

		public String getDotShape() {
			return dotShape;
		}
	}
}
