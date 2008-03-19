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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.devices;

/**
 * A typesafe enumeration of the types of PolicyOriginSelection.
 */
public class PolicyOriginSelectionType {
    /**
     * The Override PolicyOriginSelectionType.
     */
    public final static PolicyOriginSelectionType OVERRIDE =
            new PolicyOriginSelectionType();

    /**
     * The Fallback PolicyOriginSelectionType.
     */
    public final static PolicyOriginSelectionType FALLBACK =
            new PolicyOriginSelectionType();

    /**
     * The Restore Default PolicyOriginSelectionType.
     */
    public final static PolicyOriginSelectionType RESTORE =
            new PolicyOriginSelectionType();

    /**
     * Private constructor so that only this class creates and maintains
     * the types.
     */
    private PolicyOriginSelectionType() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Apr-04	3683/3	pcameron	VBM:2004030401 Some further tweaks

 14-Apr-04	3683/1	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 ===========================================================================
*/
