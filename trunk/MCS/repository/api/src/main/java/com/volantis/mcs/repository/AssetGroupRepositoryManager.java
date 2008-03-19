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
 * $Header: /src/voyager/com/volantis/mcs/repository/AssetGroupRepositoryManager.java,v 1.18 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history,
 *                              and also added methods to lock, unlock,
 *                              rename and copy.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 10-Sep-01    Allan           VBM:2001083118 - Add refreshAssetGroupCache().
 *                              Removed comment that simply said "Asset groups"
 * 26-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - removed retrieveBest methods
 *                              and modified to use AbstractCachingAccessor 
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

import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.synergetics.cache.GenericCache;

import java.util.Vector;

/**
 * The AssetGroupRepositoryManager class is the external interface to the
 * management of asset groups within the repository. It implements
 * AssetGroupRepositryAccess by invoking methods on the
 * AssetGroupRepositoryAccessor class that it references.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public class AssetGroupRepositoryManager
  extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.BASE_URL;

    /**
   * Creates a new <code>AssetGroupRepositoryManager</code> instance.
   *
   * @param repository the <code>Repository</code> to which the manager will
   * apply
   */
  public AssetGroupRepositoryManager(Repository repository) {
      super(repository);
    }

  /**
   * Establish the kind of caching to use on this manager. This method is for
   * internal use within the Mariner repository and applications only.
   *
   * @param assetGroupCache the cache to be used with this manager
   */
  public void setAssetGroupCache(GenericCache assetGroupCache) {
      // do nothing, as unused.
  }

  /**
   * Refresh the cache used with this manager.
   */
  public void refreshAssetGroupCache() {
        flushCache(POLICY_TYPE);
  }

    /**
     * Add the specified asset group to the repository.
     *
     * @param connection the <code>RepositoryConnection</code> to the repository
     * @param assetGroup the <code>AssetGroup</code> to be added
     * @throws RepositoryException if an error occurs
     */
    public void addAssetGroup(RepositoryConnection connection,
                              AssetGroup assetGroup)
            throws RepositoryException {

        PolicyBuilder policyBuilder =
                old2New.component2VariablePolicyBuilder(assetGroup);

        accessor.addPolicyBuilder(getConnection(connection),
                defaultProject, policyBuilder);
    }

  /**
   * Remove the specified asset group from the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the name of the asset group to be removed
   * @exception RepositoryException  An exception caused during access to the
   * repository
   */
  public void removeAssetGroup(RepositoryConnection connection,
                                String name)
    throws RepositoryException {

    accessor.removePolicyBuilder(getConnection(connection), defaultProject, name);
  }

  /**
   * Retrieve the specified asset group from the repository.
   *
   * @param connection a <code>RepositoryConnection</code> to the repository
   * @param name the name of the asset group to be retrieved
   * @return the requested <code>AssetGroup</code>
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public AssetGroup retrieveAssetGroup (RepositoryConnection connection,
                                        String name)
    throws RepositoryException {

      RepositoryObject repositoryObject = null;
      PolicyBuilder policyBuilder = accessor.retrievePolicyBuilder(
              getConnection(connection), defaultProject, name);
      if (policyBuilder != null) {
          Policy policy = policyBuilder.getPolicy();
          repositoryObject = new2Old.new2old(policy);
      }

      return (AssetGroup) repositoryObject;
  }

  /**
   * Rename the specified asset group within the repository.
   *
   * @param connection the <code>RepositoryConnection</code> to the repository
   * @param name the existing name of the asset group
   * @param newName the new name of the asset group
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public void renameAssetGroup (RepositoryConnection connection,
                                String name, String newName)
    throws RepositoryException {

    accessor.renamePolicyBuilder(getConnection(connection), defaultProject,
            name, newName);
  }

  /**
   * Copy an asset group within the repository.
   *
   * @param connection a <code>RepositoryConnection</code> to the repository
   * @param name the name of the asset group being copied
   * @param newName the name of the copy of the asset group
   * @exception RepositoryException an exception caused during access to the
   * repository
   *
   * @deprecated No longer supported, throws exception.
   */
  public void copyAssetGroup (RepositoryConnection connection,
                              String name, String newName)
    throws RepositoryException {

      throw new UnsupportedOperationException();
  }

  /**
   * Retrieve all asset groups from the repository.
   *
   * @param connection a <code>RepositoryConnection</code> to the repository
   * @return a <code>Vector</code> containing the asset groups
   * @exception RepositoryException an exception caused during access to the
   * repository
   */
  public Vector retrieveAllAssetGroups(RepositoryConnection connection)
    throws RepositoryException {

      Vector collection = new Vector();
      RepositoryEnumeration enumeration = accessor
              .enumeratePolicyBuilderNames(connection, defaultProject,
                      POLICY_TYPE);
      try {
          while(enumeration.hasNext()) {
              String name = (String) enumeration.next();
              AssetGroup assetGroup = retrieveAssetGroup(connection, name);
              collection.add(assetGroup);
          }
      }
      finally {
          enumeration.close();
      }

      return collection;
  }

    /**
     * Enumerate the names of all asset groups in the repository.
     *
     * @param connection a <code>RepositoryConnection</code> to the repository
     * @return a <code>RepositoryEnumeration</code> of the names from which
     *         individual names can be accessed
     * @throws RepositoryException an exception caused during access to the
     *                             repository
     */
    public RepositoryEnumeration enumerateAssetGroupNames(RepositoryConnection connection)
            throws RepositoryException {
        RepositoryEnumeration enumeration =
                accessor.enumeratePolicyBuilderNames(getConnection(connection),
                        defaultProject, POLICY_TYPE);
        return enumeration;
    }

  /**
   * Lock an asset group to prevent other programs from updating it
   * concurrently.
   *
   * @param connection a <code>RepositoryConnection</code> to the repository
   * @param name the name of the asset group to be locked
   * @exception RepositoryException if an error occurs
   *
   * @deprecated Policies can no longer be locked.
   */
  public void lockAssetGroup (RepositoryConnection connection,
                              String name)
    throws RepositoryException {
  }

  /**
   * Unlock an asset group to allow other programs to update it.
   *
   * @param connection a <code>RepositoryConnection</code> to the repository
   * @param name the name of the asset group to be locked
   * @exception RepositoryException if an error occurs
   *
   * @deprecated Policies can no longer be locked.
   */
  public void unlockAssetGroup (RepositoryConnection connection,
                                String name)
    throws RepositoryException {
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
