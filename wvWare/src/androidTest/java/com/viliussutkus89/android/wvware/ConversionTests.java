/*
 * ConversionTests.java
 *
 * Copyright (c) 2020 - 2022 ViliusSutkus89.com
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

import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.content.Context;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.viliussutkus89.android.assetextractor.AssetExtractor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ConversionTests {

  @BeforeClass
  public static void setIdlingTimeout() {
    IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.MINUTES);
    IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.MINUTES);
  }

  @BeforeClass
  public static void extractDocs() {
    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    AssetExtractor ae = new AssetExtractor(instrumentation.getContext().getAssets())
      .setNoOverwrite();
    File cacheDir = instrumentation.getTargetContext().getCacheDir();
    ae.extract(cacheDir, "sample.doc");
    ae.extract(cacheDir, "TomTaschauer.doc");
  }

  @AfterClass
  public static void cleanupExtractedDocs() {
    Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
    new File(ctx.getCacheDir(), "TomTaschauer.doc").delete();
    new File(ctx.getCacheDir(), "sample.doc").delete();
  }

  private final Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
  private final File inputFile = new File(ctx.getCacheDir(), "TomTaschauer.doc");
  private final File inputFileWithEmbeddedImage = new File(ctx.getCacheDir(), "sample.doc");
  private final wvWare wvWare = new wvWare(ctx).setInputDOC(inputFile);

  private File convertedHtml = null;

  @After
  public void cleanUp() {
    if (null != convertedHtml) {
      assertTrue("Converted HTML file not found!", convertedHtml.exists());
      assertTrue("Converted HTML file empty!", convertedHtml.length() > 0);
    }
  }

  @Test
  public void convertTest() throws wvWare.ConversionFailedException, IOException {
    convertedHtml = wvWare.convertToHTML();
  }

  @Test
  public void noGraphicsModeTest() throws wvWare.ConversionFailedException, IOException {
    wvWare.setNoGraphicsMode();
    convertedHtml = wvWare.convertToHTML();
  }

  @Test
  public void HtmlFileLargerWithImageEmbedded() throws wvWare.ConversionFailedException, IOException {
    wvWare.setInputDOC(inputFileWithEmbeddedImage);

    convertedHtml = wvWare.convertToHTML();
    File htmlNoGraphics = wvWare.setNoGraphicsMode().convertToHTML();

    assertTrue(htmlNoGraphics.exists());
    assertTrue(convertedHtml.length() > htmlNoGraphics.length());
  }
}
