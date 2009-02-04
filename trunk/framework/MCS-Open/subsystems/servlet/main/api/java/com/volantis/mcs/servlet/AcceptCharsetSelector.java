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
package com.volantis.mcs.servlet;

import com.volantis.charset.EncodingManager;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.Enumeration;

/**
 * A utility class to select a valid charset from the Accept-Charset header of 
 * a servlet request.
 * <p>
 * This will iterate through all the charsets provided in order of qvalue,
 * and select the first one that is valid according to the encoding manager 
 * and acceptable according to the user.
 * <p>
 * If no accept-charset header was provided, or if one was provided but none
 * of the specified charsets was valid and acceptable, it will fall back to 
 * the device and then system default values.
 * <p>
 * The logic here is guided/mandated by RFC 2616 Sections 3.9 and 14.2. 
 */ 
public class AcceptCharsetSelector {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AcceptCharsetSelector.class);

    /**
     * The name of the HTTP header that stores accept charsets.
     */ 
    private static final String ACCEPT_CHARSET = "Accept-Charset";

    /**
     * Hardcoded system default charset. This is only used if we cannot find
     * a default for the particular device in question. This is currently
     * UTF-8 for backwards compatibility reasons. According to the RFC, we
     * should really be using ISO-8859-1 here.
     */ 
    private static final String SYSTEM_DEFAULT_CHARSET = "UTF-8";

    /**
     * The encoding manager we use for validating charsets are valid according
     * to the JVM.
     */ 
    private EncodingManager encodingMgr;
    
    /**
     * Construct an instance of this class, with the encoding manager provided.
     * 
     * @param encodingManager
     */ 
    public AcceptCharsetSelector(EncodingManager encodingManager) {
        this.encodingMgr = encodingManager;
    }

    /**
     * Select a valid charset from the Accept-Charset header in the servlet 
     * request provided, if any. Will return null if a valid charset could
     * not be found.
     * 
     * @param requestHeaders used to extract accept-charset headers from.
     * @param device used to look up device default if necessary.
     * @return the charset selected, or null if none was found.  
     */ 
    public String selectCharset(HttpHeaders requestHeaders,
            DefaultDevice device) {
        String charset = null;
        if(device==null) {
            throw new IllegalArgumentException("device must not be null");
        }

        // First check to see if the device has the charset forced attribute
        // set. If it does then use that value, otherwise determine which
        // charset to use as before.
        final String charSetForced = device.getComputedPolicyValue(
                DevicePolicyConstants.FORCED_OUTPUT_CHARSET);
        if (charSetForced != null && charSetForced.length() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Forcing usage of charset '" + charSetForced + "'");
            }
            return charSetForced;
        }

        // Extract the accept charset headers.
        Enumeration headers = requestHeaders.getHeaders(ACCEPT_CHARSET);
        if (!headers.hasMoreElements()) {
            // They didn't specify any accept-charset headers
            // In this case the spec says that any charset is acceptable.
            // So we try our device defaults for lack of a better idea.
            if (logger.isDebugEnabled()) {
                logger.debug("Found no accept charset headers" + 
                        ", trying device default");
            }
            
            // So, try our device defaults as the best guess.
            // If this returns null we give up.
            DefaultCharsetFinder defaultFinder = new DefaultCharsetFinder(
                    encodingMgr);
            charset = defaultFinder.findDefaultCharset(device);
        } else {
            // They specified at least one accept-charset header.
            // So parse them to see if we can find something we agree on.



            // Add all the accept-charset header lines into our parser.
            AcceptParser parser = AcceptParserFactory.createCharsetParser();
            while (headers.hasMoreElements()) {
                String header = (String) headers.nextElement();
                parser.addHeaderField(header);
            }
            AcceptParser.Header header = parser.build();
            // Search the parser for a charset we recognise (including the 
            // implicit default of ISO-8859-1).
            AcceptableCharsetSearcher searcher = new AcceptableCharsetSearcher(
                    encodingMgr, parser, device);
            header.forEachAcceptable(searcher);
            charset = searcher.getCharsetName();
            // The only way this can fail is if they do not provide * and also 
            // explicitly disable ISO-8559-1 (since we add it by default 
            // otherwise), or if the platform doesn't support ISO-8859-1.
            // In this case we cannot correctly try the device defaults as 
            // they have been implicitly set to a qvalue of 0 according to 
            // the spec. Maybe in future we may wish to *incorrectly* try
            // the device defaults if we find a device which requires this.
        } 

        // If we have managed to find a valid, acceptable charset.
        if (charset != null) {
            // Use the charset we have found.
            if (logger.isDebugEnabled()) {
                logger.debug("Using charset '" + charset + "'");
            }
            return charset;
        } else {
            // We couldn't find an acceptable charset, so we give up.
            // Extract the headers as a string.
            StringBuffer buffer = new StringBuffer();
            headers = requestHeaders.getHeaders(ACCEPT_CHARSET);
            if (headers.hasMoreElements()) {
                while (headers.hasMoreElements()) {
                    String header = (String) headers.nextElement();
                    buffer.append("\"");
                    buffer.append(header);
                    buffer.append("\"");
                    if (headers.hasMoreElements()) {
                        buffer.append(",");
                    }
                }
            } else {
                buffer.append("[none]");
            }
            // Log the problem,
            logger.error("missing-charset-header", new Object[]{ACCEPT_CHARSET, buffer});
            return null;
        }
    }

    /**
     * A utility class to find valid default charsets for us.
     * <p>
     * This tries looking for a device specific value first, then falls
     * back to a hardcoded "system" default value if that is not valid.
     * <p>
     * It will reject charsets which are not known to the JVM, or have been 
     * mentioned by the user in the accept charset headers.
     */ 
    private static class DefaultCharsetFinder {

        /** The default charset validator we are using. */
        private CharsetValidator defaultValidator;

        public DefaultCharsetFinder(EncodingManager encodingManager) {
            defaultValidator = new DefaultCharsetValidator(encodingManager);
        }

        /**
         * Sets the parser to use for validating default charsets.
         * We use this in such a way that all mentioned charsets are rejected.
         * 
         * @param parser
         */ 
        void setParser(AcceptParser parser) {
            defaultValidator.setParser(parser);
        }
        
        /**
         * Try and find a default charset, according to the rules in the 
         * class comment. Will return null if no valid default was found.
         * 
         * @param device used to look up device policy entry.
         * @return a default charset, or null if one could not be found.
         */ 
        private String findDefaultCharset(DefaultDevice device) {
            String charset = null;
            if (logger.isDebugEnabled()) {
                logger.debug("Using device/system default charsets");
            }
            String defaultCharset = device.getComputedPolicyValue(
                    DevicePolicyConstants.DEFAULT_OUTPUT_CHARSET);
            // Ensure the default we selected was acceptable to client.
            if (defaultValidator.validate(defaultCharset, "Device default")) {
                // Use the device default charset.
                charset = defaultCharset;
                if (logger.isDebugEnabled()) {
                    logger.debug("Using device default charset '" + charset +
                            "'");
                }
            } else {
                // Device default not acceptable, try system default
                defaultCharset = SYSTEM_DEFAULT_CHARSET;
                if (defaultValidator.validate(defaultCharset, 
                        "System default")) {
                    // use the system default charset.
                    charset = defaultCharset;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Using system default charset '" +
                                charset + "'");
                    }
                } else {
                    // Not acceptable!
                    if (logger.isDebugEnabled()) {
                        logger.debug("No valid system default charset found");
                    }
                }
            }
            return charset;
        }
    }

    /**
     * An implementation of {@link AcceptParser}'s internal iterator
     * which looks through the parser's charsets, searching for either one 
     * which is valid according to the charset validator it is using.
     * <p>
     * If it finds the special value "*" or runs out of things to try, it will
     * fall back to device and then system defaults. 
     */ 
    private static class AcceptableCharsetSearcher 
            implements AcceptParser.InternalAcceptableIterator {

        /** The validator we are using. */
        private CharsetValidator validator;
        
        /** The class to find defaults if we need to. */
        private DefaultCharsetFinder defaultFinder;
        
        /** Used to look up device defaults. */
        private DefaultDevice device;
        
        /** The resulting charset name we have found, if any. */
        private String charsetName;

        /** True if we have already searched for a default value. */
        private boolean searchedDefaults;

        /**
         * Construct an instance of this class.
         * 
         * @param encodingMgr the encoding manager to use for validation.
         * @param parser the parser to use for validation.
         * @param device to look up device fallback charset name.
         */ 
        public AcceptableCharsetSearcher(EncodingManager encodingMgr,
                AcceptParser parser, DefaultDevice device) {
            validator = new CharsetValidator(encodingMgr);
            validator.setParser(parser);
            defaultFinder = new DefaultCharsetFinder(encodingMgr);
            defaultFinder.setParser(parser);
            this.device = device;
        }

        // Javadoc inherited.
        public void before() {
            // nothing to do before.
        }

        // Javadoc inherited.
        public void next(String value) {
            // If we haven't already found a charset name...
            if (charsetName == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found accept charset '" + value + "'");
                }
                // If it's not the "*" special value.
                if (!value.equals("*")) {
                    // If we support it, use it.
                    if (validator.validate(value, "Accept")) {
                        charsetName = value;
                    }
                    // Else, keep looking.
                } else {
                    // It was the * special value.
                    // This sets all non-mentioned charsets to a implicit 
                    // qvalue of 1, which allows us to try our device defaults.
                    
                    // So search for a default charset. 
                    // If it returns null, we ignore the * and keep searching.
                    charsetName = defaultFinder.findDefaultCharset(device);
                    searchedDefaults = true;
                }
            }
            // Else, we have already found one.
        }

        // Javadoc inherited.
        public void after() {
            // If we didn't find a charset and we haven't already tried to 
            // search a default charset, then search for a default charset.
            // NOTE: this goes against the spirit of the specification which 
            // says that we SHOULD send a 406 (Not Acceptable) if we can't
            // find a matching charset for this case (no *).
            if (charsetName == null && !searchedDefaults) {
                charsetName = defaultFinder.findDefaultCharset(device);
            }
            // If this fails we will return null, which will end up generating
            // a 406 as per the spec.
        }

        /**
         * Return the charset name found. This can be either a valid charset
         * name, or null if neither of those was found.
         * @return character set name
         */ 
        public String getCharsetName() {
            return charsetName;
        }
    }

    /**
     * A utility class to validate charsets. 
     * <p>
     * For a charset to be valid, it must be non-null and valid according to 
     * the encoding manager, i.e. be known by the JVM. 
     * <p>
     * If this class has been provided an accept charset parser, then charsets
     * will be rejected if they are listed as "not acceptable" in the parser.
     */ 
    static class CharsetValidator {
        
        /** Used to validate charsets. */
        private EncodingManager encodingMgr;
        
        /** Used to check against charsets the user provided, optional. */
        AcceptParser parser;
        
        /**
         * Construct an instance of this class with the encoding manager 
         * provided.
         * 
         * @param encodingMgr
         */ 
        CharsetValidator(EncodingManager encodingMgr) {
            this.encodingMgr = encodingMgr;
        }

        /**
         * Set the accept charset parser to use for validating charsets.
         * 
         * @param parser used to ensure charsets are acceptable.
         */ 
        void setParser(AcceptParser parser) {
            this.parser = parser;
        }

        /**
         * Validate the charset, returning <tt>true</tt> if it was valid.
         * 
         * @param charset the name of the charset to validate.
         * @param type the type of charset we are validating, used for 
         *      debug logging.
         * @return true if if was valid, false otherwise.
         */ 
        boolean validate(String charset, String type) {
            String reason = null;
            boolean valid = false;
            
            if (charset == null) {
                reason = "null";
            } else if (encodingMgr.getEncoding(charset) == null) {
                reason = "not valid";
            } else if (parser != null) {
                reason = validateWithParser(charset, reason);
            }
            
            if (reason == null) {
                valid = true;
                reason = "valid";
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug(type + " charset '" + charset + "' is " + reason);
            }
            return valid;
        }

        String validateWithParser(String charset, String reason) {
            if (parser.containsNotAcceptable(charset)) {
                reason = "not acceptable";
            }
            return reason;
        }
    }

    /**
     * A {@link com.volantis.mcs.servlet.AcceptCharsetSelector.CharsetValidator} 
     * for use validating default charsets.
     * <p>
     * This additionally rejects charsets if it has a parser, and they are 
     * listed as acceptable in the parser (that is, mentioned at all).
     * <p> 
     * This is because when we are evaluting * we must only use charsets
     * which have not been mentioned by the user.  
     */ 
    static class DefaultCharsetValidator extends CharsetValidator {
        DefaultCharsetValidator(EncodingManager encodingMgr) {
            super(encodingMgr);
        }
        
        String validateWithParser(String charset, String reason) {
            reason = super.validateWithParser(charset, reason);
            if (reason == null && parser.containsAcceptable(charset)) {
                reason = "already present";
            }
            return reason;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	8675/1	trynne	VBM:2005052602 Generalised AcceptCharsetParser to AcceptParser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 03-Aug-04	5017/3	matthew	VBM:2004073003 fix merge problems

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 23-Mar-04	3362/2	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 10-Mar-04	3374/1	byron	VBM:2004030807 Specify which Character Set to use if multiple available

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 25-Jul-03	860/2	geoff	VBM:2003071405 merge from metis again

 25-Jul-03	858/2	geoff	VBM:2003071405 detect invalid charset properly

 24-Jul-03	807/6	geoff	VBM:2003071405 use fallbacks more often and allow user to set it themselves if we can't

 24-Jul-03	807/4	geoff	VBM:2003071405 now with fixed architecture

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
