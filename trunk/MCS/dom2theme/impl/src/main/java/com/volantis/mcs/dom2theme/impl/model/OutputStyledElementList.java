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
package com.volantis.mcs.dom2theme.impl.model;

import com.volantis.shared.iteration.IterationAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of "output" styled elements.
 *
 * @mock.generate
 */
public class OutputStyledElementList {

    /**
     * The list of {@link OutputStyledElement}s.
     */
    private List elementList = new ArrayList();

    /**
     * Initialise.
     */
    public OutputStyledElementList() {
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        for (Iterator iterator = elementList.iterator(); iterator.hasNext();) {
            OutputStyledElement outputStyledElement = (OutputStyledElement) iterator.next();
            buf.append(outputStyledElement.getElement());
        }
        return buf.toString();
    }

    /**
     * Add a element to the list.
     *
     * @param element the element to be added, may not be null.
     */
    public void add(OutputStyledElement element) {

        if (element == null) {
            throw new IllegalArgumentException("element may not be null");
        }

        elementList.add(element);
    }

    /**
     * Iterate over each of the elements in the list, calling the the iteratee
     * to process each element in turn.
     * <p>
     * Note that the iteratee can rely on the styles associated with each
     * element being non empty - i.e. it will either be null or it will have
     * at least one value. 
     *
     * @param iteratee called to process each element in turn, may not be null.
     */
    public void iterate(OutputStyledElementIteratee iteratee) {

        if (iteratee == null) {
            throw new IllegalArgumentException("iteratee may not be null");
        }

        IterationAction action = IterationAction.CONTINUE;
        Iterator elements = elementList.iterator();
        while (action == IterationAction.CONTINUE && elements.hasNext()) {
            OutputStyledElement ose = (OutputStyledElement) elements.next();
            action = iteratee.next(ose);
        }
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
