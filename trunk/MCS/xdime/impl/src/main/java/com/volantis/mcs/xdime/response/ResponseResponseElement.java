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

package com.volantis.mcs.xdime.response;

import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.response.attributes.ResponseResponseAttributes;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Implementation of response element from widgets-response namespace.
 * 
 * Elements from this namespace are used for building responses to
 * AJAX requests in a device-independent way.
 */
public class ResponseResponseElement extends ResponseElement {
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ResponseResponseElement.class);

    /**
     * the runtime device layout being used for the page
     */
    private String layoutName;

    /**
     * The collection of theme style sheets associated with this page.
     */
    private CompiledStyleSheetCollection themeStyleSheets =
            new CompiledStyleSheetCollection();
    
    public ResponseResponseElement(XDIMEContextInternal context) {
        super(ResponseElements.RESPONSE, context);
        protocolAttributes = new ResponseResponseAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // In XDIME 2 mode we are responsible for setting up the canvas.
        // As the first stage of this we deal with any per-project default
        // themes and layouts. These need to be set up before we get any
        // per-page versions.

        // Get the current project.
        MarinerPageContext pageContext = getPageContext(context);
        final RuntimeProject runtimeProject = (RuntimeProject)
                pageContext.getCurrentProject();

        if (runtimeProject != null) {
            // Add any project specified style sheets into the list of
            // theme style sheets. These must be added before any style
            // sheets that are specified in the head.
            addProjectStyleSheets(context, runtimeProject);

            // Extract any default layout from the project.
            // This will be overridden with an any explicit layout
            // provided in the head.
            layoutName = runtimeProject.getDefaultProjectLayoutLocation();
            if (logger.isDebugEnabled()) {
                logger.debug("Project layout: " + layoutName);
            }
        }
        // else, presumably this is only for test...?
        // todo: later: avoid test specific code paths.
        return XDIMEResult.PROCESS_ELEMENT_BODY;        
        
    }
    
    /**
     * Add any project specified style sheets into the list of theme style
     * sheets.
     *
     * @param context
     * @param runtimeProject
     */
    private void addProjectStyleSheets(XDIMEContextInternal context,
            RuntimeProject runtimeProject) {

        MarinerPageContext pageContext = getPageContext(context);

        // Find any theme style sheets which are specified in the project,
        // compile them and add them to the collection of compiled theme style
        // sheets.
        List projectThemeLocations =
                runtimeProject.getProjectThemesLocations();
        if (projectThemeLocations != null) {
            for (Iterator themeLocations = projectThemeLocations.iterator();
                 themeLocations.hasNext();) {
                String projectThemeLocation = (String) themeLocations.next();
                if (projectThemeLocation != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Project theme: " + projectThemeLocation);
                    }
                    CompiledStyleSheet projectStyleSheet =
                            pageContext.retrieveThemeStyleSheet(
                                    projectThemeLocation);
                    if (projectStyleSheet != null) {
                        themeStyleSheets.addStyleSheet(projectStyleSheet);
                    }
                }
            }
        }
    }    

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) {
        //NO-OP
    }
    
    //javadoc unnecessary
    public CompiledStyleSheetCollection getThemeStyleSheets() {
        return themeStyleSheets;
    }
    
    /**
     * Add the provided compiled style sheet obtained from the <link> element
     * to the collection of theme style sheets.
     * The mode must be XDIME2 if this function is called.
     *
     * @param compiledStyleSheet
     */
    public void addLinkStyleSheet(CompiledStyleSheet compiledStyleSheet) {
        themeStyleSheets.addStyleSheet(compiledStyleSheet);
    }
    
    /**
     * set the name of the runtime device layout being used.
     * @param layout
     */
    public void setLayoutName(String layout) {
        this.layoutName = layout;
    }
    
    public String getLayoutName(){
        return this.layoutName;
    }
}
