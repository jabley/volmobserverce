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

import com.volantis.shared.recovery.RecoverableTransaction;
import com.volantis.shared.recovery.RecoverableTransactionMap;
import com.volantis.shared.recovery.RecoverableTransactionSet;
import com.volantis.shared.recovery.TransactionTracker;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import our.apache.commons.jxpath.Function;
import our.apache.commons.jxpath.Functions;

import java.util.Set;

/**
 * A JXPath functions implementation used to hold functions registered against
 * the expression framework and providing access to those functions from the
 * JXPath framework.
 */
public class JXPathFunctions implements Functions, RecoverableTransaction {
    /**
     * Used to resolve namespace prefixes to namespace URIs.
     */
    protected NamespacePrefixTracker namespacePrefixTracker;

    /**
     * Map to contain registered functions. This is indexed by the
     * {@link ExpandedName}s of the functions and contains {@link Function}
     * values.
     */
    protected RecoverableTransactionMap
            functions = new RecoverableTransactionMap();

    /**
     * The set of namespaces for which functions are defined.
     */
    protected RecoverableTransactionSet
            usedNamespaces = new RecoverableTransactionSet();

    /**
     * A TransactionTracker to determine our transaction state.
     */
    private TransactionTracker tracker = new TransactionTracker();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param namespacePrefixTracker the tracker used to resolve namespace URIs
     *                               from namespace prefixes
     */
    public JXPathFunctions(NamespacePrefixTracker namespacePrefixTracker) {
        this.namespacePrefixTracker = namespacePrefixTracker;
    }

    // javadoc inherited
    public Set getUsedNamespaces() {
        return usedNamespaces.getSet();
    }

    // javadoc inherited
    public Function getFunction(
            String namespace,
            String name,
            Object[] parameters) {
        // The namespace provided is actually the namespace prefix, so go
        // through these hoops to ensure that we find the function against the
        // correct namespace (the namespace for no prefix is "the default
        // function namespace" according to
        // http://www.w3.org/TR/xpath20/#id-function-calls - this
        // is modelled using the "default namespace")

        // ExpandedName requires un prefixed name to be represented via an
        // empty prefix string rather than null. Unfortunately, jxpath will
        // pass in null if no prefix is specified. Ensure we replace null
        // strings with empty strings.
        if (namespace == null) {
            namespace = "";
        }
        ExpandedName eName = namespacePrefixTracker.resolveQName(
                new ImmutableQName(namespace, name), "");

        return (Function)functions.get(eName);
    }

    /**
     * Stores a function against the specified namespace URI and local name.
     *
     * @param eName    the namespace URI and name for the function
     * @param function the function to be stored
     */
    public void addFunction(ImmutableExpandedName eName,
                            Function function) {
        functions.put(eName, function);

        usedNamespaces.add(eName.getNamespaceURI());
    }

    // javadoc inherited from RecoverableTransaction interface
    public void startTransaction() {
        tracker.startTransaction();
        functions.startTransaction();
        usedNamespaces.startTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void commitTransaction() {
        tracker.commitTransaction();
        functions.commitTransaction();
        usedNamespaces.commitTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        functions.rollbackTransaction();
        usedNamespaces.rollbackTransaction();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 21-Aug-03	388/1	doug	VBM:2003082103 Fixed null pointer problem with unprefixed function expressions

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 31-Jul-03	222/5	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jun-03	102/2	sumit	VBM:2003061906 request:getParameter XPath function support

 ===========================================================================
*/
