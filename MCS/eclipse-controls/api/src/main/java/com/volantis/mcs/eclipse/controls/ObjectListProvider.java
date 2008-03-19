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

import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TypedListener;

/**
 * Control for providing an object by selecting it from a drop-down list.
 */
public class ObjectListProvider extends Composite
        implements ValidatedObjectControl {
    /**
     * The objects to be displayed in the drop-down list.
     */
    private Object[] availableObjects;

    /**
     * The object parser to be used for displaying the objects in the list.
     */
    private ObjectParser parser;

    /**
     * The combo displaying the list of objects.
     */
    private Combo combo;

    /**
     * Default validation status
     */
    private static final ValidationStatus OK_STATUS =
            new ValidationStatus(ValidationStatus.OK, "");

    /**
     * The default parser to use if none is specified.
     */
    private static final ObjectParser DEFAULT_PARSER =
            ObjectParserFactory.getDefaultInstance().createDefaultParser();

    /**
     * Creates an object list provider.
     *
     * @param parent The parent composite
     * @param style The style for the provider
     * @param available The available objects to display in the list
     * @param parser The parser to use for converting the objects to text
     */
    public ObjectListProvider(Composite parent, int style, Object[] available,
                              ObjectParser parser) {
        super(parent, style);
        this.availableObjects = available;
        if (parser == null) {
            parser = DEFAULT_PARSER;
        }
        this.parser = parser;
        initControl();
    }

    /**
     * Build up the GUI components for this object provider.
     */
    private void initControl() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        combo = new Combo(this, SWT.READ_ONLY);
        for (int i = 0; i < availableObjects.length; i++) {
            combo.add(parser.objectToText(availableObjects[i]));
        }
        combo.select(0);

        GridData comboData = new GridData(GridData.FILL_BOTH);
        combo.setLayoutData(comboData);

        ModifyListener modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                fireModifyEvent(modifyEvent);
            }
        };
        combo.addModifyListener(modifyListener);
    }

    // Javadoc inherited
    public Object getValue() {
        return availableObjects[combo.getSelectionIndex()];
    }

    // Javadoc inherited
    public void setValue(Object newValue) {
        int selectedObject = -1;
        for (int i = 0; selectedObject == -1 &&
                i < availableObjects.length; i++) {
            if (availableObjects[i] != null &&
                    availableObjects[i].equals(newValue)) {
                selectedObject = i;
            }
        }

        if (selectedObject != -1) {
            combo.select(selectedObject);
        }
    }

    /**
     * All values are valid, since they were specified as the available objects
     * at initialisation. Therefore this method always returns OK.
     *
     * @return The validation status (OK).
     */
    public ValidationStatus validate() {
        return OK_STATUS;
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

    // Javadoc inherited
    public boolean canProvideObject() {
        return true;
    }

    //javadoc inherited
    public void addStateChangeListener(StateChangeListener listener) {
        //todo implement this method
    }

    //javadoc inherited
    public void removeStateChangeListener(StateChangeListener listener) {
        //todo implement this method
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
