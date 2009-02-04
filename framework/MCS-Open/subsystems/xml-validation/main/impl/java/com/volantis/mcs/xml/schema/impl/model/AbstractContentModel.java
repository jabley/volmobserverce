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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.impl.model;

import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractContentModel
        implements ContentModel {

    protected List exclusions;

    public ContentModel exclude(ContentModel excludedContent) {
        if (exclusions == null) {
            exclusions = new ArrayList();
        }
        exclusions.add(excludedContent);
        return this;
    }

    public ContentModel exclude(ElementSchema element) {
        exclude(element.getElementReference());
        return this;
    }

    public Iterator excluded() {
        return exclusions == null ? null : exclusions.iterator();
    }
}
