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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFFormFieldElement.java,v 1.17 2003/04/22 13:56:08 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 06-Dec-01    Paul            VBM:2001120402 - Made sure that panes were not
 *                              null before retrieving their context.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 19-Feb-02    Paul            VBM:2001100102 - Add extra FieldDescriptor
 *                              object to doField and add it to the form
 *                              descriptor.
 * 04-Mar-02    Paul            VBM:2001101803 - Made sure that a fragmented
 *                              form creates a FormDescriptor which describes
 *                              the whole form. Added a new
 *                              initialiseFieldDescriptor method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 05-Jun-02    Byron           VBM:2002053002 - Updated doField method to add
 *                              a call to setTabindex()
 * 17-Jun-02    Byron           VBM:2002061001 - Changed capitalisation for
 *                              xxxxtabindex
 * 15-Aug-02    Paul            VBM:2002081421 - Added support for some field
 *                              elements to be used outside of a form. At the
 *                              moment only action is supported but that will
 *                              change. Mostly this involved making sure that
 *                              form objects were not referenced without
 *                              checking first. Removed the
 *                              initialiseFieldDescriptor method and replaced
 *                              it with two methods, one to get the initial
 *                              value and one to get the field type. This was
 *                              so that the field type could be retrieved
 *                              on its own.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 29-Oct-02    Chris W         VBM:2002111101 - Gets pane name from
 *                              FormatReference rather than directly from
 *                              MarinerPageContext
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add throw clause for
 *                              ProtocolException.
 * 16-Apr-03    Geoff           VBM:2003041603 - Fixed previous bogus comment.
 * 06-May-03    Byron           VBM:2003042208 - Modified doField to write the
 *                              initial focus if necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFFormFieldAttributes;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The base class of the visible extended function form field elements.
 * <p>
 * Field elements which are part of a form are not processed immediately,
 * instead they are added to the form attributes and they are all processed
 * together. This is because in some protocols some elements need to know about
 * all the elements in the form including ones which follow.
 * <p>
 * Field elements which are not part of a form and are inline in the page are
 * processed immediately.
 */
