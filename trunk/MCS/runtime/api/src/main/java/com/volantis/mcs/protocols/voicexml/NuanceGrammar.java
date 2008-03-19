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
 * $Header: /src/voyager/com/volantis/mcs/protocols/voicexml/NuanceGrammar.java,v 1.2 2003/04/30 07:42:01 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 01-Aug-01    Paul            VBM:2001072506 - Added implementation of
 *                              generateMultipleSelectGrammar and renamed
 *                              generateSelectGrammar to
 *                              generateSingleSelectGrammar.
 * 29-Aug-01    Doug            VBM:2001082811 - ensured all "word tokens" are
 *                              lower case and that all return assignments
 *                              are quoted.
 * 04-Sep-01    Paul            VBM:2001081707 - Modified methods to allow
 *                              the grammar to resolve TextComponentNames.
 * 14-Sep-01    Paul            VBM:2001091302 - Made the select option's
 *                              caption default to the option's value if the
 *                              caption is null.
 * 28-Sep-01    Paul            VBM:2001081707 - Fixed minor problem with the
 *                              getting of the grammar from object.
 * 07-Dec-01    Paul            VBM:2001120703 - Provide default false and
 *                              true values if values were not supplied, or
 *                              could not be retrieved.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                                to string.
 * 01-Aug-02    Sumit           VBM:2002073109 - optgroup support added. Moved
 *                              option manupilation into recursive functions and
 *                              changed generate*Select.. to use them
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 07-Apr-03    Byron           VBM:2003032608 - Modifed method signatures as
 *                              per interface change. Reformatted code style
 *                              and updated methods to comply to interface's
 *                              specification. Added copyright and getCaption()
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 *                              Modified calls to protocol.getTextFromReference to
 *                              use context version instead due to protected 
 *                              access. 
 * 30-Apr-03    Adrian          VBM:2003042903 - Updated generateBooleanGrammar
 *                              to use the TextAsset.VOICEXML_NUANCE_GRAMMAR 
 *                              encoding when attempting to retrieve text from 
 *                              an asset 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class generates nuance style grammars.
 *
 * @todo later remove the usage of appendLiteral by using appendText which
 *            eliminates the need to use CDATA (CDATA is used to prevent the
 *            text from being parsed, but if appendText(..) is used the text
 *            will be encoded which circumvents the need for CDATA).
 */
public class NuanceGrammar implements VoiceXMLGrammar {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(NuanceGrammar.class);

    /**
     * The default set of false values to use for boolean grammar.
     */
    private static final String[] DEFAULT_FALSE_VALUES = {"0"};

    /**
     * The default set of true values to use for boolean grammar.
     */
    private static final String[] DEFAULT_TRUE_VALUES = {"1"};


    // javadoc inherited
    public void generateBooleanGrammar(
            DOMOutputBuffer dom,
            String fieldName,
            TextAssetReference falseReference,
            TextAssetReference trueReference) {

        StringBuffer outStr = new StringBuffer();
        String value;

        // The nuance rule name has to start with a capital letter.
        char[] rule = fieldName.toCharArray();
        rule[0] = Character.toUpperCase(rule[0]);

        outStr.append(rule).append(" [");

        // Get the comma separated list of false values and parse it into
        // an array of strings.
        value = getTextFromReference(falseReference, TextEncoding.VOICE_XML_NUANCE_GRAMMAR);

        String[] falseValues;
        if (value == null) {
            falseValues = DEFAULT_FALSE_VALUES;
        } else {
            falseValues = VolantisProtocol.parseCommaSeparatedList(value);
        }

        // Add the false values.
        for (int i = 0; i < falseValues.length; i += 1) {
            value = falseValues[i].trim();
            if (logger.isDebugEnabled()) {
                logger.debug("False value " + i + " = " + value);
            }
            outStr.append(" (").append(value.toLowerCase()).append(") {<");
            outStr.append(fieldName).append(" 0>}");
        }

        // Get the comma separated list of true values and parse it into
        // an array of strings.
        value = getTextFromReference(trueReference, TextEncoding.VOICE_XML_NUANCE_GRAMMAR);

        String[] trueValues;
        if (value == null) {
            trueValues = DEFAULT_TRUE_VALUES;
        } else {
            trueValues = VolantisProtocol.parseCommaSeparatedList(value);
        }

        // Add the true values.
        for (int i = 0; i < trueValues.length; i += 1) {
            value = trueValues[i].trim();
            if (logger.isDebugEnabled()) {
                logger.debug("True value " + i + " = " + value);
            }
            outStr.append(" (").append(value.toLowerCase()).append(") {<");
            outStr.append(fieldName).append(" 1>}");
        }

        outStr.append(" ]");

        dom.appendEncoded(outStr.toString());
    }

