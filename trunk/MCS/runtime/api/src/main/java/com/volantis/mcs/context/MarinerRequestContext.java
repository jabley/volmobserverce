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
 * $Header: /src/voyager/com/volantis/mcs/context/MarinerRequestContext.java,v 1.41 2003/03/26 11:43:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 19-Nov-01    Paul            VBM:2001110202 - Moved the MarinerFacilities
 *                              code into here.
 * 27-Nov-01    Paul            VBM:2001112601 - Moved the releasing of
 *                              MarinerPageContext resources into
 *                              JspMarinerRequestContext and removed the
 *                              initialisedMarinerPageContext flag.
 * 28-Nov-01    Paul            VBM:2001112202 - This class was moved from the
 *                              papi package and renamed from PAPIPageContext.
 * 29-Nov-01    Paul            VBM:2001112906 - Added an EnvironmentContext
 *                              property and package private methods to access
 *                              it.
 * 29-Nov-01    Mat             VBM:2001112913 - Added getAncestorRelationship
 *                              and a bunch of static ints for the
 *                              return codes.
 * 11-Dec-01    Doug            VBM:2001120404 - Added the method
 *                              flushExternalRepositoryPluginCache() to flush
 *                              the external repository plugin cache.
 * 19-Dec-01    Doug            VBM:2001121701 - Added the public methods
 *                              getFragments() and getFragment(String). Added
 *                              a private list property to store the
 *                              Fragments that belong to the current layout.
 * 19-Dec-01    Paul            VBM:2001120506 - Made the javadoc comments
 *                              more consistent.
 * 21-Dec-01    Paul            VBM:2001121702 - Minor change to format of
 *                              javadoc.
 * 31-Jan-02    Paul            VBM:2001122105 - PAPIClassFactory was renamed
 *                              to PAPIInternals.
 * 01-Feb-02    Doug            VBM:2002011406 - deprecated the method
 *                              retrieveAbsoluteURLAsString() as we can no
 *                              longer guarantee that an absolute URL is
 *                              returned.
 * 13-Feb-02    Paul            VBM:2002021203 - Added methods to manipulate
 *                              PageGenerationKey objects.
 * 15-Feb-02    Paul            VBM:2002021203 -  Replaced literal url
 *                              parameter names with constants from
 *                              URLConstants, also renamed the method
 *                              writeServerSideInclude to
 *                              includeContentComponent.
 * 19-Feb-02    Paul            VBM:2001100102 - Added methods to access the
 *                              request parameters which are equivalent to the
 *                              methods available on a ServletRequest.
 * 20-Feb-02    Steve           VBM:2002021404 - Added methods to retrieve a
 *                              named FormFragment from the papi so that
 *                              link text can be changed at run-time.
 * 08-Mar-02    Paul            VBM:2002030607 - Corrected problem in javadoc.
 * 12-Mar-02    Paul            VBM:2002021203 - Removed the
 *                              getPortletGenerationKey method which takes
 *                              a region name, deprecated the version which
 *                              just took an index and added a version which
 *                              takes no parameters and gets all its
 *                              information from the current region context.
 * 14-Mar-02    Mat             VBM:2002031403 - Don't generate canonical
 *                              specifiers in getPageGenerationKey() if
 *                              returning to the default fragment.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 *  9-Apr-02    Ian             VBM:2002032002 - Created createNestedRequest()
 *                              method to remove servlet dependencies in MAML.
 * 22-Apr-02    Paul            VBM:2002041901 - Added retrieveAssetURLAsString
 *                              method as a replacement for the deprecated
 *                              retrieveAbsoluteURLAsString. Also added
 *                              retrieveBest... methods for most of the asset
 *                              types.
 * 02-May-02    Steve           VBM:2002040817 - Added retrieveRemotePolicies()
 *                              to allow remote policy preloading through
 *                              a servlet or JSP.
 * 07-May-02    Steve           VBM:2002040817 - Implements RemoteCacheConstants
 *                              so that user has access to cache index numbers
 *                              from JSP or Servlet. Added the following methods
 *                              to flush the remote caches. flushRemotePolicyCache()
 *                              and flushAllRemotePolicyCaches()
 * 07-May-02    Adrian          VBM:2002042302 - Modified method getContent..
 *                              ..Extension() to retrieve the file extension
 *                              from the current device policies.  Updated the
 *                              javadoc for methods getContentExtension and
 *                              getAncestorRelationship to define the possible
 *                              return values.  Deprecated methods getPageURL
 *                              getPageGenerationKey & getPortletGenerationKey
 *                              stating that they are only for internal use as
 *                              they need to be public but are not for public
 *                              use.
 * 07-May-02    Adrian          VBM:2002042302 - updated javadoc for method
 *                              getAncestorRelationship to correctly define all
 *                              possible return values.
 * 29-May-02    Paul            VBM:2002050301 - Added getBrandedName and
 *                              getBrandedIdentity methods.
 * 24-Jul-02    Adrian          VBM:2002072401 - Added retrieveBest.....Asset
 *                              methods: Chart, Dynvis with encodings, Link,
 *                              and Scripts.  For each there is a method with
 *                              a String name as a parameter and another with
 *                              a ComponentIdentity as a parameter.
 * 02-Sep-02    Sumit           VBM:2002030703 Added functions to get a list
 *                              of PaneFormats and a PaneFormat by name for
 *                              the Panes in this layout
 * 09-Sep-02    Ian             VBM:2002081307 - Added getTranscodingRule
  *                             method.
 * 18-Nov-02    Geoff           VBM:2002111504 - Refactored to avoid deprecated
 *                              versions of methods in MarinerPageContext, also
 *                              cleaned up a javadoc and imports a bit.
 * 03-Nov-02    Geoff           VBM:2002120306 - Cleanup before rewriting
 *                              Volantis.java - call Volantis direct for
 *                              RepositoryManagers and caching rather than
 *                              use delegating methods on MarinerPageContext.
 * 17-Dec-02    Allan           VBM:2002121711 - Added contentTypeCharSet
 *                              property along with public get/set methods.
 * 20-Jan-03    Mat             VBM:2002112212 - Added retrieveBestAudioAsset
 *                              which doesn't take an encoding.  Used when
 *                              searching for a DeviceAudioAsset.
 * 20-Jan-03    Allan           VBM:2002121901 - Modified contentTypeCharSet
 *                              property renamed to characterEncoding with its
 *                              get/set methods. Updated javadoc.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported ApplicationContext
 *                              changes from Metis.
 * 12-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 21-Mar-03    Byron           VBM:2003031907 - Added getDissectingPane method
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.LinkAsset;
import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.components.AudioComponentIdentity;
import com.volantis.mcs.components.ChartComponentIdentity;
import com.volantis.mcs.components.DynamicVisualComponentIdentity;
import com.volantis.mcs.components.ImageComponentIdentity;
import com.volantis.mcs.components.LinkComponentIdentity;
import com.volantis.mcs.components.ScriptComponentIdentity;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.objects.AbstractRepositoryObjectIdentity;
import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.mcs.papi.DissectingPane;
import com.volantis.mcs.papi.PaneFormat;
import com.volantis.mcs.papi.impl.PAPIInternals;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.compatibility.EnumerationConverter;
import com.volantis.mcs.policies.compatibility.IntObjectMap;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.protocols.layouts.FormFragmentInstance;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.FragmentInstance;
import com.volantis.mcs.repository.AudioRepositoryManager;
import com.volantis.mcs.repository.ChartRepositoryManager;
import com.volantis.mcs.repository.DynamicVisualRepositoryManager;
import com.volantis.mcs.repository.ImageRepositoryManager;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.TextRepositoryManager;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class encapsulates all the state associated with the current page
 * which is being processed.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
 */
