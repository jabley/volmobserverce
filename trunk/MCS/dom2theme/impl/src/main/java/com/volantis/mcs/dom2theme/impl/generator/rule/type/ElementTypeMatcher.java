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
package com.volantis.mcs.dom2theme.impl.generator.rule.type;

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.shared.iteration.IterationAction;

/**
 */
public class ElementTypeMatcher {

    private OutputStyledElementList elementList;

    public ElementTypeMatcher(OutputStyledElementList elementList) {

        this.elementList = elementList;
    }

    public OutputStyledElementList match(final String type) {

        // todo: later: cache results by type to make this faster

        final OutputStyledElementList result;

        if (type == null) {
            result = elementList;
        } else {
            result = new OutputStyledElementList();
            elementList.iterate(new OutputStyledElementIteratee() {
                public IterationAction next(OutputStyledElement outputElement) {

                    if (type.equals(outputElement.getName())) {
                        result.add(outputElement);
                    }
                    return IterationAction.CONTINUE;
                }
            });
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/12	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
