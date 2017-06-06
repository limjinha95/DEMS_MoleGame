package com.example.ledjni;

public class LedJNI {
	static {
		System.loadLibrary("ledJNI");
	}
	
	public native void on(char data);
}
