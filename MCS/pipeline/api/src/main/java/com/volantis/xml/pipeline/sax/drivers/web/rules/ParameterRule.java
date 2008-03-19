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

import com.volantis.xml.pipeline.sax.drivers.web.DerivableHTTPMessageEntity;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverElements;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestEntityFactory;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestParameter;
import com.volantis.xml.pipeline.sax.drivers.web.simple.Setter;
import com.volantis.xml.pipeline.sax.drivers.web.simple.Setters;
import com.volantis.xml.pipeline.sax.drivers.web.simple.SettersBuilder;

/**
 * Rule for webd:parameter element.
 */
public class ParameterRule
        extends HttpMessageEntityRule {

    /**
     * The setters for the parameter specific properties that can be set
     * from the webd:get or webd:post.
     */
    private static final Setters SETTERS;
    static {
        SettersBuilder builder = new SettersBuilder();

        // Add the setters that are common across all derivable HTTP message
        // entities.
        addCommonSetters(builder);

        builder.addCombinedSetter(WebDriverElements.TARGET, new Setter() {
            public void setPropertyAsString(Object object, String string) {
                parameter(object).setTarget(string);
            }
        });

        SETTERS = builder.buildSetters();
    }

    /**
     * Cast the object to a parameter.
     *
     * @param object The object to cast.
     * @return The object as a parameter.
     */
    private static WebRequestParameter parameter(Object object) {
        return (WebRequestParameter) object;
    }
    
    /**
     * Initialise.
     */
    public ParameterRule() {
        this(WebRequestEntityFactory.getDefaultInstance());
    }

    /**
     * Initialise.
     *
     * @param factory The factory for creating parameters.
     */
    public ParameterRule(WebRequestEntityFactory factory) {
        super(factory, SETTERS, WebRequestParameter.class);
    }

    // Javadoc inherited.
    protected DerivableHTTPMessageEntity createEntity() {
        return factory.createParameter();
    }
}
