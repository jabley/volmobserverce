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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.policies.expression;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.mcs.expression.RepositoryObjectIdentityValueImpl;
import com.volantis.mcs.objects.PolicyIdentity;

public class IdentityValueExpression
 implements Expression {

    private final Value identityValue;

    public IdentityValueExpression(PolicyIdentity identity) {
        identityValue = new RepositoryObjectIdentityValueImpl(null, identity);
    }

    public Value evaluate(ExpressionContext context)
            throws ExpressionException {
        return identityValue;
    }
}
