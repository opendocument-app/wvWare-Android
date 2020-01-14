/*
 * ImageEmbedder.java
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


package com.viliussutkus89.android.wvware;

import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class ImageEmbedder {
  private static final String[] s_extensionWhitelist = {
    ".wmf", ".png"
  };

  static boolean embedImages(File inputFile, File outputFile, File imageDirectory) throws IOException {
    Map<String, Image> m_imagesAvailable = new HashMap<>();
    for (String img : imageDirectory.list()) {
      for (String ext : s_extensionWhitelist) {
        if (img.endsWith(ext)) {
          m_imagesAvailable.put(img, new Image(img, imageDirectory));
        }
      }
    }

    if (m_imagesAvailable.isEmpty()) {
      return false;
    }

    boolean imagesWereFoundInHtml = false;

    Pattern pattern = Pattern.compile("(<img.+?src=\")(.+?)\"");

    BufferedReader r = new BufferedReader(new FileReader(inputFile));
    try {
      OutputStream output = new FileOutputStream(outputFile);
      OutputStreamWriter outputWriter = new OutputStreamWriter(output);
      try {
        String line;
        while (null != (line = r.readLine())) {
          Matcher m = pattern.matcher(line);
          int processedInThisLine = 0;
          while (m.find()) {
            String prefix = m.group(1);
            String filename = m.group(2);

            int startOfMatch = m.start();
            int endOfMatch = m.end();

            int prefixLen = startOfMatch - processedInThisLine;
            if (prefixLen > 0) {
              outputWriter.write(line, processedInThisLine, prefixLen);
            }
            outputWriter.write(prefix);

            try {
              m_imagesAvailable.get(filename).outputHtmlSrc(output, outputWriter);
              imagesWereFoundInHtml = true;
            } catch (NullPointerException e) {
              outputWriter.write(filename);
            }

            outputWriter.write('"');

            processedInThisLine = endOfMatch;
          }

          int suffixLen = line.length() - processedInThisLine;
          if (suffixLen > 0) {
            outputWriter.write(line, processedInThisLine, suffixLen);
          }
          outputWriter.write("\r\n");
        }
      } finally {
        outputWriter.close();
      }
    } finally {
      r.close();
    }

    return imagesWereFoundInHtml;
  }

  private static class Image {
    private String m_filename;
    private File m_file;

    Image(String filename, File imagesDirectory) {
      this.m_filename = filename;
      this.m_file = new File(imagesDirectory, filename);
    }

    private String getMime() {
      // @TODO: Maybe use file libmagic??
      if (m_filename.endsWith("wmf")) {
        return "image/wmf";
      } else {
        return "image/png";
      }
    }

    void outputHtmlSrc(OutputStream output, OutputStreamWriter outputStreamWriter) throws IOException {
      outputStreamWriter.write("data:" + getMime() + ";base64,");
      outputStreamWriter.flush();

      Base64OutputStream b64output = new Base64OutputStream(output, Base64.NO_CLOSE);
      try {
        InputStream input = new FileInputStream(m_file);
        try {
          int bufSize = 1024 * 512;
          byte[] buffer = new byte[bufSize];
          int haveRead;
          while (-1 != (haveRead = input.read(buffer))) {
            b64output.write(buffer, 0, haveRead);
          }
        } finally {
          input.close();
        }
      } finally {
        b64output.close();
      }
      output.flush();
    }
  }
}
