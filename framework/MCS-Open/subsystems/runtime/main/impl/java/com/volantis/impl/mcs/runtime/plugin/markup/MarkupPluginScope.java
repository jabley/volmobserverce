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

package com.volantis.impl.mcs.runtime.plugin.markup;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * This typesafe enumeration class defines the different scopes of
 * MarkupPlugins.
 *
 * There are three static values, one for each possible scope:
 * <ul>
 * <li> {@link #APPLICATION} for application scope.
 * <li> {@link #SESSION} for session scope.
 * <li> {@link #CANVAS} for canvas scope.
 * </ul>
 */
public class MarkupPluginScope {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the MarkupPluginScope equivalent.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     * @associates <{MarkupPluginScope}>
     * @supplierRole scopes
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static Map scopes = new HashMap(3);

    /**
     * The internal name of the enumeration literal.
     */
    private String scope;
    private final ContainerLocator locator;
    private final boolean application;

    /**
     * The MarkupPluginScope which represents application level scope.
     */
    public static final MarkupPluginScope APPLICATION =
            new MarkupPluginScope("application",
                                  new ApplicationContainerLocator(),
                                  true);

    /**
     * The MarkupPluginScope which represents session level scope.
     */
    public static final MarkupPluginScope SESSION =
            new MarkupPluginScope("session", new SessionContainerLocator(),
                                  false);

    /**
     * The MarkupPluginScope which represents canvas level scope.
     */
    public static final MarkupPluginScope CANVAS =
            new MarkupPluginScope("canvas", new CanvasContainerLocator(),
                                  false);

    /**
     * Create a new instance of MarkupPluginScope.  This constructor is private
     * to prevent further instances from being created.
     * @param scope The literal value of the markup plugin scope.
     */
    private MarkupPluginScope(String scope, ContainerLocator locator,
                              boolean application) {
        this.scope = scope;
        this.locator = locator;
        this.application = application;
        scopes.put(scope, this);
    }

    /**
     * Indicates whether the scope is an application scope object.
     *
     * @return True if it is and false otherwise.
     */
    public boolean isApplication() {
        return application;
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return scope;
    }

    public ContainerLocator getLocator() {
        return locator;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param scope the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static MarkupPluginScope literal(String scope) {
        return (MarkupPluginScope) scopes.get(scope);
    }

    private static class ApplicationContainerLocator
            implements ContainerLocator {

        public boolean isApplication() {
            return true;
        }

        /**
         * Get the application level container.
         */
        public MarkupPluginContainer getContainer(
                MarinerRequestContext requestContext) {

            MarinerApplication application
                    = requestContext.getMarinerApplication();
            Volantis volantis
                    = ApplicationInternals.getVolantisBean(application);
            MarkupPluginContainer container
                    = volantis.getMarkupPluginContainer();

            return container;
        }
    }

    private static class SessionContainerLocator
            implements ContainerLocator {

        public boolean isApplication() {
            return false;
        }

        /**
         * Get the session level container.
         */
        public MarkupPluginContainer getContainer(
                MarinerRequestContext requestContext) {

            MarkupPluginContainer container
                    = ContextInternals
                    .getEnvironmentContext(requestContext)
                    .getCurrentSessionContext()
                    .getMarkupPluginContainer();

            return container;
        }
    }

    private static class CanvasContainerLocator
            implements ContainerLocator {

        public boolean isApplication() {
            return false;
        }

        /**
         * Get the canvas level container.
         */
        public MarkupPluginContainer getContainer(
                MarinerRequestContext requestContext) {

            MarkupPluginContainer container
                    = ContextInternals
                    .getMarinerPageContext(requestContext)
                    .getMarkupPluginContainer();

            return container;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/6	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
