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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.BigDecimalValidator;
import com.volantis.mcs.eclipse.validation.BigIntegerValidator;
import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Control;

import java.util.HashMap;


/**
 * A time provider control for entering a time.
 */
public class TimeProvider extends Composite implements ValidatedTextControl {

    /**
     * Resource prefix for the TimeSelectionDialog.
     */
    private final static String RESOURCE_PREFIX = "TimeProvider.";

    /**
     * Leading zeros for insertion if missing.
     */
    private static final String TWO_LEADING_ZEROES = "00";

    /**
     * Hours units (SMIL)
     */
    private static final String HOURS_UNITS = "h";

    /**
     * Minutes units (SMIL)
     */
    private static final String MINUTES_UNITS = "min";

    /**
     * Seconds units (SMIL)
     */
    private final static String SECONDS_UNITS = "s";

    /**
     * Mapping between fault types understood by this control and message
     * keys in ControlsMessages.properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    static {
        MESSAGE_KEY_MAPPINGS = new HashMap(4);
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_HOUR,
                RESOURCE_PREFIX + "invalidHour");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_MINUTE,
                RESOURCE_PREFIX + "invalidMinute");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_SECOND,
                RESOURCE_PREFIX + "invalidSecond");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.ZERO_TIME,
                RESOURCE_PREFIX + "zeroTime");
    }

    /**
     * The text field for hours entry. Hours is an unbounded integer,
     * with a minimum value of zero.
     */
    private Text hoursTextField;

    /**
     * The text field for minutes entry. Minutes is an integer in
     * the range 0 to 59 inclusive.
     */
    private Text minutesTextField;

    /**
     * The text field for seconds entry. Seconds is a bounded decimal
     * in the range 0 to 59.999 inclusive.
     */
    private Text secondsTextField;

    /**
     * The validation message builder used by all the validators.
     */
    private ValidationMessageBuilder validationMessageBuilder;

    /**
     * The validator for the hours.
     */
    private IndependentValidator hoursValidator;

    /**
     * The validator for the minutes.
     */
    private IndependentValidator minutesValidator;

    /**
     * The validator for the seconds.
     */
    private IndependentValidator secondsValidator;

    /**
     * Default validation status
     */
    private static final ValidationStatus OK_STATUS =
            new ValidationStatus(ValidationStatus.OK, "");

