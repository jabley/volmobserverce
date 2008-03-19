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

package com.volantis.synergetics.testtools.mock.libraries.com.volantis.synergetics.log;

import com.volantis.synergetics.log.LogDispatcher;

/**
 * Triggers auto generation of classes within
 * <code>com.volantis.synergetics.log</code>.
 *
 * @mock.generate library="true"
 */
public class LogLibrary {

    /**
     * @mock.generate interface="true"
     */
    public LogDispatcher logger;

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - post-review amendments: new reporter type

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 ===========================================================================
*/
