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

import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.xml.pipeline.sax.drivers.web.DerivableHTTPMessageEntity;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestCookie;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestEntityFactory;
import com.volantis.xml.pipeline.sax.drivers.web.simple.BooleanSetter;
import com.volantis.xml.pipeline.sax.drivers.web.simple.IntSetter;
import com.volantis.xml.pipeline.sax.drivers.web.simple.Setter;
import com.volantis.xml.pipeline.sax.drivers.web.simple.Setters;
import com.volantis.xml.pipeline.sax.drivers.web.simple.SettersBuilder;

/**
 * Rule for webd:cookie element.
 */
public class CookieRule
        extends HttpMessageEntityRule {

    /**
     * The setters for the cookie specific properties that can be set
     * from the webd:get.
     */
    private static final Setters SETTERS;
    static {
        SettersBuilder builder = new SettersBuilder();

        // Add the setters that are common across all derivable HTTP message
        // entities.
        addCommonSetters(builder);

        builder.addCombinedSetter(createExpandedName("comment"), new Setter() {
            public void setPropertyAsString(Object object, String string) {
                cookie(object).setComment(string);
            }
        });

        builder.addCombinedSetter(createExpandedName("path"), new Setter() {
            public void setPropertyAsString(Object object, String string) {
                cookie(object).setPath(string);
            }
        });

        builder.addCombinedSetter(createExpandedName("domain"), new Setter() {
            public void setPropertyAsString(Object object, String string) {
                cookie(object).setDomain(string);
            }
        });

        builder.addCombinedSetter(createExpandedName("maxAge"), new IntSetter() {
            protected void setAsInt(Object object, int i) {
                cookie(object).setMaxAge(i);
            }
        });

        builder.addCombinedSetter(createExpandedName("secure"), new BooleanSetter() {
            protected void setAsBoolean(Object object, boolean b) {
                cookie(object).setSecure(b);
            }
        });

        builder.addCombinedSetter(createExpandedName("version"), new Setter() {
            public void setPropertyAsString(Object object, String string) {
                CookieVersion version = CookieVersion.getCookieVersion(string);
                cookie(object).setVersion(version);
            }
        });

        SETTERS = builder.buildSetters();
    }

    /**
     * Cast the object to a cookie.
     *
     * @param object The object to cast.
     * @return The object as a cookie.
     */
    private static WebRequestCookie cookie(Object object) {
        return (WebRequestCookie) object;
    }

    /**
     * Initialise.
     */
    public CookieRule() {
        this(WebRequestEntityFactory.getDefaultInstance());
    }

    /**
     * Initialise.
     *
     * @param factory The factory for creating cookies.
     */
    public CookieRule(WebRequestEntityFactory factory) {
        super(factory, SETTERS, WebRequestCookie.class);
    }

    // Javadoc inherited.
    protected DerivableHTTPMessageEntity createEntity() {
        return factory.createCookie();
    }
}
