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
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.components.AbstractComponent;
import com.volantis.mcs.objects.AbstractCacheableRepositoryObject;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.project.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Component container is used to group together a component and its assets.
 *
 * This class is a base class for all the component containers. It stores
 * a container and delegates the caching setters and getters to it. Also there
 * is a list to store the assets.
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public class ComponentContainer
        extends AbstractCacheableRepositoryObject {

    /**
     * The wrapped component.
     */
    private final AbstractComponent component;

    private final PolicyType policyType;

    /**
     * The assests of the component.
     */
    private List assets;

    /**
     * The base class must be created with a valid component.
     *
     * @param component the component to wrap.
     */
    public ComponentContainer(
            final AbstractComponent component,
            PolicyType policyType) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }
        this.component = component;
        this.policyType = policyType;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    /**
     * Returns the component wrapped. Never returns <code>null</code>.
     *
     * @return the component inside the container
     */
    public AbstractComponent getComponent() {
        return component;
    }

    // javadoc inherited
    protected RepositoryObjectIdentity createIdentity() {
        return new PolicyIdentity(component.getProject(), component.getName(),
                getPolicyType());
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @param flag the new value.
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public void setCacheThisPolicy(final boolean flag) {
        component.setCacheThisPolicy(flag);
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @return the value got from the component
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public boolean getCacheThisPolicy() {
        return component.getCacheThisPolicy();
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @param flag the new value.
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public void setRetainDuringRetry(final boolean flag) {
        component.setRetainDuringRetry(flag);
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @return the value got from the component
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public boolean getRetainDuringRetry() {
        return component.getRetainDuringRetry();
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @param flag the new value.
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public void setRetryFailedRetrieval(final boolean flag) {
        component.setRetryFailedRetrieval(flag);
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @return the value got from the component
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public boolean getRetryFailedRetrieval() {
        return component.getRetryFailedRetrieval();
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @param secs the new value.
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public void setTimeToLive(final int secs) {
        component.setTimeToLive(secs);
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @return the value got from the component
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public int getTimeToLive() {
        return component.getTimeToLive();
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @param secs the new value.
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public void setRetryInterval(final int secs) {
        component.setRetryInterval(secs);
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @return the value got from the component
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public int getRetryInterval() {
        return component.getRetryInterval();
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @param count the new count.
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public void setRetryMaxCount(final int count) {
        component.setRetryMaxCount(count);
    }

    /**
     * Delegate to the encapsulated component.
     *
     * @return the value got from the component
     * @see com.volantis.mcs.objects.AbstractCacheableRepositoryObject
     */
    public int getRetryMaxCount() {
        return component.getRetryMaxCount();
    }

    // javadoc inherited
    public String getName() {
        return component.getName();
    }

    // javadoc inherited
    public Project getProject() {
        return component.getProject();
    }

    // javadoc inherited
    public void setName(final String name) {
        if (name == null) {
            throw new IllegalStateException(
                    "Name of a component cannot be null.");
        }
        component.setName(name);
        setAssetValues(assets);
        identityChanged();
    }

    // javadoc inherited
    public void setProject(final Project project) {
        component.setProject(project);
        setAssetValues(assets);
        identityChanged();
    }

    /**
     * Return the list of assets stored with the component.
     * May return <code>null</code>.
     *
     * @return the possibly empty list of assets or <code>null</code>
     */
    public List getAssets() {
        return assets;
    }

    /**
     * Sets the name and project values for the assets in the given list to the
     * ones stored for the component.
     *
     * @param assets list of assets, may be <code>null</code>
     */
    private void setAssetValues(final List assets) {
        if (assets != null) {
            for (Iterator iter = assets.iterator(); iter.hasNext();) {
                final Asset asset = (Asset) iter.next();
                asset.setName(component.getName());
                asset.setProject(component.getProject());
            }
        }
    }

    /**
     * Checks that the name and project values for the given asset are the same
     * as the one stored for the component.
     * If the asset belongs to a different project or has a different name, an
     * IllegalArgumentException is thrown.
     * If the asset does not have name or project value set, it is set from the
     * component.
     *
     * @param asset the asset to check, must not be <code>null</code>
     */
    private void checkNameAndProject(final Asset asset) {
        if (asset.getName() == null) {
            asset.setName(component.getName());
        }
        if (asset.getProject() == null) {
            asset.setProject(component.getProject());
        } else if (!asset.getProject().equals(component.getProject())) {
            throw new IllegalArgumentException("Asset belongs to different project. " +
                    asset.getProject() +
                    " vs. " + component.getProject());
        }
    }

    // javadoc inherited
    public boolean equals(final Object obj) {
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        final ComponentContainer other = (ComponentContainer) obj;
        return component.equals(other.getComponent()) &&
                (assets == null && other.getAssets() == null ||
                assets != null && assets.equals(other.getAssets()));
    }

    // javadoc inherited
    public int hashCode() {
        return component.hashCode() + (assets == null ? 0 : assets.hashCode());
    }

    /**
     * Adds the given asset to the list of assets. If an asset with the same
     * identity already exists in the list a RepositoryException is thrown.
     *
     * The asset must not belong to a different project or have a different name
     * as the component does, otherwise an IllegalArgumentException is thrown.
     *
     * @param asset - the asset to add, must not be <code>null</code>
     * @throws RepositoryException if an asset with the same identity already
     *                             exists in the list of assets
     */
    public void addAsset(final Asset asset) throws RepositoryException {
        if (asset == null) {
            throw new RepositoryException("Cannot add null asset.");
        }
        if (assets == null) {
            assets = new ArrayList();
        }
        final RepositoryObjectIdentity identity = asset.getIdentity();
        for (Iterator iter = assets.iterator(); iter.hasNext();) {
            final Asset other = (Asset) iter.next();
            if (identity.equals(other.getIdentity())) {
                throw new RepositoryException("Asset has already been added." +
                        asset.toString());
            }
        }
        checkNameAndProject(asset);
        assets.add(asset);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	9789/1	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 02-Oct-05	9652/2	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 29-Sep-05	9500/1	ianw	VBM:2005091308 Interim commit for Ian B

 28-Sep-05	9445/7	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 ===========================================================================
*/
