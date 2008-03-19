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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.FormSection;
import com.volantis.mcs.eclipse.builder.editors.common.PropertiesComposite;
import com.volantis.mcs.eclipse.builder.editors.common.PropertiesCompositeChangeListener;
import com.volantis.mcs.eclipse.builder.common.ProxyProvider;
import com.volantis.mcs.eclipse.builder.common.ProxyReceiver;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.styling.properties.StyleProperty;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The GUI section for editing style properties.
 */
public class StylePropertiesSection extends FormSection implements InteractionFocussable {
    /**
     * Prefix for resources associated with this class.
     */
    private static String RESOURCE_PREFIX = "StylePropertiesSection.";

    /**
     * Height hint for section
     */
    private static final int HEIGHT_HINT =
            ThemesMessages.getInteger(RESOURCE_PREFIX + "heightHint").
            intValue();

    /**
     * A CSS parser instance.
     */
    final static CSSParser CSS_PARSER =
            CSSParserFactory.getDefaultInstance().createStrictParser();

    /**
     * The context shared between editors of this resource.
     */
    private ThemeEditorContext context;

    /**
     * The style category selection composite.
     */
    private StyleCategoriesComposite styleCategoriesComposite;

    /**
     * A stack layout containing properties composites for each category.
     */
    private StackLayout categoryStack;

    /**
     * The container for the per-category properties composites.
     */
    private Composite categoryContainer;

    /**
     * An empty composite to display if no category is selected.
     */
    private Composite emptyCategory;

    /**
     * The display area containing the category composite and style property
     * composite.
     */
    private Composite displayArea;

    private ScrolledComposite scroller;

    /**
     * A map of {@link PropertiesComposite}s associated with categories.
     */
    private Map categoryComposites = new HashMap();

    /**
     * The style properties proxy object being edited by this section.
     */
    private BeanProxy styleProperties;

    /**
     * Construct a style properties section.
     *
     * @param composite The parent composite
     * @param style The SWT style
     * @param context
     */
    public StylePropertiesSection(Composite composite, int style,
                                  ThemeEditorContext context) {
        super(composite, style);
        this.context = context;

        createDisplayArea();
    }

    /**
     * Sets the proxy for the style properties object being represented by this
     * section. This will trigger a refresh of the values displayed on any
     * category composites that have been created.
     *
     * @param styleProperties The {@link BeanProxy} representing the style
     *                        properties to be displayed/edited
     */
    public void setStyleProperties(BeanProxy styleProperties) {
        this.styleProperties = styleProperties;
        if (styleProperties != null) {
            Iterator it = categoryComposites.values().iterator();
            while (it.hasNext()) {
                PropertiesComposite composite = (PropertiesComposite) it.next();
                composite.setEnabled(isEnabled());
                composite.updateFromProxy(styleProperties);
            }
            styleCategoriesComposite.setEnabled(true);
            styleCategoriesComposite.redraw();
        } else {
            Iterator it = categoryComposites.values().iterator();
            while (it.hasNext()) {
                PropertiesComposite composite = (PropertiesComposite) it.next();
                composite.clear();
                composite.setEnabled(false);
            }
            styleCategoriesComposite.setEnabled(false);
            styleCategoriesComposite.redraw();
        }
    }

    /**
     * Called when a style property has changed, to trigger the change to the
     * underlying model.
     *
     * @param composite The properties composite containing the changed value
     * @param property The identifier for the property
     */
    private void stylePropertyChanged(PropertiesComposite composite, PropertyIdentifier property) {
        if (styleProperties != null) {
            Proxy valueProxy = styleProperties.getPropertyProxy(property);
            PropertyValue newPropertyValue = (PropertyValue)
                    composite.getProperty(property);
            PropertyValue oldPropertyValue = (PropertyValue)
                    valueProxy.getModelObject();
            // Only set the value if it has changed - prevents changes that
            // don't alter the value (such as adding irrelevant whitespace)
            // from being seen as modifications leading to exceptions when
            // carrying out the set operation.
            if ((newPropertyValue == null) ? oldPropertyValue != null :
                    !newPropertyValue.equals(oldPropertyValue)) {
                Operation operation = valueProxy.prepareSetModelObjectOperation(newPropertyValue);
                context.executeOperation(operation);
            }
        }
    }

