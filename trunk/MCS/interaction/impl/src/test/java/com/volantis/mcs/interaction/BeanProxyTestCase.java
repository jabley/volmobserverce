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

package com.volantis.mcs.interaction;

import com.volantis.mcs.interaction.impl.BeanProxyImpl;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.model.descriptor.BeanClassDescriptorMock;

public class BeanProxyTestCase
        extends ProxyTestAbstract {
    private BeanClassDescriptorMock beanClassDescriptorMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        beanClassDescriptorMock =
                new BeanClassDescriptorMock(
                        "beanClassDescriptorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

    }

    protected InternalProxy createProxy() {
        return new BeanProxyImpl(proxyModelDescriptorMock,
                beanClassDescriptorMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/4	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
