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

import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.BuilderEditorPart;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.BeanPropertyChangedEvent;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.themes.Rule;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import java.text.MessageFormat;

/**
 */
public class ThemeDesign extends BuilderEditorPart {
    private static final String RESOURCE_PREFIX = "ThemeDesign.";

    private FormToolkit formToolkit;

    private Composite displayArea;

    private StackLayout displayAreaLayout;

    private Composite messagePage;

    private Composite nullVariantPage;

    private ScrolledComposite formPage;

    private SelectorsSection selectorsSection;

    private StylePropertiesSection stylePropertiesSection;

    private Label designLabel;

    private MessageFormat designMessageFormat;

    private boolean enabled = true;

    private InteractionEventListener changeListener = new InteractionEventListenerAdapter() {
        public void propertySet(BeanPropertyChangedEvent event) {
            updateDisplay();
        }

        public void proxyModelChanged(ProxyModelChangedEvent event) {
            updateDisplay();
        }
    };

    public ThemeDesign(ThemeEditorContext context) {
        super(context);
        context.addSelectedVariantListener(new BuilderSelectionListener() {
            public void selectionMade(BuilderSelectionEvent event) {
                registerChangeListeners((Proxy) event.getOldSelection(),
                        (Proxy) event.getSelection());
                updateDisplay();
            }
        });
    }

    private void registerChangeListeners(Proxy oldSelection, Proxy selection) {
        if (oldSelection != null && oldSelection instanceof BeanProxy) {
            Proxy type = ((BeanProxy)
                    oldSelection).getPropertyProxy(PolicyModel.CONTENT);
            type.removeListener(changeListener);
        }

        if (selection != null && selection instanceof BeanProxy) {
            Proxy type = ((BeanProxy)
                    selection).getPropertyProxy(PolicyModel.CONTENT);
            type.addListener(changeListener, false);
        }
    }

