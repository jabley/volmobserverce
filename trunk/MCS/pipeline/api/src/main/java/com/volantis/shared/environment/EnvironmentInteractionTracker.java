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

package com.volantis.shared.environment;

/**
 * Encapsulates a stack of environment interactions.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <p>An environment interaction is an abstract representation of a request
 * (and matching response) that results in a task being performed. It is
 * possible that the environment interaction will change while performing the
 * task. This interface allows those changes to the environment interaction
 * to be tracked.</p>
 * <p>The root environment interaction is the one that corresponds to the
 * underlying request that was the original trigger for the task to be
 * performed.</p>
 *
 * <h2>J2EE environment</h2>
 *
 * <p>The following uses the J2EE environment as an illustration of how the
 * environment interaction may change.</p>
 * <p>In the J2EE environment an environment interaction is needed for each
 * servlet request (including JSPs). While processing a request a servlet can
 * make use of (include) other servlets, those included servlets may be given
 * a servlet request and response that is related to the original but may
 * contain different headers or parameters. This means that each of those
 * servlets needs to create its own environment interaction object that wraps
 * the request and response that it is given. e.g.</p>
 * <ol>
 * <li>A request is received by the server that causes Servlet A to be invoked.
 * </li>
 * <li>Servlet A creates an environment interaction E1 and pushes it onto the
 * stack of interactions maintained by this object.</li>
 * <li>Servlet A does some work and then includes JSP B which creates its own
 * environment interaction object E2 and pushes it onto the stack.</li>
 * <li>JSP B pops E2 off the stack once it has finished.</li>
 * <li>Servlet A pops E1 off the stack once it has finished.</li>
 * </ol>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface EnvironmentInteractionTracker {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Push a new environment interaction.
     * <p>This invokes {@link com.volantis.shared.environment.EnvironmentInteraction#setParentInteraction} to
     * set the parent interaction and then makes this the current interaction.
     * </p>
     * @param environmentInteraction The new current environment interaction,
     * must not be null.
     */
    void pushEnvironmentInteraction(EnvironmentInteraction environmentInteraction);

    /**
     * Pop the current environment interaction from the stack of environment
     * interactions.
     * @return The popped environment interaction, must not be null.
     */
    EnvironmentInteraction popEnvironmentInteraction();

    /**
     * Get the current environment specific interaction that the pipeline is
     * part of.
     * @return The EnvironmentInteraction object.
     */
    EnvironmentInteraction getCurrentEnvironmentInteraction();

    /**
     * Get the root environment interaction, this is the first one that was
     * pushed onto the stack.
     * @return The root environment interaction, may be null if the stack is
     * empty.
     */
    EnvironmentInteraction getRootEnvironmentInteraction();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 24-Jul-03	252/1	doug	VBM:2003072403 Implemented the EnvironmentInteractionTracker interface

 ===========================================================================
*/
