/*
 * EncryptedDocTests.java
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
import android.os.Build;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.viliussutkus89.android.assetextractor.AssetExtractor;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.TimeUnit;


@RunWith(AndroidJUnit4.class)
public class EncryptedDocTests {

  @BeforeClass
  public static void setIdlingTimeout() {
    IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.MINUTES);
    IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.MINUTES);
  }

  @BeforeClass
  public static void extractDoc() {
    Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    new AssetExtractor(instrumentation.getContext().getAssets())
            .setNoOverwrite()
            .extract(instrumentation.getTargetContext().getCacheDir(), "passwordProtected.doc");
  }

  private static boolean is_ABI_X86_or_X86_64() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      return Build.CPU_ABI.equals("x86") || Build.CPU_ABI.equals("x86_64");
    } else {
      if (Build.SUPPORTED_32_BIT_ABIS.length != 0 && Build.SUPPORTED_32_BIT_ABIS[0].equals("x86")) {
        return true;
      }
      return (Build.SUPPORTED_64_BIT_ABIS.length != 0 && Build.SUPPORTED_64_BIT_ABIS[0].equals("x86_64"));
    }
  }

  private final Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
  private final File docFile = new File(ctx.getCacheDir(), "passwordProtected.doc");
  private final wvWare wvWare = new wvWare(ctx).setInputDOC(docFile);

  private File convertedHtml = null;
  @After
  public void cleanUp() {
    if (null != convertedHtml) {
      assertTrue("Converted HTML file not found!", convertedHtml.exists());
      assertTrue("Converted HTML file empty!", convertedHtml.length() > 0);
      convertedHtml.delete();
    }
  }

  @Test(expected = wvWare.PasswordRequiredException.class)
  public void PasswordRequiredExceptionTest() throws Exception {
    convertedHtml = wvWare.convertToHTML();
  }

  @Test(expected = wvWare.WrongPasswordException.class)
  public void WrongPasswordExceptionTest() throws Exception {
    wvWare.setPassword("Some wrong password");
    convertedHtml = wvWare.convertToHTML();
  }

  @Test
  public void CorrectPasswordTest() throws Exception {
    wvWare.setPassword("toc");
    // Issue #6. It seems to work on arm ABI
    if (!is_ABI_X86_or_X86_64()) {
      convertedHtml = wvWare.convertToHTML();
    }
  }
}
