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
package com.volantis.xml.pipeline.sax.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.recovery.RecoverableTransactionStack;
import com.volantis.shared.recovery.TransactionTracker;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.RecoverableExpressionContext;
import com.volantis.xml.expression.impl.SimpleExpressionContext;
import com.volantis.xml.expression.impl.SimpleExpressionContext;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.InternalXMLPipelineFactory;
import com.volantis.xml.pipeline.sax.RecoverableXMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Locator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Stack;

/**
 * Basic implementation of the XMLPipelineContext interface
 */
public class XMLPipelineContextImpl implements RecoverableXMLPipelineContext {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XMLPipelineContextImpl.class);

    /**
     * Keeps track of transaction levels.
     */
    private final TransactionTracker tracker;

    /**
     * Stack used to store the base URI's of an XML Infoset as it is
     * processed.
     */
    private final RecoverableTransactionStack baseURIs;

    /**
     * Stack used to store the Locators encountered as a XML Infoset
     * is processed
     */
    private final RecoverableTransactionStack locators;

    /**
     * Stack that holds arbitary objects
     */
//    private final RecoverableResourceOwnerStack objects;

//    /**
//     * Holds the arbirary properties, keyed on arbitrary objects.
//     */
//    private final RecoverableComplexPropertyContainer properties;

    /**
     * ExpressionContext used for managing data that is required when
     * evaluating XPath expressions
     */
    private final SimpleExpressionContext expressionContext;

    /**
     * An instance of an XMLPipelineFactory
     */
    private final XMLPipelineFactory pipelineFactory;

    /**
     * The Pipeline Configuration
     */
    private final XMLPipelineConfiguration pipelineConfiguration;

    /**
     * Manages the flow off events through the pipeline
     */
    private final FlowControlManager flowControlManager;

    /**
     * A stack of recovery points to allow us to track transaction nesting.
     */
    private final Stack recoveryPoints = new Stack();

    /**
     * Determine whether this object is in an error recovery state.
     */
    private boolean inRecoveryMode = false;

    /**
     * the unique prefix for all the debug output files produced by
     * the associated pipeline. If it is not specified then all of the debug
     * operations are treated as inactive. If the prefix ends with a / then it
     * specifies a unique directory, otherwise it specifies a directory and a
     * prefix for files within that directory.
     */
    private String debugOutputFilesPrefix;


    /**
     * Creates a new XMLPipelineContextImpl instance. The
     * <code>NamespacePrefixTracker</code> that this instance manages will
     * be the one returned from the
     * {@link ExpressionContext#getNamespacePrefixTracker}. Likewise, the
     * EnvironmentInteractionTracker that this XMLPipelineContext manages
     * will be the tracker returned from
     * {@link ExpressionContext#getEnvironmentInteractionTracker}
     * @param pipelineFactory An XMLPipelineFactory instance
     * @param pipelineConfiguration The XMLPipelineConfiguration
     * @param expressionContext the ExpressionContext
     */
    public XMLPipelineContextImpl(
            InternalXMLPipelineFactory pipelineFactory,
            XMLPipelineConfiguration pipelineConfiguration,
            ExpressionContext expressionContext) {

        this.pipelineFactory = pipelineFactory;
        this.pipelineConfiguration = pipelineConfiguration;
        this.expressionContext = (SimpleExpressionContext) expressionContext;

        flowControlManager = pipelineFactory.createFlowControlManager();
        baseURIs = new RecoverableTransactionStack();
        locators = new RecoverableTransactionStack();
//        objects = this.expressionContext.getObjectStack();
//        properties = new RecoverableComplexPropertyContainer();

        tracker = new TransactionTracker();

        DependencyContext dependencyContext =
                expressionContext.getDependencyContext();
        dependencyContext.setProperty(XMLPipelineContext.class, this);
    }

    // javadoc inherited
    public XMLPipelineConfiguration getPipelineConfiguration() {
        return pipelineConfiguration;
    }

    // javadoc inherited
    public XMLPipelineFactory getPipelineFactory() {
        return pipelineFactory;
    }

    // javadoc inherited
    public EnvironmentInteractionTracker getEnvironmentInteractionTracker() {
        return expressionContext.getEnvironmentInteractionTracker();
    }

    // javadoc inherited
    public void pushBaseURI(String baseURI)
            throws MalformedURLException {

        // get hold of the current base URL, this could be null.
        URL base = getCurrentBaseURI();
        if (null != baseURI) {
            base = new URL(base, baseURI);
        }
        // push the new base URL onto the stack
        baseURIs.push(base);
    }

    // javadoc inherited
    public URL popBaseURI() {
        if (baseURIs.empty()) {
            throw new IllegalStateException("No Base URI to pop");
        }
        return (URL)baseURIs.pop();
    }

    // javadoc inherited
    public URL getCurrentBaseURI() {
        return (baseURIs.size() > 0) ? (URL)baseURIs.peek() : null;
    }

    // javadoc inherited
    public void pushLocator(Locator locator) {
        locators.push(locator);
    }

    // javadoc inherited
    public Locator popLocator() {
        if (locators.empty()) {
            throw new IllegalStateException("No locator to pop");
        }
        return (Locator)locators.pop();
    }

    // javadoc inherited
    public Locator getCurrentLocator() {
        return (locators.size() == 0) ? null : (Locator)locators.peek();
    }

    // javadoc inherited
    /**
     * @return an Iterator for iterating over the registered Locators. The
     * returned iterator does not support the {@link Iterator#remove} method.
     */
    public Iterator getLocators() {
        // get hold of an iterator for the stack of locators
        final Iterator locatorIterator = locators.iterator();
        // wrap this iterator in one that ensures remove cannot be
        // invoked and return this iterator
        return new Iterator() {
            // javadoc inherited
            public boolean hasNext() {
                return locatorIterator.hasNext();
            }

            // javadoc inherited
            public Object next() {
                return locatorIterator.next();
            }

            // javadoc inherited
            public void remove() {
                throw new UnsupportedOperationException(
                        "remove is not supported");
            }
        };
    }

    // javadoc inherited
    public void pushObject(Object object, boolean releaseOnPop) {
        expressionContext.pushObject(object, releaseOnPop);
    }

    // javadoc inherited
    public Object popObject() {
        return expressionContext.popObject();
    }

    // Javadoc inherited.
    public Object popObject(Object expected) {
        return expressionContext.popObject(expected);
    }

    // javadoc inherited
    public Object getCurrentObject() {
        return expressionContext.getCurrentObject();
    }

    // javadoc inherited
    public Object findObject(Class clazz) {
        return expressionContext.findObject(clazz);
    }

    // javadoc inherited
    public String getDebugOutputFilesPrefix() {
        return debugOutputFilesPrefix;
    }

    // javadoc inherited
    public void setDebugOutputFilesPrefix(String debugOutputPrefix) {
        this.debugOutputFilesPrefix = debugOutputPrefix;
    }

    // javadoc inherited
    public NamespacePrefixTracker getNamespacePrefixTracker() {
        return expressionContext.getNamespacePrefixTracker();
    }

    // javadoc inherited
    public FlowControlManager getFlowControlManager() {
        return flowControlManager;
    }

    // javadoc inherited
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public DependencyContext getDependencyContext() {
        return expressionContext.getDependencyContext();
    }

    // javadoc inherited
    public void enterErrorRecoveryMode()
            throws IllegalStateException {
        inRecoveryMode = true;
    }

    // javadoc inherited
    public boolean inErrorRecoveryMode() {
        return inRecoveryMode;
    }

    // javadoc inherited
    public void exitErrorRecoveryMode()
            throws IllegalStateException {
        inRecoveryMode = false;
    }

    // javadoc inherited
    public RecoveryPoint addRecoveryPoint() {
        RecoveryPoint recoveryPoint = new RecoveryPoint() {
        };
        recoveryPoints.push(recoveryPoint);
        startTransaction();
        return recoveryPoint;
    }

    // javadoc inherited
    public void removeRecoveryPoint(RecoveryPoint recoveryPoint,
                                    boolean keepChanges) {
        if (recoveryPoints.contains(recoveryPoint)) {
            Object popped = recoveryPoints.pop();
            while (popped != null && popped != recoveryPoint) {
                endTransaction(keepChanges);
                popped = recoveryPoints.pop();
            }
            endTransaction(keepChanges);
        } else {
            throw new IllegalArgumentException("Cannot remove RecoveryPoint " +
                                               recoveryPoint + " because it unrecognised.");
        }
    }

    /**
     * End the current transaction
     * @param commit if true the transaction is committed otherwise it is
     * rolled back.
     */
    private void endTransaction(boolean commit) {
        if (commit) {
            commitTransaction();
        } else {
            rollbackTransaction();
        }
    }

    // javadoc inherited
    public void release() {
        expressionContext.release();
//        objects.release();
    }

    // javadoc inherited
    public void setProperty(Object key, Object value,
                            boolean releaseOnChange) {
        expressionContext.setProperty(key, value, releaseOnChange);
    }

    // javadoc inherited
    public Object getProperty(Object key) {
        return expressionContext.getProperty(key);
    }

    // javadoc inherited
    public Object removeProperty(Object key) {
        return expressionContext.removeProperty(key);
    }

    // javadoc inheritec from RecoverableTransaction interface
    public void startTransaction() {
        tracker.startTransaction();
        baseURIs.startTransaction();
        locators.startTransaction();
//        objects.startTransaction();

        if (expressionContext instanceof RecoverableExpressionContext) {
            ((RecoverableExpressionContext)
                    expressionContext).startTransaction();
        } else {
            logger.warn("error-recovery-may-fail",
                        "ExpressionContext");
        }
    }

    // javadoc inheritec from RecoverableTransaction interface
    public void commitTransaction() {
        tracker.commitTransaction();
        baseURIs.commitTransaction();
        locators.commitTransaction();
//        objects.commitTransaction();

        if (expressionContext instanceof RecoverableExpressionContext) {
            ((RecoverableExpressionContext)
                    expressionContext).commitTransaction();
        }
    }

    // javadoc inheritec from RecoverableTransaction interface
    public void rollbackTransaction() {
        tracker.rollbackTransaction();
        baseURIs.rollbackTransaction();
        locators.rollbackTransaction();
//        objects.rollbackTransaction();

        if (expressionContext instanceof RecoverableExpressionContext) {
            ((RecoverableExpressionContext)
                    expressionContext).rollbackTransaction();
        } else {
            logger.warn("error-recovery-may-have-failed",
                        "ExpressionContext");            
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/3	adrian	VBM:2003081001 implemented try operation

 10-Aug-03	264/5	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 01-Aug-03	258/4	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 ===========================================================================
*/
