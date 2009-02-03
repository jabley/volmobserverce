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

package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Tracking;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import com.volantis.xml.pipeline.sax.impl.validation.ValidationProcess;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionScope;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A process that tracks all events that pass through the template definition.
 *
 * <p>This process is needed to record events and to make sure that additional
 * events are not passed through. It also implements the TemplateModel
 * interface.</p>
 */
public class TemplateProcess
        extends ValidationProcess
        implements TemplateModel {

    /**
     * The paremeters from the containing model, may be null.
     */
    private final ParameterBlock containingBlock;

    /**
     * The block of parameters that are used during binding.
     */
    private ParameterBlock bindingBlock;

    /**
     * The block of parameters that are used within the body of the template.
     */
    private ParameterBlock parameterBlock;

    /**
     * The recorder used to record content of values.
     */
    private PipelineRecorder recorder;

    /**
     * The setter used to set a value in the appropriate manner depending on
     * the current location of the template.
     */
    private TemplateValueSetter valueSetter;

    /**
     * The evaluation mode of the value currently being recorded.
     */
    private EvaluationMode mode;

    private DependencyContext dependencyContext;

    /**
     * Map to store the pending variable declarations.
     *
     * <p>The keys are the expanded names of the variables and the values are
     * the initail values of the variables.</p> 
     */
    private final Map pendingVariableDeclarations;

    /**
     * Initialise.
     *
     * @param containingModel The containing model, may be null.
     */
    public TemplateProcess(TemplateModel containingModel) {
        super(TemplateSchema.EXPECT_APPLY_START, 6, "template");

        if (containingModel == null) {
            containingBlock = null;
        } else {
            containingBlock = containingModel.getParameterBlock();
        }
        pendingVariableDeclarations = new HashMap();
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        dependencyContext = getPipelineContext().getDependencyContext();
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        super.startProcess();

        contentHandler = getNextProcess();
    }

    // Javadoc inherited.
    public TValue createSimpleValue(String value)
            throws SAXException {

        transition(TemplateSchema.VALUE_START);

        XMLPipelineContext context = getPipelineContext();
        XMLPipelineFactory factory = context.getPipelineFactory();
        PipelineRecorder recorder = factory.createPipelineRecorder();
        recorder.startRecording(getPipeline());
        char[] chars = value.toCharArray();
        recorder.getRecordingHandler().characters(chars, 0, chars.length);
        PipelineRecording recording = recorder.stopRecording();

        TValue tValue = new DirectValue(
                recording, Complexity.SIMPLE, EvaluationMode.IMMEDIATE,
                dependencyContext.extractDependency());

        transition(TemplateSchema.VALUE_END);

        return tValue;
    }

    // Javadoc inherited.
    public TValue createReferenceValue(String ref)
            throws SAXException {

        transition(TemplateSchema.VALUE_START);

        TValue value = containingBlock.query(ref);
        if (value == null) {
            SAXParseException exception = new XMLPipelineException(
                    "Referenced value " + ref + " does not exist",
                    getPipelineContext().getCurrentLocator());
            fatalError(exception);
            throw exception;
        }

        TValue tValue = new DelegateValue(value);

        transition(TemplateSchema.VALUE_END);

        return tValue;
    }

    // Javadoc inherited.
    public void addBindingValue(String name, TValue value) {
        bindingBlock.add(name, value);
    }

    // Javadoc inherited.
    public void startValueDefinition(
            Element element, EvaluationMode mode) throws SAXException {

        startElement(element);

        this.mode = mode;

        XMLPipelineContext context = getPipelineContext();
        XMLPipelineFactory factory = context.getPipelineFactory();
        recorder = factory.createPipelineRecorder();
        recorder.startRecording(getPipeline());
        contentHandler = recorder.getRecordingHandler();
    }

    // Javadoc inherited.
    public void endValueDefinition(
            Element element, Complexity complexity)
            throws SAXException {

        PipelineRecording recording = recorder.stopRecording();

        TValue value = new DirectValue(recording, complexity, mode,
                dependencyContext.extractDependency());

        valueSetter.setTemplateValue(value);
        valueSetter = null;
        recorder = null;
        contentHandler = getNextProcess();
        mode = null;

        endElement(element);
    }

    // Javadoc inherited.
    public void addDefaultValue(String name, TValue value)
            throws SAXException {

        ParameterBlock block = getParameterBlock();
        block.add(name, value);

        // This binding value is automatically verified (a
        // binding value is verified if it has an equivalent
        // parameter, which this one clearly does since we're
        // creating it from a parameter!)
        value.verify();
    }

    // Javadoc inherited.
    public ParameterBlock getParameterBlock() {

        if (parameterBlock == null) {
            return containingBlock;
        }

        return parameterBlock;
    }

    // Javadoc inherited.
    public void setValueSetter(TemplateValueSetter setter) {
        this.valueSetter = setter;
    }

    // Javadoc inherited.
    public void startTemplate() throws SAXException {

        startElement(TemplateSchema.APPLY);

        bindingBlock = new ParameterBlock();
    }

    // Javadoc inherited.
    public void endTemplate() throws SAXException {

        endElement(TemplateSchema.APPLY);
    }

    // Javadoc inherited.
    public void startDeclarations() {
        startElement(TemplateSchema.DECLARATIONS);

        parameterBlock = bindingBlock;
        bindingBlock = null;

        pushDependencyTracker();
    }

    // Javadoc inherited.
    public void endDeclarations() {
        popDependencyTracker();

        endElement(TemplateSchema.DECLARATIONS);
    }

    // javadoc inherited
    public void startBindings() {
        startElement(TemplateSchema.BINDINGS);

        pushDependencyTracker();
    }

    private void pushDependencyTracker() {
        // Create and push a new dependency tracker, to collate dependency
        // information for the value. Do it here, rather than on the rules for
        // binding / parameter elements as they allow the value to be specified
        // as an attribute and the evaluation of attributes (and hence creation
        // of dependency information is done by the dynamic process before
        // invoking rules.
        dependencyContext.pushDependencyTracker(Tracking.INHERIT);
    }

    // javadoc inherited
    public void endBindings() {

        popDependencyTracker();

        endElement(TemplateSchema.BINDINGS);
    }

    private void popDependencyTracker() {
        dependencyContext.popDependencyTracker();
    }

    // javadoc inherited
    public void addPendingVariableDeclaration(
            final ExpandedName name, final Value initialValue) {
        if (pendingVariableDeclarations.containsKey(name)) {
            throw new IllegalArgumentException("This model has already have a" +
                " pending declaration for the name " + name);
        }
        pendingVariableDeclarations.put(name, initialValue);
    }

    // javadoc inherited
    public void executePendingVariableDeclarations() {
        final ExpressionScope expressionScope =
            getPipelineContext().getExpressionContext().getCurrentScope();
        for (Iterator iter = pendingVariableDeclarations.entrySet().iterator();
                iter.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final ExpandedName name = (ExpandedName) entry.getKey();
            final Value initialValue = (Value) entry.getValue();
            expressionScope.declareVariable(name, initialValue);
        }
    }
}
