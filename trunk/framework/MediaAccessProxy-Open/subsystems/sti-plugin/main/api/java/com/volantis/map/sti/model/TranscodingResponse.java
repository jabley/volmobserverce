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
 * Transcoding response element model class.
 */
public class TranscodingResponse {
    
    /**
     * Originator attribute.
     */
    protected String originatorID;

    /**
     * Operation id attribute.
     */
    protected String operationID;

    /**
     * Main return result.
     */
    protected ReturnResult mainReturnResult;

    /**
     * Return results container.
     */
    protected ReturnResults additionalReturnResults;

    /**
     * Duration of transcoding.
     */
    protected int totalDuration;

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
     * Getter for main return result.
     * 
     * @return main return result.
     */
    public ReturnResult getMainReturnResult() {
        return this.mainReturnResult;
    }

    /**
     * Setter for main return result.
     * 
     * @param mainReturnResult main return result.
     */
    public void setMainReturnResult(ReturnResult mainReturnResult) {
        this.mainReturnResult = mainReturnResult;
    }

    /**
     * Getter for additional return results.
     * 
     * @return additional return results.
     */
    public ReturnResults getAdditionalReturnResults() {
        return this.additionalReturnResults;
    }

    /**
     * Setter for additional return results.
     * 
     * @param additionalReturnResults additional return results.
     */
    public void setAdditionalReturnResults(ReturnResults additionalReturnResults) {
        this.additionalReturnResults = additionalReturnResults;
    }

    /**
     * Getter for total duration of transcoding.
     * 
     * @return total duration of transcoding.
     */
    public int getTotalDuration() {
        return this.totalDuration;
    }

    /**
     * Setter for total duration of transcoding.
     * 
     * @param totalDuration total duration of transcoding.
     */
    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
    
    /**
     * Adds job result into response.
     * 
     * @param jobResult
     */
    public void addJobResult(JobResult jobResult) {
        jobResultList.add(jobResult);
    }

    /**
     * Gets job result at specified index.
     * 
     * @param index index of job result to be returned.
     * @return job result.
     */
    public JobResult getJobResult(int index) {
        return (JobResult) jobResultList.get(index);
    }

    /**
     * Returns size of job result list elements.
     * 
     * @return size of job result list elements.
     */
    public int sizeJobResultList() {
        return jobResultList.size();
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
     * List of job result in this response.
     */
    protected ArrayList jobResultList = new ArrayList();

}
