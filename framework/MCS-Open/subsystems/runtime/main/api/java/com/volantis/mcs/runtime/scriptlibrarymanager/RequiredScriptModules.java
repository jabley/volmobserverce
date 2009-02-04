/*
This file is part of Volantis Mobility Server.

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server. If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.scriptlibrarymanager;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentScriptAssetReference;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.mcs.protocols.widgets.styles.EffectRule;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.UnregisteredScriptModuleDependencyException;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

/**
 * Provides api for storing requested script modules (widget specyfic) on per request basis 
 * as well as method for obtaining dependencies list related to those requested modules
 *
 * @mock.generate
 */
public class RequiredScriptModules {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(RequiredScriptModules.class);

    private static int MAX_JS_REFERENCES = 5;

    private Set<ScriptModule> requiredModules = new HashSet<ScriptModule>();

    private Set<String> effectStyles = new HashSet<String>();

    private MarinerPageContext pageContext;

    /**
     * Runtime project used for retrieving {@ling ScriptAssetReference}.
     */
    private static RuntimeProject project = null;

    private static File baseAssetsPath;  

    /**
     * Location of the project definition file
     */
    private static final String PROJECT_PATH = "/projects/client/mcs-project.xml";

    private DOMProtocol protocol;

    /**
     * Helper element to insert script elements to the head after
     * writeScriptElements is called.
     */
    private Element markerElement;
   
    /**
     * Creates a requested script modules instance.
     *
     * @param pageContext the asset resolver to be used
     */
    public RequiredScriptModules(MarinerPageContext pageContext) {
        this.pageContext = pageContext;
        this.protocol = (DOMProtocol) pageContext.getProtocol();
    }

    /**
     * Adds this module to the collection of required modules for this page
     * and also extracts style attributes
     *
     * @param module
     * @param attributes
     * @param allEffectsNeeded
     * @throws UnregisteredScriptModuleDependencyException
     */
    public void require(ScriptModule module, MCSAttributes attributes, boolean allEffectsNeeded)
            throws UnregisteredScriptModuleDependencyException {

        if (!ScriptModulesDefinitionRegistry.isRegistered(module)) {
            throw new UnregisteredScriptModuleDependencyException(
                    "Requested module hasn't been registered: " + module);            
        }

        if (allEffectsNeeded) {
            effectStyles.add("all");
        } else if (attributes != null && attributes.getStyles() != null) {
            Styles styles = attributes.getStyles();
            StylesExtractor concealedExtractor = new StylesExtractor(styles, StatefulPseudoClasses.MCS_CONCEALED);
            StylesExtractor extractor = new StylesExtractor(styles, null);
            effectStyles.addAll(extractor.getEffectsList());
            effectStyles.addAll(concealedExtractor.getEffectsList());
        }
        requiredModules.add(module);
    }

    /**
     * Return true if in case there is was any widget on a page
     *  
     */
    public boolean isStartupRequired() {
        return !requiredModules.isEmpty();
    }
    
    public List<ScriptModule> getDependencyList()
            throws UnregisteredScriptModuleDependencyException {

        if (requiredModules.isEmpty()) {
            return Collections.EMPTY_LIST;
        } else {
            return ScriptModulesDefinitionRegistry.getDependencyList(requiredModules, effectStyles);
        }
    }

    /**
     * Creates the marker element to be able to append script list later when
     * the list of wigets on the page is known    
     */
    public void createMarkerElement() {
         markerElement = protocol.getDOMFactory().createElement("DELETE_ME");
         DOMOutputBuffer outputBuffer =
             ((DOMOutputBuffer) protocol.getPageHead().getHead());
         outputBuffer.addElement(markerElement);    
    }
  
