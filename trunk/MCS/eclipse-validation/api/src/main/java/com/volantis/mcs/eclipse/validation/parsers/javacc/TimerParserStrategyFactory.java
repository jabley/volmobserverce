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
 * $Header: /src/voyager/com/volantis/mcs/gui/validation/parsers/javacc/TimerParserStrategyFactory.java,v 1.2 2002/11/15 09:12:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Allan           VBM:2002111110 - A ParserStrategyFactory that
 *                              creates TimerParser ParserStrategy objects.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.validation.parsers.javacc;

import com.volantis.mcs.eclipse.validation.parsers.ParserStrategyFactory;
import com.volantis.mcs.eclipse.validation.parsers.ParserStrategy;
import com.volantis.mcs.eclipse.validation.parsers.javacc.TimerParser;

import java.io.Reader;

/**
 * A ParserStrategyFactory that creates TimerParser ParserStrategy objects.
 */
public class TimerParserStrategyFactory implements ParserStrategyFactory {
    // ParserStrategyFactory interface method.
    public ParserStrategy createParserStrategy(Reader r) {
        return new TimerParser(r);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
