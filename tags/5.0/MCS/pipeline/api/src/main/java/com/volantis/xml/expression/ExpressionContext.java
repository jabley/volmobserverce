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
package com.volantis.xml.expression;

import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.PropertyContainer;
import com.volantis.xml.pipeline.sax.ResourceOwner;

/**
 * Encapsulates all the information needed to execute an expression.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <h2>Current Scope</h2>
 *
 * <p>Initially when the expression context is first created the current scope
 * is the global scope. The global scope is visible from all other scopes
 * within the expression context and so global variables can be accessed from
 * all scopes unless they have been hidden by a variable with the same name in
 * a closer enclosing scope.</p>
 *
 * <h2>Stack Frames</h2>
 *
 * <p>A stack frame is used to hold a set of variables that are independent of
 * variables used within other stack frames. Each stack frame consists of a new
 * scope in which variables can be declared. This scope is connected to the
 * global scope associated with the environment expression but is not connected
 * to scopes from the enclosing stack frame.</p>
 *
 * <h2>Namespace Resolving</h2>
 *
 * <p>It is often necessary within an expression to resolve a prefix name to an
 * expanded name. e.g. variable references typically use {@link
 * com.volantis.xml.namespace.QName}s to refer to variables rather than
 * expanded names. Therefore, when an expression context is created it is given
 * with a namespace prefix resolver to use. It is the responsibility of some
 * external mechanism to ensure that the mappings held by that resolver are
 * update properly.</p>
 *
 * <h1>Custom Properties</h1>
 *
 * <p>See {@link PropertyContainer} for more details.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface ExpressionContext extends PropertyContainer, ResourceOwner {
    /**
     * Returns the factory by which this context was created.
     *
     * @return the factory by which this context was created
     */
    public ExpressionFactory getFactory();

    /**
     * Get the current scope.
     *
     * @return The current scope.
     */
    public ExpressionScope getCurrentScope();

    /**
     * Push a new stack frame onto the stack.
     */
    public void pushStackFrame();

    /**
     * Pop the current stack from the stack.
     */
    public void popStackFrame();

    /**
     * Permits a function to be registered for use within expressions parsed
     * and evaluated within this expression context.
     *
     * @param functionName the name by which the function can be referenced
     *                     within expressions
     * @param function     the function to be invoked if the given functionName
     *                     is referenced within an expression
     */
    public void registerFunction(ImmutableExpandedName functionName,
                                 Function function);

    /**
     * Get the namespace prefix tracker that is used by this expression
     * context.
     *
     * @return The namespace prefix tracker that is used by this expression
     *         context.
     */
    public NamespacePrefixTracker getNamespacePrefixTracker();

    /**
     * Get the environment interaction tracker that is used by this expression
     * context.
     *
     * @return The environment interaction tracker that is used by this
     *         expression context.
     */
    public EnvironmentInteractionTracker getEnvironmentInteractionTracker();

    /**
     * Get the dependency context.
     *
     * @return The dependency context.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    DependencyContext getDependencyContext();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
