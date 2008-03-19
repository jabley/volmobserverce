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

package com.volantis.mcs.xml.schema.impl.model;

import com.volantis.mcs.xml.schema.model.BoundedContent;
import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ContentModelVisitor;
import com.volantis.mcs.xml.schema.model.SetMaximum;

public class BoundedContentImpl
        extends AbstractContentModel
        implements BoundedContent, SetMaximum {

    private int minimum;
    private int maximum;

    private final ContentModel model;

    public BoundedContentImpl(ContentModel model) {
        this.model = model;
        this.minimum = 0;
        this.maximum = Integer.MAX_VALUE;
    }

    public SetMaximum min(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public ContentModel max(int maximum) {
        this.maximum = maximum;
        return this;
    }

    public ContentModel optional() {
        this.minimum = 0;
        this.maximum = 1;
        return this;
    }

    public ContentModel atLeastOne() {
        this.minimum = 1;
        this.maximum = Integer.MAX_VALUE;
        return this;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public ContentModel getContentModel() {
        return model;
    }

    public void accept(ContentModelVisitor visitor) {
        visitor.visit(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
