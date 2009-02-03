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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.descriptorstore.impl;

import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.ParameterNames;
import com.volantis.synergetics.descriptorstore.Parameters;

import java.util.Date;

/**
 * A default implemenation of the Configuration Item
 */
public class DefaultResourceDescriptor implements ResourceDescriptor  {

    /**
     * This field is populated when the object is made persistent in the
     * JDO framework. DO NOT DELETE THIS
     */
    private String id;

    /**
     * The external id. Must be unique and not null  
     */
    private String externalID;

    /**
     * The type of the resource. Must not be null or empty
     */
    private String resourceType;

    /**
     * This field is updated manually by the computeDBHash method. If an attached
     * object is dirty then this must be called before commiting it.
     */
    private int hash;

    /**
     * The expiry date (last Access + timeToLive)
     */
    private Date expiry = null;

    /**
     * Simple timestamps.
     */
    private Date lastAccess = new Date();

    /**
     * When this ID was generated
     */
    private Date lastGenerated = lastAccess;

    /**
     * The configuration parameters
     */
    private DefaultParameters configParams = new DefaultParameters();

    /**
     * The requested parameter names
     */
    private DefaultParameterNames paramNames = new DefaultParameterNames();

    /**
     * The set of parameters generated to satisfy the request parameters
     */
    private DefaultParameters genParams = new DefaultParameters();

    /**
     *
     * @param configParams the configuration parameters. Must not be null
     * @param paramNames the requested response parameters. Must not be null
     */
    public DefaultResourceDescriptor(String resourceType,
                                     DefaultParameters configParams,
                                     DefaultParameterNames paramNames) {
        this.resourceType = resourceType;
        this.configParams = configParams;
        this.paramNames = paramNames;
    }

    // javadoc inherited
    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;  
    }

    // javdoc inherited
    public String getResourceType() {
        return resourceType;
    }

    // javadoc inherited
    public int computeDBHash() {
        hash = computeDBHash(resourceType, configParams);
        return hash;
    }

    /**
     * Compute a hash from the specified parameters.
     *
     * @param resourceType
     * @param configParameters
     * @return
     */
    public static int computeDBHash(String resourceType,
                                  Parameters configParameters) {
        int result = 17;
        result = 37 * result + resourceType.hashCode();
        result = 37 * result + configParameters.hashCode();
        return result;
    }

    // javadoc inherited
    public long getTimeToLive() {
        long result = -1;
        if (null != expiry) {
            // if we have a expiry date then compute the ttl
            result =  (expiry.getTime() - System.currentTimeMillis()) / 1000;
            // -1 has special meaning so indicate it has timed out using 0
            if (result < 0) {
                result = 0;
            }
        }
        return result;
    }

    // javadoc inherited
    public void setTimeToLive(long timeToLive) {
        if (timeToLive < 0 ){
            this.expiry = null;
        } else {
            this.expiry =
                new Date(System.currentTimeMillis() + (timeToLive * 1000));
        }
    }

    // javadoc inherited
    public Date getLastAccess() {
        return lastAccess;
    }

    /**
     * Set the last access time
     *
     * @param date the last access time
     */
    public void setLastAccess(Date date) {
        this.lastAccess = date;
    }

    // javadoc inherited
    public Date getLastGenerated() {
        return lastGenerated;
    }

    /**
     * Set the last generated time
     *
     * @param date the time the Configuration item was generated
     */
    public void setLastGenerated(Date date) {
        this.lastGenerated = date;
    }

    // javadoc inherited
    public Parameters getInputParameters() {
        return configParams;
    }

    // javadoc inherited
    public Parameters getOutputParameters() {
        return genParams;
    }

    // javadoc inherited
    public ParameterNames getRequestedParameterNames() {
        return paramNames;
    }

    /**
     * Note that this method does NOT comapre the dates contained in this
     * object.
     *
     * @param obj the object to compare with this for equality
     * @return true if obj is equal to this instance
     */
    public boolean equals(Object obj) {
        boolean result = false;
        if (getClass() == obj.getClass()) {
            ResourceDescriptor other = (ResourceDescriptor) obj;
            if (getExternalID().equals(other.getExternalID())
                && getInputParameters().equals(
                    other.getInputParameters())
                && getOutputParameters().equals(
                    other.getOutputParameters())
                && getRequestedParameterNames().equals(
                    other.getRequestedParameterNames())){
                result = true;
            }
        }
        return result;
    }

    /**
     * This is compatible with equals. (Note this does NOT return the same
     * value as {@link #computeDBHash()}
     *
     * @return the hashcode.
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getExternalID().hashCode();
        result = 37 * result + getInputParameters().hashCode();
        result = 37 * result + getOutputParameters().hashCode();
        result = 37 * result + getRequestedParameterNames().hashCode();
        return result;
    }


}
