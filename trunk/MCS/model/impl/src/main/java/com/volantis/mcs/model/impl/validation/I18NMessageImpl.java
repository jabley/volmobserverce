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
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.synergetics.localization.Category;
import com.volantis.synergetics.localization.MessageLocalizer;

public class I18NMessageImpl
        implements I18NMessage {

    private static final MessageLocalizer MESSAGE_LOCALIZER =
            LocalizationFactory.createMessageLocalizer(
                    ValidationContextImpl.class);

    private final String key;
    private final String message;
    private final Object[] arguments;

    public I18NMessageImpl(String key, Object[] arguments) {

        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        this.key = key;
        this.arguments = arguments;
        this.message = MESSAGE_LOCALIZER.format(key, arguments);
    }

    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }

    public int getArgumentCount() {
        return arguments == null ? 0 : arguments.length;
    }

    public Object getArgument(int argument) {
        return arguments[argument];
    }

    private Category getCategory(DiagnosticLevel level) {
        if (level == DiagnosticLevel.ERROR) {
            return Category.ERROR;
        } else if (level == DiagnosticLevel.WARNING) {
            return Category.WARN;
        } else {
            throw new IllegalArgumentException("Unknown level " + level);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof I18NMessage)) {
            return false;
        }

        I18NMessage other = (I18NMessage) obj;
        if (!getKey().equals(other.getKey())) {
            return false;
        }
        final int argumentCount = getArgumentCount();
        if (argumentCount != other.getArgumentCount()) {
            return false;
        }
        for (int i = 0; i < argumentCount; i += 1) {
            final Object argument = getArgument(i);
            final Object otherArgument = other.getArgument(i);
            if (argument == null ? otherArgument != null : !argument.equals(
                    otherArgument)) {
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        int result = getKey().hashCode();
        final int argumentCount = getArgumentCount();
        result = result * 37 + argumentCount;
        for (int i = 0; i < argumentCount; i += 1) {
            Object argument = getArgument(i);
            result = result * 37 + (argument == null ? 0 : argument.hashCode());
        }
        return result;
    }

    public String toString() {
        if (message.startsWith("CANNOT RETRIEVE LOCALIZED MESSAGE ")) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(key).append("(");
            final int argumentCount = getArgumentCount();
            for (int i = 0; i < argumentCount; i += 1) {
                Object argument = getArgument(i);
                if (i != 0) {
                    buffer.append(", ");
                }
                buffer.append(argument);
            }
            buffer.append(")");
            return buffer.toString();
        } else {
            return message;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
