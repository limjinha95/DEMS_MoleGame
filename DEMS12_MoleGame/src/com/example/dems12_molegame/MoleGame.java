package com.example.dems12_molegame;

import com.example.dems12_molegame.*;
import com.example.molegamejni.MolegameJNI;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MoleGame extends Activity{
	private Button startbtn;
	private EditText edittext;
	private MolegameJNI molegameJNI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

<<<<<<< HEAD
		molegameJNI = new MolegameJNI();
		initView();
		
=======
		final EditText edittext = (EditText)findViewById(R.id.editname);
		final Button startbtn = (Button)findViewById(R.id.startbtn);
		

>>>>>>> master
		startbtn.setOnClickListener(new Button.OnClickListener() {
			@Override
<<<<<<< HEAD
			public void onClick(View v) {
				String name = edittext.getText().toString();
				startbtn.setText("Playing..");
				//게임로직
				// 게임시작하면 lcd창 지우고 이름 출력, 피에조 시작 알림,segment 0으로 초기화
				molegameJNI.textlcdClear();
				molegameJNI.textlcdPrint1Line(name);
				molegameJNI.segmentPrint(Share.score);
=======
			public void onClick(View arg0) {
				final String name = edittext.getText().toString();
				int score = 0;
				int count = 20;
				int red, green, blue;
				char piezoData;
				char ledData = (char)0;
				boolean res = true;
				
				//초기화
				textlcdJNI.on();
				textlcdJNI.clear();
				textlcdJNI.print1Line(name);
				piezoJNI.open();	
				segmentJNI.open();
				segmentJNI.print(score);
				
				while(count != 0) {
					
					//게임
					
					if(res) {	//점수증가, 파란불
						red = 0;
						green = 0;
						blue = 100;
						score = score + 10;
						
						
					}
					else {		//엘이디, 빨간불,
						red = 100;
						green = 0;
						blue = 0;
						ledJNI.on(ledData);
					}
					
					segmentJNI.print(score);
					fullcolorledJNI.FLEDControl(1, red, green, blue);
					fullcolorledJNI.FLEDControl(2, red, green, blue);
					fullcolorledJNI.FLEDControl(3, red, green, blue);
					fullcolorledJNI.FLEDControl(4, red, green, blue);
				}
				
				piezoJNI.write(piezoData);
				piezoJNI.close();
				segmentJNI.close();
				textlcdJNI.off();
				
			}
>>>>>>> master

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