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
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;

/**
 * This abstract class allows document and contextual information to be
 * obtained from an unspecified document source. Implementations of this class
 * provide methods that access and search the document source.
 */
public abstract class DocumentInfo {
    /**
     * A simple implementation of the InputContext interface.
     */
    private class SchemaInputContext implements InputContext {
        /**
         * The containing element name. This should not be null
         */
        private String containingElementName;

        /**
         * The match name. This should not be null
         */
        private String matchName;

        /**
         * Indicates that the input context is representing input of an
         * attribute or (if not) an element
         */
        private boolean attribute;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param containingElementName the containing name
         * @param matchName             the match name
         * @param attribute             indicates that an attribute input
         *                              context has been found
         */
        public SchemaInputContext(String containingElementName,
                                  String matchName,
                                  boolean attribute) {
            if (containingElementName == null) {
                throw new IllegalArgumentException(
                        "the containing element name may not be null"); //$NON-NLS-1$
            } else if (matchName == null) {
                throw new IllegalArgumentException(
                        "the match name may not be null"); //$NON-NLS-1$
            }

            this.containingElementName = containingElementName;
            this.matchName = matchName;
            this.attribute = attribute;
        }

        // javadoc unnecessary
        public String getContainingElementName() {
            return containingElementName;
        }

        // javadoc unnecessary
        public String getMatchName() {
            return matchName;
        }

        // javadoc unnecessary
        public boolean isAttribute() {
            return attribute;
        }
    }

    /**
     * Returns an input context appropriate to the given offset within the
     * document source, or null if no appropriate context exists.
     *
     * @param index the offset within the document source for which an input
     *              context is required
     * @return an input context or null if no appropriate context exists
     */
    public InputContext getInputContext(int index) {
        InputContext result = null;

        if (getLength() > index) {
            // Steps:
            // 1. find the immediately preceeding '<' character. NB: If we find
            //    a '>' first, we cannot provide contextual assistance
            // 2. determine if we have whitespace between the current point and
            //    the '<': if we have, we are looking at attributes (note,
            //    however, in this case we can't do completions if a "/" has
            //    been seen), if not we need to continue with the following
            //    steps
            // 3. find the immediately containing element. In doing this we
            //    need to be able to ignore intervening end/start element
            //    markup. We should also ignore all content of a CDATA block
            int startOfElement = -1;
            int i = index;
            boolean contextAvailable = true;
            String containingElementName = null;
            String matchName = null;
            boolean attributes = false;
            boolean foundSlash = false;
            char ch;

            while (contextAvailable && (i >= 0) && (startOfElement == -1)) {
                if ((ch = getChar(i)) == '<') {
                    startOfElement = i;
                } else if (ch == '>') {
                    contextAvailable = false;
                } else {
                    // Attribute input is only allowed if the current character
                    // is whitespace and there is no '/' between this point
                    // in the document and the start of element "<"
                    if (ch == '/') {
                        foundSlash = true;
                    } else if (Character.isWhitespace(ch)) {
                        if (foundSlash || ((i < index) && !attributes)) {
                            contextAvailable = false;
                        } else {
                            attributes = true;
                        }
                    }

                    i--;
                }
            }

            if (startOfElement == -1) {
                contextAvailable = false;
            } else {
                // Find the (partial) element name available, if any
                StringBuffer sb = new StringBuffer(index - startOfElement);

                for (i = startOfElement + 1;
                     (i <= index) && (!Character.isWhitespace(ch = getChar(i)));
                     i++) {
                    sb.append(ch);
                }

                matchName = sb.toString();
            }

            if (contextAvailable) {
                if (attributes) {
                    // We already have the containing element in matchName
                    containingElementName = matchName;

                    // Now correctly derive a match name for the attribute

                    // Firstly make sure we have a balanced set of quotes
                    // between the element name and the input position
                    int apos = 0;
                    int quot = 0;

                    for (i = startOfElement + 1;
                         i <= index;
                         i++) {
                        if ((ch = getChar(i)) == '\'') {
                            apos++;
                        } else if (ch == '"') {
                            quot++;
                        }
                    }

                    if (((apos % 2) != 0) ||
                            (quot % 2) != 0) {
                        contextAvailable = false;
                    } else {
                        // Find the start of the attribute name
                        for (i = index;
                             (i >= 0) && !Character.isWhitespace(getChar(i));
                             i--)
                            ;

                        // Now collect the name up
                        StringBuffer sb = new StringBuffer(index - i);

                        for (int j = i + 1;
                             (j <= index) &&
                                isValidChar(ch = getChar(j));
                             j++) {
                            sb.append(ch);
                        }

                        matchName = sb.toString();
                    }
                } else {
                    // Find that containing element
                    i = startOfElement - 1;
                    int startOfContainer = -1;
                    int nesting = 1;
                    boolean inTagName = false;
                    boolean inString = false;
                    boolean closeTag = false;
                    int cdata = 0;

                    while ((i >= 0) && (startOfContainer == -1)) {
                        ch = getChar(i);

                        if ((ch == '\'') ||
                                (ch == '"')) {
                            if (cdata == 0) {
                                inString = !inString;
                            }
                        } else if (!inString) {
                            if (inTagName) {
                                if (ch == ']') {
                                    // Minimize risk of mis-interpreting a ']' in a
                                    // CDATA block as part of the terminal "]]>"
                                    // syntax
                                    if ((getChar(i + 1) == ']') ||
                                            (getChar(i + 1) == '>')) {
                                        cdata++;
                                    }
                                } else if (ch == '[') {
                                    // Minimize risk of mis-interpreting a '[' in a
                                    // CDATA block as part of the starting "<![CDATA["
                                    // syntax
                                    if (i > 0) {
                                        if ((getChar(i - 1) == 'A') ||
                                                (getChar(i - 1) == '!')) {
                                            cdata--;

                                            if (cdata == 0) {
                                                nesting++;
                                            }
                                        }
                                    }
                                } else if (cdata == 0) {
                                    if (ch == '/') {
                                        if (getChar(i + 1) == '>') {
                                            nesting++;
                                        } else {
                                            closeTag = true;
                                        }
                                    } else if (ch == '<') {
                                        inTagName = false;
                                        cdata = 0;

                                        if (closeTag) {
                                            nesting++;
                                        } else {
                                            nesting--;

                                            if (nesting == 0) {
                                                startOfContainer = i;
                                            }
                                        }
                                    }
                                }
                            } else if ((cdata == 0) && (ch == '>')) {
                                inTagName = true;
                                closeTag = false;
                            }
                        }

                        i--;
                    }

                    if (startOfContainer == -1) {
                        contextAvailable = false;
                    } else {
                        // Find the containing element name
                        StringBuffer sb = new StringBuffer(80);

                        for (i = startOfContainer + 1;
                             (i <= index) &&
                                isValidChar(ch = getChar(i));
                             i++) {
                            sb.append(ch);
                        }

                        containingElementName = sb.toString();
                    }
                }
            }

            if (contextAvailable) {
                result = new SchemaInputContext(containingElementName,
                        matchName,
                        attributes);
            }
        }

        return result;
    }

