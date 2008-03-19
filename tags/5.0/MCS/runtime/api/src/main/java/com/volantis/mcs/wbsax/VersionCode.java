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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents the WBXML 
 *                              version number in a WBSAX event stream.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents the WBXML version number in a WBSAX event stream.
 * <P>
 * Currently not enough motivation to use a flyweight factory, just to gain 
 * one object per WBXML document. 
 */ 
public class VersionCode extends SingleByteInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd ${YEAR}.";

    /**
     * A version code representing WML 1.1.
     */ 
    public static final VersionCode V1_1 = new VersionCode(1,1);
    
    /**
     * A version code representing WML 1.3.
     */ 
    public static final VersionCode V1_3 = new VersionCode(1,3);

    /**
     * The "major" part of the version number.
     */ 
    private int major = -1;
    
    /**
     * The "minor" part of the version number.
     */ 
    private int minor = -1;

    /**
     * Construct a version code from the coded representation.
     * 
     * @param code the coded version.
     */ 
    public VersionCode(int code) {
        // Ensure that the version code is inside the range we expect.
        if ((code & 0xFF) != code) {
            throw new IllegalArgumentException("Version code " + code + 
                    " invalid");
        }
        setInteger(code);
        
        // Decode into major and minor up front since it's not expensive.
        this.major = (code >> 4) + 1;
        this.minor = code & 0xF;
    }
    
    /**
     * Construct an instance of this class from a logical version number, 
     * consisting of a major and minor part.
     * 
     * @param major the major part of the version.
     * @param minor the minor part of the version.
     */ 
    public VersionCode(int major, int minor) {
        if (major < 1 || (major & 0xF) != major) {
            throw new IllegalArgumentException("Major version " + major + 
                    " invalid");
        }
        if (minor < 0 || (minor & 0xF) != minor) {
            throw new IllegalArgumentException("Minor version " + minor + 
                    " invalid");
        }
        this.major = major;
        this.minor = minor;
        
        // Encode into integer up front since it's not expensive.
        setInteger(((major - 1) << 4) + minor);
    }
    
    /**
     * Returns the "major" part of the version.
     * 
     * @return the "major" part of the version.
     */ 
    public int getMajor() {
        return major;
    }

    /**
     * Returns the "minor" part of the version.
     * 
     * @return the "minor" part of the version.
     */ 
    public int getMinor() {
        return minor;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
