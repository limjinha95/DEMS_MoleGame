#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <jni.h>
#include "textlcd.h"

#define FULL_LED1 9
#define FULL_LED2 8
#define FULL_LED3 7
#define FULL_LED4 6
#define ALL_LED 5

static int dotmatrixFD;
static int keypadFD;
static int piezoFD;
static int fullcolorledFD;
static int textlcdFD;
static int segmentFD;
static int ledFD;

/*******************************Dotmatrix***********************************/
JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_dotmatrixOpen
(JNIEnv * env, jobject obj){
	dotmatrixFD = open("/dev/fpga_dotmatrix", O_RDWR | O_SYNC);
	assert(dotmatrixFD != -1);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_dotmatrixClose
(JNIEnv * env, jobject obj){
	close(dotmatrixFD);
	dotmatrixFD = 0;
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_dotmatrixControl
(JNIEnv * env, jobject obj, jstring str){
	const char *pStr;
	int len;

	pStr = (*env)->GetStringUTFChars(env, str, 0);
	len = (*env)->GetStringLength(env, str);
	write(dotmatrixFD, pStr, len);

	(*env)->ReleaseStringUTFChars(env, str, pStr);
}
/*----------------------------------------------------------------------------*/


/******************************Piezo***************************************/

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_piezoOpen
(JNIEnv * env, jobject obj){
	piezoFD = open("/dev/fpga_piezo", O_WRONLY);
	assert(piezoFD != -1);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_piezoClose
(JNIEnv * env, jobject obj){
	close(piezoFD);
	piezoFD = 0;
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_piezoWrite
(JNIEnv * env, jobject obj, jchar data){
	write(piezoFD, &data, 1);
}
/*----------------------------------------------------------------------------*/


/******************************Fullcolorled***********************************/
JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_fullcolorledOpen
(JNIEnv * env, jobject obj){
	fullcolorledFD = open("/dev/fpga_fullcolorled", O_WRONLY);
	assert(fullcolorledFD != -1);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_FLEDControl
(JNIEnv * env, jobject obj, jint led_num, jint val1, jint val2, jint val3){
	char buf[3];
	int pos[5] = {FULL_LED1, FULL_LED2, FULL_LED3, FULL_LED4, ALL_LED};

	ioctl(fullcolorledFD, pos[(int)led_num]);
	buf[0] = val1;
	buf[1] = val2;
	buf[2] = val3;
	write(fullcolorledFD, buf, 3);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_fullcolorledClose
(JNIEnv * env, jobject obj){
	close(fullcolorledFD);
	fullcolorledFD = 0;
}
/*----------------------------------------------------------------------------*/


/*********************************Textlcd*************************************/
JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_textlcdOn
(JNIEnv * env, jobject obj){
	textlcdFD = open("/dev/fpga_textlcd", O_WRONLY);
	assert(textlcdFD != -1);
	ioctl(textlcdFD, TEXTLCD_ON);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_textlcdInitialize
(JNIEnv * env, jobject obj){
	textlcdFD = open("/dev/fpga_textlcd", O_WRONLY);
	assert(textlcdFD != -1);
	ioctl(textlcdFD, TEXTLCD_INIT);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_textlcdOff
(JNIEnv * env, jobject obj){
	ioctl(textlcdFD, TEXTLCD_OFF);
	close(textlcdFD);
	textlcdFD = 0;
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_textlcdClear
(JNIEnv * env, jobject obj){
	ioctl(textlcdFD, TEXTLCD_CLEAR);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_textlcdPrint1Line
(JNIEnv * env, jobject obj, jstring msg){
	const char *str;

	str = (*env)->GetStringUTFChars(env, msg, 0);
	ioctl(textlcdFD, TEXTLCD_LINE1);
	write(textlcdFD, str, strlen(str));
	(*env)->ReleaseStringUTFChars(env, msg, str);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_textlcdPrint2Line
(JNIEnv * env, jobject obj, jstring msg){
	const char *str;

	str = (*env)->GetStringUTFChars(env, msg, 0);
	ioctl(textlcdFD, TEXTLCD_LINE2);
	write(textlcdFD, str, strlen(str));
	(*env)->ReleaseStringUTFChars(env, msg, str);
}
/*----------------------------------------------------------------------------*/


/*********************************Segment************************************/
JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_segmentOpen
(JNIEnv * env, jobject obj){
	segmentFD = open("/dev/fpga_segment", O_WRONLY);
	assert(segmentFD != -1);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_segmentPrint
(JNIEnv * env, jobject obj, jint num){
	char nums[6];
	sprintf(nums, "%06d", num);
	write(segmentFD, nums, 6);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_segmentClose
(JNIEnv * env, jobject obj){
	close(segmentFD);
	segmentFD = 0;
}
/*----------------------------------------------------------------------------*/


/*******************************Led******************************************/
JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_ledOpen
(JNIEnv * env, jobject obj){
	ledFD = open("/dev/fpga_led", O_WRONLY);
	assert(ledFD != -1);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_ledOn
(JNIEnv * env, jobject obj, jchar data){
	write(ledFD, &data, 1);
}

JNIEXPORT void JNICALL Java_com_example_molegamejni_MolegameJNI_ledClose
(JNIEnv * env, jobject obj){
	close(ledFD);
	ledFD = 0;
}
/*----------------------------------------------------------------------------*/

