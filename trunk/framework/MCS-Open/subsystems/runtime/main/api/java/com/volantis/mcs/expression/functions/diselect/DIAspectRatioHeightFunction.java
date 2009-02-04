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
package com.volantis.mcs.expression.functions.diselect;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.functions.diselect.DIAspectRatioFunction;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;

/**
 * This Function retrieves the aspect ratio of the device from the repository
 * and returns the height component (i.e. if aspect ratio = width / height,
 * then this returns height). If the repository contains no entry for aspect
 * ratio then the supplied default value will be returned. If this default
 * value is not valid, zero will be returned.
 */
public class DIAspectRatioHeightFunction extends DIAspectRatioFunction {

    // Javadoc inherited.
    protected Value execute(ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, Value defaultValue) {

        return calculateValue(expressionContext, accessor, defaultValue,
                AspectRatioOutputFormat.HEIGHT);
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-device-aspect-ratio-height";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
