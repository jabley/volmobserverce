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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.Validator;
import com.volantis.mcs.eclipse.validation.ColorValidator;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.parsers.javacc.TimerParser;
import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.themes.parsing.ObjectParser;

import java.util.HashMap;
import java.io.StringReader;

import org.eclipse.swt.widgets.Shell;

/**
 * A factory for creating dialogs which contain a selection list.
 */
public class ListSelectionDialogFactory {

    /**
     * Resource prefix for the ColorListSelectionDialog.
     */
    private final static String COLOR_RESOURCE_PREFIX =
            "ColorListSelectionDialog.";

    /**
     * Resource prefix for the TimeSelectionDialog.
     */
    private final static String TIME_RESOURCE_PREFIX =
            "TimeSelectionDialog.";

    /**
     * Resource prefix for the ObjectListDialog.
     */
    private final static String OBJECT_LIST_RESOURCE_PREFIX =
            "TimeSelectionDialog.";

    /**
     * Ensure this cannot be instantiated.
     */
    private ListSelectionDialogFactory() {
    }

    /**
     * A customisableObjectListSelectionDialog for selecting and returning a
     * selection of objects created by {@link ValidatedObjectControlFactory}
     * instances. The initial selection can be set with setSelection or upon
     * construction, and the current selection can be read with getSelection.
     *
     * @param parent The parent shell for the dialog
     * @param preselected The preselected objects
     * @param factory The control factory for creating objects
     * @param parser The parser used to display the objects and to convert
     *               edited text back to object form if the list is editable
     * @param validator The validator to use for the dialog
     * @param editable True if the list can be edited as text (requires an
     *                 appropriate parser)
     * @param resourcePrefix The resource prefix string to use
     * @return The CustomisableObjectListSelectionDialog
     */
    public static CustomisableObjectListSelectionDialog
            createObjectListSelectionDialog(Shell parent,
                                         Object[] preselected,
                                         ValidatedObjectControlFactory factory,
                                         ObjectParser parser,
                                         IndependentValidator validator,
                                         boolean editable,
                                         String resourcePrefix) {
        return new CustomisableObjectListSelectionDialog(parent,
                resourcePrefix,
                factory, validator, false,
                preselected, parser, editable);
    }

    /**
     * A ColorListSelectionDialog for selecting and returning a selection of
     * colors. The initial selection can be set with setSelection or upon
     * construction, and the current selection can be read with getSelection.
     */
    public static CustomisableListSelectionDialog
            createColorListSelectionDialog(Shell parent,
                                           String[] preSelected) {
        return new CustomisableListSelectionDialog(
                parent.getShell(),
                COLOR_RESOURCE_PREFIX,
                new ColorSelectorFactory(),
                createColorListValidator(),
                true, // colour list is allowed duplicate colours
                preSelected);
    }

    /**
     * Creates and returns a validator for validating the list of colours.
     *
     * @return the validator
     */
    private static IndependentValidator createColorListValidator() {
        HashMap messageKeyMappings = new HashMap(1);
        messageKeyMappings.put(FaultTypes.INVALID_COLOR,
                               "ColorListSelectionDialog.invalidColor");
        ValidationMessageBuilder builder =
                new ValidationMessageBuilder(
                        ControlsMessages.getResourceBundle(),
                        messageKeyMappings,
                        null);
        Validator validator = new ColorValidator(NamedColor.getAllColors());
        return new IndependentValidator(validator, builder);
    }

    /**
     * A TimeListSelectionDialog for selecting and returning a selection of
     * times. The initial selection can be set with setSelection or upon
     * construction, and the current selection can be read with getSelection.
     */
    public static CustomisableListSelectionDialog
            createTimeListSelectionDialog(Shell parent, String[] preSelected) {
        return new CustomisableListSelectionDialog(
                parent,
                TIME_RESOURCE_PREFIX,
                new TimeProviderFactory(),
                createTimeListValidator(),
                true, // duplicate time values are allowed.
                preSelected);

    }

    /**
     * Creates and returns a validator for validating the list of times.
     *
     * @return the independent validator to use
     */
    private static IndependentValidator createTimeListValidator() {
        Validator validator = new Validator() {
            private final ValidationStatus OK_STATUS =
                    new ValidationStatus(ValidationStatus.OK, "");

            public ValidationStatus validate(Object object,
                                             ValidationMessageBuilder builder) {
                ValidationStatus status = OK_STATUS;
                String item = (String) object;
                StringReader reader = new StringReader(item);
                String errorMessage = null;
                TimerParser timerParser = new TimerParser(reader);
                try {
                    timerParser.parse();
                } catch (Throwable e) {
                    errorMessage = builder.
                            buildValidationMessage("invalidTime",
                                                   null, new String[]{item});
                }
                if ("00:00:00".equals(item)) {
                    errorMessage = builder.
                            buildValidationMessage("zeroTime",
                                                   null, null);
                }
                if (errorMessage != null) {
                    status = new ValidationStatus(ValidationStatus.ERROR,
                                                  errorMessage);
                }
                return status;
            }
        };

        HashMap messageKeyMappings = new HashMap(2);
        messageKeyMappings.put(FaultTypes.INVALID_TIME,
                               TIME_RESOURCE_PREFIX + "invalidTime");
        messageKeyMappings.put(FaultTypes.ZERO_TIME,
                               TIME_RESOURCE_PREFIX + "zeroTime");

        ValidationMessageBuilder builder =
                new ValidationMessageBuilder(
                        ControlsMessages.getResourceBundle(),
                        messageKeyMappings,
                        null);
        return new IndependentValidator(validator, builder);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 09-Mar-05	7073/5	matthew	VBM:2005022203 javadoc fixups

 09-Mar-05	7073/3	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 08-Mar-05	7073/1	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 ===========================================================================
*/
