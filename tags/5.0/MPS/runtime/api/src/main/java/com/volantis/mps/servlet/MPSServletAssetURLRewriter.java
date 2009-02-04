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
 * $Header: /src/mps/com/volantis/mps/servlet/MPSServletAssetURLRewriter.java,v 1.11 2003/01/30 16:42:58 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002091806 - Created
 * 28-Nov-02    Chris W         VBM:2002112704 - Instead of using the asset's
 *                              url to reference the asset in a message we use
 *                              the a string of hex digits representing the 
 *                              fixed length MD5 hash in rewriteAssetURL. Some
 *                              devices (e.g. phones) cannot handle forward
 *                              slashes in references. 
 * 09-Dec-02    Chris W         VBM:2002120913 - rewriteAssetURL prefixes mime
 *                              references with cid: to use Content-ID instead
 *                              of Content-Location.
 * 17-Jan-03    ianw            VBM:2003010708 - Changed asset url's to used
 *                              cid: + filename so that images display on all 
 *                              devices including Netscape.
 * 20-Jan-03    ianw            VBM:2003011504: Fixed problem with links 
 *                              being mime encoded.
 * 30-Jan-03    ianw            VBM:2003012911 - Added code to compute
 *                              correct extension for ConvertibleImageAssets.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.servlet;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.LinkAsset;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.synergetics.utilities.Base64;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mps.context.MPSApplicationContext;
import com.volantis.mps.message.MessageAsset;

import javax.servlet.http.HttpServletResponse;

/**
 * Thsi AssetURLRewriter maps Mime References to assets and stores them in the
 * <CODE>ApplicationContext</CODE> as well as encoding them in the response
 * headers. Mime References are generated by replacing '?' & '&' with '/' 
 * and '=' with '-'.
 */
public class MPSServletAssetURLRewriter implements AssetURLRewriter {
    
    private Volantis volantisBean;
    
    /** Creates a new instance of MPSAssetURLRewriter */
    public MPSServletAssetURLRewriter() {
    }
    
    //javadoc inherited
    public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext, 
                                      Asset asset, 
                                      AssetGroup assetGroup, 
                                      MarinerURL marinerURL) 
            throws RepositoryException {

        // save away mcs.ie parameter as the default URL rewritter will remove 
        // it
        String imageEncodingString = null;        
        if (asset instanceof ConvertibleImageAsset) {
            if (marinerURL != null) {
                imageEncodingString = marinerURL.getParameterValue("mcs.ie");
            }
        }

        AssetURLRewriter defaultAssetURLRewriter = 
            getVolantisBean(requestContext).getAssetURLRewriter();
        
        MarinerURL rewrittenURL = 
            defaultAssetURLRewriter.rewriteAssetURL(requestContext, 
                                                    asset, 
                                                    assetGroup, 
                                                    marinerURL);
        // We do not want to mime encode links
        if (asset instanceof LinkAsset) {
            return rewrittenURL;
        } else {
            // To create a unique reference, generate a digest of the url and
            // convert it into a hex String.                
            //String newUrl = StringHash.getDigestAsHex(oldUrl);
            String oldUrl = rewrittenURL.getExternalForm();
            String newUrl;
            
            // Add the appropriate file extension for encoded images.
            if (imageEncodingString != null) {
                int imageEncoding=new Integer(imageEncodingString).intValue();
                newUrl = getFileName(oldUrl) + "." +
                    ImageAsset.fileExtension(imageEncoding);
            } else {
                newUrl = getFileName(oldUrl);
            }
        
            MPSApplicationContext applicationContext = 
                (MPSApplicationContext)ContextInternals
                    .getApplicationContext(requestContext);

            HttpServletResponse response = 
                (HttpServletResponse)((MarinerServletRequestContext)requestContext).getResponse();

            if (assetGroup != null) {
                response.setHeader("asset." +
                                   assetGroup.getLocationType() +
                                   "." + 
                                   Base64.encodeString(newUrl),
                                   Base64.encodeString(oldUrl));
                applicationContext.mapAsset(newUrl,
                        new MessageAsset(assetGroup.getLocationType(), oldUrl)); 
            } else {
                response.setHeader("asset." +
                                   AssetGroup.ON_SERVER + 
                                   "." +
                                   Base64.encodeString(newUrl),
                                   Base64.encodeString(oldUrl));
                applicationContext.mapAsset(newUrl,
                        new MessageAsset(AssetGroup.ON_SERVER, oldUrl)); 
            }

            // Add headers for maximum file and MM (message) sizes.
            // This reuses the names found in the
            // MarinerPageContext#constructURL method and also used in the
            // MPSApplicationContext#setDevice method
            Integer maxFileSize = applicationContext.getMaxFileSize();
            Integer maxMMSize = applicationContext.getMaxMMSize();
            if (maxFileSize != null) {
                response.setHeader("maxfilesize", maxFileSize.toString());
            }
            if (maxMMSize != null) {
                response.setHeader("maxmmsize", maxMMSize.toString());
            }

            // Cannot put cid: in the string passed to the MarinerURL constructor
            // because the constructor thinks that cid is the protocol, when in
            // actual fact we want to use cid: to refer to the Content-ID header.

            //return new MarinerURL(newUrl);
            MarinerURL urlToReturn = new MarinerURL();
            urlToReturn.setPath("cid:"+newUrl);
            return urlToReturn;
        }
    }
    
  /**
   * Method to return the Volantis bean
   * @param requestContext the MarinerRequestContext
   * @return the Volantis bean
   */
  private Volantis getVolantisBean(MarinerRequestContext requestContext) {
    if(volantisBean == null) {
      MarinerPageContext marinerPageContext = 
	ContextInternals.getMarinerPageContext(requestContext);
      volantisBean = marinerPageContext.getVolantisBean();
    }
    return volantisBean;
  }
  
  private String getFileName(String pathName) {
      
    if (pathName != null) {
        int fnStart = pathName.lastIndexOf('/')+1;
        
        String fileName=pathName.substring(fnStart);
        fileName = fileName.replace('?', '_');
        fileName = fileName.replace('=','-');
        fileName = fileName.replace('&', '_');

            
        return fileName;
    }
    return pathName;
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	927/1	matthew	VBM:2005100401 Base64 has moved

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 30-Jul-04	133/3	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 ===========================================================================
*/