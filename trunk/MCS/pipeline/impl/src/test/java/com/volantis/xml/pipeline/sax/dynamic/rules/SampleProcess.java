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
package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;

import org.xml.sax.SAXException;

/**
 * A sample process which outputs some text.
 */
public class SampleProcess extends XMLProcessImpl {

    //javadoc inherited
    public void stopProcess() throws SAXException {
        String text = "process working";
        getNextProcess().characters(
                text.toCharArray(),
                0,
                text.length());
        super.stopProcess();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	32/1	trynne	VBM:2005103112 added processdef pipeline rule

 ===========================================================================
*/
