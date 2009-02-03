/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class MockDirectoryScanner
        extends DirectoryScanner {

    private char[] buffer;
    private static final char[] PATTERN = "@mock.generate".toCharArray();
    private static final int PATTERN_LENGTH = PATTERN.length;

    public MockDirectoryScanner() {
        buffer = new char[4096];
    }

    protected boolean isSelected(String name, File file) {

        int matchedIndex = 0;
        boolean select = false;
        try {
            StringBuffer sb = new StringBuffer((int) file.length());
            FileInputStream is;
            is = new FileInputStream(file);
            try {
                Reader reader = new InputStreamReader(is);
                int read;
                while ((read = reader.read(buffer)) > 0) {
                    for (int i = 0; i < read; i += 1) {
                        if (buffer[i] == PATTERN[matchedIndex]) {
                            matchedIndex += 1;
                            if (matchedIndex == PATTERN_LENGTH) {
                                return true;
                            }
                        } else {
                            matchedIndex = 0;
                        }
                    }
                }
            } finally {
                is.close();
            }

        } catch (IOException e) {
            // Do nothing
        }
        return select;
    }
}
