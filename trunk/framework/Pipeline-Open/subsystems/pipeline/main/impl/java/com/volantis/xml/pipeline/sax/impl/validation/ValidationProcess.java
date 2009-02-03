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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.validation;

import com.volantis.synergetics.cornerstone.utilities.WhitespaceUtilities;
import com.volantis.synergetics.cornerstone.stack.ArrayListStack;
import com.volantis.synergetics.cornerstone.stack.Stack;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A process that can be used as the basis for processes that amongst other
 * things validate their input to ensure that they are adhering to the schema
 * for the operation.
 */
public class ValidationProcess
        extends XMLProcessImpl
        implements ValidationModel {

    /**
     * The stack of {@link Element}.
     */
    private final Stack elements;

    /**
     * String describing the usage made of this process.
     */
    private final String usage;

    /**
     * The content handler to which events are to be routed, this changes from
     * the next process to the recorder and back again depending on whether a
     * value is being recorded or not.
     */
    protected ContentHandler contentHandler;

    /**
     * The current state, used for validation.
     */
    private State state;

    public ValidationProcess(
            State state, int expectedDepth, final String usage) {
        elements = new ArrayListStack(expectedDepth);
        this.state = state;
        this.usage = usage;
    }

    // Javadoc inherited.
    public void transition(Event event) {
        State newState = state.transition(event);
        if (newState == null) {
            throw new IllegalStateException("No transition from " +
                    state + " on event " + event);
        } else {
//            System.out.println("Moved from " + state + " to " + newState +
//                    " on event " + event);
            state = newState;
        }
    }

    // Javadoc inherited.
    public void startElement(Element element) {
        transition(element.getStartEvent());

        elements.push(element);
    }

    /**
     * Get the containing template element.
     *
     * @return The containing template element.
     */
    private Element getContainingElement() {
        return (Element) elements.peek();
    }

    // Javadoc inherited.
    public void endElement(Element element) {

        Element popped = (Element) elements.pop();
        if (popped != element) {
            throw new IllegalStateException("Unbalanced element stack, expected: " +
                    element +
                    " found " + popped);
        }

        transition(element.getEndEvent());
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        super.startProcess();

        contentHandler = getNextProcess();
    }

    // Javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {

        Element containing = getContainingElement();
        if (containing.allowsAnyContent()) {
            contentHandler.characters(ch, start, length);
        } else {
            if (!WhitespaceUtilities.isWhitespace(ch, start, length)) {
                fatalError(new XMLPipelineException("Significant character data not allowed inside " +
                        containing.getName() + " - " + state,
                        getPipelineContext().getCurrentLocator()));
            }
        }
    }

    // Javadoc inherited
    public void endElement(
            String namespaceURI, String localName, String qName)
            throws SAXException {

        Element containing = getContainingElement();
        if (containing.allowsAnyContent()) {
            contentHandler.endElement(namespaceURI, localName, qName);
        } else {
            fatalError(new XMLPipelineException("Non " +
                    usage + " markup not allowed inside " +
                    containing.getName() + " - " + state,
                    getPipelineContext().getCurrentLocator()));
        }
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        contentHandler.endPrefixMapping(prefix);
    }

    // Javadoc inherited
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {

        Element containing = getContainingElement();
        if (containing.allowsAnyContent()) {
            contentHandler.ignorableWhitespace(ch, start, length);
        }
    }

    // Javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {

        Element containing = getContainingElement();
        if (containing.allowsAnyContent()) {
            contentHandler.processingInstruction(target, data);
        } else {
            fatalError(new XMLPipelineException("Non " +
                    usage + " markup not allowed inside " +
                    containing.getName() + " - " + state,
                    getPipelineContext().getCurrentLocator()));
        }
    }

    // Javadoc inherited
    public void startElement(
            String namespaceURI, String localName, String qName,
            Attributes atts) throws SAXException {

        Element containing = getContainingElement();
        if (containing.allowsAnyContent()) {
            contentHandler.startElement(namespaceURI, localName, qName, atts);
        } else {
            fatalError(new XMLPipelineException("Non " +
                    usage + " markup not allowed inside " +
                    containing.getName() + " - " + state,
                    getPipelineContext().getCurrentLocator()));
        }
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        contentHandler.startPrefixMapping(prefix, uri);
    }
}