    /**
     * The text for the hours label.
     */
    private static final String HOURS_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "hours.label");

    /**
     * The text for the minutes label.
     */
    private static final String MINUTES_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "minutes.label");

    /**
     * The text for the seconds label.
     */
    private static final String SECONDS_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "seconds.label");

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "horizontalSpacing").intValue();

    /**
     * The width hint for the hours text field.
     */
    private static final int HOURS_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "hours.width").intValue();
    /**
     * The width hint for the minutes text field.
     */
    private static final int MINUTES_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "minutes.width").intValue();
    /**
     * The width hint for the seconds text field.
     */
    private static final int SECONDS_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "seconds.width").intValue();


    /**
     * Construct a TimeProvider with the specified parent and style.
     * @param parent the parent composite
     * @param style the style
     */
    public TimeProvider(Composite parent, int style) {
        super(parent, style);
        createControl();
        createValidators();
        initAccessible();
    }

    /**
     * Creates the validators for hours, minutes and seconds.
     */
    private void createValidators() {
        validationMessageBuilder = new ValidationMessageBuilder(
                ControlsMessages.getResourceBundle(),
                MESSAGE_KEY_MAPPINGS,
                null);
        BigIntegerValidator hoursBIV = new BigIntegerValidator("0");
        BigIntegerValidator minutesBIV = new BigIntegerValidator("0", "59");
        BigDecimalValidator secondsBDV = new BigDecimalValidator("0", "59.999");
        hoursValidator = new IndependentValidator(hoursBIV,
                validationMessageBuilder);
        minutesValidator = new IndependentValidator(minutesBIV,
                validationMessageBuilder);
        secondsValidator = new IndependentValidator(secondsBDV,
                validationMessageBuilder);
    }

    /**
     *
     */
    private void createControl() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createControls();
    }

    /**
     * Creates and adds labelled text fields for hours, minutes and seconds
     * entry, along with the Add button.
     */
    private void createControls() {
        //Create another composite to protect the labels and text fields
        //from the resizing behaviour when used by DialogListBuilder. Without this
        //composite, the resizing behaviour of the labels and text fields
        //is not what we want when used by the DialogListBuilder.
        Composite textComposite = new Composite(this, SWT.NONE);
        GridLayout textGrid = new GridLayout(5, false);
        textGrid.marginHeight = 0;
        textGrid.marginWidth = 0;
        textGrid.horizontalSpacing = HORIZONTAL_SPACING;
        textComposite.setLayout(textGrid);
        textComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //Add text field labels
        Label hoursLabel = new Label(textComposite, SWT.NONE);
        hoursLabel.setText(HOURS_TEXT);
        GridData hoursLabelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        hoursLabelData.horizontalSpan = 2;
        hoursLabel.setLayoutData(hoursLabelData);
        Label minutesLabel = new Label(textComposite, SWT.NONE);
        minutesLabel.setText(MINUTES_TEXT);
        GridData minutesLabelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        minutesLabelData.horizontalSpan = 2;
        minutesLabel.setLayoutData(minutesLabelData);
        Label secondsLabel = new Label(textComposite, SWT.NONE);
        secondsLabel.setText(SECONDS_TEXT);
        GridData secondsLabelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        secondsLabel.setLayoutData(secondsLabelData);
        //Add text fields and listener
        ModifyListener modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                fireModifyEvent(modifyEvent);
            }
        };
        hoursTextField = new Text(textComposite, SWT.BORDER | SWT.SINGLE);
        GridData hoursData = new GridData(GridData.FILL_HORIZONTAL);
        hoursData.widthHint = HOURS_WIDTH;
        hoursTextField.setLayoutData(hoursData);
        hoursTextField.addModifyListener(modifyListener);
        hoursTextField.
                setSize(HOURS_WIDTH, hoursTextField.getSize().y);
        Label colonLabel1 = new Label(textComposite, SWT.NONE);
        colonLabel1.setText(":");
        minutesTextField = new Text(textComposite, SWT.BORDER | SWT.SINGLE);
        GridData minutesData = new GridData(GridData.FILL_HORIZONTAL);
        minutesData.widthHint = MINUTES_WIDTH;
        minutesTextField.setLayoutData(minutesData);
        minutesTextField.addModifyListener(modifyListener);
        minutesTextField.
                setSize(MINUTES_WIDTH, minutesTextField.getSize().y);
        Label colonLabel2 = new Label(textComposite, SWT.NONE);
        colonLabel2.setText(":");
        secondsTextField = new Text(textComposite, SWT.BORDER | SWT.SINGLE);
        GridData secondsData = new GridData(GridData.FILL_HORIZONTAL);
        secondsData.widthHint = SECONDS_WIDTH;
        secondsTextField.setLayoutData(secondsData);
        secondsTextField.addModifyListener(modifyListener);
        secondsTextField.
                setSize(SECONDS_WIDTH, secondsTextField.getSize().y);
    }

    /**
     * Fires a ModifyText event to all registered listeners.
     * @param modifyEvent the ModifyEvent. modifyEvent.data
     * contains the new text
     */
    private void fireModifyEvent(ModifyEvent modifyEvent) {
        Event event = new Event();
        event.data = modifyEvent.data;
        event.widget = modifyEvent.widget;
        if (isListening(SWT.Modify)) {
            notifyListeners(SWT.Modify, event);
        }
    }

    /**
     * Adds a listener that is called when the contents of the text control
     * is modified.
     * @param listener for modifications
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            addListener(SWT.Modify, new TypedListener(listener));
        }
    }

    /**
     * Removes a control modification listener
     * @param listener for modifications
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            removeListener(SWT.Modify, new TypedListener(listener));
        }
    }

    /**
     * Validates the control's contents.
     * @return the validation status
     */
    public ValidationStatus validate() {
        ValidationStatus status = OK_STATUS;
        String errorMessage = null;
        String hoursText = hoursTextField.getText().trim();
        String minutesText = minutesTextField.getText().trim();
        String secondsText = secondsTextField.getText().trim();

        // Only process the time if a time has been entered.
        if (hoursText.length() > 0 ||
                minutesText.length() > 0 ||
                secondsText.length() > 0) {
            hoursText = hoursText.length() == 0 ? TWO_LEADING_ZEROES : hoursText;
            minutesText =
                    minutesText.length() == 0 ? TWO_LEADING_ZEROES : minutesText;
            secondsText =
                    secondsText.length() == 0 ? TWO_LEADING_ZEROES : secondsText;
            status = hoursValidator.validate(hoursText);
            if (status.isOK()) {
                status = minutesValidator.validate(minutesText);
                if (status.isOK()) {
                    status = secondsValidator.validate(secondsText);
                    if (!status.isOK()) {
                        errorMessage = validationMessageBuilder.
                                buildValidationMessage("invalidSecond",
                                        null, new String[]{secondsText});
                    }
                } else {
                    errorMessage = validationMessageBuilder.
                            buildValidationMessage("invalidMinute",
                                    null, new String[]{minutesText});
                }
            } else {
                errorMessage = validationMessageBuilder.
                        buildValidationMessage("invalidHour",
                                null, new String[]{hoursText});
            }
            if (status.isOK() && !(isNonZero(hoursText) ||
                    isNonZero(minutesText) ||
                    isNonZero(secondsText))) {
                errorMessage = validationMessageBuilder.
                        buildValidationMessage("zeroTime", null, null);
            }
            if (errorMessage != null) {
                status =new ValidationStatus(ValidationStatus.ERROR, errorMessage);
            }
        }
        return status;
    }

    /**
     * Gets the time value in SMIL format. Note that the time value may be
     * invalid, so ought to be validated.
     * @return the time value
     */
    public String getValue() {
        String timeValue = null;
        StringBuffer hoursBuff =
                new StringBuffer(hoursTextField.getText().trim());
        StringBuffer minutesBuff =
                new StringBuffer(minutesTextField.getText().trim());
        StringBuffer secondsBuff =
                new StringBuffer(secondsTextField.getText().trim());
        boolean hoursPresent = hoursBuff.length() > 0;
        boolean minutesPresent = minutesBuff.length() > 0;
        boolean secondsPresent = secondsBuff.length() > 0;
        //Check that only one value is present
        if (hoursPresent && !minutesPresent && !secondsPresent) {
            timeValue = hoursBuff.append(HOURS_UNITS).toString();
        } else if (!hoursPresent && minutesPresent && !secondsPresent) {
            timeValue = minutesBuff.append(MINUTES_UNITS).toString();
        } else if (!hoursPresent && !minutesPresent && secondsPresent) {
            if (secondsBuff.charAt(0) == '.') {
                secondsBuff.insert(0, '0');
            }
            timeValue = secondsBuff.append(SECONDS_UNITS).toString();
        } else {
            //More than one of hours, minutes and seconds are present
            if (!hoursPresent) {
                hoursBuff.append(TWO_LEADING_ZEROES);
            } else if (hoursBuff.length() == 1) {
                hoursBuff.insert(0, '0');
            }
            if (!minutesPresent) {
                minutesBuff.append(TWO_LEADING_ZEROES);
            } else if (minutesBuff.length() == 1) {
                minutesBuff.insert(0, '0');
            }
            if (!secondsPresent) {
                secondsBuff.append(TWO_LEADING_ZEROES);
            } else {
                if (secondsBuff.length() == 1) {
                    secondsBuff.insert(0, '0');
                }
                //The index is used to decide what to do when padding with
                //zeroes.
                int idx = secondsBuff.toString().indexOf('.');
                if (idx == secondsBuff.length() - 1) {
                    secondsBuff.append('0');
                }
                if (idx == 0) {
                    secondsBuff.insert(0, TWO_LEADING_ZEROES);
                } else if (idx == 1) {
                    secondsBuff.insert(0, '0');
                }
            }
            timeValue = hoursBuff.append(':').append(minutesBuff).
                    append(':').append(secondsBuff).toString();
        }
        return timeValue;
    }

    /**
     * Checks that a value evaluates to non zero.
     * @param value the value to check
     * @return boolean flag
     */
    private boolean isNonZero(String value) {
        boolean valid;
        try {
            valid = Float.parseFloat(value) > 0;
        } catch (NumberFormatException nfe) {
            valid = false;
        }
        return valid;
    }

    /**
     * If the value argument is an empty string then the hours, minutes &
     * seconds text fields are blanked. Otherwise an IllegalArgumentException
     * is thrown.
     * @throws IllegalArgumentException if the value argument is not an empty
     * string
     */
    public void setValue(String value) {
        if (value != null && value.trim().length() == 0) {
            hoursTextField.setText("");
            minutesTextField.setText("");
            secondsTextField.setText("");
        } else {
              throw new IllegalArgumentException(
                        "Expected an empty string value but got: " + value);
        }        
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        initAccessibleControl(hoursTextField, HOURS_TEXT);
        initAccessibleControl(minutesTextField, MINUTES_TEXT);
        initAccessibleControl(secondsTextField, SECONDS_TEXT);
    }


    /**
     * Initialise accessibility listener for a specified subcontrol.
     */
    private void initAccessibleControl(Control control, final String name) {
        StandardAccessibleListener accessibleListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = name;
                    }
                };
        accessibleListener.setControl(control);
        control.getAccessible().addAccessibleListener(accessibleListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Mar-05	7173/1	pcameron	VBM:2004092701 Fixed time validation when no time is entered

 01-Mar-05	7171/2	pcameron	VBM:2004092701 Fixed time validation when no time is entered

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 17-Aug-04	5227/1	adrian	VBM:2004070809 Fixed bug whereby times in seconds were appended with an extra zero

 23-Mar-04	3362/2	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 18-Mar-04	3416/1	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 10-Feb-04	2857/1	doug	VBM:2003112711 Added wizard for creating theme rules

 12-Jan-04	2215/3	pcameron	VBM:2003112405 Some tweaks to TimeSelectionDialog and TimeProvider

 09-Jan-04	2215/1	pcameron	VBM:2003112405 TimeSelectionDialog, ListBuilder and refactoring

 ===========================================================================
*/
