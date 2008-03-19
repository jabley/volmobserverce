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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Tracking;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.environment.RecoverableEnvironmentInteractionTracker;
import com.volantis.shared.recovery.TransactionTracker;
import com.volantis.shared.resource.RecoverableResourceOwnerStack;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.namespace.RecoverableNamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.RecoverableComplexPropertyContainer;
import com.volantis.xml.expression.impl.ExpressionDependencyContext;
import com.volantis.xml.expression.RecoverableExpressionContext;
import com.volantis.xml.expression.InternalExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.PositionScope;

/**
 * Abstract implementation of the ExpressionContext interface providing
 * the environment-independent aspects of the interface's functionality.
 *
 * <p>For simplicity of implementation the position scope object is stored
 * in the property container (which will therefore handle recovery correctly
 * without additional implementation). It is intentionally keyed on a value
 * that should not clash with any other uses of the container.</p>
 */
public abstract class SimpleExpressionContext
        implements RecoverableExpressionContext, InternalExpressionContext {
    /**
     * Used to localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    SimpleExpressionContext.class);

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(SimpleExpressionContext.class);

    /**
     * The factory by which this context was created.
     */
    protected final ExpressionFactory factory;

    /**
     * The environment interaction tracker supplied by an external party.
     */
    private final EnvironmentInteractionTracker environmentInteractionTracker;

    /**
     * The namespace prefix tracker supplied by an external party.
     */
    private final NamespacePrefixTracker namespacePrefixTracker;

    /**
     * Properties particular to this context.
     */
    private final RecoverableComplexPropertyContainer propertyContainer;

    /**
     * Stack that holds arbitary objects
     */
    private final RecoverableResourceOwnerStack objectStack;
//    private final RecoverableResourceOwnerStack objects;
//
    /**
     * A TransactionTracker to determine our transaction state.
     */
    private final TransactionTracker tracker = new TransactionTracker();
    private DependencyContext dependencyContext;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory                       the factory by which this context
     *                                      was created
     * @param environmentInteractionTracker the environment interaction tracker
     *                                      to be used and published by this
     *                                      context
     * @param namespacePrefixTracker        the namespace prefix tracker to be
     *                                      used and published by this context
     */
    public SimpleExpressionContext(
            ExpressionFactory factory,
            EnvironmentInteractionTracker environmentInteractionTracker,
            NamespacePrefixTracker namespacePrefixTracker) {
        this.factory = factory;
        this.environmentInteractionTracker = environmentInteractionTracker;
        this.namespacePrefixTracker = namespacePrefixTracker;

        // Delegate the PropertyContainer functionality
        propertyContainer = new RecoverableComplexPropertyContainer();

        objectStack = new RecoverableResourceOwnerStack();

        // Ensure that there is always a position scope
        pushPositionScope();

        dependencyContext = new ExpressionDependencyContext(this);
        dependencyContext.setProperty(ExpressionContext.class, this);


        // Push an ignoring dependency tracker to make sure that one is always
        // available.
        dependencyContext.pushDependencyTracker(Tracking.DISABLED);
    }

    // javadoc inherited
    public ExpressionFactory getFactory() {
        return factory;
    }

    // javadoc inherited
    public NamespacePrefixTracker getNamespacePrefixTracker() {
        return namespacePrefixTracker;
    }

    // javadoc inherited
    public EnvironmentInteractionTracker getEnvironmentInteractionTracker() {
        return environmentInteractionTracker;
    }

    public DependencyContext getDependencyContext() {
        return dependencyContext;
    }

    // javadoc inherited
    public void setProperty(Object key, Object value,
                            boolean releaseOnChange) {
        propertyContainer.setProperty(key, value, releaseOnChange);
    }

    // javadoc inherited
    public Object getProperty(Object key) {
        return propertyContainer.getProperty(key);
    }

    // javadoc inherited
    public Object removeProperty(Object key) {
        return propertyContainer.removeProperty(key);
    }

    public void release() {
        propertyContainer.release();
    }

    // javadoc inherited from RecoverableTransaction interface.
    public void startTransaction() {
        tracker.startTransaction();
        propertyContainer.startTransaction();
        objectStack.startTransaction();

        if (environmentInteractionTracker
                instanceof RecoverableEnvironmentInteractionTracker) {
            ((RecoverableEnvironmentInteractionTracker)
                    environmentInteractionTracker).startTransaction();
        } else {
            logger.warn("error-recovery-may-fail",
                        "EnvironmentInteractionTracker");


        }

        if (namespacePrefixTracker
                instanceof RecoverableNamespacePrefixTracker) {
            ((RecoverableNamespacePrefixTracker)
                    namespacePrefixTracker).startTransaction();
        } else {
            logger.warn("error-recovery-may-fail",
                        "NamespacePrefixTracker");
        }

    }

    // javadoc inherited from RecoverableTransaction interface.
    public void commitTransaction() {
        tracker.commitTransaction();
        propertyContainer.commitTransaction();
        objectStack.commitTransaction();

        if (environmentInteractionTracker
                instanceof RecoverableEnvironmentInteractionTracker) {
            ((RecoverableEnvironmentInteractionTracker)
                    environmentInteractionTracker).commitTransaction();
        }

        if (namespacePrefixTracker
                instanceof RecoverableNamespacePrefixTracker) {
            ((RecoverableNamespacePrefixTracker)
                    namespacePrefixTracker).commitTransaction();
        }
    }

    // javadoc inherited from RecoverableTransaction interface.
    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        propertyContainer.rollbackTransaction();
        objectStack.rollbackTransaction();

        if (environmentInteractionTracker
                instanceof RecoverableEnvironmentInteractionTracker) {
            ((RecoverableEnvironmentInteractionTracker)
                    environmentInteractionTracker).rollbackTransaction();
        } else {
            logger.warn("error-recovery-may-have-failed",
                        "EnvironmentInteractionTracker");
        }

        if (namespacePrefixTracker
                instanceof RecoverableNamespacePrefixTracker) {
            ((RecoverableNamespacePrefixTracker)
                    namespacePrefixTracker).rollbackTransaction();
        } else {
            logger.warn("error-recovery-may-have-failed",
                        "NamespacePrefixTracker");
        }
    }

    // javadoc inherited
    public PositionScope getPositionScope() {
        return (PositionScope) propertyContainer.getProperty(
                SimplePositionScope.class);
    }

    // javadoc inherited
    public void popPositionScope() {
        final PositionScope previous =
                ((SimplePositionScope)propertyContainer.getProperty(
                                SimplePositionScope.class)).getPrevious();

        if (previous == null) {
            throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("illegal-stack-pop",
                                               "PositionScope"));
        }
        
        propertyContainer.setProperty(
                SimplePositionScope.class,
                previous,
                false);

    }

    // javadoc inherited
    public void pushPositionScope() {
        propertyContainer.setProperty(
                SimplePositionScope.class,
                new SimplePositionScope(
                        (PositionScope) propertyContainer.getProperty(
                                SimplePositionScope.class)),
                false);
    }

    public RecoverableComplexPropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    public RecoverableResourceOwnerStack getObjectStack() {
        return objectStack;
    }

    // javadoc inherited
    public void pushObject(Object object, boolean releaseOnPop) {
        objectStack.push(object, releaseOnPop);
    }

    // javadoc inherited
    public Object popObject() {
        return objectStack.pop();
    }

    // Javadoc inherited.
    public Object popObject(Object expected) {
        Object popped = objectStack.pop();
        if (popped != expected) {
            throw new IllegalStateException("Expected " + expected +
                    " to be popped but found " + popped);
        }

        return popped;
    }

    // javadoc inherited
    public Object getCurrentObject() {
        return (objectStack.size() == 0) ? null : (Object)objectStack.peek();
    }

    // javadoc inherited
    public Object findObject(Class clazz) {
        return objectStack.find(clazz);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/3	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
