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
 * Job result element model class.
 */
public class JobResult {
    
    /**
     * Main return result.
     */
    protected ReturnResult mainReturnResult;

    /**
     * Additional return results.
     */
    protected ReturnResults additionalReturnResults;

    /**
     * Duration of job attribute.
     */
    protected int duration;

    /**
     * Job result output.
     */
    protected JobResultOutput output;

    /**
     * Job id attribute.
     */
    protected String jobID;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for Job id attribute. 
     * 
     * @return job id.
     */
    public String getJobID() {
        return this.jobID;
    }

    /**
     * Setter for job id attribute.
     * 
     * @param jobID job id.
     */
    public void setJobID(String jobID) {
        this.jobID = jobID;
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
     * @param additionalReturnResults
     */
    public void setAdditionalReturnResults(ReturnResults additionalReturnResults) {
        this.additionalReturnResults = additionalReturnResults;
    }

    /**
     * Adds adaptations performed attribute. 
     */
    public void addAdaptationsPerformed(String adaptationsPerformed) {
        adaptationsPerformedList.add(adaptationsPerformed);
    }

    /**
     * Getter for adaptations performed attributes. 
     * 
     * @param index index of adaptation to be returned.
     * @return adaptation performed at specified index.
     */    
    public String getAdaptationsPerformed(int index) {
        return (String) adaptationsPerformedList.get(index);
    }

    /**
     * Gets size of adaptations performed. 
     * 
     * @return size of adaptations performed.
     */
    public int sizeAdaptationsPerformedList() {
        return adaptationsPerformedList.size();
    }

    /**
     * Getter for duration attribute.
     * 
     * @return duration.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Setter for duration attribute.
     * 
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Getter for result output.
     * 
     * @return result output.
     */
    public JobResultOutput getOutput() {
        return this.output;
    }

    public void setOutput(JobResultOutput output) {
        this.output = output;
    }

    /**
     * List of adaptations performed. 
     */
    protected ArrayList adaptationsPerformedList = new ArrayList();

}
