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

import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.ValidationException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Base class for all {@link PrototypeElementValidator} implementations.
 */
public abstract class AbstractElementValidator
        implements PrototypeElementValidator {

    /**
     * A comparator that compares objects based on their string representation.
     */
    private static final Comparator STRING_REPRESENTATION_COMPARATOR =
            new Comparator() {
                public int compare(Object o1, Object o2) {
                    String s1 = o1.toString();
                    String s2 = o2.toString();
                    return s1.compareTo(s2);
                }
            };

    /**
     * The type of element to which this validator applies.
     */
    protected final ElementType elementType;

    /**
     * The validator for the content, may be null.
     */
    protected final ContentValidator validator;

    /**
     * The exclusions, may be null.
     */
    protected final Set exclusions;

    /**
     * True if this element can be used anywhere but is not actually part of a
     * content model for a particular element.
     */
    protected final boolean useAnywhere;

    /**
     * Indicates whether this element can contain PCDATA or not.
     */
    protected final boolean canContainPCDATA;

    /**
     * Initialise.
     *
     * @param elementType The type of element to which this validator applies.
     * @param validator   The validator for the content, may be null.
     * @param exclusions  The exclusions, may be null.
     * @param useAnywhere True if this element can be used anywhere but is not
     * @param canContainPCDATA
     */
    protected AbstractElementValidator(
            ElementType elementType, ContentValidator validator, Set exclusions,
            boolean useAnywhere, boolean canContainPCDATA) {
        this.elementType = elementType;
        this.validator = validator;
        this.exclusions = exclusions;
        this.useAnywhere = useAnywhere;
        this.canContainPCDATA = canContainPCDATA;
    }

    // Javadoc inherited.
    public void open(ElementValidator containingValidator) {

        // If this can be used anywhere then the containing validator will not
        // know about it and so it should not be informed about it. Otherwise,
        // inform the containing validator that an element has been seen so it
        // can check to make sure that the element is allowed.
        if (!useAnywhere) {
            if (containingValidator != null) {
                containingValidator.content(elementType, true);
            }
        }
    }

    // Javadoc inherited.
    public boolean content(Content content, boolean required)
            throws ValidationException {

        if (content == Content.PCDATA) {
            if (required && !canContainPCDATA) {
                throw new InternationalizedValidationException(
                        ValidationMessages.INVALID_CONTENT,
                        new Object[]{
                                elementType,
                                describeNextExpectedContent(),
                                content
                        });
            } else {
                return required && canContainPCDATA;
            }
        }

        // If the content model does not validate and the content is actually
        // required then it is an error.
        ValidationEffect effect = validator.check(
                content, ValidationState.CURRENT);
        boolean consumed = effect.wasToConsume();
        if (required && !consumed) {
            throw new InternationalizedValidationException(
                    ValidationMessages.INVALID_CONTENT,
                    new Object[]{
                            elementType,
                        describeNextExpectedContent(),
                        content
                    });
        }

        return consumed;
    }

    // Javadoc inherited.
    public void close()
            throws ValidationException {

        // If there is still some part of the content model that is
        // unsatisified then it is an error as something is missing.
        if (validator.checkSatisfactionLevel() == SatisfactionLevel.UNSATISFIED) {

            throw new InternationalizedValidationException(
                    ValidationMessages.MISSING_CONTENT,
                    new Object[]{
                            elementType,
                        describeNextExpectedContent(),
                    });
        }
    }

    // Javadoc inherited.
    public boolean excludes(ElementType element) {
        return exclusions != null && exclusions.contains(element);
    }

    // Javadoc inherited.
    public boolean hasExcludes() {
        return exclusions != null && !exclusions.isEmpty();
    }

    // Javadoc inherited.
    public ElementType getElementType() {
        return elementType;
    }

    /**
     * Generate a string representation of the set of content that can come
     * next in order to produce a useful error message.
     *
     * @return The string representation of the expected content.
     */
    private String describeNextExpectedContent() {
        String expectedContent;
        Set nextExpectedContent = new TreeSet(STRING_REPRESENTATION_COMPARATOR);
        boolean satisfiable = validator.determineNextExpectedContent(
                nextExpectedContent, ValidationState.CURRENT);
        if (nextExpectedContent.size() != 0) {
            StringBuffer buffer = new StringBuffer();
            if (satisfiable) {
                buffer.append("(");
            }
            for (Iterator i = nextExpectedContent.iterator(); i.hasNext();) {
                Content content = (Content) i.next();
                buffer.append(content);
                if (i.hasNext()) {
                    buffer.append("|");
                }
            }
            if (satisfiable) {
                buffer.append(")?");
            }
            expectedContent = buffer.toString();
        } else {
            expectedContent = "<EMPTY>";
        }
        return expectedContent;
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
