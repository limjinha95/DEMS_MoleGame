package com.example.dems12_molegame;

public class Share {
	static int gameCount;
	static int inputNum;//�Է��� Ű�е��� ��ġ 1 ~ 16, �ȴ����� ��� 0
	static int check;	//0 : Ȯ�ξ��� , 1 : ������, 2 : Ʋ����
	static int score;	//����
	static int fault;	//Ʋ�� Ƚ��
	static String name;
	
	public static void init() {
		gameCount = 5;
		inputNum = 0;
		check = 0;
		score = 5000;
		fault = 0;
		name = "";
	}
}
