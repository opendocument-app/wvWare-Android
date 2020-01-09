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
import android.os.Build;

import androidx.annotation.NonNull;

import com.viliussutkus89.android.assetextractor.AssetExtractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public final class wvWare {

  private String m_wvWareExe;

  private String m_tmpDir;
  private String m_dataDir;
  private File m_outputHtmlsDir;

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
    File filesDir = ctx.getFilesDir();
    File cacheDir = ctx.getCacheDir();

    File dataDir = new File(filesDir, "wv");

    AssetExtractor ae = new AssetExtractor(ctx.getAssets());
    ae.setNoOverwrite().extract(filesDir, "wv");

    this.m_dataDir = dataDir.getAbsolutePath();

    File tmpDir = new File(cacheDir, "wvWare-tmp");
    tmpDir.mkdir();
    this.m_tmpDir = tmpDir.getAbsolutePath();

    this.m_outputHtmlsDir = new File(this.m_tmpDir, "output-htmls");
    this.m_outputHtmlsDir.mkdir();

    String exeName = BuildConfig.DEBUG ? "wvWare-Debug.exe.not.so" : "wvWare.exe.not.so";

    // @TODO: outputDir may not be available
    File outputDir = new File("/data/local/tmp");
    File wvWareExe = ae.setOverwrite().extract(outputDir, getAbi() + "/" + exeName);

    this.m_wvWareExe =  wvWareExe.getAbsolutePath();
  }

  private String getAbi() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      // on newer Android versions, we'll return only the most important Abi version
      return Build.SUPPORTED_ABIS[0];
    }
    else {
      // on pre-Lollip versions, we got only one Abi
      return Build.CPU_ABI;
    }
  }

  public wvWare setInputDOC(@NonNull File inputDOC) {
    this.p_inputDOC = inputDOC;
    return this;
  }

  public wvWare setPassword(@NonNull String password) {
    this.p_password = password;
    return this;
  }

  public File convertToHTML() throws ConversionFailedException {
    if (null == this.p_inputDOC) {
      throw new ConversionFailedException("No Input DOC given!");
    }

    if (!this.p_inputDOC.exists()) {
      throw new ConversionFailedException("Input DOC does not exist!");
    }

    String inputFilenameNoDOCExt = this.p_inputDOC.getName();
    if (inputFilenameNoDOCExt.endsWith(".doc")) {
      inputFilenameNoDOCExt = inputFilenameNoDOCExt.substring(0, inputFilenameNoDOCExt.length() - 4);
    }

    File outputHtmlDir = new File(m_outputHtmlsDir, inputFilenameNoDOCExt);
    for (int i = 0; !outputHtmlDir.mkdir(); i++) {
      outputHtmlDir = new File(m_outputHtmlsDir, inputFilenameNoDOCExt + "-" + i);
    }
    File outputHtml = new File(outputHtmlDir, inputFilenameNoDOCExt + ".html");
    File conversionLog = new File(outputHtmlDir, inputFilenameNoDOCExt + ".log");

    ArrayList<String> args = new ArrayList<>(Arrays.asList(
      this.m_wvWareExe,
      "-x", "wvHtml.xml",
      "--tmpfiledir", this.m_tmpDir,
      "--assetdir", this.m_dataDir,
      "-d", outputHtmlDir.getAbsolutePath(),
      this.p_inputDOC.getAbsolutePath()
    ));

    if (!this.p_password.isEmpty()) {
      args.add("-p");
      args.add(this.p_password);
    }

    int retVal;
    try {
      Process process = new ProcessBuilder(args).start();
      inputStreamToFile(process.getInputStream(), outputHtml);
      inputStreamToFile(process.getErrorStream(), conversionLog);
      retVal = process.waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      retVal = -1;
    }

    if (0 != retVal) {
      outputHtml.delete();
      throw new ConversionFailedException("Conversion failed. Return value from wvWare: " + retVal);
    }

    return outputHtml;
  }

  private void inputStreamToFile(InputStream input, File output) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(input));
    BufferedWriter bw = new BufferedWriter(new FileWriter(output));

    String line;
    while (null != (line = br.readLine())) {
      bw.write(line);
    }
    bw.close();
    br.close();
  }

}
