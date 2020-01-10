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

#include <cstdlib>
#include <cstring>
#include <unistd.h>
#include <sys/wait.h>
#include <android/log.h>
#include <jni.h>
#include "CCharGC.h"

static bool forkBeforeConverting = true;

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

JNIEXPORT void JNICALL
Java_com_viliussutkus89_android_wvware_wvWare_setNoForking(JNIEnv *env, jobject) {
  forkBeforeConverting = false;
}

JNIEXPORT jint JNICALL
Java_com_viliussutkus89_android_wvware_wvWare__1convertToHTML(JNIEnv *env, jobject,
                                                              jstring input_file,
                                                              jstring output_dir,
                                                              jstring output_file,
                                                              jstring password_) {
  CharGC inputFile(env, input_file);
  CharGC outputDir(env, output_dir);
  CCharGC outputFile(env, output_file);
  CCharGC password(env, password_);

  int retVal = 1;

  // Forking is needed because I don't want to redirect stdout of the whole app
  pid_t pid = 0;
  if (forkBeforeConverting) {
    pid = fork();
    if (0 < pid) {
      int waitStatus;
      waitpid(pid, &waitStatus, 0);
      if (WIFEXITED(waitStatus)) {
        retVal = WEXITSTATUS(waitStatus);
      } else {
        retVal = 4;
      }
    } else if (-1 == pid) {
      __android_log_print(ANDROID_LOG_ERROR, "wvWare-Android", "Failed to fork!");
      return 3;
    }
  }

  if (0 == pid) {
    // wvWare prints the html to stdout
    // wvHtml is a shell script that pipes wvWare output to file
    // Would prefer to write to file directly,
    freopen(outputFile.c_str(), "w+", stdout);

    retVal = convert(inputFile.c_str(), outputDir.c_str(), password.c_str());

    if (forkBeforeConverting) {
      exit(retVal);
    } else {
      fclose(stdout);
#if __ANDROID_API__ >= __ANDROID_API_M__
      stdout = fdopen(STDOUT_FILENO, "w");
#else
      __sF[1] = *fdopen(STDOUT_FILENO, "w");
#endif
    }
  }

  return retVal;
}

}
