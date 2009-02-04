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
 * 15-May-03    Geoff           VBM:2003042904 - Created; represents a 
 *                              character set in a WBSAX event stream.
 * 20-May-03    Geoff           VBM:2003052102 - Add charset name.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a character set value in a WBSAX event stream. 
 * <p>
 * This is used in the {@link WBSAXContentHandler#startDocument} events. 
 * <p>
 * The valid values that this object may take are defined in the IANA 
 * <a href="http://www.iana.org/assignments/character-sets">Character Sets</a>
 * document.
 * <p>
 * The name of the character set is a bit tricky, because it must be valid 
 * from two perspectives:
 * <ul>
 *   <li>it must be a valid IANA charset name so that we can use it to insert 
 *      into generated XML (for text producers).
 *   <li>it must be a valid Java charset name so that we can use it for
 *      character conversion.
 * </ul>
 * Luckily, Java is always able to parse "mime-preferred" IANA names (see
 * <a href="http://java.sun.com/j2se/1.3/docs/api/java/lang/package-summary.html">
 * the java.lang package summary for JDK1.3</a>), therefore this class has 
 * just a single "mime-preferred" IANA name.
 * <p>
 * Note that the charset code of 0 is a reserved value according to IANA, but 
 * is defined by section 5.6 of the WBXML 1.3 spec (WAP-192-WBXML-20010725-a) 
 * to be "unknown". This can be used to represent charsets which are not known 
 * in the IANA registry. The spec "strongly recommends" this not be used for
 * any other purpose. 
 */ 
public class CharsetCode extends MultiByteInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Maximum value for MIBenum as specified by 
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     */ 
    private static final int MIB_ENUM_MAX = 2999; 
    
    /**
     * The MIBEnum value for an "unknown" charset as defined by WBXML.
     * <p>
     * This is a reserved value according to IANA, but section 5.6 of the 
     * WBXML 1.3 spec (WAP-192-WBXML-20010725-a) defines it as "unknown". 
     * <p>
     * This is <b>only</b> to be used for charsets which are not defined by 
     * IANA, as per the WBXML spec.
     */ 
    public final static int UNKNOWN = 0; 
    
    /**
     * The "mime-preferred" name for the character set, as specified by 
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This name must also be recognised by Java otherwise any character 
     * conversions will fail.
     */ 
    private String charsetName;
    
    /**
     * Create an instance of this class, using the character set code 
     * (MIBenum) and name provided.
     *  
     * @param value The MIBenum value for the character set.
     * @param charsetName The name for the character set.
     */ 
    public CharsetCode(int value, String charsetName) {
        setMaximum(MIB_ENUM_MAX);
        setInteger(value);
        this.charsetName = charsetName;
    }

    /**
     * Returns the name for the character set.
     * <p>
     * This will be defined by IANA and hopefully valid in Java as well.
     * 
     * @return the name for the character set.
     */ 
    public String getCharsetName() {
        return charsetName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	860/3	geoff	VBM:2003071405 merge from metis again

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
