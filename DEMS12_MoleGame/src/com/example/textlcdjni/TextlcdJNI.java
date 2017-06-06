package com.example.textlcdjni;

public class TextlcdJNI {
	static {
		System.loadLibrary("textlcdJNI");
	}
	
	public native void on();
	public native void off();
	public native void initialize();
	public native void clear();
	
	public native void print1Line(String str);
}
