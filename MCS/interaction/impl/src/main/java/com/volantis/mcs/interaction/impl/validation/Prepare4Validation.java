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

public class Prepare4Validation
        extends ProxyVisitorAdapter {

    protected void visitAll(Proxy proxy) {
        final InternalProxy internalProxy = (InternalProxy) proxy;
        internalProxy.prepareForValidation();
    }

    public void prepare(Proxy proxy) {
        ProxyWalker walker = new ProxyWalker(this);
        walker.walk((InternalProxy) proxy);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/10	pduffin	VBM:2005101811 Committing restructuring

 28-Oct-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
