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
package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;

/**
 * Optimize the styles on the document and create a list of the result.
 */
public interface StyledDOMOptimizer {

    /**
     * Analyze the document optimizing the properties.
     *
     * @param styledDom The document to optimize.
     * @return The list of the resulting properties, one entry for each
     *         element in the input document.
     */
    OutputStyledElementList optimize(Document styledDom);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
