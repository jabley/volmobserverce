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

import com.volantis.xml.namespace.ExpandedName;

/**
 * An expression scope defines mappings from names to various types of
 * objects.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <p>Currently the only objects that are stored in scopes are variables.</p>
 *
 * <p>Scopes are nested and if an object cannot be found in one scope then it
 * will look in the enclosing scope. New mappings are added to the current
 * scope and will be removed when the scope is discarded.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface ExpressionScope {
    /**
     * Get the enclosing scope.
     *
     * @return The enclosing scope.
     */
    public ExpressionScope getEnclosingScope();

    /**
     * Declare a new variable in this scope.
     *
     * @param name         The name of the variable.
     * @param initialValue The initial value of the variable.
     * @return The variable.
     */
    public Variable declareVariable(ExpandedName name, Value initialValue);

    /**
     * Resolve a name to a variable.
     *
     * <p>This method will first look in this scope and then ask the enclosing
     * scope to resolve the variable if necessary.</p>
     *
     * @param name The name of the variable to resolve.
     * @return The variable, or null if the name does not match any existing
     *         variable.
     */
    public Variable resolveVariable(ExpandedName name);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
