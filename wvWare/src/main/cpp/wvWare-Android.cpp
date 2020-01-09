/*
 * wvWare-Android.cpp
 *
 * Copyright (C) 2020 Vilius Sutkus'89 <ViliusSutkus89@gmail.com>
 *
 * wvWare-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

#include <jni.h>
#include "CCharGC.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_viliussutkus89_android_wvware_wvWare_setDataDir(JNIEnv *env, jobject, jstring data_dir) {
  CCharGC dataDir(env, data_dir);

  // @TODO:
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_viliussutkus89_android_wvware_wvWare__1convertToHTML(JNIEnv *env, jobject,
                                                              jstring input_file,
                                                              jstring output_dir,
                                                              jstring password_) {

  CCharGC inputFile(env, input_file);
  CCharGC outputDir(env, output_dir);
  CCharGC password(env, password_);

  // @TODO:
  return -1;
}