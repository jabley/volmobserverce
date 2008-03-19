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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xml.validation.sax;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.xml.sax.Attributes;

import java.util.ResourceBundle;

/**
 * Instances of this class can be used to derive regular expressions from
 * error messages, extracted from the given bundle using the given key. Any
 * {@link java.text.MessageFormat} style substitution arguments will be
 * replaced by "greedy, any number of any character" expression components. The
 * value of a single, ordinally-specified substitution argument can be reserved
 * as a special value that will be returned if a match is achieved against the
 * regular expression. If no argument is selected, a match is indicated using
 * the empty string.
 *
 * <p>The Apache regexp regular expression package will be used for all
 * regular expression processing.</p>
 */
public class ParserError {

    /**
     * String used in regular expressions for matching any number or character
     */
    private static String MATCH_ANY = ".*"; //$NON-NLS-1$

    /**
     * String used in regular expressions for matching any number or character.
     * This expression allows the retrieval of an text that matched after
     * the expression has been executed.
     */
    private static String MATCH_ANY_AND_SAVE = "(.*)"; //$NON-NLS-1$

    /**
     * An array of characters that have special meaning in a regular expression.
     */
    private static char[] regexpChars = new char[]
            {'.', '|', '*', '\\', '[', ']', '^', '$', '?', '+',
             '{', '}', '(', ')'};

    /**
     * A constant that indicates that there is no selectable/selected argument
     * in the specified template. Indices for arguments are identify by
     * ordinal position of a message substitution point in the message template
     * and not by message format argument index.
     */
    public static final int NO_ARG = -1;

    /**
     * The error's identifier key. This should be one of the standard keys from
     * the {@link com.volantis.mcs.eclipse.validation.ValidationMessageBuilder}.
     */
    private final String key;

    /**
     * A regular expression derived from the error message.
     */
    protected final RE template;

    /**
     * Specifies the template argument that will contain the parameter of
     * interest when a match is made against the template regular expression.
     */
    protected int paramIndex = NO_ARG;

    /**
     * Indicates whether the error relates to an attribute. If it does not, it
     * relates to an element (or perhaps a text node).
     */
    private final boolean attribute;

    /**
     * Initializes the new instance using the given parameters. This
     * constructor is for non-attribute errors without a parameter.
     * @param bundle the {@link ResourceBundle} where the error message resides
     * @param bundleKey the key that should be used to locate the error message
     * in the bundle
     * @param key the error's identifier key. This should be one of the
     * standard keys as specified in
     * {@link com.volantis.mcs.eclipse.validation.ValidationMessageBuilder}.
     * @throws ParserErrorException if the message located in the bundle is
     * badly formated
     */
    public ParserError(ResourceBundle bundle,
                       String bundleKey,
                       String key) throws ParserErrorException {
        this(bundle, bundleKey, key, NO_ARG, false);
    }

    /**
     * Initializes the new instance using the given parameters.
     * @param bundle the {@link ResourceBundle} where the error message resides
     * @param bundleKey the key that should be used to locate the error message
     * in the bundle
     * @param key the error's identifier key. This should be one of the
     * standard keys as specified in
     * {@link com.volantis.mcs.eclipse.validation.ValidationMessageBuilder}.
     * @param paramIndex specifies the template argument that will contain
     * the parameter of interest when a match is made against the templates
     * regular expression. <p><strong>Note:</strong> the parameter index may
     * not be NO_ARG if the attribute arg is true since this parameter
     * is expected to name the attribute.</p>
     * @param attribute indicates whether the error relates to an attribute.
     * If it does not, it relates to an element (or perhaps a text node).
     * @throws ParserErrorException if the message located in the bundle is
     * badly formatted
     * @throws IllegalArgumentException if the attribute parameters value is
     * true and the paramIndex parameter is NO_ARG
     */
    public ParserError(ResourceBundle bundle,
                       String bundleKey,
                       String key,
                       int paramIndex,
                       boolean attribute) throws ParserErrorException {
        if (attribute &&
                (paramIndex == NO_ARG)) {
            throw new IllegalArgumentException(
                    "A parameter naming the required attribute must be " + //$NON-NLS-1$
                    "available"); //$NON-NLS-1$
        }

        this.key = key;
        this.paramIndex = paramIndex;
        this.attribute = attribute;
        try {
            template = convertToRegExp(bundle.getString(bundleKey));
        } catch (RESyntaxException e) {
            throw new ParserErrorException(e);
        }
    }

    /**
     * Replaces message format parameter substitution points with regular
     * expression "greedy, any number of any character" pattern and escapes any
     * "special" regular expression characters found elsewhere in the message.
     *
     * <p>Note that only the {@link #paramIndex}th substitution point should be
     * constructed to allow querying of the value matched.</p>
     * @param message the message to convert
     * @return a RE regular expression constructed from the converted string
     * @throws RESyntaxException if the message string is badly formatted
     * @throws ParserErrorException if the paramIndex value is not NO_ARG and
     * the regular expression contain less message format parameters than the
     * paramIndex value.
     */
    private RE convertToRegExp(String message)
            throws RESyntaxException, ParserErrorException {
        return new RE(convertToRegExpString(message));
    }

