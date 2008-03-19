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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.builder;

import com.volantis.mcs.protocols.menu.builder.MenuModelBuilderFactoryTestAbstract;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilderFactory;

/**
 * Tests {@link ConcreteMenuModelBuilderFactory}.
 */
public class ConcreteMenuModelBuilderFactoryTestCase extends
    MenuModelBuilderFactoryTestAbstract {
    // javadoc inherited
    protected MenuModelBuilderFactory createFactory() {
        return new ConcreteMenuModelBuilderFactory();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Mar-04	3342/1	philws	VBM:2004022707 Implement the Menu Model Builder

 ===========================================================================
*/
