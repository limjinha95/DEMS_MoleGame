package com.example.dems12_molegame;

import com.example.dems12_molegame.*;
import com.example.molegamejni.MolegameJNI;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MoleGame extends Activity{
	private Button startbtn;
	private EditText edittext;
	private MolegameJNI molegameJNI;
	Button button[] = new Button[16];
	char ledNum[] = {0xff, 0x7f, 0x3f, 0x1f, 0x0f, 0x07, 0x03, 0x01, 0x00};
	DisplayThread displayThread = null;
	
	Socket sock;
	BufferedReader sock_in;
	PrintWriter sock_out;
	EditText input;
	TextView output;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		molegameJNI = new MolegameJNI();
		initView();

		molegameJNI.textlcdClear();
		
		startbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(displayThread != null) return;

				Share.init();
				
				displayThread = new DisplayThread(molegameJNI);
				Share.name = edittext.getText().toString();
				startbtn.setText("Playing..");

				molegameJNI.FLEDControl(4, 100, 100, 100);
				molegameJNI.textlcdClear();
				molegameJNI.textlcdPrint1Line(Share.name);
				molegameJNI.textlcdPrint2Line("Playing.. " + Share.gameCount);
				molegameJNI.ledOn(ledNum[Share.fault]);

				displayThread.start();
			}
		});
	}

	private void initView() {
		edittext = (EditText)findViewById(R.id.editname);
		startbtn = (Button)findViewById(R.id.startbtn);

		int btnID[] = { R.id.button1, R.id.button2, R.id.button3, R.id.button4,
				R.id.button5, R.id.button6, R.id.button7, R.id.button8,
				R.id.button9, R.id.button10, R.id.button11, R.id.button12,
				R.id.button13, R.id.button14, R.id.button15, R.id.button16 };
		for(int i= 0; i < 16; i++) {
			button[i] = (Button)findViewById(btnID[i]);
		}
	}

	@Override
	protected void onResume() {
		//molegameJNI.keypadOpen();
		molegameJNI.piezoOpen();
		molegameJNI.fullcolorledOpen();
		molegameJNI.textlcdInitialize();
		molegameJNI.segmentOpen();
		molegameJNI.ledOpen();
	
		super.onResume();
		
		
	}

	@Override
	protected void onPause() {
		//molegameJNI.keypadClose();
		molegameJNI.piezoClose();
		molegameJNI.fullcolorledClose();
		molegameJNI.textlcdOff();
		molegameJNI.segmentClose();
		molegameJNI.ledClose();

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mole_game, menu);
		return true;
	}

	public void buttonPush(View v) {
		if(Share.gameCount <= 0) return;
		int num = Integer.parseInt(((Button)v).getText().toString());
		Share.inputNum = num;

		//if(Share.gameCount > 0) break;	//���� ����
		while(Share.check == 0);
		switch(Share.check) {
		case 1:	//������ ���
			molegameJNI.FLEDControl(4, 0, 0, 100);
			molegameJNI.piezoWrite((char)0x11);
			try { Thread.sleep(300); } catch(Exception e) {}
			molegameJNI.FLEDControl(4, 100, 100, 100);
			molegameJNI.piezoWrite((char)0x00);

			Share.check = 0;
			break;
		case 2:	//Ʋ���� ���
			Share.score -= 50;
			molegameJNI.ledOn(ledNum[++Share.fault]);
			molegameJNI.FLEDControl(4, 100, 0, 0);
			//molegameJNI.piezoWrite((char)0x13);
			try { Thread.sleep(300); } catch(Exception e) {}
			molegameJNI.FLEDControl(4, 100, 100, 100);
			//molegameJNI.piezoWrite((char)0x00);

			Share.check = 0;
			if(Share.fault == 8) Share.gameCount = -1;
	
			break;
		}

	}
}

