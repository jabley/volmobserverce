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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The perspective for MCS.
 */
public class MCSPerspective implements IPerspectiveFactory {
    private static final String TOP_LEFT_FOLDER = "topLeftFolder";
    private static final String BOTTOM_FOLDER = "bottomFolder";
    private static final String ID_PACKAGE_EXPLORER =
            "org.eclipse.jdt.ui.PackageExplorer";

    private static final String ID_DEVICE_BROWSER =
            "com.volantis.mcs.eclipse.ab.views.devices.DeviceRepositoryBrowser";

    private static final String ID_FORMAT_ATTRIBUTES =
            "com.volantis.mcs.eclipse.ab.views.layout.FormatAttributesView";
    
    private static final String MCS_NAVIGATOR =
            "com.volantis.mcs.eclipse.builder.views.navigator.MCSResourceNavigator";

    public void createInitialLayout(IPageLayout pageLayout) {
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewAssetGroupWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewAudioComponentWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewChartComponentWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewDynamicVisualComponentWizard");
        pageLayout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewImageComponentWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewLayoutWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewLinkComponentWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.builder.wizards.projects.NewMCSProject");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.builder.wizards.policies.NewRolloverImagePolicyWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewScriptComponentWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewTextComponentWizard");
        pageLayout.addNewWizardShortcut("com.volantis.mcs.eclipse.ab.wizards.NewThemeWizard");

        IFolderLayout folderLayout = pageLayout.createFolder(TOP_LEFT_FOLDER,
                IPageLayout.LEFT, 0.23f,
                IPageLayout.ID_EDITOR_AREA);

        folderLayout.addView(MCS_NAVIGATOR);

        pageLayout.addView(IPageLayout.ID_OUTLINE, IPageLayout.BOTTOM,
                0.42f, TOP_LEFT_FOLDER);

        folderLayout = pageLayout.createFolder(BOTTOM_FOLDER,
                IPageLayout.BOTTOM, 0.70f,
                IPageLayout.ID_EDITOR_AREA);

        folderLayout.addView(IPageLayout.ID_PROBLEM_VIEW);
        folderLayout.addPlaceholder(ID_DEVICE_BROWSER);

        pageLayout.addPlaceholder(ID_FORMAT_ATTRIBUTES, IPageLayout.BOTTOM,
                0.52f, IPageLayout.ID_OUTLINE);

        pageLayout.addShowViewShortcut(ID_DEVICE_BROWSER);
        pageLayout.addShowViewShortcut(ID_FORMAT_ATTRIBUTES);
        pageLayout.addShowViewShortcut(MCS_NAVIGATOR);
        pageLayout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        pageLayout.addShowViewShortcut(ID_PACKAGE_EXPLORER);
        pageLayout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);

        pageLayout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10423/5	adrianj	VBM:2005110817 Remove Resource Asset Template from GUI

 23-Nov-05	10423/1	adrianj	VBM:2005110817 Removed Resource Asset Template wizard

 23-Nov-05	10417/1	adrianj	VBM:2005110817 Remove Resource Asset Templates from GUI

 23-Nov-05	10417/1	adrianj	VBM:2005110817 Remove Resource Asset Templates from GUI

 08-Feb-05	6910/1	allan	VBM:2005020702 New Resource Asset Template wizard

 01-Dec-04	6348/3	allan	VBM:2004113003 Add an MCS perspective

 ===========================================================================
*/