public abstract class XFFormFieldElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XFFormFieldElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(XFFormFieldElementImpl.class);

    /**
     * The zeroth index of the spatial iterator.
     */
    private static final NDimensionalIndex ZEROTH_INDEX =
            new NDimensionalIndex(new int[]{0});

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * The attributes to pass to the protocol methods.
     */
    private XFFormAttributes formAttributes;

    /**
     * The descriptor of the form.
     */
    private FormDescriptor formDescriptor;

    /**
     * The descriptor of the field.
     */
    private FieldDescriptor fieldDescriptor;

    /**
     * The caption pane instance.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     */
    private AbstractPaneInstance captionPaneInstance;

    /**
     * The entry pane instance.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     */
    private AbstractPaneInstance entryPaneInstance;

    /**
     * Flag which indicates whether the current form element is allowed inline.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     */
    final boolean allowedInline;

    /**
     * Create a new <code>XFFormFieldElement</code>.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     */
    XFFormFieldElementImpl(boolean allowedInline) {
        this.allowedInline = allowedInline;
    }

    /**
     * Return true if this field is being rendered inline and false otherwise.
     */
    boolean inline() {
        return (formAttributes == null);
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return formAttributes;
    }

    /**
     * Check that the named pane instances are valid and apply default rules if
     * either of them has not been specified.
     *
     * @param pageContext     the mariner page context.
     * @param captionPaneName the caption pane name.
     * @param entryPaneName   the entry pane name.
     * @return SKIP_ELEMENT_BODY or PROCESS_ELEMENT_BODY
     * @throws PAPIException if a PAPI related exception occurs.
     */
    int checkPaneInstances(
            MarinerPageContext pageContext,
            String captionPaneName,
            String entryPaneName) throws PAPIException {

        Form form;
        if (inline()) {
            form = null;
        } else {
            form = (Form) ((FormInstance) formAttributes.getFormData())
                    .getFormat();
        }

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Checking panes " + captionPaneName + " " + entryPaneName);
        }

        // Look up the caption pane instance if it has been specified.
        captionPaneInstance = lookupPaneInstance(
                pageContext, form, captionPaneName, captionPaneInstance);

        // Look up the entry pane instance if it has been specified.
        entryPaneInstance = lookupPaneInstance(
                pageContext, form, entryPaneName, entryPaneInstance);

        if ((captionPaneName == null) && (entryPaneName == null)) {
            // Neither have been specified so use the current pane if this is being
            // rendered inline, otherwise use the forms default pane.
            Pane pane;
            if (form == null) {
                pane = pageContext.getCurrentPane();
            } else {
                pane = form.getDefaultPane();
            }

            // If the pane exists then use it for both caption and entry pane.
            if (pane != null) {
                captionPaneName = entryPaneName = pane.getName();
                captionPaneInstance =
                        (PaneInstance) pageContext.getFormatInstance(
                                pane, ZEROTH_INDEX);
                entryPaneInstance = captionPaneInstance;
            }
        } else if (captionPaneName == null) {
            // The caption pane has not been specified but the entry pane has
            // so send the caption to the same pane as the entry.
            captionPaneName = entryPaneName;
            captionPaneInstance = entryPaneInstance;
        } else if (entryPaneName == null) {
            // The entry pane has not been specified but the caption pane has
            // so send the entry to the same pane as the caption.
            entryPaneName = captionPaneName;
            entryPaneInstance = captionPaneInstance;
        }

        // If output is not required for the caption pane instance then null it.
        captionPaneInstance = updatePaneInstance(captionPaneInstance);

        // If output is not required for the entry pane instance then null it.
        entryPaneInstance = updatePaneInstance(entryPaneInstance);

        if (logger.isDebugEnabled()) {
            logger.debug("Caption pane instance " + captionPaneInstance +
                    " entry pane instance " + entryPaneInstance);
        }

        // If at the end of this neither the caption pane or entry pane have
        // been set then we can't generate anything.
        if (captionPaneInstance == null && entryPaneInstance == null) {
            return SKIP_ELEMENT_BODY;
        }

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * If output is not required for the pane instance return null, otherwise
     * return the unmodified instance.
     *
     * @param instance the pane instance.
     * @return a null pane instance or the original pane instance itself.
     */
    private AbstractPaneInstance updatePaneInstance(
            AbstractPaneInstance instance) {

        AbstractPaneInstance result = instance;
        if (instance != null) {
            if (instance.ignore()) {
                result = null;
            }
        }

        return result;
    }

    /**
     * <p>Attempt to obtain the appropriate pane instance for the form given
     * the original pane name.</p>
     *
     * <p>The pane instance returned may be null if this field is not being
     * rendered inline and the referenced pane instance is not in the current
     * form.</p>
     *
     * @param pageContext         the mariner page context.
     * @param form                the Form instance.
     * @param originalPaneName    the original pane name.
     * @param currentPaneInstance the current pane instance.
     * @return the pane instance to use to for rendering. This may be the
     *         current pane instance, or null, or a new pane instance.
     */
    private AbstractPaneInstance lookupPaneInstance(
            MarinerPageContext pageContext, final Form form,
            final String originalPaneName,
            final AbstractPaneInstance currentPaneInstance) {

        AbstractPaneInstance result = currentPaneInstance;
        if (originalPaneName != null) {

            // If the pane name is null and the form isn't null then set
            // the pane name to be the default pane name.
            String paneName = originalPaneName;
            if ((paneName == null) && (form != null)) {
                paneName = form.getDefaultPane().getName();
            }

            FormatReference formatRef = FormatReferenceParser.parsePane(
                    paneName, pageContext);
            Pane pane = pageContext.getPane(formatRef.getStem());
            if (pane != null) {
                // We have the associated pane to use. Now get the zeroth
                // instance.
                result = (AbstractPaneInstance) pageContext.getFormatInstance(
                        pane, formatRef.getIndex());
            }

            // If this field is not being rendered inline then make sure that
            // the pane instance is in the current form, if it is not then
            // ignore it.
            if ((form != null) &&
                    (pane != null) &&
                    (pane.getEnclosingForm() != form)) {

                logger.warn("pane-not-in-form",
                        new Object[]{paneName, form.getName()});
                result = null;
            }
        }
        return result;
    }

    /**
     * Return true if the specified pane should be included in the current
     * fragment.
     * <p>
     * If the form is not being fragmented then it will always return true,
     * if the form is being fragmented but the pane is not part of any
     * fragment then this will return true, if the pane is part of the current
     * fragment then this will return true, otherwise it will return false.
     * </p>
     *
     * @return True if the specified pane should be included in the current
     *         fragment and false otherwise.
     */
    boolean inCurrentFragment(
            MarinerPageContext pageContext,
            AbstractPaneInstance paneInstance) {

        // If this field is being rendered inline then we need to make sure that
        // the pane is visible from the current context.
        if (inline()) {
            // TODO: Check that the current pane is in the current fragment if any.
            return true;
        }

        // This field is inside a form so make sure that it is in the current form
        // fragment.
        Form form = (Form) ((FormInstance) formAttributes.getFormData())
                .getFormat();
        if (form.isFragmented()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Enclosing form is fragmented.");
            }

            FormFragment fragment = paneInstance.getFormat().
                    getEnclosingFormFragment();
            if (logger.isDebugEnabled()) {
                logger.debug("Enclosing form fragment is " + fragment);
            }

            // The pane is not in any fragment so always include it.
            if (fragment == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Pane is not in any form fragment so will " +
                            "always be displayed");
                }
                return true;
            }

            FormFragment currentFormFragment =
                    pageContext.getCurrentFormFragment();
            if (logger.isDebugEnabled()) {
                logger.debug("Current form fragment is " + currentFormFragment);
            }

            if (fragment == currentFormFragment) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Pane is in the current form fragment so it " +
                            "will be displayed");
                }
                return true;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Pane is not in the current form fragment so it " +
                        "will not be displayed");
            }
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Form is not fragmented so all panes will be included");
        }
        return true;
    }

    /**
     * Do any common field initialisation and then add the field to the form
     * to be processed later.
     *
     * @param pageContext The MarinerPageContext within which this element is
     *                    running.
     * @param attributes  The PAPI attributes for the field.
     * @param pattributes The protocol attributes for the field.
     *                    <h2>
     *                    You MUST NOT change the protection level on this member.
     *                    </h2>
     */
    void doField(
            MarinerPageContext pageContext,
            com.volantis.mcs.papi.XFFormFieldAttributes attributes,
            com.volantis.mcs.protocols.XFFormFieldAttributes pattributes)
            throws ProtocolException {

        TextAssetReference object;

        if (logger.isDebugEnabled()) {
            logger.debug("Doing field");
        }
        // Set the caption pane and entry pane.
        pattributes.setCaptionContainerInstance(captionPaneInstance);
        pattributes.setEntryContainerInstance(entryPaneInstance);

        // Copy the core attributes.
        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());

        // get styles for the current element and set it on the MCSAttributes
        pattributes.setStyles(pageContext.getStylingEngine().getStyles());

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Process the caption as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getCaption());
        pattributes.setCaption(object);

        // Process the help as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getHelp());
        pattributes.setHelp(object);

        // Set the name attribute.
        String name = attributes.getName();
        pattributes.setName(name);

        // Process the prompt as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getPrompt());
        pattributes.setPrompt(object);

        // Process the shortcut as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getShortcut());
        pattributes.setShortcut(object);

        // Add a reference to the field descriptor into the attributes.
        pattributes.setFieldDescriptor(fieldDescriptor);

        // Update the tab index for this field
        pattributes.setTabindex(attributes.getTabindex());

        VolantisProtocol protocol = pageContext.getProtocol();
        String tabindex = (String) pattributes.getTabindex();
        if (tabindex != null) {
            String initialFocus =
                    protocol.getCanvasAttributes().getInitialFocus();
            if ((initialFocus != null)
                    && (initialFocus.equals(attributes.getId()))) {
                protocol.writeInitialFocus(tabindex);
            }
        }

        // If this field is not inside a form then render it immediately,
        // otherwise delay rendering until the whole form has been processed.
        if (inline()) {
            FieldType fieldType = getFieldType(attributes);
            fieldType.doField(protocol, pattributes);
        } else {
            // Add a reference back to the form attributes and the form.
            pattributes.setFormAttributes(formAttributes);
            pattributes.setFormData(formAttributes.getFormData());

            // Add the field to the form.
            formAttributes.addField(pattributes);
        }
    }

    /**
     * Get the initial value for the field as specified in the markup.
     *
     * @param pageContext    The MarinerPageContext within which this element is
     *                       running.
     * @param papiAttributes The attributes used for this element.
     * @return The initial value which may be null.
     *         <h2>
     *         You MUST NOT change the protection level on this member.
     *         </h2>
     */
    abstract String getInitialValue(
            MarinerPageContext pageContext,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    /**
     * Get the field type.
     *
     * @param papiAttributes The attributes used for this element.
     * @return The FieldType.
     *         <h2>
     *         You MUST NOT change the protection level on this member.
     *         </h2>
     */
    abstract FieldType getFieldType(PAPIAttributes papiAttributes);

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        XFFormFieldAttributes attributes =
                (XFFormFieldAttributes) papiAttributes;

        // If this field is not active then do nothing, the default active state
        // is true.
        String value = attributes.getActive();
        boolean active = (value == null || "true".equalsIgnoreCase(value));
        if (!active) {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring element as it is inactive");
            }

            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

        // Get the page context.
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Some XFFormField elements do not have to be inside a form.
        PAPIElement enclosingElement = pageContext.getCurrentElement();
        XFFormElementImpl formElement;
        if (enclosingElement instanceof XFFormElementImpl) {
            formElement = (XFFormElementImpl) enclosingElement;
        } else {
            if (allowedInline) {
                formElement = null;
            } else {
                throw new PAPIException(exceptionLocalizer.format(
                        "element-parent-not-form"));
            }
        }

        // Check to make sure that we are not working inline.
        if (formElement != null) {
            // Get the enclosing form element's attributes.
            formAttributes = formElement.getProtocolAttributes();

            // Get the enclosing form element's descriptor.
            formDescriptor = formElement.getFormDescriptor();

            // If the current form is fragmented then we may not have to output
            // anything for this field in the current fragment but we still need to
            // create its descriptor.
            fieldDescriptor = new FieldDescriptor();
            fieldDescriptor.setName(attributes.getName());
            fieldDescriptor.setInitialValue(getInitialValue(pageContext,
                    attributes));
            fieldDescriptor.setType(getFieldType(attributes));

            // Add the field descriptor to the form's descriptor.
            formDescriptor.addField(fieldDescriptor);
        }

        // Check the panes to see whether we actually have to output anything.
        if (checkPaneInstances(pageContext,
                attributes.getCaptionPane(),
                attributes.getEntryPane()) == SKIP_ELEMENT_BODY) {
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

        // At this point at least one of the panes will be non null and for those
        // which are non null we need to make sure that they are in the current
        // form fragment (if any). If they are not then we do not want to output
        // anything for this element.
        if ((captionPaneInstance != null
                && !inCurrentFragment(pageContext, captionPaneInstance))
                || (entryPaneInstance != null
                && !inCurrentFragment(pageContext, entryPaneInstance))) {
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

        // Push this element.
        pageContext.pushElement(this);

        return elementStartImpl(context, attributes);
    }

    /**
     * This method is only called if output for the field is required.
     *
     * @see #elementStartImpl
     */
    protected abstract int elementStartImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        if (skipped) {
            return CONTINUE_PROCESSING;
        }

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Pop this element.
        pageContext.popElement(this);

        return elementEndImpl(context, papiAttributes);
    }

    /**
     * This method is only called if output for the pane is required.
     *
     * @see #elementStartImpl
     */
    protected int elementEndImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        captionPaneInstance = null;
        entryPaneInstance = null;
        formAttributes = null;
        skipped = false;
        formDescriptor = null;
        fieldDescriptor = null;
        super.elementReset(context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	6320/1	geoff	VBM:2004112604 xfaction with captionPane= throws a NullPointerException

 22-Nov-04	6183/5	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6135/4	byron	VBM:2004081726 Allow spatial format iterators within forms

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 07-Jan-04	2473/1	mat	VBM:2003111402 Use default pane if caption or entry pane not specified

 07-Jan-04	2462/1	mat	VBM:2003111402 Use default pane if caption or entry pane not specified

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
