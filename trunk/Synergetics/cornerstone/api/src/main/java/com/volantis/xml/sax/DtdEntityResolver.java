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
package com.volantis.xml.sax;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.shared.resource.ResourceLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * An entity resolver which can resolve externally defined external entities.
 * <p>
 * This is normally only required for DTD resolution as explained below.
 * <p>
 * This is not normally required for resolving schema entities as
 * {@link com.volantis.xml.sax.VolantisXMLReaderFactory} supplies these to the
 * parser using a SAX property so entity resolution is not required. If we
 * decide in future to use entity resolution for schemas then this class should
 * be refactored with {@link com.volantis.xml.schema.JarFileEntityResolver} and
 * {@link com.volantis.xml.sax.VolantisXMLReaderFactory}.
 *
 */
public class DtdEntityResolver implements EntityResolver {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DtdEntityResolver.class);

    /**
     * localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            DtdEntityResolver.class);

    /**
     * An abstraction around class loader to load resources for us.
     */
    private final ResourceLoader resourceLoader;

    /**
     * The map from public ID to PublicIDMatch (including locally cached
     * resource name and system id).
     */
    private final Map publicId2match = new HashMap();

    /**
     * The map from public ID to locally cached resource name.
     */
    private final Map systemId2resource = new HashMap();

    /**
     * Initialise.
     *
     * @param resourceLoader loads local resources for us.
     */
    public DtdEntityResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Add the definition of a locally accessible external entity.
     *
     * @param entity the external entity definition to add.
     */
    public void addExternalEntity(LocalExternalEntity entity) {

        final String resourceName = entity.getResourceName();

        final String systemId = entity.getSystemId();
        if (systemId2resource.get(systemId) != null) {
            throw new IllegalArgumentException(
                    "system id " + systemId + " already registered");
        }
        systemId2resource.put(systemId, resourceName);

        final String publicId = entity.getPublicId();
        if (publicId != null) {
            if (publicId2match.get(publicId) != null) {
                throw new IllegalArgumentException(
                        "public id " + publicId + " already registered");
            }

            publicId2match.put(publicId,
                    new PublicIdMatch(systemId, resourceName));
        }
    }

    // Javadoc inherited
    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException {

        if (systemId2resource.size() == 0) {
            throw new IllegalStateException("no entities have been provided");
        }

        // Try and find a local resource for the supplied entity information.
        // First we match on the public id, if that is null then we match on
        // the system id.
        String localResource = null;
        if (publicId != null) {
            // If the public id was set, try and find the name of a locally
            // cached resource assocated with the supplied public id.
            PublicIdMatch match = (PublicIdMatch) publicId2match.get(publicId);
            if (match != null) {
                localResource = match.getResourceName();
                // and force the use of our system id, regardless of what they
                // supplied, since we are overriding it anyway.
                systemId = match.getSystemId();
            }
        } else if (systemId != null) {
            // If the public id was null but the system id was set, then
            // try and find the name of a locally cached resource assocated
            // with the supplied system id.
            localResource = (String) systemId2resource.get(systemId);
        }

        InputSource result = null;
        if (localResource != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using locally cached entity for public ID: "
                        + publicId + ", system ID:" + systemId);
            }

            // Obtain a reference to the locally cached resource.
            InputStream stream = resourceLoader.getResourceAsStream(
                    localResource);
            if (stream == null) {
                throw new ExtendedSAXException(
                        EXCEPTION_LOCALIZER.format("resource-not-found",
                            localResource));
            }

            // Create a new input source from the reference to our locally
            // cached resource.
            result = new InputSource(stream);
            result.setPublicId(publicId);
            result.setSystemId(systemId);

        } else {
            // No locally cached resource is known for the supplied public and
            // system ids.
            if (logger.isDebugEnabled()) {
                logger.debug("No locally cached entity available for public " +
                        "ID: " + publicId + ", system ID:" + systemId);
            }
        }

        return result;
    }

    /**
     * The locally cached resource name and system id to use when we match a
     * public id.
     */
    private class PublicIdMatch {

        private String systemId;

        private String resourceName;

        public PublicIdMatch(String systemId, String resourceName) {
            this.systemId = systemId;
            this.resourceName = resourceName;
        }

        public String getSystemId() {
            return systemId;
        }

        public String getResourceName() {
            return resourceName;
        }
    }
}
