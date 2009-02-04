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
 * $Header: /src/voyager/com/volantis/charset/EncodingManager.java,v 1.2 2003/04/28 15:24:05 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003033107 - Created to manage Encoding
 *                              classes.
 * 17-Apr-03    Mat             VBM:2003033107 - Made some javadoc changes
 *                              and moved encodingMap initialisation to 
 *                              initialiseManager.
 * 22-May-03    Mat             VBM:2003042907 - Changed to pass the MIBEnum to
 *                              the Encoding constructors.
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset;

import com.volantis.charset.configuration.Alias;
import com.volantis.charset.configuration.Charset;
import com.volantis.charset.configuration.Charsets;
import com.volantis.charset.configuration.xml.CharsetDigesterDriver;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.io.UnsupportedEncodingException;


/**
 * Class to manage a map of Encodings.
 */
public class EncodingManager {

    /**
     * Used for logging
     */
    private static LogDispatcher logger =
            LocalizationFactory.createLogger(EncodingManager.class);
    
    /** 
     * Map to hold the encodings created.
     */
    private HashMap createdEncodingMap;
    
    /**
     * Map to hold the alias to canonical mapping.
     */
    private HashMap charsetNameAliasMap;
    
    /**
     * Map of the preloaded encodings.
     */
    private HashMap preloadedEncodingMap;
    
    /**
     * Map of the charsets from the config file.
     */
    private HashMap charsetMap;
    
    private HashSet unsupportedCharsetNameSet;
    
    /** Creates a new instance of EncodingManager */
    public EncodingManager() {
        initialiseManager();
    }

    /**
     * Parse the charset-config file to get a list of character sets
     * and their aliases.
     * The aliases are stored in a map with a value of the canononical
     * charset name.  The canononical name is the only one which is 
     * used to store encoding information.
     */
    public void initialiseManager() {
        CharsetDigesterDriver dd = new CharsetDigesterDriver();
        Charsets css = dd.digest();
        createdEncodingMap = new HashMap();
        charsetNameAliasMap = new HashMap();
        preloadedEncodingMap = new HashMap();
        unsupportedCharsetNameSet = new HashSet();
        charsetMap = new HashMap();
        
        if(css != null) {
            // Walk through the list of character sets and populate the
            // various maps.
            ArrayList charsets = css.getCharsets();

            Iterator i = charsets.iterator();
            while(i.hasNext()) {
                Charset cs = (Charset) i.next();

                String name = cs.getName();
                this.charsetMap.put(name, cs);
                ArrayList aliases = cs.getAlias();
                if(aliases != null) {
                    Iterator aliasIterator = aliases.iterator();
                    while(aliasIterator.hasNext()) {
                        Alias a = (Alias) aliasIterator.next();
                        charsetNameAliasMap.put(a.getName(), name);
                    }
                }
                // Check to see whether this charset needs preloading.
                if(cs.isPreload()) {
                    try {
                        preloadedEncodingMap.put(name, createEncoding(cs));
                    } catch (UnsupportedEncodingException e) {
                        // This will only happen if we or the user add a 
                        // preloaded charset which is not supported by the 
                        // platform. Currently we only preload UTF-8 so this 
                        // should not happen often.
                        throw new RuntimeException(e);
                    }
                }
                if(logger.isDebugEnabled()) {
                    logger.debug("Loaded '" + cs + "'");
                }
            }
        }
    }
        
