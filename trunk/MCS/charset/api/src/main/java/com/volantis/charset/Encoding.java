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
 * $Header: /src/voyager/com/volantis/charset/Encoding.java,v 1.2 2003/04/28 15:24:05 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003033107 - Created to support character
 *                              encoding.
 * 22-Apr-03    Mat             VBM:2003033107 - Added some extra javadoc
 *                              renamed encoding to charsetName.
 * 23-May-03    Mat             VBM:2003042907 - Added MIBEnum.
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset;

/**
 * Abstract class which is the base of all classes that represent an encoding.
 * <p>
 * Encodings serve two purposes:
 * <ul>
 *   <li>to extend the Java 1.3 encoding support to allow us to know which
 *      characters are representable in each charset. This support is present
 *      in JDK1.4 but we cannot pre-suppose that at this stage.
 *   <li>to provide MIBEnum values for charsets so that we can use those
 *      values when writing WBXML/WMLC.
 * </ul>
 * Encodings must be known to the JVM in order to be valid.
 */
public abstract class Encoding {

    /**
     * Special value of MIBEnum which indicates that the charset/MIBEnum was 
     * not found in our configuration. This is value is not defined by IANA.
     */ 
    public static final int MIBENUM_NOT_CONFIGURED = -2;
    
    /**
     * Special value of MIBEnum which indicates that the charset/mibenum was 
     * not registered with IANA. This is value is not defined by IANA.
     */ 
    public static final int MIBENUM_NOT_REGISTERED = -1;
    
    /** 
     * The size of the bitset to build.  Currently unicode can represent
     * 65535 characters.
     */
    protected static final int ARRAY_SIZE = 65535;
    
    /** 
     * The Java name of the character set this encoding represents.
     * <p>
     * This is not accessible externally because it would only be confusing if
     * it was. Due to the way we construct and cache instances of this class,
     * this name will not generally be useful. In particular, the Encoding
     * associated with the charset that the user requested will not always
     * have the same name as the charset requested.
     */
    private String charsetName;
    
    /**
     * The MIBEnum of the character set, or one of the special values defined
     * by the constants provided.
     */
    private int MIBEnum;
    
    /** 
     * Construct an instance of this class.
     *  
     * @param charsetName The Java name of a charset.
     * @param MIBEnum The MIBenum value for this charset name.
     */
    public Encoding(String charsetName, int MIBEnum) {
        this.charsetName = charsetName;
        this.MIBEnum = MIBEnum;
    }

    /**
     * Get the MIBEnum value for this encoding.
     * <p>
     * This will return {@link #MIBENUM_NOT_CONFIGURED} if the charset name  
     * requested from {@link EncodingManager} was not configured.
     * 
     * @return The MIBenum value.
     */
    public int getMIBEnum() {
        return MIBEnum;
    }

    /* 
     * Return whether this encoding requires the character given
     * to be encoded.
     *
     * @param c  The character to check
     * @return Whether the encoding supports mapping for the character.
     */
    public abstract CharacterRepresentable checkCharacter(int c);

    protected String debugName() {
        return "Encoding";
    }

    protected String debugParameters() {
        return "name=" + charsetName + ",MIBenum=" + MIBEnum;
    }
    
    public String toString() {
        return debugName() + ":" + debugParameters();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Jan-04	2653/5	steve	VBM:2004011304 Remove visibility of constants

 19-Jan-04	2653/1	steve	VBM:2004011304 Merge from proteus

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 25-Jul-03	860/3	geoff	VBM:2003071405 merge from metis again

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
