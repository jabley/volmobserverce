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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.impl.TAttribute;
import com.volantis.mcs.dissection.dom.*;

import java.util.ArrayList;
import java.util.List;

public class TElement
    extends TNode
    implements DissectableElement {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private TString name;

    private List children;

    private List attributes;

    private ElementType type = DissectionElementTypes.getPlainElementType();

    public TElement() {
        children = new ArrayList();
        attributes = new ArrayList();
    }

    public TElement(TString name) {
        this();
        this.name = name;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public ElementType getType() {
        return type;
    }

    public TString getName() {
        return name;
    }

    public void setName(TString name) {
        this.name = name;
    }

    public void addChild(TNode node) {
        children.add(node);
    }

    public List children() {
        return children;
    }

    public TNode getChild(int index) {
        return (TNode) children.get(index);
    }

    public TElement getElementChild(int index) {
        return (TElement) children.get(index);
    }

    public TText getTextChild(int index) {
        return (TText) children.get(index);
    }

    public TNode getLastChild() {
        int count = children.size();
        return (count == 0) ? null : (TNode) children.get(count - 1);
    }

    public void addAttribute(TAttribute attribute) {
        attributes.add(attribute);
    }

    public List attributes() {
        return attributes;
    }

    public void visit(DissectableDocument document,
                      DocumentVisitor visitor)
        throws DissectionException {

        type.invoke(visitor, this);
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