    public void createPartControl(Composite composite) {
        displayArea = new Composite(composite, SWT.NONE);
        displayAreaLayout = new StackLayout();
        displayArea.setLayout(displayAreaLayout);
        displayArea.setBackground(composite.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        createMessagePage(displayArea);
        createNullVariantPage(displayArea);
        createFormPage(displayArea);

        updateDisplay();
    }

    public void dispose() {
        if (selectorsSection != null) {
            selectorsSection.dispose();
        }
        if (stylePropertiesSection != null) {
            stylePropertiesSection.dispose();
        }
        if (formToolkit != null) {
            formToolkit.dispose();
        }
        super.dispose();
    }

    private void createMessagePage(Composite parent) {
        messagePage = new Composite(parent, SWT.NONE);
        messagePage.setLayout(new GridLayout(1, false));
        Label label = new Label(messagePage, SWT.NONE);
        String noTheme =
                EditorMessages.getString(RESOURCE_PREFIX + "noDeviceTheme");
        label.setText(noTheme);
        GridData data = new GridData(GridData.FILL_BOTH);
        label.setLayoutData(data);
    }

    private void createNullVariantPage(Composite parent) {
        nullVariantPage = new Composite(parent, SWT.NONE);
        nullVariantPage.setLayout(new GridLayout(1, false));
        Label label = new Label(nullVariantPage, SWT.NONE);
        String nullTheme =
                EditorMessages.getString(RESOURCE_PREFIX + "nullDeviceTheme");
        label.setText(nullTheme);
        GridData data = new GridData(GridData.FILL_BOTH);
        label.setLayoutData(data);
    }

    private void createFormPage(Composite parent) {
        formPage = new ScrolledComposite(parent,
                SWT.H_SCROLL | SWT.V_SCROLL);
        formPage.setExpandHorizontal(true);
        formPage.setExpandVertical(true);

        Composite scrollable = new Composite(formPage, SWT.NONE);
        formPage.setContent(scrollable);

        scrollable.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        int marginHeight = EditorMessages.getInteger("Editor." +
                "marginHeight").intValue();
        int marginWidth = EditorMessages.getInteger("Editor." +
                "marginWidth").intValue();
        int verticalSpacing = EditorMessages.getInteger("Editor." +
                "verticalSpacing").intValue();

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = marginHeight;
        gridLayout.marginWidth = marginWidth;
        gridLayout.verticalSpacing = verticalSpacing;
        scrollable.setLayout(gridLayout);
        GridData data = new GridData(GridData.FILL_BOTH);
        scrollable.setLayoutData(data);

        designLabel = new Label(scrollable, SWT.NONE);
        Font font = designLabel.getFont();
        FontData fontData [] = font.getFontData();
        int designLabelHeight =
                EditorMessages.getInteger(RESOURCE_PREFIX +
                "designLabel.height").intValue();
        fontData[0].setHeight(designLabelHeight);
        fontData[0].setStyle(SWT.BOLD);
        final Font newFont = new Font(designLabel.getDisplay(), fontData[0]);
        designLabel.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                newFont.dispose();
            }
        });

        designLabel.setFont(newFont);
        designLabel.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        String designMessageFormatString =
                EditorMessages.getString(RESOURCE_PREFIX +
                "designLabel.text");

        designMessageFormat = new MessageFormat(designMessageFormatString);

        createSelectorsSection(scrollable);
        createStylePropertiesSection(scrollable);

        selectorsSection.setSize(selectorsSection.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));

        stylePropertiesSection.setSize(stylePropertiesSection.
                computeSize(SWT.DEFAULT,
                SWT.DEFAULT));

        scrollable.setSize(scrollable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrollable.layout();

        formPage.setMinSize(scrollable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public void updateDisplay() {
        BeanProxy selectedVariant =
                ((ThemeEditorContext) getContext()).getSelectedVariant();

        if (selectedVariant == null) {
            displayAreaLayout.topControl = messagePage;
        } else {
            // Disable design for null types
            Proxy type = ((BeanProxy) selectedVariant).
                    getPropertyProxy(PolicyModel.VARIANT_TYPE);
            boolean nullType = VariantType.NULL == type.getModelObject();

            if (nullType) {
                displayAreaLayout.topControl = nullVariantPage;
            } else {
                // TODO better This is silly - try again...
                String deviceName =
                        String.valueOf(selectedVariant.getModelObject());
                designLabel.setText(designMessageFormat.format(
                        new Object[] {deviceName}));
                designLabel.pack();
                displayAreaLayout.topControl = formPage;
            }
        }
        displayArea.layout();
        selectorsSection.setVariant(selectedVariant);
    }

    public void setFocus() {
    }

    private void createSelectorsSection(Composite parent) {
        if (formToolkit == null) {
            formToolkit = new FormToolkit(parent.getShell().getDisplay());
        }
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        form.setLayoutData(data);

        selectorsSection =
                new SelectorsSection(form.getBody(), SWT.NONE,
                        (ThemeEditorContext) getContext());
        data = new GridData(GridData.FILL_HORIZONTAL);
        selectorsSection.setLayoutData(data);
        selectorsSection.addSelectionChangeListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                handleRuleSelection(event);
            }
        });
    }

    private void handleRuleSelection(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        BeanProxy ruleProxy = (BeanProxy) selection.getFirstElement();
        BeanProxy stylePropertiesProxy = null;
        if (ruleProxy != null) {
            stylePropertiesProxy = (BeanProxy) ruleProxy.getPropertyProxy(Rule.STYLE_PROPERTIES);
        }
        stylePropertiesSection.setStyleProperties(stylePropertiesProxy);
    }

    private void createStylePropertiesSection(Composite parent) {
        if (formToolkit == null) {
            formToolkit = new FormToolkit(parent.getShell().getDisplay());
        }
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(data);

        stylePropertiesSection =
                new StylePropertiesSection(form.getBody(), SWT.NONE, (ThemeEditorContext) getContext());
        data = new GridData(GridData.FILL_BOTH);
        stylePropertiesSection.setLayoutData(data);
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        selectorsSection.setFocus(path);
        stylePropertiesSection.setFocus(path);
    }

    /**
     * Set the enabled state of the theme design part. Used to make the design
     * read-only when the user can not edit the theme.
     *
     * @param enabled True if the part is to be enabled, false otherwise
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        selectorsSection.setEnabled(enabled);
        stylePropertiesSection.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/4	adrianj	VBM:2005111601 Add style rule view

 24-Nov-05	10437/1	adrianj	VBM:2005111509 Display all style properties in a category

 24-Nov-05	10431/1	adrianj	VBM:2005111509 Display all style properties in a category

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/5	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
