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
 * $Header: /src/voyager/com/volantis/mcs/runtime/DefaultAssetURLRewriter.java,v 1.8 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Jan-02    Doug            VBM:2002011406 - Created.
 *                              DefaultAssetURLRewriter implements the
 *                              AssetURLRewriter interface and is used to
 *                              perform the default asset URL rewriting
 * 01-Feb-02    Doug            VBM:2002011406 - Changed this classes package
 *                              from com.volantis.mcs.integration to
 *                              com.volantis.mcs.runtime.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Apr-02    Paul            VBM:2002041901 - Added special code to cope
 *                              with DynamicVisualAssets with an encoding of
 *                              TV which need to be treated differently.
 * 26-Jul-02    Allan           VBM:2002072508 - Modified rewriteAssetUrl() to
 *                              cast the asset to a SubstantiveAsset when
 *                              calling computeExternalRepositoryURL. Changed
 *                              computeExternalRepositoryURL() to take a
 *                              SubstantiveAsset instead of an Asset.
 * 31-Jan-03    Byron           VBM:2003012712 - Added computeNoncacheableURL,
 *                              modified rewriteAssetURL to call new method and
 *                              reformatted style.
 * 19-Feb-03    Byron           VBM:2003012712 - Modified rewriteAssetURL
 *                              and computeNoncacheableURL to correct the
 *                              program flow in these merged methods.
 * 20-Feb-03    Byron           VBM:2003021809 - Modified rewriteAssetURL not
 *                              to do anything if the asset is a ChartAsset.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.PreservedArea;
import com.volantis.synergetics.log.LogDispatcher;


/**
 * Class that is used to Rewrite Asset URL values
 *
 */
public class DefaultAssetURLRewriter implements AssetURLRewriter {