    /**
     * Supporting method that returns true if the given character is valid in
     * the context of an attribute or element name (including namespace prefix)
     *
     * @param ch the character to be tested
     * @return true if the character is valid
     */
    private boolean isValidChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '-' || ch == ':' ||
                ch == '_';
    }

    /**
     * Returns the namespace prefix defined in the document source for the
     * specified namespace URI. If the namespace URI is used to define the
     * default namespace, the empty string will be returned. If there is no
     * prefix in the nameSpaceURI, MCSNamespace.LPDM is used to obtain
     * the prefix.
     *
     * @param namespaceURI the namespace URI for which the prefix is to be
     *                     found
     * @return the prefix for the namespace or null if none is defined
     */
    public String getNamespacePrefix(String namespaceURI) {
        String prefix = null;

        int index = indexOf(0, "xmlns=\"" + namespaceURI + "\"");

        if (index == -1) {
            index = indexOf(0, "xmlns='" + namespaceURI + "'");
        }

        if (index != -1) {
            prefix = "";
        } else {
            // Now looking for something of the form xmlns:prefix="uri"
            int namespaceIndex = indexOf(0, namespaceURI);

            if ((namespaceIndex != -1) &&
                    ((getChar(namespaceIndex - 1) == '"') ||
                    (getChar(namespaceIndex - 1) == '\'')) &&
                    (getChar(namespaceIndex - 2) == '=')) {
                boolean ok = true;
                StringBuffer buffer = new StringBuffer();
                index = namespaceIndex - 2;

                // Work back and find the "xmlns:" that should appear before
                // any prefix
                while ((index >= 0) &&
                        getChar(index) != ':') {
                    index--;
                }

                if ((index >= 5) &&
                        (getChar(index - 5) == 'x') &&
                        (getChar(index - 4) == 'm') &&
                        (getChar(index - 3) == 'l') &&
                        (getChar(index - 2) == 'n') &&
                        (getChar(index - 1) == 's')) {
                    for (int i = index + 1;
                         ok && (i < namespaceIndex - 2);
                         i++) {
                        final char ch = getChar(i);

                        ok = Character.isLetterOrDigit(ch);

                        if (ok) {
                            buffer.append(ch);
                        }
                    }

                    if (ok) {
                        prefix = buffer.toString();
                    }
                }
            }
        }

        if (prefix == null) {
            // Didn't find a prefix so let's default to the LPDM one
            prefix = MCSNamespace.LPDM.getPrefix();
        }

        return prefix;
    }

    /**
     * Returns the character at the specified index.
     *
     * @param index the index for which the specified character is to be
     *              obtained
     * @return the character at the specified index
     */
    protected abstract char getChar(int index);

    /**
     * Returns the length of the entire document.
     *
     * @return the document's length
     */
    protected abstract int getLength();

    /**
     * Searches the document for the specified text from the given start index,
     * returning the index or -1 if the given text cannot be found.
     *
     * @param start  the start point for the search.
     * @param search the text to be searched for
     * @return -1 or the index of the search string found after the specified
     *         index in the document
     */
    protected abstract int indexOf(int start,
                                   String search);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 08-Jan-04	2498/1	philws	VBM:2004010804 Provide end element markup completion and improve attribute markup completion handling

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
