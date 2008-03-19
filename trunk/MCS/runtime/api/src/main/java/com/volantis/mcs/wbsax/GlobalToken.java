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
 * 15-May-03    Geoff           VBM:2003042904 - Created; global Token values 
 *                              used by WBXML. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Global Token values used by WBXML (1.1 and 1.3, they are identical).
 * <p>
 * This is deliberately a class rather than an interface to stop people using
 * the "implement interface to pick up constants" anti-pattern.
 */ 
public class GlobalToken  {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    //public static final int SWITCH_PAGE = 0x00;
    public static final int END         = 0x01;
    public static final int ENTITY      = 0x02;
    public static final int STR_I       = 0x03;
    public static final int LITERAL     = 0x04;
    
    public static final int EXT_I_0     = 0x40;
    public static final int EXT_I_1     = 0x41;
    public static final int EXT_I_2     = 0x42;
    public static final int PI          = 0x43;
    public static final int LITERAL_C   = 0x44;
    
    public static final int EXT_T_0     = 0x80;
    public static final int EXT_T_1     = 0x81;
    public static final int EXT_T_2     = 0x82;
    public static final int STR_T       = 0x83;
    public static final int LITERAL_A   = 0x84;
    
    public static final int EXT_0       = 0xC0;
    public static final int EXT_1       = 0xC1;
    public static final int EXT_2       = 0xC2;
    public static final int OPAQUE      = 0xC3;
    public static final int LITERAL_AC  = 0xC4;

    /**
     * Private constructor to prevent instantiation.
     */ 
    private GlobalToken() {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
