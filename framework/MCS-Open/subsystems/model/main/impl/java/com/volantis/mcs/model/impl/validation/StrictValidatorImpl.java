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

package com.volantis.mcs.model.impl.validation;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.Validator;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.Pruneable;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.List;

public class StrictValidatorImpl
        implements StrictValidator {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StrictValidatorImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    StrictValidatorImpl.class);

    private final Validator validator;

    /**
     * If true, and the validatable is also pruneable, prune any invalid
     * components of the data before processing the errors.
     */
    private final boolean pruning;

    public StrictValidatorImpl(ModelFactory modelFactory) {
        this(modelFactory, false);
    }

    /**
     * Initialise.
     *
     * @param modelFactory required factory.
     * @param pruning if true, try and prune invalid data before processing
     *      errors.
     */
    public StrictValidatorImpl(ModelFactory modelFactory, boolean pruning) {
        validator = new ValidatorImpl(modelFactory);
        this.pruning = pruning;
    }

    public ValidationContext getValidationContext() {
        return validator.getValidationContext();
    }

    public void validate(Validatable validatable) {
        validator.validate(validatable);

        List diagnostics = validator.getDiagnostics();

        // If we are pruning and this validatable object is also prunable, then
        // prune it before we report any errors.
        if (pruning && validatable instanceof Pruneable) {
            Pruneable pruneable = (Pruneable) validatable;
            pruneable.prune(validator.getValidationContext(), diagnostics);
        }

        if (!diagnostics.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            boolean foundError = false;
            for (int i = 0; i < diagnostics.size(); i++) {
                Diagnostic diagnostic = (Diagnostic) diagnostics.get(i);

                // Add the level.
                DiagnosticLevel level = diagnostic.getLevel();
                if (level == DiagnosticLevel.ERROR) {
                    foundError = true;
                }
                buffer.append(level);

                // Add the position in the file
                buffer.append(" ");
                SourceLocation location = diagnostic.getLocation();
                buffer.append(location.getSourceDocumentName());
                buffer.append(":(");
                buffer.append(location.getSourceLineNumber());
                buffer.append(",");
                buffer.append(location.getSourceColumnNumber());
                buffer.append(")");

                // Only add the path if debug is enabled since it is internal
                // but it can be pretty useful.
                if (logger.isDebugEnabled()) {
                    buffer.append(" ");
                    buffer.append(diagnostic.getPath());
                }

                // Add the message
                buffer.append(" - ");
                buffer.append(diagnostic.getMessage());
                buffer.append("\n");
            }

            if (foundError) {
                if (logger.isErrorEnabled()) {
                    logger.error("validation-errors", buffer.toString());
                }
                throw new ValidationException(EXCEPTION_LOCALIZER.format(
                        "validation-errors", buffer));
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn("validation-errors", buffer.toString());
                }
            }
        }
    }
}
