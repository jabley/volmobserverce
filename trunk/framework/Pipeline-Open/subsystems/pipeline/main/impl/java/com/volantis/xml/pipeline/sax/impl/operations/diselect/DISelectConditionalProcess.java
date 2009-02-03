package com.volantis.xml.pipeline.sax.impl.operations.diselect;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.QName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.impl.dynamic.PassThroughProcess;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Stack;

/**
 * Process used within the preprocessing part of the pipeline to implement the
 * DISelect conditional processing functionality.
 *
 * <p>Supports 'if', 'select', 'when', 'otherwise', elements along with
 * 'diselect:expr', 'diselect:selidname', and 'diselect:selid' attributes.</p>
 *
 * <p>The selidname is maintained in the State as an {@link ExpandedName}
 * because the namespace prefix mappings may change between the point it is
 * specified and the point at which it is used. As there is no way of adding
 * new namespace prefix mappings if at the point it is used there is no non
 * empty prefix associated with the namespace then it is an error.</p>
 */
public final class DISelectConditionalProcess
        extends PassThroughProcess {

    /**
     * The parser used to parse the expressions, this could be shared with
     * another process within the same pipeline, but will not be used
     * concurrently.
     */
    private ExpressionParser parser;

    /**
     * The stack of states used by this to manage the current selidname,
     * remember whether the body of an element was processed, and the
     * select / when / otherwise interactions.
     */
    private Stack<State> states;

    /**
     * Initialise.
     *
     * @param parser The expression parser.
     */
    public DISelectConditionalProcess(ExpressionParser parser) {
        this.parser = parser;
        states = new Stack<State>();
    }

    // Javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        // If in pass through mode, or the element is in the DISelect namespace
        // and not recognized then pass it through.
        Element diSelectElement;
        if (inPassThroughMode || (diSelectElement =
                getDISelectElement(namespaceURI, localName)) == null) {
            super.startElement(namespaceURI, localName, qName, attributes);
            return;
        }

        // Get the current state, create an element specific wrapper and push
        // it as the new state. The newly created state inherits the current
        // selidname from the containing state.
        XMLPipelineContext pipelineContext = getPipelineContext();
        State containingState = peekState();
        State state = diSelectElement.createState(containingState, attributes);
        states.push(state);
        int index;

        // The attributes used on output if they have changed. This is created
        // only when it is needed.
        AttributesImpl output = null;

        // Check to see whether the current state knows whether or not to
        // process the body of the element. This supports the
        // select / when / otherwise elements.
        if (!state.canProcessBody()) {
            // The current state indicates that the element and its body should
            // not be processed so ignore this element and all its children.
            pipelineContext.getFlowControlManager().exitCurrentElement();

            // Remember that the body was not processed.
            state.setProcessBody(false);

            // Don't forward the event on.
            return;
        }

        // Determine whether this event can be forwarded to the next process,
        // this is element specific.
        boolean forwardEvent = diSelectElement.getForwardEvent();

        // Check to see whether the element supports an expr attribute and if
        // so whether it is an element specific attribute, of the global
        // diselect one.
        String exprAttributeURI = diSelectElement.getExprAttributeURI();
        if (exprAttributeURI != null) {

            // Check to see whether there is an expr attribute, use the index
            // as we may need to remove the attribute.
            index = attributes.getIndex(exprAttributeURI, "expr");
            if (index != -1) {
                String value = attributes.getValue(index);
                try {
                    Expression expression = parser.parse(value);

                    // obtain the ExpressionContext from the PipelineContext
                    // the ExpressionContext is needed in order to evaluate the
                    // expression parsed
                    ExpressionContext expressionContext =
                            pipelineContext.getExpressionContext();

                    // Evaluate the expression and cast to a boolean.
                    boolean result = PipelineExpressionHelper.fnBoolean(
                            expression.evaluate(
                                    expressionContext).getSequence())
                            .asJavaBoolean();

                    if (!result) {
                        // The expression evaluated to false so ignore this
                        // element and all its children.
                        pipelineContext.getFlowControlManager()
                                .exitCurrentElement();

                        // Remember that the body was not processed.
                        state.setProcessBody(false);

                        // Don't forward the event on.
                        return;
                    }

                    // Only update the attributes if the event is being
                    // forwarded, otherwise it is a waste of time.
                    if (forwardEvent) {
                        // The element is included but we need to remove the
                        // sel:expr attribute.
                        if (output == null) {
                            output = new AttributesImpl(attributes);

                            // Use the output attributes as the input to ensure
                            // that indeces match.
                            attributes = output;
                        }
                        output.removeAttribute(index);
                    }

                } catch (ExpressionException e) {
                    // something went wrong when evaluating the xpath.
                    // Send an error down the pipeline
                    Locator locator = pipelineContext.getCurrentLocator();
                    XMLPipelineException se = new XMLPipelineException(
                            "Could not evaluate the expression " +
                                    value,
                            locator,
                            e);

                    fatalError(se);
                }
            }
        }

        // Remember that the body of the element is to be processed, this will
        // ensure that the matching end element event is forwarded if
        // necessary, and if relevant ensure that the select / when / otherwise
        // combination works correctly.
        state.setProcessBody(true);

        // Get the current name of the attribute that will be used to replace
        // the diselect:selid attribute.
        ExpandedName selidName = state.getSelidName();

        // Check to see whether the element supports a selidname attribute and
        // if so whether it is an element specific attribute, of the global
        // diselect one.
        String selidNameAttributeURI =
                diSelectElement.getSelidNameAttributeURI();
        if (selidNameAttributeURI != null) {

            // Check to see whether there is a selidname attribute, use the
            // index as we may need to remove the attribute.
            index = attributes.getIndex(selidNameAttributeURI, "selidname");
            if (index != -1) {
                String value = attributes.getValue(index);

                // Only update the attributes if the event is being
                // forwarded, otherwise it is a waste of time.
                if (forwardEvent) {
                    // Remove the attribute.
                    if (output == null) {
                        output = new AttributesImpl(attributes);

                        // Use the output attributes as the input to ensure that
                        // indeces match.
                        attributes = output;
                    }
                    output.removeAttribute(index);
                }

                // Resolve the value to an ExpandedName, update the value for
                // the current element and store it away just in case it is
                // needed for descendant elements.
                QName qname = new ImmutableQName(value);
                selidName = pipelineContext.getNamespacePrefixTracker()
                        .resolveQName(qname, null);
                state.setSelidName(selidName);
            }
        }

        // Only elements outside the diselect namespace support the
        // diselect:selid attribute.
        if (diSelectElement.canHaveSelidAttribute()) {
            // Now check for sel:selid attribute.
            index = attributes.getIndex(Constants.DISELECT_NAMESPACE, "selid");
            if (index != -1) {

                String idURI = selidName.getNamespaceURI();
                String idLocalName = selidName.getLocalName();
                String idQName;

                // Construct the selid attribute's qname, this is dependent on
                // the namespace of the selidName and the namespace prefix
                // mappings in scope.
                if (idURI.equals("")) {
                    // The attribute has no namespace so the qname is the same
                    // as the local name.
                    idQName = idLocalName;
                } else {
                    String prefix = null;

                    // Find the first non empty prefix associated with the specified
                    // array. A non-empty prefix is required
                    String[] prefixes =
                            pipelineContext.getNamespacePrefixTracker()
                                    .getNamespacePrefixes(idURI);
                    for (String p : prefixes) {
                        if (!p.equals("")) {
                            prefix = p;
                            break;
                        }
                    }

                    // Make sure that the prefix is not empty
                    if (prefix == null) {
                        throw new IllegalStateException(
                                "Could not find a non empty prefix declared for namespace: " +
                                        idURI);
                    }

                    idQName = prefix + ":" + idLocalName;
                }

                // Always update the attribute as the selid is only allowed on
                // elements that forward their events.
                if (output == null) {
                    output = new AttributesImpl(attributes);

                    // Use the output attributes as the input to ensure that
                    // indeces match.
                    attributes = output;
                }

                output.setAttribute(index, idURI, idLocalName, idQName,
                        "xs:ID", attributes.getValue(index));
            }
        }

        if (forwardEvent) {
            super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    // Javadoc inherited
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        // If in pass through mode, or the element is in the DISelect namespace
        // and not recognized then pass it through.
        Element diSelectElement;
        if (inPassThroughMode || (diSelectElement =
                getDISelectElement(namespaceURI, localName)) == null) {
            super.endElement(namespaceURI, localName, qName);
            return;
        }

        State state = popState();

        // If this element was skipped previously then don't forward the
        // event as they would not balance.
        if (state.processedBody() && diSelectElement.getForwardEvent()) {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    /**
     * Get the DISelect element enumeration instance for the specified element.
     *
     * @param namespaceURI The namespace of the element.
     * @param localName    The local name of the element.
     * @return The DISelect element enumeration instance, or null if this is
     *         not a conditional element.
     */
    private Element getDISelectElement(String namespaceURI, String localName) {
        if (namespaceURI.equals(Constants.DISELECT_NAMESPACE)) {
            if (localName.equals("if")) {
                return Element.IF;
            }
            if (localName.equals("select")) {
                return Element.SELECT;
            }
            if (localName.equals("when")) {
                return Element.WHEN;
            }
            if (localName.equals("otherwise")) {
                return Element.OTHERWISE;
            }
            return null;
        } else {
            return Element.OTHER;
        }
    }

    /**
     * Take a look at the last state pushed onto the stack.
     *
     * @return The last state pushed onto the stack, or null if the stack is
     *         empty.
     */
    private State peekState() {
        return states.empty() ? null : states.peek();
    }

    /**
     * Remove the last state pushed onto the stack.
     *
     * @return The last state pushed onto the stack.
     */
    private State popState() {
        return states.pop();
    }
}
