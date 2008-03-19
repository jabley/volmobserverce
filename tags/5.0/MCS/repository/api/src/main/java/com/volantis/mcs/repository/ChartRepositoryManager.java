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
 * $Header: /src/voyager/com/volantis/mcs/repository/ChartRepositoryManager.java,v 1.12 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001.
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Sep-01    Allan           VBM:2001083118 - Add refreshAssetCache()
 *                              and refreshComponentCache(). Tidied indentation
 *                              and added this change history. Fixed cvs 
 *                              header.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Improved consistency of naming
 *                              of parameters.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - removed retrieveBest methods
 *                              and modified to use AbstractCachingAccessor 
 * 22-Mar-02    Adrian          VBM:2002031503 - removed import of old accessor
 * 08-May-02    Paul            VBM:2002050305 - Use an identity factory to
 *                              create the identity objects.
 * 01-Aug-02    Phil W-S        VBM:2002080113 - Renamed any retrieveObjects,
 *                              retrieveObjectsImpl, renameObjects,
 *                              renameObjectsImpl and removeObjects methods,
 *                              replacing "Objects" with "Children".
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the 
 *                              synergetics package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.common.AssetAccessor;
import com.volantis.mcs.accessors.common.ComponentAccessor;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.ChartAssetIdentity;
import com.volantis.mcs.components.ChartComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * This class provides the external interface to the management of
 * chart components and assets within the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class ChartRepositoryManager extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.CHART;

    /**
   * The object that provides access to text components.
   */
  private final ComponentAccessor componentAccessor;

  /**
   * The object that provides access to text assets.
   */
  private final AssetAccessor assetAccessor;

    /**
   * Set the component cache for this manager.
   */
  public void setComponentCache(GenericCache cache) {
    // Unused.
  }

  /**
   * Set the asset cache for this manager.
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void setAssetCache(GenericCache cache) {
      throw new UnsupportedOperationException();
  }
  
  /**
   * Refresh the component cache.
   */
  public void refreshComponentCache() {
    flushCache(POLICY_TYPE);
  }
  
  /**
   * Refresh the asset cache.
   *
   * @deprecated No longer has any effect, use {@link #refreshComponentCache}.
   */
  public void refreshAssetCache() {
    throw new UnsupportedOperationException();
  }

    public ChartRepositoryManager(RepositoryConnection connection) {
        this(connection.getRepository());
    }

    public ChartRepositoryManager(Repository repository) {
        this(repository, null);
    }
  
    public ChartRepositoryManager(Repository repository, Project project) {
        this(repository, project, null);
    }

    /**
     * Initializes a new <code>ChartRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public ChartRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Get the chart accessor.
        componentAccessor = new ComponentAccessor(accessor,
                defaultProject, POLICY_TYPE);
        assetAccessor = new AssetAccessor(accessor, ChartAsset.class,
                defaultProject);
    }


  /**
      Add the specified chart component to the repository.
  */
  public void addChartComponent(RepositoryConnection connection,
				ChartComponent chartComponent)
    throws RepositoryException {
    
    componentAccessor.addObject(getConnection(connection),chartComponent);
  }
  
  /**
     Remove the specified chart component from the repository.
  */
  public void removeChartComponent(RepositoryConnection connection,
				   String name)
    throws RepositoryException {

    componentAccessor.removeObject(getConnection(connection), name);
  }
  
  /**
     Retrieve the specified chart component from the repository.
  */
  public ChartComponent retrieveChartComponent(RepositoryConnection conn, 
					       String name) 
    throws RepositoryException {

    return (ChartComponent) componentAccessor.retrieveObject(conn, name);
  }
  
  public void renameChartComponent (RepositoryConnection connection,
				    String name, String newName)
    throws RepositoryException {

    componentAccessor.renameObject (getConnection(connection), name, newName);
  }

    /**
     * @deprecated No longer supported, throws exception.
     */
  public void copyChartComponent (RepositoryConnection connection,
				  String name, String newName)
    throws RepositoryException {

        throw new UnsupportedOperationException();
  }

  /**
   * Retrieve all the ChartComponents in the repository.
   * @param conn RepositoryConnection to connect to.
   * @return Vector containing all the ChartComponents available via the
   * given RepositoryConnection.
   */
  public Vector retrieveAllChartComponents(RepositoryConnection conn) 
    throws RepositoryException {
    return new Vector (componentAccessor.retrieveAllComponents(conn)); // TBD OBJECTS
  }
  
  public RepositoryEnumeration enumerateChartComponentNames(RepositoryConnection connection)
    throws RepositoryException {
      return componentAccessor.enumerateComponentNames(getConnection(connection));
  }
  
  /**
   * Lock a chart component.
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockChartComponent (RepositoryConnection connection,
				  String name)
    throws RepositoryException {
  }
  
  /**
   * Unlock a chart component.
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockChartComponent (RepositoryConnection connection,
				    String name)
    throws RepositoryException {
  }
  
  /**
   * Add the specified chart asset to the repository.
   */
  public  void addChartAsset(RepositoryConnection connection,
			     ChartAsset chart)
    throws RepositoryException {
    assetAccessor.addObject (getConnection(connection), chart);
  }
  
  /**
     Remove the specified chart asset from the repository.
  */
  public void removeChartAsset(RepositoryConnection connection,
			       String name) 
    throws RepositoryException {
    ChartAssetIdentity identity
      = new ChartAssetIdentity (defaultProject, name);
    assetAccessor.removeObject(getConnection(connection), identity);
  }
  
  /**
   *   Retrieve the specified chart asset from the repository.
   */
  public ChartAsset retrieveChartAsset(RepositoryConnection connection, 
				       String name)
    throws RepositoryException {
    ChartAssetIdentity identity
      = new ChartAssetIdentity (defaultProject, name);
    return (ChartAsset)assetAccessor.retrieveObject(getConnection(connection), identity);
  }
  
  public void renameChartAsset (RepositoryConnection connection,
				String name,
				String newName)
    throws RepositoryException {
    ChartAssetIdentity identity
      = new ChartAssetIdentity (defaultProject, name);    
    assetAccessor.moveAsset (getConnection(connection), identity, newName);
  }

    /**
     * @deprecated No longer supported, throws exception.
     */
  public void copyChartAsset (RepositoryConnection connection,
			      String name,
			      String newName)
    throws RepositoryException {

        throw new UnsupportedOperationException();
  }
  
  public void removeComponentChartAssets (RepositoryConnection connection,
					  String name)
    throws RepositoryException {

    assetAccessor.removeChildren (getConnection(connection), name);
  }
  
  public void renameComponentChartAssets (RepositoryConnection connection,
					  String name, String newName)
    throws RepositoryException {

    assetAccessor.moveAssets (getConnection(connection), name, newName);
  }

    /**
     * @deprecated No longer supported, throws exception.
     */
  public void copyComponentChartAssets (RepositoryConnection connection,
					String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }
  
  /**
   * Retrieve all the chart assets in the repository that belong to
   * a specified chart component; put them into a Vector and return the 
   * Vector.
   *
   * @param conn RepositoryConnection specifically a JDBCRepositoryConnectionImpl
   * @param name String name of the ChartComponent whose
   * ChartAssets we want.
   * @return a Vector containing all the ChartAsset objects 
   * available from the Repository for the named ChartComponent
   */
  public Vector retrieveComponentChartAssets(RepositoryConnection conn,
					     String name) 
    throws RepositoryException {

    return new Vector (assetAccessor.retrieveChildren(conn, name));
  }
  
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
