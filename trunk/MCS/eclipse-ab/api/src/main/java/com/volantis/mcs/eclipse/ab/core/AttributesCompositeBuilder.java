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

package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.common.AttributeValueExchange;
import com.volantis.mcs.eclipse.common.ControlType;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.common.ResourceUnits;
import com.volantis.mcs.eclipse.common.URIFragmentExchange;
import com.volantis.mcs.eclipse.controls.ActionableHandler;
import com.volantis.mcs.eclipse.controls.CellIterations;
import com.volantis.mcs.eclipse.controls.ColorSelector;
import com.volantis.mcs.eclipse.controls.ColorSelectorFactory;
import com.volantis.mcs.eclipse.controls.ComboActionable;
import com.volantis.mcs.eclipse.controls.ComboViewer;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import com.volantis.mcs.eclipse.controls.ControlsPlugin;
import com.volantis.mcs.eclipse.controls.PackingLayout;
import com.volantis.mcs.eclipse.controls.SelectionDialogDetails;
import com.volantis.mcs.eclipse.controls.StyleSelector;
import com.volantis.mcs.eclipse.controls.TextActionable;
import com.volantis.mcs.eclipse.controls.TextButton;
import com.volantis.mcs.eclipse.controls.TimeSelector;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This class constructs groups of controls using an element name. All properties
 * for the element are determined and appropriate controls are added to the
 * AttributeComposite parent control. Note that properties may be filtered using
 * the inclusion or exclusion list.
 * Note that controls are added in the order specified in the inclusion list or
 * the order extracted from the element itself (may not be alphabetical).
 *
 * AttributesCompositeBuilider will have to cope with elements that can contain
 * different kinds of assets. For example imageComponent can contain
 * convertibleImageAsset, deviceImageAsset, and genericImageAsset. Both
 * deviceImageAsset and genericImageAsset have unique attributes that need to
 * appear in the same AttributesComposite (i.e. both device and widthHint
 * controls need to go into the AttributesComposite for imageElement). Here is
 * how to handle this:
 * <p/>
 * PropertyValueLookUp will allow calls to get the dependent elements for a
 * given element (e.g. provide imageComponent and get back a List containing
 * convertibleImageAsset, deviceImageAsset and genericImageAsset).
 * PropertyValueLookUp also converts from element name to class name so for
 * each dependent element it is possible to get the associated class. The
 * ObjectHelper is used to get the properties (i.e. attributes) of the class
 * (see EuropaComponentPanelBuilder in the pre-Triton gui). The properties for
 * all the dependent element should be merged to give the superset containing
 * all the properties in all the dependent elements. This superset represents
 * the attribute that need controls in the AttributesComposite.
 *
 * IMPORTANT: Many private methods are used through reflection. These show up
 * as not being used in Idea.
 *
 * NOTE: Only controls that need to be validated outwith DOM/XSD validation
 * are validated. Currently this amounts to only those controls
 * that are represented by policy selectors as these are used by the
 * policy creation wizards.
 *
 */
public class AttributesCompositeBuilder {

    /**
     * Singleton instance for this object.
     */
    private static AttributesCompositeBuilder singleton =
            new AttributesCompositeBuilder();

    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX =
            "AttributesCompositeBuilder.";

    /**
     * Some controls require prior knowledge of a project so we store
     * have a ProjectProvider for that purpose.
     */
    private ProjectProvider projectProvider;

    /**
     * The attribute name key used for storing the attribute name in each
     * controls data are, using ctrl.setData(ATTRIBUTE_NAME_KEY, attributeName);
     */
    private static final String ATTRIBUTE_NAME_KEY = "attribute.name";

    /**
     * An attribute value exchanger for use in URI Fragment type controls.
     */
    private static final AttributeValueExchange URI_FRAGMENT_EXCHANGE =
            new URIFragmentExchange();

    /**
     * Get the singleton instance of this class.
     */
    public static AttributesCompositeBuilder getSingleton() {
        return singleton;
    }

    /**
     * Construct a new AttributesCompositeBuilder.
     */
    private AttributesCompositeBuilder() {
    }

    /**
     * Get the resource string for the specified attribute.
     *
     * @param attribute the attribute to use to get the resource string.
     * @return the resource string for the specified attribute.
     */
    private String getResourceString(String attribute) {
        StringBuffer buffer = new StringBuffer(RESOURCE_PREFIX);
        buffer.append(attribute).append(".label");
        return ControlsMessages.getString(buffer.toString());
    }

    /**
     * Build the attribute composite using a filter on the properties.
     *
     * @param parent  The parent of the AttributesComposite. Cannot be null.
     * @param attributesDetails
     *                The details of the attributes for the AttributesComposite.
     *                Cannot be null.
     * @param project The project that may be used by some controls whilst
     *                building the attribute composite.
     * @param handler the actionable handler.
     * @return a newly created attributes composite.
     * @throws java.lang.IllegalArgumentException
     *          if either parent or attributeDetails are null or if there are no
     *          attributes obtainable from attributesDetails.
     */
    public AttributesComposite
            buildAttributesComposite(Composite parent,
                                     AttributesDetails attributesDetails,
                                     final IProject project,
                                     ActionableHandler handler)
            throws IllegalArgumentException {

        return buildAttributesComposite(parent, attributesDetails,
                new ProjectProvider() {
                    public IProject getProject() {
                        return project;
                    }
                }, handler);
    }

