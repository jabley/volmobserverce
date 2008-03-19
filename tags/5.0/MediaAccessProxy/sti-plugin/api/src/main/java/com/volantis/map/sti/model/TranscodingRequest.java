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

import java.util.ArrayList;

/**
 * Transcoding request element model class. Root element for requests.
 */
public class TranscodingRequest {
    
    /**
     * Originator id attribute.
     */
    protected String originatorID;

    /**
     * Operation id attribute.
     */
    protected String operationID;

    /**
     * Profile id attribute.
     */
    protected String profileID;

    /**
     * Application type.
     */
    protected String applicationType;

    /**
     * Size limit of application.
     */
    protected long applicationSizeLimit;

    /**
     * Policy ref.
     */
    protected String policyRef;

    /**
     * Adaptation classes.
     */
    protected AdaptationClasses adaptationClasses;

    /**
     * Transcoding parameters.
     */
    protected TranscodingParams transcodingParams;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for originator ID attribute.
     * 
     * @return originator id.
     */
    public String getOriginatorID() {
        return this.originatorID;
    }

    /**
     * Setter for originator ID attribute.
     * 
     * @param originatorID originator id.
     */
    public void setOriginatorID(String originatorID) {
        this.originatorID = originatorID;
    }

    /**
     * Getter for operation ID attribute.
     * 
     * @return operation id.
     */
    public String getOperationID() {
        return this.operationID;
    }

    /**
     * Setter for operation ID attribute.
     * 
     * @param operationID operation id.
     */
    public void setOperationID(String operationID) {
        this.operationID = operationID;
    }

    /**
     * Getter for profile ID attribute.
     * 
     * @return profile id.
     */
    public String getProfileID() {
        return this.profileID;
    }

    /**
     * Setter for profile ID attribute.
     * 
     * @param profileID profile id.
     */
    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    /**
     * Getter for application type attribute.
     * 
     * @return application type.
     */
    public String getApplicationType() {
        return this.applicationType;
    }

    /**
     * Setter for application type attribute.
     * 
     * @param applicationType application type.
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
     * @param applicationSizeLimit application size limit.
     */
    public void setApplicationSizeLimit(long applicationSizeLimit) {
        this.applicationSizeLimit = applicationSizeLimit;
    }

    /**
     * Getter for policy ref. attribute.
     * 
     * @return policy ref. attribute.
     */
    public String getPolicyRef() {
        return this.policyRef;
    }

    /**
     * Setter for policy ref. attribute.
     * 
     * @param policyRef policy ref. attribute.
     */
    public void setPolicyRef(String policyRef) {
        this.policyRef = policyRef;
    }

    /**
     * Getter for adaptation classes.
     * 
     * @return adaptation classes.
     */
    public AdaptationClasses getAdaptationClasses() {
        return this.adaptationClasses;
    }

    /**
     * Setter for adaptation classes.
     * 
     * @param adaptationClasses adaptation classes.
     */
    public void setAdaptationClasses(AdaptationClasses adaptationClasses) {
        this.adaptationClasses = adaptationClasses;
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
     * @param transcodingParams transcoding parameters.
     */
    public void setTranscodingParams(TranscodingParams transcodingParams) {
        this.transcodingParams = transcodingParams;
    }

    /**
     * Adds transcoding job into request.
     * 
     * @param transcodingJob transcoding job to be added.
     */
    public void addTranscodingJob(TranscodingJob transcodingJob) {
        transcodingJobList.add(transcodingJob);
    }

    /**
     * Gets transcoding job at specified index.
     * 
     * @param index index of transcoding job to be returned.
     * @return transcoding job.
     */
    public TranscodingJob getTranscodingJob(int index) {
        return (TranscodingJob) transcodingJobList.get(index);
    }

    /**
     * Gets size of transcofing jobs list.
     * 
     * @return number of transcoding jobs.
     */
    public int sizeTranscodingJobList() {
        return transcodingJobList.size();
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

    /**
     * List of transcoding jobs in this request.
     */
    protected ArrayList transcodingJobList = new ArrayList();

}
