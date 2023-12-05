#include <jni.h>
#include <string>
#include <fstream>
#include <android/log.h>
#include <sstream>

#define TAG "FileAccessApp - C++"

extern "C" JNIEXPORT jstring JNICALL
Java_vendor_alvenan_fileaccessjni_MainActivity_readFile(
        JNIEnv *env,
        jobject,
        jstring yourFilepath) {

    jstring ret;

    std::ifstream file(env->GetStringUTFChars(yourFilepath, 0));
    jstring status_msg;
    std::ostringstream buf;

    if (file.is_open()) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "Openned file succesfully! Path: %s",
                            env->GetStringUTFChars(yourFilepath, 0));
        buf << file.rdbuf();
        ret = env->NewStringUTF(buf.str().c_str());
        file.close();
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Cannot open the file! Path: %s",
                            env->GetStringUTFChars(yourFilepath, 0));
        ret = env->NewStringUTF("Cannot open the file!");
    }
    return ret;
}

extern "C" JNIEXPORT jstring JNICALL
Java_vendor_alvenan_fileaccessjni_MainActivity_writeFile(
        JNIEnv *env,
        jobject,
        jstring yourFilepath,
        jstring jtext) {

    std::ofstream file(env->GetStringUTFChars(yourFilepath, 0));
    jstring status_msg;

    if (file.is_open()) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "Openned file succesfully! Path: %s",
                            env->GetStringUTFChars(yourFilepath, 0));
        file << env->GetStringUTFChars(jtext, 0);
        file.close();
        status_msg = env->NewStringUTF("Message successfully written!");
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Cannot open the file! Path: %s",
                            env->GetStringUTFChars(yourFilepath, 0));
        status_msg = env->NewStringUTF("Cannot open the file!");
    }
    return status_msg;
}

extern "C" JNIEXPORT jstring JNICALL
Java_vendor_alvenan_fileaccessjni_MainActivity_removeFile(
        JNIEnv *env,
        jobject,
        jstring yourFilepath) {

    jstring ret;
    jstring status_msg;

    if (!std::remove(env->GetStringUTFChars(yourFilepath, 0))) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "Removed file successfully! Path: %s",
                            env->GetStringUTFChars(yourFilepath, 0));
        ret = env->NewStringUTF("Removed file successfully!");
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Cannot delete the file! Path: %s",
                            env->GetStringUTFChars(yourFilepath, 0));
        ret = env->NewStringUTF("Cannot remove the file!");
    }
    return ret;
}