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
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.CaptionSideKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Object responsible for rendering object element markup
 * Algorithm of choosing which markup to render (anchor,div,span,object,image,text) is implemented
 * here. When type of markup is specified suitable methods on protocol are called. 
 */
public class ProtocolIndependentObjectRenderer {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer messageLocalizer =
        LocalizationFactory.createMessageLocalizer(ProtocolIndependentObjectRenderer.class);


    /**
     * acceptable  mimetypes
     */
    private static String IMAGE_TYPE = "image/";
    private static String AUDIO_TYPE = "audio/";
    private static String VIDEO_TYPE = "video/";
    private static String TEXT_TYPE = "text/";
    private static String FLASH_TYPE = "application/x-shockwave-flash";

    /**
     *  allowable values of mcs-media-style
     */
    private static String MCS_MEDIA_STYLE_EXTERNAL = "external";
    private static String MCS_MEDIA_STYLE_INTEGRAL = "integral";

    /**
     * name of param that contains text used when anchor is generated output
     */
    private static String MCS_EXTERNAL_LABEL = "mcs-external-label";

    /**
     * Map mapping mimeType to device policy which determine external/integral 
     * support for resources 
     */
    private static Map mediaIntegralSupportMap = new HashMap();
    static {
    	
    	mediaIntegralSupportMap.put("image/gif", "gifinpage");
    	mediaIntegralSupportMap.put("image/png", "pnginpage");
    	mediaIntegralSupportMap.put("image/jpeg", "jpeginpage");    	
    	mediaIntegralSupportMap.put("image/bmp", "bmpinpage");
    	mediaIntegralSupportMap.put("image/vnd.wap.wbmp", "bmpinpage");
    	mediaIntegralSupportMap.put("image/vnd.videotex", "videotexinpage");    
    	mediaIntegralSupportMap.put("image/tiff", "tiffinpage");

    	mediaIntegralSupportMap.put("audio/amr", "amrinpage");
    	mediaIntegralSupportMap.put("audio/x-rmf", "rmfinpage");
    	mediaIntegralSupportMap.put("audio/basic", "baudioinpage");
    	mediaIntegralSupportMap.put("audio/x-gsm", "gsmaudioinpage");
    	mediaIntegralSupportMap.put("audio/sp-midi", "spmidiinpage");
    	mediaIntegralSupportMap.put("audio/amr", "amrinpage");
    	mediaIntegralSupportMap.put("audio/x-wav", "wavinpage");
    	mediaIntegralSupportMap.put("audio/wav", "wavinpage");
    	mediaIntegralSupportMap.put("audio/mp3", "mp3inpage");
    	mediaIntegralSupportMap.put("audio/x-pn-realaudio", "realaudioinpage");
    	mediaIntegralSupportMap.put("audio/ms-wma", "msmainpage");
    	mediaIntegralSupportMap.put("audio/midi", "midiinpage");
    	
    	mediaIntegralSupportMap.put("video/vnd.rn-realvideo", "realvidinpage");
    	mediaIntegralSupportMap.put("video/quicktime", "qtimeinpage");
    	mediaIntegralSupportMap.put("video/x-ms-wmv", "msvidinpage");
    	mediaIntegralSupportMap.put("video/mpeg", "mpeg4inpage");
    	
    	// all resources with video/mpeg mime type will check integral 
    	// support against mpeg4inpage device policy. Is it correct?
    	// mediaStyles.put("video/mpeg", "mpeg1inpage");
    	
    	mediaIntegralSupportMap.put("application/x-director", "swinpage");
    	mediaIntegralSupportMap.put("application/x-shockwave-flash", "flashinpage");
    	mediaIntegralSupportMap.put("application/vnd.smaf", "smafinpage");    	
    	mediaIntegralSupportMap.put("application/vnd.nokia.ringing-tone", "nokringinpage");    	
    }
    
    /**
     * reference to protocol
     */
    VolantisProtocol protocol;

    /**
     * reference to context
     */
    MarinerPageContext context;

    /**
     * buffer to collecting output betwen open and close
     */
    OutputBuffer objectContentOutputBuffer;

    /**
     * type of object
     */
    ObjectType objectType;

    /**
     * attributes passed to protocol when output markup is written out
     */
    MAPAttributes markupSpecificAttributes;

