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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.controls.FixedListSelectionDialog;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Element;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

/**
 * The PolicyValueSelectionDialog allows multiple values to be selected for a
 * multi-valued policy. The values are given to the dialog by a
 * "selection element" for the specific policy.
 */
public class PolicyValueSelectionDialog extends FixedListSelectionDialog {

    /**
     * The resource prefix for the PolicyValueSelectionDialog.
     */
    private static final String RESOURCE_PREFIX =
            "PolicyValueSelectionDialog.";

    /**
     * Constructs a new PolicyValueSelectionDialog.
     * @param parent the parent Shell for the dialog. Cannot be null.
     * @param policyName the name of the policy. Cannot be null nor empty.
     * @param selectionElement the element containing the selection choices.
     *                         Cannot be null.
     * @throws IllegalArgumentException if policyName is null or
     *                                  selectionElement is invalid
     */
    public PolicyValueSelectionDialog(Shell parent, String policyName,
                                      Element selectionElement) {
        // Duplicates are not allowed.
        super(parent, false);

        // Reject a null element
        if (selectionElement == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "selectionElement");
        }

        // Reject an element not named "selection"
        if (!selectionElement.getName().equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME)) {
            throw new IllegalArgumentException("selectionElement has " +
                    "invalid name. Expected element named " +
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_SELECTION_ELEMENT_NAME + " but got " +
                    selectionElement.getName());
        }

        createTitle(policyName);
        setAvailableSelection(getChoices(selectionElement));
    }

    /**
     * Gets the choices provided by the supplied selection element.
     * @param selectionElement the element encapsulating the available choices
     * @return the available choices
     * @throws IllegalArgumentException if selectionElement is not a valid
     *                                   selection element
     */
    private String[] getChoices(Element selectionElement) {
        String[] choices = null;
        List children = selectionElement.getChildren();
        choices = new String[children.size()];
        Iterator it = children.iterator();
        int index = 0;
        while (it.hasNext()) {
            Element keywordElement = (Element) it.next();

            if (!keywordElement.getName().
                    equals(DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_KEYWORD_ELEMENT_NAME)) {
                // Reject children not named "keyword"
                throw new IllegalArgumentException("The selection " +
                        "element " + selectionElement.getName() +
                        " is invalid. Expected element named " +
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_KEYWORD_ELEMENT_NAME +
                        " but got " + keywordElement.getName());
            }
            choices[index++] = keywordElement.getText();
        }
        return choices;
    }

    /**
     * Creates the title for the dialog using a message format resource and
     * the supplied policy name.
     * @param policyName the policy name to use in the title
     */
    private void createTitle(String policyName) {
        String titleFormat = DevicesMessages.getString(RESOURCE_PREFIX +
                "title");
        MessageFormat mf = new MessageFormat(titleFormat);
        setTitle(mf.format(new String[]{policyName}));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4161/2	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 11-May-04	4276/1	byron	VBM:2004051007 New policy creation fails for selection type policies

 01-Apr-04	3602/4	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 10-Mar-04	3383/6	pcameron	VBM:2004030412 Added exception to PolicyValueSelectionDialog constructor

 10-Mar-04	3383/4	pcameron	VBM:2004030412 Some tweaks to PolicyValueSelectionDialog

 10-Mar-04	3383/2	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 ===========================================================================
*/
