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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions.pipeline;

import java.util.List;

import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.impl.SimpleExpressionContext;
import com.volantis.xml.pipeline.sax.impl.operations.tryop.TryModel;
import org.xml.sax.SAXParseException;

/**
 * Base class for error functions.
 */
public abstract class AbstractErrorFunction extends AbstractFunction {

    protected SAXParseException getException (ExpressionContext context) throws ExpressionException {
        TryModel model = (TryModel) ((SimpleExpressionContext) context).findObject(TryModel.class);
        if (model == null) {
            throw new ExpressionException("Error functions must be used inside try block.");
        }

        List exceptions = model.getExceptions();
        SAXParseException x = exceptions.isEmpty() ? null : (SAXParseException) exceptions.get(0);
        return x;    
    }

}
