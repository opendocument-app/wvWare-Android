/*
 * wvWare.java
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

import android.content.Context;

import androidx.annotation.NonNull;

import com.viliussutkus89.android.assetextractor.AssetExtractor;

import java.io.File;
import java.io.FileNotFoundException;

public final class wvWare {
  static {
    System.loadLibrary("wvware-android");
  }

  private File m_outputDir;

  private File p_inputDOC;

  private String p_password = "";

  public static class ConversionFailedException extends Exception {
    public ConversionFailedException(String errorMessage) {
      super(errorMessage);
    }
  }

  public wvWare(@NonNull Context ctx) {
    init(ctx);
  }

  private synchronized void init(@NonNull Context ctx) {
    AssetExtractor ae = new AssetExtractor(ctx.getAssets()).setNoOverwrite();
    setDataDir(ae.extract(ctx.getFilesDir(), "wv").getAbsolutePath());

    this.m_outputDir = new File(ctx.getCacheDir(), "wvWare");
    this.m_outputDir.mkdir();
  }

  public wvWare setInputDOC(@NonNull File inputDOC) {
    this.p_inputDOC = inputDOC;
    return this;
  }

  public wvWare setPassword(@NonNull String password) {
    this.p_password = password;
    return this;
  }

  public wvWare setNoForking(@NonNull boolean redirectStdoutToDevNullAfterConversion) throws IllegalArgumentException {
    if (!redirectStdoutToDevNullAfterConversion) {
      throw new IllegalArgumentException();
    }
    setNoForking();
    return this;
  }

  public File convertToHTML() throws ConversionFailedException, FileNotFoundException {
    if (null == this.p_inputDOC) {
      throw new ConversionFailedException("No Input DOC given!");
    }

    if (!this.p_inputDOC.exists()) {
      throw new FileNotFoundException();
    }

    String inputFilenameNoDOCExt = this.p_inputDOC.getName();
    if (inputFilenameNoDOCExt.endsWith(".doc")) {
      inputFilenameNoDOCExt = inputFilenameNoDOCExt.substring(0, inputFilenameNoDOCExt.length() - 4);
    }

    File outputDir = new File(this.m_outputDir, inputFilenameNoDOCExt);
    for (int i = 0; !outputDir.mkdir(); i++) {
      outputDir = new File(this.m_outputDir, inputFilenameNoDOCExt + "-" + i);
    }

    File outputFile = new File(outputDir, inputFilenameNoDOCExt + ".html");
    int retVal = _convertToHTML(this.p_inputDOC.getAbsolutePath(), outputDir.getAbsolutePath(), outputFile.getAbsolutePath(), this.p_password);
    if (0 != retVal) {
      outputFile.delete();
      throw new ConversionFailedException("Conversion failed. Return value from wvWare: " + retVal);
    }

    return outputFile;
  }

  private native void setDataDir(String dataDir);

  private native int _convertToHTML(String inputFile, String outputDir, String outputFile, String password);

  private native void setNoForking();
}
