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

package com.volantis.styling.integration.compiler;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.impl.compiler.MatcherFactory;
import com.volantis.styling.impl.compiler.MatcherFactoryImpl;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;
import com.volantis.mcs.xdime.XDIMESchemata;

/**
 * Test cases for {@link MatcherFactory}.
 */
public class MatcherFactoryTestCase
        extends TestCaseAbstract {
    private MatcherFactory factory;

    protected void setUp() throws Exception {
        super.setUp();

        final SimpleMatcherMock defaultNamespaceMatcherMock =
                new SimpleMatcherMock("defaultNamespaceMatcherMock",
                        expectations);

        factory = new MatcherFactoryImpl(defaultNamespaceMatcherMock);
    }

    /**
     * Test that an invalid namespace prefix is detected.
     */
    public void testInvalidNamespacePrefix() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        try {
            factory.createNamespaceMatcher("abc");
            fail("Did not detect invalid prefix");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown prefix 'abc'", e.getMessage());
        }
    }

    /**
     * Test that a valid namespace prefix is allowed.
     */
    public void testValidNamespacePrefix() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MatcherContextMock contextMock =
                new MatcherContextMock("contextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.hasDirectRelationship().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = factory.createNamespaceMatcher("*");
        assertNull(matcher);

        checkNamespaceMapping(contextMock, "cdm", XDIMESchemata.CDM_NAMESPACE);
        checkNamespaceMapping(contextMock, "xhtml2", XDIMESchemata.XHTML2_NAMESPACE);
        checkNamespaceMapping(contextMock, "xforms", XDIMESchemata.XFORMS_NAMESPACE);
    }

    private void checkNamespaceMapping(
            final MatcherContextMock contextMock, final String namespacePrefix,
            final String namespaceURI) {
        contextMock.expects.getNamespace().returns(namespaceURI);
        Matcher matcher = factory.createNamespaceMatcher(namespacePrefix);
        MatcherResult result = matcher.matches(contextMock);
        assertEquals(MatcherResult.MATCHED, result);
    }
}
