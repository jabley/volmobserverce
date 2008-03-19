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

import com.volantis.mcs.xml.schema.impl.validation.AnyContentValidator;
import com.volantis.mcs.xml.schema.impl.validation.BasicElementValidator;
import com.volantis.mcs.xml.schema.impl.validation.BoundedContentValidator;
import com.volantis.mcs.xml.schema.impl.validation.ContentChoiceValidator;
import com.volantis.mcs.xml.schema.impl.validation.ContentSequenceValidator;
import com.volantis.mcs.xml.schema.impl.validation.ContentValidator;
import com.volantis.mcs.xml.schema.impl.validation.EmptyContentValidator;
import com.volantis.mcs.xml.schema.impl.validation.MultipleTypeValidator;
import com.volantis.mcs.xml.schema.impl.validation.PrototypeElementValidator;
import com.volantis.mcs.xml.schema.impl.validation.SingleTypeValidator;
import com.volantis.mcs.xml.schema.impl.validation.UnlimitedValidator;
import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.List;
import java.util.Set;

public class ValidatorPrototypeFactoryImpl
        implements ValidatorPrototypeFactory {

    private static final ContentValidator EMPTY_CONTENT =
            new EmptyContentValidator();
    
    private static final ContentValidator ANY_CONTENT =
            new AnyContentValidator();

    public ContentValidator createBoundedContentValidator(
            ContentValidator validator, int minimum, int maximum) {
        return new BoundedContentValidator(validator, minimum, maximum);
    }

    public ContentValidator createUnlimited(ContentValidator validator) {
        return new UnlimitedValidator(validator);
    }

    public ContentValidator createSequenceValidator(List validators) {
        ContentValidator[] array = toArray(validators);
        return new ContentSequenceValidator(array);
    }

    private ContentValidator[] toArray(List validators) {
        int length = validators.size();
        ContentValidator[] array = new ContentValidator[length];
        validators.toArray(array);
        return array;
    }

    public ContentValidator createEmptyContent() {
        return EMPTY_CONTENT;
    }

    public PrototypeElementValidator createElementValidator(
            ElementType type, ContentValidator validator, Set exclusions,
            boolean useAnywhere, boolean transparent, boolean canContainPCDATA) {

        if (transparent) {
            return new TransparentElementValidator(
                    type, exclusions, useAnywhere);
        } else {
            return new BasicElementValidator(
                    type, validator, exclusions, useAnywhere, canContainPCDATA);
        }
    }

    public ContentValidator createMultipleTypeValidator(Set types) {
        return new MultipleTypeValidator(types);
    }

    public ContentValidator createChoiceValidator(List validators) {
        ContentValidator[] array = toArray(validators);
        return new ContentChoiceValidator(array);
    }

    public ContentValidator createSingleTypeValidator(Content content) {
        return new SingleTypeValidator(content);
    }

    public ContentValidator createAnyContentValidator() {
        return ANY_CONTENT;
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
