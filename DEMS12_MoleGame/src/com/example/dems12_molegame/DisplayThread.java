package com.example.dems12_molegame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import com.example.molegamejni.MolegameJNI;

import android.util.Log;

/* ���׸�Ʈ, ��Ʈ��Ʈ������ ���� ���������� ����ؾ� �ϴ� �������ġ�� ó���ϴ� ��� 
 * ������ ��� ���*/
public class DisplayThread extends Thread {
	private final static int DOT_TIME = 100;
	private Result result;
	private MolegameJNI molegameJNI;

	final String serverIP = "172.20.10.2";
	final int serverPort = 12000;
	
	
	//��Ʈ��Ʈ������ ���׸�Ʈ��ġ�� ����ϱ� ���� JNI��ü�� ����
	public DisplayThread(MolegameJNI molegameJNI) {
		this.molegameJNI = molegameJNI;
	}

	//���������� ���� �־���ϴ� ��ġ�鿡 ���� �������� ����� ����
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
				if (result.ansPosition == Share.inputNum) {	//�δ����� ���� ����� ����
					result.setDotShape("00003f212121213f0000");
					result.setAnsPosition(-1);
					Share.score += 100;
					Share.check = 1;
					Share.inputNum = 0;
				} else if(Share.inputNum != 0) {	//� �Էµ� ���� ����� ����
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
		//��� ������ ������ ���
		molegameJNI.textlcdClear();
		if(Share.gameCount == 0) {
			molegameJNI.textlcdPrint1Line("Total score : ");
			molegameJNI.textlcdPrint2Line(""+Share.score);
			
			//������ ����� �����ϱ� ����
			try {
				Socket sock = new Socket(serverIP, serverPort);
				PrintWriter sock_out = new PrintWriter(sock.getOutputStream(), true);
				BufferedReader sock_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				sock_out.println(Share.name +","+ Share.score);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(Share.gameCount == -1)	{//8�� �̻� �߸� ������ ���
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

	//�δ����� ����� ���� ��Ʈ��Ʈ������ ����� ���� ����
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

	//��Ʈ��Ʈ������ ����� ���� �δ����� ��ġ�� �����ϱ� ���� Ŭ����
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
