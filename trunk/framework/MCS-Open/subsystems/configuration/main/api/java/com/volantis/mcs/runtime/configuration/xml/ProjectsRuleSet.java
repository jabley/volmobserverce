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
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
import com.volantis.mcs.runtime.configuration.project.JdbcPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.configuration.project.XmlPoliciesConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Adds digester rules for the project element and it's sub elements.
 */ 
public class ProjectsRuleSet extends PrefixRuleSet {

    /**
     * Construct an instance of this class, using the prefix provided.
     * 
     * @param prefix the prefix to add the rules to the digester under.
     */ 
    public ProjectsRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited.
    public void addRuleInstances(Digester digester) {
        
        // <projects>
        String pattern = prefix + "/projects";
        digester.addObjectCreate(pattern, ProjectsConfiguration.class);
        digester.addSetNext(pattern, "setProjects");

        // <projects>/<default>
        String defaultPattern = pattern + "/default";
        digester.addObjectCreate(defaultPattern, RuntimeProjectConfiguration.class);
        digester.addSetNext(defaultPattern, "setDefaultProject");
        digester.addSetProperties(defaultPattern,
                new String[] { "preload" },
                new String[] { "preload" }
        );
        addProjectChildRules(digester, defaultPattern + "/");
        
        // <projects>/<project>
        String projectPattern = pattern + "/project";
        digester.addObjectCreate(projectPattern, RuntimeProjectConfiguration.class);
        digester.addSetNext(projectPattern, "addProject");
        digester.addSetProperties(projectPattern,
                new String[] { "name"  },
                new String[] { "name" }
        );
        addProjectChildRules(digester, projectPattern + "/");
        
    }

    /**
     * Add digester rules for the child elements of an individual project tag.
     * 
     * @param digester the digester to add rules to.
     * @param prefix prefix to add the rules under.
     */ 
    private void addProjectChildRules(Digester digester, String prefix) {

        String pattern;

        // <fallback-project>
        pattern = prefix + "fallback-project";
        digester.addSetProperties(pattern,
                new String[] { "name" },
                new String[] { "fallbackProjectName" }
        );

        // <xml-policies>
        pattern = prefix + "xml-policies";
        digester.addObjectCreate(pattern, XmlPoliciesConfiguration.class);
        digester.addSetNext(pattern, "setPolicies");
        digester.addSetProperties(pattern,
                new String[] { "directory" }, 
                new String[] { "directory" }
        );

        // <jdbc-policies>
        pattern = prefix + "jdbc-policies";
        digester.addObjectCreate(pattern, JdbcPoliciesConfiguration.class);
        digester.addSetNext(pattern, "setPolicies");
        digester.addSetProperties(pattern,
                new String[] { "name" }, 
                new String[] { "name" }
        );

        // <assets>
        pattern = prefix + "assets";
        digester.addObjectCreate(pattern, AssetsConfiguration.class);
        digester.addSetNext(pattern, "setAssets");
        digester.addSetProperties(pattern,
                new String[] { "base-url" }, 
                new String[] { "baseUrl" }
        );
        String assetPrefix = pattern + "/";
        addAssetRules(digester, assetPrefix, "audio", "Audio");
        addAssetRules(digester, assetPrefix, "dynamic-visual", "DynamicVisual");
        addAssetRules(digester, assetPrefix, "image", "Image");
        addAssetRules(digester, assetPrefix, "script", "Script");
        addAssetRules(digester, assetPrefix, "text", "Text");
        
        // <generated-resources>
        pattern = prefix + "generated-resources";
        digester.addObjectCreate(pattern, GeneratedResourcesConfiguration.class);
        digester.addSetNext(pattern, "setGeneratedResources");
        digester.addSetProperties(pattern,
                new String[] { "base-dir" },
                new String[] { "baseDir" }
        );
    }

    /**
     * Add digester rules for an individual *-assets tag.
     * 
     * @param digester the digester to add rules to.
     * @param prefix prefix to add the rules under.
     * @param tagId the type of asset, as used in the tag name (i.e. the *).
     * @param methodId the type of asset, as used in the method name.
     */ 
    private void addAssetRules(Digester digester, String prefix, 
            String tagId, String methodId) {
        
        // <*-assets>
        String pattern = prefix + tagId + "-assets";
        digester.addObjectCreate(pattern, AssetConfiguration.class);
        digester.addSetNext(pattern, "set" + methodId + "Assets");
        digester.addSetProperties(pattern,
                new String[] { "prefix-url" }, 
                new String[] { "prefixUrl" }
        );
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 26-Jan-04	2724/3	geoff	VBM:2004011911 Add projects to config (whoops - add javadoc)

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
