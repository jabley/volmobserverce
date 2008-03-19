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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.eclipse.common.odom.ODOMElement; 
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import junit.framework.Assert;
import org.jdom.Element;

/**
 * An ErrorReporter that is parameterized on construction with the details of
 * the expected error. When an error is reported the the details are checked to
 * ensure that they match the expected details.
 */
public class CheckingErrorReporter implements ErrorReporter {
        /**
         * The element for which an error is expected to be reported
         */
        private ODOMElement element;

        /**
         * The error key expected
         */
        private String expectedErrorKey;

        /**
         * Indicates whether {@link #reportError} has been invoked
         */
        private boolean reported = false;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param element          the element against which an error is
         *                         expected
         * @param expectedErrorKey the error key expected to be reported
         */
        public CheckingErrorReporter(ODOMElement element,
                                     String expectedErrorKey) {
            this.element = element;
            this.expectedErrorKey = expectedErrorKey;
        }

        // javadoc inherited
        public void reportError(ErrorDetails details) {
            XPath xPath = details.getXPath();
            String key = details.getKey();

            Assert.assertEquals("XPath not as",
                                new XPath(element).getExternalForm(),
                                xPath.getExternalForm());
            Assert.assertEquals("Error key not as",
                                expectedErrorKey,
                                key);
            reported = true;
        }

        // javadoc inherited
        public void validationStarted(Element root, XPath xpath) {
        }

        // javadoc inherited
        public void validationCompleted(XPath xpath) {
        }

        // javadoc unnecessary
        public boolean isReported() {
            return reported;
        }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/3	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 09-Aug-04	5130/1	doug	VBM:2004080310 MCS

 ===========================================================================
*/
