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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransTableHelper;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A remove table rule for XHTMLFull protocols.
 * <p>
 * Since XHTMLFull protocols have the normal "stylistic" attributes on table
 * (e.g. border, etc), then we check to see if none of these are present and
 * the table has only a single column. If so, we consider it has no visual
 * impact and allow it to be removed.
 */
public class XHTMLFullRemoveTableRule implements RemoveTableRule {

    /**
     * A list of attributes that are style attributes associated with the
     * table. If any of the attributes in this list are present in a table
     * this will disallow table removal.
     */
    private static final ArrayList STYLE_ATTRS = new ArrayList();

    static {
        // Initialize STYLE_ATTRS
        STYLE_ATTRS.add("align");
        STYLE_ATTRS.add("bgcolor");
        STYLE_ATTRS.add("cellspacing");
        STYLE_ATTRS.add("cellpadding");
        STYLE_ATTRS.add("border");
        // NOTE: style has been added so that we can safely add small parts
        // of inline css for protocols which do not support many css things
        // and ensure that the styling makes it into the output.
        STYLE_ATTRS.add("style");
    }

    /**
     * The transfactory associated with this rule.
     */
    private final TransFactory transFactory;

    /**
     * Initialise.
     * 
     * @param transFactory the TransFactory used by this rule.
     */
    public XHTMLFullRemoveTableRule(TransFactory transFactory) {
        this.transFactory = transFactory;
    }

    /**
     * Return true if the table contains only a single column and that
     * table has no stylistic attributes.
     */
    // rest of javadoc inherited.
    public boolean canRemoveTable(TransTable transTable) {

        // todo: presumably should also check for class and id attributes here?
        // otherwise we will throw able tables which have styles associated
        
        return transTable.getCols() == 1 &&
                !hasStyleAttributes(transTable.getElement());
    }

    /**
     * Check whether a given table element has stylistic attributes.
     * @param table a table Element
     */
    private boolean hasStyleAttributes(Element table) {
        boolean hasStyleAttrs = false;
        TransTableHelper tth = TransTableHelper.getInstance();
        Iterator styleAttrs = STYLE_ATTRS.iterator();
        while(styleAttrs.hasNext() && !hasStyleAttrs) {
            String styleAttr = (String) styleAttrs.next();
            hasStyleAttrs = tth.tableHasAttribute(table, transFactory,
                    styleAttr, true);
        }

        return hasStyleAttrs;
    }
}
