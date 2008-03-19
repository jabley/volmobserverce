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

package com.volantis.mcs.css.impl.parser.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps from function name to parser to use to validated and create style
 * value.
 */
public class FunctionParserFactory {

    /**
     * Map from function name to FunctionParser.
     */
    private Map functionNameToParser;
    private final PassThroughFunctionParser passThroughFunctionParser;

    /**
     * Initialise.
     */
    public FunctionParserFactory() {
        functionNameToParser = new HashMap();

        ComponentURIFunctionParser uriFunctionParser =
                new ComponentURIFunctionParser();

        functionNameToParser.put("attr", new AttrFunctionParser());
        functionNameToParser.put("mariner-component-url", uriFunctionParser);
        functionNameToParser.put("mcs-component-url", uriFunctionParser);
        functionNameToParser.put("mcs-container-instance",
                                 new MCSContainerInstanceFunctionParser());
        functionNameToParser.put("mcs-transcodable-url",
                                 new TranscodableURIFunctionParser());
        functionNameToParser.put("rgb", new RGBFunctionParser());
        functionNameToParser.put("url", new URIFunctionParser());
        functionNameToParser.put("counter", new CounterFunctionParser());
        functionNameToParser.put("counters", new CountersFunctionParser());
        passThroughFunctionParser = new PassThroughFunctionParser();
    }

    /**
     * Get the function parser for the specified name.
     *
     * @param name The name of the parser.
     *
     * @return The function parser for the name, or a generic one that creates
     * a StyleFunctionCall value if a specific one could not be found.
     */
    public FunctionParser getFunctionParser(String name) {
        FunctionParser functionParser =
                (FunctionParser) functionNameToParser.get(name);

        if (functionParser == null) {
            functionParser = passThroughFunctionParser;
        }

        return functionParser;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
