/*
 * Copyright 2011-2016 Asakusa Framework Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include "env.hpp"

#include <iostream>
#include <m3bp/m3bp.hpp>

static JavaVM *_java_vm;
__thread bool _java_attached = false;

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    _java_vm = vm;
    return JNI_VERSION_1_8;
}

JavaVM *java_vm() {
    return _java_vm;
}

JNIEnv *java_env() {
    JNIEnv *env;
    jint result = _java_vm->GetEnv((void**) &env, JNI_VERSION_1_8);
    if (result == JNI_OK) {
        return env;
    } else {
        throw BridgeError("failed to obtain JVM");
    }
}

JNIEnv *java_attach() {
    JNIEnv *env;
    jint result;
    result = _java_vm->GetEnv((void**) &env, JNI_VERSION_1_8);
    if (result == JNI_OK) {
        return env;
    }
    JavaVMAttachArgs thread_args = {
        .version = JNI_VERSION_1_8,
        .name = nullptr,
        .group = nullptr
    };
    result = _java_vm->AttachCurrentThreadAsDaemon((void**) &env, &thread_args);
    if (result == JNI_OK) {
        _java_attached = true;
        return env;
    } else {
        throw BridgeError("failed to attach to JVM");
    }
}

void java_detach() {
    if (!_java_attached) {
        return;
    } else {
        _java_vm->DetachCurrentThread();
        _java_attached = false;
    }
}
