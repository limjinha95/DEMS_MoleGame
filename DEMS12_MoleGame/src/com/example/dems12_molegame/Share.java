package com.example.dems12_molegame;

public class Share {
	static int gameCount;//게임 남은 횟수
	static int inputNum;//입력한 키패드의 위치 1 ~ 16, 안눌렀을 경우 0
	static int check;	//0 : 확인안함 , 1 : 맞췄음, 2 : 틀렸음
	static int score;	//점수
	static int fault;	//틀린 횟수
	static String name;	//플레이어 이름
	
	public static void init() {
		gameCount = 20;
		inputNum = 0;
		check = 0;
		score = 5000;
		fault = 0;
		name = "";
	}
}
