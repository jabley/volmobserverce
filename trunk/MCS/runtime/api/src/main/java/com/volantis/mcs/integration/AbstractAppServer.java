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
package com.volantis.mcs.integration;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.management.tracking.jmx.JMXPageTrackerFactory;

import javax.sql.DataSource;

/**
 * Abstract implementation of the AppServerInterface. This provides reasonable
 * defaults for all methods in the AppServerInterface.
 */
public abstract class AbstractAppServer implements AppServerInterface {
    /**
     * The PageTrakerFactory to use.
     */
    protected final PageTrackerFactory pageTrackerFactory;

    /**
     * The volantis bean to use.
     */
    protected Volantis volantisBean;

    /**
     * Initializes the new instance.
     */
    protected AbstractAppServer() {
        this.pageTrackerFactory = createPageTrackerFactory();
    }

    /**
     * Factory method used to create the page tracker factory.
     *
     * @return an appropriate PageTrackerFactory implementation instance
     */
    protected PageTrackerFactory createPageTrackerFactory() {
        return new JMXPageTrackerFactory();
    }

    // javadoc inherited
    public void setVolantisBean(Volantis bean) {
        volantisBean = bean;
    }

    // javadoc inherited
    public Volantis getVolantisBean() {
        return volantisBean;
    }

    // javadoc inherited
    public boolean useAppServerDataSource() {
        return false;
    }

    // javadoc inherited
    public DataSource getAppServerDataSource() {
        return null;
    }

    // javadoc inherited
    public PageTrackerFactory getPageTrackerFactory() {
        return this.pageTrackerFactory;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jul-05	8940/1	philws	VBM:2005060606 Provider installer compatible app server interface implementations

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
