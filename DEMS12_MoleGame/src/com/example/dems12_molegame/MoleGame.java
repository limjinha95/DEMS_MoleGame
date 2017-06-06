package com.example.dems12_molegame;

import com.example.dems12_molegame.R;
import com.example.textlcdjni.TextlcdJNI;
import com.example.piezojni.PiezoJNI;
import com.example.fullcolorledjni.FullcolorledJNI;
import com.example.ledjni.LedJNI;
import com.example.segmentjni.SegmentJNI;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MoleGame extends Activity{
	
	private TextlcdJNI textlcdJNI = new TextlcdJNI();
	private PiezoJNI piezoJNI = new PiezoJNI();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final EditText edittext = (EditText)findViewById(R.id.edittext);
		final Button startbtn = (Button)findViewById(R.id.startbtn);

		startbtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String str1 = edittext.getText().toString();
				textlcdJNI.clear();
				textlcdJNI.print1Line(str1);
				piezoJNI.open();
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