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

static FILE * g_htmlOutputFileHandle = nullptr;

extern "C" int no_graphics;

extern "C" {

char *s_WVDATADIR = NULL;
char *s_HTMLCONFIG = NULL;

int convert(char *inputFile, char *outputDir, const char *password);

char *strdup_and_append(const char *a, const char *b) {
  const size_t szA = strlen(a);
  const size_t szB = strlen(b);
  char *buf = new char[szA + szB + 1];
  strcpy(buf, a);
  strcpy(buf + szA, b);
  buf[szA + szB] = '\0';
  return buf;
}

char *strdup_and_append_twice(const char *a, const char *b, const char *c) {
  const size_t szA = strlen(a);
  const size_t szB = strlen(b);
  const size_t szC = strlen(c);

  char *buf = new char[szA + szB + szC + 1];
  strcpy(buf, a);
  strcpy(buf + szA, b);
  strcpy(buf + szA + szB, c);
  buf[szA + szB + szC] = '\0';
  return buf;
}

JNIEXPORT void JNICALL
Java_com_viliussutkus89_android_wvware_wvWare_setDataDir(JNIEnv *env, jobject, jstring data_dir) {
  CCharGC dataDir(env, data_dir);

  if (NULL != s_WVDATADIR) {
    free(s_WVDATADIR);
  }
  s_WVDATADIR = strdup(dataDir.c_str());

  if (NULL != s_HTMLCONFIG) {
    free(s_HTMLCONFIG);
  }
  s_HTMLCONFIG = strdup_and_append(dataDir.c_str(), "/wvHtml.xml");
}

int printfRedirect(const char *fmt, ...) {
  int retVal = 0;
  if (nullptr != g_htmlOutputFileHandle) {
    std::va_list args;
    va_start(args, fmt);
    retVal = vfprintf(g_htmlOutputFileHandle, fmt, args);
    va_end(args);
  } else {
    __android_log_print(ANDROID_LOG_ERROR, "wvWare-Android", "Output file handle unset!");
  }
  return retVal;
}

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

  int retVal = convert(inputFile.c_str(), imagesDir.c_str(), password.c_str());

  fclose(g_htmlOutputFileHandle);
  g_htmlOutputFileHandle = nullptr;

  return retVal;
}

}