    public ProtocolIndependentObjectRenderer(VolantisProtocol protocol){
        this.protocol = protocol;
        context = protocol.getMarinerPageContext();
    }

    /**
     * generate open markup for object
     * @param attributes
     * @throws ProtocolException
     */
    public void writeOpenObject(ObjectAttribute attributes)
        throws ProtocolException{

        checkRendererStateIntegrity();

        String mediaStyle = getMediaStyle(attributes);
        if(MCS_MEDIA_STYLE_EXTERNAL.equals(mediaStyle)) {
            objectType = ObjectType.ANCHOR;
        } else if (MCS_MEDIA_STYLE_INTEGRAL.equals(mediaStyle)) {
            objectType = determineOutputObjectType(attributes);            
        } else {
        	objectType = null;
        }
                	
        markupSpecificAttributes
                = createMarkupSpecificAttributes(attributes, objectType);
        
        writeOpenMarkup(attributes,markupSpecificAttributes,objectType);

        OutputBuffer objectContentOutputBuffer =
            protocol.getOutputBufferFactory().createOutputBuffer();
        context.pushOutputBuffer(objectContentOutputBuffer);
    }


    /**
     * generate content and close markup for object
     * @param attributes
     * @throws ProtocolException
     */
    public void writeCloseObject(ObjectAttribute attributes)
            throws ProtocolException{
        OutputBuffer objectContentOutputBuffer = context.getCurrentOutputBuffer();
        context.popOutputBuffer(objectContentOutputBuffer);

        writeCloseMarkup(attributes,markupSpecificAttributes,objectType,objectContentOutputBuffer);

        ElementFinalizer finalizer = null;
        
        if (markupSpecificAttributes instanceof ImageAttributes) {
            finalizer = ((ImageAttributes) markupSpecificAttributes).getFinalizer();
        } else if (markupSpecificAttributes instanceof AnchorAttributes) {
            finalizer = ((AnchorAttributes) markupSpecificAttributes).getFinalizer();
        } else if (markupSpecificAttributes instanceof ObjectAttribute) {
            finalizer = ((ObjectAttribute) markupSpecificAttributes).getFinalizer();
        }

        if(finalizer != null){
            attributes.setFinalizer(createWrappingFinalizer(markupSpecificAttributes,finalizer));
        }

        cleanRendererState();
    }

    /**
     * Check if open is not called for the second time without close
     * it shouldn't hapend, when it happends it means that way
     * of generating object from ObjectElement was changed, and therefore
     * protocol methods for object also must be changed.
     * @todo currently renderer is stateful but perhaps should be converted into stateless
     * @throws ProtocolException
     */
    private void checkRendererStateIntegrity() throws ProtocolException{
        if(objectType != null){
            throw new ProtocolException("This is statefull object and open should" +
                    " not be called second time before close");
        }
    }

    /**
     * null all global variables to clean renderer state.
     * @todo currently renderer is stateful but perhaps should be converted into stateless
     */
    private void cleanRendererState(){
        objectContentOutputBuffer = null;
        markupSpecificAttributes = null;
        objectType = null;
    }

    /**
     * create finalizer that pass execute method to concrete finalizer
     * @param internalFinalizer
     * @return
     */
    private ElementFinalizer createWrappingFinalizer(final MAPAttributes markupSpecificAttributes,
                                                     final ElementFinalizer internalFinalizer){
        ElementFinalizer finalizer;
        finalizer = new ElementFinalizerWrapper(internalFinalizer){
            protected MAPAttributes getMarkupSpecificAttributes() {
                return markupSpecificAttributes;
            }
        };
        return finalizer;
    }

    /**
     * Parse the contents buffer and extract raw, unformated text for inclusion
     * in the altText of the ImageAttribute.
     *
     * @param contentBuffer from which text will be extracted.
     * @return a String object which may be zero length.
     */
    private String retrieveAltText(OutputBuffer contentBuffer) {

        final StringBuffer altTextBuffer = new StringBuffer();

        if (contentBuffer instanceof DOMOutputBuffer) {
            DOMOutputBuffer domBuffer = (DOMOutputBuffer)contentBuffer;
            Element root = domBuffer.getRoot();
            WalkingDOMVisitor visitor = new WalkingDOMVisitorStub() {

                // Javadoc inherited.
                public void visit(Text text) {
                    if (text.getLength() > 0 && !text.isWhitespace()) {
                        altTextBuffer.append(
                            text.getContents(), 0, text.getLength());
                    }
                }
            };
            DOMWalker walker = new DOMWalker(visitor);

            // We can't pass the root node into walk() as the root node
            // has no name and that results in an exception.
            root.forEachChild(walker);

        }
        return altTextBuffer.toString();
    }

