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

import javax.ccpp.Attribute;
import javax.ccpp.Component;
import javax.ccpp.Profile;
import javax.ccpp.ProfileFactory;
import javax.ccpp.ProfileFragment;
import javax.ccpp.ProfileFragmentFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Parses the contents of a single CC/PP UAProf Profile URL in RDF/XML format 
 * using JSR-188 CC/PP API and creates a simple Map of the device attributes 
 * found for that definition.
 * <p>
 * This expects that the JSR-188 CC/PP API is all set up and ready to go, i.e.
 * that the various Factories have their singleton instances set. Yeah, I love
 * singletons!
 */ 
public class ProfileParser {

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.ProfileParser");

    private int validationMode;
    
    private final ProfileFactory profileFactory;
    
    private final ProfileFragmentFactory profileFragmentFactory;

    /**
     * Construct an instance of this class.
     */ 
    public ProfileParser(int validationMode)  {
        
        this.validationMode = validationMode;
        
        profileFactory = ProfileFactory.getInstance();
        if (profileFactory == null) {
            throw new IllegalStateException("Profile Factory instance is " +
                    "null; is CC/PP API set up?");
        }
        profileFragmentFactory = ProfileFragmentFactory.getInstance();
        if (profileFragmentFactory == null) {
            throw new IllegalStateException("Profile Fragment Factory " + 
                    "instance is null; is CC/PP API set up?");
        }
        
    }

    public Map processURL(String urlString)
            throws Exception {
        
        URL url = new URL(urlString);

        // Create the profile by parsing a single "fragment".
        // Normally "dynamic" CC/PP processing involves constructing device 
        // definitions dynamically from multiple fragments, but in our case we 
        // expect to get the whole thing "statically" in one go, since we are 
        // not operating in a real time / web environment.
        logger.info("Parsing CC/PP UAProf RDF URL: " + url);
        ProfileFragment fragment =
                profileFragmentFactory.newProfileFragment(url);
        ProfileFragment fragments[] = new ProfileFragment[] {fragment};
        Profile profile = profileFactory.newProfile(fragments, validationMode);
        
        if (profile != null) {
            Map attributeMap = new HashMap();
            logger.info("Parsed Profile using Namespace Vocabulary URI: " +
                    profile.getDescription().getURI());

            // Iterate over the components.
            for (Iterator components = profile.getComponents().iterator();
                 components.hasNext();) {

                Component component = (Component) components.next();
                logger.fine("Found component: " + 
                        component.getDescription().getLocalType() + " (" + 
                        component.getDescription().getURI() + ")");

                // Iterate over the attributes of the component.
                for (Iterator attributes = component.getAttributes().iterator();
                     attributes.hasNext();) {

                    Attribute attribute = (Attribute) attributes.next();
                    logger.fine("Found attribute: " + 
                            attribute.getDescription().getName() + " (" + 
                            attribute.getDescription().getURI() + ") [" +
                            attribute.getClass().getName() + "]" + 
                            " = " + 
                            attribute.getValue());

                    attributeMap.put(attribute.getName(), attribute.getValue());
                }

            }
        
            return attributeMap;
            
        } else {
            
            // Then the profile factory wasn't able to deal with the fragment.
            throw new Exception("No profile created; input invalid?");
            
        }
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05     7240/1  emma    VBM:2005022812 mergevbm from MCS 3.3

 02-Mar-05     7214/1  emma    VBM:2005022812 Fixing leftover localization logging problems

 02-Mar-05	7240/1	emma	VBM:2005022812 mergevbm from MCS 3.3

 02-Mar-05	7214/1	emma	VBM:2005022812 Fixing leftover localization logging problems

 08-Dec-04	6232/3	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 04-Jun-04	4640/5	geoff	VBM:2004060402 UA Prof tool : cannot build uaprof tool

 09-Oct-03	1461/6	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/4	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
