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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.googledocs;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A FetchAdapterProcess is an AdapterProcess that includes another
 * Document that is specified via a URL.
 *
 * <p>This is used by DSB.</p>
 *
 * @volantis-api-include-in InternalAPI
 */
public class
        AuthenticateRule
        extends DynamicElementRuleImpl {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new AuthenticateRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(AuthenticateRule.class);

    /**
     * Identifier for the user id attribute
     */
    static final String USER_ID_ATTRIBUTE = "user-id";

    /**
     * Identifier for the password attribute
     */
    static final String PASSWORD_ATTRIBUTE = "password";

    /**
     * Identifier for the captcha key attribute
     */
    static final String CAPTCHA_KEY_ATTRIBUTE = "captcha-key";

    /**
     * Identifier for the captcha value attribute
     */
    static final String CAPTCHA_VALUE_ATTRIBUTE = "captcha-value";

    /**
     * Creates a new AuthenticateRule
     */
    public AuthenticateRule() {
    }

    // Javadoc inherited
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        // gather authorization credentials from attributes and store them in context,
        // so they are available for GDocsRules
        AuthData authData = new AuthData(attributes);
        authData.storeInContext(dynamicProcess.getPipelineContext());
        
        return null;
    }

    //javadoc inherited
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}