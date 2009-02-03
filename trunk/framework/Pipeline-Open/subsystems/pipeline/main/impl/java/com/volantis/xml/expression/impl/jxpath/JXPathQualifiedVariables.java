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
package com.volantis.xml.expression.impl.jxpath;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.recovery.RecoverableTransaction;
import com.volantis.shared.recovery.RecoverableTransactionStack;
import com.volantis.shared.recovery.TransactionTracker;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionScope;
import com.volantis.xml.expression.RecoverableExpressionScope;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.Variable;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import our.apache.commons.jxpath.Variables;

import java.util.Iterator;

/**
 * This class implements the JXPath Variables interface to allow variables
 * declarations to be managed.
 */
public class JXPathQualifiedVariables
        implements Variables, RecoverableTransaction {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JXPathQualifiedVariables.class);

    /**
     * Stack used to provide variable scoping. Each entry represents a
     * variable stack frame. Each stack frame is represented by an
     * {@link ExpressionScope}
     */
    private RecoverableTransactionStack frames;

    /**
     * The factory that should be used to create expression related objects.
     */
    private ExpressionFactory factory;

    /**
     * The namespace prefix tracker used to convert prefixed names into
     * expanded names.
     */
    private NamespacePrefixTracker namespacePrefixTracker;

    /**
     * The global scope is where variables are defined when no frames have
     * been defined. All frames' expression scopes are enclosed (directly or
     * indirectly) by this global scope.
     */
    private ExpressionScope globalScope;

    /**
     * A TransactionTracker to determine our transaction state.
     */
    private TransactionTracker tracker = new TransactionTracker();

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory                the factory that should be used to create
     *                               expression related objects
     * @param namespacePrefixTracker the namespace prefix tracker used to
     *                               convert prefixed names into expanded
     *                               names
     */
    protected JXPathQualifiedVariables(
            ExpressionFactory factory,
            NamespacePrefixTracker namespacePrefixTracker) {
        this.factory = factory;
        this.namespacePrefixTracker = namespacePrefixTracker;

        frames = new RecoverableTransactionStack();
        globalScope = factory.createExpressionScope(null);
    }

    /**
     * Allocates a new variable stack frame
     */
    public void pushStackFrame() {
        // Push a new scope onto the stack to represent the new frame. When
        // we support pushing and popping scopes as well as frames, we will
        // have a need for an object that actually represents the frame
        // explicitly. Until then, this will do.
        //
        // Note that the new scope is enclosed by the "global" scope in order
        // to meet the API specification.
        frames.push(factory.createExpressionScope(globalScope));
    }

    /**
     * Pops the variable stack frame that is at the top of the stack.
     * Throws an IllegalStateException if stack is empty.
     */
    public void popStackFrame() {
        if (frames.size() == 0) {
            throw new IllegalStateException(
                    "No stack frame to pop");
        } else {
            frames.pop();
        }
    }

    public void pushBlockScope() {
        // Note that the new scope is enclosed by the current scope in order
        // to meet the API specification.
        frames.push(factory.createExpressionScope(getCurrentScope()));
    }

    public void popBlockScope() {
        if (frames.size() == 0) {
            throw new IllegalStateException(
                    "No stack frame / scope to pop");
        } else {
            frames.pop();
        }
    }

    /**
     * Returns the current expression scope. If no stack frame has been
     * defined, this returns the global expression scope.
     *
     * @return the current expression scope
     */
    public ExpressionScope getCurrentScope() {
        ExpressionScope currentScope = globalScope;

        if (frames.size() > 0) {
            currentScope = (ExpressionScope)frames.peek();
        }

        return currentScope;
    }

    // javadoc inherited
    public void declareVariable(String variable, Object value) {
        ExpressionScope scope = getCurrentScope();

        if (scope == null) {
            throw new IllegalStateException(
                    "An attempt has been made to declare a variable " +
                    "while there is no current expression scope");
        } else if (!(value instanceof Value)) {
            throw new IllegalArgumentException(
                    "An attempt has been made to declare a variable " +
                    "with a value that doesn't implement " +
                    Value.class.getName() +
                    " (" + value.getClass().getName() + ")");
        } else {
            ExpandedName name = expandName(variable);

            scope.declareVariable(name, (Value)value);
        }
    }

    // javadoc inherited
    public boolean isDeclaredVariable(String variable) {
        return getCurrentScope().resolveVariable(expandName(variable)) != null;
    }

    // javadoc inherited
    public void undeclareVariable(String variable) {
        if (logger.isDebugEnabled()) {
            logger.debug("An attempt was made to undeclare variable " +
                         variable + " but undeclaring variables is not " +
                         "supported by the API");
        }
    }

    // javadoc inherited
    public Object getVariable(String variable) {
        Variable var = getCurrentScope().resolveVariable(expandName(variable));

        if (var == null) {
            throw new IllegalArgumentException(
                    "An attempt was made to get an unknown variable " +
                    variable);
        } else {
            return var.getValue();
        }
    }

    /**
     * Return the variable stack frame count.
     *
     * @return the count
     */
    public int getStackFrameCount() {
        return (frames == null) ? 0 : frames.size();
    }

    /**
     * Given a variable string return an ExpandedName that represents the
     * exanded variable (i.e. namespace prefixes will have been mapped to an
     * actual namespaceURI.)
     *
     * @param name the name of the variable
     * @return the {@link ExpandedName} that represents the expanded variable.
     * @throws IllegalArgumentException if the variable is prefixed and an
     *                                  NamespacePrefixTracker is not available
     *                                  or if the variable name is null or
     *                                  empty
     */
    private ExpandedName expandName(String name) {
        ExpandedName expandedName = null;

        if (name == null) {
            throw new IllegalArgumentException(
                    "Cannot expand a null variable name");
        } else if (name.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "Cannot expand an empty variable name");
        } else if ((namespacePrefixTracker == null) &&
                (name.indexOf(':') != -1)) {
            throw new IllegalArgumentException(
                    "Cannot expand the variable " + name +
                    " as no NamespacePrefixTracker has been defined");
        } else if (namespacePrefixTracker == null) {
            // The "default namespace" is used to represent the "no namespace"
            // for variables: see comment below
            expandedName = new ImmutableExpandedName("",
                                                     name);
        } else {
            // The given name could be a prefixed name, so go through these
            // hoops to ensure that we register the variable against the
            // correct namespace (the namespace for no prefix is "no namespace"
            // according to http://www.w3.org/TR/xpath20/#id-variables - this
            // is modelled using the "default namespace" for variables)
            expandedName = namespacePrefixTracker.resolveQName(
                    new ImmutableQName(name), "");
        }

        return expandedName;
    }

    // javadoc inherited from RecoverableTransaction interface
    public void startTransaction() {
        tracker.startTransaction();
        frames.startTransaction();

        if (globalScope instanceof RecoverableExpressionScope) {
            ((RecoverableExpressionScope)globalScope).startTransaction();
        } else {
            logger.warn("expression-scope-is-not-recoverable", globalScope);
        }

        Iterator iterator = frames.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof RecoverableExpressionScope) {
                ((RecoverableExpressionScope)object).startTransaction();
            } else {
                logger.warn("expression-scope-is-not-recoverable", object);
            }
        }
    }

    // javadoc inherited from RecoverableTransaction interface
    public void commitTransaction() {
        tracker.commitTransaction();
        frames.commitTransaction();

        if (globalScope instanceof RecoverableExpressionScope) {
            ((RecoverableExpressionScope)globalScope).startTransaction();
        }

        Iterator iterator = frames.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof RecoverableExpressionScope) {
                ((RecoverableExpressionScope)object).startTransaction();
            }
        }
    }

    // javadoc inherited from RecoverableTransaction interface
    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        frames.rollbackTransaction();

        if (globalScope instanceof RecoverableExpressionScope) {
            ((RecoverableExpressionScope)globalScope).startTransaction();
        } else {
            logger.warn("expression-scope-is-not-recoverable", globalScope);
        }

        Iterator iterator = frames.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof RecoverableExpressionScope) {
                ((RecoverableExpressionScope)object).startTransaction();
            } else {
                logger.warn("expression-scope-is-not-recoverable", object);             
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 31-Jul-03	222/5	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 18-Jun-03	100/1	sumit	VBM:2003061602 Converted all references to org.apache to our.apache

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
