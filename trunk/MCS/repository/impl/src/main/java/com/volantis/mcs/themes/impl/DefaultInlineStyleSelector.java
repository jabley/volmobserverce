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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.ThemeVisitor;
import com.volantis.mcs.model.validation.ValidationContext;

public class DefaultInlineStyleSelector implements InlineStyleSelector {

    private final int elementId;

    public DefaultInlineStyleSelector(int elementId) {
        this.elementId = elementId;
    }

    //javadoc inherited
    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    //javadoc inherited
    public void validate(ValidationContext context) {
        //no validaiton required as the only requirement is that the elementId
        //is present. This is forced due to the primitive type of elementId.
    }

    //javadoc inherited
    public Object copy() {
        return new DefaultInlineStyleSelector(elementId);
    }

    //javadoc inherited
    public void accept(ThemeVisitor visitor) {
        visitor.visit(this);
    }

    public int getElementId() {
        return elementId;
    }
}
