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
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.FocusSelector;
import com.volantis.mcs.themes.PseudoClassSelector;

/**
 * Represents a pseudo class selector of the form :focus
 */
public class DefaultFocusSelector extends DefaultPseudoClassSelector
        implements FocusSelector {

    /**
     * Initialize a new instance.
     */
    public DefaultFocusSelector() {
        super(PseudoClassTypeEnum.FOCUS);
    }

    // Javadoc inherited.
    protected PseudoClassSelector copyImpl() {
        return new DefaultFocusSelector();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 ===========================================================================
*/