    /**
     * Build the attribute composite using a filter on the properties.
     *
     * @param parent  The parent of the AttributesComposite. Cannot be null.
     * @param attributesDetails
     *                The details of the attributes for the AttributesComposite.
     *                Cannot be null.
     * @param project The project that may be used by some controls whilst
     *                building the attribute composite.
     * @param packingLayout
     *                the {@link com.volantis.mcs.eclipse.controls.PackingLayout} class to use for the newly
     *                created attributes composite's layout. If this parameter
     *                is null a default GridLayout will be used.
     * @param handler the actionable handler.
     * @return a newly created attributes composite.
     * @throws java.lang.IllegalArgumentException
     *          if either parent or attributeDetails are null or if there are no
     *          attributes obtainable from attributesDetails, or packingLayout
     *          is null.
     */
    public AttributesComposite
            buildAttributesComposite(Composite parent,
                                     AttributesDetails attributesDetails,
                                     final IProject project,
                                     PackingLayout packingLayout,
                                     ActionableHandler handler)
            throws IllegalArgumentException {

        if (packingLayout == null) {
            throw new IllegalArgumentException(
                    "Packing layout may not be null.");
        }
        return buildAttributesComposite(parent, attributesDetails,
                new ProjectProvider() {
                    public IProject getProject() {
                        return project;
                    }
                }, packingLayout, handler);
    }

    /**
     * Build the attribute composite using a filter on the properties.
     *
     * @param parent  The parent of the AttributesComposite. Cannot be null.
     * @param attributesDetails
     *                The details of the attributes for the AttributesComposite.
     *                Cannot be null.
     * @param projectProvider
     *                The ProjectProvider providing the project that may be used
     *                by some controls whilst building the attribute composite.
     * @param handler the actionable handler.
     * @return a newly created attributes composite.
     * @throws java.lang.IllegalArgumentException
     *          if either parent or attributeDetails are null or if there are no
     *          attributes obtainable from attributesDetails.
     */
    public AttributesComposite
            buildAttributesComposite(Composite parent,
                                     AttributesDetails attributesDetails,
                                     ProjectProvider projectProvider,
                                     ActionableHandler handler)
            throws IllegalArgumentException {

        GridLayout gridLayout = new GridLayout(2, false);

        gridLayout.marginHeight = ControlsMessages.getInteger(RESOURCE_PREFIX +
                "marginHeight").intValue();
        gridLayout.marginWidth = ControlsMessages.getInteger(RESOURCE_PREFIX +
                "marginWidth").intValue();
        gridLayout.verticalSpacing = gridLayout.marginHeight;

        return buildAttributesComposite(parent, attributesDetails,
                projectProvider, gridLayout, handler);
    }

    /**
     * Build the attribute composite using a filter on the properties.
     *
     * Note that this method is private and all buildAttributesComposite methods
     * ultimately call this method.
     *
     * @param parent  The parent of the AttributesComposite. Cannot be null.
     * @param attributesDetails
     *                The details of the attributes for the AttributesComposite.
     *                Cannot be null.
     * @param projectProvider
     *                The ProjectProvider providing the project that may be used
     *                by some controls whilst building the attribute composite.
     * @param layout  the layout that is to be associated with the newly created
     *                attributes composite.
     * @param handler the actionable handler.
     * @return a newly created attributes composite.
     * @throws java.lang.IllegalArgumentException
     *          if either parent or attributeDetails are null or if there are no
     *          attributes obtainable from attributesDetails.
     */
    private AttributesComposite
            buildAttributesComposite(Composite parent,
                                     AttributesDetails attributesDetails,
                                     ProjectProvider projectProvider,
                                     Layout layout,
                                     ActionableHandler handler)
            throws IllegalArgumentException {

        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent.");
        }

