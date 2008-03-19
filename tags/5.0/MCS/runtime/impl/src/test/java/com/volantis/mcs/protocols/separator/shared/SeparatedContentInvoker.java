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

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

import java.lang.reflect.InvocationTargetException;

/*
    Interface to allow the invocation of a method returning a SeparatedContent from an ArbitratorDecision 
 */
public interface SeparatedContentInvoker {
    SeparatedContent invoke(Object object)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$
 ===========================================================================
*/
