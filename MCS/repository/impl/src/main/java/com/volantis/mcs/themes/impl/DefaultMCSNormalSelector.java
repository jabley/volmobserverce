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

import com.volantis.mcs.themes.MCSNormalSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;

/**
 * Represents a pseudo class selector of the form :mcs-normal
 */
public class DefaultMCSNormalSelector extends DefaultPseudoClassSelector
        implements MCSNormalSelector {

    /**
     * Initialize a new instance.
     */
    public DefaultMCSNormalSelector() {
        super(PseudoClassTypeEnum.MCS_NORMAL);
    }

    // Javadoc inherited.
    protected PseudoClassSelector copyImpl() {
        return new DefaultMCSNormalSelector();
    }
}
