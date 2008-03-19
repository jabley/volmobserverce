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

package com.volantis.mcs.clientapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.volantis.mcs.clientapp.ItemGalleryFeed.GalleryItem;

/**
 * Construct response for gallery widget ajax request.
 */
class GalleryResponseBuilder extends SampleAppResponseBuilder {

    /**
     * Property  pointing first element to read from list
     */
    private String MCS_START_PARAM = "mcs-start";
    
    /**
     * Property pointing last element to read from list
     */
    private String MCS_END_PARAM = "mcs-end";
    
    /**
     * Name of attribute describing total galery size
     */
    private String COUNT_ATTRIBUTE = "count";
    
    /**
     * Feed providing data for gallery
     */
    private ItemGalleryFeed itemGalleryFeed;
    
    
    public GalleryResponseBuilder(ItemGalleryFeed itemGalleryFeed){
        this.itemGalleryFeed = itemGalleryFeed;
    }

    /**
     * Method writes additional namespaces for gallery.
     */
    protected void openResponse(PrintWriter out) throws IOException {
        out.println("<response:response " +
                "xmlns=\"http://www.w3.org/2002/06/xhtml2\" " +
                "xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\" " +
                "xmlns:widget=\"http://www.volantis.com/xmlns/2006/05/widget\" " +
                "xmlns:response=\"http://www.volantis.com/xmlns/2006/05/widget/response\" "+
                "xmlns:gallery=\"http://www.volantis.com/xmlns/2006/10/gallery-widget\">");
    }    
    
    // javadoc inherited
    protected void writeBodyContents(Map params, PrintWriter out)
            throws IOException {
        
        List items = this.itemGalleryFeed.getGalleryItems();
        Object mcsStart;
        Object mcsEnd;
        int mcsStartInt = 0;
        int mcsEndInt = items.size();
        if((mcsStart = params.get(MCS_START_PARAM)) != null){
            mcsStartInt = new Integer(((String[])mcsStart)[0]).intValue()-1;
        }
        if((mcsEnd = params.get(MCS_END_PARAM))!= null){
            mcsEndInt = new Integer(((String [])mcsEnd)[0]).intValue();
        }
        Map attributes = new HashMap();
        attributes.put(COUNT_ATTRIBUTE,new Integer(items.size()));
        
        items = items.subList(mcsStartInt,mcsEndInt);
        
        Iterator iter = items.iterator();
        openGalleryElement(out,attributes,"items");
        while(iter.hasNext()){
            openGalleryElement(out,new HashMap(),"item");
            GalleryItem item = (GalleryItem)iter.next();
            out.print("<widget:summary style='mcs-effect-style:fade; mcs-effect-duration:1s'>");
            out.print("<div>");
            out.print("<object src='/"+item.itemSummaryLink+"'><param name='mcs-transcode' value='false'/></object></div>");
            out.print("</widget:summary>");
            out.print("<widget:detail style='mcs-effect-style:slide-left; mcs-effect-duration:1s'>");
            out.print("<div style=\"text-align: center;width:100%;height:100%\">" +
                    "<div style=\"width:100%;height:10%\">");
            out.print(item.itemDetailsText);
            out.print("</div>");
            out.print("<div style=\"width:100%;height:80%\"><object src='/"+item.itemDetailsLink+"'>" +
                    "   <param name='mcs-transcode' value='false'/></object></div>");
            out.print("<div style=\"width:100%;height:10%\">");
            out.print(item.itemDetailsDescription);
            out.print("</div></div>");
            out.print("</widget:detail>");            
            closeGalleryElement(out,"item");
        }
        closeGalleryElement(out,"items");
    }
    
    /**
     * Open markup from gallery namespace.
     * 
     * @param out - writer
     * @param attributesMap - attributes to be written to markup
     * @param markup - element name
     */
    private void openGalleryElement(PrintWriter out,Map attributesMap, String elementName){
        out.print("<gallery:"+elementName+" ");
        Iterator iter = attributesMap.keySet().iterator();
        while(iter.hasNext()){
            String key = (String)iter.next();
            out.print(" "+key+"=\""+attributesMap.get(key).toString()+"\"");
        }
        out.print(">");
    }
    
    /**
     * Close markup from gallery namespace.
     * 
     * @param out
     * @param markup
     */
    private void closeGalleryElement(PrintWriter out, String elementName){
        out.print("</gallery:"+elementName+">");
    }

}
