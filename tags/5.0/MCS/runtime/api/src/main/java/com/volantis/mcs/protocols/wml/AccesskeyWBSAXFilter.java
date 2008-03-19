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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.AttributeValueCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.NullReferenceResolver;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXFilterHandler;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class translates elements with "logical" access key value references
 * into "physical" access key value references, in order to write out
 * accesskey numbers which are in a 1 to 9 sequence (only).
 * <p/>
 * This is required here because when the protocol writes out the accesskey
 * values, it does not know which bits may be dissected away. Thus, we must
 * calculate these values after dissection.
 * <p/>
 * It looks for an element of the following format:
 * <pre>
 *   &lt;elt accesskey="x"&gt;x ...&lt;elt&gt;
 * </pre>
 * If it finds one, it will look for any AccesskeyOpaqueValue attribute and
 * content values in an attempt to replace the logical value (shown with an
 * "x") with the physical value that it has calculated.
 * <p/>
 * Once it has counted up to 9, all the accesskeys are used up, so it must
 * suppress these values (and possibly the entire accesskey attribute) for
 * values larger than 9.
 * <p/>
 * NOTE: this assumes that an accesskey attribute is either a normal one with
 * as many attribute values as it wants, or a special one with a single
 * accesskey opaque value (and NO others). Any more than this will cause this
 * to fail ungracefully.
 * <p/>
 * NOTE: this means that pages with logical accesskey references that exceed
 * 9 will actually be slightly smaller than is calculated by the dissector.
 * This is not good, as dissection is supposed to be accurate, but should only
 * happen in very few places, and since it shrinks rather than grows the page
 * size, it will not blow up the phone. Basically, its not worth trying to
 * get the dissector to calculate these sizes correctly.
 * <p/>
 * NOTE: it would be easier to implement this at the WBDOM level as these
 * kinds of changes are cleaner using a DOM. However, there is no current
 * interface to plug in things which affect the way a DOM is serialised, so
 * for now it's easier to do a WBSAX filter. In future it is proposed that
 * there be a WBDOM optimisation step in the dissection process, but even this
 * would probably not help us as this would be optimisation that is common to
 * all renderings of a page (i.e. it makes permanent changes to the DOM).
 */
