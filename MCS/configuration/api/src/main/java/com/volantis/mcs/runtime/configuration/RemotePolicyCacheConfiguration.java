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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/RemotePolicyCacheConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about individual remote policies. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 * Holds configuration information about individual remote policies. 
 */ 
public class RemotePolicyCacheConfiguration {

    private Integer maxCacheSize;

    private Boolean defaultCacheThisPolicy;
    private Integer defaultTimeToLive;
    private Boolean defaultRetryFailedRetrieval;
    private Integer defaultRetryInterval;
    private Integer defaultRetryMaxCount;
    private Boolean defaultRetainDuringRetry;

    private Boolean allowCacheThisPolicy;
    private Integer maxTimeToLive;
    private Boolean allowRetryFailedRetrieval;
    private Integer minRetryInterval;
    private Integer maxRetryMaxCount;
    private Boolean allowRetainDuringRetry;

    public Boolean getDefaultCacheThisPolicy() {
        return defaultCacheThisPolicy;
    }

    public void setDefaultCacheThisPolicy(Boolean defaultCacheThisPolicy) {
        this.defaultCacheThisPolicy = defaultCacheThisPolicy;
    }

    public Boolean getAllowCacheThisPolicy() {
        return allowCacheThisPolicy;
    }

    public void setAllowCacheThisPolicy(Boolean allowCacheThisPolicy) {
        this.allowCacheThisPolicy = allowCacheThisPolicy;
    }

    public Integer getDefaultTimeToLive() {
        return defaultTimeToLive;
    }

    public void setDefaultTimeToLive(Integer defaultTimeToLive) {
        this.defaultTimeToLive = defaultTimeToLive;
    }

    public Boolean getDefaultRetryFailedRetrieval() {
        return defaultRetryFailedRetrieval;
    }

    public void setDefaultRetryFailedRetrieval(Boolean defaultRetryFailedRetrieval) {
        this.defaultRetryFailedRetrieval = defaultRetryFailedRetrieval;
    }

    public Integer getDefaultRetryInterval() {
        return defaultRetryInterval;
    }

    public void setDefaultRetryInterval(Integer defaultRetryInterval) {
        this.defaultRetryInterval = defaultRetryInterval;
    }

    public Integer getDefaultRetryMaxCount() {
        return defaultRetryMaxCount;
    }

    public void setDefaultRetryMaxCount(Integer defaultRetryMaxCount) {
        this.defaultRetryMaxCount = defaultRetryMaxCount;
    }

    public Boolean getDefaultRetainDuringRetry() {
        return defaultRetainDuringRetry;
    }

    public void setDefaultRetainDuringRetry(Boolean defaultRetainDuringRetry) {
        this.defaultRetainDuringRetry = defaultRetainDuringRetry;
    }

    public Integer getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(Integer maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public Integer getMaxTimeToLive() {
        return maxTimeToLive;
    }

    public void setMaxTimeToLive(Integer maxTimeToLive) {
        this.maxTimeToLive = maxTimeToLive;
    }

    public Boolean getAllowRetryFailedRetrieval() {
        return allowRetryFailedRetrieval;
    }

    public void setAllowRetryFailedRetrieval(Boolean allowRetryFailedRetrieval) {
        this.allowRetryFailedRetrieval = allowRetryFailedRetrieval;
    }

    public Integer getMinRetryInterval() {
        return minRetryInterval;
    }

    public void setMinRetryInterval(Integer minRetryInterval) {
        this.minRetryInterval = minRetryInterval;
    }

    public Integer getMaxRetryMaxCount() {
        return maxRetryMaxCount;
    }

    public void setMaxRetryMaxCount(Integer maxRetryMaxCount) {
        this.maxRetryMaxCount = maxRetryMaxCount;
    }

    public Boolean getAllowRetainDuringRetry() {
        return allowRetainDuringRetry;
    }

    public void setAllowRetainDuringRetry(Boolean allowRetainDuringRetry) {
        this.allowRetainDuringRetry = allowRetainDuringRetry;
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
