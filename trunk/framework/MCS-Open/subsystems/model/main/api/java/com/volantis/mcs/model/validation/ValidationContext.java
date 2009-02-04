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

package com.volantis.mcs.model.validation;

import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.Step;

/**
 * The content within which validation occurs.
 *
 * @mock.generate 
 */
public interface ValidationContext {

    /**
     * Add a diagnostic message for the current path.
     *
     * @param source  The source location.
     * @param level    The diagnostic level.
     * @param message  The message itself.
     */
    void addDiagnostic(
            SourceLocation source, DiagnosticLevel level, I18NMessage message);

    /**
     * Add a diagnostic message.
     *
     * @param source  The source location.
     * @param path    The path to the source of the diagnostic message.
     * @param level   The diagnostic level.
     * @param message The message itself.
     */
    void addDiagnostic(
            SourceLocation source, Path path, DiagnosticLevel level,
            I18NMessage message);

    /**
     * Create an i18n message that takes no arguments.
     *
     * @param messageKey The message key.
     * @return The i18n message.
     */
    I18NMessage createMessage(String messageKey);

    /**
     * Create an i18n message that takes one argument.
     *
     * @param messageKey The message key.
     * @param arg        The argument.
     * @return The i18n message.
     */
    I18NMessage createMessage(String messageKey, Object arg);

    /**
     * Create an i18n message that takes two arguments.
     *
     * @param messageKey The message key.
     * @param arg1       The first argument.
     * @param arg2       The second argument.
     * @return The i18n message.
     */
    I18NMessage createMessage(String messageKey, Object arg1, Object arg2);

    /**
     * Create an i18n message that takes a number of arguments.
     *
     * @param messageKey The message key.
     * @param arguments  The array of arguments.
     * @return The i18n message.
     */
    I18NMessage createMessage(String messageKey, Object[] arguments);

    DefinitionScope beginDefinitionScope(DefinitionType definitionType);

    DefinitionScope getDefinitionScope(DefinitionType definitionType);

    void endDefinitionScope(DefinitionType definitionType);

    Step pushPropertyStep(PropertyIdentifier property);

    Step pushPropertyStep(String property);

    Step pushIndexedStep(int index);

    Path getCurrentPath();

    void popStep(Step step);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9992/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Added stack of objects in validation context

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