public final class AccesskeyWBSAXFilter extends WBSAXFilterHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AccesskeyWBSAXFilter.class);

    /**
     * The max value that can be assigned to an accesskey attribute.
     */
    private static final int MIN_ACCESSKEY_VALUE = 1;

    /**
     * The max value that can be assigned to an accesskey attribute.
     */
    private static final int MAX_ACCESSKEY_VALUE = 9;

    /**
     * The name of the accesskey attribute.
     */
    private static final String ACCESSKEY_ATTRIBUTE = "accesskey";

    /**
     * Codec used to create new string values when necessary.
     */
    private final Codec codec;

    /**
     * Integer value of the attribute start code for the accesskey attribute.
     */
    private final int accesskeyCode;

    /**
     * Single instance of literal cached element start, to conserve memory.
     */
    private final LiteralCachedElementStart literalElementStart =
            new LiteralCachedElementStart();

    /**
     * Single instance of a coded cached element start, to conserve memory.
     */
    private final CodedCachedElementStart codedElementStart =
            new CodedCachedElementStart();

    /**
     * Single instance of a literal cached attribute start, to conserve memory.
     */
    private final LiteralCachedAttributeStart literalAttributeStart =
            new LiteralCachedAttributeStart();

    /**
     * Single instance of a coded cached attribute start, to conserve memory.
     */
    private final CodedCachedAttributeStart codedAttributeStart =
            new CodedCachedAttributeStart();

    /**
     * The cached element start, or null if we currently have none.
     */
    private CachedElementStart cachedElement;

    private CachedElementStart lastCachedElement;
    /**
     * The cached first attribute start, or null if we currently have none.
     */
    private CachedAttributeStart cachedAttributeStart;

    /**
     * True if we have already found an attribute for this element.
     */
    private boolean attributeFound;

    /**
     * The value to use for the current access key attribute.
     * <p/>
     * Note that we use a pre-incrementing strategy for this attribute which
     * means that it will be incremented to the minimum value before it is
     * used. This is because it's value must be maintained after the accesskey
     * attribute is written so that any content prefix can pick up the correct
     * value.
     */
    private int accessKeyValue = MIN_ACCESSKEY_VALUE - 1;

    /**
     * Construct an instance of this class.
     *
     * @param codec   the codec used to create new string values, if necessary.
     * @param handler the WBSAX content handler we are filtering.
     */
    public AccesskeyWBSAXFilter(
            Codec codec, WBSAXContentHandler handler,
            AttributeStartFactory factory) {
        super(handler, new NullReferenceResolver());
        this.codec = codec;
        // Calculate the int that the accesskey attribute value corresponds to.
        accesskeyCode = factory.create("accesskey", null).getInteger();
    }

    /*
    - if a start element is found 
      - if it has attributes,
        - ensure the cache is empty
        - cache the start element (cache = se)
        - reset attribute count
    */

    // Inherit Javadoc.
    public void startElement(
            ElementNameCode name, boolean attributes,
            boolean content) throws WBSAXException {
        if (attributes) {
            codedElementStart.initialise(name, content);
            cacheElement(codedElementStart);
            attributeFound = false;
        } else {
            super.startElement(name, attributes, content);
        }
        lastCachedElement = null;
    }

    // Inherit Javadoc.
    public void startElement(
            StringReference name, boolean attributes,
            boolean content) throws WBSAXException {
        if (attributes) {
            literalElementStart.initialise(name, content);
            cacheElement(literalElementStart);
            attributeFound = false;
        } else {
            super.startElement(name, attributes, content);
        }
        lastCachedElement = null;
    }

    /*
    - if we find an end element
    */
    
    public void endElement() throws WBSAXException {
        if (lastCachedElement != null &&
                !(lastCachedElement.attributes || lastCachedElement.content)) {
            // suppress any end element.
        } else {
            super.endElement();
        }
    }
    
    /*
    - if an attribute start is found
      - if it is the first one and it is an accesskey 
        - cache the attribute start (cache = se,sa:ak)
      - increment attribute count
    */
    
    // Inherit Javadoc.
    public void addAttribute(StringReference name)
            throws WBSAXException {
        if (!attributeFound &&
                name.resolveString().getString().equals(ACCESSKEY_ATTRIBUTE)) {
            literalAttributeStart.initialise(name);
            cacheAttributeStart(literalAttributeStart);
        } else {
            flushCache(false);
            super.addAttribute(name);
        }
        attributeFound = true;
    }

    // Inherit Javadoc.
    public void addAttribute(AttributeStartCode start)
            throws WBSAXException {
        if (!attributeFound && start.getInteger() == accesskeyCode) {
            codedAttributeStart.initialise(start);
            cacheAttributeStart(codedAttributeStart);
        } else {
            flushCache(false);
            super.addAttribute(start);
        }
        attributeFound = true;
    }

    /*
    - if an opaque attribute value is found,
      - if it is an AccesskeyOpaqueValue
        - if accesskey-count > 9
          - if cache contains start attribute (cache = se, sa:ak)
            - remove start attribute from cache (cache = se)
        - else
          - if the cache has a value (cache = se, sa:ak)
            - flush the cache.
          - replace dummy value with ++accesskey-count
      - else
        - if necessary, flush the cache 

        NOTE: we do not support > 1 opaque accesskey attribute value being 
        provided per accesskey attribute. Behaviour in this case is undefined.
    */

    // Inherit Javadoc.
    public void addAttributeValueOpaque(OpaqueValue part)
            throws WBSAXException {
        if (part instanceof AccesskeyOpaqueValue) {
            accessKeyValue += 1;
            if (accessKeyValue > MAX_ACCESSKEY_VALUE) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding attribute value opaque: " +
                                 "ignoring accesskey");
                }
                cachedAttributeStart = null;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding attribute value opaque: " +
                                 "using accesskey: " + accessKeyValue);
                }
                flushCache(false);
                AccesskeyOpaqueValue value = (AccesskeyOpaqueValue) part;
                value.setValue(new WBSAXString(codec,
                                               String.valueOf(accessKeyValue)));
                super.addAttributeValueOpaque(value);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding attribute value opaque: not accesskey");
            }
            flushCache(false);
            super.addAttributeValueOpaque(part);
        }
    }

    /*
    - if a non opaque attribute value is found,
      - if necessary, flush the cache 
    */
    
    // Inherit Javadoc.
    public void addAttributeValue(AttributeValueCode part)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValue(part);
    }

    // Inherit Javadoc.
    public void addAttributeValue(StringReference part)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValue(part);
    }

    // Inherit Javadoc.
    public void addAttributeValue(WBSAXString part)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValue(part);
    }

    // Inherit Javadoc.
    public void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValueEntity(entity);
    }

    // Inherit Javadoc.
    public void addAttributeValueExtension(Extension code)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValueExtension(code);
    }

    // Inherit Javadoc.
    public void addAttributeValueExtension(
            Extension code, StringReference value)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValueExtension(code, value);
    }

    // Inherit Javadoc.
    public void addAttributeValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        flushCache(false);
        super.addAttributeValueExtension(code, value);
    }

    /*
    - if a start attributes is found,
      - if we are caching, ignore it (will be generated by at flush time)
     */
    public void startAttributes() throws WBSAXException {
        if (cachedElement == null) {
            super.startAttributes();
        }
    }
    
    /*
    - if an end attributes is found,
      - if necessary, flush the cache (ensuring start element marked as no atts)
    */

    // Inherit Javadoc.
    public void endAttributes()
            throws WBSAXException {
        // If we still have a cached element, then we must have deleted the
        // only attribute start we had.
        if (cachedElement != null) {
            flushCache(true);
        } else {
            super.endAttributes();
        }
    }

    /*
    - if an opaque content value is found,
      - if it is an AccesskeyOpaqueValue and we are prefixing content values 
        - if accesskey-count > 9
          - replace first two chars with nothing
        - else
          - replace first two chars with accesskey-count + " "
    */
    
    // Inherit Javadoc.
    public void addContentValueOpaque(OpaqueValue part)
            throws WBSAXException {

        if (part instanceof AccesskeyOpaqueValue) {
            AccesskeyOpaqueValue value = (AccesskeyOpaqueValue) part;
            if (accessKeyValue > MAX_ACCESSKEY_VALUE) {
                // then throw away the opaque value since we cannot
                // represent access keys greater than the max value.
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding content value opaque: " +
                                 "ignoring accesskey");
                }
            } else {
                // we have a valid accesskey value, so set up the opaque
                // value to act as the simulated prefix.
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding content value opaque: " +
                                 "using accesskey: " + accessKeyValue);
                }
                String accessKeyPrefix = accessKeyValue + " ";
                value.setValue(new WBSAXString(codec, accessKeyPrefix));
                super.addContentValueOpaque(part);
            }
        } else {
            // We are not interested in this opaque value, just pass it thru
            if (logger.isDebugEnabled()) {
                logger.debug("Adding content value opaque: not accesskey");
            }
            super.addContentValueOpaque(part);
        }
    }


    //
    // Helper Methods
    //

    /**
     * Cache the element provided.
     *
     * @param element the element to be cached.
     */
    private void cacheElement(CachedElementStart element) {
        // Check the state of the cache.
        if (cachedElement != null) {
            throw new IllegalStateException("found non-null element");
        }
        if (cachedAttributeStart != null) {
            throw new IllegalStateException("found non-null attribute start");
        }
        // Cache the element.
        cachedElement = element;
    }

    /**
     * Cache the attribute start provided.
     * <p/>
     * This should only be used to cache the first attribute start
     *
     * @param attribute
     */
    private void cacheAttributeStart(CachedAttributeStart attribute) {
        // Check the state of the cache.
        if (cachedElement == null) {
            throw new IllegalStateException("found null element");
        }
        if (cachedAttributeStart != null) {
            throw new IllegalStateException("found non-null attribute start");
        }
        // Cache the attribute.
        cachedAttributeStart = attribute;
    }

    /**
     * Flush the element start and first attribute start from the cache, if
     * they are present.
     *
     * @throws WBSAXException if there was a problem flushing.
     */
    private void flushCache(boolean removeAttributes) throws WBSAXException {
        // Check the state of the cache.
        if (cachedElement == null && cachedAttributeStart != null) {
            throw new IllegalStateException(
                    "found null element and non-null attribute start");
        }
        
        // Flush the cache.
        if (cachedElement != null) {

            if (removeAttributes) {
                // Ensure we haven't tried to cache an attribute
                // Mark the element has having no attributes.
                cachedElement.attributes = false;
            }

            // Flush the element.
            cachedElement.flush();
            // If it was marked as having attributes
            if (cachedElement.attributes) {
                // Flush the start attribute call.
                handler.startAttributes();
                // If we had a cached attribute start already
                if (cachedAttributeStart != null) {
                    // Flush the cached attribute start
                    cachedAttributeStart.flush();
                }
            } else {
                // Not marked as having attributes
                // So just check that our internal state reflects this.
                if (cachedAttributeStart != null) {
                    throw new IllegalStateException(
                            "found cached attribute flushing no-attr element");
                }
            }
            // Save the cached element 
            lastCachedElement = cachedElement;
            cachedElement = null;
            cachedAttributeStart = null;
        }
    }

    //
    // Helper Classes
    //

    private interface Flushable {
        void flush() throws WBSAXException;
    }

    private abstract class CachedElementStart implements Flushable {
        protected boolean attributes;
        protected boolean content;

        void initialise(boolean content) {
            this.attributes = true;
            this.content = content;
        }
    }

    /**
     * Flushable cache for literal element starts.
     */
    private class LiteralCachedElementStart extends CachedElementStart {
        private StringReference name;

        public void initialise(StringReference name, boolean content) {
            super.initialise(content);
            this.name = name;
        }

        public void flush() throws WBSAXException {
            handler.startElement(name, attributes, content);
        }
    }

    /**
     * Flushable cache for coded element starts.
     */
    private class CodedCachedElementStart extends CachedElementStart {
        private ElementNameCode name;

        public void initialise(ElementNameCode name, boolean content) {
            super.initialise(content);
            this.name = name;
        }

        public void flush() throws WBSAXException {
            handler.startElement(name, attributes, content);
        }
    }

    /**
     * "Marker" superclass for cached attribute starts.
     */
    private abstract class CachedAttributeStart implements Flushable {
        // no code here ... marker class only. 
    }

    /**
     * Flushable cache for literal attribute starts.
     */
    private class LiteralCachedAttributeStart extends CachedAttributeStart {
        private StringReference name;

        public void initialise(StringReference name) {
            this.name = name;
        }

        public void flush() throws WBSAXException {
            handler.addAttribute(name);
        }
    }

    /**
     * Flushable cache for coded attribute starts.
     */
    private class CodedCachedAttributeStart extends CachedAttributeStart {
        AttributeStartCode name;

        public void initialise(AttributeStartCode name) {
            this.name = name;
        }

        public void flush() throws WBSAXException {
            handler.addAttribute(name);
        }
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-03	1469/7	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/5	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
