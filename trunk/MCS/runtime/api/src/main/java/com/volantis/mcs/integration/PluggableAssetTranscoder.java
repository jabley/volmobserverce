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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.integration;


/**
 * This interface permits different transcoder products to be integrated with
 * MCS. It is assumed that a single instance of an implementation of this
 * interface will be used by a given instance of MCS. The implementation
 * should, therefore, be thread safe and should not maintain state between
 * invocations.
 *
 * <p>In order to use a specific implementation of this interface in a deployed
 * instance of MCS, the implementation must be defined as the asset transcoder
 * plugin in the instance's configuration file. See the MCS Administrator's
 * Guide for details.</p>
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface PluggableAssetTranscoder {
    /**
     * This method will be called by MCS to construct the URL for a
     * ConvertibleImageAsset. If the method can not evaluate the parameters
     * correctly then it should throw a TranscodingException and should log an
     * appropriate error.
     * @param ctx  provides all information necessary to transcode the url
     *
     * @return the fully qualified url for the transcoded image
     * @throws TranscodingException if the parameters cannot be evaluated
     *                              correctly or some other problem is
     *                              encountered
     */
    String constructImageURL(AssetTranscoderContext ctx)
        throws TranscodingException;
    
    /**
     * This method will return the URL parameter used to specify the remote
     * host on which the image is stored.
     *
     * @return The parameter used to represent the remote host or null if not
     *         supported by this transcoder.
     */
    String getHostParameter();
    
    /**
     * This method will return the URL parameter used to specify the remote
     * host's port on which the image is stored.
     *
     * @return The parameter used to represent the remote port or null if not
     *         supported by this transcoder.
     */
    String getPortParameter();
    
    /**
     * This method will return the URL parameter used to specify the width of
     * the returned image in pixels.
     *
     * @return The parameter used to represent the width or null if not
     *         supported by this transcoder.
     */
    String getWidthParameter();
    
    /**
     * This method will return the URL parameter used to specify the maximum
     * size of the returned image in pixels.
     *
     * @return The parameter used to represent the maximum image size or null
     *         if not supported by this transcoder.
     */
    String getMaxImageSizeParameter();
    
    /**
     * This method will return the URL parameter used to specify the 
     * preserved area
     *
     * @return The parameter used to represent preserved area or null
     *         if not supported by this transcoder.
     */
    String getPreserveAreaParameter();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset
 
 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build
 
 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build
 
 04-Nov-04	6109/2	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names
 
 28-Jul-04	4976/1	philws	VBM:2004072801 Add the PluggableAssetTranscoder to the Public API
 
 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations
 
 ===========================================================================
 */
