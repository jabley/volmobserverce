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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.ab.editors.dom.LPDMJDOMFactory;
import com.volantis.mcs.eclipse.ab.editors.themes.StyleValueComposite;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.ThemeSchemaType;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Testcase for StyleValueComposite.
 */
public class StyleValueCompositeTest extends ControlsTestAbstract {

    /**
     * Factory for creating JDOM relating objects
     */
    private static JDOMFactory factory = new LPDMJDOMFactory();

    /**
     * A map that will store JDOMElements that that have the attributes
     * set for a particular style value. The key to the element will be
     * the appropriate type
     */
    private static Map elements;

    static {

        elements = new HashMap();

        // add the various elements to the map.
        elements.put(StyleValueType.ANGLE,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.ANGLE_ATTRIBUTE.getName(),
                             "100"),
                         factory.attribute(
                             ThemeSchemaType.ANGLE_UNITS_ATTRIBUTE.getName(),
                             "rad")
                     }));

        elements.put(StyleValueType.COLOR,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.COLOR_ATTRIBUTE.getName(),
                             "blue"),
                     }));

        elements.put(StyleValueType.COMPONENT_URI,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.COMPONENT_URI_ATTRIBUTE.getName(),
                             "mcs-component"),
                     }));

        elements.put(StyleValueType.INHERIT,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.INHERIT_ATTRIBUTE.getName(),
                             "true"),
                     }));

        elements.put(StyleValueType.INTEGER,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.INTEGER_ATTRIBUTE.getName(),
                             "7"),
                     }));

        elements.put(StyleValueType.KEYWORD,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.KEYWORD_ATTRIBUTE.getName(),
                             "keyword_2"),
                     }));

        elements.put(StyleValueType.LENGTH,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.LENGTH_ATTRIBUTE.getName(),
                             "87878"),
                         factory.attribute(
                             ThemeSchemaType.LENGTH_UNITS_ATTRIBUTE.getName(),
                             "ex")
                     }));

        Element listElement = factory.element("dummy");
        for (int i=0; i<3; i++) {
            Element listItem = factory.element(
                ThemeSchemaType.ITEM_ELEMENT.getName());
            listItem.setAttribute("color", "blue");
            listElement.addContent(listItem);
        }
        elements.put(StyleValueType.LIST, listElement);


        elements.put(StyleValueType.NUMBER,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.NUMBER_ATTRIBUTE.getName(),
                             "2.0"),
                     }));

        elements.put(StyleValueType.PERCENTAGE,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.PERCENTAGE_ATTRIBUTE.getName(),
                             "76"),
                     }));

        elements.put(StyleValueType.STRING,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.STRING_ATTRIBUTE.getName(),
                             "myString"),
                     }));

        elements.put(StyleValueType.TIME,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.TIME_ATTRIBUTE.getName(),
                             "60"),
                         factory.attribute(
                             ThemeSchemaType.TIME_UNITS_ATTRIBUTE.getName(),
                             "s")
                     }));

        elements.put(StyleValueType.URI,
                     createElement(new Attribute[]{
                         factory.attribute(
                             ThemeSchemaType.KEYWORD_ATTRIBUTE.getName(),
                             "http://foo.com"),
                     }));
    }

    /**
     * Initializes a <code>StyleValueCompositeTest</code> instance with the
     * given argument
     * @param title the name of the test.
     */
    public StyleValueCompositeTest(String title) {
        super(title);
    }

    // javadoc inherited
    public void createControl() {

        GridLayout layout = new GridLayout(2, false);
        layout.horizontalSpacing = 5;
        layout.verticalSpacing = 12;
        layout.makeColumnsEqualWidth = true;
        Composite container = new Composite(getShell(), SWT.DEFAULT);
        container.setLayout(layout);

        // most types
        addControlRow(container, new StyleValueType[]{
            StyleValueType.ANGLE,
            StyleValueType.COLOR,
            StyleValueType.COMPONENT_URI,
            StyleValueType.INHERIT,
            StyleValueType.INTEGER,
            StyleValueType.KEYWORD,
            StyleValueType.LENGTH,
            StyleValueType.LIST,
            StyleValueType.NUMBER,
            StyleValueType.PERCENTAGE,
            StyleValueType.STRING,
            StyleValueType.TIME,
            StyleValueType.URI
        });

        // no color button
        addControlRow(container, new StyleValueType[]{
            StyleValueType.ANGLE,
            StyleValueType.COMPONENT_URI,
            StyleValueType.INHERIT,            
            StyleValueType.TIME,
            StyleValueType.URI
        });

        // no color button, or units drop down
        addControlRow(container, new StyleValueType[]{
            StyleValueType.INHERIT,
            StyleValueType.COMPONENT_URI,
            StyleValueType.KEYWORD
        });

        // no color button, or units drop down, or browse button
        addControlRow(container, new StyleValueType[]{
            StyleValueType.INHERIT,
            StyleValueType.NUMBER
        });

    }

    /**
     * Adds a row of controls to the layout. Each row has a StyleValueComposite
     * control that allows a style value to be entered. There is an associated
     * text control that displays the String that the
     * {@link com.volantis.mcs.eclipse.ab.editors.themes.StyleValueComposite#getValue} method returns. Finally, there is
     * a drop down list that allows an "style value type" to be selected. When
     * a type is selected a jdom Element that has the correct attributes set
     * for the selected type is passed to the StyleValueComposites
     * displayElement(Element) method.
     * @param parent the parent
     * @param types the types of style value that the control should support
     */
    private void addControlRow(Composite parent, StyleValueType[] types) {

        Set valueTypes = createTypeSet(types);
        // create the StyleValueComposite object
        final StyleValueComposite styleValueComposite =
            createStyleValueComposite(parent, valueTypes);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        styleValueComposite.setLayoutData(data);
        
        // new container that the 2 supporting controls will be added to.
        GridLayout layout = new GridLayout(2, false);
        Composite container = new Composite(parent, SWT.DEFAULT);
        container.setLayout(layout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        // create the drop down that will call the displayElement method
        // on the StyleValueComposite whenever an item is selected
        createLabel(container, "Display Element Type: ");
        final Combo combo = createDiplayElememntSelection(container,
                                                          valueTypes);
        combo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                StyleValueType type = StyleValueType.get(combo.getText());
                Element element = (Element) elements.get(type);
                styleValueComposite.displayElement(element);
            }
        });

        // add the text field that will display the value that the
        // StyleValueComposites getValue method returns
        createLabel(container, "value: ");
        final Text textField = createTextField(container);

        styleValueComposite.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                textField.setText(styleValueComposite.getValue());
            }
        });
    }

    /**
     * Creates a StyleValueComposite control
     * @param parent the parent Composite
     * @param valueTypes the StyleValueTypes that the control should support
     * @return a StyleValueComposite instance
     */
    private StyleValueComposite createStyleValueComposite(Composite parent,
                                                          Set valueTypes) {
        final StyleValueComposite composite =
            new StyleValueComposite(parent,
                                    valueTypes,
                                    createKeywordMapper(),
                                    createBrowseAction(),
                                    null);
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER &
                                             GridData.FILL_HORIZONTAL));
        return composite;
    }

    /**
     * Creates a text KeywordMapper instance
     * @return a KeywordMapper
     */
    private KeywordMapper createKeywordMapper() {
        KeywordMapper mapper = new KeywordMapper(2) {
        };
        mapper.addMapping(0, "keyword_1");
        mapper.addMapping(1, "keyword_2");
        return mapper;
    }

    /**
     * Creates a test BrowseAction instance
     * @return a BrowseAction instance
     */
    private StyleValueComposite.BrowseAction createBrowseAction() {
        return new StyleValueComposite.BrowseAction() {
            public String doBrowse(String value, Composite parent) {
                return "browsed";
            }
        };
    }

    /**
     * Creates a Combo with a list of supported StyleValueTypes
     * @param parent the parent Composite
     * @param valueTypes the Set of types
     * @return a Combo instance
     */
    private Combo createDiplayElememntSelection(Composite parent,
                                                Set valueTypes) {
        // create the combo
        Combo displayTypes = new Combo(parent, SWT.READ_ONLY);
        GridData comboData = new GridData(GridData.FILL_HORIZONTAL);
        displayTypes.setLayoutData(comboData);

        // add a blank line to the combo
        displayTypes.add("");
        StyleValueType type;
        for (Iterator i = valueTypes.iterator(); i.hasNext();) {
            type = (StyleValueType) i.next();
            displayTypes.add(type.getType());
        }
        return displayTypes;
    }

    /**
     * Creates a read only text field
     * @param parent the parent Composite
     * @return a Text instance
     */
    private Text createTextField(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        GridData textData = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(textData);
        return text;
    }

    /**
     * Creates a Label instance
     * @param parent the parent Composite
     * @param labelText the labels text
     * @return a Label instance
     */
    private Label createLabel(Composite parent, String labelText) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(labelText);
        return label;
    }

    /**
     * Factory method that will create an element with the given attributes
     * @param attributes an array of attributes
     * @return an Element
     */
    public static Element createElement(Attribute[] attributes) {
        Element element = factory.element("property");
        element.setAttribute(factory.attribute(
            ThemeSchemaType.PRIORITY_ATTRIBUTE.getName(),
            "important"));
        for (int i = 0; i < attributes.length; i++) {
            element.setAttribute(attributes[i]);
        }
        return element;
    }

    /**
     * Creates a Set of <code>StyleValueType</code> objects for the given
     * array of StyleValueType objects
     * @param types the array of StyleValueType objects
     * @return a Set of StyleValueType objects
     */
    public Set createTypeSet(StyleValueType[] types) {
        Set valueTypes = new HashSet();
        for (int i = 0; i < types.length; i++) {
            valueTypes.add(types[i]);
        }
        return valueTypes;
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the StyleValueCompositeTest does not require input arguments.
     */
    public static void main(String[] args) {
        new StyleValueCompositeTest("StyleValueCompositeTest").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 15-Jan-04	2526/4	doug	VBM:2003112607 Added the StyleValueComposite control

 14-Jan-04	2526/1	doug	VBM:2003112607 Added the StyleValueComposite control

 ===========================================================================
*/
