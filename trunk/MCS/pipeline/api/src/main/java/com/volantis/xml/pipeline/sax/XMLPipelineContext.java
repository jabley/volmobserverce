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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Doug            VBM:2003030405 - Created. The XMLPipelineContext
 *                              class is used to share pipeline information
 *                              amongst XMLProcesses in the pipeline.
 * 06-May-03    Phil W-S        VBM:2003030610 - Add the ability to associate
 *                              arbitrary values with the context using
 *                              arbitrary keys to identify the values.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.expression.ExpressionContext;
import org.xml.sax.Locator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Contains contextual information that is needed by processes within the
 * pipeline.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <p>Processes can be added to the pipeline after it has been running which
 * means that they may have missed some of the events that carry contextual
 * information that may be needed in order to interpret other events,
 * e.g. setDocumentLocator, startPrefixMapping, etc.
 * This class provides access to that information.</p>
 * <p>Each pipeline has its own instance of this object which is created when
 * the pipeline is created and discarded when the pipeline is discarded. Nested
 * pipelines use the same instance of this class as the containing pipeline.</p>
 * <p>In addition to the normal SAX contextual information this class also
 * provides facilities for pipeline objects such as XMLProcesses to communicate
 * with each other.</p>
 *
 * <h1>XML Base</h1>
 * <p>The XML Base standard defines a mechanism to allow page authors to specify
 * the base URI used when resolving relative URIs. This interface provides
 * support for managing them using the following methods.</p>
 * <ul>
 *   <li>{@link #pushBaseURI}</li>
 *   <li>{@link #popBaseURI}</li>
 *   <li>{@link #getCurrentBaseURI}</li>
 *   <li>{@link #pushLocator}</li>
 *   <li>{@link #popLocator}</li>
 *   <li>{@link #getCurrentLocator}</li>
 * </ul>
 *
 * <h1>Namespace mappings</h1>
 * todo: document this
 *
 * <h1>Environment Interaction Tracker</h1>
 * <p>The {@link EnvironmentInteractionTracker} is used to keep track of the
 * environment interactions that affect the pipeline.</p>
 *
 * <h1>Object Stack</h1>
 * <p>The object stack can be used by objects (processes, dynamic element rules,
 * etc) to communicate and cooperate with one another.</p>
 * <p>The {@link ResourceOwner#release} method will be invoked on stack objects
 * that implement the interface when the object is popped off the stack.</p>
 * <p>While pushing an {@link XMLProcess} on the object stack is allowed it is
 * not recommended as it can cause problems. An {@link XMLProcess} is a
 * {@link ResourceOwner} and so popping it off the stack could
 *
 * <h1>Custom Properties</h1>
 * <p>These are used to allow pipeline objects (processes, rules) to
 * communicate and cooperate with both the user of the pipeline and other
 * pipeline objects. See {@link PropertyContainer} for more details.</p>
 * <p>The {@link ResourceOwner#release} method will be invoked on property
 * values that implement the interface if the property is changed or removed.
 * </p>
 *
 * <h1>Flow Control</h1>
 * <p>Conditional content and error recovery require the ability to skip over
 * events within the pipeline until a certain point within the event stream is
 * reached at which time the pipeline carries on processing as normal. The
 * mechanism to support this is called flow control.</p>
 *
 * <h1>Error Recovery</h1>
 * <p>As part of error recovery this object provides support for recovering
 * back to a well known state. This can involve discarding property values and
 * objects on the object stack. The {@link ResourceOwner#release} method will
 * be invoked for those property values and stack objects that implement that
 * interface and are discarded during error recovery.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * 
 * @mock.generate
 */
public interface XMLPipelineContext
        extends PropertyContainer, ResourceOwner {

    /**
     * Get the pipeline configuration that was used to create this pipeline.
     * @return The pipeline configuration that was used to create this pipeline.
     */
    public XMLPipelineConfiguration getPipelineConfiguration();

    /**
     * Get the XMLPipelineFactory that was used to create this pipeline and
     * should be used to create any additional pipeline objects.
     * @return The XMLPipelineFactory used to create this pipeline.
     */
    public XMLPipelineFactory getPipelineFactory();

    /**
     * Pushes a new base URI onto the stack.
     * <p>The first time that this is called the baseURI must be an absolute
     * path and must also be a valid {@link URL}. Subsequent calls can pass in
     * either absolute or relative URIs. Relative URIs are resolved against the
     * current base URI before they are pushed onto the stack.</p>
     *
     * @param baseURI The new base URI.
     * @throws MalformedURLException
     */
    public void pushBaseURI(String baseURI)
            throws MalformedURLException;

    /**
     * Pops the current base URI from the stack and makes the previous base URI
     * the current one.
     * @return The popped base URI.
     */
    public URL popBaseURI();

    /**
     * Get the current base URI.
     * @return The current base URI.
     */
    public URL getCurrentBaseURI();

    /**
     * Push a SAX Locator onto the stack.
     * <p>The systemId referenced by the Locator is pushed onto the stack of
     * base URIs.</p>
     * <p>It is illegal if the same Locator object is pushed twice without it
     * being popped in between.</p>
     * @param locator The locator to push.
     */
    public void pushLocator(Locator locator);

    /**
     * Pop the current SAX Locator from the stack.
     * <p>It also pops the current base uri off the stack.</p>
     * @return The popped Locator.
     */
    public Locator popLocator();

    /**
     * Get the current locator.
     * <p>The restrictions on accessing Locators detailed in the
     * {@link org.xml.sax.ContentHandler} documentation also apply to Locators
     * retrieved by this method.</p>
     * @return The current document locator.
     */
    public Locator getCurrentLocator();

    /**
     * Iterate over the locator stack.
     * <p>The returned iterator does not support the {@link Iterator#remove}
     * method. It iterates through the locators starting with the most recent
     * one (the current one) and ending with the first one.</p>
     * @return A non modifying Iterator over the Locator objects.
     */
    public Iterator getLocators();

    /**
     * Get the environment interaction tracker that is used by this object.
     * @return The EnvironmentInteractionTracker used by this object.
     */
    public EnvironmentInteractionTracker getEnvironmentInteractionTracker();

    /**
     * Pushes an object onto the stack of objects.
     * @param object       The object to push onto the stack.
     * @param releaseOnPop Indicates whether any resources owned by the popped
     *                     object should be released or not. If this is true
     *                     and the object implements {@link ResourceOwner} then
     *                     its <code>release</code> method is invoked after it
     *                     is popped and before it is returned, otherwise it is
     *                     not.
     */
    public void pushObject(Object object, boolean releaseOnPop);

    /**
     * Pop the object from the stack of objects.
     *
     * @return The popped object.
     *
     * @see #pushObject
     */
    public Object popObject();

    /**
     * Pop the object from the stack of objects.
     *
     * @param expected The expected object,
     * @return The popped object.
     * @throws IllegalStateException If the popped object does not match the
     *                               expected one.
     * @see #pushObject
     */
    public Object popObject(Object expected);

    /**
     * Get the current object from the stack of objects.
     * <p>This method is here for completeness but should probably not be
     * called. Use the {@link #findObject} for maximum flexibility.</p>
     *
     * @return The current object.
     */
    public Object getCurrentObject();

    /**
     * Find the object on the stack that is of the specified class.
     * <p>This method checks each object in the stack starting with the last one
     * pushed to see whether it is of the specified class. An object is of the
     * specified class if it is an instance of that class as defined by
     * {@link Class#isInstance}.</p>
     * @param clazz The class of the object to find.
     * @return The first object of the specified class, or null if none could be
     * found.
     */
    public Object findObject(Class clazz);

    /**
     * Returns the unique prefix for all the debug output files produced by
     * the associated pipeline. If it is not specified then all of the debug
     * operations are treated as inactive. If the prefix ends with a / then it
     * specifies a unique directory, otherwise it specifies a directory and a
     * prefix for files within that directory.
     * @return the debug output prefix
     */
    public String getDebugOutputFilesPrefix();

    /**
     * Set the unique prefix for all the debug output files produced by
     * the associated pipeline. If it is not specified then all of the debug
     * operations are treated as inactive. If the prefix ends with a / then it
     *  specifies a unique directory, otherwise it specifies a directory and a
     * prefix for files within that directory.
     * @param debugOutputPrefix the debug output prefix
     */
    public void setDebugOutputFilesPrefix(String debugOutputPrefix);

    /**
     * Provides access to the lower level namespace prefix mapping facilities.
     * <p>The returned object should not be cached between events, or other
     * calls to the pipeline as it may change.</p>
     * @return The NamespacePrefixTracker in use by this context.
     */
    public NamespacePrefixTracker getNamespacePrefixTracker();

    /**
     * Get the flow control manager that is responsible for flow control in
     * this pipeline context.
     * @return The flow control manager.
     */
    public FlowControlManager getFlowControlManager();

    /**
     * Get the expression context associated with this context.
     * @return The expression context.
     */
    public ExpressionContext getExpressionContext();

    /**
     * Get the dependency context.
     *
     * <p>The stack of dependency trackers managed by the
     * {@link DependencyContext} use the stack of objects maintained by this
     * context in order to support recovery. As a result calls to manipulate
     * the stacks must be well formed.</p>
     * 
     * @return The dependency context.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    DependencyContext getDependencyContext();

    /**
     * Enters error recovery mode.
     * @throws IllegalStateException If the pipeline is already in error
     * recovery mode.
     */
    public void enterErrorRecoveryMode()
            throws IllegalStateException;

    /**
     * Checks to see whether we are in error recovery mode.
     * @return True if the pipeline is in error recovery mode and false
     * otherwise.
     */
    public boolean inErrorRecoveryMode();

    /**
     * Exit error recovery mode.
     * @throws IllegalStateException If the pipeline is not in error recovery
     * mode.
     */
    public void exitErrorRecoveryMode()
            throws IllegalStateException;

    /**
     * Add a recovery point in the pipeline context.
     * <p>This method causes the pipeline context to remember the current state
     * so that it can revert back to it if necessary, such as part of error
     * recovery. All state accessible through this interface is affected,
     * e.g. namespace prefix mappings, expression context, ....</p>
     * <p>A pipeline context supports multiple recovery points that are stacked
     * one on top of another. Each recovery point defines a boundary between
     * two pipeline context deltas, unless it is the first one in which case it
     * defines a boundary between a delta and the pipeline context state. e.g.
     * </p>
     * <pre>
     *     (top of the stack)
     *     Pipeline Context Delta
     *     Recovery Point
     *     Pipeline Context Delta
     *     Recovery Point
     *     Pipeline Context State
     *     (bottom of the stack)
     * </pre>
     * <p>The addition of recovery points must not affect the perceived
     * behaviour of a correctly used context. However, it is possible that an
     * incorrectly used context may behave differently when recovery points are
     * used. e.g. if the interleaving of calls to push objects on and off the
     * stack and calls to add and remove recovery points is not well formed
     * then an error will occur. e.g. If a process calls {@link #pushObject}
     * then {@link #addRecoveryPoint} when it receives a
     * {@link XMLProcess#startElement} event then it must call
     * {@link #removeRecoveryPoint} then {@link #popObject} when it receives
     * a {@link XMLProcess#endElement} event.</p>
     * <p>The returned recovery point must be used to remove the recovery point
     * from the context.</p>
     * @return The object that identifies the recovery point within the context.
     */
    public RecoveryPoint addRecoveryPoint();

    /**
     * Remove the specified recovery point.
     * <p>This method removes the recovery point from the pipeline context
     * and specifies the action that should be taken with the changes that have
     * been made in the pipeline since that recovery point was set.</p>
     * <p>The changes can either be kept, or they can be discarded. If the
     * changes are kept then the recovery point must be the top one on the
     * stack, otherwise an {@link IllegalStateException} is thrown. Keeping the
     * changes involves adding the delta to the delta (or state) underneath (see
     * {@link #addRecoveryPoint}) the recovery point.</p>
     * <p>If the changes are discarded then the delta above the recovery point
     * is discarded. If that delta contained custom properties or objects in
     * the object stack that owned resources then those resources are released.
     * If the recovery point is not the top one on the stack then the top delta
     * is discarded until it is.</p>
     * @param keepChanges If true then keeps the changes, otherwise discards
     * them.
     * @throws IllegalArgumentException If action is null.
     * @throws IllegalStateException If the action was to keep the changes but
     * the recovery point was not the top one on the stack.
     */
    public void removeRecoveryPoint(RecoveryPoint recoveryPoint,
                                    boolean keepChanges);

    /**
     * Identifies a recovery point within the pipeline context.
     *
     * <p><strong>Warning: This is a facade provided for use by user code, not for
     * implementation by user code. User implementations of this interface are
     * highly likely to be incompatible with future releases of the product at both
     * binary and source levels.</strong></p>
     */
    public interface RecoveryPoint {

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/2	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 31-Jul-03	222/5	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 28-Jul-03	232/8	doug	VBM:2003071804 Tidied up comments in XMLPipelineContext

 28-Jul-03	232/6	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 25-Jun-03	102/4	sumit	VBM:2003061906 request:getParameter XPath function support

 23-Jun-03	90/4	adrian	VBM:2003061606 fixed conflicts

 19-Jun-03	90/1	adrian	VBM:2003061606 Added Expression support to Tag attributes

 20-Jun-03	104/2	adrian	VBM:2003061801 Updated OperationProcess stack mechanism

 13-Jun-03	78/1	philws	VBM:2003061205 Added some JSP test pages and fixed some tag bugs

 06-Jun-03	26/7	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
