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

package com.volantis.xml.pipeline.sax.impl.template;

import java.util.HashMap;

/**
 * This typesafe enumeration defines how template parameter definitions should
 * be evaluated during pipeline processing. Evaluation means whether or not
 * pipeline preprocessing should be performed.
 */
public class EvaluationMode {
    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the EvaluationMode equivalent.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     */
    private static final HashMap entries = new HashMap();

    /**
     * All pipeline markup should be evaluated at the time of definition of
     * a template parameter value.
     */
    public static final EvaluationMode IMMEDIATE = new EvaluationMode(
            "immediate");

    /**
     * Pipeline markup should be passed through unevaluated at the time of
     * definition of a template parameter value and should be evaluated once
     * and only once when the template parameter value is first used.
     */
    public static final EvaluationMode DEFERRED = new EvaluationMode(
            "deferred");

    /**
     * Pipeline markup should be passed through unevaluated at the time of
     * definition of a template parameter value and should be evaluated
     * each time the template parameter is used.
     */
    public static final EvaluationMode REPEATED = new EvaluationMode(
            "repeated");

    /**
     * The internal name of the enumeration literal.
     */
    private final String name;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param name the internal name of the enumeration literal
     */
    private EvaluationMode(String name) {
        this.name = name;

        entries.put(name, this);
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static EvaluationMode literal(String name) {
        return (EvaluationMode) entries.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jun-03	88/1	sumit	VBM:2003061303 Changed enumeration constants to be final

 10-Jun-03	13/2	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/
