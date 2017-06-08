package com.example.molegamejni;

public class LedJNI {
	static {
		System.loadLibrary("ledJNI");
	}
	
	public native void on(char data);
}