        if (attributesDetails == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "attributesDetails.");
        }

        String[] attributes = attributesDetails.getAttributes();
        if (attributes == null || attributes.length == 0) {
            throw new IllegalArgumentException("AttributesDetails contains " +
                    "no values.");
        }

        // The project is required for device selector control (it uses the
        // project to extract the repository file name from the session).
        // It is also required for PolicySelector controls to find the root
        // IContainer.
        this.projectProvider = projectProvider;

        AttributesComposite attributesComposite = new AttributesComposite(
                parent, SWT.NONE);

        attributesComposite.setLayout(layout);

        GridData data = new GridData(GridData.FILL_BOTH);
        attributesComposite.setLayoutData(data);
        for (int i = 0; i < attributes.length; i++) {
            createAttributeControl(new ControlDetails(attributesComposite,
                    attributes[i], attributesDetails, handler));
        }

        attributesComposite.setAttributesNames(attributes);

        return attributesComposite;
    }


    /**
     * Use reflection to call the appropriate method within this class. The
     * method called should add the appropriate controls to the parent
     * composite.
     * @param controlDetails The details of the control to create.
     */
    private void createAttributeControl(
            ControlDetails controlDetails) {
        // work out what the control type is.
        ControlType controlType =
                    controlDetails.attributesDetails.getAttributeControlType(
                                controlDetails.attribute);
        // We only add a label if the control is not a CheckBox. CheckBox
        // controls add the label to the right of the control.
        if (ControlType.CHECK_BOX != controlType) {
            createAttributeLabel(controlDetails);
        }

        try {
            Class helperClass = this.getClass();
            String methodName = createMethodName(controlDetails);

            Class[] paramTypes = {ControlDetails.class};
            Object params [] = {controlDetails};
            final String name = methodName.toString();

            Method method = helperClass.getDeclaredMethod(name, paramTypes);
            method.invoke(this, params);
        } catch (Exception e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Create a method name.
     *
     * @return The newly created method name.
     */
    private String createMethodName(ControlDetails controlDetails) {
        StringBuffer methodName = new StringBuffer("create");
        String attribute = controlDetails.attribute;

        ControlType controlType = controlDetails.attributesDetails.
                getAttributeControlType(attribute);

        if (controlType != null) {
            // If we have a control type use it to create a more generic
            // method name.
            methodName.append(controlType.name);
        } else {
            // Otherwise fallback to the old fashioned approach of using
            // the name of the property for the method name.
            methodName.append(Character.toUpperCase(attribute.charAt(0))).
                    append(attribute.substring(1));
        }
        methodName.append("Control");
        return methodName.toString();
    }

    /**
     * Add the asset group name control to the parent composite.
     * @param controlDetails The details for this control.
     */
    private void createAssetGroupNameControl(ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "assetGroup");
    }

    /**
     * Add the character set control to the parent composite.
     */
    private void createCharacterSetControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the encoding control to the parent composite.
     */
    private void createEncodingControl(ControlDetails controlDetails) {
        createReadOnlyComboViewerControl(controlDetails);
    }


    /**
     * Add the device name control to the parent composite.
     */
    private void createDeviceNameControl(ControlDetails controlDetails) {
        createDeviceSelectorControl(controlDetails);
    }

    /**
     * Add the down image component name control to the parent composite.
     */
    private void createDownImageComponentNameControl(
            ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "imageComponent");
    }

    /**
     * Add the fallback audio component name control to the parent composite.
     */
    private void createFallbackAudioComponentNameControl(
            ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "audioComponent");
    }

    /**
     * Add the fallback dynamic visual component name control to the parent
     * composite.
     */
    private void createFallbackDynVisComponentNameControl(
            ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "dynamicVisualComponent");
    }

    /**
     * Add the fallback chart component name control to the parent composite.
     */
    private void createFallbackChartComponentNameControl(
            ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "chartComponent");
    }

    /**
     * Add the fallback image component name control to the parent composite.
     */
    private void createFallbackImageComponentNameControl(ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "imageComponent");
    }

    /**
     * Add the fallback text component name control to the parent composite.
     */
    private void createFallbackTextComponentNameControl(ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "textComponent");
    }

    /**
     * Add the height hint control to the parent composite.
     */
    private void createHeightHintControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the local src control to the parent composite.
     */
    private void createLocalSrcControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the location type control to the parent composite.
     */
    private void createLocationTypeControl(ControlDetails controlDetails) {
        createReadOnlyComboViewerControl(controlDetails);
    }

    /**
     * Add the image over component name control to the parent composite.
     */
    private void createOverImageComponentNameControl(ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "imageComponent");
    }

    /**
     * Add the mime type control to the parent composite.
     */
    private void createMimeTypeControl(ControlDetails controlDetails) {
        createComboViewerControl(controlDetails, true);
    }

    /**
     * Add the name control to the parent composite.
     */
    private void createNameControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the image component name control to the parent composite.
     */
    private void createNormalImageComponentNameControl(
            ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "imageComponent");
    }

    /**
     * Add the pixels depth control to the parent composite.
     */
    private void createPixelDepthControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the pixels X control to the parent composite.
     */
    private void createPixelsXControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the pixels Y control to the parent composite.
     */
    private void createPixelsYControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the prefix URL control to the parent composite.
     */
    private void createPrefixURLControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the preserve left control to the parent composite.
     */
    private void createPreserveLeftControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the preserve right control to the parent composite.
     */
    private void createPreserveRightControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the programming language control to the parent composite.
     */
    private void createProgrammingLanguageControl(ControlDetails controlDetails) {
        createComboViewerControl(controlDetails, true);
    }

    /**
     * Add the rendering control to the parent composite.
     */
    private void createRenderingControl(ControlDetails controlDetails) {
        createReadOnlyComboViewerControl(controlDetails);
    }

    /**
     * Add the type control to the parent composite.
     */
    private void createTypeControl(ControlDetails controlDetails) {
        createReadOnlyComboViewerControl(controlDetails);
    }

    /**
     * Add the image component control to the parent composite.
     */
    private void createUpImageComponentNameControl(
            ControlDetails controlDetails) {
        createPolicySelectorControl(controlDetails,
                "imageComponent");
    }

    /**
     * Add the value control to the parent composite.
     */
    private void createValueControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the value type control to the parent composite.
     */
    private void createValueTypeControl(ControlDetails controlDetails) {
        createComboViewerControl(controlDetails, false);
    }

    /**
     * Add the width hint control to the parent composite.
     */
    private void createWidthHintControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the x interval control to the parent composite.
     */
    private void createXIntervalControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the x title control to the parent composite.
     */
    private void createXTitleControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the rules control to the parent composite.
     */
    private void createYIntervalControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the y title control to the parent composite.
     */
    private void createYTitleControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the cache this policy control to the parent composite.
     */
    private void createCacheThisPolicyControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the retain during retry control to the parent composite.
     */
    private void createRetainDuringRetryControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the retry failed retrieval control to the parent composite.
     */
    private void createRetryFailedRetrievalControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the time to live control to the parent composite.
     */
    private void createTimeToLiveControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the retry interval control to the parent composite.
     */
    private void createRetryIntervalControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the retry max count control to the parent composite.
     */
    private void createRetryMaxCountControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the language field to the panel. Currently the language property is
     * not to be exposed to the user since it exists for future use so this
     * method does nothing.
     */
    private void createLanguageControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the canvas control to the parent composite.
     */
    private void createCanvasControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the default form fragment name control to the parent composite.
     */
    private void createDefaultFormFragmentNameControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the default fragment name control to the parent composite.
     */
    private void createDefaultFragmentControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the default segment name control to the parent composite.
     */
    private void createDefaultSegmentControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the destination layout control to the parent composite.
     */
    private void createDestinationLayoutControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the format count control to the parent composite.
     */
    private void createFormatCountControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the format scope control to the parent composite.
     */
    private void createFormatScopeControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the layout name control to the parent composite.
     */
    private void createLayoutNameControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the montage control to the parent composite.
     */
    private void createMontageControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the root format control to the parent composite.
     */
    private void createRootFormatControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the external style sheet URL control to the parent composite.
     */
    private void createExternalStyleSheetControl(
            ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the rule control to the parent composite.
     */
    private void createRuleControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the rule count control to the parent composite.
     */
    private void createRuleCountControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the rules control to the parent composite.
     */
    private void createRulesControl(ControlDetails controlDetails) {
        createTextControl(controlDetails);
    }

    /**
     * Add the text control to the parent composite such that illegal URI
     * characters are automatically translated into and out of the attribute.
     *
     * @param controlDetails the details for the control
     */
    private void createURIFragmentControl(ControlDetails controlDetails) {
        final Text text = new Text(controlDetails.attributesComposite,
                                   SWT.BORDER | SWT.SINGLE);
        final AttributesComposite.AttributeAccessor attributeAccessor =
                new AttributesComposite.AttributeAccessor() {
                    // javadoc inherited
                    public String getValue(String attribute) {
                        return URI_FRAGMENT_EXCHANGE.toModelForm(
                                text.getText());
                    }

                    // javadoc inherited
                    public void setValue(String attribute, String value) {
                            text.setText(URI_FRAGMENT_EXCHANGE.toControlForm(
                                    value));
                    }
                };

        configureTextControl(controlDetails, text, attributeAccessor);
    }

    /**
     * Add the text control to the attributes composite parent object for the
     * specified property name (which will be stored in the data name value
     * pair object).
     */
    private void createTextControl(ControlDetails controlDetails) {
        final Text text = new Text(controlDetails.attributesComposite,
                                   SWT.BORDER | SWT.SINGLE);
        final AttributesComposite.AttributeAccessor attributeAccessor =
                new AttributesComposite.AttributeAccessor() {
            public String getValue(String attribute) {
                return text.getText();
            }

            public void setValue(String attribute, String value) {
                text.setText(value);
            }
        };

        configureTextControl(controlDetails, text, attributeAccessor);
    }

    /**
     * Configures the specified text control with the defined attribute
     * accessor, required sizing etc. and sets up listeners to handle
     * accessibility and editing.
     *
     * @param controlDetails
     *             the details associated with the control
     * @param text the text control to be configured
     * @param attributeAccessor
     *             the attribute accessor to be associated with the text
     *             control
     */
    private void configureTextControl(
            ControlDetails controlDetails,
            final Text text,
            final AttributesComposite.AttributeAccessor attributeAccessor) {
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.widthHint = ControlsMessages.getInteger(
                "Text.textWidth").intValue();
        text.setLayoutData(textGridData);

        controlDetails.attributesComposite.
                setData(controlDetails.attribute, text);

        text.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                     attributeAccessor);

        TextModifyListener listener =
                new TextModifyListener(controlDetails.attributesComposite,
                        controlDetails.attribute);

        // Add an accessible listener
        addAccessibleListener(text, controlDetails);

        text.addListener(SWT.Modify, listener);

        text.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        associateHandlerWithText(controlDetails.handler, text);
    }

    /**
     * Add the policy selector text button to the attributes composite parent
     * object for the specified property name (which will be stored in the data
     * name value pair object).
     *
     * @param controlDetails the AttributesComposite parent object.
     */
    private void createPolicySelectorControl(final ControlDetails controlDetails) {
        AttributesDetails attrDetails = controlDetails.attributesDetails;

        String attributeType =
                attrDetails.getAttributeType(controlDetails.attribute);

        if (attributeType == null) {
            throw new IllegalStateException("Expected an attributeType for " +
                    controlDetails.attribute + " but there was none.");
        }

        createPolicySelectorControl(controlDetails, attributeType);
    }

    /**
     * Add the policy selector text button to the attributes composite parent
     * object for the specified property name (which will be stored in the data
     * name value pair object).
     *
     * @param controlDetails the AttributesComposite parent object.
     * @param attributeType the attributeType representing the policy
     */
    private void createPolicySelectorControl(final ControlDetails controlDetails,
                                             String attributeType) {
        final PolicySelector policySelector;
        FileExtension extension =
                FileExtension.getFileExtensionForPolicyType(attributeType);
        try {
            FileExtension[] extensions = new FileExtension[]{extension};
            SelectionDialogDetails selectionDialogDetails =
                    new SelectionDialogDetails(
                            attributeType,
                            ControlsMessages.getString(
                                    PolicySelector.TITLE_KEY_PREFIX +
                    attributeType),
                            ControlsMessages.getString(
                                    PolicySelector.MESSAGE_KEY_PREFIX +
                    attributeType),
                            ControlsMessages.getString(
                                    PolicySelector.EMPTY_LIST_KEY_PREFIX +
                    attributeType),
                            extensions);

            policySelector = new PolicySelector(
                    controlDetails.attributesComposite,
                    SWT.NONE, selectionDialogDetails,
                    projectProvider);

            // Create a PolicyNameValidator suitable for this control.
            PolicyNameValidator validator =
                    new PolicyNameValidator(null, extensions, false, true);

            policySelector.setData(AttributesComposite.VALIDATOR_KEY,
                    validator);

            createTextButton(policySelector, controlDetails);

        } catch (Exception e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }


    /**
     * Add the policy selector text button to the attributes composite parent
     * object for the specified property name (which will be stored in the data
     * name value pair object).
     *
     * @param controlDetails the AttributesComposite parent object.
     */
    private void createBackgroundComponentControl(
            final ControlDetails controlDetails) {
        final PolicySelector policySelector;
        String attributeType = "component";
        try {
            FileExtension[] extensions = new FileExtension[2];
            extensions[0] = FileExtension.IMAGE_COMPONENT;
            extensions[1] = FileExtension.DYNVIS_COMPONENT;
            SelectionDialogDetails selectionDialogDetails =
                    new SelectionDialogDetails(
                            attributeType,
                            ControlsMessages.getString(
                                    PolicySelector.TITLE_KEY_PREFIX +
                    attributeType),
                            ControlsMessages.getString(
                                    PolicySelector.MESSAGE_KEY_PREFIX +
                    attributeType),
                            ControlsMessages.getString(
                                    PolicySelector.EMPTY_LIST_KEY_PREFIX +
                    attributeType),
                            extensions);

            policySelector =
                    new PolicySelector(controlDetails.attributesComposite,
                            SWT.NONE, selectionDialogDetails,
                            projectProvider);

            // Create a PolicyNameValidator suitable for this control.
            PolicyNameValidator validator =
                    new PolicyNameValidator(null, extensions, false);

            policySelector.setData(AttributesComposite.VALIDATOR_KEY,
                    validator);

            createTextButton(policySelector, controlDetails);

        } catch (Exception e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Add a TextButton control to the AttributesComposite.
     * @param textButton The TextButton to add.
     * @param controlDetails The details for this control.
     */
    private void createTextButton(final TextButton textButton,
                                  ControlDetails controlDetails) {
        controlDetails.attributesComposite.
                setData(controlDetails.attribute, textButton);

        textButton.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return textButton.getValue();
                    }

                    public void setValue(String attribute, String value) {
                        textButton.setValue(value);
                    }
                });

        TextModifyListener listener =
                new TextModifyListener(controlDetails.attributesComposite,
                        controlDetails.attribute);

        // Add an accessible listener
        addAccessibleListener(textButton, controlDetails);

        textButton.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        textButton.getText().addListener(SWT.Modify, listener);

        associateHandlerWithText(controlDetails.handler, textButton.getText());

        textButton.setAccessibleName(
                getResourceString(controlDetails.attribute));
    }

    /**
     * Add the device selector text button to the attributes composite parent
     * object for the specified property name (which will be stored in the data
     * name value pair object).
     *
     */
    private void createDeviceSelectorControl(ControlDetails controlDetails) {
        final DeviceSelector deviceSelector;
        try {
            deviceSelector =
                    new DeviceSelector(controlDetails.attributesComposite,
                            SWT.SINGLE,
                            projectProvider);
            createTextButton(deviceSelector, controlDetails);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Creat an editable Combo.
     * @param controlDetails The details of the control.
     */
    private void createEditableComboControl(ControlDetails controlDetails) {
        createComboControl(controlDetails, true);
    }

    /**
     * Create a check box control.
     * @param controlDetails the details of the control.
     */
    private void createCheckBoxControl(final ControlDetails controlDetails) {

        final Button checkBox = new Button(controlDetails.attributesComposite,
                SWT.CHECK);

        GridData data = new GridData();
        data.horizontalSpan = 2;
        checkBox.setLayoutData(data);

        checkBox.setText(getResourceString(controlDetails.attribute));
        checkBox.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return checkBox.getSelection() ?
                                Boolean.TRUE.toString() :
                                Boolean.FALSE.toString();
                    }

                    public void setValue(String attribute, String value) {
                        checkBox.setSelection(Boolean.valueOf(value).booleanValue());
                    }
                });

        // Propogate the property change to the appropriate listener.
        checkBox.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Object oldValue = checkBox.getSelection() ? Boolean.FALSE.toString() : Boolean.TRUE.toString();
                Object newValue = checkBox.getSelection() ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        checkBox, controlDetails.attribute, oldValue, newValue);
                controlDetails.attributesComposite.
                        propertyChange(propertyChange);
            }
        });

        // Store the attribute name in the control's data map (test team request).
        checkBox.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute, checkBox);
    }

    /**
     * Create a colour selector control.
     * @param controlDetails The details of the control.
     */
    private void createColorSelectorControl(final ControlDetails controlDetails) {

        final ColorSelector colourSelector = (ColorSelector)
                new ColorSelectorFactory().buildValidatedTextControl(
                        controlDetails.attributesComposite, SWT.NONE);

        colourSelector.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return colourSelector.getValue();
                    }

                    public void setValue(String attribute, String value) {
                        colourSelector.setValue(value);
                    }
                });

        // Add the modify listener to this control.
        TextModifyListener listener =
                new TextModifyListener(controlDetails.attributesComposite,
                        controlDetails.attribute);

        colourSelector.addListener(SWT.Modify, listener);


        // Store the attribute name in the control's data map (test team request).
        colourSelector.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // Add an accessible listener
        addAccessibleListener(colourSelector, controlDetails);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute,
                colourSelector);

        associateHandlerWithCombo(controlDetails.handler, colourSelector.getCombo());
    }

    /**
     * Create the layout number of units control
     * @param controlDetails the Control Details.
     */
    private void createLayoutNumberUnitsControl(final ControlDetails controlDetails) {
        List list = new ArrayList();
        list.add(ResourceUnits.PIXEL);
        list.add(ResourceUnits.PERCENT);
        final LayoutNumberUnit control = new LayoutNumberUnit(
                controlDetails.attributesComposite, list);

        final String supplementary = controlDetails.attributesDetails.
                getSupplementaryValue(controlDetails.attribute);

        control.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        String result = null;
                        if (attribute.equals(supplementary)) {
                            result = control.getUnit();
                        } else {
                            result = control.getNumber();
                        }
                        return result;
                    }

                    public void setValue(String attribute, String value) {
                        if (attribute.equals(supplementary)) {
                            control.setUnit(value);
                        } else {
                            control.setNumber(value);
                        }
                    }
                });

        // Listen to unit changes.
        control.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                Object oldValue = null;
                Object newValue = control.getUnit();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        control, supplementary, oldValue, newValue);
                controlDetails.attributesComposite.propertyChange(propertyChange);
            }
        });

        // Listen to value changes.
        control.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Object oldValue = null;
                Object newValue = control.getNumber();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        control, controlDetails.attribute, oldValue, newValue);
                controlDetails.attributesComposite.
                        propertyChange(propertyChange);
            }
        });


        // Add an accessible listener
        addAccessibleListener(control, controlDetails);

        // Store the attribute name in the control's data map (test team request).
        control.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute, control);
        controlDetails.attributesComposite.setData(supplementary, control);

        associateHandlerWithText(controlDetails.handler, control.getText());
    }

    /**
     * Create the text definition control.
     * @param controlDetails the controls details.
     */
    private void createTextDefinitionControl(final ControlDetails controlDetails) {
        final TextDefinition textDefinition = new TextDefinition(
                controlDetails.attributesComposite, SWT.NONE, projectProvider);

        textDefinition.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return textDefinition.getText();
                    }

                    public void setValue(String attribute, String value) {
                        textDefinition.setText(value);
                    }
                });

        // Add a modify listener on the text control.
        textDefinition.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Object oldValue = null;
                Object newValue = textDefinition.getText();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        textDefinition, controlDetails.attribute, oldValue, newValue);
                controlDetails.attributesComposite.
                        propertyChange(propertyChange);
            }
        });

        // Add an accessible listener
        addAccessibleListener(textDefinition, controlDetails);

        // Store the attribute name in the control's data map (test team request).
        textDefinition.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute,
                textDefinition);

        associateHandlerWithText(controlDetails.handler, textDefinition.getTextField());

        String name = getResourceString(controlDetails.attribute);
        textDefinition.setAccessibleName(name);
    }

    /**
     * Create the time selector control.
     * @param controlDetails the controls details.
     */
    private void createTimeSelectorControl(ControlDetails controlDetails) {
        final TimeSelector selector = new TimeSelector(
                controlDetails.attributesComposite, SWT.NONE);

        createTextButton(selector, controlDetails);

        selector.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return selector.getText().getText();
                    }

                    public void setValue(String attribute, String value) {
                        selector.getText().setText(value);
                    }
                });

        // Add a text modify listener on the text control.
        TextModifyListener listener =
                new TextModifyListener(controlDetails.attributesComposite,
                        controlDetails.attribute);

        selector.getText().addListener(SWT.Modify, listener);

        // Add an accessible listener
        addAccessibleListener(selector, controlDetails);

        // Store the attribute name in the control's data map (test team request).
        selector.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute,
                selector);

    }

    /**
     * Create the style selector control.
     * @param controlDetails the controls details.
     */
    private void createStyleSelectorControl(ControlDetails controlDetails) {
        final StyleSelector selector = new StyleSelector(
                controlDetails.attributesComposite, SWT.NONE);

        createTextButton(selector, controlDetails);

        selector.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return selector.getText().getText();
                    }

                    public void setValue(String attribute, String value) {
                        selector.getText().setText(value);
                    }
                });

        // Add a text modify listener on the text control.
        TextModifyListener listener =
                new TextModifyListener(controlDetails.attributesComposite,
                        controlDetails.attribute);

        selector.getText().addListener(SWT.Modify, listener);

        // Store the attribute name in the control's data map (test team request).
        selector.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // Add an accessible listener
        addAccessibleListener(selector, controlDetails);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute,
                selector);
    }

    /**
     * Create the cell iterations control.
     * @param controlDetails
     */
    private void createCellIterationsControl(final ControlDetails controlDetails) {
        final CellIterations cellIterations = new CellIterations(
                controlDetails.attributesComposite);

        final String supplementary = controlDetails.attributesDetails.
                getSupplementaryValue(controlDetails.attribute);

        cellIterations.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {

                    public String getValue(String attribute) {
                        String result;
                        if (attribute.equals(supplementary)) {
                            result = cellIterations.getIterationsQualifier();
                        } else {
                            result = cellIterations.getIterations();
                        }
                        return result;
                    }

                    public void setValue(String attribute, String value) {
                        if (attribute.equals(supplementary)) {
                            cellIterations.setIterationQualifier(value);
                        } else {
                            cellIterations.getText().setText(value);
                        }
                    }
                });

        cellIterations.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                Object oldValue = null;
                Object newValue = cellIterations.getIterationsQualifier();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        cellIterations, supplementary, oldValue, newValue);
                controlDetails.attributesComposite.propertyChange(propertyChange);
            }
        });

        // Add an accessible listener
        addAccessibleListener(cellIterations, controlDetails);

        // Add a modify listener to this control (for the text value).
        cellIterations.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Object oldValue = null;
                Object newValue = cellIterations.getIterations();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        cellIterations, controlDetails.attribute, oldValue, newValue);
                controlDetails.attributesComposite.
                        propertyChange(propertyChange);
            }
        });

        // Store the attribute name in the control's data map (test team request).
        cellIterations.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        // For this attribute composite, associate the attribute with the control
        // so that setting the visibility on or off may be easily achieved.
        // See AttributesComposite.setVisible(..)
        controlDetails.attributesComposite.setData(controlDetails.attribute,
                cellIterations);

        // Store the rows and columns controls too (used for updating the
        // iteration qualifier).
        if (supplementary != null) {
            controlDetails.attributesComposite.setData(supplementary, cellIterations);
        }

        associateHandlerWithText(controlDetails.handler, cellIterations.getText());
        associateHandlerWithCombo(controlDetails.handler, cellIterations.getCombo());
    }

    /**
     * Creat a read only Combo.
     * @param controlDetails The details of the control.
     */
    private void createReadOnlyComboControl(ControlDetails controlDetails) {
        createComboControl(controlDetails, false);
    }

    /**
     * Creat an editable Combo.
     * @param controlDetails The details of the control.
     */
    private void createEditableComboViewerControl(ControlDetails controlDetails) {
        createComboViewerControl(controlDetails, true);
    }

    /**
     * Creat a read only Combo.
     * @param controlDetails The details of the control.
     */
    private void createReadOnlyComboViewerControl(ControlDetails controlDetails) {
        createComboViewerControl(controlDetails, false);
    }

    /**
     * Add a combo box to the attributes composite parent object for the
     * specified property name (which will be stored in the data name value pair
     * object).
     *
     * @param editable     true if this combo should be editable, false
     *                     otherwise.
     */
    private void createComboControl(final ControlDetails controlDetails,
                                    boolean editable) {
        AttributesDetails attrDetails = controlDetails.attributesDetails;
        Object[] items =
                attrDetails.
                getAttributeValueSelection(controlDetails.attribute);

        int style = editable ? SWT.DROP_DOWN : (SWT.READ_ONLY | SWT.DROP_DOWN);
        final Combo combo = new Combo(controlDetails.attributesComposite, style);
        for (int i = 0; i < items.length; i++) {
            combo.add(items[i].toString());
        }

        controlDetails.attributesComposite.
                setData(controlDetails.attribute, combo);

        combo.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return combo.getText();
                    }

                    public void setValue(String attribute, String value) {
                        combo.setText(value);
                    }
                });

        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                Object oldValue = null;
                Object newValue = combo.getItem(combo.getSelectionIndex());
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        combo, controlDetails.attribute, oldValue, newValue);
                controlDetails.attributesComposite.propertyChange(propertyChange);
            }
        });

        // Add an accessible listener
        addAccessibleListener(combo, controlDetails);

        GridData comboGridData = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(comboGridData);

        combo.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        associateHandlerWithCombo(controlDetails.handler, combo);
    }

    /**
     * Add a ComboViewer to the attributes composite parent object for the
     * specified property name (which will be stored in the data name value
     * pair object).
     *
     * @param editable true if this combo should be editable; false
     * otherwise.
     */
    private void createComboViewerControl(final ControlDetails controlDetails,
                                          boolean editable) {
        AttributesDetails attrDetails = controlDetails.attributesDetails;
        PresentableItem[] items =
                attrDetails.
                getAttributePresentableItems(controlDetails.attribute);

        // @todo there is a problem on windows that results in editable combo viewers behaving incorrectly.
        // @todo this will be fixed.
        //int style = editable ? SWT.DROP_DOWN : (SWT.READ_ONLY | SWT.DROP_DOWN);
        int style = (SWT.READ_ONLY | SWT.DROP_DOWN);
        final ComboViewer combo =
                new ComboViewer(controlDetails.attributesComposite, style,
                        items);

        controlDetails.attributesComposite.
                setData(controlDetails.attribute, combo);

        combo.setData(AttributesComposite.ATTRIBUTE_ACCESSOR_KEY,
                new AttributesComposite.AttributeAccessor() {
                    public String getValue(String attribute) {
                        return (String) combo.getValue();
                    }

                    public void setValue(String attribute, String value) {
                        combo.getCombo().deselectAll();
                        combo.setValue(value);
                    }
                });

        combo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Object oldValue = null;
                Object newValue = combo.getValue();
                PropertyChangeEvent propertyChange = new PropertyChangeEvent(
                        combo, controlDetails.attribute, oldValue, newValue);
                controlDetails.attributesComposite.
                        propertyChange(propertyChange);
            }
        });

        // Add an accessible listener
        addAccessibleListener(combo, controlDetails);

        GridData comboGridData = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(comboGridData);

        combo.setData(ATTRIBUTE_NAME_KEY, controlDetails.attribute);

        associateHandlerWithCombo(controlDetails.handler, combo.getCombo());
    }

    /**
     * Associate the action handler with the combo control.
     * @param handler the action handler to associated with the combo control.
     * @param combo the combo control used in the association.
     */
    private void associateHandlerWithCombo(ActionableHandler handler, Combo combo) {
        if (handler != null) {
            handler.addControl(new ComboActionable(combo));
        }
    }

    /**
     * Associate the action handler with the text control.
     * @param handler the action handler to associated with the text control.
     * @param text the combo control used in the association.
     */
    private void associateHandlerWithText(ActionableHandler handler, Text text) {
        if (handler != null) {
            handler.addControl(new TextActionable(text));
        }
    }

    /**
     * All labels are added via this method.<p>
     *
     * We don't create labels for the checkbox controls (the actual checkbox
     * button has its own label). Not creating a label here  means that we
     * cannot expect a control to have a matching label in {@link
     * AttributesComposite#setVisible} and have to check for null labels in that
     * method.
     *
     * @param controlDetails the control details instance.
     */
    private void createAttributeLabel(ControlDetails controlDetails) {

        ControlType type = controlDetails.attributesDetails.
                getAttributeControlType(controlDetails.attribute);
        if (type != ControlType.CHECK_BOX) {
            Label label = new Label(controlDetails.attributesComposite, SWT.NONE);
            label.setText(getResourceString(controlDetails.attribute));
            label.setData(AttributesComposite.LABEL_KEY, controlDetails.attribute);
        }
    }

    /**
     * Adds an {@link AccessibleListener} to the specified control. The name
     * to be returned by the listener is as specified by the {@link
     * ControlDetails} argument.
     * @param cont The control for which the listener should be set
     * @param det The details for the control
     */
    private void addAccessibleListener(Control cont, ControlDetails det) {
        AccessibleListener listener =
                new AttributesControlAccessibleListener(det);
        cont.getAccessible().addAccessibleListener(listener);
    }

    /**
     * A simple {@link AccessibleListener} implementation for providing
     * accessibility information for generated controls.
     */
    private class AttributesControlAccessibleListener
            extends AccessibleAdapter {
        /**
         * The name to be returned by this listener.
         */
        private String name;

        // Javadoc inherited
        public void getName(AccessibleEvent ae) {
            ae.result = name;
        }

        /**
         * Initialise the name from the {@link ControlDetails} object.
         * @param det The ControlDetails for the component this listener is
         *            associated with
         */
        public AttributesControlAccessibleListener(ControlDetails det) {
            StringBuffer bufferedName =
                    new StringBuffer(getResourceString(det.attribute));
            // Trim ampersands where appropriate
            int pos = bufferedName.length();
            boolean lastWasAmpersand = false;
            while ((pos) > 0) {
                pos -= 1;
                if (bufferedName.charAt(pos) == '&') {
                    if (lastWasAmpersand) {
                        lastWasAmpersand = false;
                    } else {
                        bufferedName.deleteCharAt(pos);
                        lastWasAmpersand = true;
                    }
                } else {
                    lastWasAmpersand = false;
                }
            }
            name = bufferedName.toString();
        }
    }

    /**
     * The listener that will forward property change events to the
     * AttributesComposite parent object based on text modify events.
     *
     * NOTE: A ModifyListener is not used here because for some reason
     * it caused a NullPointerException in TypedListener at line 183
     * indicating that there was no listener! ???
     */
    private class TextModifyListener
            implements Listener {
        private final AttributesComposite attributesComposite;
        private final String attribute;

        public TextModifyListener(AttributesComposite parent,
                                  String attribute) {
            this.attributesComposite = parent;
            this.attribute = attribute;
        }

        public void handleEvent(Event e) {
            Object value = attributesComposite.getAttributeValue(attribute);
            PropertyChangeEvent event = new PropertyChangeEvent(
                    attributesComposite.getData(attribute), attribute, null,
                    value);
            attributesComposite.propertyChange(event);
        }
    }


    /**
     * Container class for passing around information to control creation
     * methods in a generic fashion.
     */
    private static class ControlDetails {
        /**
         * The parent Composite for the Control.
         */
        final AttributesComposite attributesComposite;

        /**
         * The attribute that the Control will represent.
         */
        final String attribute;

        /**
         * The AttributeDetails associated with the attribute and control.
         */
        final AttributesDetails attributesDetails;

        /**
         * The Actionable handler that is used to associate actions to the
         * control(s) created.
         */
        final ActionableHandler handler;

        /**
         * Constructor for the control details.
         *
         * @param parent    the parent attributes composite.
         * @param attribute the attributes string.
         * @param attributesDetails
         *                  the attributes details.
         * @param handler   the action handler.
         */
        ControlDetails(AttributesComposite parent,
                       String attribute,
                       AttributesDetails attributesDetails,
                       ActionableHandler handler) {
            this.attributesComposite = parent;
            this.attribute = attribute;
            this.attributesDetails = attributesDetails;
            this.handler = handler;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	10124/1	adrianj	VBM:2005110305 GUI support for preserveLeft/preserveRight image properties

 03-Nov-05	10121/1	adrianj	VBM:2005110305 Add GUI support for preserveLeft/preserveRight properties

 03-Nov-05	10112/1	adrianj	VBM:2005110305 Add support for preserve left/right in GUI

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 16-Mar-05	7426/1	philws	VBM:2004121405 Port URI fragment asset value encoding and decoding from 3.3

 15-Mar-05	7374/1	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 07-Jan-05	6603/1	adrianj	VBM:2004120801 Added names for accessible components

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 08-Nov-04	6118/1	adrianj	VBM:2004102601 Add AccessibleListener for AttributesCompositeBuilder controls

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 04-Aug-04	5022/4	doug	VBM:2004073009 Added a new Import Parent Device Theme control to device theme editor

 04-Aug-04	5022/2	doug	VBM:2004073009 Added a new Import Parent Device Theme control to device theme editor

 18-May-04	4231/4	tom	VBM:2004042704 rework for 2004042704

 29-Apr-04	4072/1	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 19-Mar-04	3471/1	byron	VBM:2004030504 Component Wizard does not add / to front of fallback components

 05-Mar-04	3332/1	byron	VBM:2004020902 Components: Value Type is not updated for secondary script asset

 23-Feb-04	3057/3	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 18-Feb-04	3068/3	allan	VBM:2004021115 Validate fallback extensions in wizards.

 17-Feb-04	3066/3	byron	VBM:2004021707 PackingLayout is creating invisible controls in AttributesComposite - updated constructors

 17-Feb-04	3066/1	byron	VBM:2004021707 PackingLayout is creating invisible controls in AttributesComposite

 13-Feb-04	2891/7	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - take 2

 13-Feb-04	2891/4	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 13-Feb-04	2985/5	allan	VBM:2004012803 Fix for null project in ProjectProviders

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 10-Feb-04	2857/4	doug	VBM:2003112711 Made ComboViewer always read only

 09-Feb-04	2913/1	philws	VBM:2004020801 Change device selection box to single selection

 06-Feb-04	2870/1	pwells	VBM:2004020216 layout: Error when adding a pane to a spatial iterator

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 23-Jan-04	2728/1	byron	VBM:2004012301 Miscellaneous bug fixes cell iterations/listeners/enablement/etc

 22-Jan-04	2540/5	byron	VBM:2003121505 Added main formats attribute page

 06-Jan-04	2412/4	allan	VBM:2004010407 Fixed merge conflicts.

 06-Jan-04	2412/1	allan	VBM:2004010407 Fixed dirty status handling when switching editor page.

 06-Jan-04	2323/2	doug	VBM:2003120701 Added better validation error messages

 05-Jan-04	2393/5	pcameron	VBM:2004010204 Programming language uses an editable ComboViewer

 05-Jan-04	2393/2	pcameron	VBM:2004010204 ComboViewer now also uses ModifyListeners

 05-Jan-04	2386/1	byron	VBM:2004010206 Ensure Programming Language displays as an editable combo box

 02-Jan-04	2332/2	richardc	VBM:2003122902 Property name changes and associated knock-ons

 29-Dec-03	2258/2	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 23-Dec-03	2279/1	richardc	VBM:2003121722 text+script component have valueType=url or literal (combo)

 17-Dec-03	2213/4	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 16-Dec-03	2213/2	allan	VBM:2003121401 More editors and fixes for presentable values.

 15-Dec-03	2208/6	allan	VBM:2003121201 Reword issues.

 14-Dec-03	2208/3	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 28-Nov-03	2013/6	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 27-Nov-03	2013/4	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 24-Nov-03	1825/6	byron	VBM:2003092601 Updated testcase - and fixed minor resource text width bug

 19-Nov-03	1878/4	richardc	VBM:2003110901 Fixed bug in setInput and 2 other minor bugs

 18-Nov-03	1878/2	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 17-Nov-03	1909/9	allan	VBM:2003102405 Rework issues.

 16-Nov-03	1909/7	allan	VBM:2003102405 All policy wizards.

 16-Nov-03	1909/2	allan	VBM:2003102405 Done Image Component Wizard.

 15-Nov-03	1825/4	byron	VBM:2003092601 Create generic policy property composite

 15-Nov-03	1825/1	byron	VBM:2003092601 Create generic policy property composite

 ===========================================================================
*/
