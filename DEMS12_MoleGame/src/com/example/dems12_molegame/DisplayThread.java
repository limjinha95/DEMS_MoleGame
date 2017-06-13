package com.example.dems12_molegame;

import java.util.Random;

import com.example.dotmatrixjni.DotMatrixJNI;
import com.example.molegamejni.MolegameJNI;

import android.util.Log;


public class DisplayThread extends Thread {
	private int gameCount;
	private Result result;
	
	public DisplayThread(MolegameJNI molegameJNI, int gameCount) {
		this.gameCount = gameCount;
		this.molegameJNI = molegameJNI;
	}
	
    @Override
    public void run() {
        try {
    		molegameJNI.dotmatrixOpen();

            for(int i = 0; i < gameCount; i++) {
                result = makeMole();
                for(int j = 0; j < 100; j++) {
                    molegameJNI.dotmatrixControl(result.getDotShape());
                    if (result.ansPosition == Share.inputNum) {
                        result.setDotShape("3f212121213f00000000");
                        Share.check = 1;
                    }
                }
            }
        } catch(Exception e) {}
        
        molegameJNI.dotmatrixClose();
    }

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
        for(int i = 0; i < 5; i++) {
        	String tmp = new String();
        	tmp = Integer.toHexString(hex_base[i]);
        	if(tmp.length() < 2) str += "0";
        	str += tmp;
        }
        
        result = new Result(row*4+col, str + "00000000");
        
        return result;
    }
    
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
    	public int getAnsPosition() {
    		return ansPosition;
    	}
    	
    	public String getDotShape() {
    		return dotShape;
    	}
    }
}
