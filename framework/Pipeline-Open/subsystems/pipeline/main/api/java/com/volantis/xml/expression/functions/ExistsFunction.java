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

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-exists">fn:exists</a>.
 */ 
public class ExistsFunction
        implements Function {

    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {

        if (arguments.length != 1) {
            throw new IllegalArgumentException("empty() takes only one argument");
        }

        Sequence sequence = arguments[0].getSequence();
        boolean empty = (sequence.getLength() == 0);

        return empty ? BooleanValue.FALSE : BooleanValue.TRUE;
    }
}
