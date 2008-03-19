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

import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.Validator;
import com.volantis.mcs.model.validation.Pruneable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestValidator {

    /**
     * If true, and the validatable is also pruneable, prune any invalid
     * components of the data before processing the errors.
     */
    private boolean pruning;

    private List expectedDiagnostics;

    /**
     * Initialise.
     */
    public TestValidator() {
        this(false);
    }

    /**
     * Initalise.
     *
     * @param pruning if true, try and prune invalid data before processing
     *      errors.
     */
    public TestValidator(boolean pruning) {
        expectedDiagnostics = new ArrayList();
        this.pruning = pruning;
    }

    public void validate(Validatable validatable) {
        Validator validator = ModelFactory.getDefaultInstance()
                .createValidator();
        validator.validate(validatable);

        if (pruning && validatable instanceof Pruneable) {
            ((Pruneable)validatable).prune(validator.getValidationContext(),
                    validator.getDiagnostics());
        }

        StringBuffer buffer = new StringBuffer();

        List diagnostics = validator.getDiagnostics();
        for (int i = 0; i < expectedDiagnostics.size(); i++) {
            ExpectedDiagnostic expectedDiagnostic = (ExpectedDiagnostic)
                    expectedDiagnostics.get(i);

            boolean matched = false;
            for (Iterator d = diagnostics.iterator();
                 !matched && d.hasNext();) {
                Diagnostic diagnostic = (Diagnostic) d.next();
                if (expectedDiagnostic.matches(diagnostic)) {
                    d.remove();
                    matched = true;
                }
            }

            if (!matched) {
                buffer.append("Expected Diagnostic: " + expectedDiagnostic +
                        " was unmatched\n");
            }
        }

        if (!diagnostics.isEmpty()) {
            if (buffer.length() != 0) {
                buffer.append("\n");
            }
            buffer.append("The following diagnostics were unexpected:");
            for (Iterator i = diagnostics.iterator(); i.hasNext();) {
                Diagnostic diagnostic = (Diagnostic) i.next();
                buffer.append(diagnostic).append("\n");
            }
        }

        if (buffer.length() != 0) {
            throw new IllegalStateException(
                    "Diagnostics not matched\n" + buffer);
        }
    }

    public void expectDiagnostic(DiagnosticLevel level, String path,
            String key) {

        ExpectedDiagnostic expectedDiagnostic =
                new ExpectedDiagnostic(path, level, key, null);
        expectedDiagnostics.add(expectedDiagnostic);
    }

    public void expectDiagnostic(DiagnosticLevel level, String path,
            String key, Object argument) {

        ExpectedDiagnostic expectedDiagnostic =
                new ExpectedDiagnostic(path, level, key,
                        new Object[] {argument});
        expectedDiagnostics.add(expectedDiagnostic);
    }

    public void expectDiagnostic(DiagnosticLevel level, String path,
            String key, Object[] arguments) {

        ExpectedDiagnostic expectedDiagnostic =
                new ExpectedDiagnostic(path, level, key, arguments);
        expectedDiagnostics.add(expectedDiagnostic);
    }

    private static class ExpectedDiagnostic {
        private final String path;
        private final DiagnosticLevel level;
        private final String key;
        private final Object[] arguments;

        public ExpectedDiagnostic(String path,
                DiagnosticLevel level, String key,
                Object[] arguments) {

            this.path = path;
            this.level = level;
            this.key = key;
            this.arguments = arguments;
        }

        boolean matches(Diagnostic diagnostic) {

            I18NMessage message = diagnostic.getMessage();
            int argumentCount = message.getArgumentCount();

            boolean matched;
            if (diagnostic.getPath().getAsString().equals(path)
                    && diagnostic.getLevel() == level
                    && message.getKey().equals(key)
                    && (arguments == null ?
                    argumentCount == 0 :
                    argumentCount == arguments.length)) {

                matched = true;
                for (int a = 0; matched && a < argumentCount; a += 1) {
                    Object expected = arguments[a];
                    Object actual = message.getArgument(a);
                    if (expected == null ?
                            actual != null : !expected.equals(actual)) {
                        matched = false;
                    }
                }
            } else {
                matched = false;
            }

            return matched;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(level).append(" - ").append(path).append(" ")
                    .append(key);
            if (arguments != null && arguments.length != 0) {
                buffer.append("(");
                for (int i = 0; i < arguments.length; i++) {
                    Object argument = arguments[i];
                    if (i != 0) {
                        buffer.append(", ");
                    }
                    buffer.append(argument);
                }
                buffer.append(")");
            }
            return buffer.toString();
        }
    }
}
