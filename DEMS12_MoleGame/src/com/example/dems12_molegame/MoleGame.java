package com.example.dems12_molegame;

import com.example.dems12_molegame.*;
import com.example.molegamejni.MolegameJNI;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//뷰 초기화
		initView();
		
		//스타트 버튼눌렀을 경우
		startbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Share.name = edittext.getText().toString();
				Intent intent = new Intent(MoleGame.this, ButtonActivity.class);
				startActivity(intent);	//망치역할의 버튼액티비티를 띄움
			}
		});
	}

	private void initView() {
		edittext = (EditText)findViewById(R.id.editname);
		startbtn = (Button)findViewById(R.id.startbtn);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mole_game, menu);
		return true;
	}
}

