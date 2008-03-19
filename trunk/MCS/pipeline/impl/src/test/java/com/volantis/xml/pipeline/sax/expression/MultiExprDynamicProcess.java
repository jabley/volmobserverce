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
package com.volantis.xml.pipeline.sax.expression;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.ExpressionProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcess;
import org.xml.sax.SAXException;

/**
 * Test process for multiple expression declarations, e.g. %{} or {}
 */
public class MultiExprDynamicProcess extends SimpleDynamicProcess {

    /**
     * Expression prefixes, matched with EXPRESSION_SUFFIXES on an [i]-[i]
     * basis.
     */
    private static final String[] EXPRESSION_PREFIXES = {"%{",
                                                         "{"};

    /**
     * Expression suffixes, matched with EXPRESSION_PREFIXES on an [i]-[i]
     * basis.
     */
    private static final String[] EXPRESSION_SUFFIXES = {"}",
                                                         "}"};

    /**
     * Escaped expression prefixes, matched with ESCAPED_EXPRESSION_SUFFIXES on
     * an [i]-[i] basis.
     */
    private static final String[] ESCAPED_EXPRESSION_PREFIXES = {"\\%{",
                                                                 "\\{"};

    /**
     * Escaped expression suffixes, matched with ESCAPED_EXPRESSION_PREFIXES on
     * an [i]-[i] basis.
     */
    private static final String[] ESCAPED_EXPRESSION_SUFFIXES = {"}",
                                                                 "}"};

    /**
     * Initialise.
     *
     * @param configuration the process configuration.
     */
    public MultiExprDynamicProcess(
            DynamicProcessConfiguration configuration) {
        super(configuration);
    }

    /**
     * Initialise.
     *
     * @param context the pipeline context.
     */
    public MultiExprDynamicProcess(XMLPipelineContext context)
            throws SAXException {
        super(context);
    }

    /**
     * Creates the expression process used by the process.  The expressions
     * have multiple declaration types (see class javadoc).
     *
     * @return an ExpressionProcess valid for multiple declaration types.
     */
    protected ExpressionProcess createExpressionProcess() {
        return new ExpressionProcess(EXPRESSION_PREFIXES,
                                     EXPRESSION_SUFFIXES,
                                     ESCAPED_EXPRESSION_PREFIXES,
                                     ESCAPED_EXPRESSION_SUFFIXES);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-05	8751/1	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 ===========================================================================
*/
