package com.example.dems12_molegame;

import com.example.dems12_molegame.R;
import com.example.molegamejni.FullcolorledJNI;
import com.example.molegamejni.LedJNI;
import com.example.molegamejni.PiezoJNI;
import com.example.molegamejni.SegmentJNI;
import com.example.molegamejni.TextlcdJNI;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MoleGame extends Activity{
	
	private TextlcdJNI textlcdJNI = new TextlcdJNI();
	private PiezoJNI piezoJNI = new PiezoJNI();
	private FullcolorledJNI fullcolorledJNI = new FullcolorledJNI();
	private LedJNI ledJNI = new LedJNI();
	private SegmentJNI segmentJNI = new SegmentJNI();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final EditText edittext = (EditText)findViewById(R.id.editname);
		final Button startbtn = (Button)findViewById(R.id.startbtn);
		

		startbtn.setOnClickListener(new Button.OnClickListener() {

			@Override
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

		});

	}
	
	@Override
	protected void onResume() {
		textlcdJNI.initialize();
		super.onResume();
	}

	@Override
	protected void onPause() {
		textlcdJNI.off();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mole_game, menu);
		return true;
	}

}