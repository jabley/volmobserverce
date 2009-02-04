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

package com.volantis.mcs.protocols.widgets.renderers;

import java.io.File;
import java.net.URI;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentScriptAssetReference;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.ScriptLibraryManager;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.styling.Styles;

/**
 * Helper class, used by widgets that not extend WidgetDefaultRenderer
 */

public class WidgetHelper {

    /**
     * Runtime project used for retrieving {@ling ScriptAssetReference}.
     */
    private static RuntimeProject project = null;

    /**
     * Location of the project definition file
     */
    private static final String PROJECT_PATH = "/projects/client/mcs-project.xml";

    /**
     * Location of installed version (Base URL to local JS files) TODO: remove
     * when URL-rewriter based solution is implemented
     */
    private static final String LOCAL_JS_FILES_URL = "file:///C:/system/data/volantis";

    /**
     * Load Runtime project.
     * 
     * @param pageContext
     * @throws MarinerContextException
     */
    private static synchronized void loadProject(MarinerPageContext pageContext)
            throws MarinerContextException {
        
        if (project == null) {
            EnvironmentContext ec = pageContext.getEnvironmentContext();
            ProjectManager pm = 
                pageContext.getVolantisBean().getProjectManager();
            
            File projectFile = new File(ec.getRealPath(PROJECT_PATH));
            URI projectFileURI = projectFile.toURI().normalize();
            project = pm.getProject(projectFileURI.toString(), null);
        }
    }

    /**
     * The method should be called by derived classes to indicate that given JS
     * library is required. Library name should start with "/".
     * 
     * @param libraryName
     * @param protocol
     */
    public static void requireLibrary(String libraryName, VolantisProtocol protocol)
            throws ProtocolException {
        try {
            loadProject(protocol.getMarinerPageContext());
            ScriptAssetReference scriptAssetReference = loadAssetReference(libraryName, protocol
                    .getMarinerPageContext());
            
            ScriptLibraryManager libraryManager =
                protocol.getMarinerPageContext().getScriptLibraryManager();
            libraryManager.addScript(scriptAssetReference);
        } catch (MarinerContextException e) {
            throw new ProtocolException(e);
        }
    }


    /**
     * Returns reference to {@link ScriptAssetReference} for given assetName
     * 
     * @param assetName
     * @parma pageContext
     */
    private static ScriptAssetReference loadAssetReference(String assetName,
           MarinerPageContext pageContext) throws MarinerContextException {
        loadProject(pageContext);
        final PolicyReferenceFactory factory = pageContext.getPolicyReferenceFactory();
        
        
        final RuntimePolicyReference reference = factory.createNormalizedReference(
                project, assetName, PolicyType.SCRIPT);
        return new DefaultComponentScriptAssetReference(
                reference, pageContext.getAssetResolver());
   
    }
   
    /**
     * Returns reference to {@link RuntimePolicyReference} for given assetName
     * 
     * @param assetName
     * @parma pageContext
     */
    public static RuntimePolicyReference loadImageReference(String assetName,
           MarinerPageContext pageContext) throws MarinerContextException {
        loadProject(pageContext);

        final PolicyReferenceFactory factory = pageContext.getPolicyReferenceFactory();
        
        return factory.createNormalizedReference(
                project, assetName, PolicyType.IMAGE);
    }
   
    /**
     * Create wrapper class for ScriptAssetReference, wrapper is responsible for
     * forwarding method calls except getURL which returns path useful for
     * scripts stored device. 
     * 
     * @param reference
     * @param packagerPath path to script when script is on device
     * @param pageContext
     * @return
     */
    private static ScriptAssetReference createWrapper(
                final DefaultComponentScriptAssetReference reference,
                final String packagerPath,
                final MarinerPageContext pageContext){
        
        return new DefaultComponentScriptAssetReference(
                reference.getPolicyReference(),
                pageContext.getAssetResolver()) {
            
            // javadoc inherited
            public RuntimePolicyReference getPolicyReference() {
                return reference.getPolicyReference();
            }

            // javadoc inherited
            public TextAssetReference retrieveTextFallback() {
                return reference.retrieveTextFallback();
            }

            // javadoc inherited
            public String getScript() {
                return reference.getScript();
            }

            // javadoc inherited
            public ScriptAsset getScriptAsset() {
                return reference.getScriptAsset();
            }

            // javadoc inherited
            public String getURL() {
                return packagerPath;
            }

            // javadoc inherited
            public boolean isPolicyReference() {
                return reference.isPolicyReference();
            }

            // javadoc inherited
            public boolean isURL() {
                return reference.isURL();
            }
            
        };
    }
    
    /**
     * Creates and returns styles extractor instance,
     * ready to extract values from specified styles.
     *  
     * @param protocol The protocol.
     * @param styles The styles to extract values from.
     * @return The styles extractor instance.
     */
    public static StylesExtractor createStylesExtractor(VolantisProtocol protocol, Styles styles) {
        StylesExtractor stylesExtractor = new StylesExtractor();
        
        stylesExtractor.setProtocol(protocol);
        
        stylesExtractor.setStyles(styles);
        
        return stylesExtractor;
    }
 
}
