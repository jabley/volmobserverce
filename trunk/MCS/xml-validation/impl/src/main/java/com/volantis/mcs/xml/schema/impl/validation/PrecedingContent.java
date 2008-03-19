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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.impl.validation;

/**
 * Enumeration of the different types of content that may precede character
 * data within the {@link DocumentValidatorImpl}.
 */
public abstract class PrecedingContent {

    /**
     * Previous content was character data that was consumed.
     */
    public static final PrecedingContent CONSUMED_PCDATA =
            new PrecedingContent("CONSUMED_PCDATA") {

                // Javadoc inherited.
                public boolean validatesSubsequentPCDATA(boolean whitespace) {
                    // Previously validating and consuming character data
                    // means that character data is supported and is allowed
                    // so no need to validate subsequent character data.
                    return true;
                }

                // Javadoc inherited.
                public boolean wasConsumed() {
                    return true;
                }
            };

    /**
     * Previous content was whitespace that was ignored because character data
     * is not supported in the current element.
     */
    public static final PrecedingContent IGNORED_WHITESPACE =
            new PrecedingContent("IGNORED_WHITESPACE") {

                // Javadoc inherited.
                public boolean validatesSubsequentPCDATA(boolean whitespace) {
                    // Previously validating and ignoring a block of whitespace
                    // prevents us from having to validate more whitespace but
                    // does not prevent us from validating other character
                    // data. In that case we know that because the whitespace
                    // was ignored that any non whitespace character data is
                    // not valid and will generate an error but we validate
                    // anyway in order to ensure that a meaningful error
                    // message is produced.
                    return whitespace;
                }

                // Javadoc inherited.
                public boolean wasConsumed() {
                    return false;
                }
            };

    /**
     * Previous content was not character data, i.e. was an element.
     */
    public static final PrecedingContent NOT_PCDATA =
            new PrecedingContent("NOT_PCDATA") {

                // Javadoc inherited.
                public boolean validatesSubsequentPCDATA(boolean whitespace) {
                    // Validation of preceding content that was not character
                    // data (i.e. was element) cannot validate character data.
                    return false;
                }

                // Javadoc inherited.
                public boolean wasConsumed() {
                    throw new IllegalStateException("Unreachable");
                }
            };

    /**
     * The name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    private PrecedingContent(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }

    /**
     * Determine whether preceding content that has been successfully validated
     * validates the use of a subsequent piece of character data.
     *
     * @param whitespace True if the subsequent piece of character data is all
     *                   whitespace.
     * @return True if the validation of the preceding content prevents us from
     *         having to validate the current piece of character, false if we
     *         do have to validate the current content.
     */
    public abstract boolean validatesSubsequentPCDATA(boolean whitespace);

    /**
     * Determine whether the preceding content was consumed or not.
     *
     * @return True if it was consumed, false otherwise.
     */
    public abstract boolean wasConsumed();
}
