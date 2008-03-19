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
 * $Header: /src/voyager/com/volantis/mcs/repository/LayoutRepositoryManager.java,v 1.25 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Modified methods for locking
 *                              layouts.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 20-Aug-01    Mat             VBM:2001060401 - Added
 *                              writeDeviceLayoutAsDynamoPortalXML()
 * 05-Sep-01    Paul            VBM:2001083105 - Sorted out comments.
 * 10-Sep-01    Allan           VBM:2001083118 - Add refreshLayoutCache().
 * 15-Oct-01    Paul            VBM:2001101202 - Updated to use the renamed
 *                              methods in LayoutRepositoryManager.
 * 16-Oct-01    Paul            VBM:2001082807 - Removed requester parameter
 *                              from the lock... method.
 * 17-Oct-01    Paul            VBM:2001101701 - Changed return type for
 *                              removeDeviceLayout(Impl) from boolean to void
 *                              to match other accessors/managers and removed
 *                              the unused enumerateDeviceLayoutNames method.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package and
 *                              Layout has been renamed DeviceLayout.
 * 05-Nov-01    Paul            VBM:2001092607 - Made more consistent with
 *                              other accessors by renaming layoutName to name.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Changed to use underlying 
 *                              connection as the IMD repository does not
 *                              support layouts.  If IMD repository is not
 *                              in use, underlying connection wil lbe the 
 *                              original page connection.
 * 05-Apr-02    Mat             VBM:2002022009 - Removed references to 
 *                              getUnderLyingConnection as thus is now 
 *                              handled in the accessor.
 * 08-May-02    Paul            VBM:2002050305 - Use an identity factory to
 *                              create the identity objects.
 * 27-May-02    Paul            VBM:2002050301 - Modified to use
 *                              RepositoryObjectAccessors, some methods which
 *                              cannot easily be implemented using them still
 *                              use the old accessor. These methods are only
 *                              used by the user interface.
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the 
 *                              synergetics package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.AccessorHelper;
import com.volantis.mcs.accessors.CollectionRepositoryEnumeration;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.TargetedSelection;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The LayoutRepositoryManager class is the external interface to the
 * management of layouts within the repository. It implements the
 * LayoutRepositoryAccessor interface by invoking methods on the
 * LayoutRepositoryAccessor class that it encapsulates.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class LayoutRepositoryManager
        extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.LAYOUT;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param connection a <code>RepositoryConnection</code> value
     */
    public LayoutRepositoryManager(RepositoryConnection connection) {
        this(connection.getRepository());
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param repository a <code>Repository</code> value
     */
    public LayoutRepositoryManager(Repository repository) {
        this(repository, null, null);
    }

    /**
     * Initializes a new <code>LayoutRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public LayoutRepositoryManager(
            Repository repository, Project project,
            DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);

        // Don't activate.
    }

    /**
     * Set the device specific layout cache for this accessor.
     *
     * @param cache the cache for this accessor
     * @volantis-api-exclude-from PublicAPI
     */
    public void setLayoutCache(GenericCache cache) {
        // Unused
    }

    /**
     * Refresh the device specific layout cache.
     */
    public void refreshLayoutCache() {
        flushCache(POLICY_TYPE);
    }

    /**
     * Enumerate all layouts by name.
     *
     * @param connection        The connection to use to access the repository.
     * @return An enumeration of layouts. The enumeration's next method returns
     *  a String.
     * @throws RepositoryException If there was a problem accessing the
     *  repository.
     */
    public RepositoryEnumeration enumerateLayoutNames(
            RepositoryConnection connection) throws RepositoryException {

        return accessor.enumeratePolicyBuilderNames(
                getConnection(connection), defaultProject, POLICY_TYPE);
    }

    /**
     * Enumerate all devices for which the specified layout has been created.
     *
     * @param connection    The connection to use to access the repository.
     * @param name          The name of the layout for which to retrieve
     *                      device names.
     * @return An enumeration of devices. The enumeration's next method returns
     *  a String.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    public RepositoryEnumeration enumerateLayoutsDevices(
            RepositoryConnection connection, String name)
            throws RepositoryException {

        if (name == null) {
            throw new IllegalArgumentException("Layout name cannot be null" +
                    "when requesting devices with a layout of that name ");
        }

        RepositoryEnumeration devices = null;

        // The parent repository object passed to getPolicyNames
        // must be null.
        VariablePolicyBuilder policy = (VariablePolicyBuilder)
                accessor.retrievePolicyBuilder(getConnection(connection),
                        defaultProject, name);
        if (policy == null) {
            devices = AccessorHelper.getEmptyEnumeration();
        } else {
            List variants = policy.getVariantBuilders();
            List deviceNames = new ArrayList();
            for (Iterator i = variants.iterator(); i.hasNext();) {
                Variant variant = (Variant) i.next();
                Selection selection = variant.getSelection();
                if (selection instanceof TargetedSelection) {
                    TargetedSelection targeted = (TargetedSelection) selection;
                    List deviceReferences = targeted.getDeviceReferences();
                    for (Iterator j = deviceReferences.iterator();
                         j.hasNext();) {
                        DeviceReference reference = (DeviceReference) j.next();
                        deviceNames.add(reference.getDeviceName());
                    }
                }
            }
            devices = new CollectionRepositoryEnumeration(deviceNames);
        }

        return devices;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 18-Apr-05	7715/1	philws	VBM:2005040402 Port Public API generation changes from 3.3

 15-Apr-05	7676/1	philws	VBM:2005040402 Public API corrections and IBM Public API documentation subset generation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Oct-04	5794/1	geoff	VBM:2004100801 MCS Import slow

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/
