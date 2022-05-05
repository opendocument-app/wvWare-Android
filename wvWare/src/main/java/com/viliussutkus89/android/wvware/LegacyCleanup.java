package com.viliussutkus89.android.wvware;

// Taken from https://github.com/ViliusSutkus89/pdf2htmlEX-Android/blob/master/pdf2htmlEX/src/main/java/com/viliussutkus89/android/pdf2htmlex/LegacyCleanup.java

import android.content.Context;
import java.io.File;

// Some files in previous releases were installed to a different location
// cleanup those files

final class LegacyCleanup {

    private static class LegacyEntry {
        private final String m_directory;
        private final String[] m_files;

        LegacyEntry(String directory, String[] files) {
            this.m_directory = directory;
            this.m_files = files;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        void cleanup(File filesDirPrefix) {
            File filesDir = new File(filesDirPrefix, m_directory);
            if (!filesDir.exists() || !filesDir.isDirectory()) {
                return;
            }

            for (String file : m_files) {
                File f = new File(filesDir, file);
                if (f.exists() && f.isFile()) {
                    f.delete();
                }
            }

            try {
                String[] leftoverFilesInDir = filesDir.list();
                if (null != leftoverFilesInDir && 0 == leftoverFilesInDir.length) {
                    filesDir.delete();
                }
            } catch (SecurityException ignored) {
            }
        }
    }

