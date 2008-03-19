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
package com.volantis.xml.expression.impl;

import com.volantis.shared.recovery.RecoverableTransactionMap;
import com.volantis.shared.recovery.TransactionTracker;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.expression.RecoverableExpressionScope;
import com.volantis.xml.expression.InternalExpressionScope;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionScope;
import com.volantis.xml.expression.Variable;
import com.volantis.xml.expression.Value;

/**
 * This is a simple, generic implementation of the ExpressionScope interface.
 *
 * @see com.volantis.xml.expression.impl.SimpleExpressionFactory
 */
public class SimpleExpressionScope
        implements RecoverableExpressionScope, InternalExpressionScope {
    
    /**
     * The factory by which this scope was created.
     */
    protected ExpressionFactory factory;

    /**
     * The scope enclosing this scope, or null if this scope is top-level.
     */
    private ExpressionScope enclosingScope;

    /**
     * The variables defined within this scope.
     */
    private RecoverableTransactionMap variables;

    /**
     * A TransactionTracker to determine our transaction state.
     */
    private TransactionTracker tracker = new TransactionTracker();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory        the factory by which the scope was created
     * @param enclosingScope the scope enclosing this one
     */
    public SimpleExpressionScope(ExpressionFactory factory,
                                 ExpressionScope enclosingScope) {
        this.factory = factory;
        this.enclosingScope = enclosingScope;

        this.variables = new RecoverableTransactionMap();
    }

    // javadoc inherited
    public ExpressionScope getEnclosingScope() {
        return enclosingScope;
    }

    // javadoc inherited
    public Variable declareVariable(ExpandedName name,
                                    Value initialValue) {
        Variable variable = null;

        if (variables.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Variable " + name.getLocalName() + " in namespace \"" +
                    name.getNamespaceURI() + "\" already exists in this scope");
        } else {
            variable = factory.createVariable(name, initialValue);

            variables.put(name, variable);
        }

        return variable;
    }

    public Variable declareVariable(ExpandedName name) {
        // Declare an uninitialised variable.
        return declareVariable(name, SimpleVariable.UNSET);
    }

    // javadoc inherited
    public Variable resolveVariable(ExpandedName name) {
        Variable variable = (Variable)variables.get(name);

        // Since the current scope doesn't have this variable defined in it
        // look for it in the enclosing scope, if there is one
        if ((variable == null) &&
                (enclosingScope != null)) {
            variable = enclosingScope.resolveVariable(name);
        }

        return variable;
    }

    // javadoc inherited from RecoverableTransaction interface
    public void startTransaction() {
        tracker.startTransaction();
        variables.startTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void commitTransaction() {
        tracker.commitTransaction();
        variables.commitTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface
    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        variables.rollbackTransaction();
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

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
