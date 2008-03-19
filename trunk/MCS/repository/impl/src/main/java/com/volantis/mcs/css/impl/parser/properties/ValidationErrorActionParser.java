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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.MCSValidationErrorActionKeywords;
import com.volantis.mcs.themes.properties.AllowableKeywords;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * Parser for the mcs-validation-error-action property.
 */
public class ValidationErrorActionParser
        extends AbstractKeywordOrListParser {

    /**
     * The converter for items in the list.
     */
    private static final ValueConverter ITEM_CONVERTER;

    private static final AllowableKeywords SINGLE_KEYWORD;

    private static final AllowableKeywords KEYWORD_LIST;

    static {
        Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.KEYWORD);

        ArrayList keywords = new ArrayList();
        keywords.add(MCSValidationErrorActionKeywords.NONE);
        SINGLE_KEYWORD = new AllowableKeywords(keywords);

        ArrayList keywordsList = new ArrayList();
        keywordsList.add(MCSValidationErrorActionKeywords.FOCUS);
        keywordsList.add(MCSValidationErrorActionKeywords.MESSAGE);
        KEYWORD_LIST = new AllowableKeywords(keywordsList);

        ITEM_CONVERTER = new SimpleValueConverter(supportedTypes, KEYWORD_LIST);
    }

    /**
     * Initialise.
     */
    public ValidationErrorActionParser() {

        super(StylePropertyDetails.MCS_VALIDATION_ERROR_ACTION,
                SINGLE_KEYWORD,
                ITEM_CONVERTER, false);
    }
}
