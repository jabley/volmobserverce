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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicTransMapper.java,v 1.5 2003/01/17 17:44:26 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Updated to utilize the new
 *                              AbridgedTransMapper interface and to set
 *                              up the abridgeMap. This is designed to
 *                              ensure that abridged mappings map a single
 *                              table level (rather than recursing through the
 *                              entire identified DOM).
 * 18-Nov-02    Phil W-S        VBM:2002100307 - Updated to set up valid
 *                              attributes for the div tag so only those
 *                              attributes appropriate for a div will be
 *                              retained on re-mapping.
 * 17-Jan-03    Byron           VBM:2003011501 - Added getValidDivAttributes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.trans.AbstractTransMapper;
import com.volantis.mcs.protocols.trans.BasicMapperElement;
import com.volantis.mcs.protocols.trans.TransMapper;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;
import com.volantis.mcs.themes.properties.DisplayKeywords;

import java.util.HashMap;

/**
 * The trans mapper for the XHTMLBasic unabridged transformer algorithm.
 */
public class XHTMLBasicTransMapper extends AbstractTransMapper {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration {@link TransformationConfiguration} which is used
     * when remapping elements. May be null.
     */
    public XHTMLBasicTransMapper(TransformationConfiguration configuration) {
        this.transformationConfiguration = configuration;
        initialize();
    }

    /**
     * Initializes the elementMap and abridgeMap with entries appropriate for
     * XHTMLBasic element remapping. Element mappings are:
     *
     * <ol>
     *   <li>table to div (can be optimized away without a style class)</li>
     *   <li>tr to div (can be optimized away without a style class)</li>
     *   <li>td to div (should not be optimized away without a style
     * class)</li>
     * </ol>
     *
     * Abridging should be applied to cell elements when abridging is required.
     */
    private void initialize() {
        TransMapper divOpt = new BasicMapperElement("div",
                getValidDivAttributes(), null, true,
                transformationConfiguration,
                DisplayKeywords.BLOCK);

        TransMapper divKeep = new BasicMapperElement("div",
                getValidDivAttributes(), null, false,
                transformationConfiguration,
                DisplayKeywords.BLOCK);

        String[] cells = XHTMLBasicElementHelper.getInstance().getCells();

        // Create the abridge map
        abridgeMap = new HashMap();

        // Initialize the element map
        elementMap.put(XHTMLBasicElementHelper.getInstance().getTable(),
                       divOpt);
        elementMap.put(XHTMLBasicElementHelper.getInstance().getRow(),
                       divOpt);

        // Use all cell element tags for mapping and abridging
        for (int i = 0;
             i < cells.length;
             i++) {
            elementMap.put(cells[i],
                           divKeep);
            abridgeMap.put(cells[i],
                           null);
        }
    }

    /**
     * The valid attributes for the div tag.
     * @return the valid attributes for the div tag.
     */
    protected String[] getValidDivAttributes() {
        String validAttributes[] = {"id", "class", "title",
                                       "dir", "lang", "xml:lang"};
        return validAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
