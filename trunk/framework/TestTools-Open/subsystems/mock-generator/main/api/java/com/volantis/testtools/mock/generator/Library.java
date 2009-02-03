/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import java.util.List;

public class Library {

    private final String libraryName;

    private final List fields;

    public Library(String libraryName, List fields) {
        this.libraryName = libraryName;
        this.fields = fields;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public List getFields() {
        return fields;
    }
}
