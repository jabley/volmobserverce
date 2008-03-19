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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Composite;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;

/**
 * A GUI component for creating pseudo element selectors.
 */
public class PseudoElementProvider extends PseudoSelectorProvider {
    /**
     * Create a new pseudo element provider.
     *
     * @param parent The parent composite in which the provider will be created
     * @param style The style for the provider
     * @param templates The internal pseudo element names to use
     * @param displayValues The display pseudo element names to use
     * @param parameterised The pseudo element names which can have parameters
     *                      associated with them
     */
    PseudoElementProvider(Composite parent, int style,
                          String[] templates, String[] displayValues,
                          String[] parameterised) {
        super(parent, style, templates, displayValues, parameterised);
    }

    // Javadoc inherited
    public Object getValue() {
        int selectedIndex = pseudoSelectorsCombo.getSelectionIndex();
        String selectedTemplate = templates[selectedIndex];
        PseudoElementSelector selector =
                MODEL_FACTORY.createPseudoElementSelector(selectedTemplate);

        return selector;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 20-Sep-05	9380/3	adrianj	VBM:2005082401 Tidy up and javadoc nth-child support

 14-Sep-05	9380/1	adrianj	VBM:2005082401 GUI support for nth-child

 ===========================================================================
*/