    /**
     * Returns set of referencable modules (as opposed to embedded ones)    
     *
     * @param modules - set of modules     
     * @return set of modules that are supposed to be referenced (and not embedded)
     */
    private Set<ScriptModule> getReferencableModules(Set<ScriptModule> modules) {

        List<ScriptModule> list = new ArrayList<ScriptModule>(modules);

        Collections.sort(list, new Comparator<ScriptModule>() {
                public int compare(ScriptModule sm1, ScriptModule sm2) {

                    if (sm1.isCacheable() == sm2.isCacheable()) {
                        int weight_a =
                                sm1.getSize() * ScriptModulesDefinitionRegistry.getReusability(sm1);
                        int weight_b =
                                sm2.getSize() * ScriptModulesDefinitionRegistry.getReusability(sm2);
                        if (weight_a > weight_b) {
                            return 1;
                        } else if (weight_a == weight_b) {
                            return 0;
                        } else {
                            return -1;
                        }

                    } else if (sm1.isCacheable() == true) {
                        return 1;
                    } else {
                        return -1;
                    }

                }
            });

        Collections.reverse(list);

        if (list.size() > MAX_JS_REFERENCES) {
            return new HashSet<ScriptModule>(list.subList(0, MAX_JS_REFERENCES));
        } else {
            return new HashSet<ScriptModule>(list);            
        }
    }

    /**
     * Writes scripts
     */
    public void writeScriptElements() throws ProtocolException {

       try {
            List<ScriptModule> depList = getDependencyList();
            Set<ScriptModule> referencableSet =
                    getReferencableModules(new HashSet<ScriptModule>(depList)); 

            for (ScriptModule sm : depList) {

                loadProject(pageContext);
                ScriptAssetReference scriptAssetReference =
                        loadAssetReference(sm.getAssetName(), pageContext);
                sm.setScriptAssetReference(scriptAssetReference);

                // collect the attributes
                final ScriptAttributes attributes =
                    assetToAttributes(sm.getScriptAssetReference());

                if (!referencableSet.contains(sm)) {
                    attributes.setEmbeddable(true);
                    attributes.setScriptFilePath(
                            new File(baseAssetsPath, attributes.getScriptReference().getURL()));
                }

                // write out the script element
                final Element scriptElement =
                    protocol.createScriptElement(attributes);
                scriptElement.insertBefore(markerElement);

            }

        } catch (UnregisteredScriptModuleDependencyException e) {
            throw new RuntimeException(e);
        } catch (MarinerContextException e) {
            throw new RuntimeException(e);
        }

        markerElement.remove();

    }

    /**
     * Load Runtime project.
     *
     * @param pageContext
     * @throws com.volantis.mcs.context.MarinerContextException
     */
    private static synchronized void loadProject(MarinerPageContext pageContext)
            throws MarinerContextException {

        if (project == null) {
            EnvironmentContext ec = pageContext.getEnvironmentContext();
            ProjectManager pm =
                pageContext.getVolantisBean().getProjectManager();

            File projectFile = new File(ec.getRealPath(PROJECT_PATH));
            baseAssetsPath = new File(ec.getRealPath("/")).getParentFile();
            URI projectFileURI = projectFile.toURI().normalize();
            project = pm.getProject(projectFileURI.toString(), null);
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
     * Stores the attributes from a script variant in a ScriptAttributes object
     * @param scriptReference
     * @return the created ScriptAttributes object
     */
    private static ScriptAttributes assetToAttributes(
            final ScriptAssetReference scriptReference) throws ProtocolException {
        final ScriptAsset scriptAsset = scriptReference.getScriptAsset();
        if (scriptAsset == null) {
           throw new ProtocolException(exceptionLocalizer.format(
                    "widget-missing-script-policy-variant"));
        }  
        final ScriptAttributes attributes = new ScriptAttributes();
        attributes.setCharSet(scriptAsset.getCharacterSet());
        attributes.setLanguage(scriptAsset.getProgrammingLanguage());
        attributes.setType(scriptAsset.getMimeType());
        attributes.setScriptReference(scriptReference);
        return attributes;
    }




}