    private static final LegacyEntry[] c_legacy_files_pre_v1_2_7 = new LegacyEntry[] {
            new LegacyEntry("wv/patterns", new String[] {
                    "battributes.jpg",
                    "bdeleted.jpg",
                    "bnewlytyped.jpg",
                    "clear.gif",
                    "columnbreak.gif",
                    "commentbegin.gif",
                    "commentend.gif",
                    "deleted.jpg",
                    "doccommentb.jpg",
                    "doccommente.jpg",
                    "documentend.gif",
                    "eattributes.jpg",
                    "edeleted.jpg",
                    "endnotebegin.gif",
                    "endnoteend.gif",
                    "enewlytyped.jpg",
                    "footnotebegin.gif",
                    "footnoteend.gif",
                    "pagebreak.gif",
                    "sectionendcolumn.gif",
                    "sectionendcontinous.gif",
                    "sectionendeven.gif",
                    "sectionendnewpage.gif",
                    "sectionendodd.gif",
                    "wmf.jpg",
                    "14.gif",
                    "15.gif",
                    "16.gif",
                    "17.gif",
                    "18.gif",
                    "19.gif",
                    "20.gif",
                    "21.gif",
                    "22.gif",
                    "23.gif",
                    "24.gif",
                    "25.gif",
                    "26.gif",
                    "27.gif",
                    "28.gif",
                    "29.gif",
                    "30.gif",
                    "31.gif",
                    "32.gif",
                    "33.gif",
                    "34.gif",
                    "35.gif",
                    "36.gif",
                    "37.gif",
                    "38.gif",
                    "39.gif",
                    "40.gif",
                    "41.gif",
                    "42.gif",
                    "43.gif",
                    "44.gif",
                    "45.gif",
                    "46.gif",
                    "47.gif",
                    "48.gif",
                    "49.gif",
                    "50.gif",
                    "51.gif",
                    "52.gif",
                    "53.gif",
                    "54.gif",
                    "55.gif",
                    "56.gif",
                    "57.gif",
                    "58.gif",
                    "59.gif",
                    "60.gif",
                    "61.gif",
                    "62.gif",
                    "63.gif",
                    "64.gif",
                    "65.gif",
                    "66.gif",
                    "67.gif",
                    "68.gif",
                    "69.gif",
                    "70.gif",
                    "71.gif",
                    "72.gif",
                    "73.gif",
                    "74.gif",
                    "75.gif",
                    "76.gif",
                    "77.gif",
                    "78.gif",
                    "79.gif",
                    "80.gif",
                    "81.gif",
                    "82.gif",
                    "83.gif",
                    "84.gif",
                    "85.gif",
                    "86.gif",
                    "87.gif",
                    "88.gif",
                    "89.gif",
                    "90.gif",
                    "91.gif",
                    "92.gif",
                    "93.gif",
                    "94.gif",
                    "95.gif",
                    "96.gif",
                    "97.gif",
                    "98.gif",
                    "99.gif",
                    "100.gif",
                    "101.gif",
                    "102.gif",
                    "103.gif",
                    "104.gif",
                    "105.gif",
                    "106.gif",
                    "107.gif",
                    "108.gif",
                    "109.gif",
                    "110.gif",
                    "111.gif",
                    "112.gif",
                    "113.gif",
                    "114.gif",
                    "115.gif",
                    "116.gif",
                    "117.gif",
                    "118.gif",
                    "119.gif",
                    "120.gif",
                    "121.gif",
                    "122.gif",
                    "123.gif",
                    "124.gif",
                    "125.gif",
                    "126.gif",
                    "127.gif",
                    "128.gif",
                    "129.gif",
                    "130.gif",
                    "131.gif",
                    "132.gif",
                    "133.gif",
                    "134.gif",
                    "135.gif",
                    "136.gif",
                    "137.gif",
                    "138.gif",
                    "139.gif",
                    "140.gif",
                    "141.gif",
                    "142.gif",
                    "143.gif",
                    "144.gif",
                    "145.gif",
                    "146.gif",
                    "147.gif",
                    "148.gif",
                    "149.gif",
                    "150.gif",
                    "151.gif",
                    "152.gif",
                    "153.gif",
                    "154.gif",
                    "155.gif",
                    "156.gif",
                    "157.gif",
                    "158.gif",
                    "159.gif",
                    "160.gif",
                    "161.gif",
                    "162.gif",
                    "163.gif",
                    "164.gif",
                    "165.gif",
                    "166.gif",
                    "167.gif",
                    "168.gif",
                    "169.gif",
                    "170.gif",
                    "171.gif",
                    "172.gif",
                    "173.gif",
                    "174.gif",
                    "175.gif",
                    "176.gif",
                    "177.gif",
                    "178.gif",
                    "179.gif",
                    "180.gif",
                    "181.gif",
                    "182.gif",
                    "183.gif",
                    "184.gif",
                    "185.gif",
                    "186.gif",
                    "187.gif",
                    "188.gif",
                    "189.gif",
                    "190.gif",
                    "191.gif",
                    "192.gif",
                    "193.gif",
                    "194.gif",
                    "195.gif",
                    "196.gif",
                    "197.gif",
                    "198.gif",
                    "199.gif",
                    "200.gif",
                    "201.gif",
                    "202.gif",
                    "203.gif",
                    "204.gif",
                    "205.gif",
            }),

            new LegacyEntry("wv/wingdingfont", new String[] {
                    "35.gif",
                    "36.gif",
                    "37.gif",
                    "38.gif",
                    "39.gif",
                    "40.gif",
                    "41.gif",
                    "42.gif",
                    "43.gif",
                    "44.gif",
                    "45.gif",
                    "46.gif",
                    "47.gif",
                    "48.gif",
                    "49.gif",
                    "50.gif",
                    "51.gif",
                    "52.gif",
                    "53.gif",
                    "54.gif",
                    "55.gif",
                    "56.gif",
                    "57.gif",
                    "58.gif",
                    "59.gif",
                    "60.gif",
                    "61.gif",
                    "62.gif",
                    "63.gif",
                    "64.gif",
                    "65.gif",
                    "66.gif",
                    "67.gif",
                    "68.gif",
                    "69.gif",
                    "70.gif",
                    "71.gif",
                    "72.gif",
                    "73.gif",
                    "74.gif",
                    "75.gif",
                    "76.gif",
                    "77.gif",
                    "78.gif",
                    "79.gif",
                    "80.gif",
                    "81.gif",
                    "82.gif",
                    "83.gif",
                    "84.gif",
                    "85.gif",
                    "86.gif",
                    "87.gif",
                    "88.gif",
                    "89.gif",
                    "90.gif",
                    "91.gif",
                    "92.gif",
                    "93.gif",
                    "94.gif",
                    "95.gif",
                    "96.gif",
                    "97.gif",
                    "98.gif",
                    "99.gif",
                    "100.gif",
                    "101.gif",
                    "102.gif",
                    "103.gif",
                    "104.gif",
                    "105.gif",
                    "106.gif",
                    "107.gif",
                    "108.gif",
                    "109.gif",
                    "110.gif",
                    "111.gif",
                    "112.gif",
                    "113.gif",
                    "114.gif",
                    "115.gif",
                    "116.gif",
                    "117.gif",
                    "118.gif",
                    "119.gif",
                    "120.gif",
                    "121.gif",
                    "122.gif",
                    "123.gif",
                    "124.gif",
                    "125.gif",
                    "126.gif",
                    "127.gif",
                    "128.gif",
                    "129.gif",
                    "130.gif",
                    "131.gif",
                    "132.gif",
                    "133.gif",
                    "134.gif",
                    "135.gif",
                    "136.gif",
                    "137.gif",
                    "138.gif",
                    "139.gif",
                    "140.gif",
                    "141.gif",
                    "142.gif",
                    "143.gif",
                    "144.gif",
                    "145.gif",
                    "146.gif",
                    "147.gif",
                    "148.gif",
                    "149.gif",
                    "150.gif",
                    "151.gif",
                    "152.gif",
                    "153.gif",
                    "154.gif",
                    "155.gif",
                    "156.gif",
                    "157.gif",
                    "158.gif",
                    "159.gif",
                    "160.gif",
                    "161.gif",
                    "162.gif",
                    "163.gif",
                    "164.gif",
                    "165.gif",
                    "166.gif",
                    "167.gif",
                    "168.gif",
                    "169.gif",
                    "170.gif",
                    "171.gif",
                    "172.gif",
                    "173.gif",
                    "174.gif",
                    "175.gif",
                    "176.gif",
                    "177.gif",
                    "178.gif",
                    "179.gif",
                    "180.gif",
                    "181.gif",
                    "182.gif",
                    "183.gif",
                    "184.gif",
                    "185.gif",
                    "186.gif",
                    "187.gif",
                    "188.gif",
                    "189.gif",
                    "190.gif",
                    "191.gif",
                    "192.gif",
                    "193.gif",
                    "194.gif",
                    "195.gif",
                    "196.gif",
                    "197.gif",
                    "198.gif",
                    "199.gif",
                    "200.gif",
                    "201.gif",
                    "202.gif",
                    "203.gif",
                    "204.gif",
                    "205.gif",
                    "206.gif",
                    "207.gif",
                    "208.gif",
                    "209.gif",
                    "210.gif",
                    "211.gif",
                    "212.gif",
                    "213.gif",
                    "214.gif",
                    "215.gif",
                    "216.gif",
                    "217.gif",
                    "218.gif",
                    "219.gif",
                    "220.gif",
                    "221.gif",
                    "222.gif",
                    "223.gif",
                    "224.gif",
                    "225.gif",
                    "226.gif",
                    "227.gif",
                    "228.gif",
                    "229.gif",
                    "230.gif",
                    "231.gif",
                    "232.gif",
                    "233.gif",
                    "234.gif",
                    "235.gif",
                    "236.gif",
                    "237.gif",
                    "238.gif",
                    "239.gif",
                    "240.gif",
                    "241.gif",
                    "242.gif",
                    "243.gif",
                    "244.gif",
                    "245.gif",
                    "246.gif",
                    "247.gif",
                    "248.gif",
                    "249.gif",
                    "250.gif",
                    "251.gif",
                    "252.gif",
                    "253.gif",
                    "254.gif",
                    "255.gif",
            }),

            new LegacyEntry("wv", new String[] {
                    "wvAbw.xml",
                    "wvCleanLaTeX.xml",
                    "wvConfig.xml",
                    "wvDocbook.xml",
                    "wvHtml.xml",
                    "wvLaTeX.xml",
                    "wvText.xml",
                    "wvWml.xml",
                    "wvXml.dtd",
                    "wvXml.xml",
            })
    };

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void cleanup(Context ctx) {
        final File filesDir = ctx.getFilesDir();
        for (LegacyEntry e : c_legacy_files_pre_v1_2_7) {
            e.cleanup(filesDir);
        }

        final File cacheDir = ctx.getCacheDir();

        final File[] legacyCaches = new File[] {
            new File(cacheDir, "homeForFontforge")
        };
        for (File cacheEntry: legacyCaches) {
            cacheEntry.delete();
        }

    }
}
