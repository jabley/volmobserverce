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
package com.volantis.xml.namespace;

import com.volantis.shared.recovery.TransactionTracker;

import java.util.Stack;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.NamespaceSupport;

/**
 * Default implementation of the NamespacePrefixTracker interface
 */
public class DefaultNamespacePrefixTracker
        implements RecoverableNamespacePrefixTracker {

    /**
     * A NamespaceSupport object that this class delegates to
     */
    protected NamespaceSupport namespaceSupport;

    /**
     * A TransactionTracker to determine our transaction state.
     */
    protected TransactionTracker tracker = new TransactionTracker();

    /**
     * A State object to record the internal state of this class.
     */
    protected State state = new State();

    /**
     * Creates a new DefaultNamespacePrefixTracker instance.
     */
    public DefaultNamespacePrefixTracker() {
        namespaceSupport = new NamespaceSupport();
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, String namespaceURI) {
        // push a new context onto the underlying NamespaceSupport instance
        namespaceSupport.pushContext();

        boolean validPrefix = namespaceSupport.declarePrefix(prefix,
                                                             namespaceURI);
        if (!validPrefix) {
            namespaceSupport.popContext();
            throw new IllegalArgumentException("Invalid namespace prefix: " +
                                               prefix);
        }
        state.push();
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix) {
        // pop the current context off the underlying NamespaceSupport instance
        namespaceSupport.popContext();
        state.pop();
    }

    // javadoc inherited
    public void startElement() {
    }

    // javadoc inherited
    public void endElement() {
    }

    // javadoc inherited
    public String getNamespaceURI(String prefix) {
        return namespaceSupport.getURI(prefix);
    }

    // javadoc inherited
    public String getNamespacePrefix(String namespaceURI) {
        return namespaceSupport.getPrefix(namespaceURI);
    }

    // javadoc inherited
    public String[] getPrefixes() {
        Enumeration theEnum = namespaceSupport.getPrefixes();
        List result = new ArrayList();

        // add the default namespace if it is defined
        String defaultNS = namespaceSupport.getURI("");
        if(defaultNS != null && !"".equals(defaultNS)) {
            result.add("");
        }
        
        while(theEnum.hasMoreElements()) {
            result.add(theEnum.nextElement());
        }
        return (String[])result.toArray(new String[result.size()]);
    }

    /**
     * Resolve an attributes QName into an ExpandedName
     * @param name The QName as an object.
     * @return The ExpandedName object that the QName resolved to, or null if
     * the QName contained an unknown prefix.
     */
    public ExpandedName resolveAttributeQName(QName name) {
        return resolveQName(name, getNamespaceURI(""));
    }

    /**
     * Resolve an elements QName into an ExpandedName
     * @param name The QName as an object.
     * @return The ExpandedName object that the QName resolved to, or null if
     * the QName contained an unknown prefix.
     */
    public ExpandedName resolveElementQName(QName name) {
        return resolveQName(name, "");
    }

    /**
     * Resolve a QName into an ExpandedName.
     * @param name The QName as an object.
     * @param defaultNamespaceURI the default namespace URI
     * @return The ExpandedName object that the QName resolved to, or null if
     * the QName contained an unknown prefix.
     */
    public ExpandedName resolveQName(QName name, String defaultNamespaceURI) {

        String uri = defaultNamespaceURI;
        if (name.getPrefix().length() > 0) {
            uri = getNamespaceURI(name.getPrefix());
        }
        if (uri == null) {
            uri = "";
        }

        ExpandedName exp = new ImmutableExpandedName(uri, name.getLocalName());
        return exp;
    }

    // javadoc inherited from RecoverableTransaction interface
    public void startTransaction() {
        tracker.startTransaction();
        state.startTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void commitTransaction() {
        tracker.commitTransaction();
        state.commitTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        state.rollbackTransaction();
    }

    /**
     * This allows us to maintain our internal state across transactions.
     */
    private class State {

        /**
         * The number of prefixes pushed since the start of a transaction.
         */
        private int pushed = 0;

        /**
         * A Stack to keep the state of parent transactions.
         */
        private Stack states = new Stack();

        /**
         * Record that a prefix has been pushed.
         */
        public void push() {
            pushed++;
        }

        /**
         * Record that a prefix has been popped.
         */
        public void pop() {
            pushed--;
        }

        /**
         * Start a new transaction.
         */
        public void startTransaction() {
            states.push(new Integer(pushed));
            pushed = 0;
        }

        /**
         * Commit a transaction.
         */
        public void commitTransaction() {
            if (!states.isEmpty()) {
                Integer integer = (Integer)states.pop();
                pushed = pushed + integer.intValue();
            }
        }

        /**
         * Rollback a transaction.
         */
        public void rollbackTransaction() {
            // pop everything pushed in this transaction
            for (int i = 0; i < pushed; i++) {
                namespaceSupport.popContext();
            }
            if (!states.isEmpty()) {
                Integer integer = (Integer)states.pop();
                pushed = integer.intValue();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 25-Jul-03	242/2	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 27-Jun-03	127/2	doug	VBM:2003062306 Column Conditioner Modifications

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
