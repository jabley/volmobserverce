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
 * $Header: /src/voyager/com/volantis/mcs/context/ApplicationContext.java,v 1.5 2003/04/28 15:27:11 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 31-Jan-03    Phil W-S        VBM:2003013013 - Updated to add the packager
 *                              and packageResources properties. Also remove
 *                              the redundant private no-argument constructor.
 *                              Fix the javadoc to use @return instead of
 *                              @returns while I'm at it.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 25-Apr-03    Mat             VBM:2003033108 - Added encoding property.
 * 13-May-03    Mat             VBM:2003033108 - Changed the encoding 
 *                              property to be the EncodingManager
 * 30-May-03    Mat             VBM:2003042911 - Added WMLCSupported property.
 *                              Also fixed debug statements in 
 *                              canvasTagSupported which weren't surrounded
 *                              by isDebugEnabled()
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.integration.ImageURLModifier;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.runtime.RuntimePageURLRewriter;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.PackagedURLEncoder;
import com.volantis.mcs.runtime.packagers.Packager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.StringHash;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * This context provides application dependent information to Mariner to enable
 * other applications to alter the behaviour of Mariner core functionality.
 * The application is able to change the behaviour of Device recognition,
 * Asset URL rewritting and whether Dissection and Fragmentation are supported.
 * The context can also act as a container for comunicating with applications.
 */
