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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEMode;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;
import java.util.List;

/**
 * XHTML V2 Html element object.
 */
public class HtmlElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(HtmlElement.class);

    /**
     * used to record if the mode is XDIME1/2
     */
    private XDIMEMode mode;

    /**
     * the runtime device layout being used for the page
     */
    private String layoutName;

    /**
     * title of the page
     */
    private String title;

    /**
     * The collection of theme style sheets associated with this page.
     */
    private CompiledStyleSheetCollection themeStyleSheets =
            new CompiledStyleSheetCollection();

    //constructor
    public HtmlElement(XDIMEContextInternal context) {
        super(XHTML2Elements.HTML, UnstyledStrategy.STRATEGY, context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // Decide on the XDIME mode we are in.
        // If the canvas has already been initialised,
        if (getPageContext(context).initialisedCanvas()) {
            // we must have a containing canvas element so this is XDIME CP.
            if (logger.isDebugEnabled()) {
                logger.debug("Canvas already initialised - XDIME CP page");
            }
            mode = XDIMEMode.XDIMECP;
        } else {
            // we have no containing canvas element so this is XDIME 2.
            if (logger.isDebugEnabled()) {
                logger.debug("Canvas not initialised - XDIME 2 page");
            }
            mode = XDIMEMode.XDIME2;

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
        }
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
    protected void callCloseOnProtocol(XDIMEContextInternal context) {
        //NO-OP
    }

    //javadoc unnecessary
    public XDIMEMode getXDIMEMode() {
        return mode;
    }

    /**
     * set the name of the runtime device layout being used.
     *
     * The mode must be XDIME2 if this function is called
     * @param layout
     */
    public void setLayoutName(String layout) {
        ensureXDIME2();
        this.layoutName = layout;
    }

    /**
     * get the name of the runtime device layout being used.
     *
     * The mode must be XDIME2 if this function is called
     * @return layout
     */
    public String getLayoutName() {
        ensureXDIME2();
        return layoutName;
    }

    /**
     * Add the provided compiled style sheet obtained from the <link> element
     * to the collection of theme style sheets.
     * <p>
     * The mode must be XDIME2 if this function is called.
     *
     * @param compiledStyleSheet
     */
    public void addLinkStyleSheet(CompiledStyleSheet compiledStyleSheet) {
        ensureXDIME2();
        themeStyleSheets.addStyleSheet(compiledStyleSheet);
    }

    /**
     * Add the provided compiled style sheet obtained from the <style> element
     * to the collection of theme style sheets.
     * <p>
     * The mode can be either XDIME2 or XDIMECP if this function is called.
     *
     * @param compiledStyleSheet
     */
    public void addInlineStyleSheet(CompiledStyleSheet compiledStyleSheet) {
        themeStyleSheets.addStyleSheet(compiledStyleSheet);
    }

    /**
     * test to ensure the mode is XDIME2
     */
    private void ensureXDIME2() {
        if (mode != XDIMEMode.XDIME2) {
            throw new IllegalStateException("Cannot access canvas data for " +
                    "non XDIME 2 page");
        }
    }

    //javadoc unnecessary
    public void setTitle(String title) {
        this.title = title;
    }

    //javadoc unnecessary
    public String getTitle() {
        return title;
    }

    //javadoc unnecessary
    public CompiledStyleSheetCollection getThemeStyleSheets() {
        return themeStyleSheets;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05      9673/5  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05      9673/3  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 22-Sep-05      9128/5  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05      9128/3  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05      9128/1  pabbott VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
