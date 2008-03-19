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
 * $Header: /src/voyager/com/volantis/mcs/runtime/repository/remote/xml/RepositoryEntityResolver.java,v 1.2 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * April-02     Steve           VBM:2002041706 - Remote Repository XML parser.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.config;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.schema.W3CSchemata;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A resolver for DTD and XSD files for the remote repository. 
 * 
 * <p>If a DTD is being resolved then the public ID will be non null and can
 * be processed against the constants in {@link RepositoryEntityResolver}
 * if the ID resolves to a known ID then the dtd or entity file will be opened
 * and a stream returned to read it.</p>
 * <p> If an XSD is being resolved, the public ID will be null and the system ID
 * will be the URL of the XSD. This can be intercepted to a known XSD which
 * can then be opened and a stream returned to read it.</p>
 * <p>If the entity cannot be resolved, the resolver returns null and the SAX
 * parser will have to find another way of getting the DTD or XSD.</p>
 * 
 * @author steve
 */
public class RepositoryEntityResolver
    implements EntityResolver {

    /**
     * Any DOCTYPE that has a public ID with this prefix will be resolved to a
     * version of marlin-rpdm.dtd
     */
    private static final String RPDM_DTD_PUBLIC_PREFIX =
        "-//VOLANTIS/DTD MARLIN-RPDM";

    /** The mariner path where dtd files are held. */
    private static final String RPDM_DTD_PATH = "com/volantis/mcs/dtd/";

    /** The name of the rpdm dtd file */
    private static final String RPDM_DTD_FILE = "marlin-rpdm.dtd";

    /**
     * Any DOCTYPE that has a public ID with this prefix will be resolved to a
     * version of marlin-cdm.dtd
     */
    private static final String CDM_DTD_PUBLIC =
        "-//VOLANTIS//DTD MARLIN-CDM//EN";

    /** The mariner path where dtd files are held. */
    private static final String CDM_DTD_PATH =
        "com/volantis/mcs/dtd/marlin-cdm.dtd";

    /**
     * Any DOCTYPE that has this public ID will be resolved to a
     * version of xhtml-lat1.ent
     */
    private static final String XHTML_LATIN_ENTITY =
            W3CSchemata.XHTML_LAT1_ENT_PUBLIC_ID;

    /**
     * The mariner path to the latin entity file
     */
    private static final String XHTML_LATIN_PATH =
        "com/volantis/mcs/dtd/xhtml-lat1.ent";

    /**
     * Any DOCTYPE that has this public ID will be resolved to a
     * version of xhtml-special.ent
     */
    private static final String XHTML_SPECIAL_ENTITY =
            W3CSchemata.XHTML_SPECIAL_ENT_PUBLIC_ID;

    /**
     * The mariner path to the special entity file
     */
    private static final String XHTML_SPECIAL_PATH =
        "com/volantis/mcs/dtd/xhtml-special.ent";

    /**
     * Any DOCTYPE that has this public ID will be resolved to a
     * version of xhtml-symbol.ent
     */
    private static final String XHTML_SYMBOL_ENTITY =
        W3CSchemata.XHTML_SYMBOL_ENT_PUBLIC_ID;

    /**
     * The mariner path to the special entity file
     */
    private static final String XHTML_SYMBOL_PATH =
        "com/volantis/mcs/dtd/xhtml-symbol.ent";

    // TODO: refactor these into PolicySchemas

    /** The URL prefix of the rpdm xsd */
    private static final String RPDM_XSD_URL = "http://www.volantis.com/xsd/";

    /** The mariner path where xsd files are held */
    private static final String RPDM_XSD_PATH = "com/volantis/schema/";

    /** The name of the rpdm xsd schema */
    private static final String RPDM_XSD_FILE = "marlin-rpdm.xsd";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(RepositoryEntityResolver.class);
    private Map entityMap;
    private String version = null;

    public RepositoryEntityResolver() {

        entityMap = new HashMap();

        /* Map the entity files and marlin-cdm.dtd */
        entityMap.put(XHTML_LATIN_ENTITY, XHTML_LATIN_PATH);
        entityMap.put(XHTML_SPECIAL_ENTITY, XHTML_SPECIAL_PATH);
        entityMap.put(XHTML_SYMBOL_ENTITY, XHTML_SYMBOL_PATH);
        entityMap.put(CDM_DTD_PUBLIC, CDM_DTD_PATH);
    }

    /**
     * Given a public and/or system ID resolve these into a dtd, entity file
     * or XSD and return it as an InputSource. 
     *
     * @param publicID The public ID read from a DTD DOCTYPE or null if an XSD
     * @param systemID The URL of the DTD or XSD in question
     *
     * @return an InputSource to the resolved DTD or XSD or <code>null</code>
     * if the entity cannot be resolved.
     */
    public InputSource resolveEntity(String publicID, String systemID) {
        String filePath;
        URL url = null;

        // Get the class loader.
        ClassLoader loader = getClass().getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        if (publicID != null) {
            // Check if we recognise this ID
            if (publicID.startsWith(RPDM_DTD_PUBLIC_PREFIX)) {
                // this is a volantis header -- Get the version number
                int end = publicID.lastIndexOf("//");
                if (end < 0) {
                    end = publicID.length();
                }

                int start = RPDM_DTD_PUBLIC_PREFIX.length();

                // Get the version, trim the spaces and replace any '.' 
                // characters with '-' characters
                version =
                    publicID.substring(start, end).trim().replace('.', '-');

                // If there was no version number, default to 'latest'
                if (version.length() == 0) {
                    version = "latest";
                }

                filePath = RPDM_DTD_PATH + version + "/" + RPDM_DTD_FILE;
                if (logger.isDebugEnabled()) {
                    logger.debug("Resolved dtd " + filePath);
                }

                url = loader.getResource(filePath);
            } else {
                // Get the default path for the entity
                String entityPath = (String) entityMap.get(publicID);

                // If we didnt recognise the public ID of the entity... 
                // give up now
                if (entityPath != null) {
                    // First we try the entity file in the same directory as 
                    // the dtd
                    if (version != null) {
                        int lastSlash = entityPath.lastIndexOf('/');
                        if (lastSlash > 0) {
                            filePath =
                                RPDM_DTD_PATH + version +
                                entityPath.substring(lastSlash);

                            // Try to open the file
                            if (logger.isDebugEnabled()) {
                                logger.debug("Resolved entity " + filePath);
                            }

                            url = loader.getResource(filePath);
                        }
                    }

                    // If the file didnt exist, try the default
                    if (url == null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Resolved entity " + entityPath);
                        }

                        url = loader.getResource(entityPath);
                    }
                }
            }
        }

        if (url == null) {
            // Check if we recognise this namespace
            if (systemID != null) {
                if (systemID.startsWith(RPDM_XSD_URL)) {
                    // this is a volantis xsd -- Get the version number
                    int start = RPDM_XSD_URL.length();
                    int end = systemID.indexOf('/', start);
                    if (end > 0) {
                        version = systemID.substring(start, end).trim();
                    } else {
                        version = "latest";
                    }
    
                    filePath = RPDM_XSD_PATH + "v" + version + "/" + RPDM_XSD_FILE;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Resolved xsd " + filePath);
                    }
    
                    url = loader.getResource(filePath);
                }
            }
        }

        if (url != null) {
            return new InputSource(url.toExternalForm());
        } else {
            return null;
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/3	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/4	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04	2551/3	steve	VBM:2003121905 Javadoced and move component cache attribute reading

 10-Feb-04	2551/1	steve	VBM:2003121905 Remote Repository Overhaul

 ===========================================================================
*/
