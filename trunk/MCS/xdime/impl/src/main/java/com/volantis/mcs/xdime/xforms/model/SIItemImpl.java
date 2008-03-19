/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms.model;

import java.util.StringTokenizer;

/**
 * Concrete implementation of {@link SIItem}.
 */
public class SIItemImpl implements SIItem {

    private String value;
    private String name;
    private String enclosingModelID;
    private boolean isReferenced;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param value                 of the si:item element
     * @param name                  identifier of the si:item element. Must be
     *                              unique within the xform and non null.
     * @param enclosingModelID      identifier of the model in which this
     *                              si:item was defined
     */
    public SIItemImpl(String value, String name, String enclosingModelID) {

        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("si:item name cannot be empty");
        }
        this.name = name;

        if (value != null) {
            this.value = value;
        } else {
            this.value ="";            
        }

        this.enclosingModelID = enclosingModelID;
        this.isReferenced = false;
    }

    // Javadoc inherited.
    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public String getEnclosingModelID() {
        return enclosingModelID;
    }

    // Javadoc inherited.
    public String[] getValue() {

        String[] results = null;

        if (value != null) {
            // check if the result is a comma separated list of values...
            if (value.indexOf(',') != -1) {

                StringTokenizer t = new StringTokenizer(value, ",");
                final int numTokens = t.countTokens();
                results = new String[numTokens];

                for (int i = 0; i < numTokens; i++) {
                    results[i] = t.nextToken();
                }
            } else {
                results = new String[]{value};
            }
        }
        return results;
    }

    // Javadoc inherited.
    public String getUnprocessedValue() {
        return value;
    }

    // Javadoc inherited.
    public boolean isReferenced() {
        return isReferenced;
    }

    // Javadoc inherited.
    public void setIsReferenced() {
        this.isReferenced = true;
    }

    // Javadoc inherited.
    public boolean isEnclosingModelID(String modelID) {
        if (modelID == null || enclosingModelID.equals(modelID)) {
            return true;
        }
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9637/5	emma	VBM:2005092807 Adding tests for XForms emulation

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