public abstract class MarinerRequestContext
    implements RemoteCacheConstants
{

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(MarinerRequestContext.class);

    // The following four finals need to be viewable to the
   // outside world.
  /**
   * Used as a return code for getAncestorRelationship(deviceName)
   *
   * The relationship cannot be determined.
   * This is usually because there is no device currently associated
   * with the request
   */
  public static final int UNKNOWN = -1;

  /**
   * Used as a return code for getAncestorRelationship(deviceName)
   *
   * The device named in the parameter is unrelated to the device
   * currently in use
   */
  public static final int IS_UNRELATED = 0;

  /**
   * Used as a return code for getAncestorRelationship(deviceName)
   *
   * The device named in the parameter is an ancestor of the device
   * currently in use
   */
  public static final int IS_ANCESTOR = 1;

  /**
   * Used as a return code for getAncestorRelationship(deviceName)
   *
   * The device named in the parameter is the device currently in use
   */
  public static final int IS_DEVICE = 2;

  /**
   * The wrapped <code>MarinerPageContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this member.
   * </h2>
   */
  private MarinerPageContext pageContext;

  /**
   * The wrapped <code>EnvironmentContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this member.
   * </h2>
   */
  private EnvironmentContext environmentContext;

  /**
   * A list of <code>Fragment</code> objects that represents the
   * fragments that belong to the current layout
   * <h2>
   * You MUST NOT change the protection level on this member.
   * </h2>
   */
  private List fragments;

  /**
   * The wrapped <code>ApplicationContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this member.
   * </h2>
   */
  private ApplicationContext applicationContext;

  /**
   * A list of <code>Pane</code> objects that represents the
   * fragments that belong to the current layout
   * <h2>
   * You MUST NOT change the protection level on this member.
   * </h2>
   */
  private List panes;

    /**
     * The <code>MarinerRequestContext</code> of the enclosing page.
     */
    protected MarinerRequestContext enclosingRequestContext;

    /**
     * The <code>MarinerApplication</code> associated with this request context.
     */
    private MarinerApplication marinerApplication;

    /**
     * The connection used within the page.
     *
     * @deprecated See {@link #getPageConnection()}.
     */
    private RepositoryConnection pageConnection;


    /**
     * Create a new <code>MarinerRequestContext</code>.
     */
    protected MarinerRequestContext() {
    }

    /**
     * Create a new <code>MarinerRequestContext</code> around an existing
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code> to wrap.
     * 
     * @todo apparently unused? can be deleted? used by MPS???
     */
    protected MarinerRequestContext(MarinerRequestContext requestContext) {
        pageContext = requestContext.pageContext;
    }

    // todo: can these javadocs be updated?
   
    /**
     * <p>Get the <code>characterEncoding</code> property to use when
     * generating the ContentType attribute in the HTTP response.</p>
     *
     * <p>If this request context is nested inside another, then the enclosing
     * request context's character encoding will be used (doesn't make
     * sense to change the character encoding within a page).</p>
     *
     * <p>NOTE: This method is not currently supported in a JSP environment.</p>
     *
     * @return The <code>characterEncoding</code> property or null if the
     * property has not been set for this <code>MarinerRequestContext</code>.
     */
    public String getCharacterEncoding() {
        if (enclosingRequestContext !=  null) {
            return enclosingRequestContext.getCharacterEncoding();
        } else {
            return pageContext.getCharsetName();
        }
    }

    /**
     * <p>Set the <code>charset</code> property on the <code>ContentType</code>
     * attribute. The HTTP response generated by Mariner based on this <code>
     * MarinerRequestContext</code> will use the specified value to set the
     * <code>charset</code> property of the <code>ContentType</code>
     * attribute.</p>
     *
     *<p>If this request context is nested inside another, then this method
     * will have no effect (the character encoding can only be modified on
     * the outermost canvas - it does not make sense to change the character 
     * encoding within a page).</p>
     *
     * <p>NOTE: This method is not currently supported in a JSP environment.</p>
     *
     * @param  characterEncoding The value of the <code>charset</code>
     * property.
     * @throws IllegalArgumentException if the charset was not known to
     *      the underlying platform.
     * @volantis-api-exclude-from PublicAPI
     */
    public void setCharacterEncoding(String characterEncoding)
            throws IllegalArgumentException {
        if (enclosingRequestContext == null) {
            pageContext.setCharsetName(characterEncoding);
        }
    }    

  /**
   * Set the wrapped MarinerPageContext.
   * <h2>
   * You MUST NOT change the protection level on this method.
   * </h2>
   * <p>
   * To access this from another package you must use
   * {@link ContextInternals#setMarinerPageContext}.
   * </p>
   * @param marinerPageContext The MarinerPageContext.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  void setMarinerPageContext (MarinerPageContext marinerPageContext) {
    this.pageContext = marinerPageContext;
  }

  /**
   * Get the wrapped MarinerPageContext.
   * <h2>
   * You MUST NOT change the protection level on this method.
   * </h2>
   * <p>
   * To access this from another package you must use
   * {@link ContextInternals#getMarinerPageContext}.
   * </p>
   * @return The wrapped MarinerPageContext.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  MarinerPageContext getMarinerPageContext () {
    return pageContext;
  }

  /**
   * Set the <code>EnvironmentContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this method.
   * </h2>
   * <p>
   * To access this from another package you must use
   * {@link ContextInternals#setEnvironmentContext}.
   * </p>
   * @param environmentContext The <code>EnvironmentContext</code>.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  void setEnvironmentContext (EnvironmentContext environmentContext) {
    this.environmentContext = environmentContext;
  }

  /**
   * Get the <code>EnvironmentContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this method.
   * </h2>
   * <p>
   * To access this from another package you must use
   * {@link ContextInternals#getEnvironmentContext}.
   * </p>
   * @return The <code>EnvironmentContext</code>.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  EnvironmentContext getEnvironmentContext () {
    return environmentContext;
  }

  /**
   * Set the <code>ApplicationContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this method.
   * </h2>
   * <p>
   * To access this from another package you must use
   * {@link ContextInternals#setApplicationContext}.
   * </p>
   * @param applicationContext The <code>ApplicationContext</code>.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  void setApplicationContext (ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Get the <code>ApplicationContext</code>.
   * <h2>
   * You MUST NOT change the protection level on this method.
   * </h2>
   * <p>
   * To access this from another package you must use
   * {@link ContextInternals#getApplicationContext}.
   * </p>
   * @return The <code>ApplicationContext</code>.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
  ApplicationContext getApplicationContext () {
    return applicationContext;
  }

  // --------------------------------------------------------------------------
  //           Public methods.
  // --------------------------------------------------------------------------

    /**
     * Return an instance of a class that can be used to access and manage
     * audio assets and components held in the Mariner repository.
     *
     * @return An AudioRepositoryManager object.
     * @deprecated Use {@link com.volantis.mcs.project.PolicyManager} instead.
     *             This was deprecated in version 3.5.1.
     */
    public AudioRepositoryManager getAudioRepositoryManager() {
        return pageContext.getVolantisBean().getAudioRepositoryManager();
    }

    /**
     * Return an instance of a class that can be used to access and manage
     * chart assets and components held in the Mariner repository.
     *
     * @return A ChartRepositoryManager object
     * @deprecated Use {@link com.volantis.mcs.project.PolicyManager} instead.
     *             This was deprecated in version 3.5.1.
     */
    public ChartRepositoryManager getChartRepositoryManager() {
        return pageContext.getVolantisBean().getChartRepositoryManager();
    }

    /**
     * Return an instance of a class that can be used to access and manage
     * dynamic visual assets and components held in the Mariner repository.
     *
     * @return A DynamicVisualRepositoryManager object.
     * @deprecated Use {@link com.volantis.mcs.project.PolicyManager} instead.
     *             This was deprecated in version 3.5.1.
     */
    public DynamicVisualRepositoryManager getDynamicVisualRepositoryManager() {
        return pageContext.getVolantisBean().getDynamicVisualRepositoryManager();
    }

    /**
     * Return an instance of a class that can be used to access and manage
     * image assets and components held in the Mariner repository.
     *
     * @return An ImageRepositoryManager object.
     * @deprecated Use {@link com.volantis.mcs.project.PolicyManager} instead.
     *             This was deprecated in version 3.5.1.
     */
    public ImageRepositoryManager getImageRepositoryManager() {
        return pageContext.getVolantisBean().getImageRepositoryManager();
    }

    /**
     * Return an instance of a class that can be used to access and manage
     * text assets and components held in the Mariner repository.
     *
     * @return A TextRepositoryManager object.
     * @deprecated Use {@link com.volantis.mcs.project.PolicyManager} instead.
     *             This was deprecated in version 3.5.1.
     */
    public TextRepositoryManager getTextRepositoryManager() {
        return pageContext.getVolantisBean().getTextRepositoryManager();
    }

  /**
   * Return a connection that can be used when accessing the Mariner
   * repository using repository manager objects.
   * <p>
   * <B>NOTE:</B>Connections retrieved using this mechanism must <B>NOT</B>
   * be explicitly disconnected. These connections are managed automatically
   * by Mariner on behalf of the JSP.
   * @return A connection to the Mariner repository.
   * @deprecated Use {@link com.volantis.mcs.project.PolicyBuilderManager}
   */
  public RepositoryConnection getPageConnection() {
      if (pageConnection == null) {
          Volantis volantis = pageContext.getVolantisBean();
          InternalProject project = volantis.getDefaultProject();
          LocalPolicySource source = (LocalPolicySource) project.getPolicySource();
          LocalRepository repository = source.getRepository();
          try {
              pageConnection = repository.connect();
          } catch (RepositoryException e) {
              throw new ExtendedRuntimeException(e);
          }
      }

      return pageConnection;
  }

   /**
    * Return the name for the device being used to access the current page.
    * @return The name of the device.
    */
   public String getDeviceName () {
     return pageContext.getDeviceName ();
   }

  /**
   * Return the value of the specified policy for the device being used to
   * access the current page.
   * @param policyName The name of the policy whose value is required
   * @return The value of the specified policy
   */
  public String getDevicePolicyValue (String policyName) {
    return pageContext.getDevicePolicyAccessor()
            .getDevicePolicyValue (policyName);
  }

  /**
   * Return the appropriate Transcoding rule for the device rendering
   * parameters.
   * @param renderMode The rendering mode COLOR or MONOCHROME.
   * @param encodingType The image encoding type.
   * @param pixelDepth The device pixel depth.
   * @return The rule name or null.
   */
  public String getTranscodingRule(int renderMode, int encodingType, int pixelDepth ) {
    return MarinerPageContext.getTranscodingRule(renderMode,
                                                 encodingType,
                                                 pixelDepth);
  }

    /**
     * Retrieve the absolute URL associated with the specified asset
     *
     * @param asset The asset whose URL is to be determined
     * @return The absolute URL associated with the asset
     * @throws RepositoryException An exception encountered when
     *                             accessing The Mariner repository.
     * @deprecated No longer guaranteed to return an absolute URL. Use
     *             {@link #retrieveVariantURLAsString(PolicyReference, Encoding)}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public String retrieveAbsoluteURLAsString(Asset asset)
            throws RepositoryException {

        // Make sure that the asset has a project set.
        asset = ensureHasProject(asset);

        return pageContext.getAssetResolver().computeURLAsString(asset);
    }

    /**
     * Ensure that the asset has a project set.
     *
     * @param asset The asset to check.
     * @return The original asset if it has a project set, or a clone with the
     *         project set to the current project.
     */
    private Asset ensureHasProject(Asset asset) {
        if (asset.getProject() == null) {
            asset = (Asset) asset.clone();
            asset.setProject(getCurrentProject());
        }
        return asset;
    }

    /**
     * Retrieve the URL associated with the specified asset
     *
     * @param asset The asset whose URL is to be determined
     * @return The URL associated with the asset
     * @throws RepositoryException An exception encountered when
     *                             accessing the repository.
     * @deprecated Use
     *             {@link #retrieveVariantURLAsString(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public String retrieveAssetURLAsString(Asset asset)
            throws RepositoryException {

        // Make sure that the asset has a project set.
        asset = ensureHasProject(asset);

        return pageContext.getAssetResolver().computeURLAsString(asset);
    }

    /**
     * This method retrieves policies from a remote policy server and adds them
     * to the remote repository. This is achieved by sending a request for
     * policies at the supplied URL. The policies returned from the URL are
     * added to the remote repository caches.
     *
     * @param url The URL which will return a RemotePolicySetResponse which can
     *            be processed and added to the remote repository caches.
     * @throws RepositoryException An exception encountered while retrieving
     *                             the policies from the URL or
     *                             when processing the returned response.
     * @deprecated use
     *             {@link MarinerApplication#preloadRemotePolicies(String)}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void retrieveRemotePolicies(String url)
            throws RepositoryException {
        marinerApplication.preloadRemotePolicies(url);
    }

    /**
     * Flush a given remote policy cache where the URL of the policy starts
     * with a path.
     *
     * @param cache The ID of the cache as defined in RemoteCacheConstants
     * @param path  The URL prefix of the items to remove from the cache
     * @deprecated use
     *             {@link MarinerApplication#flushRemotePolicyCache(String)}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushRemotePolicyCache(int cache, String path) {
        marinerApplication.flushRemotePolicyCache(path);
    }

    /**
     * Flush all remote entries from a given policy cache
     *
     * @param cache The ID of the cache as defined in RemoteCacheConstants
     * @deprecated use
     *             {@link MarinerApplication#flushRemotePolicyCache()}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushRemotePolicyCache(int cache) {
        marinerApplication.flushRemotePolicyCache();
    }

    /**
     * Flush all remote policy caches where the path of the policy matches a
     * given path.
     *
     * @param path The URL prefix of the items to remove from all caches
     * @deprecated use
     *             {@link MarinerApplication#flushRemotePolicyCache(String)}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushAllRemotePolicyCaches(String path) {
        marinerApplication.flushRemotePolicyCache(path);
    }

    /**
     * Flush all remote policy caches of all entries.
     *
     * @deprecated use
     *             {@link MarinerApplication#flushRemotePolicyCache()}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushAllRemotePolicyCaches() {
        marinerApplication.flushRemotePolicyCache();
    }

    /**
     * Retrieve the variant URL as a string.
     *
     * <p>Retrieves the referenced policy and selects the best variant from
     * it. It then calculates a URL to the referenced resource.</p>
     *
     * @param reference The reference to the policy.
     * @param encoding  The optional required encoding for the selected variant.
     * @return A URL to the resource referenced from the best variant, or null
     *         if either the policy could not be found, no variant was
     *         acceptable, or no URL could be generated.
     * @since 3.5.1
     */
    public String retrieveVariantURLAsString(PolicyReference reference,
                                             Encoding encoding) {

        RuntimePolicyReference runtimePolicyReference =
                createPolicyReference(reference);

        return pageContext.getAssetResolver().retrieveVariantURLAsString(
                runtimePolicyReference, getEncodingCollection(encoding));
    }

    /**
     * Selects the best variant from the referenced policy.
     *
     * <p>If the policy cannot be found then this returns null, otherwise it
     * returns an instance of {@link BestVariant}, even if no variant could be
     * selected. This is to enable calling code to be able to access alternates
     * from the policy without having to retrieve it again.</p>
     *
     * <p>The policy within the returned {@link BestVariant} will be the policy
     * to which the variant belongs which will not necessarily be the
     * referenced policy if it had alternates.</p>
     *
     * @param reference The reference to the policy.
     * @param encoding  The optional required encoding for the selected variant.
     * @return The best variant, or null if the policy could not be found.
     * @since 3.5.1
     */
    public BestVariant selectBestVariant(PolicyReference reference,
                                           Encoding encoding) {

        RuntimePolicyReference runtimePolicyReference =
                createPolicyReference(reference);

        final AssetResolver resolver = pageContext.getAssetResolver();
        SelectedVariant selected = resolver.selectBestVariant(
                runtimePolicyReference, getEncodingCollection(encoding));

        BestVariant best;
        if (selected == null) {
            best = null;
        } else {
            best = new BestVariantImpl(selected.getPolicy(),
                    selected.getVariant());
        }
        return best;
    }

    /**
     * Retrieve the URL for the asset associated with the specified audio
     * component.
     *
     * @param name     The name of the audio component for which the asset URL
     *                 is required.
     * @param encoding The type of encoding of the desired asset. Valid values
     *                 are defined as constants in {@link AudioAsset}.
     * @return The URL for the asset, returned as a String, or <B>null</B>
     *         if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use
     *             {@link #retrieveVariantURLAsString(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public String retrieveAudioAssetURLAsString(String name, int encoding)
            throws RepositoryException {

        RuntimePolicyReference reference = createPolicyReference(name,
                PolicyType.AUDIO);
        final AssetResolver resolver = pageContext.getAssetResolver();
        return resolver.retrieveVariantURLAsString(reference,
                getEncodingCollection(EnumerationConverter.AUDIO_ENCODING,
                        encoding));
    }

    /**
     * Retrieve the asset associated with the specified audio component with
     * the specified encoding.
     *
     * @param identity The identity of the audio component for which the asset
     *                 is required.
     * @param encoding The type of encoding of the desired asset. Valid values
     *                 are defined as constants in {@link AudioAsset}.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public AudioAsset retrieveBestAudioAsset(AudioComponentIdentity identity,
                                             int encoding)
            throws RepositoryException {

        return (AudioAsset) getAsset(createPolicyReference(identity,
                PolicyType.AUDIO),
                getEncodingCollection(EnumerationConverter.AUDIO_ENCODING,
                        encoding));
    }

    private EncodingCollection getEncodingCollection(
            final IntObjectMap converter, int encoding) {
        return getEncodingCollection((Encoding) converter.get(encoding));
    }

    private EncodingCollection getEncodingCollection(final Encoding encoding) {
        if (encoding == null) {
            return null;
        } else {
            final EncodingCollectionFactory encodingCollectionFactory =
                EncodingCollectionFactory.getDefaultInstance();
            return encodingCollectionFactory.createEncodingCollection(encoding);
        }
    }

    /**
     * Retrieve the asset associated with the specified audio component with
     * the specified encoding.
     *
     * @param name     The name of the audio component for which the asset is
     *                 required.
     * @param encoding The type of encoding of the desired asset. Valid values
     *                 are defined as constants in {@link AudioAsset}.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public AudioAsset retrieveBestAudioAsset(String name, int encoding)
            throws RepositoryException {

        return (AudioAsset) getAsset(createPolicyReference(name,
                PolicyType.AUDIO),
                getEncodingCollection(EnumerationConverter.AUDIO_ENCODING,
                        encoding));
    }

    /**
     * Retrieve the asset associated with the specified audio component.
     *
     * @param identity The identity of the audio component for which the asset
     *                 is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public AudioAsset retrieveBestAudioAsset(AudioComponentIdentity identity)
            throws RepositoryException {

        return (AudioAsset) getAsset(createPolicyReference(identity,
                PolicyType.AUDIO), null);
    }

    /**
     * Retrieve the asset associated with the specified audio component.
     *
     * @param name The name of the audio component for which the asset is
     *             required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public AudioAsset retrieveBestAudioAsset(String name)
            throws RepositoryException {

        return (AudioAsset) getAsset(createPolicyReference(name,
                PolicyType.AUDIO), null);
    }

    /**
     * Retrieve the asset associated with the specified chart component.
     *
     * @param identity The identity of the chart component for which the asset
     *                 is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @volantis-api-exclude-from PublicAPI
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public ChartAsset retrieveBestChartAsset(ChartComponentIdentity identity)
            throws RepositoryException {

        return (ChartAsset) getAsset(createPolicyReference(identity,
                PolicyType.CHART), null);
    }

    /**
     * Retrieve the asset associated with the specified chart component.
     *
     * @param name The name of the chart component for which the asset is
     *             required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @volantis-api-exclude-from PublicAPI
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public ChartAsset retrieveBestChartAsset(String name)
            throws RepositoryException {

        return (ChartAsset) getAsset(createPolicyReference(name,
                PolicyType.CHART), null);
    }

    /**
     * Return the URL of the dynamic visual asset associated with the specified
     * component most appropriate for the device currently in use.
     *
     * @param name The name of the dynamic visual component for which the asset
     *             URL is required
     * @return The URL for the asset as a String, or <B>null</B> if no asset
     *         can be found.
     * @throws RepositoryException An exception associated with access
     *                             to the Mariner repository.
     * @deprecated Use
     *             {@link #retrieveVariantURLAsString(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public String retrieveDynamicVisualAssetURLAsString(String name)
            throws RepositoryException {

        RuntimePolicyReference reference = createPolicyReference(name,
                PolicyType.VIDEO);
        return pageContext.getAssetResolver().retrieveVariantURLAsString(
                reference, null);
    }

    /**
     * Retrieve the asset associated with the specified dynamic visual
     * component which is most appropriate for the device requesting the
     * current page.
     *
     * @param name The name of the dynamic visual component for which the asset
     *             is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public DynamicVisualAsset retrieveBestDynamicVisualAsset(String name)
            throws RepositoryException {

        return (DynamicVisualAsset) getAsset(createPolicyReference(name,
                PolicyType.VIDEO), null);
    }

    /**
     * Retrieve the asset associated with the specified dynamic visual
     * component with the specified encoding which is most appropriate for the
     * device requesting the current page.
     *
     * @param identity The identity of the dynamic visual component for which
     *                 the asset is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public DynamicVisualAsset retrieveBestDynamicVisualAsset
            (DynamicVisualComponentIdentity identity)
            throws RepositoryException {

        return (DynamicVisualAsset) getAsset(createPolicyReference(identity,
                PolicyType.VIDEO), null);
    }

    /**
     * Retrieve the asset associated with the specified dynamic visual
     * component with the specified encoding which is most appropriate for the
     * device requesting the current page.
     *
     * @param name     The name of the dynamic visual component for which the
     *                 asset is required.
     * @param encoding The type of encoding of the desired asset. Valid values
     *                 are defined as constants in
     *                 {@link com.volantis.mcs.assets.DynamicVisualAsset}.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public DynamicVisualAsset retrieveBestDynamicVisualAsset(String name,
                                                             int encoding)
            throws RepositoryException {

        return (DynamicVisualAsset) getAsset(createPolicyReference(name,
                PolicyType.VIDEO),
                getEncodingCollection(EnumerationConverter.VIDEO_ENCODING,
                        encoding));
    }

    /**
     * Retrieve the asset associated with the specified dynamic visual
     * component with the specified encoding which is most appropriate for the
     * device requesting the current page.
     *
     * @param identity The identity of the dynamic visual component for which
     *                 the asset is required.
     * @param encoding The type of encoding of the desired asset. Valid values
     *                 are defined as constants in {@link DynamicVisualAsset}.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public DynamicVisualAsset retrieveBestDynamicVisualAsset
            (DynamicVisualComponentIdentity identity, int encoding)
            throws RepositoryException {

        return (DynamicVisualAsset) getAsset(createPolicyReference(identity,
                PolicyType.VIDEO),
                getEncodingCollection(EnumerationConverter.VIDEO_ENCODING,
                        encoding));
    }

    private Asset getAsset(SelectedVariant selected) {
        return selected == null ? null : (Asset) selected.getOldObject();
    }

    private Asset getAsset(RuntimePolicyReference reference,
                           EncodingCollection requiredEncodings) {
        SelectedVariant selected = pageContext.getAssetResolver().
                selectBestVariant(reference, requiredEncodings);
        return getAsset(selected);
    }

    /**
     * Retrieve the URL of the image asset associated with the specified
     * component which is most appropriate for use on the device accessing the
     * current page.
     *
     * @param name The name of the image component for which the asset
     *             URL is required.
     * @return The URL associated with the asset, or <B>null</B> if
     *         there is none suitable.
     * @throws RepositoryException An exception associated with accessing the
     *                             Mariner repository.
     * @deprecated Use
     *             {@link #retrieveVariantURLAsString(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public String retrieveImageAssetURLAsString(String name)
            throws RepositoryException {

        return pageContext.getAssetResolver().retrieveVariantURLAsString(
                createPolicyReference(name,PolicyType.IMAGE), null);
    }

    /**
     * Retrieve the asset associated with the specified image component
     * which is most appropriate for the device requesting the current page.
     *
     * @param name The name of the image component for which the asset
     *             is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public ImageAsset retrieveBestImageAsset(String name)
            throws RepositoryException {

        return (ImageAsset) getAsset(createPolicyReference(name,
                PolicyType.IMAGE), null);
    }

    /**
     * Retrieve the asset associated with the specified image component
     * which is most appropriate for the device requesting the current page.
     *
     * @param identity The identity of the image component for which
     *                 the asset is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public ImageAsset retrieveBestImageAsset(ImageComponentIdentity identity)
            throws RepositoryException {

        return (ImageAsset) getAsset(createPolicyReference(identity,
                PolicyType.IMAGE), null);
    }

    /**
     * Retrieve the asset associated with the specified link component
     * which is most appropriate for the device requesting the current page.
     *
     * @param identity The identity of the link component for which the asset
     *                 is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public LinkAsset retrieveBestLinkAsset(LinkComponentIdentity identity)
            throws RepositoryException {

        return (LinkAsset) getAsset(createPolicyReference(identity,
                PolicyType.LINK), null);
    }

    /**
     * Retrieve the asset associated with the specified link component
     * which is most appropriate for the device requesting the current page.
     *
     * @param name The name of the link component for which the asset is
     *             required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public LinkAsset retrieveBestLinkAsset(String name)
            throws RepositoryException {

        return (LinkAsset) getAsset(createPolicyReference(name,
                PolicyType.LINK), null);
    }

    /**
     * Retrieve the asset associated with the specified script component
     * which is most appropriate for the device requesting the current page.
     *
     * @param identity The identity of the script component for which the asset
     *                 is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public ScriptAsset retrieveBestScriptAsset(ScriptComponentIdentity identity)
            throws RepositoryException {

        return (ScriptAsset) getAsset(createPolicyReference(identity,
                PolicyType.SCRIPT), null);
    }

    /**
     * Retrieve the asset associated with the specified script component
     * which is most appropriate for the device requesting the current page.
     *
     * @param name The name of the script component for which the asset is
     *             required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public ScriptAsset retrieveBestScriptAsset(String name)
            throws RepositoryException {

        return (ScriptAsset) getAsset(createPolicyReference(name,
                PolicyType.SCRIPT), null);
    }

    /**
     * Retrieve the asset associated with the specified text component
     * which is most appropriate for the device requesting the current page.
     *
     * @param name The name of the text component for which the asset
     *             is required.
     * @return The asset, or <B>null</B> if no asset can be found.
     * @throws RepositoryException If repository exception occured
     * @deprecated Use {@link #selectBestVariant(PolicyReference, Encoding)}
     *             instead. This was deprecated in version 3.5.1.
     */
    public TextAsset retrieveBestTextAsset(String name)
            throws RepositoryException {

        return (TextAsset) getAsset(createPolicyReference(name,
                PolicyType.TEXT), null);
    }

    private RuntimePolicyReference createPolicyReference(
            RepositoryObjectIdentity identity, PolicyType policyType) {

        PolicyReferenceFactory factory =
                pageContext.getPolicyReferenceFactory();
        return factory.createLazyNormalizedReference(
                (RuntimeProject) identity.getProject(),
                pageContext.getBaseURL(), identity.getName(), policyType);
    }

    private RuntimePolicyReference createPolicyReference(
            String name, PolicyType policyType) {
        PolicyReferenceFactory factory =
                pageContext.getPolicyReferenceFactory();
        return factory.createLazyNormalizedReference(
                pageContext.getCurrentProject(),
                pageContext.getBaseURL(), name, policyType);
    }

    private RuntimePolicyReference createPolicyReference(PolicyReference reference) {
        RuntimePolicyReference runtimePolicyReference;
        if (reference instanceof RuntimePolicyReference) {
            runtimePolicyReference = (RuntimePolicyReference) reference;
        } else {
            PolicyReferenceFactory factory =
                    pageContext.getPolicyReferenceFactory();

            return factory.createLazyNormalizedReference(
                    pageContext.getCurrentProject(),
                    pageContext.getBaseURL(),
                    reference.getName(),
                    reference.getExpectedPolicyType());
        }
        return runtimePolicyReference;
    }

    /**
     * Get the branded version of a policy name.
     *
     * @param name The unbranded policy name.
     *
     * @return The branded version of the policy name.
     */
    public String getBrandedName(String name) {
        return pageContext.getBrandedName(name);
    }

    /**
     * Get the branded version of a component identity.
     *
     * @deprecated Branding is done automatically by MCS so there should be no
     *             need to do this, however, if necessary
     *             {@link #getBrandedName(String)} can be used. This was
     *             deprecated in version 3.5.1.
     */
    public RepositoryObjectIdentity getBrandedIdentity
            (RepositoryObjectIdentity identity) {

        String brandedName = pageContext.getBrandedName(identity.getName());

        // Need to map back from policy identity to old style identity.
        AbstractRepositoryObjectIdentity abstractIdentity =
                (AbstractRepositoryObjectIdentity) identity;

        return abstractIdentity.getRenamedIdentity(brandedName);
    }

    /**
     * Retrieve the level of authentication of the current user.
     *
     * @deprecated Not supported, always returns -1. This was deprecated prior
     *             to version 3.5.1.
     */
    public int getAuthenticationLevel() {
        return -1;
    }

    /**
     * Return the type of user currently accessing the page
     *
     * @deprecated Not supported, always returns -1. This was deprecated prior
     *             to version 3.5.1.
     */
    public int getUserType()
            throws RepositoryException {

        return -1;
    }

    /**
     * Retrieve the current user's identification.
     *
     * @deprecated Not supported, always returns null. This was deprecated
     *             prior to version 3.5.1.
     */
    public String getUserIdentification() {
        return null;
    }

  /**
   * Retrieve an estimate of the bandwidth available to the
   * device currently accessing the page. The estimate will be
   * based on the actual bandwidth available if possible. Failing
   * that, values from the repository will be used, if available.
   * @return An estimate of the bandwidth available to the device
   * currently accessing the page. If no estimate is available,
   * 0 is returned.
   * @throws RepositoryException An exception encountered when accessing the
   * Mariner repository
   */
  public int getCurrentBandWidthAsBAUD ()
    throws RepositoryException {

    return pageContext.getCurrentBandWidthAsBAUD ();
  }

    /**
     * A method to flush all the cached content in Mariner.
     *
     * @deprecated Use {@link MarinerApplication#flushAllCaches}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushAllCaches() {
        marinerApplication.flushAllCaches();
    }

    /**
     * A method to flush all the theme cached content in Mariner.
     *
     * @deprecated Use {@link MarinerApplication#flushThemeCache}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushThemeCache() {
        marinerApplication.flushThemeCache();
    }

    /**
     * A method to flush all the layout cached content in Mariner.
     *
     * @deprecated Use {@link MarinerApplication#flushLayoutCache}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushLayoutCache() {
        marinerApplication.flushLayoutCache();
    }

    /**
     * A method to flush all the device cached content in Mariner.
     * Note that calling this method does not flush the device
     * patterns (@see refreshDevicePatterns)
     *
     * @deprecated Use {@link MarinerApplication#flushDeviceCache}
     *             instead.  This was deprecated prior to version 3.5.1.
     */
    public void flushDeviceCache() {
        marinerApplication.flushDeviceCache();
    }

    /**
     * A method to clear the device patterns and re-initialise them.
     *
     * @deprecated Use {@link MarinerApplication#refreshDevicePatterns}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void refreshDevicePatterns() {
        marinerApplication.refreshDevicePatterns();
    }

    /**
     * A method to flush all the external repository plugin cached
     * content in Mariner.
     *
     * @deprecated External repository plugins have not been supported in MCS
     *             for a while so this method does nothing.  This was
     *             deprecated prior to version 3.5.1.
     */
    public void flushExternalRepositoryPluginCache() {
    }

    /**
     * A method to flush all the component cached content in Mariner.
     *
     * @deprecated Use {@link MarinerApplication#flushComponentAssetCache}
     *             instead. This was deprecated prior to version 3.5.1.
     */
    public void flushComponentAssetCache() {
        marinerApplication.flushComponentAssetCache();
    }


  /**
   * Check whether the deviceName given is an ancestor
   * of the current device.
   * @param deviceName The device to check
   * @return MarinerRequestContext.IS_DEVICE if the specified device is the
   * same as the device currently accessing the page.
   * Or MarinerRequestContext.IS_ANCESTOR if the specified device is an
   * ancestor of the device currently accessing the page.
   * Or MarinerRequestContext.IS_UNRELATED if the specified device is not an
   * ancestor of the device currently accessing the page.
   */
  public int getAncestorRelationship (String deviceName) {
      return pageContext.getAncestorRelationship (deviceName);
  }

    /**
     * A method to return the List of <code>Fragment</code> objects that belong
     * to the current Layout.
     *
     * @return a List of <code>Fragment</code> objects
     */
    public List getFragments() {
        // If the list has already been generated the return it
        if (null != fragments) {
            return fragments;
        }

        List fragmentsList = new ArrayList();
        Map fragmentInstances =
                pageContext.getDeviceLayoutContext().
                getFragmentInstancesMap();

        Iterator i = fragmentInstances.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            FragmentInstance fi = (FragmentInstance) (entry.getValue());
            fragmentsList.add(PAPIInternals.createFragment(fi));
        }

        fragments = Collections.unmodifiableList(fragmentsList);

        return fragments;
    }


    /**
     * A method to return a named <code>Fragment</code> object from the current
     * Layout.
     *
     * @param name the name of the <code>Fragment</code> to retrieve
     * @return the specified <code>Fragment</object> or null if it does not
     *         exist.
     */
    public com.volantis.mcs.papi.Fragment getFragment(String name) {
        // NOTE: THIS METHOD RETURNS THE PAPI VERSION OF A FRAGMENT!
        // (I would put this warning in the javadoc but its Public API).

        com.volantis.mcs.papi.Fragment fragment = null;
        Map fragmentInstances = pageContext.
                getDeviceLayoutContext().getFragmentInstancesMap();

        if (fragmentInstances == null) {
            return null;
        }

        FragmentInstance fi = (FragmentInstance) (fragmentInstances.get(name));

        if (fi != null) {
            fragment = PAPIInternals.createFragment(fi);
        }

        return fragment;
    }

    /**
     * Get the dissecting pane given the name of the pane.
     *
     * @param name the name of the pane.
     * @return the dissecting pane given the name of the pane.
     */
    public com.volantis.mcs.papi.DissectingPane getDissectingPane(
            String name) {
        // NOTE: THIS METHOD RETURNS THE PAPI VERSION OF A DISSECTING PANE!
        // (I would put this warning in the javadoc but its Public API).

        // Create a null reference to a PAPI dissecting pane.
        DissectingPane papiPane = null;

        // Grab the pane layout of the name given.
        Pane pane = pageContext.getPane(name);

        // If it is a dissecting pane layout...
        if (pane instanceof com.volantis.mcs.layouts.DissectingPane) {

            // Then grab it's associated dissecting pane layout instance.
            com.volantis.mcs.layouts.DissectingPane dissPane =
                    (com.volantis.mcs.layouts.DissectingPane) pane;
            DissectingPaneInstance dissectingInstance = (DissectingPaneInstance)
                    pageContext.getDeviceLayoutContext().getFormatInstance(
                            dissPane, NDimensionalIndex.ZERO_DIMENSIONS);

            // Check the context for an existing instance of the PAPI version
            // of the dissecting pane.
            papiPane = dissectingInstance.getDissectingPane();

            // If it wasn't already there,
            if (papiPane == null) {

                // Create a PAPI version of the pane from the context now.
                papiPane = PAPIInternals.createDissectingPane(dissectingInstance);

                // And save it for later reference.
                dissectingInstance.setDissectingPane(papiPane);
            }
            // Now we have an instance of the PAPI dissecting pane to return...
        }

        // Return the PAPI dissecting pane we found, if any.
        return papiPane;

    }

    /**
     * A method to return a named <code>FormFragment</code> object from the
     * current Layout.
     *
     * @param name the name of the <code>FormFragment</code> to retrieve
     * @return the specified <code>FormFragment</object> or null if it does not
     *         exist.
     */
    public com.volantis.mcs.papi.FormFragment getFormFragment(String name) {
        // NOTE: THIS METHOD RETURNS THE PAPI VERSION OF A FORM FRAGMENT!
        // (I would put this warning in the javadoc but its Public API).

        com.volantis.mcs.papi.FormFragment fragment = null;
        Map fragmentContexts = pageContext.
                getDeviceLayoutContext().getFormFragmentInstancesMap();

        if (fragmentContexts == null) {
            return null;
        }

        FormFragmentInstance fi = (FormFragmentInstance)
                fragmentContexts.get(name);

        if (fi != null) {
            fragment = PAPIInternals.createFormFragment(fi);
        }

        return fragment;
    }

  /**
   * A method to return the Panes of this layout as a list of PaneFormats
   * @return A list of PaneFormat objects that represent the panes in this
   * layout
   */

  public List getPaneFormats() {
      if(panes!=null)
          return panes;

     panes=new ArrayList();
     Map paneContexts = pageContext.getDeviceLayoutContext()
                            .getPaneInstancesMap();
     if(paneContexts == null) {
         return null;
     }
     Iterator itr = paneContexts.values().iterator();
     while(itr.hasNext()) {
         FormatInstance instance = (FormatInstance)itr.next();
         if(instance!=null) {
             panes.add(PAPIInternals.createPaneFormat(instance));
         }
     }
     return panes;
  }

  /**
   * A method to get the PaneFormat for a given pane name
   * @param name The name of the pane to create a PaneFormat for
   * @return A PaneFormat object for the pane name or null if the pane does
   * not exist.
   */

  public PaneFormat getPaneFormat(String name) {
    PaneFormat paneFormat = null;
    Map paneContexts
      = pageContext.getDeviceLayoutContext().getPaneInstancesMap();
    if(paneContexts == null) {
      return null;
    }
    FormatInstance fi = (FormatInstance)(paneContexts.get(name));
    if(fi != null) {
      paneFormat = PAPIInternals.createPaneFormat(fi);
    }
    return paneFormat;
  }

    /**
   * Return the extension that should be added to cached pages in order
   * for a web server to correctly set the mime type.
   * @return The file extension for cached pages as defined in the device
   * policies for the current requesting device.  Typically, HTML devices will
   * have a file extension of "html", WML devices will have a file extension of
   * "wml", and VoiceXML devices will have a file extension of "vxml".
   */
  public String getContentExtension () {
    return pageContext.getDevice().
      getPolicyValue(DevicePolicyConstants.CACHE_FILE_EXT);
  }

  /**
   * Returns the value of a request parameter as a String.
   * @param name The name of the parameter.
   * @see javax.servlet.ServletRequest#getParameter
   */
  public String getParameter (String name) {
    MarinerURL requestURL = pageContext.getPureRequestURL ();
    return requestURL.getParameterValue (name);
  }

  /**
   * Returns a Map containing all the values of the request parameters.
   * @seenotinservlet22spec javax.servlet.ServletRequest#getParameterMap
   */
  public Map getParameterMap () {
    MarinerURL requestURL = pageContext.getPureRequestURL ();
    return requestURL.getParameterMap ();
  }

  /**
   * Returns an Enumeration of all the request parameter names.
   * @see javax.servlet.ServletRequest#getParameterNames
   */
  public Enumeration getParameterNames () {
    MarinerURL requestURL = pageContext.getPureRequestURL ();
    return requestURL.getParameterNames ();
  }

  /**
   * Returns an array of String objects containing all the values for the
   * specified request parameter.
   * @param name The name of the parameter.
   * @see javax.servlet.ServletRequest#getParameterValues
   */
  public String [] getParameterValues (String name) {
    MarinerURL requestURL = pageContext.getPureRequestURL ();
    return requestURL.getParameterValues (name);
  }

  /**
   * Abstract method returns a new requestContext
   * @return new MarinerRequestContext
   * @volantis-api-exclude-from PublicAPI
   */
  abstract public MarinerRequestContext createNestedContext()
      throws IOException,
             RepositoryException,
             MarinerContextException;
    /**
     * Push a new project onto the stack.
     * <p>
     * The new project will become the current one until it is popped off
     * the stack.
     * </p>
     * @param project The project push.
     */
    public void pushProject(Project project) {
        ProjectStack projectStack = pageContext.getProjectStack();
        projectStack.pushProject((RuntimeProject) project);
    }

    /**
     * Pop the current project off the stack.
     * @param expected The project that is expected to be the current. If this
     * is specified and does not match the current one then an exception is
     * thrown. If this is null then no checking is performed.
     * @return The project that was popped off the stack.
     */
    public Project popProject(Project expected) {
        ProjectStack projectStack = pageContext.getProjectStack();
        return projectStack.popProject((RuntimeProject) expected);
    }

    /**
     * Get the current project.
     * <p>
     * The current project is the last one that was pushed onto but has
     * not yet been popped off the stack. The stack always contains at least
     * one project as the default project specified in the configuration is
     * pushed onto the stack.
     * </p>
     * @return The current project;
     */
    public Project getCurrentProject() {
        ProjectStack projectStack = pageContext.getProjectStack();
        return projectStack.getCurrentProject();
    }

    /**
     * Release any resources.
     */
    public void release () {

        if (pageConnection != null) {
            try {
                pageConnection.disconnect();
            } catch (RepositoryException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Could not disconnection connection", e);
                }
            }
        }

        if (pageContext != null) {
            pageContext.popRequestContext();
            pageContext.release();

            // Clear the marinerPageContext reference.
            ContextInternals.setMarinerPageContext(this, null);
        }
        pageContext = null;
        environmentContext = null;
        fragments = null;
    }

    /**
     * Sets the <code>MarinerApplication</code> associated with this request context.
     * @param marinerApplication The <code>MarinerApplication</code> to associate with
     *        this request context.
     */
    void setMarinerApplication(MarinerApplication marinerApplication) {
        this.marinerApplication = marinerApplication;
    }

    /**
     * Gets the <code>MarinerApplication</code> associated with this request context.
     * @return the <code>MarinerApplication</code> associated with this request context.
     */
      public MarinerApplication getMarinerApplication() {
        return marinerApplication;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 24-May-05	7890/4	pduffin	VBM:2005042705 Committing extensive restructuring changes

 28-Apr-05	7922/2	pduffin	VBM:2005042801 Removed User and UserFactory classes

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-May-05	8132/1	philws	VBM:2005050510 Port format instance collection bug fix from 3.3

 09-May-05	8128/1	philws	VBM:2005050510 Ensure that format instances are collected for the entire layout format tree

 11-Apr-05	7376/2	allan	VBM:2005031101 SmartClient bundler - commit for testing

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 30-Sep-04	5705/1	geoff	VBM:2004093002 IllegalArgumentException trying to use dissection (shard link text is null)

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 02-Mar-04	2736/4	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 19-Feb-04	3090/1	ianw	VBM:2004021716 Changed project stack to check enclosing request context first

 03-Feb-04	2838/4	claire	VBM:2004011914 Fixing pop exceptions

 03-Feb-04	2838/2	claire	VBM:2004011914 Handling current and different projects

 03-Feb-04	2832/3	mat	VBM:2004020215 Corrected JAVADOC on cache flush methods

 23-Jan-04  2736/1  steve   VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04  2685/1  steve   VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 15-Jan-04	2626/1	mat	VBM:2004011507 Separate out the refreshDevicePatternCache from refreshDeviceCache

 15-Jan-04	2608/1	mat	VBM:2004011507 Proteus2

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 19-Jun-03	407/1	steve	VBM:2002121215 Flow elements and PCData in regions

 ===========================================================================
*/
