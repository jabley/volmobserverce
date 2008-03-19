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
package com.volantis.styling;

import com.volantis.shared.iteration.IterationAction;

/**
 * An internal iterator interface for {@link PseudoStyleEntity}s.
 *
 * @mock.generate
 */
public interface PseudoStyleEntityIteratee {

    /**
     * Called for each {@link PseudoStyleEntity} in the collection.
     *
     * @param entity the entity to process.
     * @return {@link IterationAction.CONTINUE} to continue,
     *      {@link IterationAction.BREAK} to stop.
     */
    IterationAction next(PseudoStyleEntity entity);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
