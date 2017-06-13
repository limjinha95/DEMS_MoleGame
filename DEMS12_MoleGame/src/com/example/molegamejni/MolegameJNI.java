package com.example.molegamejni;

public class MolegameJNI {
	static {
		System.loadLibrary("molegameJNI");
	}
	
	public native void dotmatrixOpen();
	public native void dotmatrixClose();
	public native void dotmatrixControl(String str);
	
	public native void keypadOpen();
	public native void keypadClose();
	public native int keypadRead();
	
	public native void piezoOpen();
	public native void piezoClose();
	public native void piezoWrite(char data);
	
	public native void fullcolorledOpen();
	public native void FLEDControl(int ledNum, int red, int green, int blue);
	public native void fullcolorledClose();
	
	public native void textlcdOn();
	public native void textlcdInitialize();
	public native void textlcdOff();
	public native void textlcdClear();
	public native void textlcdPrint1Line(String str);
	public native void textlcdPrint2Line(String str);
	
	public native void segmentOpen();
	public native void segmentPrint(int num);
	public native void segmentClose();
	
	public native void ledOpen();
	public native void ledOn(char data);
	public native void ledClose();
}
