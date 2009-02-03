/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import java.io.File;

public class UpToDateChecker {

    private final long dependencyTimestamp;

    public UpToDateChecker(long dependencyTimestamp) {
        this.dependencyTimestamp = dependencyTimestamp;
    }

    public UpToDateChecker(File dependencyFile) {
        this.dependencyTimestamp = dependencyFile.lastModified();
    }

    public boolean isUpToDate(File targetFile) {
        long targetTimestamp = targetFile.lastModified();
        if (targetTimestamp == 0) {
            return false;
        }
        return isUpToDate(targetTimestamp);
    }

    public boolean isUpToDate(long targetTimestamp) {
        return targetTimestamp >= dependencyTimestamp;
    }
}
