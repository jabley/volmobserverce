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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Adrian          VBM:2003030509 - This class 'records' sax
 *                              events as they are represented by calls to the
 *                              ContentHandler methods as a list of SAXEvents
 *                              which can be later played back.
 * 23-May-03    Phil W-S        VBM:2003030610 - Ensure that clone is public
 *                              and that it performs the required processing.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.xml.schema.recorder;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.util.ArrayList;

/**
 * If being used by an XMLProcess, the getPlaybackLocator method should be used
 * to retrieve the playback document locator and set it on the pipeline context
 * with which the XMLProcess is associated prior to playback. The playback
 * document locator should be removed from the pipeline context after playback
 * has completed.
 */
public class SAXEventRecorder implements ContentHandler, Cloneable {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * This {@link Locator} is used during playback of the {@link SAXEvent}s.
     */
    LocatorImpl playbackLocator = new LocatorImpl();

    /**
     * This {@link Locator} is used during recording of {@link SAXEvent}s.
     */
    transient Locator documentLocator;

    /**
     * Indicates whether the recorder has recorded non-simple markup where...
     * simple markup is whitespace or characters only.
     */
    boolean complex = false;

    /**
     * The {@link java.util.List} of {@link SAXEvent}s to playback later.
     */
    ArrayList events = new ArrayList();

    /**
     * Get the {@link Locator} for use during playback of the {@link SAXEvent}s
     * @return The {@link Locator} for use during playback.
     */
    public Locator getPlaybackLocator() {
        return playbackLocator;
    }

    /**
     * Iterate over the list of {@link SAXEvent}s calling playback on each one.
     * <b>N.B. {@link #initialize} must be called before this method.</b>
     *
     * @param target The {@link ContentHandler} on which the {@link SAXEvent}s
     * will call the appropriate methods with their stored attributes.
     *
     * @throws SAXException if there was a problem playing back the SAXEvents.
     */
    public void playback(ContentHandler target) throws SAXException {
        for (int i = 0; i < events.size(); i++) {
            SAXEvent event = (SAXEvent)events.get(i);
            event.playback(target, playbackLocator);
        }
    }

    /**
     * Initialize the playback {@link Locator} so that its initial state is the
     * same as the specified {@link Locator}.  This should be called prior to
     * {@link #playback}.
     * @param locator - The {@link Locator} from which we will copy the state
     * into our playback {@link Locator}.
     */
    public void initialize(Locator locator) {
        copy(locator, playbackLocator);
    }

    /**
     * Copy the fields from a specified {@link Locator} to a specified
     * {@link LocatorImpl}
     * @param from The {@link Locator} from which to copy the fields.
     * @param to The {@link LocatorImpl} to copy the fields into.
     */
    private void copy(Locator from, LocatorImpl to) {
        if (from != null && to != null) {
            to.setColumnNumber(from.getColumnNumber());
            to.setLineNumber(from.getLineNumber());
            to.setPublicId(from.getPublicId());
            to.setSystemId(from.getSystemId());
        }
    }

    /**
     * Indicates whether the recorder has recorded non-simple markup where
     * simple markup is whitespace or characters only.
     * @return true if the recorder has recorded non-simple markup.
     */
    public boolean isComplex() {
        return complex;
    }

    /**
     * Clear the content of the {@link java.util.List} of {@link SAXEvent}s
     * and reset the state of this SAXEventRecorder.
     */
    public void clear() {
        events.clear();
        complex = false;
        playbackLocator = new LocatorImpl();
        documentLocator = null;
    }

