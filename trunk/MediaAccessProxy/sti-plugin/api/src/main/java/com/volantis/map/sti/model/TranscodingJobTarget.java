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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.sti.model;

/**
 * Transcoding job target element model class.
 */
public class TranscodingJobTarget {
    
    /**
     * Target externam location.
     */
    protected TranscodingJobTargetExternalLocation externalLocation;

    /**
     * Profile id attribute.
     */
    protected String profileID;

    /**
     * Type of application.
     */
    protected String applicationType;

    /**
     * Limit for application size.
     */
    protected long applicationSizeLimit;

    /**
     * Transcoding parameters.
     */
    protected TranscodingParams transcodingParams;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for external location of target.
     * 
     * @return external location.
     */
    public TranscodingJobTargetExternalLocation getExternalLocation() {
        return this.externalLocation;
    }

    /**
     * Setter for external location of target.
     * 
     * @param external location.
     */
    public void setExternalLocation(
            TranscodingJobTargetExternalLocation externalLocation) {
        this.externalLocation = externalLocation;
    }

    /**
     * Getter for profile id attribute.
     * 
     * @return profile id.
     */
    public String getProfileID() {
        return this.profileID;
    }

    /**
     * Setter for profile id attribute.
     * 
     * @param profile id.
     */
    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    /**
     * Getter for application type.
     * 
     * @return application type.
     */
    public String getApplicationType() {
        return this.applicationType;
    }

    /**
     * Setter for application type.
     * 
     * @param application type.
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * Getter for application size limit.
     * 
     * @return application size limit.
     */
    public long getApplicationSizeLimit() {
        return this.applicationSizeLimit;
    }

    /**
     * Setter for application size limit.
     * 
     * @param application size limit.
     */
    public void setApplicationSizeLimit(long applicationSizeLimit) {
        this.applicationSizeLimit = applicationSizeLimit;
    }

    /**
     * Getter for transcoding parameters.
     * 
     * @return transcoding parameters.
     */
    public TranscodingParams getTranscodingParams() {
        return this.transcodingParams;
    }

    /**
     * Setter for transcoding parameters.
     * 
     * @param transcoding parameters.
     */
    public void setTranscodingParams(TranscodingParams transcodingParams) {
        this.transcodingParams = transcodingParams;
    }

    /**
     * Getter for extension data properties. 
     * 
     * @return extension data properties.
     */
    public Properties getExtensionData() {
        return this.extensionData;
    }

    /**
     * Setter for extension data properties.
     * 
     * @param extensionData extension data properties.
     */
    public void setExtensionData(Properties extensionData) {
        this.extensionData = extensionData;
    }

}
