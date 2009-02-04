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
package com.volantis.mcs.eclipse.ab.wizards;

import com.volantis.mcs.eclipse.common.ProjectProvider;
import org.eclipse.core.resources.IProject;

/**
 * Define an Abstract Factory for creating PolicyWizard objects.
 */
public interface PolicyWizardPageFactory {
    /**
     * Create a PolicyWizardPage.
     * @param project A Project for the wizard page. Can be null.
     */
    public PolicyWizardPage
            createPolicyWizardPage(IProject project);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 ===========================================================================
*/