    /**
     * Check if this recorder does not contain any recorded SAX events.
     * @return true if there have been no SAX events recorded since
     * construction or a call to {@link #clear}
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }

    // Javadoc inherited from superclass
    public Object clone() throws CloneNotSupportedException {
        SAXEventRecorder clone = (SAXEventRecorder)super.clone();

        clone.events = (ArrayList)events.clone();
        clone.playbackLocator = new LocatorImpl(playbackLocator);

        return clone;
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#setDocumentLocator} for further detail.
     *
     * This method sets the {@link Locator} for this recorder and stores a
     * {@link SetDocumentLocatorSAXEvent} for later playback of this event.
     *
     * @param locator - An object that can return the location of any SAX
     * document event.
     */
    public void setDocumentLocator(Locator locator) {
        this.documentLocator = locator;

        SetDocumentLocatorSAXEvent event =
                new SetDocumentLocatorSAXEvent(documentLocator);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#startDocument} for further detail.
     *
     * This method stores a {@link StartDocumentSAXEvent} for later playback of
     * this event.
     */
    public void startDocument() throws SAXException {
        StartDocumentSAXEvent event =
                new StartDocumentSAXEvent(documentLocator);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#endDocument} for further detail.
     *
     * This method stores a {@link EndDocumentSAXEvent} for later playback of
     * this event.
     */
    public void endDocument() throws SAXException {
        EndDocumentSAXEvent event = new EndDocumentSAXEvent(documentLocator);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#startPrefixMapping} for further detail.
     *
     * This method stores a {@link StartPrefixMappingSAXEvent} for later
     * playback of this event.
     *
     * @param prefix - The Namespace prefix being declared. An empty string is
     * used for the default element namespace, which has no prefix.
     * @param uri - The Namespace URI the prefix is mapped to.
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        StartPrefixMappingSAXEvent event =
                new StartPrefixMappingSAXEvent(documentLocator, prefix, uri);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#endPrefixMapping} for further detail.
     *
     * This method stores a {@link EndPrefixMappingSAXEvent} for later
     * playback of this event.
     *
     * @param prefix - The prefix that was being mapped. This is the empty
     * string when a default mapping scope ends.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        EndPrefixMappingSAXEvent event =
                new EndPrefixMappingSAXEvent(documentLocator, prefix);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#startElement} for further detail.
     *
     * This method stores a {@link StartElementSAXEvent} for later
     * playback of this event.  We also know that we have encountered complex
     * content so {@link #isComplex} will return true.
     *
     * @param namespaceURI - The Namespace URI, or the empty string if the
     * element has no Namespace URI or if Namespace processing is not being
     * performed.
     * @param localName - The local name (without prefix), or the empty string
     * if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if
     * qualified names are not available.
     * @param atts - The attributes attached to the element. If there are no
     * attributes, it shall be an empty Attributes object.
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
        StartElementSAXEvent event = new StartElementSAXEvent(documentLocator,
                                                              namespaceURI, localName, qName, atts);
        events.add(event);
        complex = true;
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#endElement} for further detail.
     *
     * This method stores a {@link EndElementSAXEvent} for later playback of
     * this event.
     *
     * @param namespaceURI - The Namespace URI, or the empty string if the
     * element has no Namespace URI or if Namespace processing is not being
     * performed.
     * @param localName - The local name (without prefix), or the empty string
     * if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if
     * qualified names are not available.
     */
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {
        EndElementSAXEvent event = new EndElementSAXEvent(documentLocator,
                                                          namespaceURI, localName, qName);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#characters} for further detail.
     *
     * This method stores a {@link CharactersSAXEvent} for later playback of
     * this event.
     *
     * @param chars - The characters from the XML document.
     * @param start - The start position in the array.
     * @param length - The number of characters to read from the array.
     */
    public void characters(char[] chars, int start, int length)
            throws SAXException {
        CharactersSAXEvent event = new CharactersSAXEvent(documentLocator,
                                                          chars, start, length);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#ignorableWhitespace} for further detail.
     *
     * This method stores a {@link IgnorableWhitespaceSAXEvent} for later
     * playback of this event.
     *
     * @param chars - The characters from the XML document.
     * @param start - The start position in the array.
     * @param length - The number of characters to read from the array.
     */
    public void ignorableWhitespace(char[] chars, int start, int length)
            throws SAXException {
        IgnorableWhitespaceSAXEvent event = new IgnorableWhitespaceSAXEvent(
                documentLocator, chars, start, length);
        events.add(event);
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#processingInstruction} for further detail.
     *
     * This method stores a {@link ProcessingInstructionSAXEvent} for later
     * playback of this event.  We also know that we have encountered complex
     * content so {@link #isComplex} will return true.
     *
     * @param target - The processing instruction target.
     * @param data - The processing instruction data, or null if none was
     * supplied. The data does not include any whitespace separating it from
     * the target.
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        ProcessingInstructionSAXEvent event = new
                ProcessingInstructionSAXEvent(documentLocator, target, data);
        events.add(event);
        complex = true;
    }

    /**
     * ContentHandler interface method.
     * See {@link ContentHandler#skippedEntity} for further detail.
     *
     * This method stores a {@link SkippedEntitySAXEvent} for later playback of
     * this event.
     *
     * @param name - The name of the skipped entity. If it is a parameter
     * entity, the name will begin with '%', and if it is the external DTD
     * subset, it will be the string "[dtd]".
     */
    public void skippedEntity(String name) throws SAXException {
        SkippedEntitySAXEvent event =
                new SkippedEntitySAXEvent(documentLocator, name);
        events.add(event);
    }

    /**
     * In order that the playback Locator can be Serialized we must create
     * a Serializable specialisation of LocatorImpl.
     *
     * Serializability of a class is enabled by the class implementing the
     * java.io.Serializable interface. Classes that do not implement this
     * interface will not have any of their state serialized or deserialized.
     * All subtypes of a serializable class are themselves serializable. The
     * serialization interface has no methods or fields and serves only to
     * identify the semantics of being serializable.
     *
     * To allow subtypes of non-serializable classes to be serialized, the
     * subtype may assume responsibility for saving and restoring the state of
     * the supertype's public, protected, and (if accessible) package fields.
     * The subtype may assume this responsibility only if the class it extends
     * has an accessible no-arg constructor to initialize the class's state.
     * It is an error to declare a class Serializable in this case. The error
     * will be detected at runtime.
     *
     * During deserialization, the fields of non-serializable classes will be
     * initialized using the public or protected no-arg constructor of the
     * class. A no-arg constructor must be accessible to the subclass that is
     * serializable. The fields of serializable subclasses will be restored
     * from the stream.
     *
     * Classes that require special handling during the serialization and
     * deserialization process must implement special methods with these exact
     * signatures:
     *
     * private void writeObject(java.io.ObjectOutputStream out)
     *                          throws IOException
     *
     * private void readObject(java.io.ObjectInputStream in)
     *                         throws IOException, ClassNotFoundException;
     */
    protected class SerializableLocator extends LocatorImpl
            implements Serializable {

        /**
         * Construct a new instance of SerializableLocator.  This default
         * constructor is required to ensure that the class is Serializable.
         */
        public SerializableLocator() {
        }

        /**
         * Copy constructor.
         *
         * Create a persistent copy of the current state of a locator. When the
         * original locator changes, this copy will still keep the original
         * values (and it can be used outside the scope of DocumentHandler
         * methods).
         *
         * @param locator The {@link Locator} to copy.
         */
        public SerializableLocator(Locator locator) {
            super(locator);
        }

        /**
         * This method is responsible for writing the state of the Object so
         * that the corresponding readObject method can restore it. The default
         * mechanism for saving an Object's fields can be invoked by calling
         * out.defaultWriteObject. The method does not need to concern itself
         * with the state belonging to its superclasses or subclasses. State is
         * saved by writing the individual fields to the ObjectOutputStream
         * using the writeObject method or by using the methods for primitive
         * data types supported by DataOutput.
         *
         * @param out The {@link ObjectOutputStream} to which we will write
         * this Object's fields
         * @throws IOException If there was a problem writing to the
         * {@link ObjectOutputStream}
         */
        private void writeObject(ObjectOutputStream out)
                throws IOException {
            out.defaultWriteObject();
            out.writeInt(getColumnNumber());
            out.writeInt(getLineNumber());
            out.writeUTF(getPublicId());
            out.writeUTF(getSystemId());
        }

        /**
         * The readObject method is responsible for reading from the stream and
         * restoring the classes fields. It may call in.defaultReadObject to
         * invoke the default mechanism for restoring the object's non-static
         * and non-transient fields. The defaultReadObject method uses
         * information in the stream to assign the fields of the object saved
         * in the stream with the correspondingly named fields in the current
         * object. This handles the case when the class has evolved to add new
         * fields. The method does not need to concern itself with the state
         * belonging to its superclasses or subclasses.
         *
         * @param in The {@link ObjectInputStream} from which we read this
         * Object's fields.
         * @throws IOException If there was a problem reading from the
         * {@link ObjectInputStream}
         * @throws ClassNotFoundException
         */
        private void readObject(ObjectInputStream in)
                throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            setColumnNumber(in.readInt());
            setLineNumber(in.readInt());
            setPublicId(in.readUTF());
            setSystemId(in.readUTF());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 ===========================================================================
*/
