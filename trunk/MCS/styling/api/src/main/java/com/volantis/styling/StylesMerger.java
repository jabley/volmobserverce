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

/**
 * Merges two Styles together, with the property values of one of the Styles
 * always 'winning' over the other if there are conflicts.
 *
 * @mock.generate
 */
public interface StylesMerger {

    /**
     * Merge the two Styles together, where the property values from the winner
     * 'win' if specified in both sets.
     *
     * @param winner Styles to merge, may be null.
     * @param luser  Styles to merge, may be null.
     * @return merged Styles. Will be null if both parameters are null.
     */
    Styles merge(Styles winner, Styles luser);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 ===========================================================================
*/
