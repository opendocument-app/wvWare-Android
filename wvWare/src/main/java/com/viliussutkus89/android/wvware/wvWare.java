/*
 * wvWare.java
 *
 * Copyright (C) 2020, 2022 ViliusSutkus89.com
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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getkeepsafe.relinker.ReLinker;
import com.viliussutkus89.android.assetextractor.AssetExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public final class wvWare {
  private static final String s_TAG = "wvWare-Android";

  private File m_outputDir;
  private File p_inputDOC;
  private File p_outputHTML;
  private String p_password = "";
  private boolean p_isNoGraphicsMode = false;

  public static class ConversionFailedException extends Exception {
    public ConversionFailedException(String errorMessage) {
      super(errorMessage);
    }
  }

  public static class PasswordRequiredException extends ConversionFailedException {
    public PasswordRequiredException(String errorMessage) {
      super(errorMessage);
    }
  }

  public static class WrongPasswordException extends ConversionFailedException {
    public WrongPasswordException(String errorMessage) {
      super(errorMessage);
    }
  }

  public wvWare(@NonNull Context ctx) {
    init(ctx);
  }

  private synchronized void init(@NonNull Context ctx) {
    ReLinker.recursively().loadLibrary(ctx, "wvware-android");

    EnvVar.set("TMPDIR", ctx.getCacheDir().getAbsolutePath());

    LegacyCleanup.cleanup(ctx);

    AssetExtractor ae = new AssetExtractor(ctx.getAssets()).setNoOverwrite();
    setDataDir(ae.extract(ctx.getFilesDir(), "wv").getAbsolutePath());

    this.m_outputDir = new File(ctx.getCacheDir(), "wvWare");
    this.m_outputDir.mkdir();
  }

  public wvWare setInputDOC(@NonNull File inputDOC) {
    this.p_inputDOC = inputDOC;
    return this;
  }

  public wvWare setOutputHtml(@Nullable File outputHtml) {
    this.p_outputHTML = outputHtml;
    return this;
  }

  public wvWare setPassword(@NonNull String password) {
    this.p_password = password;
    return this;
  }

  public wvWare setNoGraphicsMode() {
    this.p_isNoGraphicsMode = true;
    return this;
  }

  public static String generateOutputFilename(String inputFilename) {
    String inputFilenameNoExt = inputFilename;
    if (inputFilenameNoExt.endsWith(".doc")) {
      inputFilenameNoExt = inputFilenameNoExt.substring(0, inputFilenameNoExt.length() - 4);
    }
    return inputFilenameNoExt + ".html";
  }

  public File convertToHTML() throws PasswordRequiredException, WrongPasswordException,
    ConversionFailedException, FileNotFoundException, IOException {
    if (null == this.p_inputDOC) {
      throw new ConversionFailedException("No Input DOC given!");
    }

    if (!this.p_inputDOC.exists()) {
      throw new FileNotFoundException();
    }

    String filename = removeExtensionFromFilename();
    File outputFile = this.p_outputHTML;
    if (null == outputFile) {
      outputFile = generateUniqueFile(filename, ".html");
    }

    File imagesDir = generateUniqueFolder(filename);

    int retVal = _convertToHTML(this.p_inputDOC.getAbsolutePath(), outputFile.getAbsolutePath(),
      imagesDir.getAbsolutePath(), this.p_password, this.p_isNoGraphicsMode);

    if (0 != retVal) {
      outputFile.delete();
      removeFileOrDirectoryRecursively(imagesDir);
      if (100 == retVal) {
        throw new PasswordRequiredException("Password is required to decrypt this encrypted document!");
      } else if (101 == retVal) {
        throw new WrongPasswordException("Wrong password is supplied to decrypt this encrypted document");
      } else {
        throw new ConversionFailedException("Return value from wvWare: " + retVal);
      }
    }

    if (this.p_isNoGraphicsMode) {
      try {
        File htmlWithImagesEmbedded = generateUniqueFile(outputFile.getName(), ".imagesEmbedded.html");
        if (ImageEmbedder.embedImages(outputFile, htmlWithImagesEmbedded, imagesDir)) {
          htmlWithImagesEmbedded.renameTo(outputFile);
        } else {
          htmlWithImagesEmbedded.delete();
        }
      } catch (IOException e) {
        e.printStackTrace();
        Log.e(s_TAG, "Failed to embed images");
      }
    }

    // Clean up extracted images from cache
    removeFileOrDirectoryRecursively(imagesDir);

    return outputFile;
  }

  private void removeFileOrDirectoryRecursively(File node) {
    String[] subNodes = node.list();
    if (null != subNodes) {
      for (String subNode: subNodes) {
        removeFileOrDirectoryRecursively(new File(node, subNode));
      }
    }
    node.delete();
  }

  private String removeExtensionFromFilename() {
    String filename = this.p_inputDOC.getName();
    int pos = filename.lastIndexOf('.');
    if (0 < pos) {
      return filename.substring(0, pos);
    }
    return filename;
  }

  private File generateUniqueFolder(String prefix) {
    File result = new File(this.m_outputDir, prefix);
    for (int i = 0; !result.mkdir(); i++) {
      result = new File(this.m_outputDir, prefix + "-" + i);
    }
    return result;
  }

  private File generateUniqueFile(String prefix, String suffix) throws IOException {
    File result = new File(this.m_outputDir, prefix + suffix);
    for (int i = 0; !result.createNewFile(); i++) {
      result = new File(this.m_outputDir, prefix + "-" + i + suffix);
    }
    return result;
  }

  private native void setDataDir(String dataDir);

  private native int _convertToHTML(String inputFile, String outputFile, String imagesDir, String password, boolean isNoGraphicsMode);
}
