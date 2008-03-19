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
package com.volantis.mcs.devices;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is responsible for providing utilility methods for
 * converting policy values obtained from
 * {@link com.volantis.mcs.devices.InternalDevice} (typically strings) into
 * a more convinient form.
 */
public class DevicePolicyValueUtil {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DevicePolicyValueUtil.class);
    /**
     * Constant for the character used to seperate attribute-value pairs,
     * ie ,. [key]=[value], [key1]=[value1], ...
     */
    private static final char KEY_VALUE_PAIR_SEPERATOR = ',';

    /**
     * Constant value for equals '='.
     */
    private static final char EQUALS = '=';

    /**
     * Constant value for ".
     */
    private static final char QUOTE = '\"';

    /**
     * Utility method that converts a string of comma seperated attribute,
     * value pairs into a Map.
     * <p>
     * e.g name1=content1,name2=content2
     * </p>
     * <p>
     * Note that values that require multiple values or contain
     * non alphanumeric characters MUST be placed within
     * quotes. e.g  name1="fred,bob,sue",name2="this=that"
     * </p>
     *
     * @param attributeValuePairPolicyValues the key value pairs to be placed
     * in a Map.
     *
     * @return a Map containing the key value pairs supplied.  If a null
     * policy value is supplied, an empty Map will be returned.
     */
    public Map createMapFromPolicyValues(String attributeValuePairPolicyValues) {

        Map policyValueMap = new HashMap();
         // Guard against null policy value.
        if (attributeValuePairPolicyValues != null) {
            ArrayList attributeValuePairs = split(attributeValuePairPolicyValues,
                                                  KEY_VALUE_PAIR_SEPERATOR);

            // obtain the individual attributes and values for each pair
            Iterator attributeValuePairIter = attributeValuePairs.iterator();
            while (attributeValuePairIter.hasNext()) {
                String attributeValuePair = (String)attributeValuePairIter.next();

                // split the attribute and value
                ArrayList list = split(attributeValuePair, EQUALS);
                String attribute = (String)list.get(0);
                String value = (String)list.get(1);

                // Add the retrieved name value pair to the Map.
                policyValueMap.put(attribute.trim(), value.trim());
            }
        }
        return policyValueMap;
    }


    /**
     * Returns a collection of strings from the supplied policyValues that have
     * been split with the supplied seperator.
     * <p>
     * e.g
     * </p>
     * <p>
     * policyValues = name1=1, name2=2, name3=3
     * seperator = ,
     * </p>
     * returns [ [name1=1], [name2=2], [name3=3]]
     *
     * @param policyValues the policy values to be split by the supplied
     * seperator.
     *
     * @param seperator the character by which the supplied policyValues will
     * be split.
     *
     * @return collection of strings from the supplied policyValues that were
     * seperated by the supplied seperator.
     */
    private ArrayList split(String policyValues, char seperator) {

        ArrayList splitSubStrings = new ArrayList();

        char[] policyValuesCharacters = policyValues.toCharArray();

        boolean insideQuote = false;

        boolean foundInvalidAttributeValuePair = false;

        StringBuffer splitSubStringBuffer = new StringBuffer();
        // Copy each character to the buffer until the seperator is found.
        for (int i = 0; i < policyValuesCharacters.length; i++) {

            // Have we encountered a quote?
            if (policyValuesCharacters[i] == QUOTE ) {
                if (insideQuote) {
                    insideQuote = false;
                } else {
                    insideQuote = true;
                }
            }

            // Ignore the seperator if we are inside quotes as we may have a
            // multiple value, eg name="value1,value2,value3".
            if (policyValuesCharacters[i] == seperator && !insideQuote) {

                String splitSubString = splitSubStringBuffer.toString();

                // If ',' is used as the seperator then we expect
                // attribute-value pairs to be specified as either:
                // 1. [attribute]=[value]
                // 2. [attribute]="[value],[value2],[value3]..."

                if (isAttributeValuePairValid(splitSubString, seperator)) {

                    splitSubStrings.add(splitSubString);
                    // Empty the buffer ready for the next seperated string
                    splitSubStringBuffer.delete(0,
                                                splitSubStringBuffer.length());

                } else {
                    // We have found an invalid
                    foundInvalidAttributeValuePair = true;
                    splitSubStringBuffer.delete(0,
                                               splitSubStringBuffer.length());
                }
            } else {
                // Quotes are only used when an attribute has multiple
                // values seperated by ,.  e.g name="a,b,c". In this case
                // we do not want to preserve the quotes as there only purpose
                // is to avoid breaking up the string when this method
                // is called.
                if (!(policyValuesCharacters[i] == QUOTE)) {
                    // add current character to the buffer.
                    splitSubStringBuffer.append(policyValuesCharacters[i]);
                }

            }
        }
        // Ensure that the last substring is included if
        // there is no seperator at the end of the string.

        String splitSubString = splitSubStringBuffer.toString();
        if (!("".equals(splitSubString))) {
            if (isAttributeValuePairValid(splitSubString, seperator)) {
               splitSubStrings.add(splitSubStringBuffer.toString());
            } else {
                int numberOfSubStrings = splitSubStrings.size();
                splitSubStrings.remove(numberOfSubStrings - 1);
            }
        }

        if (foundInvalidAttributeValuePair) {
            // We have found an invalid attribute value pair. If this case
            // arises we ignore all attributes supplied.

            // Log warning: Device specific META attributes have not been written as
            // an invalid attribute-value pair has been supplied
            logger.warn("invalid-attribute-value-pair");
            splitSubStrings = new ArrayList();
        }

        return splitSubStrings;
    }

    /**
     * Returns true if the attribute value is valid.
     * <p>
     * If ',' is used as the seperator then we expect
     * attribute-value pairs to be specified as either:
     * <br>
     * 1. [attribute]=[value] <br>
     * 2. [attribute]="[value],[value2],[value3]..." <br>
     * </p>
     * @param attributeValuePair the attribute value pair.
     * @param seperator the seperator being used in {@link #split}.
     *
     * @return true if the attribute value pair is valid; otherwise false.
     */
    private boolean isAttributeValuePairValid(String attributeValuePair,
                                              char seperator) {

        boolean isValid = true;
        if (KEY_VALUE_PAIR_SEPERATOR == seperator &&
                        attributeValuePair.indexOf("=") == -1 ) {
            isValid = false;
        }
        return isValid;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10785/1	ianw	VBM:2005071309 Ported forward meta changes

 22-Jul-05	9117/1	rgreenall	VBM:2005071309 Merge from 323: Support device specific META elements.

 22-Jul-05	9085/6	rgreenall	VBM:2005061306 Post review improvements

 20-Jul-05	9085/4	rgreenall	VBM:2005061306 Add support for adding device specific meta elements - rework

 20-Jul-05	9085/2	rgreenall	VBM:2005061306 Add support for adding device specific meta elements

 ===========================================================================
*/
