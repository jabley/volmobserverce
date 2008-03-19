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

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.web.DerivableHTTPMessageEntity;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestEntityFactoryMock;
import com.volantis.xml.pipeline.sax.drivers.web.simple.SimpleElementProcess;

/**
 * Base class for tests of {@link DerivableHTTPMessageEntity}s.
 */
public abstract class HttpMessageEntityRuleTestAbstract
        extends DynamicRuleTestAbstract {

    protected WebRequestEntityFactoryMock entityFactoryMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        entityFactoryMock = new WebRequestEntityFactoryMock(
                "entityFactoryMock", expectations);
    }

    protected void expectAddSimpleElementProcess(
            final DerivableHTTPMessageEntity entity) {

        dynamicProcessMock.fuzzy.addProcess(mockFactory.expectsAny())
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        SimpleElementProcess process =
                                (SimpleElementProcess)
                                event.getArgument(XMLProcess.class);

                        assertSame(entity, process.getObject());

                        return null;
                    }
                });
    }
}
