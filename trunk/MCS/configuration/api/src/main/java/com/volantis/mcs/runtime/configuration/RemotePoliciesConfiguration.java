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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/RemotePoliciesConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about all remote policies. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Holds configuration information about all remote policies. 
 */ 
public class RemotePoliciesConfiguration {

    /**
     * The default connection timeout to use.
     */
    public static final int DEFAULT_TIMEOUT = 15000;

    private Integer connectionTimeout;

    private RemotePolicyCacheConfiguration policyCache;

    // ========
    
    private List quotas = new ArrayList();
    
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Get the connection timeout.
     *
     * <p>If no connection timeout was specified then return the default.</p>
     *
     * @return The connection timeout.
     */
    public int getRealConnectionTimeout() {
        if (connectionTimeout == null) {
            return DEFAULT_TIMEOUT;
        } else {
            return connectionTimeout.intValue();
        }
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public RemotePolicyCacheConfiguration getPolicyCache() {
        return policyCache;
    }

    public void setPolicyCache(RemotePolicyCacheConfiguration policyCache) {
        this.policyCache = policyCache;
    }

    // ========

    public Iterator getQuotaIterator() {
        return quotas.iterator();
    }

    public void addQuota(RemotePolicyQuotaConfiguration quota) {
        this.quotas.add(quota);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
