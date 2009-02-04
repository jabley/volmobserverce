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

package com.volantis.mcs.xml.schema.impl.compiler;

import com.volantis.mcs.xml.schema.impl.validation.ContentTypeValidator;
import com.volantis.mcs.xml.schema.impl.validation.ContentValidator;
import com.volantis.mcs.xml.schema.impl.validation.ElementValidator;
import com.volantis.mcs.xml.schema.model.AnyContent;
import com.volantis.mcs.xml.schema.model.BoundedContent;
import com.volantis.mcs.xml.schema.model.CharacterContent;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ContentChoice;
import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ContentModelVisitor;
import com.volantis.mcs.xml.schema.model.ContentSequence;
import com.volantis.mcs.xml.schema.model.ElementReference;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.EmptyContent;
import com.volantis.mcs.xml.schema.model.WrappingContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidatorPrototypeBuilder
        implements ContentModelVisitor {

    private final ValidatorPrototypeFactory prototypeFactory;

    private final Map model2ContentValidator;

    private ContentValidator contentValidator;

    private ContentCollector contentCollector;

    private Set exclusions;

    private boolean mixed;

    public ValidatorPrototypeBuilder(
            ValidatorPrototypeFactory prototypeFactory) {
        this.prototypeFactory = prototypeFactory;

        model2ContentValidator = new HashMap();

        contentCollector = new ContentCollector();
    }

    public ElementValidator build(ElementSchema schema) {

        ElementType type = schema.getElementType();
        ContentModel model = schema.getContentModel();
        boolean useAnywhere = schema.getUseAnywhere();
        boolean transparent = schema.isTransparent();
        if (!transparent && model == null) {
            throw new IllegalStateException("Model not provided: " +
                    type);
        }

        mixed = false;
        this.exclusions = null;

        ContentValidator contentValidator = null;
        if (model != null) {
            contentValidator = processReferencedValidator(model);
            if (contentValidator == null) {
                throw new IllegalStateException("No validator created");
            }
        }

        return prototypeFactory.createElementValidator(
                type, contentValidator, exclusions, useAnywhere, transparent,
                mixed);
    }

    private ContentValidator processReferencedValidator(ContentModel model) {

        updateExclusions(model);

        // Check to see whether this builder has already created a content
        // validator for the model.
        CachedValidator cachedValidator;
        ContentValidator validator;
        cachedValidator = (CachedValidator) model2ContentValidator.get(model);
        if (cachedValidator != null) {
            mixed = mixed || cachedValidator.isMixed();
            return cachedValidator.getValidator();
        }

        ContentValidator savedValidator = contentValidator;
        contentValidator = null;

        boolean savedMixed = mixed;
        mixed = false;

        model.accept(this);

        validator = contentValidator;
        if (validator != null) {
            // todo store special marker or change above code to check
            // todo for containment of the model in order to allow this to
            // todo record that no validator was found.

            cachedValidator = new CachedValidator(validator, mixed);

            // Store the validator in the model.
            model2ContentValidator.put(model, cachedValidator);
        }

        mixed |= savedMixed;
        contentValidator = savedValidator;

        return validator;
    }

    public void visit(WrappingContent wrapping) {
        ContentModel nested = wrapping.getContentModel();
        nested.accept(this);
    }

    public void visit(BoundedContent bounded) {
        ContentModel nested = bounded.getContentModel();
        int minimum = bounded.getMinimum();
        int maximum = bounded.getMaximum();

        // Get the validator for the nested content.
        ContentValidator nestedValidator = processReferencedValidator(nested);
        if (nestedValidator == null) {
            return;
        }

        if (minimum == 0 && maximum == 1) {

            // optional
            contentValidator = prototypeFactory.createBoundedContentValidator(
                    nestedValidator, minimum, maximum);

        } else if (minimum == 0 && maximum == Integer.MAX_VALUE) {
            // unlimited

            // If the elementValidator requires per element state then we need
            // to wrap it in a repeating validator but otherwise we can just
            // return it as is.
            if (nestedValidator.requiresPerElementState()) {
                contentValidator =
                        prototypeFactory.createBoundedContentValidator(
                                nestedValidator, minimum, maximum);
            } else {
                contentValidator =
                        prototypeFactory.createUnlimited(nestedValidator);
            }

        } else if (minimum == 1 && maximum == Integer.MAX_VALUE) {

            // at least one
            contentValidator = prototypeFactory.createBoundedContentValidator(
                    nestedValidator, minimum, maximum);

        } else {
            // range
            contentValidator = prototypeFactory.createBoundedContentValidator(
                    nestedValidator, minimum, maximum);
        }
    }

    public void visit(ContentChoice choice) {
        List validators = visitCompositeContents(choice);
        if (validators.isEmpty()) {
            return;
        }

        // Optimise the validators.
        // Merge all the type validators together, if they are all type
        // validators then do not bother wrapping them in a choice.
        List optimisedValidators = null;
        Set validTypes = null;
        for (int i = 0; i < validators.size(); i++) {
            ContentValidator validator = (ContentValidator) validators.get(i);
            if (validator instanceof ContentTypeValidator) {
                ContentTypeValidator typeValidator =
                        (ContentTypeValidator) validator;
                if (validTypes == null) {
                    validTypes = new HashSet();
                }
                typeValidator.addTo(validTypes);
            } else {
                if (optimisedValidators == null) {
                    optimisedValidators = new ArrayList();
                }
                optimisedValidators.add(validator);
            }
        }

        // Create a multiple validator.
        if (validTypes != null) {
            contentValidator = prototypeFactory
                    .createMultipleTypeValidator(validTypes);
            if (optimisedValidators != null) {
                optimisedValidators.add(contentValidator);
            }
        }

        if (optimisedValidators != null) {
            contentValidator = prototypeFactory.createChoiceValidator(
                    optimisedValidators);
        }
//
//        if (contentValidator == null) {
//            throw new IllegalArgumentException("No validators found");
//        }
    }

    public void visit(ElementReference element) {
        contentValidator = prototypeFactory.createSingleTypeValidator(
                element.getElementType());
    }

    public void visit(EmptyContent empty) {
        contentValidator = prototypeFactory.createEmptyContent();
    }

    public void visit(CharacterContent pcdata) {
        contentValidator = prototypeFactory.createSingleTypeValidator(
                Content.PCDATA);

        mixed = true;
    }

    public void visit(AnyContent any) {
        contentValidator = prototypeFactory.createAnyContentValidator();
    }

    public void visit(ContentSequence sequence) {
        List validators = visitCompositeContents(sequence);
        if (validators.isEmpty()) {
            return;
        }

        contentValidator =
                prototypeFactory.createSequenceValidator(validators);
    }

    private void updateExclusions(ContentModel model) {
        Iterator i = model.excluded();
        if (i != null) {
            while (i.hasNext()) {
                ContentModel nested = (ContentModel) i.next();
                exclusions = contentCollector.collectContent(
                        nested, exclusions);
            }
        }
    }

    private List visitCompositeContents(CompositeModel composite) {
        Iterator iterator = composite.iterator();
        List validators = new ArrayList();
        while (iterator.hasNext()) {
            ContentModel model = (ContentModel) iterator.next();
            ContentValidator validator = processReferencedValidator(model);
            if (validator != null) {
                validators.add(validator);
            }
        }
        return validators;
    }

    private static class ContentCollector
            implements ContentModelVisitor {

        private Set content;

        public Set collectContent(ContentModel model, Set content) {
            this.content = content;
            model.accept(this);
            return this.content;
        }


        public void visit(WrappingContent wrapping) {
            wrapping.getContentModel().accept(this);
        }

        public void visit(BoundedContent bounded) {
            bounded.getContentModel().accept(this);
        }

        public void visit(ContentChoice choice) {
            visitComposite(choice);
        }

        public void visit(ContentSequence sequence) {
            visitComposite(sequence);
        }

        private void visitComposite(CompositeModel composite) {
            Iterator i = composite.iterator();
            while (i.hasNext()) {
                ContentModel model = (ContentModel) i.next();
                model.accept(this);
            }
        }

        public void visit(ElementReference element) {
            if (content == null) {
                content = new HashSet();
            }

            content.add(element.getElementType());
        }

        public void visit(EmptyContent empty) {
        }

        public void visit(CharacterContent pcdata) {
            if (content == null) {
                content = new HashSet();
            }

            content.add(Content.PCDATA);
        }

        public void visit(AnyContent any) {
            throw new IllegalStateException(
                    "Cannot collect content from any model");
        }
    }

    private static class CachedValidator {

        private final ContentValidator validator;

        private final boolean mixed;

        public CachedValidator(ContentValidator validator, boolean mixed) {
            this.mixed = mixed;
            this.validator = validator;
        }

        public ContentValidator getValidator() {
            return validator;
        }

        public boolean isMixed() {
            return mixed;
        }
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
