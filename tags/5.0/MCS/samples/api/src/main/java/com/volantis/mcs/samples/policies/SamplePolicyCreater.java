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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.samples.policies;

import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.project.PolicyManager;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.ProjectConfiguration;
import com.volantis.mcs.project.ProjectFactory;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;

import java.io.File;

/**
 * Sample code for creating policies.
 */
public class SamplePolicyCreater {

    public static void main(String[] args) throws Exception {

        File file = new File(args[0]);
        String path = file.getAbsolutePath();

        // Create the project.
        ProjectFactory projectFactory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration config
                = projectFactory.createProjectConfiguration();
        config.setPolicyLocation(path);
        XMLRepositoryFactory xmlRepositoryFactory =
                XMLRepositoryFactory.getDefaultInstance();
        LocalRepository localRepository =
                xmlRepositoryFactory.createXMLRepository(
                xmlRepositoryFactory.createXMLRepositoryConfiguration());
        config.setRepository(localRepository);
        Project project = projectFactory.createProject(config);

        // Get the policy manager for the project.
        PolicyManager policyManager = project.createPolicyManager();

        // Build the policy.
        PolicyFactory factory = PolicyFactory.getDefaultInstance();

        // Create the policy builder.
        VariablePolicyBuilder builder = factory.createVariablePolicyBuilder(
                VariablePolicyType.IMAGE);
        builder.setName("/policy.mimg");

        // Create the variant builder and add it to the policy builder.
        VariantBuilder variantBuilder = factory.createVariantBuilder(
                VariantType.IMAGE);
        builder.addVariantBuilder(variantBuilder);

        // Create a targeted selection to target the variant at a category and
        // a device.
        TargetedSelectionBuilder selectionBuilder =
                factory.createTargetedSelectionBuilder();
        selectionBuilder.addCategory("custom.category");
        selectionBuilder.addDevice("PC");
        variantBuilder.setSelectionBuilder(selectionBuilder);

        // Create meta data to describe the variant.
        ImageMetaDataBuilder metaDataBuilder =
                factory.createImageMetaDataBuilder();
        metaDataBuilder.setConversionMode(ImageConversionMode.NEVER_CONVERT);
        metaDataBuilder.setWidth(100);
        metaDataBuilder.setHeight(200);
        metaDataBuilder.setImageEncoding(ImageEncoding.GIF);
        metaDataBuilder.setPixelDepth(8);
        metaDataBuilder.setRendering(ImageRendering.COLOR);
        variantBuilder.setMetaDataBuilder(metaDataBuilder);

        // Create content to describe the location of the resource.
        URLContentBuilder contentBuilder = factory.createURLContentBuilder();
        contentBuilder.setURL("/images/abc.gif");
        variantBuilder.setContentBuilder(contentBuilder);

        // Add the policy to the repository.
        VariablePolicy policy = builder.getVariablePolicy();
        policyManager.addPolicy(policy);
    }
}
