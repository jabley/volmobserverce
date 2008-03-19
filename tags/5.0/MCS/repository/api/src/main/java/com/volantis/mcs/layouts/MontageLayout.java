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

package com.volantis.mcs.layouts;

import com.volantis.mcs.project.Project;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.project.Project;

/**
 * @mock.generate base="Layout"
 */
public class MontageLayout
        extends Layout {

    public MontageLayout(Project project) {
        super();
    }

    /**
     * @deprecated Not valid, don't use except in tests.
     */
    public MontageLayout() {
        super();
    }

    public LayoutType getType() {
        return LayoutType.MONTAGE;
    }

    public boolean isCanvas() {
        return false;
    }
}
