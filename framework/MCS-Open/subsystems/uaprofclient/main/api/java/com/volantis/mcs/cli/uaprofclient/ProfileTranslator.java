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
package com.volantis.mcs.cli.uaprofclient;

import com.volantis.mcs.cli.uaprofclient.translator.AttributeTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.CollectionTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.ColorTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.DimensionTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.DuplicateTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.JavaTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.KeywordTranslator;
import com.volantis.mcs.cli.uaprofclient.translator.SimpleTranslator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Translates an entire profile device attribute map from CC/PP UAProf format 
 * to MCS format.
 * <p>
 * This delegates the detail of the work to individual classes in the 
 * translator package to translate the individual types of attributes.
 */ 
public class ProfileTranslator {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.ProfileTranslator");

    private Map nameTranslationMap;

    private char[] warnOnChars;

    private List customProcessorList = new ArrayList();
    
    public ProfileTranslator(Map nameTranslationMap, char[] warnOnChars) {
        this.nameTranslationMap = nameTranslationMap;
        this.warnOnChars = warnOnChars;

        //
        // Create the processors required to translate the attributes that
        // have "complex", custom rules.
        // 
        
        // ColorCapable
        ColorTranslator color = new ColorTranslator(this);
        customProcessorList.add(color);
        
        // Keyboard
        Map keyboardMap = new HashMap();
        keyboardMap.put("Disambiguating", "virtual");
        keyboardMap.put("Qwerty", "full");
        keyboardMap.put("PhoneKeypad", "numeric");
        KeywordTranslator keyboard = 
                new KeywordTranslator("Keyboard", 
                        "kbdtype", keyboardMap, this);
        customProcessorList.add(keyboard);
        
        // Model
        DuplicateTranslator model = new DuplicateTranslator("Model", 
                "modelnum", "prodname", this);
        customProcessorList.add(model);
        
        // ScreenSize
        DimensionTranslator screenSize = 
                new DimensionTranslator("ScreenSize", 
                        "fullpixels", "x", "y", this);
        customProcessorList.add(screenSize);

        // ScreenSizeChar
        DimensionTranslator screenSizeChar = 
                new DimensionTranslator("ScreenSizeChar", 
                        "characters", "x", "y", this);
        customProcessorList.add(screenSizeChar);
        
        // JavaEnabled / JavaPlatform
        JavaTranslator java = 
                new JavaTranslator(this);
        customProcessorList.add(java);
        
        //  MmsMaxImageResolution
        DimensionTranslator mmsMaxImageResolution = 
                new DimensionTranslator("MmsMaxImageResolution", 
                        "maxmmsimage", "width", "height", this);
        customProcessorList.add(mmsMaxImageResolution);
        
    }

    /**
     * Translates the input device attribute map in CC/PP format into the 
     * output device attribute map in MCS format.
     *   
     * @param inputMap the device attributes of a profile in CC/PP format.
     * @return the device attributes of a profile in output map in MCS format.
     */ 
    public Map translate(Map inputMap) {
        
        // TYPES OF TRANSLATION REQUIRED
        // 
        // Hardcoded:
        // - multiple (custom) to single
        // - single keyword lookup
        // - single Dimension to double int
        // Default:
        // - single Bag to single quoted csv
        // - single Sequence to single quoted csv
        // - single to single copy* (fallback)
        //
        // Thus:
        // - general case is multiple inputs to multiple outputs
        // - storage must be Map input, Map output.
        // - but, we run the hardcoded custom processors first
        // 
        // Processing model can be:
        // - populate map of attributes to be processed.
        // - run hardcoded processors, which will remove CCPP attributes
        //   from the input map and add MCS attributes to the output map.
        // - iterate over the remaining CCPP attribues, processing them 
        //   according to their types, and renaming them according to the name 
        //   properties file.

        Map outputMap = new HashMap();
        
        //
        // First of all, process the "custom" attributes.
        // These may take multiple inputs if they want, and they delete any
        // Attributes they process from the input map. 
        // NOTE: These processors ignore the name translation map.
        //
        
        for (Iterator fixed = customProcessorList.iterator(); 
             fixed.hasNext(); ) {
            
            // NOTE: It would make sense to verify that there are no names 
            // registered against any of the fixed processors.  
            
            AttributeTranslator processor = (AttributeTranslator) fixed.next();
            processor.processAttribute(inputMap, outputMap);
            
        }
        
        //
        // Then, process the remaining "normal" attributes.
        // These may only take a single input since they have their name
        // mapping defined in an external properties file, and are processed
        // one attribute at a time in an iterator.
        //
        
        // Iterate over the Attributes.
        for (Iterator attributes = inputMap.keySet().iterator();
             attributes.hasNext(); ) {

            // Process a single attribute value.
            String inputName = (String) attributes.next();
            Object inputValue = inputMap.get(inputName); 
            AttributeTranslator processor = null;
            
            // Look up the translated name.
            String outputName = (String) nameTranslationMap.get(inputName);
            // If we found a translated name.
            if (outputName != null) {

                // We can process the attribute.

                // Find the processor based on the value type.
                if (inputValue instanceof Collection) {
                    
                    // It needs translation from a collection into a CSV.
                    processor = new CollectionTranslator(attributes, 
                            inputName, inputValue, outputName, this);
                    
                } else {
                    
                    // Just try default translation for the other types.
                    processor = new SimpleTranslator(attributes, 
                            inputName, inputValue, outputName, this);
                    
                }
                
                // OK, we have a processor, so lets process this attribute.
                processor.processAttribute(inputMap, outputMap);
                
            } else {
                
                // This attribute is not present in the name translation
                // file. In this case, we assume that it is not valid in MCS, 
                // so we can't process it.
                logger.warning("Unable to process attribute " + inputName + 
                        " as it has no translated name registered, ignoring");
                
            }
        }
        
        return outputMap;
    }

    public char[] getWarnOnChars() {
        return warnOnChars;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jun-04	4640/4	geoff	VBM:2004060402 UA Prof tool : cannot build uaprof tool

 09-Oct-03	1461/9	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/7	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
