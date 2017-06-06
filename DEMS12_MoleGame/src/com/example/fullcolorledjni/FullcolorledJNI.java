package com.example.fullcolorledjni;

public class FullcolorledJNI {
	static {
		System.loadLibrary("fullcolorledJNI");
	}
	
	public native void FLEDControl(int ledNum, int red, int green, int blue); 
}
