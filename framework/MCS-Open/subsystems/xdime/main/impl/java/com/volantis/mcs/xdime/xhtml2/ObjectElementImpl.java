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

import java.util.*;

import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.*;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * XHTML V2 Object element object.
 */
public class ObjectElementImpl
        extends XHTML2Element
        implements ObjectElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ObjectElementImpl.class);

    // TODO: see RFC 2048 Section 2.1.4 . (Using x- is discouraged.)
    // Should be using something like application/vnd.volanftis.mcs-image

    /**
     * The media type for device independent image.
     */
    private static final String MCS_IMAGE_MEDIA_TYPE =
            "application/x-mcs-image";

    /**
     * The media type for device independent text.
     */
    private static final String MCS_TEXT_MEDIA_TYPE =
            "application/x-mcs-text";

    /**
     * The media type for device independent audio.
     */
    private static final String MCS_AUDIO_MEDIA_TYPE =
            "application/x-mcs-audio";

    /**
     * The media type for device independent video.
     */
    private static final String MCS_VIDEO_MEDIA_TYPE =
            "application/x-mcs-video";
    
    /**
     * Pattern which match to all device independent resources 
     */
    private static final String INDEPENDENT_PATERN = "application/x-mcs-.*";
    
    /**
     * Contains all supported device independent resources 
     */
    private static final Set independentResources = new HashSet();
    static {
        independentResources.add(MCS_IMAGE_MEDIA_TYPE);
        independentResources.add(MCS_TEXT_MEDIA_TYPE);
        independentResources.add(MCS_AUDIO_MEDIA_TYPE);
        independentResources.add(MCS_VIDEO_MEDIA_TYPE);    
    }
    
    /**
     * Buffer to hold the content seen inside this object.
     */
    private OutputBuffer bodyContentBuffer;

    /**
     * The src URI as provided in the input attributes.
     */
    private String src;

    /**
     * A sorted map of q values (Double) to media types.
     */
    private TreeMap sourceTypes;

    /**
     * The object type shared by all the content types supplied in the input
     * attributes. They must all share the same object type or we throw an
     * exception.
     */
    private ObjectType objectType;
    
    /**
     * Initialise.
     * @param context
     */
    public ObjectElementImpl(XDIMEContextInternal context) {
        super(XHTML2Elements.OBJECT, context);

        protocolAttributes = new ObjectAttribute();
    }

    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context,
                                                       XDIMEAttributes attributes) {

        ObjectAttribute objectAtr = (ObjectAttribute)protocolAttributes;

        objectAtr.setSrc(getAttribute(XDIMEAttribute.SRC, attributes));
        objectAtr.setSrcType(
            getAttribute(XDIMEAttribute.SRC_TYPE, attributes));
    }

    // Javadoc inherited.
    public void setCaptionBuffer(OutputBuffer captionContentBuffer) {
        ((ObjectAttribute)protocolAttributes).setCaptionContent(captionContentBuffer);
    }

    // Javadoc inherited.
    public void addParameter(String name, Object value) {
    	Map paramsMap = ((ObjectAttribute)protocolAttributes).getParamMap();
    	paramsMap.put(name, value);
    }

    // Javadoc inherited.
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // Process the attributes. We will do as much up front processing as is
        // possible for the src URI and srctypes content types, and store the
        // results to be used in the close method.
        ObjectAttribute objectAttributes = (ObjectAttribute)protocolAttributes;

        src = objectAttributes.getSrc();

        sourceTypes = createSourceTypesTree(objectAttributes);
        fillSrcTypeIfEmpty(sourceTypes,objectAttributes);
        objectType = determineObjectType(sourceTypes,objectAttributes.getSrcType());

        // Create an output buffer for the content of the object element.
        // This will enable us to grab it easily when the element ends and
        // either throw it away if the src attribute can be used or use it
        // instead if not.
        bodyContentBuffer = createOutputBuffer(context);
        getPageContext(context).pushOutputBuffer(bodyContentBuffer);

        ObjectParameter.addDefaultValues((HashMap) 
        		((ObjectAttribute)protocolAttributes).getParamMap());
        
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    /**
     * determine objectType basing on sourcetypes tree
     * @param sourceTypes
     * @param srcType
     * @return
     * @throws XDIMEException
     */
    private ObjectType determineObjectType(TreeMap sourceTypes,String srcType)
        throws XDIMEException{
        ObjectType calculatedObjectType = null;
        Iterator mediaTypes = sourceTypes.values().iterator();
        while (mediaTypes.hasNext()) {
            String mediaType = (String) mediaTypes.next();
            // Find the object type for this content type.
            ObjectType nextType = null;

            // it is device independent if match to pattern
            if(isDeviceIndependentType(mediaType)) {
                nextType = ObjectType.DEVICE_INDEPENDENT;
            } else if(isDeviceDependentType(mediaType)){
                nextType = ObjectType.DEVICE_DEPENDENT;
            } else {
                throw new XDIMEException(exceptionLocalizer.format(
                        "invalid-content-types",
                        srcType));
            }

            // If we don't already have an object type,
            if (calculatedObjectType == null) {
                // use this one
                calculatedObjectType = nextType;
            } else if (nextType != objectType) {
                throw new XDIMEException(exceptionLocalizer.format(
                    "invalid-content-types",
                    srcType));
            }
        }
        return calculatedObjectType;
    }

    /**
     * Fill src type if initial empty - it means it was determined basing on
     * mimeType heurisitc
     * @param srcTypes
     * @param objectAttributes
     * @throws XDIMEException
     */
    private void fillSrcTypeIfEmpty(TreeMap srcTypes,ObjectAttribute objectAttributes)
        throws XDIMEException{
        if(null == objectAttributes.getSrcType()){
            if((null != srcTypes) && (!srcTypes.isEmpty())){
                objectAttributes.setSrcType(srcTypes.get(srcTypes.firstKey()).toString());
            }   else {
                throw new XDIMEException(exceptionLocalizer.format(
                        "invalid-resource", src));
            }
        }
    }

    /**
     * create source types tree from srcType or from mimeType heuristic
     * @param objectAttributes
     * @return
     * @throws XDIMEException
     */
    private TreeMap createSourceTypesTree(ObjectAttribute objectAttributes) throws XDIMEException {
        TreeMap treeMap = null;
        String srcType = objectAttributes.getSrcType();

        if (srcType != null && srcType.length() > 0) {
            // Parse the srcTypes provided to extract the set of content types
            // contained within.
            treeMap = parseSrcType(srcType);
        } else {
            // If they do not specify content types, then we guess a set of
            // media types from the extension using a heuristic. Note this
            // means that old style remote repository URIs using .xml extension
            // must be accompanied by an x-mcs-* media type as the media
            // type cannot be guessed from the extension.
            treeMap = new TreeMap();

            // get path from src
            URI uri = null;
            try {
                uri = new URI(src);
            } catch (URISyntaxException e) {
                throw new XDIMEException(exceptionLocalizer.format(
                        "invalid-resource", src));
            }

            String mimeType = detectMimeType(context,uri.getPath());
            if (mimeType != null) {
                treeMap.put(new Double(1.0), mimeType);
            } else {
                throw new XDIMEException(exceptionLocalizer.format(
                        "invalid-resource", src));
            }
            // Note: sourceTypes may validly be empty at this point. This means
            // that we had URI to an unrecognised device dependent content type.
        }
        return treeMap;
    }


    /**
     * check if mime type is device dependent
     * @param mimeType
     * @return true when it is device independent and belongsto independend resources set
     */
    private boolean isDeviceIndependentType(String mimeType){
        return mimeType.matches(INDEPENDENT_PATERN) &&
                independentResources.contains(mimeType);
    }

    /**
     * check if mimeType is device depented
     * @param mimeType
     * @return true if it is not device independent
     */
    private boolean isDeviceDependentType(String mimeType){
        return !mimeType.matches(INDEPENDENT_PATERN);
    }

    /**
     * return mime type detected from file extension
     * @param context
     * @param path
     * @return
     */
    private String detectMimeType(XDIMEContextInternal  context,String path){
        int lastDot = path.lastIndexOf('.');
        String resultMimeType = null;

        if (lastDot != -1) {
            MarinerPageContext pageContext = getPageContext(context);
            EnvironmentContext envContext = pageContext.getEnvironmentContext();
            resultMimeType = envContext.getMimeType(path);
        }
        return resultMimeType;
    }
    
    /**
     * Parse a string in the <a
     * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">form of an
     * accept header</a> into a sorted map of q-values (Double) to media type
     * (String).
     *
     * @param acceptHeader a string in the form of an accept header.
     * @return a sorted map of q-values to media types.
     * @throws XDIMEException
     */
    private TreeMap parseSrcType(String acceptHeader)
            throws XDIMEException {

        // todo: this code is duplicated in AcceptParser. Refactor.

        TreeMap result = new TreeMap();

        StringTokenizer commas = new StringTokenizer(acceptHeader, ",");
        while (commas.hasMoreTokens()) {

            // Parse a declaration in the form "value;q=x.y"
            String listValue = commas.nextToken();
            String name;

            Double qValue = new Double(1.0);

            // Search for the first ;
            // We expect the list value to have only one parameter - q
            int semicolon = listValue.indexOf(";");
            if (semicolon >= 0) {
                // We found a semicolon, so parse out the qvalue
                name = listValue.substring(0, semicolon).trim();
                String parameter = listValue.substring(semicolon + 1);
                StringTokenizer equals = new StringTokenizer(parameter,
                                                             "=");
                if (equals.hasMoreTokens() &&
                    equals.nextToken().trim().equals("q") &&
                    equals.hasMoreTokens()) {
                    String qString = equals.nextToken().trim();
                    try {
                        double qFoundValue = Double.parseDouble(qString);
                        if (qFoundValue < 0.0 || qFoundValue > 1.0) {
                            throw new XDIMEException(exceptionLocalizer.format(
                                "invalid-q-value",
                                qString));
                        } else {
                            qValue = new Double(qFoundValue);
                        }
                    } catch (NumberFormatException e) {
                        throw new XDIMEException(exceptionLocalizer.format(
                            "invalid-q-value",
                            qString),
                                                 e);
                    }
                } else {
                    // else, ignore invalid q parameters
                }
            } else {
                name = listValue.trim();
            }

            result.put(qValue, name);
        }
        return result;
    }

    // Javadoc inherited.
    protected void callCloseOnProtocol(XDIMEContextInternal context)
        throws XDIMEException {

        final MarinerPageContext pageContext = getPageContext(context);        
        pageContext.popOutputBuffer(bodyContentBuffer);
                
        // URIs with device dependent types (e.g. image/gif) are handled
        // differently to URIs with device independent types (e.g
        // application/x-mcs-image). Note that all types apart from the x-mcs-*
        // types are assumed to be device dependent.
        if (objectType == null ||
            objectType == ObjectType.DEVICE_DEPENDENT) {
            // We have device dependent content.
            writeDeviceDependentObject(context);
        } else {
            // We have device independent content.
            writeDeviceIndependentObject(context);
        }
    }

    /**
     * Write out a device dependent object. This will involve optionally
     * transcoding the URI if it is an image type, and then calling the
     * protocol to render the URI appropriately.
     *
     * @param context
     * @return true if the markup was written.
     * @throws XDIMEException
     */
    private void writeDeviceDependentObject(XDIMEContextInternal context)
            throws XDIMEException {

        DeviceDependentTypeProcessor deviceDependentTypeProcessor =
                new DeviceDependentTypeProcessor();
        deviceDependentTypeProcessor.writeObject(context,(ObjectAttribute)protocolAttributes,
                bodyContentBuffer);
    }

    /**
     * Write out a device independent object. This will involve calling
     * device independent type processor.
     *
     * @param context
     * @throws XDIMEException
     */
    private void writeDeviceIndependentObject(XDIMEContextInternal context)
            throws XDIMEException {

        DeviceIndependentTypeProcessor deviceIndependentTypeProcessor =
                new DeviceIndependentTypeProcessor();
        deviceIndependentTypeProcessor.writeObject(context,(ObjectAttribute)protocolAttributes,
                bodyContentBuffer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	9839/3	geoff	VBM:2005101702 Fix the XDIME2 Object element

 12-Oct-05      9673/6  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05      9673/4  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 06-Oct-05      9736/1  pabbott VBM:2005100512 Add the XHTML2 object testcase

 30-Sep-05      9562/6  pabbott VBM:2005092011 Add XHTML2 Object element

 22-Sep-05      9128/6  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05      9128/4  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05      9128/2  pabbott VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
