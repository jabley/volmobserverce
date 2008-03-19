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
package com.volantis.mcs.eclipse.builder.wizards.themes;

import com.volantis.mcs.eclipse.controls.ValidatedObjectControlFactory;
import com.volantis.mcs.eclipse.controls.ValidatedObjectControl;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;

/**
 * Factory for selector sequence providers.
 */
public class SelectorSequenceProviderFactory
        implements ValidatedObjectControlFactory {
    /**
     * The associated project
     */
    private IProject project;

    /**
     * Initialises a <code>SelectorSequenceProviderFactory</code> instance with
     * the given parameter
     * @param project the associated <code>IProject<code>
     */
    public SelectorSequenceProviderFactory(IProject project) {
        this.project = project;
    }

    // Javadoc inherited
    public ValidatedObjectControl buildValidatedObjectControl(Composite parent, int style) {
        // create a SelectorProvider instance
        return new SelectorSequenceProvider(parent, style, project);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
