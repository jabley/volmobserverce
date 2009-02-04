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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.servlet;

import com.volantis.mcs.context.MarinerSessionContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.runtime.plugin.markup.MarkupFactory;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * This specialisation of MarinerSessionContext allows us to provide the
 * HttpSessionBindingListener interface methods.  When an HttpSession is 
 * invalidated or times out we are informed via the {@link #valueBound}
 * method which we use to invoke our {@link #release} method.
 */
public class MarinerServletSessionContext extends MarinerSessionContext 
        implements HttpSessionBindingListener {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a new <code>MarinerServletSessionContext</code>, this is only
     * to be used when the object is Externalized and thereforce expects
     * readExternal() to be run to populate the device and user objects.
     * As a device object contains references to otehr devices, at the
     * moment rather than serialize the device and user objects we leave
     * them set to null. initialSession() will check if device or user
     * are null and populate them with the current requests details.
     */
    public MarinerServletSessionContext() {
        this(null, MarkupFactory.getDefaultInstance());
    }

    /**
     * Initialise.
     */
    public MarinerServletSessionContext(InternalDevice device,
                                        MarkupFactory markupFactory) {
        super(device, markupFactory);
    }

    /**
     * Create a new <code>MarinerServletSessionContext</code> for the specified
     * InternalDevice.
     * @param device The InternalDevice which this session is for.
     */
    public MarinerServletSessionContext(InternalDevice device) {
        super(device, MarkupFactory.getDefaultInstance());
    }
    
    // javadoc inherited from HttpSessionBindingListener interface
    public void valueBound(HttpSessionBindingEvent event) {
        // nothing to do
    }

    // javadoc inherited from HttpSessionBindingListener interface
    public void valueUnbound(HttpSessionBindingEvent event) {
        release();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 19-Jul-03	812/1	adrian	VBM:2003071609 Support session scope markup plugins

 ===========================================================================
*/
