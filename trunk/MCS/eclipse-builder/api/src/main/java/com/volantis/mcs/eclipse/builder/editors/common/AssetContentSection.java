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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.LayoutModificationListener;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyModelSet;
import com.volantis.mcs.eclipse.builder.editors.themes.PolicySelectorBrowseAction;
import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.policies.variants.layout.LayoutContentBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.mcs.themes.model.ThemeModel;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;

/**
 * Section containing the asset content.
 */
public class AssetContentSection extends AssetEditorSection {
    /**
     * Common prefix for all resources in this section.
     */
    private static final String RESOURCE_PREFIX = "AssetContentSection.";

    /**
     * Factory for creating new policy model objects.
     */
    private static final InternalPolicyFactory POLICY_FACTORY =
            (InternalPolicyFactory)PolicyFactory.getDefaultInstance();

    /**
     * The label for the default fragment entry field.
     */
    private static final String FRAGMENT_LABEL_TEXT = EditorMessages.getString(
            RESOURCE_PREFIX + "defaultFragment.label");

    /**
     * The label for the default segment entry field.
     */
    private static final String SEGMENT_LABEL_TEXT = EditorMessages.getString(
            RESOURCE_PREFIX + "defaultSegment.label");

    /**
     * A visitor for identifying the values available for default segments or
     * fragments.
     */
    private static final DefaultTargetFormatVisitor
            DEFAULT_TARGET_FORMAT_VISITOR = new DefaultTargetFormatVisitor();

    /**
     * Browse action for selecting policies.
     */
    PolicySelectorBrowseAction ASSET_GROUP_BROWSE_ACTION =
            new PolicySelectorBrowseAction(FileExtension.ASSET_GROUP);

    private PolicyEditorContext context;

    private boolean providesURL;
    private boolean providesEmbedded;
    private boolean providesNull;
    private boolean providesSequence;
    private boolean providesTheme;
    private boolean providesLayout;

    private boolean handlingEvents = true;

