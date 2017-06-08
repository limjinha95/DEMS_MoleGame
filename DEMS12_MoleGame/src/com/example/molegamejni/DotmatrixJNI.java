package com.example.molegamejni;

public class DotmatrixJNI {
	static {
		System.loadLibrary("dotmatrixJNI");
	}
	
	public native void DotMatrixControl(String str);
}