    /**
     * Get an Encoding instance for the character set name provided.
     * <p>
     * If the charset is configured within MCS, then Encoding returned will 
     * have the MIBEnum value as defined in the configuration. If not, it will 
     * a MIBEnum value of {@link Encoding#MIBENUM_NOT_CONFIGURED}.   
     * <p>
     * If the charset is not known to the underlying platform, then an 
     * exception is thrown.
     *
     * @param charsetName The name of the charset.
     * @return An encoding for this name, or null if unsupported.
     */
    public Encoding getEncoding(String charsetName) {
        
        Encoding encoding = null;
        
        // Check to see if this encoding is an alias 
        String aliasName = charsetName.toLowerCase();
        String canonicalName = (String) charsetNameAliasMap.get(aliasName);
        if(canonicalName == null) {
            // We found no entry in the alias map.
            if (logger.isDebugEnabled()) {
                logger.debug("No alias found for '" + charsetName + "'");
            }
            // So assume this is the canonical name (or java name) rather than
            // an alias name.
            canonicalName = aliasName;
        } else {
            // We found an entry in the alias map.
            // So the original name is the alias name and the returned value
            // is the canonical name.
            if (logger.isDebugEnabled()) {
                logger.debug("Using charset '" + canonicalName +
                        " for alias '" + charsetName + "'");
            }
        }

        // First we search for an encoding which was created at startup time.
        encoding = searchPreloaded(canonicalName);

        return encoding;
    }

    /**
     * Search for Encodings which are in the config file and were marked for 
     * preloading. These have already been created at startup.
     * 
     * @param charsetName
     * @return An encoding for this name, or null if unsupported.
     */ 
    private Encoding searchPreloaded(String charsetName) {
        // See if the charset we are looking for is registered as preloaded.
        // We treat the preloaded map as immutable so no need for sync here. 
        Encoding encoding = (Encoding) preloadedEncodingMap.get(charsetName);
        if (encoding != null) {
            // We found a preloaded encoding.
            if (logger.isDebugEnabled()) {
                logger.debug("Found preloaded encoding for '" + charsetName + "'");
            }
        } else {
            // Keep looking.
            if (logger.isDebugEnabled()) {
                logger.debug("No preloaded encoding for '" + charsetName + "'");
            }
            encoding = searchCreated(charsetName);
        }
        return encoding;
    }

