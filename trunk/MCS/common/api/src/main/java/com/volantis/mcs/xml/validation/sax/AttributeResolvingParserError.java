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
package com.volantis.mcs.xml.validation.sax;

import org.xml.sax.Attributes;

import java.util.ResourceBundle;

/**
 * This ParserError specialization should be used when the error is related
 * to an attribute but the error message contains the attributes value rather
 * than its name. The {@link #matchedArgument} will return the name of the
 * attribute if this error matches.
 */
public class AttributeResolvingParserError extends ParserError {

    /**
     * Initializes an <code>AttributeResolvingParserError</code with the
     * given parameters
     *
     * @param bundle the {@link ResourceBundle} where the error message resides
     * @param bundleKey the key that should be used to locate the error message
     * in the bundle
     * @param key the error's identifier key. This should be one of the
     * standard keys as specified in
     * {@link com.volantis.mcs.eclipse.validation.ValidationMessageBuilder}.
      * @param paramIndex specifies the template argument that will contain
     * the parameter of interest when a match is made against the templates
     * regular expression. <p><strong>Note:</strong> the parameter index may
     * not be NO_ARG.</p>
     * @throws ParserErrorException if the message located in the bundle is
     * badly formated
     */
    public AttributeResolvingParserError(ResourceBundle bundle,
                                         String bundleKey,
                                         String key,
                                         int paramIndex)
            throws ParserErrorException {
        // this error is always associated with an attribute
        super(bundle, bundleKey, key, paramIndex, true);
    }

    // javadoc inherited
    public String matchedArgument(Attributes attributes, String message) {
        String attName = null;
        // see if the message matches this errors template
        if (template.match(message)) {
            // a match was found
            // extract the parameter that matched.
            String attValue = template.getParen(1);
            // the param shoul refer to an attributes value we need to resolve
            // this down to the attribute name. If we can't resolve this
            // then we do not have a match.
            if (attributes != null) {
                // find the name of the attribute that has the specified
                // value.
                for (int i=0;
                     i<attributes.getLength() && attName == null;
                     i++) {
                    if(attValue.equals(attributes.getValue(i))) {
                        // found the name of the attribute that is in error
                        attName = attributes.getLocalName(i);
                    }
                }
            }
        }
        return attName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Dec-03	2057/3	doug	VBM:2003112803 Addressed several rework issues

 09-Dec-03	2057/1	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
