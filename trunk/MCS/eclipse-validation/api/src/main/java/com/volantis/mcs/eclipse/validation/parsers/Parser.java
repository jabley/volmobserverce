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
 * $Header: /src/voyager/com/volantis/mcs/gui/validation/parsers/Parser.java,v 1.2 2002/11/15 09:12:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Allan           VBM:2002111110 - A Parser that uses a
 *                              given ParserStrategyFactory to create a
 *                              ParserStrategy that it uses to parse a
 *                              String.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.validation.parsers;

import com.volantis.mcs.eclipse.validation.parsers.ParserStrategyFactory;
import com.volantis.mcs.eclipse.validation.parsers.ParserStrategy;

import java.io.Reader;
import java.io.StringReader;
import java.io.EOFException;
import java.text.ParseException;

/**
 * A Parser that uses a given ParserStrategyFactory to create a
 * ParserStrategy that it uses to parse a String.
 */
public class Parser {

    /**
     * The ParserStrategyFactory associated with this Parser.
     */
    ParserStrategyFactory strategyFactory;

    /**
     * Construct a new Parser.
     * @param strategyFactory The ParserStrategyFactory for this Parser.
     */
    public Parser(ParserStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    /**
     * Parse a String.
     * @param s The String to parse.
     * @throws ParseException If there is an illegal character.
     * @throws EOFException If there a no more characters and the parser
     * is in the middle of constructing a token - i.e. there is an
     * unexpected end.
     */
    public void parse(String s) throws ParseException, EOFException {
        if(s!=null && s.length()>0 && strategyFactory!=null) {
            Reader r = new StringReader(s);
            ParserStrategy ps = strategyFactory.createParserStrategy(r);
            ps.parse();
        }
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
