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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.validation;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ValidationModel {
    /**
     * Update the state based on the event.
     *
     * @param event The event that has occurred.
     * @throws IllegalStateException If there is no transition from the current
     *                               state on the supplied event.
     */
    void transition(Event event);

    /**
     * A start element event has been received for the specified element.
     *
     * <p>Invokes {@link #transition(Event)} with the
     * {@link Element#getStartEvent()} and pushes the element onto a
     * stack for use in checking for invalid content.</p>
     *
     * @param element The element.
     */
    void startElement(Element element);

    /**
     * An end element event has been received for the specified element.
     *
     * <p>Pops the element off the stack (making sure that it is the expected
     * one) and invokes {@link #transition(Event)} with the
     * {@link Element#getEndEvent()}.</p>
     *
     * @param element The element.
     */
    void endElement(Element element);
}
