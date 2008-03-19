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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestCookie;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link CookieRule}.
 */
public class CookieRuleTestCase
        extends HttpMessageEntityRuleTestAbstract {

    /**
     * Ensure that the attributes are picked up properly.
     */
    public void testAttributes() throws Exception {

        final String name = "name value";
        final String value = "value value";
        final String from = "from value";
        final String comment = "comment value";
        final String domain = "domain value";
        final CookieVersion version = CookieVersion.RFC2109;

        final int maxAge = 100;
        final String path = "path value";
        final boolean secure = false;

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        WebRequestCookie cookie = new WebRequestCookie();
        HTTPMessageEntities entities = new SimpleHTTPMessageEntities();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectAddSimpleElementProcess(cookie);

        entityFactoryMock.expects.createCookie().returns(cookie);

        contextMock.expects.getProperty(WebRequestCookie.class)
                .returns(entities);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        addAttribute("name", name);
        addAttribute("value", value);
        addAttribute("from", from);

        addAttribute("comment", comment);
        addAttribute("domain", domain);
        addAttribute("maxAge", Integer.toString(maxAge));
        addAttribute("path", path);
        addAttribute("secure", "" + secure);
        addAttribute("version", version.getName());

        DynamicElementRule rule = new CookieRule(entityFactoryMock);
        XMLProcess process = (XMLProcess) rule.startElement(dynamicProcessMock,
                elementName, attributes);

        dynamicProcessMock.expects.removeProcess(process);

        rule.endElement(dynamicProcessMock, elementName, process);

        assertNotNull(cookie);

        assertEquals("Name should match", name,
                cookie.getName());
        assertEquals("Value should match", value,
                cookie.getValue());
        assertEquals("From should match", from,
                cookie.getFrom());
        assertEquals("Comment should match", comment,
                cookie.getComment());
        assertEquals("Domain should match", domain,
                cookie.getDomain());
        assertEquals("MaxAge should match", maxAge,
                cookie.getMaxAge());
        assertEquals("Path should match", path,
                cookie.getPath());
        assertEquals("Secure should match", secure,
                cookie.isSecure());
        // returns an int
        assertEquals("Version should match", version,
                cookie.getVersion());
    }
}
