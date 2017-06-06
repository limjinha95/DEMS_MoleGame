package com.example.segmentjni;

public class SegmentJNI {
	static {
		System.loadLibrary("segmentJNI");
	}
	
	public native void open();
	public native void print(int num);
	public native void close();
}