    /**
     * write output markup specified by objectType
     * @param originalAttributes
     * @param newAttributes
     * @param objectType
     * @throws ProtocolException
     */
    private void writeOpenMarkup(ObjectAttribute originalAttributes,
                             MAPAttributes newAttributes,
                             ObjectType objectType
                             )
        throws ProtocolException{

        if (objectType == ObjectType.OBJECT) {
            protocol.directWriteOpenObject((ObjectAttribute)newAttributes);
        } else if(objectType == ObjectType.ANCHOR) {
            protocol.writeOpenAnchor((AnchorAttributes)newAttributes);
        } else if(objectType == ObjectType.IMAGE) {
            if (originalAttributes.needsFinalizer()) {
                ImageAttributes imageAttributes =(ImageAttributes)newAttributes;
                this.writeImageMarkup(imageAttributes,originalAttributes);
            }
            // if not we can move writeImageMarkup into closeMarkup method
            // when all attributes will be collected - altText
        }
    }

    private void writeCloseMarkup(ObjectAttribute originalAttributes,
                             MAPAttributes newAttributes,
                             ObjectType objectType,
                             OutputBuffer outputBuffer
                             )
        throws ProtocolException{

        if (objectType == ObjectType.OBJECT){
            context.getCurrentOutputBuffer().transferContentsFrom(outputBuffer);
            protocol.directWriteCloseObject((ObjectAttribute)newAttributes);
        } else if (objectType == ObjectType.ANCHOR) {
            Map paramMap = originalAttributes.getParamMap();
            Object externalLabelObject = paramMap.get(MCS_EXTERNAL_LABEL);
            if(null != externalLabelObject){
                context.getCurrentOutputBuffer().writeText(externalLabelObject.toString());
            } else {
                // render default external lab if not defined by user inside object tag
                context.getCurrentOutputBuffer().writeText(messageLocalizer.format("default-mcs-external-label"));
            }
            protocol.writeCloseAnchor((AnchorAttributes)newAttributes);
        } else if (objectType == ObjectType.IMAGE) {
            ImageAttributes imageAttributes =(ImageAttributes)newAttributes;
            imageAttributes.setAltText(retrieveAltText(outputBuffer));
            if (!originalAttributes.needsFinalizer()) {
                // because wasn't written before
                this.writeImageMarkup(imageAttributes,originalAttributes);
            }
        } else if (objectType == ObjectType.TEXT) {
            this.writeText(originalAttributes);
        } else {
            context.getCurrentOutputBuffer().transferContentsFrom(outputBuffer);
        }
    }


    /**
     * Write out the object with the supplied text, and a caption if available.
     *
     * @param attributes the text to write.
     * @throws ProtocolException
     */
    public void writeText(ObjectAttribute attributes) throws ProtocolException {
        if (hasCaptionContent(attributes.getCaptionContent())) {
            writeCaptionedText(attributes);
        } else {
            writeWrappedText(attributes);
        }
    }


    /**
     * Write text inside caption.
     * @param attributes
     * @throws ProtocolException
     */
    private void writeCaptionedText(final ObjectAttribute attributes) throws ProtocolException{
        final Content textContent = new Content() {
            public void addContent() {
                context.getCurrentOutputBuffer().writeText(attributes.getTextContainer());
            }
        };
        Captioner captioner = new Captioner();
        captioner.caption(textContent,attributes.getCaptionContent(),attributes);
    }

    /**
     * Write the supplied content surrounded by a container element whose type
     * is derived from the CSS display property of the supplied attribute's
     * styles.
     *
     * @param attributes the attributes containing styles
     * @throws ProtocolException
     */
    private void writeWrappedText(ObjectAttribute attributes)
            throws ProtocolException {

        writeOpenContainerElement(attributes);
        context.getCurrentOutputBuffer().writeText(attributes.getTextContainer());
        writeCloseContainerElement(attributes);
    }

