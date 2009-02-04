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

package com.volantis.mcs.runtime.plugin.markup;

import java.util.HashMap;
import java.util.Map;

/**
 * This typesafe enumeration class defines the different methods of
 * MarkupPlugins.
 *
 * There are three static values, one for each possible scope:
 * <ul>
 * <li> {@link #INITIALIZE} for
 *      {@link MarkupPlugin#initialize}
 * <li> {@link #PROCESS} for
 *      {@link MarkupPlugin#process}
 * <li> {@link #RELEASE} for
 *      {@link MarkupPlugin#release}
 * </ul>
 */
public final class MarkupPluginMethod {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the MarkupPluginMethod equivalent.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     * @associates <{MarkupPluginMethod}>
     * @supplierRole methods
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static Map methods = new HashMap(3);

    /**
     * The internal name of the enumeration literal.
     */
    private String method;

    /**
     * The MarkupPluginMethod which represents the method
     * {@link MarkupPlugin#initialize}
     */
    public static final MarkupPluginMethod INITIALIZE =
            new MarkupPluginMethod("initialize");

    /**
     * The MarkupPluginMethod which represents the method
     * {@link MarkupPlugin#process}
     */
    public static final MarkupPluginMethod PROCESS =
            new MarkupPluginMethod("process");

    /**
     * The MarkupPluginMethod which represents the method
     * {@link MarkupPlugin#release}
     */
    public static final MarkupPluginMethod RELEASE =
            new MarkupPluginMethod("release");

    /**
     * Create a new instance of MarkupPluginScope.  This constructor is private
     * to prevent further instances from being created.
     * @param scope The literal value of the markup plugin scope.
     */
    private MarkupPluginMethod(String scope) {
        this.method = scope;
        methods.put(scope, this);
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return method;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param scope the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static MarkupPluginMethod literal(String scope) {
        return (MarkupPluginMethod) methods.get(scope);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/3	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
