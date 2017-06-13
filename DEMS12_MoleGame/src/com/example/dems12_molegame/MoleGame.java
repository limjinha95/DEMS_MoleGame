package com.example.dems12_molegame;

import com.example.dems12_molegame.*;
import com.example.molegamejni.MolegameJNI;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
	
	final String serverIP= "192.168.1.9";
	final int serverPort= 9555;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		molegameJNI = new MolegameJNI();
		initView();
		
		startbtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = edittext.getText().toString();
				startbtn.setText("Playing..");
				//게임로직
				// 게임시작하면 lcd창 지우고 이름 출력, 피에조 시작 알림,segment 0으로 초기화
				molegameJNI.textlcdClear();
				molegameJNI.textlcdPrint1Line(name);
				molegameJNI.segmentPrint(Share.score);

				(new DisplayThread(molegameJNI, 30)).start();
			}
		});

		while(true) {
				Share.inputNum = molegameJNI.keypadRead();

				switch(Share.check) {
				case 1:
					molegameJNI.FLEDControl(4, 100, 0, 0);
					molegameJNI.piezoWrite((char)0x11);
					molegameJNI.segmentPrint(++Share.score);
					break;
				case 0:
					molegameJNI.FLEDControl(4, 0, 0, 100);
					molegameJNI.piezoWrite((char)0x13);
					
					break;
				}
		}
	}

	private void initView() {
		edittext = (EditText)findViewById(R.id.editname);
		startbtn = (Button)findViewById(R.id.startbtn);
	}
	
	@Override
	protected void onResume() {
		molegameJNI.keypadOpen();
		molegameJNI.piezoOpen();
		molegameJNI.fullcolorledOpen();
		molegameJNI.textlcdInitialize();
		molegameJNI.segmentOpen();
		molegameJNI.ledOpen();
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		molegameJNI.keypadClose();
		molegameJNI.piezoClose();
		molegameJNI.fullcolorledClose();
		molegameJNI.textlcdOff();
		molegameJNI.segmentClose();
		molegameJNI.ledClose();
		
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mole_game, menu);
		return true;
	}
}