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

import com.volantis.xml.pipeline.sax.impl.validation.Element;
import com.volantis.xml.pipeline.sax.impl.validation.ValidationModel;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.expression.Value;

import org.xml.sax.SAXException;

/**
 * The model of the template.
 *
 * <p>This is the central place that defines the template model.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface TemplateModel extends ValidationModel {

    /**
     * Create a simple value from a string.
     *
     * @param value The value.
     * @return The new template value.
     * @throws SAXException If there was a problem.
     */
    TValue createSimpleValue(String value)
            throws SAXException;

    /**
     * Create a reference to a value in a containing template.
     *
     * @param ref The name of the parameter in the containing template.
     * @return The new template value.
     * @throws SAXException If there was a problem.
     */
    TValue createReferenceValue(String ref)
            throws SAXException;

    /**
     * Prepare to record the contents of the current element and store it in
     * a value.
     *
     * @param element The current element.
     * @param mode    The mode of the value.
     * @throws SAXException If there was a problem.
     */
    void startValueDefinition(Element element, EvaluationMode mode)
            throws SAXException;

    /**
     * Stop recording the contents of the current element, create the value and
     * store it away in the appropriate location.
     *
     * @param element    The current element.
     * @param complexity The complexity of the value.
     * @throws SAXException If there was a problem.
     */
    void endValueDefinition(Element element, Complexity complexity)
            throws SAXException;

    /**
     * Add the default value to the parameter block that is being checked while
     * processing the parameter elements.
     *
     * @param name  The name of the parameter.
     * @param value The value.
     * @throws SAXException If there was a problem.
     */
    void addDefaultValue(String name, TValue value) throws SAXException;

    /**
     * Get the parameter block.
     *
     * <p>The actual parameter block returned depends on just where in the
     * processing of the template this is called. If this is called before the
     * &lt;template:declarations&gt; has been processed then it returns the
     * parameter block for the containing template, this is to allow values
     * from containing template to be used within bindings. Otherwise it
     * returns the block for this template model.</p>
     *
     * @return The parameter block, may be null.
     */
    ParameterBlock getParameterBlock();

    /**
     * Add a value to the bindings.
     *
     * <p>Must only be called from within a template:binding element.</p>
     *
     * @param name  The name of the parameter.
     * @param value The value.
     */
    void addBindingValue(String name, TValue value);

    /**
     * Set the value setter that will be used to set the value once it has
     * been created by template:simpleValue or template:complexValue.
     *
     * @param setter The setter to use.
     */
    void setValueSetter(TemplateValueSetter setter);

    /**
     * Start the template.
     *
     * @throws SAXException If there was a problem.
     */
    void startTemplate() throws SAXException;

    /**
     * End the template.
     *
     * @throws SAXException If there was a problem.
     */
    void endTemplate() throws SAXException;

    /**
     * Start the declarations.
     */
    void startDeclarations();

    /**
     * End the declarations.
     */
    void endDeclarations();

    void startBindings();

    void endBindings();

    /**
     * Adds a new pending variable declaration to the current model.
     *
     * @param name the name of the variable
     * @param initialValue the initial value
     */
    void addPendingVariableDeclaration(ExpandedName name, Value initialValue);

    /**
     * Executes the collected pending declarations.
     */
    void executePendingVariableDeclarations();
}
