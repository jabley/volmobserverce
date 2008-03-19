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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.separator;


/**
 * Represents some content that needs separating.
 *
 * <p>This is a marker interface that represents a type of content that needs
 * separating. If necessary it can be used by the {@link SeparatorArbitrator}
 * in order to make its decision about whether it should write out a
 * separator.</p>
 *
 * <p>This is very similar in usage to an extensible type safe enumeration in
 * that there will be single implementations of this interface for each type
 * of content that is significant to the arbitrator.</p>
 *
 * todo: Examine whether it makes sense to make this an extensible type safe enumeration that has a single literal value that can be used when the content has no effect on whether a separator is written out or not.
 * 
 * @mock.generate
 */
public interface SeparatedContent {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
