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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.utilities.sax;

/**
 * Container for general SAX associated utility methods.
 */
public class SAXUtils {
    /**
     * The default length for a local name if the name is not yet known.
     */
    static final int DEFAULT_LOCAL_NAME_LENGTH = 10;

    /**
     * Create a prefixed name for a local name using a given prefix.
     * @param prefix The required prefix.
     * @param localName The local name for the qName.
     * @return A prefixed local name.
     */
    public static String createPrefixedName(String prefix, String localName) {

        StringBuffer qNameBuf = createPrefixBuffer(prefix, localName);
        qNameBuf.append(localName);
        String qName = qNameBuf.toString();

        return qName;
    }

    /**
     * Create a buffer that contains the prefix part of a qName.
     * @param prefix The prefix. If this is null an empty StringBuffer will
     * be returned.
     * @param localName If not null this is used to set the length of the
     * buffer.
     * @return A StringBuffer that contains the prefix + ':'.
     */
    public static StringBuffer createPrefixBuffer(String prefix,
                                                  String localName) {
        int localNameLength = localName == null ? DEFAULT_LOCAL_NAME_LENGTH :
                localName.length();
        int prefixLength = prefix == null ? 0 :
                prefix.length() + localNameLength + 1;
        StringBuffer prefixBuf = new StringBuffer(prefixLength);
        if (prefix != null) {
            prefixBuf.append(prefix).append(':');
        }

        return prefixBuf;
    }

    /**
     * Determine if a character array or section of a character array
     * contains only whitespace.
     * @param chars the character array
     * @param start the index of the first character from which to start
     * looking for only whitespace
     * @param length the number of characters that are allowed to be whitespace
     * after the start
     * @return true if the given character array contains only whitespace
     * characters; false otherwise.
     */
    public static boolean isWhitespaceCharacters(char chars [],
                                                 int start, int length) {
        boolean onlyWhitespace = true;

        if (chars == null) {
            // todo : isn't this an error?
            return true;
        }

        int end = start+length;
        if (end > chars.length) {
              end = chars.length;
        }
        for(int i=start; i<end && onlyWhitespace; i++) {
            onlyWhitespace = Character.isWhitespace(chars[i]);
        }

        return onlyWhitespace;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	10090/1	pabbott	VBM:2005103105 White space collapse problem

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 29-Jun-03	98/1	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/
