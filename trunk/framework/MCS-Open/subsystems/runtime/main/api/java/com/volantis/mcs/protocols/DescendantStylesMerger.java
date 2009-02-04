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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;

/**
 * {@link RecursingDOMVisitor} implementation which allows the styles on a element
 * to be merged with those of each of its children. The child styles take
 * precendence over those of the parent if there is a conflict.
 */
public class DescendantStylesMerger extends RecursingDOMVisitor {

    /**
     * Merge the {@link Styles} on this element with those of its children
     *
     * @param element whose Styles to merge with those of its children
     */
    public void pushStylesDown(Element element) {
        element.forEachChild(this);
    }


    public void visit(Element element) {
        Element parent = element.getParent();
        StylesMerger merger = StylingFactory.
                getDefaultInstance().getStylesMerger();
        Styles mergedStyles = merger.merge(element.getStyles(),
                parent.getStyles());
        element.setStyles(mergedStyles);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 ===========================================================================
*/
