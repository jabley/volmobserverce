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

package com.volantis.mcs.model;

import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.Validator;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeHandler;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * todo Document this.
 */
public abstract class ModelFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.model.impl.ModelFactoryImpl",
                        ModelFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ModelFactory getDefaultInstance() {
        return (ModelFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create a validator for validating a model.
     *
     * @return The newly created validator.
     */
    public abstract Validator createValidator();

    public abstract StrictValidator createStrictValidator();

    /**
     * Create a strict validator which prunes invalid data before reporting
     * any errors.
     *
     * @return the created strict validator.
     */
    public abstract StrictValidator createStrictPruningValidator();

    /**
     * Create an i18n message that takes no arguments.
     *
     * @param messageKey The message key.
     * @return The i18n message.
     */
    public abstract I18NMessage createMessage(String messageKey);

    /**
     * Create an i18n message that takes one argument.
     *
     * @param messageKey The message key.
     * @param arg        The argument.
     * @return The i18n message.
     */
    public abstract I18NMessage createMessage(String messageKey, Object arg);

    /**
     * Create an i18n message that takes two arguments.
     *
     * @param messageKey The message key.
     * @param arg1       The first argument.
     * @param arg2       The second argument.
     * @return The i18n message.
     */
    public abstract I18NMessage createMessage(
            String messageKey, Object arg1, Object arg2);

    /**
     * Create an i18n message that takes a number of arguments.
     *
     * @param messageKey The message key.
     * @param arguments  The array of arguments.
     * @return The i18n message.
     */
    public abstract I18NMessage createMessage(
            String messageKey, Object[] arguments);

    /**
     * Create a Diagnostic object.
     *
     * @param location The location within the source.
     * @param level    The level.
     * @param message  The message.
     * @return The diagnostic instance.
     */
    public abstract Diagnostic createDiagnostic(
            SourceLocation location, DiagnosticLevel level,
            I18NMessage message);

    /**
     * Create a definition type that is identified by a class.
     *
     * @param definitionClass The definition class.
     * @return The definition type.
     */
    public abstract DefinitionType registerDefinitionType(
            Class definitionClass, DefinitionTypeHandler handler);

    /**
     * Parse the string as a path to a proxy and return an equivalent object.
     *
     * @param path The path to parse.
     * @return The returned path.
     */
    public abstract Path parsePath(String path);

    public abstract PathBuilder createPathBuilder();

    /**
     * Create a {@link SourceLocation}.
     *
     * @param url    The document source.
     * @param line   The line.
     * @param column The column.
     * @return The location.
     */
    public abstract SourceLocation createSourceLocation(
            String url, int line, int column);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/6	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
