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

import com.volantis.xml.expression.InternalExpressionContext;
import com.volantis.xml.expression.InternalExpressionScope;
import com.volantis.xml.expression.PositionScope;
import com.volantis.xml.expression.SequenceIndexOutOfBoundsException;
import com.volantis.xml.expression.Variable;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.SAXException;

/**
 * An iterator over a sequence.
 */
public class ForEachIterator {

    /**
     * The recording containing the body content.
     */
    private final PipelineRecording recording;

    /**
     * The fully qualified name of the variable.
     */
    private final ExpandedName variableName;

    /**
     * The sequence to iterate over.
     */
    private final Sequence sequence;

    /**
     * Initialise.
     *
     * @param recording The recorder containing the body content.
     * @param variableName The fully qualified name of the variable.
     * @param sequence The sequence to iterate over.
     */
    public ForEachIterator(
            PipelineRecording recording, ExpandedName variableName,
            Sequence sequence) {
        this.recording = recording;
        this.variableName = variableName;
        this.sequence = sequence;
    }

    /**
     * Iterate over the sequence.
     *
     * @param dynamicProcess The dynamic process.
     * @throws SAXException If there was a problem.
     */
    public void iterate(DynamicProcess dynamicProcess)
            throws SAXException {
        
        XMLPipelineContext pipelineContext =
                dynamicProcess.getPipeline().getPipelineContext();
        InternalExpressionContext expressionContext =
                (InternalExpressionContext)
                pipelineContext.getExpressionContext();

        PipelinePlayer player = recording.createPlayer();

        // Push a new scope to contain the variable and shadow any variable
        // with the same name.
        expressionContext.pushBlockScope();

        // Push a new position scope to track the iteration position
        expressionContext.pushPositionScope();
        PositionScope pc = expressionContext.getPositionScope();

        // Declare an unitialized variable.
        InternalExpressionScope scope = (InternalExpressionScope)
                expressionContext.getCurrentScope();
        Variable variable = scope.declareVariable(variableName);

        // Iterate over the sequence.
        int length = sequence.getLength();

        for (int i = 1; i <= length; i += 1) {
            // Increment to the next position in the sequence
            pc.increment();

            // Get the item.
            try {
                variable.setValue(sequence.getItem(i));
            } catch (SequenceIndexOutOfBoundsException e) {
                throw new ExtendedSAXException(e);
            }

            // Replay the events through the dynamic process.
            player.play(dynamicProcess);
        }

        // Make sure to pop the scope for tracking position the one that
        // contains the variable.
        expressionContext.popPositionScope();
        expressionContext.popBlockScope();
    }
}
