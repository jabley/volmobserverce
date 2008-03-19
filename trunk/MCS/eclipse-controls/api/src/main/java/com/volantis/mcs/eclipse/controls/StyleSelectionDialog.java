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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.RepositorySchemaResolverFactory;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.validation.sax.xerces.XercesBasedDOMValidator;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.validation.CharacterSetValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.repository.xml.PolicySchemas;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Document;
import org.jdom.Element;
import org.xml.sax.SAXException;

import java.util.HashMap;

/**
 * A StyleSelectionDialog for selecting and returning a selection of style
 * classes. The initial selection can be set with setStyles or upon
 * construction, and the current selection can be read with getStyles.
 */
public class StyleSelectionDialog extends MessageAreaSelectionDialog {
    /**
     * Resource prefix.
     */
    private final static String RESOURCE_PREFIX = "StyleSelectionDialog.";

    /**
     * Used to validate the style classes.
     */
    private DOMValidator validator;

    /**
     * Character set validator that ensures each style class in the list
     * doesn't contain any ' ' characters.
     */
    private CharacterSetValidator charsetValidator;

    /**
     * Mapping between fault types understood by this page and
     * message keys in the this dialog's properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    /**
     * Default validation status
     */
    private static final ValidationStatus OK_STATUS =
            new ValidationStatus(ValidationStatus.OK, "");