    /**
     * Write image with or without caption
     * @param imageAttributes
     * @param objectAttributes
     * @throws ProtocolException
     */
    private void writeImageMarkup(ImageAttributes imageAttributes,
                                  ObjectAttribute objectAttributes) throws ProtocolException{
        if (hasCaptionContent(objectAttributes.getCaptionContent())) {
            writeCaptionedImage(imageAttributes,objectAttributes.getCaptionContent());
        } else {
            protocol.writeImage(imageAttributes);
        }
    }

    /**
     * check if there is caption content
     * @param captionContentBuffer
     * @return true if captionContentBuffer is not empty
     */
    private boolean hasCaptionContent(OutputBuffer captionContentBuffer){
        return captionContentBuffer != null &&
                !captionContentBuffer.isEmpty();

    }

    /**
     * write image as caption
     * @param imageAttributes
     * @throws ProtocolException
     */
    private void writeCaptionedImage(final ImageAttributes imageAttributes,
                                     OutputBuffer captionContentBuffer) throws ProtocolException{
        final Content imageContent = new Content() {
            public void addContent() throws ProtocolException {
                protocol.writeImage(imageAttributes);
            }
        };
        Captioner captioner = new Captioner();
        captioner.caption(imageContent,captionContentBuffer,imageAttributes);
    }

    /**
     * Implementations of the interface may be called to write out their
     * content.
     */
    private interface Content {

        /**
         * Write out the content contained within this object.
         *
         * @throws ProtocolException
         */
        void addContent() throws ProtocolException;
    }

    /**
     * Manage the addition of captions for the object.
     * <p>
     * Captions must be added either at the top or bottom of the object content.
     */
    public class Captioner {

        void caption(Content content,OutputBuffer captionContentBuffer,MCSAttributes attributes)
                throws ProtocolException {

            if (!hasCaptionContent(captionContentBuffer)) {
                throw new IllegalStateException();
            }

            MCSAttributes displayBlockAttributes =
                    createDisplayBlockAttributes(attributes);

            // If we have a caption then we force the entire object to be
            // display:block by surrounding it with a block element. We need to
            // do this because it is the only way that having a caption makes
            // sense.
            writeOpenContainerElement(displayBlockAttributes);

            final MutablePropertyValues propertyValues =
                    attributes.getStyles().getPropertyValues();
            StyleValue captionSide = propertyValues.getComputedValue(
                    StylePropertyDetails.CAPTION_SIDE);
            if (captionSide instanceof StyleKeyword) {
                final OutputBuffer buffer =
                        context.getCurrentOutputBuffer();
                if (captionSide == CaptionSideKeywords.TOP) {
                    // First add the caption
                    buffer.transferContentsFrom(captionContentBuffer);
                    // And then the content.
                    content.addContent();
                } else if (captionSide == CaptionSideKeywords.BOTTOM) {
                    // First add the content.
                    content.addContent();
                    // And then the caption.
                    buffer.transferContentsFrom(captionContentBuffer);
                } else {
                    throw new IllegalStateException();
                }
            }
            writeCloseContainerElement( displayBlockAttributes);
        }
    }


    /**
     * Create MCS attributes with display:block from the attributes supplied.
     *
     * @param attributes the attributes to copy.
     * @return the copied attributes with display:block set.
     */
    MCSAttributes createDisplayBlockAttributes(MCSAttributes attributes) {
        MCSAttributes result = new MCSAttributes() { };
        result.copy(attributes);
        StyleValue block = DisplayKeywords.BLOCK;
        result.getStyles().getPropertyValues().setComputedValue(
                StylePropertyDetails.DISPLAY, block);
        return result;
    }

    /**
     * Write out the close tag of a container element whose type is derived from
     * the CSS display property of the supplied attribute's styles.
     *
     * @param attributes the attributes containing styles.
     */
    private void writeCloseContainerElement(MCSAttributes attributes) {
        if (isDisplayBlock(attributes)) {
            DivAttributes divAttributes = new DivAttributes();
            divAttributes.copy(attributes);
            protocol.writeCloseDiv(divAttributes);
        } else {
            SpanAttributes spanAttributes = new SpanAttributes();
            spanAttributes.copy(attributes);
            protocol.writeCloseSpan(spanAttributes);
        }
    }

