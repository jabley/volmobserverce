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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.integration;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.utilities.PreservedArea;

/**
 * This class encapsulates information necessary to transcode
 * asset URLs.
 */
public class AssetTranscoderContext {
    
    /**
     * The URL of the image to be transcoded. This will
     * include the host address of the transcoding server as
     * well as the relative path of the image and any optional
     * parameters
     */
    private String url;
    
    /**
     * The value of the rule obtained from the relevant
     * transcoding rule policy value. The transcoding
     * interface is not responsible for working out which rule
     * applies to the asset as this functionality is common to
     * all transcoding server
     */
    private String ruleValue;
    
    /**
     * The desired width for the image. The transcoding server
     * does not have to honour this value if the width is
     * larger than the source image. Must be greater than
     * zero
     */
    private int width;
    
    /**
     * The maximum size of the returned image. This is an
     * advisory to the Transcoding Server and does not have to
     * be strictly honored. Will be a positive value if set,
     * otherwise should be treated as undefined
     */
    private int maxSize;
    
    /**
     * Provided to enable additional device policies to be
     * queried to help make more inteligent decisions when
     * constructing the URL for the Transcoding Server
     */
    private MarinerRequestContext context;
    
    /**
     * The area of the image that should be preseved if the image is clipped.
     */
    private PreservedArea preservedArea;
    
    /**
     * Constructor
     * @param url the URL of the image to be transcoded
     * @param ruleValue the transcoder conversion rule
     * @param width the desired width for the image
     * @param maxSize the maximum size of the returned image
     * @param context the request context
     */
    public AssetTranscoderContext(String url,
            String ruleValue,
            int width,
            int maxSize,
            MarinerRequestContext context)
    {
        this(url,ruleValue,width,maxSize,context, null);
    }
    
    /**
     * Constructor
     * @param url the URL of the image to be transcoded
     * @param ruleValue the transcoder conversion rule
     * @param width the desired width for the image
     * @param maxSize the maximum size of the returned image
     * @param context request context
     * @param preservedArea the protected area of the image
     */
    public AssetTranscoderContext(String url,
            String ruleValue,
            int width,
            int maxSize,
            MarinerRequestContext context,
            PreservedArea preservedArea)
    {
        this.url = url;
        this.ruleValue = ruleValue;
        this.width = width;
        this.maxSize = maxSize;
        this.context = context;
        this.preservedArea = preservedArea;
    }
    
    /**
     * This gets the request context
     * @return request context
     */
    public MarinerRequestContext getContext() {
        return context;
    }
    
    /**
     * This sets the request context
     * @param context request context
     */
    public void setContext(MarinerRequestContext context) {
        this.context = context;
    }
    
    /**
     * This gets the maximum size of the returned image
     * @return the maximum size of the returned image
     */
    public int getMaxSize() {
        return maxSize;
    }
    
    /**
     * This set the maximum size of the returned image.
     * @param maxSize the maximum size of the returned image
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    /**
     * This gets the transcoder conversion rule.
     * @return the transcoder conversion rule
     */
    public String getRuleValue() {
        return ruleValue;
    }
    
    
    /**
     * This sets the transcoder conversion rule.
     * @param ruleValue the transcoder conversion rule
     */
    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }
    
    /**
     * This gets the URL of the image to be transcoded.
     * @return the URL of the image to be transcoded
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * This sets the URL of the image to be transcoded.
     * @param url the URL of the image to be transcoded
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * This gets he desired width for the image
     * @return the desired width for the image
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * This sets the desired width for the image.
     * @param width the desired width for the image
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * This gets  the protected area of the image.
     * @return  the protected area of the image
     */
    public PreservedArea getPreservedArea() {
        return preservedArea;
    }
    
    /**
     * This sets  the protected area of the image.
     * @param preservedArea  the protected area of the image
     */
    public void setPreservedArea(PreservedArea preservedArea) {
        this.preservedArea = preservedArea;
    }	
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10170/2	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	9999/3	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset

 ===========================================================================
*/
