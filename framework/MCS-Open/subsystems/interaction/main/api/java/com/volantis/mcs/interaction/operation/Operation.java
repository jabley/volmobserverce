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

package com.volantis.mcs.interaction.operation;

/**
 * Represents an operation that can be performed on a proxy object.
 *
 * <p>Operations returned from methods must be executed to have any effect on
 * the proxy. Execution will fail if the proxy is modified between creation
 * and execution.</p>
 */
public interface Operation {

    /**
     * Execute the operation.
     *
     * <p>This must be called first, before a call to {@link #undo}.</p>
     */
    void execute();

    /**
     * Undo the operation.
     *
     * <p>This may only be called after either a call to {@link #execute}, or
     * {@link #redo}.</p>
     */
    void undo();

    /**
     * Redo the operation.
     *
     * <p>This may only be called after a call to {@link #undo}.</p>
     */
    void redo();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