  /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DefaultAssetURLRewriter.class);

  /**
   * The Volantis object
   */
  private Volantis volantisBean = null;


    /**
     * Determine if we have logged out a message common in viking settlements,
     * which may cause seasonal flooding of their console if they provide
     * GIGO in their policies WOW.
     *
     * Explanation:
     * American Casualty Department is ER.
     * A Chip is often nown as an IC.
     * Eric the Viking is Eric Bowman from Three.
     *
     * Eric has made several remartks about the code quality and uses
     * expressions like suprise suprise it doesn't work WOW!
     *
     * When pointed out the Garbage In = Garbage Out he complained that the
     * messages generated would flood his console (ah bless)!
     */
  private static boolean vikingCasualtyChipWOWFlag = false;

  /**
   * Method to allow an Asset URL to be rewritten
   *
   * @param  requestContext      the request context object
   * @param  asset               the asset associated with the URL
   * @param  assetGroup          the assets associated asset group
   * @param  marinerURL          the URL that is to be rewritten
   * @return                     the rewritten URL as a MarinerURL object that
   *                             is immutable (read only)
   * @throws RepositoryException if a repository exception occurs.
   */
  public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                    Asset asset,
                                    AssetGroup assetGroup,
                                    MarinerURL marinerURL)
          throws RepositoryException {

      // If the asset is a chart asset then do nothing.
      MarinerURL urlResult = null;
      if (asset instanceof ChartAsset) {
          urlResult = marinerURL;
      } else {
          // Compute the non-cacheable url and if we do not obtain a valid url
          // (the url is null) then we do have to build the 'cached URL'.
          urlResult = computeNoncacheableURL(requestContext,
                  assetGroup,
                  asset);
      }

      // If the resulting URL is null then we need to compute the cacheable
      // url.
      if (urlResult == null) {
          urlResult = computeCacheableURL(requestContext,asset, assetGroup,
                                          marinerURL);
      }
      return  urlResult;
  }

    /**
     * Creates a MarinerUrl
     *
     * @param requestContext
     * @param asset
     * @param assetGroup
     * @param marinerURL
     * @return
     * @throws RepositoryException
     */
    public static MarinerURL createMarinerURL(
            MarinerRequestContext requestContext,
            Asset asset,
            AssetGroup assetGroup,
            MarinerURL marinerURL) throws RepositoryException {

        MarinerURL urlResult = computeURL(requestContext,
                asset, assetGroup, marinerURL);

        if (asset instanceof ConvertibleImageAsset) {
            String url = urlResult.getExternalForm();

            // Construct the url for the convertible image asset.
            ConvertibleImageAsset convertible = (ConvertibleImageAsset)asset;
            PreservedArea area = PreservedArea.get(
                    convertible.getPreserveLeft(),
                    convertible.getPreserveRight(),
                    true);
            String value = ContextInternals.constructImageURL(
                    requestContext, url, area);

            urlResult = new MarinerURL(value);
        }

        // Complete the URL
        urlResult = completeURL(requestContext, asset,
                assetGroup,
                urlResult);

        // Make sure that the url cannot be modified (even if it is a
        // convertible image asset).
        urlResult.makeReadOnly();

        return urlResult;
    }


    /**
     * Return a <code>MarinerURL</code> for any cacheable urls.
     *
     * @param  requestContext      the mariner request context
     * @param  assetGroup          the assetgroup
     * @param  asset               the asset
     * @return                     a mariner url or null if none can be
     *                             computed.
     */
    protected MarinerURL computeCacheableURL(
            MarinerRequestContext requestContext,
            Asset asset,
            AssetGroup assetGroup,
            MarinerURL marinerURL) throws RepositoryException {

        MarinerURL urlResult = null;
        // we are not caching the URLs
        urlResult = createMarinerURL(requestContext, asset,
                assetGroup, marinerURL);
        if (logger.isDebugEnabled()) {
            logger.debug("Asset " + asset + " resolved absolute url '"
                    + urlResult + "' without using cache");
        }

        // If we get here we should definitely have a url from the cache or one
        // that has been computed and then completed.
        return urlResult;
    }

    /**
     * Return a <code>MarinerURL</code> (or null) for any non-cacheable urls.
     *
     * @param  requestContext      the mariner request context
     * @param  assetGroup          the assetgroup
     * @param  asset               the asset
     * @return                     a mariner url or null if none can be
     *                             computed.
     * @throws RepositoryException if a repository exception occurs.
     */
    protected MarinerURL computeNoncacheableURL(
            MarinerRequestContext requestContext,
            AssetGroup assetGroup,
            Asset asset)
                throws RepositoryException {

        // If the asset is a DynamicVisualAsset and the encoding is TV then the
        // asset url consists of the device specific tv channel prefix and the
        // value appended if it is set. If the tv channel prefix is not valid in
        // a url then we have problems.
        if (asset.getClass() == DynamicVisualAsset.class) {
            DynamicVisualAsset dynamicVisualAsset = (DynamicVisualAsset) asset;
            if (dynamicVisualAsset.getEncoding() == DynamicVisualAsset.TV) {
                MarinerPageContext marinerPageContext
                        = ContextInternals.getMarinerPageContext(requestContext);
                InternalDevice device = marinerPageContext.getDevice();

                String tvChannelPrefix = device.getTVChannelPrefix();
                String url = tvChannelPrefix;
                String value = dynamicVisualAsset.getValue();
                if (value != null) {
                    url += value;
                }
                return  new MarinerURL(url);
            }
        }
        return null;
    }

    /**
     * Method that computes the URL for an asset.
     * The computed URL may require completing at a later stage.
     * @param  requestContext the mariner request context
     * @param asset the Asset whose URL we are computing
     * @param assetGroup the AssetGroup associated with the Asset
     * @param marinerURL the URL that is being rewritten/computed
     * @return the computed URL
     */
    public static MarinerURL computeURL(MarinerRequestContext requestContext,
                                        Asset asset,
                                        AssetGroup assetGroup,
                                        MarinerURL marinerURL) {

        RuntimeProject project =
            (RuntimeProject)asset.getIdentity().getProject();
        if (project == null) {
            project = (RuntimeProject)requestContext.getCurrentProject();
        }
        MarinerURL prefix = null;
        MarinerURL newURL = marinerURL;

        if (assetGroup != null) {
            prefix = new MarinerURL(assetGroup.getPrefixURL());
            if (logger.isDebugEnabled()) {
                logger.debug("Retrieved prefix from asset group as " + prefix);
            }
        } else {
            prefix = getPrefixURL(project, asset);
            if (logger.isDebugEnabled()) {
                logger.debug("Retrieved prefix from project " +
                    project + " as " + prefix);
            }
        }
        if (prefix != null) {
            newURL = new MarinerURL(prefix, marinerURL);
         }

        return newURL;
    }

    private static MarinerURL getPrefixURL(
            RuntimeProject project, Asset asset) {

        VariantType variantType;
        if (asset instanceof AudioAsset) {
            variantType = VariantType.AUDIO;
        } else if (asset instanceof DynamicVisualAsset) {
            variantType = VariantType.VIDEO;
        } else if (asset instanceof ImageAsset) {
            variantType = VariantType.IMAGE;
        } else if (asset instanceof ScriptAsset) {
            variantType = VariantType.SCRIPT;
        } else if (asset instanceof TextAsset) {
            variantType = VariantType.TEXT;
        } else {
            variantType = null;
        }

        return project.getPrefixURL(variantType);
    }

    /**
     * Method that completes a computed URL
     * @param  requestContext the mariner request context
     * @param asset the Asset whose URL we are completing
     * @param assetGroup the AssetGroup associated with the Asset
     * @param marinerURL the URL that is being completed
     * @return the completed URL
     */
    public static MarinerURL completeURL(MarinerRequestContext requestContext,
                                         Asset asset,
                                         AssetGroup assetGroup,
                                         MarinerURL marinerURL) {

        // Get the project that owns the resource. If an asset group is
        // provided then it is the asset group's project, otherwise it is the
        // asset's project.
        RuntimeProject assetProject = (RuntimeProject) asset.getProject();
        RuntimeProject project = null;
        if (assetGroup == null) {
            project = assetProject;
        } else {
            project = (RuntimeProject) assetGroup.getProject();
        }
        if (project == null) {
            throw new IllegalStateException("Project not set");
        }

        MarinerURL assetsBaseURL = project.getAssetsBaseURL();
        if (isClientSideURL(asset, assetGroup)) {
            // Client side URLs are not affected by the project's base URL.
        } else if (project.getContainsOrphans() && project.isRemote()) {
            // Asset URLs from remote policies that are not in a remote project
            // should already be fully qualified.
            if (!marinerURL.isAbsolute()) {
                synchronized(DefaultAssetURLRewriter.class) {
                    if (!vikingCasualtyChipWOWFlag) {
                        vikingCasualtyChipWOWFlag = true;
                        logger.warn("url-not-absolute",
                                marinerURL.getExternalForm());
                    }
                }
            }
        } else if (marinerURL.containsDocumentRelativePath()) {

            // Document relative assets should only be resolved if the project
            // is portable, otherwise leave them as they are.
            // todo Always resolve these and then provide a way later to optimise URLs in the page to try and make them relative if possible.
            if (project.isPortable()) {

                // If the project is portable then get the project relative path to the
                // policy so that it can be used to resolve relative asset references
                // against.
                MarinerURL projectRelativePath;
                String name = asset.getName();
                if (name.startsWith("/")) {
                    projectRelativePath = new MarinerURL(name);
                } else {
                    projectRelativePath = new MarinerURL(
                            assetProject.makeProjectRelativePath(name, true));
                }

                // Resolve relative asset references against the project
                // relative path and then make sure that it can be resolved
                // against the assets base URL by removing leading /.
                marinerURL = new MarinerURL(projectRelativePath, marinerURL);
                marinerURL = new MarinerURL(
                        URLNormalizer.convertHostRelativeToDocumentRelative(
                                marinerURL.getExternalForm()));

                // Resolve the document relative asset URL against the assets
                // base URL.
                marinerURL = new MarinerURL(assetsBaseURL, marinerURL);

                // The result must be absolute, or host relative.
                if (marinerURL.isAbsolute()) {
                    // Nothing more to do.
                } else if (marinerURL.containsHostRelativePath()) {
                    // May need to make it relative to the context.
                    EnvironmentContext environmentContext =
                            ContextInternals.getEnvironmentContext(
                                    requestContext);
                    MarinerURL contextPath =
                            environmentContext.getContextPathURL();
                    marinerURL = new MarinerURL(contextPath,
                            URLNormalizer.convertHostRelativeToDocumentRelative(
                                    marinerURL.getExternalForm()));
                } else {
                    throw new IllegalStateException("The rewritten URL " +
                            marinerURL + " for remote asset " + asset + " " +
                            "must be absolute or host relative but is not");
                }
            }

        } else if (marinerURL.containsHostRelativePath()) {

            // Host relative paths are treated as being relative to the
            // project's asset base URL. This is because otherwise assets
            // and asset groups would need to know the host relative path to
            // the assets including the web application context (if any).

            // NOTE: I have the feeling this should be dealt with when the url
            // is computed but there is no description for the intermediate form
            // of the url so I am not prepared to change that. This class needs
            // rewriting/clarifying. Until then we are left with the following
            // bodge...
            // todo: later: deal with document relative urls which are from
            // asset groups which are host relative.
            //
            // If the url was computed from an asset group which was relative
            // to the host, then we should leave this as host relative.
            if (assetGroup != null && assetGroup.getLocationType() ==
                    AssetGroup.HOST) {
                // Leave the url as host relative. This will mean than when
                // resolved against the context the context is ignored.
                if (logger.isDebugEnabled()) {
                    logger.debug("leaving existing host relative url computed " +
                            "from host relative asset group url as host " +
                            "relative");
                }
            } else {
                // Either it was not from an asset group or the asset group was
                // context relative. In either case we should...

                // Strip the / off the front of the host relative URL to make it
                // a document relative URL so it will resolve against the base
                // URL properly.
                String url = URLNormalizer.convertHostRelativeToDocumentRelative(
                        marinerURL.getExternalForm());
                marinerURL = new MarinerURL(assetsBaseURL, url);
            }


            // The resulting URL must be either a host relative path or an
            // absolute URL. If it is not then it is a fatal error as it could
            // only have arisen due to invalid configuration which should have
            // been detected during init.
            if (marinerURL.isRelative() &&
                    marinerURL.containsDocumentRelativePath()) {
                throw new RuntimeException("The rewritten URL " + marinerURL +
                        " for Asset " + asset + " is not host relative or " +
                         "absolute.  The configuration is probably wrong.");
            }

        }
        return marinerURL;
    }

  /**
   * Is the asset a client-side asset or a server-side asset
   * @param asset the Asset
   * @param assetGroup the AssetGroup associated with the Asset
   * @return true if the asset is a  client-side asset, false if asset
   * is a server side-asset
   */
  protected static boolean isClientSideURL(Asset asset,
                                    AssetGroup assetGroup) {
    if(assetGroup != null &&
       assetGroup.getLocationType() == AssetGroup.ON_DEVICE) {
      return true;
    }
    if(asset instanceof ImageAsset &&
       ((ImageAsset)asset).isLocalSrc() ) {
      return true;
    }
    return false;
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Nov-05      10261/1 ianw    VBM:2005110812 Fixup memory leaks

 10-Nov-05      10233/1 ianw    VBM:2005110812 Fixup memory leaks

 07-Nov-05      10168/1 ianw    VBM:2005102504 port forward web clipping

 07-Nov-05      10170/1 ianw    VBM:2005102504 port forward web clipping

 28-Apr-05      7914/1  pduffin VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 19-Apr-05      7738/1  philws  VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05      7720/1  philws  VBM:2004102604 Localize RepositoryException messages

 11-Mar-05      7308/4  tom     VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 11-Mar-05      7308/2  tom     VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 17-Jan-05      6693/3  allan   VBM:2005011403 Remove MPS specific image url parameters

 17-Jan-05      6693/1  allan   VBM:2005011403 Remove MPS specific image url parameters

 08-Dec-04      6416/4  ianw    VBM:2004120703 New Build

 08-Dec-04      6416/2  ianw    VBM:2004120703 New Build

 29-Nov-04      6232/4  doug    VBM:2004111702 Refactored Logging framework

 27-Feb-04      3215/1  steve   VBM:2004021911 Patch from Proteus2 and fixes for RemoteProject

 25-Feb-04      3041/4  claire  VBM:2004021208 Fixed supermerge problems

 17-Feb-04      3041/1  claire  VBM:2004021208 Refactored RuntimeProject and added a unit test

 19-Feb-04      3090/4  ianw    VBM:2004021716 Merged

 19-Feb-04      3090/1  ianw    VBM:2004021716 Changed project stack to check enclosing request context first

 19-Feb-04      2789/6  tony    VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04      2789/4  tony    VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04      2846/19 claire  VBM:2004011915 Exception handling for incorrect complete URLs

 11-Feb-04      2846/17 claire  VBM:2004011915 Added more rewrite URL testcases

 11-Feb-04      2846/15 claire  VBM:2004011915 Ensured asset url rewriting works as specified, with testcases

 10-Feb-04      2846/13 claire  VBM:2004011915 Update to applying base URLs

 10-Feb-04      2846/11 claire  VBM:2004011915 Update to comments and JavaDoc

 09-Feb-04      2846/9  claire  VBM:2004011915 Exception for mishandled URLs

 09-Feb-04      2846/7  claire  VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 09-Feb-04      2846/5  claire  VBM:2004011915 Refactoring URL handling again

 09-Feb-04      2846/3  claire  VBM:2004011915 Refactoring URL handling

 05-Feb-04      2846/1  claire  VBM:2004011915 Asset URL computation based on base and prefix

 05-Dec-03      2075/1  mat     VBM:2003120106 Rename Device and add a public Device Interface

 26-Sep-03      1454/1  philws  VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 ===========================================================================
*/
