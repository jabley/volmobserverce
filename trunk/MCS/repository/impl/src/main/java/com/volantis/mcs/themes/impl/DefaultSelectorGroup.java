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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.SelectorGroup;
import com.volantis.mcs.themes.SelectorSequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A basic implementation of the {@link com.volantis.mcs.themes.SelectorGroup} interface.
 */
public class DefaultSelectorGroup implements SelectorGroup {
    /**
     * The selectors associated with this group.
     */
    private List selectors;

    // Javadoc inherited
    public synchronized List getSelectors() {
        if (selectors == null) {
            selectors = new ArrayList();
        }
        return selectors;
    }

    // Javadoc inherited
    public synchronized void setSelectors(List newSelectors) {
        selectors = newSelectors;
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        Iterator it = selectors.iterator();
        while (it.hasNext()) {
            SelectorSequence selector = (SelectorSequence) it.next();
            selector.validate(context);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
