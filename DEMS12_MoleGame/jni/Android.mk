LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := molegameJNI
LOCAL_SRC_FILES := molegameJNI.c

include $(BUILD_SHARED_LIBRARY)