    /**
     * The title of the dialog.
     */
    private static final String DIALOG_TITLE =
            ControlsMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
                                        "horizontalSpacing").intValue();

    /**
     * The margin height of the dialog.
     */
    private static final int MARGIN_HEIGHT =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
                                        "marginHeight").intValue();

    /**
     * The margin width of the dialog.
     */
    private static final int MARGIN_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
                                        "marginWidth").intValue();

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
                                        "verticalSpacing").intValue();

    /**
     * The width hint for each list widget.
     */
    private static final int WIDTH_HINT =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
                                        "widthHint").intValue();

    /**
     * The height hint for the error message display area.
     */
    private static final int ERROR_MSG_HEIGHT_HINT = ControlsMessages.getInteger(
                    RESOURCE_PREFIX + "errorMessage.heightHint").intValue();

    /**
     * A style class element that is used to validate the list of style
     * classes.
     */
    private Element styleClassElement;

    /**
     * Validation message builder
     */
    private ValidationMessageBuilder messageBuilder;


    static {
        // Initialize message key mappings
        MESSAGE_KEY_MAPPINGS = new HashMap();
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_SCHEMA_PATTERN_VALUE,
                                 RESOURCE_PREFIX + "invalidSchemaPatternValue");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                                 RESOURCE_PREFIX + "nameTooLong");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_CHARACTER,
                                 RESOURCE_PREFIX + "invalidCharacter");
    }

    /**
     * The DialogListBuilder widget used by the dialog.
     */
    private ListValueBuilder listBuilder;

    /**
     * The current selection of the dialog.
     */
    private Object[] currentSelection;

    /**
     * The style class error reporter.
     */
    private StyleclassErrorReporter errorReporter;

    /**
     * The maximum amount of characters for each style class.
     */
    private static final int STYLE_CLASS_MAX_CHARS = 128;

    /**
     * Error Reporter that will be used when validating the styleclasses.
     */
    private class StyleclassErrorReporter implements ErrorReporter {
        /**
         * Current error message
         */
        private String errorMessage;

        /**
         * Returns the current error
         * @return the current error or null if there is no error.
         */
        public String getErrorMessage() {
            return errorMessage;
        }

        // javadod inehrited
        public void reportError(ErrorDetails details) {
            String key = details.getKey();
            String param = details.getParam();

            assert key != null;

            // Only want to report the first error message
            if (errorMessage == null) {
                // build the message
                errorMessage = messageBuilder.buildValidationMessage(
                            key,
                            param,
                            new String[]{ param });
            }
        }

        // javadoc inherited
        public void validationCompleted(XPath xpath) {
            // does nothing
        }

        // javadoc inherited
        public void validationStarted(Element root, XPath xpath) {
            // set the current error to null
            errorMessage = null;
        }
    }

    /**
     * Constructor for a StyleSelectionDialog.
     *
     * @param parent the parent shell of the dialog
     * @param initialSelection
     *               the initial selection for the dialog
     */
    public StyleSelectionDialog(Shell parent, String[] initialSelection) {
        super(parent);
        this.currentSelection = initialSelection;
        setTitle(DIALOG_TITLE);
        setBlockOnOpen(true);

        messageBuilder = new ValidationMessageBuilder(
                    ControlsMessages.getResourceBundle(),
                    MESSAGE_KEY_MAPPINGS,
                    null);


        createValidator();
        createElement();

        char[] invalidChars = {' '};
        charsetValidator = new CharacterSetValidator(
                invalidChars, CharacterSetValidator.INVALID_CHAR_VALIDATION);
        charsetValidator.setMaxChars(STYLE_CLASS_MAX_CHARS);
    }

    /**
     * Create the style class element used during validation. This element must
     * contain certain attributes (columns, columnCount, rows, name, rowCount)
     * and child element in order for the validation on the style class element
     * to work.
     */
    private void createElement() {
        styleClassElement = new Element(
            FormatType.SPATIAL_FORMAT_ITERATOR.getElementName(),
            MCSNamespace.LPDM);

        styleClassElement.setAttribute("columns", "variable");
        styleClassElement.setAttribute("columnCount", "0");
        styleClassElement.setAttribute("rows", "variable");
        styleClassElement.setAttribute("name", "spatial");
        styleClassElement.setAttribute("rowCount", "1");

        Element child = new Element(FormatType.PANE.getElementName(),
                                    MCSNamespace.LPDM);
        child.setAttribute("name", "pane");
        styleClassElement.addContent(child);

        Document document = new Document();
        document.setRootElement(styleClassElement);
    }

    /**
     * Create a validator with an appropriate error reporter.
     */
    private void createValidator() {
        errorReporter = new StyleclassErrorReporter();

        String schemaLocation = MCSNamespace.LPDM.getURI() + " " +
                    PolicySchemas.MARLIN_LPDM_CURRENT.getLocationURL();
        try {
            JarFileEntityResolver repositorySchemaResolver =
                    RepositorySchemaResolverFactory.create();

            // create the validator
            validator = new XercesBasedDOMValidator(
                    repositorySchemaResolver,
                        errorReporter);
            // provide the validator with the schemas location
            validator.declareSchemaLocation(schemaLocation);
        } catch (SAXException e) {
            // report the error
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        } catch (ParserErrorException e) {
            // report the error
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Creates the dialog's control.
     *
     * @param parent the parent of the dialog's control
     * @return the created control
     */
    protected Control createDialogArea(Composite parent) {
        final Composite topLevel = (Composite) super.createDialogArea(parent);
        GridLayout topLevelGrid = new GridLayout();
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.verticalSpacing = VERTICAL_SPACING;
        topLevelGrid.marginHeight = MARGIN_HEIGHT;
        topLevelGrid.marginWidth = MARGIN_WIDTH;
        topLevel.setLayout(topLevelGrid);
        addListBuilder(topLevel);
        addErrorMessageArea(topLevel);
        validateDialog();
        topLevel.layout();
        return topLevel;
    }

    /**
     * Creates and adds the DialogListBuilder with a ModifyListener to listen
     * for item removals, and populates the list with an initial selection.
     *
     * @param parent the parent composite
     */
    private void addListBuilder(Composite parent) {
        listBuilder = new ListValueBuilder(parent, true, null);
        listBuilder.setItems(currentSelection);

        GridData listGridData = new GridData(GridData.FILL_BOTH);
        listBuilder.setLayoutData(listGridData);
        listGridData.widthHint = WIDTH_HINT;
        listBuilder.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                currentSelection = listBuilder.getItems();
                validateDialog();
            }
        });
    }

    /**
     * Sets the style selection for the dialog and populates the dialog's list
     * with the items in the array.
     *
     * @param items the array of items
     */
    public void setStyles(String[] items) {
        currentSelection = items;
        // This may be called on a dialog that has been disposed, so need
        // to check listBuilder.
        if (listBuilder != null && !listBuilder.isDisposed()) {
            listBuilder.setItems(currentSelection);
        }
    }

    /**
     * Gets the selection from the dialog.
     *
     * @return the selected items
     */
    public String getStyles() {
        String styles = null;
        if (currentSelection != null) {
            // Concatenate the styles together.
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < currentSelection.length; i++) {
                buffer.append(currentSelection[i]);
                if (i < currentSelection.length - 1) {
                    buffer.append(' ');
                }
            }
            styles = buffer.toString();
        }
        return styles;
    }

    /**
     * Gets the result from the dialog.
     *
     * @return the selected items in the listBuilder
     */
    public final Object[] getResult() {
        return currentSelection;
    }

    /**
     * Creates and adds the message area for error messages.
     *
     * @param parent the parent composite
     */
    private void addErrorMessageArea(Composite parent) {
        Composite errorMessageComposite =
                (Composite) createErrorMessageArea(parent);
        GridData emData = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
        emData.heightHint = ERROR_MSG_HEIGHT_HINT;
        errorMessageComposite.setLayoutData(emData);
    }

    /**
     * Validates the selector and list items. Error messages from the selector
     * validation take precedence.
     */
    private void validateDialog() {
        ValidationStatus status = OK_STATUS;
        for (int i = 0; status.isOK() && (i < currentSelection.length); i++) {
            status = charsetValidator.validate(currentSelection[i],
                                               messageBuilder);
        }
        if (status.isOK()) {
            // Doesn't matter if is row or columnStyleClasses attribute as
            // long as the validation for its value happens we're OK.
            styleClassElement.setAttribute("rowStyleClasses", getStyles());

            validator.validate(styleClassElement);

            // if the element is invalid then we need to return an ERROR
            // validation status.
            if (errorReporter.getErrorMessage() != null) {
                status = new ValidationStatus(ValidationStatus.ERROR,
                                              errorReporter.getErrorMessage());
            }
        }
        if (status.isOK()) {
            // Clear error message
            setErrorMessage(null);
        } else if (status.getSeverity() == ValidationStatus.ERROR) {
            setErrorMessage(status.getMessage());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 02-Dec-04	6354/2	adrianj	VBM:2004112605 Refactor XML validation error reporting

 13-Oct-04	5771/3	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 ===========================================================================
*/
