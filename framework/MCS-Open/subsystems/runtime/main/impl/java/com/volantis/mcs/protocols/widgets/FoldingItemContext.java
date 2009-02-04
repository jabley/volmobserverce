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

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;

/**
 * This class is used to store context for currently processed
 * FoldingItem element.
 */
public class FoldingItemContext {
    private String summaryElementId;
    
    private String detailsElementId;
    
    private String foldedMarkerId;
    
    private String unfoldedMarkerId;
    
    private String summaryUnfoldedStyles;
    
    private Styles foldedMarkerStyles;
    
    private Styles unfoldedMarkerStyles;

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
     * @return Returns the summaryUnfoldedStyles.
     */
    public String getSummaryUnfoldedStyles() {
        return summaryUnfoldedStyles;
    }
    /**
     * @param summaryUnfoldedStyles The summaryUnfoldedStyles to set.
     */
    public void setSummaryUnfoldedStyles(String summaryUnfoldedStyles) {
        this.summaryUnfoldedStyles = summaryUnfoldedStyles;
    }

    /**
     * get/set folded marker value for storing styles of unfolded marker in folding-item
     * context.
     * @return
     */
    public Styles getFoldedMarkerStyles() {
        return foldedMarkerStyles;
    }

    /**
     * @param foldedMarkerStyles
     */
    public void setFoldedMarkerStyles(Styles foldedMarkerStyles) {
        this.foldedMarkerStyles = foldedMarkerStyles;
    }

    /**
     * get/set unfolded marker value for storing styles of unfolded marker in folding-item
     * context.
     * @return unfolded marker styles
     */
    public Styles getUnfoldedMarkerStyles() {
        return unfoldedMarkerStyles;
    }

    /**
     * @param unfoldedMarkerStyles
     */
    public void setUnfoldedMarkerStyles(Styles unfoldedMarkerStyles) {
        this.unfoldedMarkerStyles = unfoldedMarkerStyles;
    }

}
