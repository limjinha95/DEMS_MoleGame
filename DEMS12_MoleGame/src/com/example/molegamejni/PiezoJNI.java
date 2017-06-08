package com.example.molegamejni;

public class PiezoJNI {
	static {
		System.loadLibrary("piezoJNI");
	}
	
	public native void open();
	public native void write(char data);
	public native void close();
}
