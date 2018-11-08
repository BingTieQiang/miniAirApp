//
// Created by ruiao on 2018/10/19.
//
#include <jni.h>
#include <string>
#include <stdlib.h>
#include <string.h>
#include "strtool.h"
#include "com_clj_blesample_ariset_AirSetActivity.h"
#include "com_clj_blesample_set_ZeroActivity.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "JNI", __VA_ARGS__)

extern "C" {

/*
 * Class:     com_clj_blesample_ariset_AirSetActivity
 * Method:    getStr
 * Signature: ([B)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_clj_blesample_ariset_AirSetActivity_getStr
        (JNIEnv *env, jobject obj, jbyteArray arr_) {
//    std::string hello = "Hello from C++";
    std::string hello;
    jbyte *bt1 = env->GetByteArrayElements(arr_, false);


    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(arr_, 0);
    int chars_len = env->GetArrayLength(arr_);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;

    env->ReleaseByteArrayElements(arr_, bytes, 0);

    hello = chars;

    return env->NewStringUTF(hello.c_str());
}
}
extern "C" {
JNIEXPORT jstring JNICALL Java_com_clj_blesample_set_ZeroActivity_getStr1
        (JNIEnv *env, jobject obj, jbyteArray arr_) {
//    std::string hello = "Hello from C++";
    std::string hello;
    jbyte *bt1 = env->GetByteArrayElements(arr_, false);


    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(arr_, 0);
    int chars_len = env->GetArrayLength(arr_);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;

    env->ReleaseByteArrayElements(arr_, bytes, 0);

    hello = chars;

    return env->NewStringUTF(hello.c_str());
}


}



