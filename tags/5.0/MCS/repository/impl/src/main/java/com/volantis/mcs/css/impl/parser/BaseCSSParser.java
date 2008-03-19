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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.css.impl.parser.functions.FunctionParser;
import com.volantis.mcs.css.impl.parser.functions.FunctionParserFactory;
import com.volantis.mcs.css.impl.parser.properties.DefaultPropertyParserFactory;
import com.volantis.mcs.css.impl.parser.properties.PropertyParser;
import com.volantis.mcs.css.impl.parser.properties.PropertyParserFactory;
import com.volantis.mcs.css.impl.parser.properties.StyleValueConverter;
import com.volantis.mcs.css.impl.parser.properties.StyleValueIterator;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.DiagnosticListener;
import com.volantis.mcs.css.parser.ExtensionHandler;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.styling.properties.StyleProperty;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

/**
 * The base class for the CSS parser.
 * <p/>
 * <p>The main reason for this is that IDEA does not yet support all its
 * wonderful mechanisms for writing Java code within a JavaCC file so most of
 * the Java code that can be is in here.</p>
 */
public abstract class BaseCSSParser
        implements ParserContext, CSSParser {

    /**
     * The factory to use to create style values.
     */
    protected final StyleValueFactory styleValueFactory =
            StyleValueFactory.getDefaultInstance();

    /**
     * The factory to use to create diagnostics.
     */
    protected final ModelFactory modelFactory =
            ModelFactory.getDefaultInstance();

    /**
     * The factory to use to access property parsers.
     */
    protected final PropertyParserFactory propertyParserFactory;

    /**
     * The factory to use to access function parsers.
     */
    protected final FunctionParserFactory functionParserFactory;

    /**
     * The converter for performing generic conversions, i.e. converting
     * an 'inherit' identifier into a StyleInherit value.
     */
    protected final GenericStyleValueConverter genericStyleValueConverter =
            new GenericStyleValueConverter();

    /**
     * The converter for performing the property specific conversions.
     */
    private final StyleValueConverter converter;

    /**
     * The style sheet that is being built.
     */
    protected StyleSheet styleSheet;

    /**
     * The mutable properties that are currently being constructed.
     * <p/>
     * <p>This is only set while processing a declaration, at all other times
     * any attempt to access this will result in exceptions being thrown.</p>
     */
    protected MutableStyleProperties mutableStyleProperties;

    /**
     * The current property that is being processed.
     * <p/>
     * <p>This is only set while processing a declaration, at all other times
     * any attempt to access this will result in exceptions being thrown.</p>
     */
    protected String currentPropertyName;

    /**
     * The factory to use to create any objects relating to the style sheet.
     */
    protected StyleSheetFactory factory;

    /**
     * The current priority.
     * <p/>
     * <p>This is only set while processing a declaration, at all other times
     * any attempt to access this will result in exceptions being thrown.</p>
     */
    protected Priority currentPriority;

    /**
     * The listener to which diagnostic messages should be sent.
     */
    private DiagnosticListener listener;

    /**
     * The extension handler.
     */
    protected ExtensionHandler extensionHandler;

    /**
     * The source URL.
     */
    protected String sourceURL;

    /**
     * Initialise.
     */
    protected BaseCSSParser() {
        propertyParserFactory = new DefaultPropertyParserFactory(
                "PropertyParsers.properties");
        functionParserFactory = new FunctionParserFactory();
        converter = new StyleValueConverter(this, styleValueFactory);
    }

    /**
     * Must be called immediately after constructing an instance of this
     * class.
     * <p/>
     * <p>This is needed because the generated CSS Parser will only work if the
     * base class has a default constructor.</p>
     *
     * @param factory The factory to use to create the theme model objects.
     */
    protected void initialise(
            StyleSheetFactory factory, DiagnosticListener listener,
            ExtensionHandler extensionHandler) {

        this.factory = factory;
        this.listener = listener;
        this.extensionHandler = extensionHandler;
    }

    // Javadoc inherited.
    public StyleSheet parseStyleSheet(Reader reader, String url) {

        this.sourceURL = url;

        styleSheet = factory.createStyleSheet();

        listener.startParsing();

        doParseStyleSheet(reader);

        listener.endParsing();

        return styleSheet;
    }

    // Javadoc inherited.
    public StyleSheet parseInlineStyleAttribute(String styleAttribute,
                                                String url,
                                                Selector inlineStyleSelector) {
        styleSheet = factory.createStyleSheet();

        listener.startParsing();

        doParseInlineStyleSheet(styleAttribute, inlineStyleSelector);

        listener.endParsing();

        return styleSheet;
    }

    // Javadoc inherited.
    public MutableStyleProperties parseDeclarations(String css) {
        mutableStyleProperties = factory.createStyleProperties();

        listener.startParsing();

        doParseDeclarations(new StringReader(css));

        listener.endParsing();

        return mutableStyleProperties;
    }

    // Javadoc inherited.
    public List parseSelectorGroup(String css) {

        listener.startParsing();

        List selectorGroup = doParseSelectorGroup(new StringReader(css));

        listener.endParsing();

        return selectorGroup;
    }

    // Javadoc inherited.
    public Selector parseSelector(String css) {

        listener.startParsing();

        Selector selector = doParseSelector(new StringReader(css));

        listener.endParsing();

        return selector;
    }

    // Javadoc inherited.
    public StyleValue parseStyleValue(StyleProperty property, String css) {

        listener.startParsing();

        css = property.getName() + ":" + css;

        MutableStyleProperties properties = parseDeclarations(css);
        StyleValue styleValue = properties.getStyleValue(property);

        listener.endParsing();

        return styleValue;
    }

    // Javadoc inherited.
    public PropertyValue parsePropertyValue(StyleProperty property, String css) {

        listener.startParsing();

        css = property.getName() + ":" + css;

        MutableStyleProperties properties = parseDeclarations(css);
        PropertyValue propertyValue = properties.getPropertyValue(property);

        listener.endParsing();

        return propertyValue;
    }

    /**
     * Parse a style sheet.
     *
     * @param reader Contains CSS style sheet.
     */
    protected abstract void doParseStyleSheet(Reader reader);

    /**
     * Parse a sytle attribute
     *
     * @param styleAttribute attribute value
     * @param inlineStyleSelector
     */
    protected abstract void doParseInlineStyleSheet(String styleAttribute, Selector inlineStyleSelector);

    /**
     * Parse a block of declarations.
     *
     * @param reader Contains CSS declarations.
     */
    protected abstract void doParseDeclarations(Reader reader);

    /**
     * Parse a selector group.
     *
     * @param reader Contains CSS selector group.
     * @return The list of {@link Selector}.
     */
    protected abstract List doParseSelectorGroup(Reader reader);

    /**
     * Parse a style sheet.
     *
     * @param reader Contains CSS style sheet.
     */
    protected abstract Selector doParseSelector(Reader reader);

    /**
     * Add a new rule to the style sheet.
     *
     * @param selectors  The selector group.
     * @param properties The style properties.
     */
    protected void addRule(List selectors, MutableStyleProperties properties) {
        Rule rule = ThemeFactory.getDefaultInstance().createRule();
        rule.setSelectors(selectors);
        rule.setProperties(properties);
        styleSheet.getRules().add(rule);
    }

    protected MutableStyleProperties createMutableStyleProperties() {
        return ThemeFactory.getDefaultInstance().createMutableStyleProperties();
    }

    /**
     * Create a pseudo class selector for the identifier.
     *
     * @param identifier The pseudo class identifier.
     * @return The pseudo class selector.
     */
    protected PseudoClassSelector createPseudoClassSelector(String identifier) {

        PseudoClassSelector selector =
                factory.createPseudoClassSelector(identifier);

        if (selector.getPseudoClassType() == PseudoClassTypeEnum.INVALID) {
            addDiagnostic(CSSParserMessages.UNKNOWN_PSEUDO_CLASS,
                    new Object[]{identifier});
        }

        return selector;
    }

    /**
     * Create a pseudo element selector for the identifier.
     *
     * @param identifier The pseudo element identifier.
     * @return The pseudo element selector.
     */
    protected PseudoElementSelector createPseudoElementSelector(
            String identifier) {

        PseudoElementSelector selector =
                factory.createPseudoElementSelector(identifier);

        if (selector.getPseudoElementType() == PseudoElementTypeEnum.INVALID) {
            addDiagnostic(CSSParserMessages.UNKNOWN_PSEUDO_ELEMENT,
                          new Object[]{
                              identifier
                          });
        }

        return selector;
    }

    /**
     * Create a combined selector.
     * <p/>
     * <p>If the parent is not null then the newly created combined selector
     * is set as the subject of the parent.</p>
     *
     * @param parent     The parent combined selector, may be null.
     * @param combinator The combinator for the combined selector.
     * @return The new combined selector.
     */
    protected CombinedSelector createCombinedSelector(
            CombinedSelector parent, CombinatorEnum combinator) {

        CombinedSelector combined = factory.createCombinedSelector();
        combined.setCombinator(combinator);

        if (parent != null) {
            parent.setSubject(combined);
        }

        return combined;
    }

    /**
     * Create a combined selector.
     * <p/>
     * <p>If the parent is not null then the newly created combined selector
     * is set as the subject of the parent.</p>
     *
     * @param parent     The parent combined selector, may be null.
     * @param combinator The combinator for the combined selector.
     * @param sequence   The contextual selector sequence.
     * @return The new combined selector.
     */
    protected CombinedSelector createCombinedSelector(
            CombinedSelector parent, CombinatorEnum combinator,
            SelectorSequence sequence) {

        CombinedSelector combined = createCombinedSelector(parent, combinator);
        combined.setContext(sequence);

        return combined;
    }

    /**
     * Method use by the token manager to convert a string using CSS syntax
     * to represent an identifier, possibly containing escaped characters into
     * a Java string representing the identifier.
     *
     * @param cssIdentifier The CSS syntax identifier.
     * @return The Java string.
     */
    static String cssIdentifierToJavaString(String cssIdentifier) {
        return convertStringRange(cssIdentifier, 0, cssIdentifier.length());
    }

    /**
     * Strip the quotes off from around the string and replace escaped
     * characters with the actual Unicode characters.
     *
     * @param cssString The string.
     * @return The Java string.
     */
    static String cssStringToJavaString(String cssString) {
        String s = cssString;

        if (!(s.startsWith("\"") && s.endsWith("\""))
                && !(s.startsWith("\'") && s.endsWith("\'"))) {
            // This should never happen.
            throw new TokenMgrError();
        }

        return convertStringRange(s, 1, s.length() - 1);
    }

    /**
     * Convert the range of the string from CSS syntax into actual Java
     * characters.
     *
     * @param s      The string to convert.
     * @param offset The offset of the start of the range.
     * @param length The length of the range.
     * @return The converted string.
     */
    static String convertStringRange(String s, int offset, int length) {

        StringBuffer buffer = new StringBuffer(length);

        // Iterate over the list of characters.
        int index = offset;
        while (index < length) {
            char c = s.charAt(index);
            if (c == '\\') {
                if (++index < length) {
                    c = s.charAt(index);
                    switch (c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            int numValue = Character.digit(c, 16);
                            int count = 0;

                            while (index + 1 < length && count < 6) {
                                c = s.charAt(index + 1);

                                if (Character.digit(c, 16) != -1) {
                                    numValue = (numValue * 16) + Character.digit(
                                            c, 16);
                                    index++;
                                } else {
                                    if (c == ' ') {
                                        // skip the latest white space
                                        index++;
                                    }
                                    break;
                                }
                            }

                            buffer.append((char) numValue);
                            break;
                        case '\n':
                        case '\f':
                            break;
                        case '\r':
                            if (index + 1 < length) {
                                if (s.charAt(index + 1) == '\n') {
                                    index++;
                                }
                            }
                            break;
                        default:
                            buffer.append(c);
                    }
                } else {
                    // Escaped character terminated prematurely.

                }
            } else {
                buffer.append(c);
            }
            index++;
        }

        return buffer.toString();
    }

    /**
     * Convert a URL in CSS escaped syntax into a Java string.
     *
     * @param cssURL The CSS URL.
     * @return The Java string.
     */
    protected String cssURLToJavaString(String cssURL) {
        return convertStringRange(cssURL, 0, cssURL.length());
    }

    /**
     * Process a function like call and convert it into a style value.
     *
     * @param location  The source location of the object, may be null.
     * @param name      The name of the function.
     * @param arguments The arguments (as StyleValue)s.
     * @return The StyleValue, which may or may not be a StyleFunction.
     */
    protected StyleValue processFunction(
            SourceLocation location, String name, List arguments) {
        FunctionParser parser = functionParserFactory.getFunctionParser(name);
        if (parser == null) {
            addDiagnostic(CSSParserMessages.UNKNOWN_FUNCTION, new Object[]{
                name
            });
        } else {
            // Catch and consume any validation exceptions occuring in the
            // function parsers.
            try {
                return parser.parse(this, location, name, arguments);
            } catch (ParserValidationException e) {
                // Return null, the thrower is responsible for adding a
                // diagnostic.
                return null;
            }
        }

        return null;
    }

    /**
     * Parse the property.
     *
     * <p>This finds and uses the appropriate property parser if it can find
     * one.</p>
     *
     * @param propertyName The name of the property.
     * @param values The values (as StyleValue)s.
     * @param priority The priority.
     */
    protected void parseProperty(
            PropertyParser parser,
            String propertyName, final List values,
            Priority priority) {

        if (parser == null) {
            throw new IllegalArgumentException("parser cannot be null");
        }

        currentPropertyName = propertyName;
        currentPriority = priority;
        try {
            for (int i = 0; i < values.size(); i++) {
                Object object = values.get(i);
                if (object instanceof StyleValue) {
                    StyleValue value = (StyleValue) values.get(i);

                    values.set(i, genericStyleValueConverter.convert(value));
                }
            }

            StyleValueIterator iterator = new StyleValueIterator() {

                private int index = 0;

                public void separator(Separator separator) {
                    Object object = values.get(index);
                    if (object == separator) {
                        index += 1;
                    } else {
                        addDiagnostic(CSSParserMessages.EXPECTED_SEPARATOR,
                                new Object[]{
                                    getCurrentPropertyName(),
                                    separator,
                                    object
                                });
                    }
                }

                public boolean isSeparator(Separator separator) {
                    return (index < values.size() &&
                            values.get(index) == separator);
                }

                public StyleValue value() {
                    Object object = values.get(index);
                    if (object instanceof Separator) {
                        addDiagnostic(CSSParserMessages.UNEXPECTED_SEPARATOR,
                                new Object[]{
                                    getCurrentPropertyName(),
                                    object
                                });
                        return null;
                    } else {
                        return (StyleValue) object;
                    }
                }

                public void consume() {
                    index += 1;
                }

                public int remaining() {
                    return values.size() - index;
                }

                public boolean hasMore() {
                    return remaining() > 0;
                }
            };

            parser.parse(this, iterator);
        } finally {
            currentPropertyName = null;
            currentPriority = null;
        }
    }

    /**
     * Process a dimension.
     *
     * @param numberString A string representing the number.
     * @param unitString A string representing the unit.
     * @return The appropriate style value.
     */
    protected StyleValue processDimension(
            SourceLocation location,
            String numberString, String unitString) {
        double number = Double.parseDouble(numberString);

        // Units are not case sensitive.
        unitString = unitString.toLowerCase();
        Object unit;
        if ((unit = AngleUnit.getUnitByName(unitString)) != null) {
            return styleValueFactory.getAngle(
                    location, number, (AngleUnit) unit);
        } else if ((unit = LengthUnit.getUnitByName(unitString)) != null) {
            return styleValueFactory.getLength(
                    location, number, (LengthUnit) unit);
        } else if ((unit = TimeUnit.getUnitByName(unitString)) != null) {
            return styleValueFactory.getTime(
                    location, number, (TimeUnit) unit);
        } else if ((unit = FrequencyUnit.getUnitByName(unitString)) != null) {
            return styleValueFactory.getFrequency(
                    location, number, (FrequencyUnit) unit);
        } else {
            addDiagnostic(location, CSSParserMessages.UNKNOWN_UNIT,
                          new Object[]{
                              unitString
                          });

            return styleValueFactory.getNumber(location, number);
        }
    }

    // Javadoc inherited.
    public MutableStyleProperties getStyleProperties() {
        if (mutableStyleProperties == null) {
            throw new IllegalStateException("Not inside declarations block");
        }

        return mutableStyleProperties;
    }

    // Javadoc inherited.
    public String getCurrentPropertyName() {
        if (currentPropertyName == null) {
            throw new IllegalStateException("Not inside declarations block");
        }
        return currentPropertyName;
    }

    // Javadoc inherited.
    public Priority getCurrentPriority() {
        if (currentPriority == null) {
            throw new IllegalStateException("Not inside declarations block");
        }
        return currentPriority;
    }

    // Javadoc inherited.
    public void addDiagnostic(String key, Object[] args) {
        I18NMessage i18nMessage = modelFactory.createMessage(key, args);
        Diagnostic diagnostic = modelFactory.createDiagnostic(
                null, DiagnosticLevel.ERROR, i18nMessage);
        listener.message(diagnostic);
    }

    public void addDiagnostic(SourceLocation location, String key, Object arg) {
        I18NMessage i18nMessage = modelFactory.createMessage(key, arg);
        Diagnostic diagnostic = modelFactory.createDiagnostic(
                location, DiagnosticLevel.ERROR, i18nMessage);
        listener.message(diagnostic);
    }

    public void addDiagnostic(SourceLocation location, String key, Object [] args) {
        I18NMessage i18nMessage = modelFactory.createMessage(key, args);
        Diagnostic diagnostic = modelFactory.createDiagnostic(
                location, DiagnosticLevel.ERROR, i18nMessage);
        listener.message(diagnostic);
    }

    // Javadoc inherited.
    public StyleValue convert(
            Set supportedTypes, AllowableKeywords allowableKeywords, StyleValue value) {

        return converter.convert(supportedTypes, allowableKeywords, value);
    }

    // Javadoc inherited.
    public StyleValue convertAndConsume(
            Set supportedTypes, AllowableKeywords allowableKeywords,
            StyleValueIterator iterator) {

        StyleValue value = iterator.value();
        // Only attempt to convert the value if it is non null (i.e. values
        // that could not be successfully parsed should be ignored).
        if (value != null) {
            value = converter.convert(supportedTypes, allowableKeywords, value);
        }

        if (value != null) {
            iterator.consume();
        }
        return value;
    }

    public void setPropertyValue(StyleProperty property, StyleValue value) {
        PropertyValue propertyValue =
            ThemeFactory.getDefaultInstance().createPropertyValue(
                property, value, getCurrentPriority());
        getStyleProperties().setPropertyValue(propertyValue);
    }

    /**
     * Process a selector to add an InlineStyleSelector.
     *
     * If the selector is not null then the inlineStyleSelector will be added
     * to the selector sequence within the selector, otherwise the inline
     * style selector is added to the selector group. The reason the inline
     * selector is being added is to mark the selector as one that has been
     * created from a style attribute value and can then be matched using the
     * InlineStyleMatcher.
     *
     * @param selector - The main selector to process
     * @param inlineStyleSelector - The InlineStyleSelector to add
     * @param selectorGroup - The selector group containing the selectors.
     */
    protected void processInlineSelector(Selector selector,
                                         Selector inlineStyleSelector,
                                         List selectorGroup) {
        if (selector != null) {
            List nestedSelectors =
                    ((SelectorSequence) selector).getSelectors();
            nestedSelectors.add(inlineStyleSelector);
            selectorGroup.add(selector);
        } else {
            SelectorSequence sequence = factory.createSelectorSequence();
            sequence.addSelector(inlineStyleSelector);
            selectorGroup.add(sequence);
        }
    }

    /**
     * Process the token into a priority value.
     *
     * @param token The token to process.
     * @return The priority, or null if it was not value.
     */
    public Priority processPriority(String token) {
        Priority priority;
        if (token.equals("important")) {
            priority = Priority.IMPORTANT;
        } else {
            // Pass through to the extension handler.
            priority = extensionHandler.customPriority(token);
        }

        return priority;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/2	schaloner	VBM:2005070711 Added marker pseudo-element support

 05-Oct-05	9440/2	schaloner	VBM:2005070711 Added marker pseudo-element support

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
