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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.attributes.TabAttributes;
import com.volantis.styling.Styles;

/**
 * Tabs context.
 * 
 * <p>Helper class used to gather data for currently processed Tabs element.</p>
 */
public class TabsContext {

    private MarinerPageContext pageContext;
    private LinkedList tabAndLabelAttributes;
    private String activeTab;
    private String firstTab;
    private String contentId;
    private int colspan;  
    private RegionInstance contentsRegionInstance;
    private RegionInstance labelsRegionInstance;
    
    /**
     * Storage class for properties associated with one tab 
     */
    public static class TabWithLabelPair {
        public TabAttributes tab;
        public Styles activeLabelStyles;
        public boolean usesImageLabels;
        public String labelTdId;
        public String inactiveContentId;
        public String activeContentId;
    };
    
    public MarinerPageContext getPageContext() {
        return pageContext;
    }

    public void initPageContext(MarinerPageContext pageContext) {
        this.pageContext = pageContext;                
    }

    /**
     * Adds and returns a new properties structure for a tab
     * @return created structure 
     */
    public TabWithLabelPair registerNewTab() {
        if (null == tabAndLabelAttributes) {
            tabAndLabelAttributes = new LinkedList();
        }
        tabAndLabelAttributes.add(new TabWithLabelPair());
        return getCurrentTabAndLabel();
    }
    
    public TabWithLabelPair getCurrentTabAndLabel() {
        return (TabWithLabelPair)tabAndLabelAttributes.getLast();
    }
    
    public List getTabAndLabelAttributesList() {
        return Collections.unmodifiableList(tabAndLabelAttributes);
    }

    public String getActiveTabId() {
        return activeTab;
    }
    
    public void setActiveTabId(String input) {
        activeTab = input;
    }

    public String getFirstTabId() {
        return firstTab;
    }
    
    public void setFirstTabId(String value) {
        firstTab = value;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void addColspan(int i) {
        this.colspan += i;
    }
    
    public int getColspan() {
        return colspan;
    }
    
    /**
     * @return Returns the labelsRegionInstance.
     */
    public RegionInstance getLabelsRegionInstance() {
        return labelsRegionInstance;
    }

    /**
     * @param labelsRegionInstance
     *            The labelsRegionInstance to set.
     */
    public void setLabelsRegionInstance(RegionInstance labelsRegionInstance) {
        this.labelsRegionInstance = labelsRegionInstance;
    }

    /**
     * @return Returns the contentsRegionInstance.
     */
    public RegionInstance getContentsRegionInstance() {
        return contentsRegionInstance;
    }

    /**
     * @param contentsRegionInstance
     *            The contentsRegionInstance to set.
     */
    public void setContentsRegionInstance(RegionInstance contentsRegionInstance) {
        this.contentsRegionInstance = contentsRegionInstance;
    }
    
}