    private InteractionEventListener readOnlyListener =
            new InteractionEventListenerAdapter() {
                public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                    if (event.isOriginator ()) {
                        setEnabled(!event.isReadOnly());
                    }
                }
            };

    /**
     * A collection of controls that are only enabled if embedded data is
     * specified.
     */
    private Collection embeddedControls = new ArrayList();

    /**
     * A collection of controls that are only enabled if URL data can be edited.
     */
    private Collection urlControls = new ArrayList();

    /**
     * A collection of controls that are always enabled.
     */
    private Collection miscControls = new ArrayList();

    /**
     * A collection of controls that are only enabled if Theme data can be
     * edited.
     */
    private Collection themeControls = new ArrayList();

    /**
     * A collection of controls that are enabled if Layout data can be edited
     * for a montage layout.
     */
    private Collection montageLayoutControls = new ArrayList();

    /**
     * A collection of controls that are enabled if Layout data can be edited
     * for a canvas layout.
     */
    private Collection canvasLayoutControls = new ArrayList();

    /**
     * The checkbox used to indicate null content.
     */
    private Button nullCheckbox;

    /**
     * The checkbox used to indicate embedded content.
     */
    private Button embeddedCheckbox;

    /**
     * The checkbox used to indicate a parent theme should be imported.
     */
    private Button importParentCheckbox;

    /**
     * A text entry field for URL content.
     */
    private Text urlText;

    /**
     * A text entry area for embedded content.
     */
    private Text embeddedText;

    /**
     * A text entry field for referencing a base URL policy.
     */
    private Text baseURLText;

    /**
     * A combo viewer for the default fragment on a canvas layout.
     */
    private ComboViewer defaultFragmentCombo;

    /**
     * A combo viewer for the default segment on a montage layout.
     */
    private ComboViewer defaultSegmentCombo;

    /**
     * A layout modification listener to refresh the default targets (fragment
     * and segment).
     */
    private LayoutModificationListener layoutModificationListener;

    private PropertiesComposite composite;

    public AssetContentSection(Composite parent, int style,
                               EditorContext context) {
        super(parent, style, RESOURCE_PREFIX);
        this.context = (PolicyEditorContext)context;
        createDisplayArea();

        this.context.addSelectedVariantListener(new BuilderSelectionListener() {
            public void selectionMade(BuilderSelectionEvent event) {
                if (event.getOldSelection() != null) {
                    ((Proxy) event.getOldSelection()).removeListener(readOnlyListener);
                }

                if (event.getSelection() == null) {
                    setNullCheckbox(false);
                    setEmbeddedCheckbox(false);
                    setEmbeddedText("");
                    setURLText("");
                    setEnabled(false);
                } else {
                    BeanProxy selectedVariant = (BeanProxy) event.getSelection();
                    selectedVariant.addListener(readOnlyListener, false);
                    setDisplayContent(selectedVariant);
                    setEnabled(!selectedVariant.isReadOnly());
                }
            }
        });

        BeanProxy selectedVariant = this.context.getSelectedVariant();
        setEnabled(selectedVariant != null && !selectedVariant.isReadOnly());
    }

    /**
     * Sets the enabled state of all controls handling embedded content types.
     *
     * @param enabled True to enable the controls, false to disable
     */
    private void setEmbeddedEnabled(boolean enabled) {
        if (providesEmbedded) {
            setCollectionEnabled(embeddedControls, enabled);
        }
    }

    /**
     * Sets the enabled state of all controls handling URL content types.
     *
     * @param enabled True to enable the controls, false to disable
     */
    private void setURLEnabled(boolean enabled) {
        if (providesURL) {
            setCollectionEnabled(urlControls, enabled);
            composite.setPropertyEnabled(PolicyModel.BASE_LOCATION, enabled);
        }
    }

    /**
     * Sets the enabled state of all controls handling Layout content types.
     * If the layout is null, disable them.
     *
     * @param enabled True to enable the controls, false to disable
     */
    private void setLayoutEnabled(boolean enabled) {
        if (providesLayout) {
            // Retrieve the layout type - canvas, montage or null.
            InternalLayoutContentBuilder content =
                    (InternalLayoutContentBuilder) getModelContent();
            LayoutType layoutType = null;
            if (content != null) {
                Layout layout = content.getLayout();
                if (layout != null) {
                    layoutType = layout.getType();
                }
            }

            if (layoutType == LayoutType.CANVAS) {
                // The variant is a canvas layout - disable montage controls
                // regardless of state and enable canvas ones as directed
                setCollectionEnabled(montageLayoutControls, false);
                setCollectionEnabled(canvasLayoutControls, enabled);
            } else if (layoutType == LayoutType.MONTAGE) {
                // The variant is a montage layout - disable canvas controls
                // regardless of state and enable montage ones as directed
                setCollectionEnabled(canvasLayoutControls, false);
                setCollectionEnabled(montageLayoutControls, enabled);
            } else {
                // The layout type was null, so nothing is enabled regardless
                // of the value of the enabled flag.
                setCollectionEnabled(canvasLayoutControls, false);
                setCollectionEnabled(montageLayoutControls, false);
            }
        }
    }

    /**
     * Sets the enabled state of all controls handling Theme content types.
     *
     * @param enabled True to enable the controls, false to disable
     */
    private void setThemeEnabled(boolean enabled) {
        if (providesTheme) {
            setCollectionEnabled(themeControls, enabled);
        }
    }

    /**
     * Sets the enabled state of all controls handling miscellaneous content types
     *
     * @param enabled True to enable the controls, false to disable
     */
    private void setMiscEnabled(boolean enabled) {
        setCollectionEnabled(miscControls, enabled);
    }

    /**
     * Sets the enabled state for all controls within a collection.
     *
     * @param collection A collection of {@link Control} objects
     * @param enabled The new enabled state
     */
    private void setCollectionEnabled(Collection collection, boolean enabled) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Control control = (Control) it.next();
            control.setEnabled(enabled);
        }
    }

    /**
     * Create the controls that will be used to display and edit content.
     */
    private void createDisplayArea() {
        checkContentTypes();
        Composite displayArea = createDefaultDisplayArea();

        GridLayout layout = new GridLayout(2, false);
        displayArea.setLayout(layout);


        // Create the PropertiesComposite.
        composite = new PropertiesComposite(displayArea, SWT.NORMAL, context);
        composite.setSuppressEventsWhenDisabled(true);

        GridData data = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(data);

        if (providesNull) {
            addNullCheckBox(composite);
        }

        ModifyListener updateContentOnModification = new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                updateContent();
            }
        };

        if (providesEmbedded) {
            addEmbeddedContent(composite, updateContentOnModification);
        }

        if (providesTheme) {
            addThemeContent(composite);
        }

        if (providesLayout) {
            addLayoutContent(composite, updateContentOnModification);
        }

        if (providesURL || providesSequence) {
            addURL(composite, updateContentOnModification);
        }

        composite.addPropertiesCompositeChangeListener(
                new PropertiesCompositeChangeListener() {
                    public void propertyChanged(PropertiesComposite composite,
                                                PropertyDescriptor property,
                                                Object newValue) {
                        updateContent();
                    }
                });
        // Set the color and enabled state of the properties composite after
        // all children have been added.
        setDefaultColour(composite);

        // Eclipse v3.1.x and later require the composite to be enabled (and
        // therefore in the list of enableable controls) in order for the child
        // controls to be considered to be enabled.
        composite.setEnabled(true);

    }

    /**
     * Add the controls for specifying layout-specific content properties.
     *
     * @param displayArea The properties composite containing the controls
     * @param updateContentOnModification A modify listener to notify when the
     *                                    values change
     */
    private void addLayoutContent(PropertiesComposite displayArea,
        ModifyListener updateContentOnModification) {

        defaultFragmentCombo = addComboViewer(displayArea, FRAGMENT_LABEL_TEXT,
                updateContentOnModification, canvasLayoutControls,
                new LayoutDefaultTargetContentProvider(
                        DefaultTargetType.FRAGMENT));

        defaultSegmentCombo = addComboViewer(displayArea, SEGMENT_LABEL_TEXT,
                updateContentOnModification, montageLayoutControls,
                new LayoutDefaultTargetContentProvider(
                        DefaultTargetType.SEGMENT));

        layoutModificationListener = new LayoutModificationListener() {
            public void layoutModified(Layout layout) {
                refreshTargets();
            }
        };
    }

    /**
     * Returns a layout modification listener that refreshes the default targets
     * on layout change.
     *
     * @return A layout modification listener, or null if this section does not
     *         support layouts.
     */
    public LayoutModificationListener getLayoutModificationListener() {
        return layoutModificationListener;
    }

    /**
     * Add a combo viewer to a parent composite.
     *
     * @param parent The parent composite
     * @param labelText The text for the associated label
     * @param modifyListener A listener to notify on changes to the data
     * @param controls A list to which created controls should be added
     * @param contentProvider The content provider for the combo box
     * @return the newly created ComboViewer control
     */
    private ComboViewer addComboViewer(Composite parent, String labelText,
                                ModifyListener modifyListener,
                                Collection controls,
                                IContentProvider contentProvider) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(labelText);
        setDefaultColour(label);
        controls.add(label);

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        final ComboViewer text = new ComboViewer(parent, SWT.BORDER);
        text.setContentProvider(contentProvider);
        controls.add(text.getControl());

        data = new GridData(GridData.FILL_HORIZONTAL);
        text.getControl().setLayoutData(data);
        text.getCombo().addModifyListener(modifyListener);
        return text;
    }

    private void addThemeContent(PropertiesComposite displayArea) {

        importParentCheckbox = new Button(displayArea, SWT.CHECK);
        setDefaultColour(importParentCheckbox);
        themeControls.add(importParentCheckbox);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        importParentCheckbox.setLayoutData(data);
        importParentCheckbox.setText(EditorMessages.getString(RESOURCE_PREFIX + "importParent.label"));

        importParentCheckbox.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                updateContent();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                updateContent();
            }
        });

    }

    private void addURL(PropertiesComposite displayArea,
        ModifyListener updateContentOnModification) {
        GridLayout layout;
        Label urlLabel = new Label(displayArea, SWT.NORMAL);
        setDefaultColour(urlLabel);
        urlControls.add(urlLabel);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        urlLabel.setLayoutData(data);
        urlLabel.setText(EditorMessages.getString(RESOURCE_PREFIX + "urlContent.label"));

        urlText = new Text(displayArea, SWT.BORDER);
        urlControls.add(urlText);
        data = new GridData(GridData.FILL_HORIZONTAL);
        urlText.setLayoutData(data);

        urlText.addModifyListener(updateContentOnModification);

        Label baseURLLabel = new Label(displayArea, SWT.NORMAL);
        setDefaultColour(baseURLLabel);
        urlControls.add(baseURLLabel);
        data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        baseURLLabel.setLayoutData(data);
        baseURLLabel.setText(EditorMessages.getString(RESOURCE_PREFIX + "baseURL.label"));

        final Composite inputComposite = new Composite(displayArea, SWT.NORMAL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        inputComposite.setLayoutData(data);
        setDefaultColour(inputComposite);

        layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        inputComposite.setLayout(layout);

        baseURLText = new Text(inputComposite, SWT.BORDER);
        urlControls.add(baseURLText);
        data = new GridData(GridData.FILL_HORIZONTAL);
        baseURLText.setLayoutData(data);

        baseURLText.addModifyListener(updateContentOnModification);

        Button browsePolicies = new Button(inputComposite, SWT.NONE);
        urlControls.add(browsePolicies);
        data = new GridData(GridData.HORIZONTAL_ALIGN_END);
        browsePolicies.setLayoutData(data);
        browsePolicies.setText(EditorMessages.getString(RESOURCE_PREFIX + "browsePolicies.button"));

        browsePolicies.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                browsePolicies();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                browsePolicies();
            }

            private void browsePolicies() {
                baseURLText.setText(ASSET_GROUP_BROWSE_ACTION.doBrowse(baseURLText.getText(), inputComposite, context));
            }
        });

        BeanClassDescriptor bcd = (BeanClassDescriptor) PolicyModel.MODEL_DESCRIPTOR.getTypeDescriptorStrict(BaseURLPolicyBuilder.class);
        PropertyDescriptor[] properties = new PropertyDescriptor[] {
            bcd.getPropertyDescriptor(PolicyModel.BASE_LOCATION)
        };

        Map comboDescriptors = ComboDescriptorUtil.getBaseLocationDescriptors();

        displayArea.addProperties(properties, false, comboDescriptors, null);
    }


    private void addEmbeddedContent(PropertiesComposite displayArea,
        ModifyListener updateContentOnModification) {
        embeddedCheckbox = new Button(displayArea, SWT.CHECK);
        setDefaultColour(embeddedCheckbox);
        miscControls.add(embeddedCheckbox);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        embeddedCheckbox.setText(EditorMessages.getString(RESOURCE_PREFIX + "embeddedContent.label"));
        embeddedCheckbox.setLayoutData(data);

        embeddedCheckbox.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                if (providesNull) {
                    nullCheckbox.setSelection(false);

                }
                updateVariantType();
                updateContent();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                if (providesNull) {
                    nullCheckbox.setSelection(false);
                }
                updateVariantType();
                updateContent();
            }
        });

        embeddedText = new Text(displayArea, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        embeddedControls.add(embeddedText);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 100;
        embeddedText.setLayoutData(data);

        embeddedText.addModifyListener(updateContentOnModification);
    }

    private void addNullCheckBox(PropertiesComposite displayArea) {
        nullCheckbox = new Button(displayArea, SWT.CHECK);
        setDefaultColour(nullCheckbox);
        miscControls.add(nullCheckbox);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        nullCheckbox.setLayoutData(data);
        nullCheckbox.setText(EditorMessages.getString(RESOURCE_PREFIX + "nullAsset.label"));

        nullCheckbox.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                updateVariantType();
                updateContent();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                updateVariantType();
                updateContent();
            }
        });
    }

    /**
     * Called by null assset button listener to update the type of the currently
     * selected variant. Toggles variant type between NULL and the default
     * type specified in the context object.
     */
    private void updateVariantType() {
        BeanProxy selectedVariant = context.getSelectedVariant();
        Proxy variantType = selectedVariant.getPropertyProxy(PolicyModel.VARIANT_TYPE);
        if (nullCheckbox.getSelection()) {
            variantType.setModelObject(VariantType.NULL);
        } else {
            VariantType defaultType = context.getDefaultVariantType();
            variantType.setModelObject(defaultType);
        }
    }

    /**
     * Check the valid content types for the policy type supported by this
     * asset content section, and set up a series of flags indicating whether
     * the policy supports each possible type of content.
     */
    private void checkContentTypes() {
        PolicyType policyType = context.getPolicyType();
        PolicyModelSet modelSet = PolicyModelSet.getModelSet(policyType);

        Class[] contentTypes = modelSet.getContentClasses();
        if (contentTypes != null) {
            for (int i = 0; i < contentTypes.length; i++) {
                Class contentType = contentTypes[i];
                if (contentType == null) {
                    providesNull = true;
                } else if (contentType == EmbeddedContentBuilder.class) {
                    providesEmbedded = true;
                } else if (contentType == AutoURLSequenceBuilder.class) {
                    providesSequence = true;
                } else if (contentType == URLContentBuilder.class) {
                    providesURL = true;
                } else if (contentType == ThemeContentBuilder.class) {
                    providesTheme = true;
                } else if (contentType == LayoutContentBuilder.class) {
                    providesLayout = true;
                }
            }
        }
    }

    // Javadoc inherited
    public void setFocus(Path path) {
    }

    /**
     * Set the content from the currently entered data and update the state of
     * the components.
     */
    private void updateContent() {
        if (handlingEvents) {
            handlingEvents = false;
            if (providesNull && nullCheckbox.getSelection()) {
                // Null resource takes priority over everything else - clear the
                // rest of the data and set the content to null
                setEmbeddedCheckbox(false);
                setEmbeddedText("");
                setURLText("");
                setFragmentName("");
                setSegmentName("");

                setModelContent(null);
            } else if (providesEmbedded && embeddedCheckbox.getSelection()) {
                // Embedded takes priority over URL entry - clear URL data and
                // set the content as embedded
                setURLText("");

                EmbeddedContentBuilder embeddedContent =
                        POLICY_FACTORY.createEmbeddedContentBuilder();
                embeddedContent.setData(embeddedText.getText());
                setModelContent(embeddedContent);
            } else if (providesLayout) {
                updateLayoutModel();
            } else if (providesTheme) {
                updateThemeModel();
            } else if (providesURL) {
                URLContentBuilder urlContent =
                        POLICY_FACTORY.createURLContentBuilder();
                urlContent.setURL(urlText.getText());

                String baseUrlString = baseURLText.getText();
                if (baseUrlString != null && baseUrlString.length() > 0) {
                    PolicyReference baseURL =
                            POLICY_FACTORY.createPolicyReference(
                                    baseUrlString, PolicyType.BASE_URL);
                    urlContent.setBaseURLPolicyReference(baseURL);
                }

                BaseLocation baseLocation =
                    (BaseLocation)composite.getProperty(PolicyModel.BASE_LOCATION);
                urlContent.setBaseLocation(baseLocation);

                setModelContent(urlContent);

            }

            setDefaultEnabledState();
            handlingEvents = true;
        }
    }

    /**
     * Update the model from the layout content controls.
     */
    private void updateLayoutModel() {
        ContentBuilder contentBuilder = getModelContent();
        if (contentBuilder != null) {
            InternalLayoutContentBuilder layoutContent =
                    (InternalLayoutContentBuilder) contentBuilder;
            Layout layout = layoutContent.getLayout();

            boolean changed = false;
            String defaultFragment = layout.getDefaultFragmentName();
            String defaultSegment = layout.getDefaultSegmentName();

            String newFragment = defaultFragmentCombo.getCombo().getText();
            if (!textMatchEmptyAsNull(newFragment, defaultFragment)) {
                layout.setDefaultFragmentName(newFragment);
                changed = true;
            }

            String newSegment = defaultSegmentCombo.getCombo().getText();
            if (!textMatchEmptyAsNull(newSegment, defaultSegment)) {
                layout.setDefaultSegmentName(newSegment);
                changed = true;
            }

            if (changed) {
                // Because we're not modifying this value through the interaction
                // layer the context will not automatically be flagged as dirty.
                // This is a temporary measure until layouts are brought into line
                // with other policies and edited purely through the interaction
                // layer.
                context.setDirty(true);
            }

            setModelContent(contentBuilder);
        }
    }

    /**
     * A method to check whether two strings are equal, treating null values
     * as equivalent to empty strings.
     *
     * @param s1 The first string
     * @param s2 The second string
     * @return True if the strings are equal (or one is null and one empty)
     */
    private boolean textMatchEmptyAsNull(String s1, String s2) {
        if (s1 == null) {
            s1 = "";
        }

        if (s2 == null) {
            s2 = "";
        }

        return s1.equals(s2);
    }

    /**
     * Update the theme model from the GUI.
     */
    private void updateThemeModel() {
        BaseProxy baseContentProxy = (BaseProxy) context.getSelectedVariant().getPropertyProxy(PolicyModel.CONTENT);
        Proxy contentProxy = baseContentProxy.getConcreteProxy();
        if (contentProxy != null) {
            BeanProxy contentBeanProxy = (BeanProxy) contentProxy;
            Proxy importParent = contentBeanProxy.getPropertyProxy(ThemeModel.IMPORT_PARENT_DEVICE_THEME);
            importParent.setModelObject(Boolean.valueOf(importParentCheckbox.getSelection()));
        } else {
            // add a content builder to avoid "variant no content
            final ThemeContentBuilder contentBuilder =
                InternalPolicyFactory.getInternalInstance().
                    createThemeContentBuilder();
            setModelContent(contentBuilder);
        }
    }

    /**
     * Updates the display for the specified variant.
     *
     * @param selectedVariant to display the contents of
     */
    private void setDisplayContent(BeanProxy selectedVariant) {

        VariantType type =
            (VariantType)selectedVariant.
                getPropertyProxy(PolicyModel.VARIANT_TYPE).getModelObject();

        ContentBuilder content =
            (ContentBuilder) selectedVariant.
                getPropertyProxy(PolicyModel.CONTENT).getModelObject();

        handlingEvents = false;

        setDefaultEnabledState();

        if (type == VariantType.NULL) {
            // We have a null asset
            setNullCheckbox(true);
            setEmbeddedCheckbox(false);
            setEmbeddedText("");
            setURLText("");
            setBaseURL("");
            setLayoutEnabled(false);
            setFragmentName("");
            setSegmentName("");
            setThemeEnabled(false);
        } else if (content == null) {
            // We have a normal but probably uninitialised asset
            setNullCheckbox(false);
            setEmbeddedCheckbox(false);
            setEmbeddedText("");
            setURLText("");
            setBaseURL("");
            setLayoutEnabled(false);
            setFragmentName("");
            setSegmentName("");
            setThemeEnabled(false);
        } else {
            // We have a normal asset
            ContentBuilderVisitor updateDisplayVisitor = new ContentBuilderVisitor() {
                public void visit(AutoURLSequenceBuilder content) {
                    // TODO later implement this
                }

                public void visit(EmbeddedContentBuilder content) {
                    setNullCheckbox(false);
                    setEmbeddedCheckbox(true);
                    setEmbeddedText(content.getData());
                    setURLText("");
                    setBaseURL("");
                    setLayoutEnabled(false);
                    setThemeEnabled(false);
                }

                public void visit(LayoutContentBuilder content) {
                    setNullCheckbox(false);
                    setEmbeddedCheckbox(false);
                    setEmbeddedText("");
                    setURLText("");
                    setBaseURL("");
                    setLayoutEnabled(true);
                    setThemeEnabled(false);

                    // Retrieve the default fragment/segment values where
                    // appropriate.
                    refreshTargets();

                    Layout layout = ((InternalLayoutContentBuilder)content).getLayout();
                    if (layout != null && layout.getDefaultFragmentName() != null) {
                        setFragmentName(layout.getDefaultFragmentName());
                    } else {
                        setFragmentName("");
                    }

                    if (layout != null && layout.getDefaultSegmentName() != null) {
                        setSegmentName(layout.getDefaultSegmentName());
                    } else {
                        setSegmentName("");
                    }
                }

                public void visit(ThemeContentBuilder content) {
                    setNullCheckbox(false);
                    setEmbeddedCheckbox(false);
                    setEmbeddedText("");
                    setURLText("");
                    setBaseURL("");
                    setLayoutEnabled(false);
                    setThemeEnabled(true);
                    boolean importParent = ((InternalThemeContentBuilder)content).getImportParent();
                    setImportParent(importParent);

                }

                public void visit(URLContentBuilder content) {
                    setNullCheckbox(false);
                    setEmbeddedCheckbox(false);
                    setEmbeddedText("");
                    setURLText(content.getURL());
                    setLayoutEnabled(false);
                    setThemeEnabled(false);
                    PolicyReference baseUrl = content.getBaseURLPolicyReference();
                    if (baseUrl != null && baseUrl.getName() != null) {
                        setBaseURL(baseUrl.getName());
                    } else {
                        setBaseURL("");
                    }

                    composite.setProperty(
                        PolicyModel.BASE_LOCATION, content.getBaseLocation());

                }
            };

            ((InternalContentBuilder) content).accept(updateDisplayVisitor);
        }

        handlingEvents = true;
    }

    /**
     * Sets the specified value as the content for the currently selected
     * variant.
     *
     * @param content The new content for the current variant
     */
    private void setModelContent(ContentBuilder content) {
        BeanProxy selectedVariant = context.getSelectedVariant();
        if (selectedVariant != null) {
            Proxy contentProxy = selectedVariant.getPropertyProxy(PolicyModel.CONTENT);
            Operation setContent = contentProxy.prepareSetModelObjectOperation(content);
            context.executeOperation(setContent);
        }
    }

    /**
     * Retrieve the content builder for the currently selected variant. If none
     * exists, return null.
     *
     * @return The content builder for the currently selected variant
     */
    private ContentBuilder getModelContent() {
        ContentBuilder contentBuilder = null;
        BeanProxy selectedVariant = context.getSelectedVariant();
        if (selectedVariant != null) {
            Proxy contentProxy = selectedVariant.getPropertyProxy(PolicyModel.CONTENT);
            Object model = contentProxy.getModelObject();
            if (model instanceof ContentBuilder) {
                contentBuilder = (ContentBuilder) model;
            }
        }
        return contentBuilder;
    }

    /**
     * Set all controls to their default enabled state.
     */
    private void setDefaultEnabledState() {
        setMiscEnabled(true);

        if (providesNull && nullCheckbox.getSelection()) {
            setEmbeddedEnabled(false);
            setURLEnabled(false);
            setLayoutEnabled(false);
            setThemeEnabled(false);
        } else if (providesEmbedded && embeddedCheckbox.getSelection()) {
            setEmbeddedEnabled(true);
            setURLEnabled(false);
            setLayoutEnabled(false);
            setThemeEnabled(false);
        } else if (providesLayout) {
            setLayoutEnabled(true);
        } else if (providesTheme) {
            setThemeEnabled(true);
        } else {
            setEmbeddedEnabled(false);
            setURLEnabled(true);
        }
    }

    // Javadoc inherited
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setDefaultEnabledState();
        } else {
            setEmbeddedEnabled(false);
            setURLEnabled(false);
            setMiscEnabled(false);
            setThemeEnabled(false);
            setLayoutEnabled(false);
        }
    }

    /**
     * Sets the null checkbox in the GUI, protecting against cases where
     * the control does not exist.
     *
     * @param newValue The new value
     */
    private void setNullCheckbox(boolean newValue) {
        if (providesNull) {
            nullCheckbox.setSelection(newValue);
        }
    }

    /**
     * Sets the embedded checkbox in the GUI, protecting against cases where
     * the control does not exist.
     *
     * @param newValue The new value
     */
    private void setEmbeddedCheckbox(boolean newValue) {
        if (providesEmbedded) {
            embeddedCheckbox.setSelection(newValue);
        }
    }

    /**
     * Sets the URL text value in the GUI, protecting against cases where
     * the control does not exist.
     *
     * @param newValue The new value
     */
    private void setURLText(String newValue) {
        if (providesURL) {
            urlText.setText(newValue);
        }
    }

    /**
     * Sets the base URL value in the GUI, protecting against cases where
     * the control does not exist.
     *
     * @param newValue The new value
     */
    private void setBaseURL(String newValue) {
        if (providesURL) {
            baseURLText.setText(newValue);
        }
    }

    /**
     * Sets the embedded text value in the GUI, protecting against cases where
     * the control does not exist.
     *
     * @param newValue The new value
     */
    private void setEmbeddedText(String newValue) {
        if (providesEmbedded) {
            embeddedText.setText(newValue);
        }
    }

    /**
     * Sets the default fragment text value in the GUI, protecting against
     * cases where the control does not exist.
     *
     * @param newValue The new value
     */
    private void setFragmentName(String newValue) {
        if (providesLayout) {
            defaultFragmentCombo.getCombo().setText(newValue);
        }
    }

    /**
     * Sets the default segment text value in the GUI, protecting against
     * cases where the control does not exist.
     *
     * @param newValue The new value
     */
    private void setSegmentName(String newValue) {
        if (providesLayout) {
            defaultSegmentCombo.getCombo().setText(newValue);
        }
    }

    /**
     * Refresh the combos for default targets.
     */
    private void refreshTargets() {
        InternalLayoutContentBuilder builder =
                (InternalLayoutContentBuilder) getModelContent();
        if (builder != null) {
            Layout layout = builder.getLayout();
            if (layout != null) {
                // Preserve the current values before we update the input -
                // this is required because combo viewers handle changing inputs
                // badly, losing any pre-entered values.
                String defaultFragment = layout.getDefaultFragmentName();
                String defaultSegment = layout.getDefaultSegmentName();

                defaultFragmentCombo.setInput(layout);
                defaultSegmentCombo.setInput(layout);

                defaultFragmentCombo.getCombo().setText(
                        defaultFragment == null ? "" : defaultFragment);
                defaultSegmentCombo.getCombo().setText(
                        defaultSegment == null ? "" : defaultSegment);
            }
        }
    }

    /**
     * Sets the embedded checkbox in the GUI, protecting against cases where
     * the control does not exist.
     *
     * @param newValue The new value
     */
    private void setImportParent(boolean newValue) {
        if (providesTheme) {
            importParentCheckbox.setSelection(newValue);
        }
    }

    /**
     * Content provider for providing lists of valid targets (fragment,
     * segment) from a layout input.
     */
    private class LayoutDefaultTargetContentProvider
            implements IStructuredContentProvider {
        /**
         * The target type for this content.
         *
         * @see DefaultTargetType#FRAGMENT
         * @see DefaultTargetType#SEGMENT
         */
        private DefaultTargetType targetType;

        public LayoutDefaultTargetContentProvider(DefaultTargetType type) {
            targetType = type;
        }

        // Javadoc inherited
        public Object[] getElements(Object o) {
            Layout layout = (Layout) o;
            Collection values = DEFAULT_TARGET_FORMAT_VISITOR.
                    getValidNames(layout, targetType);
            Object[] elements = values.toArray();
            Arrays.sort(elements);
            return elements;
        }

        // Javadoc inherited
        public void dispose() {
        }

        // Javadoc inherited
        public void inputChanged(Viewer viewer, Object o, Object o1) {
        }
    }
}