    /**
     * Search for Encodings which are in the config file but were not marked 
     * for preloading. These will be created on the fly.
     * 
     * @param charsetName
     * @return An encoding for this name, or null if unsupported.
     */ 
    private Encoding searchCreated(String charsetName) {
        Encoding encoding;
        // See if the charset we are looking for is registered as created.
        // But before we do, synchronize on the intern of the encoding 
        // String. This means that only threads for the same encoding will 
        // block.
        synchronized(charsetName.intern()) {
            encoding = (Encoding) createdEncodingMap.get(charsetName);
            if (encoding != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found previously created encoding for '" + 
                            charsetName + "'");
                }
            } else {
                // Keep looking.
                if (logger.isDebugEnabled()) {
                    logger.debug("No previously created encoding for '" + 
                            charsetName + "'");
                }
                encoding = searchSupported(charsetName);
            }
        }
        return encoding;
    }

    /**
     * Search for Encodings which are supported by the platform.
     * @param charsetName
     * @return An encoding for this name, or null if unsupported.
     */ 
    private Encoding searchSupported(String charsetName) {
        if (isSupportedCharset(charsetName)) {
            try {
                return searchCharset(charsetName);
            } catch (UnsupportedEncodingException e) {
                // We don't expect this to happen since we already made sure
                // it is supported, so just throw a RuntimeException.
                throw new RuntimeException(e);
            }
        } else {
            // We return null if the charset is not supported.
            return null;
        }
    }
    
    private Encoding searchCharset(String charsetName) 
            throws UnsupportedEncodingException {
        Encoding encoding;
        // If this charset was in the config file...
        Charset charset = (Charset) charsetMap.get(charsetName);
        if (charset != null) {
            // Then we can create a Encoding from the charset.
            if (logger.isDebugEnabled()) {
                logger.debug("A charset configuration was found for '" + 
                        charsetName + "', creating encoding from " + 
                        "configuration " + charset);
            }
            encoding = createEncoding(charset);
        } else {
            // Else, it wasn't in the config file. 
            if (logger.isDebugEnabled()) {
                logger.debug("No charset configuration was found for '" + 
                        charsetName + "', creating encoding from " + 
                        "Java charset name only (no MIBEnum)");
            }
            // We'd like to just give up at this point, but we can still kind 
            // of keep going. That is, we can create an encoding with just the 
            // Java charset name (and a MIBEnum of "unconfigured").
            // This will work for text output, as this does not require a 
            // MIBenum, but for WMLC output this will cause a error to be 
            // generated at a later point. Note that because Java itself has 
            // alias charset names, this means the charset name we create with 
            // may well be a Java alias name, and that we may create > 1 
            // Encoding for each canonical Java charset. Ugly? You bet!
            encoding = new BitSetEncoding(charsetName,
                    Encoding.MIBENUM_NOT_CONFIGURED);
        }
        createdEncodingMap.put(charsetName, encoding);
        return encoding;
    }

    private boolean isSupportedCharset(String charsetName) {
        boolean supported;
        // First, check that Java supports this charset.
        // Give up straight away if we remember it was bad.
        if (unsupportedCharsetNameSet.contains(charsetName)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Encoding '" + charsetName +
                        "' not supported (cached)");
            }
            supported = false;
        } else {
            // We have never tried to create this one before.
            // So, test the charset name by doing a conversion.
            try {
                "test".getBytes(charsetName);
                // If we made it here, then Java knows this charset name.
                if (logger.isDebugEnabled()) {
                    logger.debug("Encoding '" + charsetName +
                            "' is supported");
                }
                supported = true;
            } catch (UnsupportedEncodingException e) {
                // If we made it here, it's invalid.
                if (logger.isDebugEnabled()) {
                    logger.debug("Encoding '" + charsetName +
                            "' not supported");
                }
                // So add it to the unsupported set and give up.
                unsupportedCharsetNameSet.add(charsetName);
                supported = false;
            } catch (RuntimeException e) {
                // If we made it here, it's invalid.
                // NOTE: this clause was added to work around problem in 
                // IBM JDK 1.4 where it throws nio IllegalCharsetNameException
                // when it shouldn't.
                if (logger.isDebugEnabled()) {
                    logger.debug("Encoding '" + charsetName +
                            "' invalid", e);
                }
                // So add it to the unsupported set and give up.
                unsupportedCharsetNameSet.add(charsetName);
                supported = false;
            }
        }
        return supported;
    }
    
    /**
     * Private helper method to create an Encoding from a Charset 
     * configuration.
     *  
     * @param charset
     * @return the created encoding
     * @throws UnsupportedEncodingException
     */ 
    private Encoding createEncoding(Charset charset) 
            throws UnsupportedEncodingException {
        Encoding encoding;
        if (charset.isComplete()) {
            encoding = new NoEncoding(charset.getName(), charset.getMIBenum());
        } else {
            encoding = new BitSetEncoding(charset.getName(), 
                    charset.getMIBenum());
        }
        return encoding;
    }

    // The following methods are package protected to allow Mariner classes 
    // in this package to access the maps (and also testcases),
    // but to prevent third party classes
    // accessing them.  The protection should not be changed.
    
    /** Getter for property aliasesMap.
    * @return Value of property aliasesMap.
    *
     */
    HashMap getCharsetNameAliasMap() {
      return charsetNameAliasMap;
    }

    /** Getter for property preloadMap.
    * @return Value of property preloadMap.
    *
     */
    HashMap getPreloadedEncodingMap() {
      return preloadedEncodingMap;
    }

    /** Getter for property charsetMap.
    * @return Value of property charsetMap.
    *
     */
    HashMap getCharsetMap() {
      return charsetMap;
    }

    // End of package protected methods.
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 25-Jul-03	860/5	geoff	VBM:2003071405 yet another bug fix merge from metis

 25-Jul-03	860/3	geoff	VBM:2003071405 merge from metis again

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/2	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/4	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
