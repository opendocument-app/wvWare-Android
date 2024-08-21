/*
 * ConversionTests.java
 *
 * Copyright (c) 2020 - 2022, 2024 ViliusSutkus89.com
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
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.viliussutkus89.android.assetextractor.AssetExtractor;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@LargeTest
@RunWith(Parameterized.class)
public class ConversionTests {
  private final File docFile;
  public ConversionTests(File docFile) {
    this.docFile = docFile;
  }

  @BeforeClass
  public static void setIdlingTimeout() {
    IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.MINUTES);
    IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.MINUTES);
  }

  @BeforeClass
  public static void extractDocs() {
    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    new AssetExtractor(instrumentation.getContext().getAssets())
            .setNoOverwrite()
            .extract(instrumentation.getTargetContext().getCacheDir(), "testDocs");
  }

  @Parameterized.Parameters(name = "{0}")
  public static List<File> listPDFs() throws IOException {
    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    File extractedToDir = new File(instrumentation.getTargetContext().getCacheDir(), "testDocs");
    List<File> testFiles = new ArrayList<>();
    String[] assetFiles = instrumentation.getContext().getAssets().list("testDocs");
    if (assetFiles != null) {
      for (String assetFile : assetFiles) {
        if (!assetFile.equals("passwordProtected.doc"))
          testFiles.add(new File(extractedToDir, assetFile));
      }
    }
    return testFiles;
  }

  @Test
  public void convertPDF() throws wvWare.ConversionFailedException, IOException {
    Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
    wvWare wvWare = new wvWare(ctx).setInputDOC(docFile);

    if (docFile.getName().equals("sample.doc")) {
      wvWare.setNoGraphicsMode();
    }
    File htmlFile = wvWare.convertToHTML();

    assertTrue("Converted HTML file not found! " + docFile.getName(), htmlFile.exists());
    assertTrue("Converted HTML file empty! " + docFile.getName(), htmlFile.length() > 0);
  }
}
