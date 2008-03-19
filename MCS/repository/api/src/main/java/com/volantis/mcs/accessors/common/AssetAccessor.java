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
package com.volantis.mcs.accessors.common;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides access to Asset objects stored in a repository.
 */
public class AssetAccessor extends DependentAccessor {

    private final Class supportedClass;

    /**
     * Create a new <code>XMLAssetAccessor</code>.
     *
     * @param containerAccessor - the parent component container accessor
     * @param supportedClass - the class of the asset
     * @param defaultProject
     */
    public AssetAccessor(
            final PolicyBuilderAccessor containerAccessor,
            final Class supportedClass,
            Project defaultProject) {

        super(containerAccessor, defaultProject);
        this.supportedClass = supportedClass;
    }

    // Javadoc inherited from super class.
    private Class getSupportedClass() {
        return supportedClass;
    }

    // Javadoc inherited from super class.
    public void addObject(
            final RepositoryConnection connection,
            final RepositoryObject object)
            throws RepositoryException {

        checkConnection(connection);

        Project project = getProject(object);

        final VariablePolicyBuilder policyBuilder =
                getVariablePolicyBuilder(connection, project,
                        object.getName(), true);

        policyBuilder.addVariantBuilder(old2New.asset2VariantBuilder(object));

        policyAccessor.updatePolicyBuilder(connection, project,
                policyBuilder);
    }

    // Javadoc inherited from super class.
    public void removeObject(
            final RepositoryConnection connection,
            final RepositoryObjectIdentity identity)
            throws RepositoryException {

        checkConnection(connection);

        Project project = getProject(identity);
        final VariablePolicyBuilder policyBuilder =
                getVariablePolicyBuilder(connection, project,
                        identity.getName(), true);

        if (removeAsset(policyBuilder, identity) != null) {
            policyAccessor.updatePolicyBuilder(connection, project,
                    policyBuilder);
        }
    }

    // Javadoc inherited from super class.
    public RepositoryObject retrieveObject(
            final RepositoryConnection connection,
            final RepositoryObjectIdentity identity)
            throws RepositoryException {

        checkConnection(connection);

        Project project = getProject(identity);
        final VariablePolicyBuilder policyBuilder =
                getVariablePolicyBuilder(connection, project,
                        identity.getName(), false);

        if (policyBuilder == null) {
            return null;
        }

        List variantBuilders = policyBuilder.getVariantBuilders();
        for (Iterator i = variantBuilders.iterator(); i.hasNext();) {
            VariantBuilder variantBuilder = (VariantBuilder) i.next();

            Asset asset = new2Old.variantBuilder2Asset(variantBuilder,
                    identity.getName(), project);
            if (asset.getIdentity().equals(identity)) {
                return asset;
            }
        }

        return null;
    }

    // Javadoc inherited from super class.
    public void moveAsset(
            RepositoryConnection connection,
            RepositoryObjectIdentity assetIdentity,
            String newName)
            throws RepositoryException {

        checkConnection(connection);

        Project project = getProject(assetIdentity);
        VariablePolicyBuilder oldPolicyBuilder =
                getVariablePolicyBuilder(connection, project,
                        assetIdentity.getName(), false);
        if (oldPolicyBuilder == null) {
            return;
        }

        Asset asset = removeAsset(oldPolicyBuilder, assetIdentity);
        if (asset == null) {
            return;
        }

        VariantBuilder variantBuilder = old2New.asset2VariantBuilder(asset);

        VariablePolicyBuilder newPolicyBuilder = getVariablePolicyBuilder(
                connection, project, newName, true);

        newPolicyBuilder.addVariantBuilder(variantBuilder);

        policyAccessor.updatePolicyBuilder(connection, project,
                newPolicyBuilder);
        policyAccessor.updatePolicyBuilder(connection, project,
                oldPolicyBuilder);
    }

    // Javadoc inherited from super class.
    public void removeChildren(RepositoryConnection connection, String name)
            throws RepositoryException {

        checkConnection(connection);

        Project project = defaultProject;
        final VariablePolicyBuilder policyBuilder =
                getVariablePolicyBuilder(connection, project,
                        name, false);
        if (policyBuilder == null) {
            return;
        }

        policyBuilder.getVariantBuilders().clear();

        policyAccessor.updatePolicyBuilder(connection, project,
                policyBuilder);
    }

    // Javadoc inherited from super class.
    public List retrieveChildren(RepositoryConnection connection, String name)
            throws RepositoryException {

        checkConnection(connection);

        Project project = defaultProject;
        final VariablePolicyBuilder policy = getVariablePolicyBuilder(
                connection, project, name, false);
        if (policy == null) {
            return null;
        }

        return getAssets(policy, supportedClass, false, project);
    }

    // Javadoc inherited from super class.
    public void moveAssets(
            RepositoryConnection connection, String name, String newName)
            throws RepositoryException {

        checkConnection(connection);

        Project project = defaultProject;
        VariablePolicyBuilder oldPolicyBuilder = getVariablePolicyBuilder(
                connection, project, name, false);
        if (oldPolicyBuilder == null) {
            return;
        }

        List assets = getAssets(oldPolicyBuilder, getSupportedClass(), true,
                project);

        VariablePolicyBuilder newPolicyBuilder = getVariablePolicyBuilder(
                connection, project, newName, true);
        for (Iterator i = assets.iterator(); i.hasNext();) {
            Asset asset = (Asset) i.next();

            VariantBuilder variantBuilder = old2New.asset2VariantBuilder(asset);
            newPolicyBuilder.addVariantBuilder(variantBuilder);
        }

        policyAccessor.updatePolicyBuilder(connection, project,
                newPolicyBuilder);
        policyAccessor.updatePolicyBuilder(connection, project,
                oldPolicyBuilder);
    }

    private Asset removeAsset(
            final VariablePolicyBuilder policyBuilder,
            final RepositoryObjectIdentity identity) {

        Project project = getProject(identity);
        List variantBuilders = policyBuilder.getVariantBuilders();
        for (Iterator i = variantBuilders.iterator(); i.hasNext();) {
            VariantBuilder variantBuilder = (VariantBuilder) i.next();

            Asset asset = new2Old.variantBuilder2Asset(variantBuilder,
                    identity.getName(), project);
            if (asset.getIdentity().equals(identity)) {
                i.remove();
                return asset;
            }
        }

        return null;
    }

    /**
     * Returns the sublists of assets that have the specified class. Never
     * returns <code>null</code> but may return empty list.
     *
     * @param supportedClass - the class of assets to look for
     * @param remove
     * @param project
     * @return the (possibly empty) list of assets with the specified class
     */
    private List getAssets(
            VariablePolicyBuilder policyBuilder, final Class supportedClass,
            boolean remove, final Project project) {

        List variantBuilders = policyBuilder.getVariantBuilders();
        if (variantBuilders == null || variantBuilders.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        final List result = new LinkedList();
        for (Iterator i = variantBuilders.iterator(); i.hasNext();) {
            VariantBuilder variantBuilder = (VariantBuilder) i.next();

            Asset asset = new2Old.variantBuilder2Asset(variantBuilder,
                    policyBuilder.getName(), project);
            if (asset.getClass() == supportedClass) {
                result.add(asset);
                if (remove) {
                    i.remove();
                }
            }
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 17-Nov-05	9789/7	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 23-Oct-05	9789/2	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 28-Sep-05	9445/4	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 ===========================================================================
*/
