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

package com.volantis.mcs.interaction.impl.validation;

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.interaction.impl.ProxyVisitorAdapter;
import com.volantis.mcs.interaction.impl.ProxyWalker;

public class FinishValidation
        extends ProxyVisitorAdapter {

    protected void visitAll(Proxy proxy) {
        ((InternalProxy) proxy).finishValidation();
    }

    public void finish(Proxy proxy) {
        ProxyWalker walker = new ProxyWalker(this);
        walker.walk((InternalProxy) proxy);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
