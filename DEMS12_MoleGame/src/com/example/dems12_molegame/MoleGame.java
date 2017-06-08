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
				final int score = 0;
				
				//게임로직
				// 게임시작하면 lcd창 지우고 이름 출력, 피에조 시작 알림,segment 0으로 초기화
				textlcdJNI.clear();
				textlcdJNI.print1Line(name);	
				piezoJNI.open();	
				segmentJNI.open();
				segmentJNI.print(score);
				
				
				
				
				
				
				
				
				
				
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