    /**
     * Write out the open tag of a container element whose type is derived from
     * the CSS display property of the supplied attribute's styles.
     * <p>
     * For example, if the the styles have display:inline the container element
     * may be a span, or if they styles have display:block the container element
     * may be a div.
     *
     * @param attributes the attributes containing styles.
     * @throws ProtocolException
     */
    private void writeOpenContainerElement(MCSAttributes attributes)
            throws ProtocolException {
        if (isDisplayBlock(attributes)) {
            DivAttributes divAttributes = new DivAttributes();
            divAttributes.copy(attributes);
            protocol.writeOpenDiv(divAttributes);
        } else {
            SpanAttributes spanAttributes = new SpanAttributes();
            spanAttributes.copy(attributes);
            protocol.writeOpenSpan(spanAttributes);
        }
    }

    /**
     * Return true if the supplied attributes styles have display:block.
     *
     * @param attributes the attributes containing styles to check.
     * @return true if display:block, false otherwise.
     */
    private boolean isDisplayBlock(MCSAttributes attributes) {
        boolean block = false;
        final MutablePropertyValues propertyValues =
                attributes.getStyles().getPropertyValues();
        if (propertyValues.getComputedValue(StylePropertyDetails.DISPLAY)
                == DisplayKeywords.BLOCK) {
            block = true;
        }
        return block;
    }

    /**
     * Hold possible object types
     */
    public static class ObjectType {
        public static ObjectType OBJECT = new ObjectType("object");
        public static ObjectType IMAGE = new ObjectType("image");
        public static ObjectType ANCHOR = new ObjectType("anchor");
        public static ObjectType TEXT = new ObjectType("text");

        private String typeName;

        private ObjectType(String typeName){
            this.typeName =  typeName;
        }

        public String getTypeName(){
            return this.typeName;
        }

    }

    /**
     * Check if object is external type, intergral or none of them
     *
     * @param attributes attributes containing styles                                                
     * @return three possible values: "external", "integral" or null
     */
    private String getMediaStyle(ObjectAttribute attributes){

        // In general the algorithm is: The values in the style property are combined
        // with those device policies that determine whether it supports the specific media
        // as an integral part of the page, externally, or both. Any value that is not supported
        // by the device is ignored and then the first remaining value, if any, determines
        // the markup to generate. If there are no remaining values then the <object> element
        // is ignored and its body is processed instead.


        StyleValue styleValueList = attributes.getStyles().getPropertyValues().
                getComputedValue(StylePropertyDetails.MCS_MEDIA_STYLE);

    	String result = null;

        List list = ((StyleList)styleValueList).getList();
        Iterator i = list.iterator();
        while (i.hasNext()) {

            StyleValue value = (StyleValue) i.next();
            String mediaValue = value.getStandardCSS();

            if (mediaValue.equals(MCS_MEDIA_STYLE_EXTERNAL)) {
                // This is not restricted by any device policy,
                // so if it's in style value, we can use it.
                result = MCS_MEDIA_STYLE_EXTERNAL;
                break;
            } else if(mediaValue.equals(MCS_MEDIA_STYLE_INTEGRAL)) {
                // get appropriate policy name
                String policyName = (String)mediaIntegralSupportMap.get(attributes.getSrcType());

                // If this is not restricted by any device policy (null == policyName)
                // or device policy is set to true, we may use this value
                if ((null == policyName)
                    || (context.getDevice().getBooleanPolicyValue(policyName))) {
                    result = MCS_MEDIA_STYLE_INTEGRAL;
                    break;
                }
            }
        }
        return result;
    }


    /**
     * Get object type what determines markup type that generated as output
     * It works according to agorithm specified in
     * http://mantis.uk.volantis.com:8080/mantis/Mantis_View.jsp?mantisid=2007041801
     * @param attributes original object attributes
     * @return
     */
    public ObjectType determineOutputObjectType(ObjectAttribute attributes){
        ObjectType result = null;
        if(null != attributes.getTextContainer()){
            result = ObjectType.TEXT;
        } else if(null != attributes.getSrcType()){
            String srcType = attributes.getSrcType();
            if(srcType.startsWith(AUDIO_TYPE) ||
                srcType.startsWith(VIDEO_TYPE) ||
                srcType.startsWith(FLASH_TYPE)){
                result = ObjectType.OBJECT;
            } else if(srcType.startsWith(IMAGE_TYPE) && (attributes.getSrc() != null)){
                result = ObjectType.IMAGE;
            }
        }
        return result;
    }

