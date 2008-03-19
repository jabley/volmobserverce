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

package com.volantis.xml.pipeline.sax.operations.foreach;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.InternalExpressionContext;
import com.volantis.xml.expression.SequenceIndexOutOfBoundsException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.QName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The rule for the <code>for-each</code> element.
 *
 * <p>The <code>for-each</code> operation evaluates its body for each item
 * in a sequence. The value of the item is stored in the specified variable and
 * can be used within expressions in the body. The variable is specified in the
 * <code>variable</code> attribute and the sequence is specified as an
 * expression (does not need to be wrapped in %{...}) in the <code>in</code>
 * attribute. It is possible to nest <code>for-each</code> inside one
 * another.</p>
 *
 * <p>The loop variable will shadow a variable within the same name defined
 * outside the element, e.g. in a containing <code>for-each</code>. The reason
 * for this is to make it safer to combine separately authored DCI fragments
 * together without having to worry about variable clashes, unless they are
 * part of the fragment's interface.</p>
 *
 * <p>Its behaviour is optimised for the number of elements in the sequence.
 * If the sequence is empty then it just skips the element's body. If the
 * sequence has one value then it creates the variable in a new scope and
 * processes its body as normal. Only if the sequence has more than one item
 * does it save its unprocessed body and iterate over the sequence. For each
 * item in the sequence it sets the variable to the item and evaluates the
 * body.</p>
 *
 * @todo Do the following performance optimisation when nesting.
 *
 * <p>When for-each elements are nested the outermost <code>for-each</code>
 * may already have recorded all the contents of the nested
 * <code>for-each</code> in which case it would be more efficient if it could
 * reuse that somehow. This applies to other processes as well.</p>
 */
public class ForEachRule
        implements DynamicElementRule {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new ForEachRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName expandedName,
            Attributes attributes) throws SAXException {

        XMLPipelineContext context =
                dynamicProcess.getPipeline().getPipelineContext();
        InternalExpressionContext expressionContext =
                (InternalExpressionContext)
                context.getExpressionContext();

        // The iterator that is returned.
        EndElementAction action;

        // Evaluate the in expression to a sequence.
        String inExpression = attributes.getValue("in");
        final Sequence sequence = evaluateExpression(expressionContext, inExpression);
        int length = sequence.getLength();
        if (length == 0) {
            // The sequence is empty so there is nothing to do so just skip
            // the body and do not create an iterator.
            context.getFlowControlManager().exitCurrentElement();
            action = EndElementAction.DO_NOTHING;
        } else {

            // Resolve the variable name into an ExpandedName, if the variable
            // name does not have a prefix then it belongs in no namespace,
            // rather than the default namespace.
            String variableName = attributes.getValue("variable");
            QName variableQName = new ImmutableQName(variableName);
            final ExpandedName variableExpandedName =
                    context.getNamespacePrefixTracker()
                    .resolveQName(variableQName, null);

            // If the sequence only has a single value in then it is not
            // necessary to store the body away to be evaluated multiple times.
            if (length == 1) {

                // Create a new block scope to contain the variable.
                expressionContext.pushBlockScope();

                // Create a variable in the current stack frame.
                try {
                    expressionContext.getCurrentScope()
                            .declareVariable(variableExpandedName,
                                    sequence.getItem(1));
                } catch (SequenceIndexOutOfBoundsException e) {
                    throw new ExtendedSAXException(e);
                }

                // Remember to pop the block scope off after the content has
                // been processed.
                action = new PopBlockScopeAction();

            } else {

                // Make the dynamic process pass the element contents straight
                // through without evaluating them so that they can be recprded
                // and evaluated for each value of the variable. The dynamic
                // process will automatically come out of pass through mode
                // once it sees the matching end element event.
                dynamicProcess.passThroughElementContents();

                XMLPipelineFactory factory = context.getPipelineFactory();
                final PipelineRecorder recorder =
                        factory.createPipelineRecorder();
                recorder.startRecording(dynamicProcess.getPipeline());

                final XMLProcess process = recorder.getRecordingProcess();

                dynamicProcess.addProcess(process);

                // Create an action to wrap the iterator and remove the
                // recording process once the body has been evaluated.
                action = new EndElementAction() {
                    public void doAction(DynamicProcess dynamicProcess)
                            throws SAXException {

                        // Remove the process from the head.
                        dynamicProcess.removeProcess(process);

                        // Create an iterator to perform the actual iteration once the
                        // body has been recorded.
                        PipelineRecording recording = recorder.stopRecording();
                        final ForEachIterator iterator = new ForEachIterator(
                                recording, variableExpandedName, sequence);

                        // Perform the iteration.
                        iterator.iterate(dynamicProcess);
                    }
                };
            }
        }

        return action;
    }

    // Javadoc inherited.
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName expandedName, Object o)
            throws SAXException {

        // Perform the action.
        EndElementAction action = (EndElementAction) o;
        action.doAction(dynamicProcess);
    }

    /**
     * Parse and evaluate the expression and return the result as a sequence.
     *
     * @param expressionContext The content within which the expression is to
     * be evaluated.
     * @param expressionAsString The expression as a string which needs parsing.
     *
     * @return The result of the expression as a sequence.
     * @throws ExtendedSAXException If there was a problem parsing or
     * evaluating the expression.
     */
    private Sequence evaluateExpression(
            final ExpressionContext expressionContext,
            String expressionAsString)
            throws ExtendedSAXException {
        ExpressionFactory factory = expressionContext.getFactory();
        ExpressionParser parser = factory.createExpressionParser();
        Sequence sequence;
        try {
            Expression expression = parser.parse(expressionAsString);
            Value value = expression.evaluate(expressionContext);
            sequence = value.getSequence();
        } catch (ExpressionException e) {
            throw new ExtendedSAXException(e);
        }
        return sequence;
    }
}