    /**
     * Replaces message format parameter substitution points with regular
     * expression "greedy, any number of any character" pattern and escapes any
     * "special" regular expression characters found elsewhere in the message.
     *
     * <p>Note that only the {@link #paramIndex}th substitution point should be
     * constructed to allow querying of the value matched.</p>
     * @param message the message to convert
     * @return the converted string
     * @throws ParserErrorException if the paramIndex value is not NO_ARG and
     * the regular expression contain less message format parameters than the
     * paramIndex value.
     */
    private String convertToRegExpString(String message)
            throws ParserErrorException {
        // This method performs the following converts the message
        // argument as follows.
        // 1. escape any required special RE characters using "\":
        //    ".", "|", "*", "\", "[", "]", "^", "$", "?", "+", "{", "}",
        //    "(" and ")"
        // 2. replace each "\{<number>\}" with ".*" in the template, except
        //    for the {@link #paramIndex}th one which must be substituted by
        //    "(.*)" to enable querying of that matched value. Note that if
        //    the {@link #paramIndex}th entry doesn't exist an exception must
        //    be thrown - the instance is incorrectly initialized
        // 3. Note: the message passed in will be read from a properties file
        //    that is intended to be used via a MessageFormat instance.
        //    Therefore we need to be aware that a single quote is used to
        //    escape { and }. Also a single quote is escaped by a second single
        //    quote

        char c;
        boolean escaped;
        int paramSubstitutionCount = 0;
        // flag used to control whether {<number>} should be processed. If
        // a single quote has not been closed we do not process any
        // substitution points
        boolean processSubstitutionPoints = true;
        StringBuffer buffer = new StringBuffer(message.length() * 2);
        for (int i = 0; i < message.length(); i++) {
            // extract the character
            c = message.charAt(i);
            escaped = false;
            // see if the current character needs to be escaped.
            for (int j = 0; j < regexpChars.length && !escaped; j++) {
                if (c == regexpChars[j]) {
                    // this character needs to be escaped. However, we treat {
                    // character specially
                    escaped = true;
                    if (c != '{') {
                        buffer.append('\\').append(c);
                    } else {
                        // If we find {<number>} where <number> is any numeric
                        // value we replace {<number>} with .* or (.*).
                        // If <number> is not numeric then we must
                        // escape the { character

                        boolean nonNumeric = false;
                        boolean foundClosingBrace = false;
                        char d;
                        // break out of this loop if we get to the end
                        // of the message or we find an closing brace or we
                        // find a non numeric character
                        for (int pos = i + 1;
                             pos < message.length() && !foundClosingBrace
                                && !nonNumeric && processSubstitutionPoints;
                             pos++) {

                            // see if characters up to the next '}'
                            // are all numeric
                            d = message.charAt(pos);
                            if (d == '}') {
                                // found the matching }
                                foundClosingBrace = true;
                                // as we are still in this loop all the
                                // characters must have been numeric

                                String re;
                                // replace the {<number>} with either .* or (.*)
                                if (paramSubstitutionCount++ == paramIndex) {
                                    re = MATCH_ANY_AND_SAVE;
                                } else {
                                    re = MATCH_ANY;
                                }
                                buffer.append(re);
                                // set the counter in the outer loop so that
                                // we skip the {<number} string that we have
                                // just processed.
                                i = pos;
                            } else if (!Character.isDigit(d)) {
                                // non numeric character.
                                nonNumeric = true;
                            }
                        }
                        if (!foundClosingBrace ||
                            nonNumeric ||
                            !processSubstitutionPoints) {

                            // not all numeric or no closing brace or not
                            // processing substitution ponts. Either way
                            // we need to escape the { and continue.
                            buffer.append('\\').append('{');
                        }
                    }
                }
            }
            if (!escaped) {
                if (c == '\'') {
                    // lookahead to see if the next character is a single
                    // quote as well
                    if (i + 1 < message.length() &&
                        message.charAt(i + 1) == '\'') {
                        // The single quote is escaped by another single quote
                        // so print one out.
                        buffer.append(c);
                        // as the next character is a single quote which is
                        // escaping the current single quote we only write one
                        // of them out. Increment the counter in the outer
                        // loop so that we do not process the next character
                        i++;
                    } else {
                        processSubstitutionPoints = !processSubstitutionPoints;
                    }
                } else {
                    // charachter does not need escaping, just append it to the
                    // StringBuffer
                    buffer.append(c);
                }
            }
        }
        if (paramIndex != NO_ARG && paramSubstitutionCount <= paramIndex) {
            // Must throw an exception if the template has less arguments
            // than the paramIndex value
            throw new ParserErrorException(
                    "Template had less arguments than the paramIndex value" + //$NON-NLS-1$
                    paramIndex);
        }
        return buffer.toString();
    }

    // javadoc unnecessary
    public String getKey() {
        return key;
    }

    // javadoc unnecessary
    public boolean isAttribute() {
        return attribute;
    }

    /**
     * This method returns a non-null value if the given message is matched by
     * the parser error's template.
     *
     * <p>This return value is either the empty string (e.g. if the parameter
     * index is {@link #NO_ARG} or the parameter is an empty value) or the
     * string value found of the associated parameter index.</p>
     * @param attributes the Attributes that this error might be associated
     * with. This is ignored by this class. Sub classes may require this
     * information.
     * @param message the message to test for a match
     * @return null if no match was found. An empty string if the message
     * matched and the {@link #paramIndex} is {@link #NO_ARG}. If a match
     * occurs where the parameter is defined, the value obtained using
     * {@link RE#getParen}(1) will be returned
     */
    public String matchedArgument(Attributes attributes, String message) {
        String value = null;
        // see if the message matches this errors template
        boolean matched = template.match(message);
        if (matched) {
            // a match was found
            if (paramIndex == NO_ARG) {
                // no parameter to extract so return an empty string
                value = ""; //$NON-NLS-1$
            } else {
                // extract the parameter that matched.
                value = template.getParen(1);
            }
        }
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 10-Dec-03	2057/7	doug	VBM:2003112803 Addressed several rework issues

 09-Dec-03	2057/5	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
