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
 * $Header: /src/voyager/com/volantis/mcs/gui/validation/parsers/ParserStrategy.java,v 1.2 2002/11/15 09:12:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Allan           VBM:2002111110 - A ParserStrategy. This 
 *                              interface defines the methods that
 *                              instances of ParserStrategy must provide.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.validation.parsers;

import java.text.ParseException;
import java.io.EOFException;

/**
 * A ParserStrategy. This interface defines the methods that
 * instances of ParserStrategy must provide.
 */
public interface ParserStrategy {
    /**
     * Parse whatever characters it can parse.
     * @throws ParseException If there is an illegal character.
     * @throws EOFException If there a no more characters and the parser
     * is in the middle of constructing a token - i.e. there is an
     * unexpected end.
     */
    public void parse() throws ParseException, EOFException;
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
