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

package com.volantis.mcs.protocols.widgets;

import com.volantis.mcs.protocols.MCSAttributes;

/**
 * The field expander context.
 * 
 * Used to gather data for currently processed FieldExpander element.
 */
public class FieldExpanderContext {
    private String summaryElementId;
    private String detailsElementId;
    private String summaryFieldId;
    
    private MCSAttributes markerAttributes;
    private String foldedMarkerId;
    private String unfoldedMarkerId;
    
    /**
     * @return Returns the detailsElementId.
     */
    public String getDetailsElementId() {
        return detailsElementId;
    }
    /**
     * @param detailsElementId The detailsElementId to set.
     */
    public void setDetailsElementId(String detailsElementId) {
        this.detailsElementId = detailsElementId;
    }
    /**
     * @return Returns the summaryElementId.
     */
    public String getSummaryElementId() {
        return summaryElementId;
    }
    /**
     * @param summaryElementId The summaryElementId to set.
     */
    public void setSummaryElementId(String summaryElementId) {
        this.summaryElementId = summaryElementId;
    }
    /**
     * @return Returns the summaryFieldId.
     */
    public String getSummaryFieldId() {
        return summaryFieldId;
    }
    /**
     * @param summaryFieldId The summaryFieldId to set.
     */
    public void setSummaryFieldId(String summaryFieldId) {
        this.summaryFieldId = summaryFieldId;
    }
    /**
     * @return Returns the foldedMarkerId.
     */
    public String getFoldedMarkerId() {
        return foldedMarkerId;
    }
    /**
     * @param foldedMarkerId The foldedMarkerId to set.
     */
    public void setFoldedMarkerId(String foldedMarkerId) {
        this.foldedMarkerId = foldedMarkerId;
    }
    /**
     * @return Returns the unfoldedMarkerId.
     */
    public String getUnfoldedMarkerId() {
        return unfoldedMarkerId;
    }
    /**
     * @param unfoldedMarkerId The unfoldedMarkerId to set.
     */
    public void setUnfoldedMarkerId(String unfoldedMarkerId) {
        this.unfoldedMarkerId = unfoldedMarkerId;
    }
    /**
     * @return Returns the markerAttributes.
     */
    public MCSAttributes getMarkerAttributes() {
        return markerAttributes;
    }
    /**
     * @param markerAttributes The markerAttributes to set.
     */
    public void setMarkerAttributes(MCSAttributes markerAttributes) {
        this.markerAttributes = markerAttributes;
    }
}