    /**
     * Constructs the display area for this GUI section.
     */
    private void createDisplayArea() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        Section section =
                createSection(this, EditorMessages.getString(RESOURCE_PREFIX +
                "title"), null, Section.EXPANDED);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);
        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        data = new GridData(GridData.FILL_BOTH);
        data.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        data.heightHint = HEIGHT_HINT;

        displayArea.setLayout(gridLayout);
        displayArea.setLayoutData(data);

        // add the style categories tree to this display area
        styleCategoriesComposite = new StyleCategoriesComposite(displayArea,
                new ProxyProvider() {
                    // Javadoc inherited
                    public Proxy getProxy() {
                        return styleProperties;
                    }
                },
                new ProxyReceiver() {
                    // Javadoc inherited
                    public void setProxy(Proxy proxy) {
                        // This is used to set the properties that were passed
                        // in by the proxy provider, so we can safely assume
                        // the value being passed in is the same as the one we
                        // provided.
                        setStyleProperties(styleProperties);
                    }
                });
        // add a listener to the tree so that we can update
        // StyleCategoryComposite that is at the top of the stack layout.
        ISelectionChangedListener styleCategoriesListener =
                new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                handleStyleCategorySelection(event);
            }
        };
        styleCategoriesComposite.
                addCategorySelectionListener(styleCategoriesListener);

        // Create a scroll pane so that we can handle large quantities of
        // properties in a single category...
        scroller = new ScrolledComposite(displayArea, SWT.V_SCROLL);
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);
        scroller.setLayoutData(new GridData(GridData.FILL_BOTH));

        // create the container for the StyleCategoryComposite panels
        categoryContainer = new Composite(scroller, SWT.NONE);
        scroller.setContent(categoryContainer);

        categoryStack = new StackLayout();
        categoryStack.marginHeight = 0;
        categoryStack.marginWidth = 0;

        categoryContainer.setLayout(categoryStack);
        categoryContainer.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        styleCategoriesComposite.layout();
        categoryContainer.layout();

        // ensure that the first category is selected.
        StyleCategory[] categories = styleCategoriesComposite.getCategories();
        if (categories != null && categories.length > 0) {
            styleCategoriesComposite.selectCategory(categories[0]);
        }
    }

    /**
     * Respond to a change in the selection in the list of available
     * categories.
     * <p>When a category is selected, then if an existing property composite
     * is in place for the category it is brought to the front, otherwise a
     * new property composite is created for that category.</p>
     *
     * @param event The selection event representing the selection of a new
     *              style category
     */
    private void handleStyleCategorySelection(SelectionChangedEvent event) {
        ISelection selection = event.getSelection();
        // unfortunately the eclipse framework requires us to check that the
        // we have a selection of the correct type.
        if (selection instanceof IStructuredSelection) {
            Composite categoryComposite;
            if (selection.isEmpty()) {
                // if the selections is empty we should display an empty panel
                if (emptyCategory == null) {
                    emptyCategory = new Composite(categoryContainer, SWT.NONE);
                    emptyCategory.setBackground(getDisplay().
                            getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                }
                categoryComposite = emptyCategory;
            } else {
                IStructuredSelection structuredSelection =
                        (IStructuredSelection) selection;
                // just get the first element - there should only be one.
                StyleCategory category = (StyleCategory)
                        structuredSelection.getFirstElement();

                // see if we have a composite for this category
                categoryComposite = (PropertiesComposite)
                        categoryComposites.get(category);

                // if we do not have a category then we need to build one.
                if (categoryComposite == null) {
                    List properties = category.getProperties();
                    List propertyDescriptors = new ArrayList();
                    Iterator it = properties.iterator();

                    BeanClassDescriptor typeDescriptor = (BeanClassDescriptor)
                            PolicyModel.MODEL_DESCRIPTOR.
                            getTypeDescriptorStrict(StyleProperties.class);

                    while (it.hasNext()) {
                        StyleProperty details = (StyleProperty) it.next();
                        PropertyIdentifier identifier = ThemeModel.
                                getPropertyIdentifierForStyleProperty(details);
                        PropertyDescriptor descriptor =
                                typeDescriptor.getPropertyDescriptor(identifier);
                        propertyDescriptors.add(descriptor);
                    }

                    PropertyDescriptor[] descriptors = new PropertyDescriptor[propertyDescriptors.size()];
                    descriptors = (PropertyDescriptor[]) propertyDescriptors.toArray(descriptors);

                    // build the category
                    categoryComposite = new PropertiesComposite(categoryContainer, SWT.NONE, descriptors, context, category.isIsSynchronizable());
                    categoryComposite.setBackground(getDisplay().
                            getSystemColor(SWT.COLOR_LIST_BACKGROUND));

                    // store the category composite in the map
                    categoryComposites.put(category, categoryComposite);

                    // disable the composite if there are no rules selected
                    categoryComposite.setEnabled(styleProperties != null && isEnabled());

                    PropertiesComposite propertiesComposite = (PropertiesComposite) categoryComposite;
                    if (styleProperties != null) {
                        propertiesComposite.updateFromProxy(styleProperties);
                    }
                    propertiesComposite.addPropertiesCompositeChangeListener(new PropertiesCompositeChangeListener() {
                        public void propertyChanged(PropertiesComposite composite, PropertyDescriptor property, Object newValue) {
                            stylePropertyChanged(composite, property.getIdentifier());
                        }
                    });
                }
            }
            // display the new composite
            categoryStack.topControl = categoryComposite;

            // The scrollable area will have changed - recalculate the size of
            // the component being scrolled.
            scroller.setMinHeight(categoryStack.topControl.
                    computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
            displayArea.layout();
            categoryContainer.layout();
        } else {
            throw new IllegalArgumentException("Expected an " +
                    "IStructuredSelection but " +
                    "got a " + selection.getClass());
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        // Re-set the style properties to set the enabled state on the 
        setStyleProperties(styleProperties);
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        boolean seekingStyleProperties = true;
        int stepCount = path.getStepCount();
        for (int i = 0; i < stepCount && seekingStyleProperties; i++) {
            Step step = path.getStep(i);
            if (step instanceof PropertyStep) {
                PropertyStep property = (PropertyStep) step;
                if (Rule.STYLE_PROPERTIES.getName().equals(property.getProperty())) {
                    if ((i + 1) < stepCount) {
                        Step stylePropertyStep = path.getStep(i + 1);
                        if (stylePropertyStep instanceof PropertyStep) {
                            String propertyName = ((PropertyStep) stylePropertyStep).getProperty();
                            focusStyleProperty(propertyName);
                        }
                    }
                    seekingStyleProperties = false;
                }
            }
        }
    }

    private void focusStyleProperty(String propertyName) {
        Set styleCategories = categoryComposites.keySet();
        Iterator it = styleCategories.iterator();
        boolean seekingProperty = true;
        while (it.hasNext() && seekingProperty) {
            StyleCategory category = (StyleCategory) it.next();
            PropertiesComposite composite = (PropertiesComposite) categoryComposites.get(category);
            Set styleProperties = composite.getSupportedPropertyIdentifiers();
            Iterator propertiesIterator = styleProperties.iterator();
            while (propertiesIterator.hasNext()) {
                PropertyIdentifier identifier = (PropertyIdentifier) propertiesIterator.next();
                if (identifier.getName().equals(propertyName)) {
                    seekingProperty = false;
                    styleCategoriesComposite.selectCategory(category);
                    composite.selectProperty(identifier);
                }
            }
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/3	adrianj	VBM:2005111601 Add style rule view

 07-Dec-05	10617/4	adrianj	VBM:2005101809 Fixed conflicts

 06-Dec-05	10617/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10589/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10652/1	adrianj	VBM:2005112110 Add context menu for StyleCategoriesComposite

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10455/1	adrianj	VBM:2005112310 Post-processing for content values

 24-Nov-05	10431/1	adrianj	VBM:2005111509 Display all style properties in a category

 09-Nov-05	10197/2	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 25-Nov-05	10459/1	adrianj	VBM:2005112310 Post processing for content style property

 25-Nov-05	10455/1	adrianj	VBM:2005112310 Post-processing for content values

 24-Nov-05	10437/1	adrianj	VBM:2005111509 Display all style properties in a category

 24-Nov-05	10431/1	adrianj	VBM:2005111509 Display all style properties in a category

 10-Nov-05	10246/1	adrianj	VBM:2005110434 Allow user-friendly data entry for style properties

 09-Nov-05	10197/2	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9992/4	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 28-Oct-05	9886/2	adrianj	VBM:2005101811 New theme GUI

 31-Oct-05	9886/5	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
