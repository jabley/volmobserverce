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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.pickle;

import com.volantis.mcs.papi.impl.AbstractElementImplTestAbstract;

/**
 * Abstract test class for AbstractPickleElementImpl. This class is needed so that
 * we build up the same hierarchy of classes for both the Pickle elements and
 * their corresponding testcases. 
 */
public abstract class AbstractPickleTestAbstract extends 
    AbstractElementImplTestAbstract {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