public class ApplicationContext {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ApplicationContext.class);

    /**
     * The ImageURLModifier associated with this ApplicationContext.
     */
    private ImageURLModifier imageURLModifier;

    /**
     * Map containg references and values for SMIL textual assets.
     */
    protected Map smilAssetMap = new HashMap();

    /**
     * The application specific AssetURLRewriter.
     */
    protected AssetURLRewriter assetURLRewriter;

    /**
     * The application specific PageURLRewriter.
     */
    private PageURLRewriter pageURLRewriter;

    /**
     * The (optional) packager that should be used to package the associated
     * request's response.
     *
     * @supplierRole packager
     * @supplierCardinality 0..1
     */
    protected Packager packager;

    /**
     * The (optional) packaged URL encoder instance used to encode URLs for use
     * in a package. Note that this is only of interest if the {@link
     * #packager} is non-null.
     *
     * @supplierRole packagedURLEncoder
     * @supplierCardinality 0..1
     */
    protected PackagedURLEncoder packagedURLEncoder;

    /**
     * The (optional) package resources instance used to identify the
     * package resources that may be and are utilized in the associated
     * request's response. Note that this is only of interest if the
     * {@link #packager} is non-null.
     *
     * @supplierRole packageResources
     * @supplierCardinality 0..1
     */
    protected PackageResources packageResources;

    /**
     * The application specific device.
     */
    protected InternalDevice device;

    /**
     * The application  protocol.
     */
    protected VolantisProtocol protocol;

    /**
     * The current request context.
     */
    protected MarinerRequestContext requestContext;

    /*
     * Support for Dissecting Panes
     */
    protected boolean dissectionSupported;

    /*
     * Support for Form Fragmentation.
     */
    protected boolean fragmentationSupported;

    /**
     * Support for the PAPI Canvas Tag.
     */
    protected boolean canvasTagSupported;
    
    /**
     * Whether the device supports WMLC
     */
    protected boolean WMLCSupported;
    
    /**
     * The encoding manager for this context.
     */
    protected EncodingManager encodingManager;

    /**
     * Instanciate a new application context.
     * @param requestContext MarinerRequestContext
     */
    public ApplicationContext(MarinerRequestContext requestContext) {
        initialise(requestContext);
    }

    /**
     * This method provides any application specific initialisation and
     * is called from the constructor.
     * @param requestContext MarinerRequestContext
     */
    public void initialise(MarinerRequestContext requestContext) {

        this.requestContext = requestContext;
    }

    /**
     * Get the ImageURLModifier associated with this ApplicationContext.
     * @return the ImageURLModifier or null if no ImageURLModifier has been
     * set on this ApplicationContext.
     */
    public ImageURLModifier getImageURLModifier() {
        return imageURLModifier;
    }

    /**
     * Set the ImageURLModifier associated with this ApplicationContext.
     * @param imageURLModifier the ImageURLModifier - can by null.
     */
    public void setImageURLModifier(ImageURLModifier imageURLModifier) {
        this.imageURLModifier = imageURLModifier;
    }
    
    /**
     * Get the application specific AssetURLRewriter.
     * 
     * <p>
     * This rewriter will not be automatically combined in the runtime with the
     * user defined rewriter from Volantis bean. This rewriter should
     * incorporate the user defined rewriter into itself.
     * </p>
     * 
     * <p>
     * Note, that this is not the case with PageURLRewriters.
     * </p> 
     * 
     * @return The application specific AssetURLWriter.
     */
    public AssetURLRewriter getAssetURLRewriter() {
        return assetURLRewriter;
    }

    public void setAssetURLRewriter(AssetURLRewriter assetURLRewriter) {
        this.assetURLRewriter = assetURLRewriter;
    }

    /**
     * Get the application specific PageURLRewriter (may be null).
     * 
     * <p>
     * Note, that in case there's a global (user-defined) rewriter specified on
     * Volantis bean, the returned rewriter is combined with it, so that the
     * user defined rewriter is invoked before application specific rewriter.
     * </p>
     * 
     * <p>
     * While PageURLRewriters works as described above, this is still not the
     * case with the AssetURLRewriter, which needs to be explicitely combined
     * with the user defined rewriter.
     * </p>
     * 
     * @return the application specific PageURLWriter (may be null).
     */
    public PageURLRewriter getPageURLRewriter() {
        return pageURLRewriter;
    }
    
    /**
     * Sets application specific PageURLRewriter (may be null).
     * 
     * @param pageURLRewriter application specific PageURLRewriter.
     */
    public void setPageURLRewriter(PageURLRewriter pageURLRewriter) {
        // Get global rewriter (this is never null).
        RuntimePageURLRewriter globalRewriter = Volantis.getInstance().getPageURLRewriter();
        
        // If global rewriter contains user-defined rewriter and application
        // specific rewriter is specified, combine them both into single
        // rewriter. Otherwise, use one or none of them.
        if ((pageURLRewriter != null) && globalRewriter.isUserDefinedRewriter()) {
            this.pageURLRewriter = new CombinedPageURLRewriter(
                    globalRewriter, pageURLRewriter);
        } else {
            this.pageURLRewriter = globalRewriter.isUserDefinedRewriter() ?
                    globalRewriter : pageURLRewriter;
        }
    }

    /**
     * Returns the application specific Packager. May be null.
     *
     * @return the application specific Packager
     */
    public Packager getPackager() {
        return packager;
    }

    /**
     * Allows the application specific Packager to be set or reset. A null
     * value is allowed.
     *
     * @param packager the new Packager or null if packaging is not enabled
     */
    public void setPackager(Packager packager) {
        this.packager = packager;
    }

    /**
     * Returns the application specific PackagedURLEncoder instance. May be
     * null (and will be returned as null if packaging is not enabled even if
     * a value has been set).
     *
     * @return the application specific PackagedURLEncoder instance.
     */
    public PackagedURLEncoder getPackagedURLEncoder() {
        PackagedURLEncoder encoder = packagedURLEncoder;

        if (packager == null) {
            encoder = null;
        }

        return encoder;
    }

    /**
     * Allows the application specific PackagedURLEncoder instance to be set
     * or reset. A null value is allowed.
     *
     * @param packagedURLEncoder the new PackagedURLEncoder instance or
     *                             null if packaging is not enabled
     */
    public void setPackagedURLEncoder(PackagedURLEncoder packagedURLEncoder) {
        this.packagedURLEncoder = packagedURLEncoder;
    }

    /**
     * Returns the application specific PackageResources instance. May be
     * null (and will be returned as null if packaging is not enabled even if
     * a value has been set).
     *
     * @return the application specific PackageResources instance
     */
    public PackageResources getPackageResources() {
        PackageResources pr = packageResources;

        if (packager == null) {
            pr = null;
        }

        return pr;
    }

    /**
     * Allows the application specific PackageResources instance to be set
     * or reset. A null value is allowed.
     *
     * @param packageResources the new PackageResources instance or
     *                             null if packaging is not enabled
     */
    public void setPackageResources(PackageResources packageResources) {
        this.packageResources = packageResources;
    }

    /**
     * Get the application specific InternalDevice.
     * @return The application specific InternalDevice.
     */
    public InternalDevice getDevice() {
        return device;
    }

    /**
     * Set the application specific InternalDevice.
     * @param device The InternDevice.
     */
    public void setDevice(InternalDevice device) {
        this.device = device;
    }

    /**
     * Get the application specific Protocol.
     * @return The application specific Protocol.
     */
    public VolantisProtocol getProtocol() {
        return protocol;
    }

    /**
     * Set the application specific Protocol.
     * @param protocol The name of the protocol.
     */
    public void setProtocol(VolantisProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Get the application specific support for dissecting panes.
     * @return true if Dissecting Panes are supported.
     */
    public boolean isDissectionSupported() {
        return dissectionSupported;
    }

    /**
     * Set the application specific support for dissecting panes.
     * @param dissectionSupported True to support dissecting panes.
     */
    public void setDissectionSupported(boolean dissectionSupported) {
        this.dissectionSupported = dissectionSupported;
    }

    /**
     * Get the application specific support for Fragmentation.
     * @return true if Fragmentation are supported.
     */
    public boolean isFragmentationSupported() {
        return fragmentationSupported;
    }

    /**
     * Set the application specific support for Form Fragmentation.
     * @param fragmentationSupported True to support Form Fragmentation.
     */
    public void setFragmentationSupported(boolean fragmentationSupported) {
        this.fragmentationSupported = fragmentationSupported;
    }

    /**
     * Get the application specific support for Canvas Tag.
     * @return true if Message Tag is supported.
     */
    public boolean isCanvasTagSupported() {
        if(logger.isDebugEnabled()) {
            logger.debug("Returning canvasTagSupported as " + canvasTagSupported);
        }
        return canvasTagSupported;
    }

    /**
     * Set the application specific support for Canvas Tag.
     * @param canvasTagSupported True to support Message Tag.
     */
    public void setCanvasTagSupported(boolean canvasTagSupported) {
        if(logger.isDebugEnabled()) {
            logger.debug("Setting canvasTagSupported to " + canvasTagSupported);
        }
        this.canvasTagSupported = canvasTagSupported;
    }
    
    /**
     * Whether the device being used can accept WMLC
     * 
     * @param WMLCSupported  True if the device accepts WMLC
     */
    public void setWMLCSupported(boolean WMLCSupported) {
        if(logger.isDebugEnabled()) {
            logger.debug("WMLCSupported set to  = " + WMLCSupported);
        }
        this.WMLCSupported = WMLCSupported;
    }
    
    /**
     * Whether the device being used can accept WMLC
     * @return true if WMLC is supported.
     */
    public boolean isWMLCSupported() {
        if(logger.isDebugEnabled()) {
            logger.debug("Returning WMLCSupported as " + WMLCSupported);
        }
        return WMLCSupported;
    }
    
    /**

    /**
     * Map a SMIL textual reference to a value.
     * @param value The value for the textual component.
     * @return The key that is used to identity this bit of text. Here it is
     * an SHA digest of the text to make it globally unique
     */
    public String mapSMILAsset(String value) {
        String mimeReference = StringHash.getDigestAsHex(value);
        smilAssetMap.put(mimeReference, value);
        return mimeReference;
    }

    /**
     * Retrieves the SMIL asset Map containing references and textual objects.
     * @return SMIL asset map.
     */
    public Map getSMILAssetMap() {
        return smilAssetMap;
    }

    /** Getter for property encoding.
    * @return Value of property encoding.
    *
     */
    public EncodingManager getEncodingManager() {
      return encodingManager;
    }

    /** Setter for property encodingManager.
    * @param encodingManager New value of property encodingManager.
    *
     */
    public void setEncodingManager(EncodingManager encodingManager) {
      this.encodingManager = encodingManager;
    }
       
    /**
     * A combined PageURLRewriter, which combines global (user defined) rewriter
     * specified on Volantis bean with the local (application specific) rewriter
     * specified on this application context.
     */
    private class CombinedPageURLRewriter implements PageURLRewriter {
        private final RuntimePageURLRewriter globalRewriter;
        private final PageURLRewriter localRewriter;
        
        /**
         * Initializes this instance with global (user defined) and local
         * (application specific) rewriters.
         * 
         * @param globalRewriter the global (user defined) rewriter.
         * @param localRewriter the local (application specific) rewriter.
         */
        public CombinedPageURLRewriter(RuntimePageURLRewriter globalRewriter, PageURLRewriter localRewriter) {
            this.globalRewriter = globalRewriter;
            this.localRewriter = localRewriter;
        }
        
        // Javadoc inherited
        public MarinerURL rewriteURL(MarinerRequestContext context, MarinerURL url, PageURLDetails details) {
            // First, rewrite the URL using user defined rewriter specified on Volantis bean.
            url = globalRewriter.rewriteURL(context, url, details);

            // Then, rewrite the URL using application specific rewriter.
            url = localRewriter.rewriteURL(context, url, details);
            
            return url;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 17-Jan-05	6693/1	allan	VBM:2005011403 Remove MPS specific image url parameters

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 15-Sep-04	5521/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 14-Sep-04	5519/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/3	mat	VBM:2003120106 Correct javadoc and tidy imports

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 05-Jun-03	285/3	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
