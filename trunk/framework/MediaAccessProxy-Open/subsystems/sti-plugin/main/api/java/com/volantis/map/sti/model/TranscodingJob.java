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
 * Transcoding job element model class.
 */
public class TranscodingJob {
    
    /**
     * Source of job.
     */
    protected TranscodingJobSource source;

    /**
     * Target of job.
     */
    protected TranscodingJobTarget target;

    /**
     * Policy ref attribute.
     */
    protected String policyRef;

    /**
     * Adaptation classes.
     */
    protected AdaptationClasses adaptationClasses;

    /**
     * Job id.
     */
    protected String jobID;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for job id attribute.
     * 
     * @return job id.
     */
    public String getJobID() {
        return this.jobID;
    }

    /**
     * Setter for job id attribute.
     * 
     * @param jobID
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
     * Getter for transcoding job source.
     * 
     * @return transcoding job source.
     */
    public TranscodingJobSource getSource() {
        return this.source;
    }

    /**
     * Setter for transcoding job source.
     * 
     * @param source transcoding job source.
     */
    public void setSource(TranscodingJobSource source) {
        this.source = source;
    }

    /**
     * Getter for transcoding job target.
     * 
     * @return transcoding job target.
     */
    public TranscodingJobTarget getTarget() {
        return this.target;
    }

    /**
     * Setter for transcoding job target.
     * 
     * @param source transcoding job target.
     */
    public void setTarget(TranscodingJobTarget target) {
        this.target = target;
    }

    /**
     * Getter for policy ref. attribute.
     * 
     * @return policyRef.
     */
    public String getPolicyRef() {
        return this.policyRef;
    }

    /**
     * Setter for policy ref attribute.
     * 
     * @param policyRef
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
     * @param adaptationClasses
     */
    public void setAdaptationClasses(AdaptationClasses adaptationClasses) {
        this.adaptationClasses = adaptationClasses;
    }

}
