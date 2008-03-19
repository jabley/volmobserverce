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

package com.volantis.mcs.model.impl;

import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.impl.path.PathBuilderImpl;
import com.volantis.mcs.model.impl.path.PathImpl;
import com.volantis.mcs.model.impl.validation.I18NMessageImpl;
import com.volantis.mcs.model.impl.validation.ValidatorImpl;
import com.volantis.mcs.model.impl.validation.StrictValidatorImpl;
import com.volantis.mcs.model.impl.validation.DiagnosticImpl;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.Validator;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ModelFactoryImpl
        extends ModelFactory {

    /**
     * The empty path.
     */
    private static final PathImpl EMPTY_PATH = new PathImpl(null);

    private Map definitionTypeRegistry = new HashMap();

    public Validator createValidator() {
        return new ValidatorImpl(this);
    }

    public StrictValidator createStrictValidator() {
        return new StrictValidatorImpl(this, false);
    }

    // Javadoc inherited.
    public StrictValidator createStrictPruningValidator() {
        return new StrictValidatorImpl(this, true);
    }

    // Javadoc inherited.
    public I18NMessage createMessage(String messageKey) {
        return new I18NMessageImpl(messageKey, null);
    }

    // Javadoc inherited.
    public I18NMessage createMessage(String messageKey, Object arg) {
        return new I18NMessageImpl(messageKey, new Object[]{arg});
    }

    // Javadoc inherited.
    public I18NMessage createMessage(
            String messageKey, Object arg1, Object arg2) {
        return new I18NMessageImpl(messageKey, new Object[]{arg1, arg2});
    }

    // Javadoc inherited.
    public I18NMessage createMessage(String messageKey, Object[] arguments) {
        return new I18NMessageImpl(messageKey, arguments);
    }

    // Javadoc inherited.
    public Diagnostic createDiagnostic(
            SourceLocation location, DiagnosticLevel level,
            I18NMessage message) {
        return new DiagnosticImpl(location, EMPTY_PATH, level, message);
    }

    public DefinitionType registerDefinitionType(
            Class definitionClass, DefinitionTypeHandler handler) {

        if (definitionClass == null) {
            throw new IllegalArgumentException("definitionClass cannot be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler cannot be null");
        }
        
        DefinitionType type = (DefinitionType) definitionTypeRegistry.get(
                definitionClass);
        if (type != null) {
            throw new IllegalArgumentException(
                    "Definition type for " + definitionClass +
                    " is already registered");
        }

        type = new DefinitionType(handler);
        definitionTypeRegistry.put(definitionClass, type);
        return type;
    }

    public Path parsePath(String path) {

        PathBuilder builder = new PathBuilderImpl();
//        PathImpl pathImpl = new PathImpl();

        StringTokenizer tokenizer = new StringTokenizer(path, "/");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            // Try and convert it to an integer.
            try {
                int index = Integer.parseInt(token);
                builder.addIndexedStep(index);
            } catch (NumberFormatException e) {
                // Assume it is a property name.
                builder.addPropertyStep(token);
            }
        }

        return builder.getPath();
    }

    public PathBuilder createPathBuilder() {
        return new PathBuilderImpl();
    }

    // Javadoc inherited.
    public SourceLocation createSourceLocation(
            String url, int line, int column) {
        return new SourceLocationImpl(url, line, column);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
