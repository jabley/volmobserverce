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
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContextMock;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessMock;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Base class for tests of {@link DynamicElementRule}.
 */
public abstract class DynamicRuleTestAbstract
        extends TestCaseAbstract {

    protected DynamicProcessMock dynamicProcessMock;
    protected XMLPipelineContextMock contextMock;
    private Locator locator;
    protected AttributesImpl attributes;
    protected ImmutableExpandedName elementName;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        dynamicProcessMock =
                new DynamicProcessMock("dynamicProcessMock", expectations);

        contextMock = new XMLPipelineContextMock("contextMock",
                expectations);

        locator = new LocatorImpl();

        dynamicProcessMock.expects.getPipelineContext()
                .returns(contextMock).any();

        contextMock.expects.getCurrentLocator().returns(locator).any();

        attributes = new AttributesImpl();

        elementName = new ImmutableExpandedName("namespace", "element");
    }

    /**
     * Add an attribute to the set of SAX attributes.
     *
     * @param localName The local name.
     * @param value     The value.
     */
    protected void addAttribute(final String localName, String value) {
        attributes.addAttribute("", localName, localName, "CDATA", value);
    }

    /**
     * Add an expectation that the dynamic process will receive an error and
     * throw it.
     */
    protected void addExpectErrorAndThrow() {
        dynamicProcessMock.fuzzy.error(mockFactory.expectsAny())
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        Throwable t = (Throwable) event.getArgument(
                                SAXParseException.class);
                        throw t;
                    }
                });
    }

    /**
     * Invoke the startElement assuming that it will fail.
     *
     * @param rule    The rule to test.
     * @param message The expected message.
     * @throws SAXException If the exception was not of the correct type.
     */
    protected void doStartFailure(
            DynamicElementRule rule, final String message)
            throws SAXException {
        addExpectErrorAndThrow();
        try {
            rule.startElement(dynamicProcessMock, elementName, attributes);
            fail("Did not fail as expected");
        } catch (SAXParseException expected) {
            assertEquals(message, expected.getMessage());
        }
    }
}
