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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.MetaContentProcessor;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.MetaContentProcessorFactory;
import com.volantis.mcs.xdime.xhtml2.meta.property.MetaPropertyHandler;
import com.volantis.mcs.xdime.xhtml2.meta.property.MetaPropertyHandlerFactory;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.NamespacePrefixTracker;

/**
 * XHTML V2 MetaInformation element object.
 */
public class MetaInformationElement extends XHTML2Element {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer LOCALIZER =
        LocalizationFactory.createMessageLocalizer(
            MetaInformationElement.class);

    private static final String ATTR_NAME_PROPERTY = "property";
    private static final String ATTR_NAME_DATA_TYPE = "datatype";
    private static final String ATTR_NAME_ABOUT = "about";

    private MetaPropertyHandler propertyHandler;
    private MetaContentProcessor contentProcessor;
    private String targetId;
    private String propertyName;

    public MetaInformationElement(XDIMEContextInternal context) {
        super(XHTML2Elements.META, UnstyledStrategy.STRATEGY, context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(final XDIMEContextInternal context,
                                      final XDIMEAttributes attributes)
            throws XDIMEException {

        XDIMEResult result = XDIMEResult.PROCESS_ELEMENT_BODY;

        propertyName = getPropertyName(attributes, context);
        if (propertyName != null) {
            targetId = getTargetId(context, attributes);

            // get the property handler
            propertyHandler =
                MetaPropertyHandlerFactory.getHandler(propertyName);
            if (propertyHandler == null) {
                throw new XDIMEException(LOCALIZER.format(
                    "no-meta-property-handler-registered", propertyName));
            }

            // get the content processor
            final String dataTypeAttrValue =
                ElementUtils.getAttributeValue(context, attributes,
                    XDIMESchemata.XHTML2_NAMESPACE, ATTR_NAME_DATA_TYPE);
            final ImmutableExpandedName dataType;
            if (dataTypeAttrValue == null || dataTypeAttrValue.length() == 0) {
                dataType = propertyHandler.getDefaultDataType().getExpandedName();
            } else {
                dataType = getAsExpandedName(dataTypeAttrValue, context);
            }
            contentProcessor =
                MetaContentProcessorFactory.getProcessor(dataType);
            if (contentProcessor == null) {
                throw new XDIMEException(LOCALIZER.format(
                    "no-meta-content-processor-registered", dataTypeAttrValue));
            }

            // check if this is the right property-data type pair
            if (!propertyHandler.isAcceptableType(contentProcessor.getType())) {
                throw new XDIMEException(LOCALIZER.format(
                    "invalid-type-for-meta-property",
                    new Object[]{dataTypeAttrValue, propertyName}));
            }

            contentProcessor.startProcess(context, this, attributes);
        } else {
            result = XDIMEResult.SKIP_ELEMENT_BODY;
        }
        return result;
    }

    protected boolean forceProcessing(final XDIMEContextInternal context,
                                      final XDIMEAttributes attributes)
            throws XDIMEException {

        return getPropertyName(attributes, context) != null;
    }

    private String getTargetId(final XDIMEContextInternal context,
                               final XDIMEAttributes attributes)
            throws XDIMEException {

        String result = ElementUtils.getAttributeValue(context, attributes,
            XDIMESchemata.XHTML2_NAMESPACE, ATTR_NAME_ABOUT);
        if (result != null) {
            if (result.length() == 0) {
                result = null;
            } else if (!result.startsWith("#")) {
                throw new XDIMEException(LOCALIZER.format(
                    "invalid-meta-reference",
                    new Object[]{XDIMESchemata.XDIME2_MCS_NAMESPACE, result}));
            } else {
                result = result.substring(1);
            }
        }
        return result;
    }

    private String getPropertyName(final XDIMEAttributes attributes,
                                   final XDIMEContextInternal context)
            throws XDIMEException {

        String localName = null;
        final String property = ElementUtils.getAttributeValue(context,
            attributes, XDIMESchemata.XHTML2_NAMESPACE, ATTR_NAME_PROPERTY);

        final ExpandedName name = getAsExpandedName(property, context);
        if (name != null) {
            if (XDIMESchemata.XDIME2_MCS_NAMESPACE.equals(name.getNamespaceURI())
                    || XDIMESchemata.DC_NAMESPACE.equals(name.getNamespaceURI())
                    || "".equals(name.getNamespaceURI())){
                // only set the result if the property has the right
                // namespace. It might be mcs, dc or default (empty) namespace
                localName = name.getLocalName();
            }            
        } else {
            throw new XDIMEException(LOCALIZER.format(
                    "illegal-meta-data-property-missing"));
        }
        return localName;
    }

    private ImmutableExpandedName getAsExpandedName(
            final String string, final XDIMEContextInternal context) {

        ExpandedName name;
        if (string != null && string.length() > 0) {
            ImmutableQName qName = new ImmutableQName(string);
            NamespacePrefixTracker tracker = context.getExpressionContext().
                    getNamespacePrefixTracker();
            name = tracker.resolveQName(qName, "");
        } else {
            name = null;
        }

        final ImmutableExpandedName result;
        if (name == null) {
            result = null;
        } else {
            result = name.getImmutableExpandedName();
        }
        return result;
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(final XDIMEContextInternal context)
            throws XDIMEException {

        if (contentProcessor != null) {
            contentProcessor.endProcess(context, this);
            final Object result = contentProcessor.getResult();
            propertyHandler.process(result, context, targetId, propertyName);
            propertyHandler = null;
            contentProcessor = null;
            targetId = null;
        }
    }

    /**
     * Increased visibility to let content processors create their own output
     * buffer.
     */
    public OutputBuffer createOutputBuffer(final XDIMEContextInternal context) {
        return super.createOutputBuffer(context);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10523/1	ianw	VBM:2005112406 Make XDIMECP Meta tag noop for now until we process it properly

 02-Dec-05	10514/1	ianw	VBM:2005112406 Make XDIMECP Meta tag noop for now until we process it properly

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