    private String getTextFromReference(
            TextAssetReference reference, final TextEncoding encoding) {
        if (reference == null) {
            return null;
        } else {
            return reference.getText(encoding);
        }
    }

    // javadoc inherited
    public void generateSingleSelectGrammar(DOMOutputBuffer dom,
                                            VoiceXMLRoot protocol,
                                            String fieldName,
                                            Collection options) {

        StringBuffer outStr = new StringBuffer();

        // The nuance rule name has to start with a capital letter.
        char[] rule = fieldName.toCharArray();
        rule[0] = Character.toUpperCase(rule[0]);

        outStr.append(rule).append(" [");

        // Add the options.
        addSingleOptions(options, outStr, fieldName);
        outStr.append(" ]").append(" ]]>");

        dom.appendEncoded(outStr.toString());
    }

    /**
     * Added this method as a result of refactoring. The code is common in
     * addSingleOptions and addMultipleOptions
     *
     * @param  option   the SelectOption
     * @return          a trimmed lowercase version of the representing the
     *                  caption.
     */
    private String getCaption(SelectOption option) {

        String value = option.getValue();
        // Get the caption from the object, if it is null then use the
        // value.
        TextAssetReference reference = option.getCaption();
        String caption = getTextFromReference(reference, TextEncoding.PLAIN);
        if (caption == null) {
            caption = value;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Caption = " + caption + " value = " + value);
        }
        return caption.trim().toLowerCase();
    }

    /**
     * This method is used to recursively add single options to the
     * StringBuffer's outStr. It iterates over the collection and appends
     * various string literals to the outStr paramater as necessary.
     *
     * @param options   the collection of options (must be a
     *                  <code>SelectOption</code>)
     * @param outStr    the <code>StringBuffer</code> buffer used to append the
     *                  string literals to.
     * @param fieldName The name of the field for which the grammar is being
     */
    private void addSingleOptions(
            Collection options, StringBuffer outStr,
            String fieldName) {

        for (Iterator i = options.iterator(); i.hasNext();) {
            Object unknown = i.next();
            if (unknown instanceof SelectOption) {
                SelectOption option = (SelectOption) unknown;

                outStr.append(" (").append(getCaption(option))
                        .append(") {<").append(fieldName)
                        .append(" \"").append(option.getValue()).append("\">}");
            } else {
                addSingleOptions(((SelectOptionGroup) unknown).getSelectOptionList(),
                                 outStr, fieldName);
            }
        }
    }

    // javadoc inherited
    public void generateMultipleSelectGrammar(
            DOMOutputBuffer dom,
            String fieldName,
            Collection options) {

        StringBuffer outStr = new StringBuffer();

        // The nuance rule name has to start with a capital letter.
        char[] rule = fieldName.toCharArray();
        rule[0] = Character.toUpperCase(rule[0]);

        outStr.append(rule)
                .append(" (").append(rule).append(1)
                .append(" *(?and ").append(rule).append(1).append(")) ")
                .append(rule).append(1).append(" [");

        // Add the options.
        int o = 0;
        addMultipleOptions(options, outStr, o);
        outStr.append(" ]");

        dom.appendEncoded(outStr.toString());
    }

    /**
     * This method is used to recursively add multiple options to the
     * StringBuffer's outStr. It iterates over the collection and appends
     * various string literals to the outStr paramater as necessary.
     *
     * @param options  the collection of options (must be a
     *                 <code>SelectOption</code>)
     * @param outStr   the <code>StringBuffer</code> buffer used to append the
     *                 string literals to.
     * @param count    The counter used to append to the option literal string
     */
    private void addMultipleOptions(
            Collection options, StringBuffer outStr,
            int count) {

        for (Iterator i = options.iterator(); i.hasNext(); count += 1) {
            Object unknown = i.next();
            if (unknown instanceof SelectOption) {
                SelectOption option = (SelectOption) unknown;

                outStr.append(" (").append(getCaption(option))
                        .append(") {<option").append(count)
                        .append(" true>}");
            } else {
                addMultipleOptions(((SelectOptionGroup) unknown).getSelectOptionList(),
                                   outStr, count);
            }
        }
    }

    // javadoc inherited
    public void generateGrammarFromObject(
            DOMOutputBuffer dom,
            TextAssetReference reference) {

        String result = getTextFromReference(reference, TextEncoding.VOICE_XML_NUANCE_GRAMMAR);
        if (result != null) {
            dom.appendLiteral(result);
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
