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
package com.volantis.xml.schema;

import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An {@link org.xml.sax.EntityResolver} that retrieves  a resource in a
 * jar file for entities with a specified system id.
 */
public class JarFileEntityResolver implements EntityResolver {

    /**
     * Contains the mappings from an entities systemID to the fully
     * qualified path in the jar.
     */
    private Map jarFileResources = new HashMap() {
        /**
         * Override get so that we can cope with relative system id urls.
         */
        // rest of javadoc inherited
        public Object get(Object key) {
            Object result = super.get(key);
            if (result == null) {
                // look for a relative url match this is slower.
                Iterator keys = keySet().iterator();
                while (keys.hasNext() && result == null) {
                    String systemId = (String) keys.next();
                    result = systemId.endsWith((String) key) ?
                            get(systemId) : null;
                }
            }
            return result;
        }
    };

    /**
     * Maps a systemId to an InputSource.
     */
    private Map inputSources = new HashMap();

    /**
     * Some member of the jar which is to be searched: used to define the
     * jar which is to be searched
     */
    private final Object jarMember;

    /**
     * Constructor, the use of which means that the jar that will be
     * searched is the jar the the specified jarMember's class belongs to.
     * Typically this will be set to "this" (or ConstructorClass.class),
     * to indicate that the jar containing the constructor caller's class
     * is the jar to be searched. However, an arbitrary object can be used
     * to search an arbitrary class.
     *
     * @param jarMember Any member of the jar which is to be searched.
     */
    public JarFileEntityResolver(Object jarMember) {
        this.jarMember = jarMember;
    }

    /**
     * Default constructor, the use of which means that the jar that will be
     * searched is the jar that <p><strong>THIS CLASS</strong</p> belongs to.
     */
    public JarFileEntityResolver() {
        this.jarMember = this;
    }

    /**
     * Add register a given entities systemId with the path to the resource
     * that we should retrieve from the jar.
     * @param systemId the entities systemId
     * @param resourcePath the fully qualified path for the resource that is
     * to be retrieved from the jar.
     */
    public void addSystemIdMapping(String systemId, String resourcePath) {
        jarFileResources.put(systemId, resourcePath);
    }

    /**
     * Add a mapping from remote location to the local java resource of a
     * schema using the provided schema definition.
     *
     * @param schema the schema definition
     */
    public void addSystemIdMapping(SchemaDefinition schema) {

        addSystemIdMapping(schema.getLocationURL(), schema.getResourceName());
    }

    /**
     * Add a mapping from remote location to the local java resource of a
     * schema using the provided schema definition.
     *
     * @param schemata the schema definition
     */
    public void addSystemIdMapping(Schemata schemata) {
        Iterator iterator = schemata.iterator();
        while (iterator.hasNext()) {
            SchemaDefinition schema = (SchemaDefinition) iterator.next();
            addSystemIdMapping(schema);
        }
    }

    // javadoc inherited
    public InputSource resolveEntity(String publicID, String systemID)
            throws SAXException, IOException {
        // check to see if we have processed this systemID already
        InputSource inputSource =
                (InputSource) inputSources.get(systemID);

        if (inputSource == null) {
            inputSource = resolveInputSource(systemID);

            // Cache the input source if one was retrieved.
            if (inputSource != null) {
                inputSources.put(systemID, inputSource);
            }
        }

        return inputSource;
    }

    protected InputSource resolveInputSource(String systemID)
            throws ExtendedSAXException {

        InputSource inputSource = null;
        // we haven't processed this system id before.
        // check to see if we need to retieve a resource out of the
        // jar
        String resourceFile = (String) jarFileResources.get(systemID);
        if (resourceFile != null) {
            // retrieve a URL to the resource in the jar.
            URL url = getResourceURL(resourceFile);
            if (url == null) {
                throw new ExtendedSAXException("Could not resolve the entity " +
                        systemID);
            } else {
                // create the InputSource
                inputSource = new InputSource(url.toExternalForm());
            }
        }

        return inputSource;
    }

    /**
     * Returns a URL to a given named resource.
     * @param resource the resource to look for.
     * @return the URL to the resource
     */
    protected URL getResourceURL(String resource) {
        ClassLoader cl;
        URL url = null;

        // Use the context class loader first if any.
        cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            url = cl.getResource(resource);
        }

        // If that did not work then use the jarMember's class's class loader.
        if (url == null) {
            cl = jarMember.getClass().getClassLoader();
            url = cl.getResource(resource);
        }

        // If that did not work then use the system class loader.
        if (url == null) {
            cl = ClassLoader.getSystemClassLoader();
            url = cl.getResource(resource);
        }

        return url;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 11-Apr-05	7376/3	allan	VBM:2005031101 SmartClient bundler - commit for testing

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5577/2	adrianj	VBM:2004081004 Workaround for corrupted bundleresource URIs

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 13-May-04	4333/1	allan	VBM:2004051015 Handle relative urls in JarFileEntityResolver.

 26-Apr-04	4037/3	doug	VBM:2004042301 Provided mechanism for obtaining an EntityResolver that resolves all MCS repository schemas

 19-Jan-04	2665/1	allan	VBM:2003112702 Provide ThemeEditorContext.

 16-Jan-04	2564/5	richardc	VBM:2003112502 JarFileEntityResolver and StyleCategory and related class additions and changes

 16-Jan-04	2564/3	richardc	VBM:2003112502 JarFileEntityResolver and StyleCategory and related class additions and changes

 16-Jan-04	2564/1	richardc	VBM:2003112502 JarFileEntityResolver and StyleCategory (and related) changes

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 ===========================================================================
*/
