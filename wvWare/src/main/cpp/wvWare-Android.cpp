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

#include <cstdarg>
#include <cstdlib>
#include <cstring>
#include <android/log.h>
#include <jni.h>
#include "CCharGC.h"
#include <wv/wv.h>

extern "C" {

JNIEXPORT jint JNICALL
Java_com_viliussutkus89_android_wvware_wvWare__1convertToHTML(JNIEnv *env, jobject,
                                                              jstring input_file,
                                                              jstring output_file,
                                                              jstring images_dir,
                                                              jstring password_,
                                                              jboolean is_no_graphics_mode) {
  CharGC inputFile(env, input_file);
  CCharGC outputFile(env, output_file);
  CharGC imagesDir(env, images_dir);
  CCharGC password(env, password_);

  no_graphics = is_no_graphics_mode == JNI_TRUE ? 1 : 0;

  g_htmlOutputFileHandle = fopen(outputFile.c_str(), "w");

  int retVal = wvHtml_convert(inputFile.c_str(), imagesDir.c_str(), password.c_str());

  fclose(g_htmlOutputFileHandle);
  g_htmlOutputFileHandle = nullptr;

  return retVal;
}

}