    /**
     * Create attributes for specified output type: anchor,image,object
     * @param attributes original object attributes
     * @param objectType
     * @return attributes specific to generated ouptut type
     */
    public MAPAttributes createMarkupSpecificAttributes(ObjectAttribute attributes,
                                                 ObjectType objectType){
        MAPAttributes newAttributes = null;
        
        if (objectType == ObjectType.ANCHOR){
            newAttributes = new AnchorAttributes();
        } else if (objectType == ObjectType.IMAGE) {
            newAttributes = new ImageAttributes();
        } else if (objectType == ObjectType.OBJECT) {
            newAttributes = new ObjectAttribute();
        }
        
        //if (!attributes.needsFinalizer()) {
            updateMarkupSpecificAttributes(attributes, 
                    newAttributes);
        //}
        
        return newAttributes;
    }
    
    public void updateMarkupSpecificAttributes(ObjectAttribute attributes,
            MAPAttributes objectSpecificAttributes) {

        if (objectSpecificAttributes instanceof AnchorAttributes) {
            updateAnchorAttributes(attributes, 
                    (AnchorAttributes) objectSpecificAttributes);
        } else if (objectSpecificAttributes instanceof ImageAttributes) {
            updateImageAttributes(attributes, 
                    (ImageAttributes) objectSpecificAttributes);
        } else if (objectSpecificAttributes instanceof ObjectAttribute) {
            updateObjectAttributes(attributes, 
                    (ObjectAttribute) objectSpecificAttributes);
        }
    }

    /**
     * Create image attributes used to generate output markup when image
     * @param originalAttributes objectAttributes from ObjectElement
     */
    private void updateImageAttributes(ObjectAttribute originalAttributes,
            ImageAttributes imageAttributes) {
        imageAttributes.copy(originalAttributes);

        imageAttributes.setSrc(originalAttributes.getSrc());
        if(hasCaptionContent(originalAttributes.getCaptionContent())){
            imageAttributes.setId(null);
        }

        Map paramMap = originalAttributes.getParamMap();
        Object width = paramMap.get("width");
        if(width != null){
            imageAttributes.setWidth(width.toString());
        }
        Object height = paramMap.get("height");
        if(height != null){
            imageAttributes.setHeight(height.toString());
        }
        if(originalAttributes.needsFinalizer()){
            imageAttributes.setNeedsFinalizer();
        }
        imageAttributes.setConvertibleImageAsset(
                originalAttributes.isConvertibleImageAsset());
    }

    /**
     * Create object attributes used to generate output markup when object
      * @param originalAttributes
     */
    private void updateObjectAttributes(ObjectAttribute originalAttributes,
            ObjectAttribute objectAttribute) {
        objectAttribute.copy(originalAttributes);
        objectAttribute.setSrc(originalAttributes.getSrc());
        objectAttribute.setSrcType(originalAttributes.getSrcType());
        if(originalAttributes.needsFinalizer()){
            objectAttribute.setNeedsFinalizer();
        }
    }

    /**
     * create anchor attributes used to generate outptu markup when anchor
     * @param originalAttributes
     * @return
     */
    private void updateAnchorAttributes(ObjectAttribute originalAttributes,
            AnchorAttributes anchorAttributes) {
        anchorAttributes.copy(originalAttributes);
        //if(null != originalAttributes.getSrcType()){
            anchorAttributes.setHref(originalAttributes.getSrc());
        //}
        if(originalAttributes.needsFinalizer()){
            anchorAttributes.setNeedsFinalizer();
        }
    }

    /**
     * Finalizer Wrapper acting as a proxy between object, that is generic object without
     * knowledge about output type (anchor,object,href,image) and concrete output (one from mentioned types)
     */
    private abstract class ElementFinalizerWrapper implements ElementFinalizer {

        private ElementFinalizer concreteFinalizer;

        public ElementFinalizerWrapper(ElementFinalizer internalFinalizer){
            this.concreteFinalizer = internalFinalizer;
        }

        public void finalizeElement(MAPAttributes attributes) throws ProtocolException {
            MAPAttributes markupSpecificAttributes = getMarkupSpecificAttributes();
            
            updateMarkupSpecificAttributes(
                    (ObjectAttribute) attributes,
                    markupSpecificAttributes); 
            
            concreteFinalizer.finalizeElement(markupSpecificAttributes);
        }

        abstract protected  MAPAttributes getMarkupSpecificAttributes();
    };
}
