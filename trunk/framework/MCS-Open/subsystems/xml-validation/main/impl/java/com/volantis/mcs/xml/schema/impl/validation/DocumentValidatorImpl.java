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

package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.impl.compiler.CompiledSchemaInternal;
import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.ValidationException;
import com.volantis.synergetics.cornerstone.stack.Stack;
import com.volantis.synergetics.cornerstone.stack.ArrayListStack;

/**
 * Default implementation of {@link DocumentValidator}.
 */
public class DocumentValidatorImpl
        implements DocumentValidator {

    /**
     * The compiled schema for which this validator applies.
     */
    private final CompiledSchemaInternal compiledSchema;

    /**
     * The stack of element validators, is empty until the root element is
     * processed after which there is one entry for each element in the stack
     * hierarchy.
     */
    private final Stack /*of ElementValidator*/ elementValidatorStack;

    /**
     * A count of the number of validators in the stack. Used to optimize
     * searching for excluded elements.
     */
    private int excludingValidatorCount;

    /**
     * Indicates whether the preceding piece of content was ignored whitespace,
     * consumed character data, or not character data, i.e. element.
     *
     * <p>This is used to ensure that adjacent pieces of character content are
     * treated as a single block.</p>
     */
    private PrecedingContent precedingContent;

    /**
     * Initialise.
     *
     * @param compiledSchema The compiled schema against which this will
     *                       validate.
     */
    public DocumentValidatorImpl(CompiledSchemaInternal compiledSchema) {
        this.compiledSchema = compiledSchema;
        elementValidatorStack = new ArrayListStack();
        precedingContent = PrecedingContent.NOT_PCDATA;
    }

    public ElementType getValidatingElement() {
        if (elementValidatorStack.isEmpty()) {
            return null;
        } else {
            return ((ElementValidator) elementValidatorStack.peek()).getElementType();
        }
    }

    // Javadoc inherited.
    public void open(ElementType element)
            throws ValidationException {

        // If any validators have exclusions then iterate through the
        // validators looking to see if any of them have excluded this
        // element.
        if (excludingValidatorCount > 0) {
            int depth = elementValidatorStack.depth();
            for (int i = 0; i < depth; i += 1) {
                ElementValidator validator = (ElementValidator)
                        elementValidatorStack.peek(i);
                if (validator.excludes(element)) {

                    // The validator has excluded the element so it is an
                    // error.
                    throw new InternationalizedValidationException(
                            ValidationMessages.EXCLUDED_CONTENT,
                            new Object[]{
                                    element,
                                    validator.getElementType()
                            });
                }
            }
        }

        // Get the validator for the element.
        ElementValidator validator =
                compiledSchema.getElementValidator(element);
        if (validator == null) {
            throw new IllegalArgumentException(
                    "Could not find validator for " + element);
        }

        // Get the containing validator if any.
        ElementValidator containingValidator = null;
        if (!elementValidatorStack.isEmpty()) {
            containingValidator = (ElementValidator)
                    elementValidatorStack.peek();
        }

        validator.open(containingValidator);
        elementValidatorStack.push(validator);
        if (validator.hasExcludes()) {
            excludingValidatorCount += 1;
        }

        // Record the fact that this content is not character data.
        precedingContent = PrecedingContent.NOT_PCDATA;
    }

    // Javadoc inherited.
    public boolean pcdata(boolean isWhitespace)
            throws ValidationException {

        // If the preceding piece of content was character data that was
        // consumed then don't bother trying to validate this as it should
        // be treated as simply part of that content.
        if (precedingContent.validatesSubsequentPCDATA(isWhitespace)) {
            return precedingContent.wasConsumed();
        }

        final boolean consumed;
        if (elementValidatorStack.isEmpty()) {
            // Character data has been found outside the document root. If
            // it is all whitespace then ignore it, otherwise it is an error.
            if (isWhitespace) {
                consumed = false;
            } else {
                throw new InternationalizedValidationException(
                        ValidationMessages.PCDATA_OUTSIDE_DOCUMENT_ROOT,
                        null);
            }
        } else {
            ElementValidator containingValidator =
                    (ElementValidator) elementValidatorStack.peek();
            consumed = containingValidator.content(
                    Content.PCDATA, !isWhitespace);
        }

        // Record whether this character data was consumed or ignored.
        precedingContent =
                consumed ? PrecedingContent.CONSUMED_PCDATA
                        : PrecedingContent.IGNORED_WHITESPACE;

        return consumed;
    }

    // Javadoc inherited.
    public void close(ElementType element) throws ValidationException {
        ElementValidator validator = (ElementValidator)
                elementValidatorStack.pop();
        if (validator.hasExcludes()) {
            excludingValidatorCount -= 1;
        }

        validator.close();

        // Record the fact that this content is not character data.
        precedingContent = PrecedingContent.NOT_PCDATA;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
