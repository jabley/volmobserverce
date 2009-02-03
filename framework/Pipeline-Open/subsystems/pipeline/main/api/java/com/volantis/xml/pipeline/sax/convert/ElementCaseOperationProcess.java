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

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.synergetics.NameValuePair;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The <code>ElementCaseOperationProcess</code> that will convert the element
 * to uppercase or lowercase depending on the mode.
 */
public class ElementCaseOperationProcess
        extends AbstractOperationProcess {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ElementCaseOperationProcess.class);

    /**
     * This is the conversion mode of this operation process which may be
     * LOWERCASE or UPPERCASE.
     */
    private boolean convertToLowerCase = true;

    /**
     * This list MUST be sorted alphabetically by the name and the name MUST be
     * capitilized (all caps). If not the unit test case will fail.
     */
    private static final String htmlElements[] = {
        "A", "ABBR", "ACRONYM", "ADDRESS", "APPLET", "AREA", "B", "BASE",
        "BASEFONT", "BDO", "BIG", "BLOCKQUOTE", "BODY", "BR", "BUTTON",
        "CAPTION", "CENTER", "CITE", "CODE", "COL", "COLGROUP", "DD", "DEL",
        "DFN", "DIR", "DIV", "DL", "DT", "EM", "FIELDSET", "FONT", "FORM",
        "FRAME", "FRAMESET", "H1", "H2", "H3", "H4", "H5", "H6", "HEAD", "HR",
        "HTML", "I", "IFRAME", "IMG", "INPUT", "INS", "ISINDEX", "KBD", "LABEL",
        "LEGEND", "LI", "LINK", "MAP", "MENU", "META", "NOFRAMES", "NOSCRIPT",
        "OBJECT", "OL", "OPTGROUP", "OPTION", "P", "PARAM", "PRE", "Q", "S",
        "SAMP", "SCRIPT", "SELECT", "SMALL", "SPAN", "STRIKE", "STRONG",
        "STYLE", "SUB", "SUP", "TABLE", "TBODY", "TD", "TEXTAREA", "TFOOT",
        "TH", "THEAD", "TITLE", "TR", "TT", "U", "UL", "VAR"
    };

    /**
     * The static cached array of NameValuePair objects which is initialized
     * by the constructor.
     */
    private static final Object[] cacheArray = new Object[htmlElements.length];

    /**
     * Initialize the contents of the static cache. This is done in this manner
     * because <code>NameValuePair</code> has one constructor that takes a name
     * AND a value.
     * <p>
     * Therefore using this constructor in the static initialization (and not
     * requiring htmlElemnts at all) may easily result in human error since the
     * name and value must be in uppercase and lowercase respectively and must
     * be equal (besides their case).
     *
     * Example (how it could've been initialized):
     * <p>
     * <pre>
     * private static final Object[] cacheArray = {
     *   new NameValuePair("HTML", "html"),
     *   :
     *   new NameValuePair("VAR", "var")
     * };
     * </pre>
     *
     * It is also easier to maintain the initialization via the htmlElements
     * array above.
     */
    static {
        for (int i = 0; i < htmlElements.length; i++) {
            String element = htmlElements[i];
            cacheArray[i] = new NameValuePair(element, element.toLowerCase());
        }
    }

    /**
     * Store the dynamic cache. Ideally this should be a sorted list so that we
     * could leverage the binarySearch methods on lists. However, it would cost
     * too much to use Collections.sort(..) since it creates an array of the
     * objects and then sorts that array AND we would have to do this every
     * time we want to perform the binarySearch search.
     * <p>
     * An obvious alternative is to add elements into the list in such a way
     * that the list is always sorted. This requires a custom SortedArrayList
     * implementation (there is one in MCS utilities which is partially
     * implemented) but the overhead in maintaining this sorted list is
     * probably not worth all the effort given the dynamic cache has a limited
     * lifespan anyway (the scope of the operation process start and end tag).
     * <p>
     * Therefore finding an element in this cache is done by a sequentially
     * searching and comparing the required name with each of the stored
     * objects' names.
     */
    private List dynamicCache = new ArrayList();

    /**
     * Store the comparator.
     */
    private static NameValuePairStringComparator comparator =
            new NameValuePairStringComparator();

    /**
     * The mode of this operation process which may be LOWERCASE or UPPERCASE.
     *
     * @param mode the mode of this operation process which may be LOWERCASE or
     *             UPPERCASE.
     */
    public void setMode(String mode) {

        if ("lower".equals(mode)) {
            convertToLowerCase = true;
        } else if ("upper".equals(mode)) {
            convertToLowerCase = false;
        } else {
            throw new IllegalArgumentException(
                    "Mode should be 'upper' or 'lower' only.");
        }
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {

        localName = getConvertedLocalName(localName);
        qName = getConvertedQName(qName, localName);
        super.startElement(namespaceURI, localName, qName, atts);
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {

        localName = getConvertedLocalName(localName);
        qName = getConvertedQName(qName, localName);
        super.endElement(namespaceURI, localName, qName);
    }

    /**
     * Get the converted local name from the input name parameter.
     *
     * @param  name the name used for intput.
     * @return      the converted local name.
     */
    private String getConvertedLocalName(String name) {

        String result = name;
        // Try the static cache first.
        int index = Arrays.binarySearch(cacheArray, name, comparator);
        if (index >= 0) {
            NameValuePair pair = (NameValuePair)cacheArray[index];

            // If we are converting to lower case use the value, otherwise
            // use the name (which is uppercase).
            result = convertToLowerCase ? pair.getValue() : pair.getName();
            if (logger.isDebugEnabled()) {
                logger.debug("Found local name in static cache: " + name);
            }
        } else {
            NameValuePair pair = findCachedNameValuePair(
                    new CaseInsensitiveName(name));

            if (pair == null) {
                pair = new NameValuePair(name, convertLocalName(name));

                if (logger.isDebugEnabled()) {
                    logger.debug("Adding localName object to dynamic cache: " +
                                 pair);
                }
                dynamicCache.add(pair);
            }
            result = pair.getValue();
        }
        return result;
    }

    /**
     * Convert the localname to uppercase or lowercase.
     *
     * @param  name the local name to use for the conversion.
     * @return      the converted result.
     */
    private String convertLocalName(String name) {
        return convertToLowerCase ? name.toLowerCase() : name.toUpperCase();
    }

    /**
     * Convert the qName to uppercase or lowercase whilst preserving the
     * prefixed namespace case and re-using the already converted local name.
     * For example: qName='wsd:tagname' becomes 'wsd:TAGNAME' for uppercase
     * conversion. Note that convertedLocalName would be 'TAGNAME'.
     *
     * @param  qName              the qualified name.
     * @param  convertedLocalName the converted local name
     * @return                    a new string contained the prefix and
     *                            converted local name.
     */
    private String convertQName(String qName, String convertedLocalName) {
        String convertedQName = convertedLocalName;
        int index = qName.indexOf(':');
        if (index != -1) {
            convertedQName = qName.substring(0, index + 1) +
                    convertedLocalName;
        }
        return convertedQName;
    }

    /**
     * Given a qName and an already converted localName, create a new string
     * containing the converted qName.
     * <p>
     * For example: qName is 'wsd:driver', localName is 'DRIVER' then the
     * result will be 'wsd:DRIVER'.
     *
     * @param  qName              the qualified name with the namespace prefix
     *                            which may be empty.
     * @param  convertedLocalName the already converted local name.
     * @return                    the newly converted qualified name, or
     *                            unmodified qName.
     */
    private String getConvertedQName(String qName, String convertedLocalName) {

        String result = convertedLocalName;
        if (qName.length() == 0) {
            result = qName;
        } else if (!qName.equalsIgnoreCase(convertedLocalName)) {

            NameValuePair pair = findCachedNameValuePair(qName);

            if (pair == null) {
                pair = new NameValuePair(
                        qName, convertQName(qName, convertedLocalName));

                if (logger.isDebugEnabled()) {
                    logger.debug("Adding qName object to dynamic cache: " +
                                 pair);
                }
                dynamicCache.add(pair);
            }
            result = pair.getValue();
        }
        return result;
    }

    /**
     * Find the cache <code>NameValuePair</code> given the name.
     *
     * @param  comparable the comparable object to use to match to the cached
     *                    name value pair object.
     * @return            the name value pair object, or null if it cannot be
     *                    found in the cache.
     */
    private NameValuePair findCachedNameValuePair(Comparable comparable) {
        NameValuePair result = null;

        int size = dynamicCache.size();
        for (int i = 0; (i < size) && (result == null); i++) {

            NameValuePair nameValuePair = (NameValuePair)dynamicCache.get(i);
            if (comparable.compareTo(nameValuePair.getName()) == 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found object in dynamic cache: " +
                                 nameValuePair);
                }
                result = nameValuePair;
            }
        }
        return result;
    }

    /**
     * Comparator that matches the sort order specified in the static cache.
     * This is used by the binary search method in Arrays to locate an object.
     */
    private static class NameValuePairStringComparator
            implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((NameValuePair)o1).getName().compareToIgnoreCase((String)o2);
        }
    }

    /**
     * Provide a case-insensitive comparable object.
     */
    private static class CaseInsensitiveName implements Comparable {
        private String name;

        public CaseInsensitiveName(String name) {
            this.name = name;
        }

        public int compareTo(Object o) {
            return name.compareToIgnoreCase((String)o);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	323/5	byron	VBM:2003080802 Provide ConvertElementCase pipeline process - bugfixes and additional test cases

 13-Aug-03	323/3	byron	VBM:2003080802 Provide ConvertElementCase pipeline process - bugfixes and additional test cases

 12-Aug-03	323/1	byron	VBM:2003080802 Provide ConvertElementCase pipeline process

 ===========================================================================
*/
