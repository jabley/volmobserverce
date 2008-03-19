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
package com.volantis.xml.expression.impl.jxpath.functions;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import our.apache.commons.jxpath.ExpressionContext;
import our.apache.commons.jxpath.Function;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple test case.
 */
public class JXPathFunctionsTestCase extends TestCaseAbstract {
    public JXPathFunctionsTestCase(String name) {
        super(name);
    }

    public void testGetUsedNamespaces() throws Exception {
        NamespacePrefixTracker namespacePrefixTracker =
            new DefaultNamespacePrefixTracker();
        JXPathFunctions functions =
            new JXPathFunctions(namespacePrefixTracker);
        ImmutableExpandedName nameA =
            new ImmutableExpandedName("http://spaceA",
                                      "a");
        ImmutableExpandedName nameB = new ImmutableExpandedName("", "b");
        ImmutableExpandedName nameC =
            new ImmutableExpandedName("http://spaceC",
                                      "c");
        ImmutableExpandedName nameC2 =
            new ImmutableExpandedName("http://spaceC",
                                      "c2");
        Function a = new TestFunction();
        Function b = new TestFunction();
        Function c = new TestFunction();
        Set namespaceURLs;
        Set expectedURLs = new HashSet();

        functions.addFunction(nameA, a);
        functions.addFunction(nameB, b);
        functions.addFunction(nameC, c);
        functions.addFunction(nameC2, c);

        expectedURLs.add(nameA.getNamespaceURI());
        expectedURLs.add(nameB.getNamespaceURI());
        expectedURLs.add(nameC.getNamespaceURI());
        expectedURLs.add(nameC2.getNamespaceURI());

        namespaceURLs = functions.getUsedNamespaces();

        assertEquals("number of used namespaces not as",
                     3,
                     namespaceURLs.size());

        assertEquals("number of expected used namespaces not correct",
                     expectedURLs.size(),
                     namespaceURLs.size());
        assertEquals("set of used namespaces not as",
                     expectedURLs,
                     namespaceURLs);
    }

    public void testGetFunction() throws Exception {
        NamespacePrefixTracker namespacePrefixTracker =
            new DefaultNamespacePrefixTracker();
        JXPathFunctions functions =
            new JXPathFunctions(namespacePrefixTracker);
        ImmutableExpandedName nameA =
            new ImmutableExpandedName("http://spaceA",
                                      "a");
        ImmutableExpandedName nameB = new ImmutableExpandedName("", "b");
        ImmutableExpandedName nameC =
            new ImmutableExpandedName("http://spaceC",
                                      "c");
        Function a = new TestFunction();
        Function b = new TestFunction();

        functions.addFunction(nameA, a);
        functions.addFunction(nameB, b);

        // Add the non-default prefixes
        namespacePrefixTracker.startPrefixMapping("spaceA",
                                                  nameA.getNamespaceURI());
        namespacePrefixTracker.startPrefixMapping("spaceC",
                                                  nameC.getNamespaceURI());
        
        assertSame("function A",
                   a,
                   functions.getFunction("spaceA",
                                         nameA.getLocalName(),
                                         null));
        assertSame("function B",
                   b,
                   functions.getFunction("",
                                         nameB.getLocalName(),
                                         null));
        assertNull("function C",
                   functions.getFunction("spaceC",
                                         nameC.getLocalName(),
                                         null));
    }

    /**
     * Test that ensures the {@link JXPathFunctions#getFunction} method
     * handles functions that are not prefixed
     * @throws Exception if an error occurs
     */
    public void testUnprefixedFunction() throws Exception {
        // create a function
        Function function = new TestFunction();
        // create a JXPathFunctions instance
        NamespacePrefixTracker namespacePrefixTracker =
            new DefaultNamespacePrefixTracker();
        JXPathFunctions functions =
            new JXPathFunctions(namespacePrefixTracker);

        ImmutableExpandedName eName = new ImmutableExpandedName("", "foo");

        // register the function
        functions.addFunction(eName, function);

        // retrieve the function
        assertSame("Unexpected function ",
                   function,
                   functions.getFunction(null, "foo", null));
    }

    public void testAddFunction() throws Exception {
        NamespacePrefixTracker namespacePrefixTracker =
            new DefaultNamespacePrefixTracker();
        JXPathFunctions functions =
            new JXPathFunctions(namespacePrefixTracker);
        ImmutableExpandedName nameA =
            new ImmutableExpandedName("http://spaceA",
                                      "a");
        ImmutableExpandedName nameB = new ImmutableExpandedName("", "b");
        ImmutableExpandedName nameC =
            new ImmutableExpandedName("http://spaceC",
                                      "c");
        ImmutableExpandedName nameA2 =
            new ImmutableExpandedName("http://spaceA",
                                      "a");
        Function a = new TestFunction();
        Function b = new TestFunction();
        Function c = new TestFunction();

        assertEquals("there should be no functions registered yet",
                     0,
                     functions.functions.size());

        functions.addFunction(nameA, a);
        functions.addFunction(nameB, b);

        assertEquals("number of registered functions not as",
                     2,
                     functions.functions.size());
        assertSame("function A not registered correctly",
                   a,
                   functions.functions.get(nameA));
        assertSame("function B not registered correctly",
                   b,
                   functions.functions.get(nameB));
        assertNull("function C should not be found yet",
                   functions.functions.get(nameC));

        functions.addFunction(nameA2, c);
        functions.addFunction(nameC, c);

        assertEquals("number of registered functions not as",
                     3,
                     functions.functions.size());
        assertSame("function A (replacement) not registered correctly",
                   c,
                   functions.functions.get(nameA));
        assertSame("function C not registered correctly",
                   c,
                   functions.functions.get(nameC));
    }

    /**
     * Trivial implementation of the JXPath function for testing purposes
     */
    protected static class TestFunction implements Function {
        public Object invoke(ExpressionContext expressioncontext,
                             Object parameters[]) {
            return null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 21-Aug-03	388/1	doug	VBM:2003082103 Fixed null pointer problem with unprefixed function expressions

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
