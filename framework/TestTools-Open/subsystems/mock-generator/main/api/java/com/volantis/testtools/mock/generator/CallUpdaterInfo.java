/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

public class CallUpdaterInfo {

    private final String qualifiedName;

    private final String creationCode;

    public CallUpdaterInfo(String qualifiedName, String creationCode) {
        this.qualifiedName = qualifiedName;
        this.creationCode = creationCode;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getCreationCode() {
        return creationCode;
    }
}
