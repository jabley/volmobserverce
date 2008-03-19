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

/**
 * Defines a mapping for an external entity from public and or system id to a
 * local resource name.
 * <p>
 * This data should allow an entity resolver to resolve an external entity
 * reference to a local resource.
 */
public class LocalExternalEntity {

    /**
     * The public id of the entity, or null if matching should be done on
     * system id only.
     */
    private String publicId;

    /**
     * The system id of the entity.
     */
    private String systemId;

    /**
     * The local resource name of the entity.
     * <p>
     * This should be an absolute path of the form
     * "/com/volantis/blah/file.ext".
     */
    private String resourceName;

    /**
     * Initialise.
     *
     * @param systemId the system id of the entity.
     * @param resourceName the local resource name of the entity.
     */
    public LocalExternalEntity(String systemId,
            String resourceName) {
        this(null, systemId, resourceName);
    }

    /**
     * Initialise.
     *
     * @param publicId the public id of the entity, or null.
     * @param systemId the system id of the entity.
     * @param resourceName the local resource name of the entity.
     */
    public LocalExternalEntity(String publicId, String systemId,
            String resourceName) {
        if (publicId == null && systemId == null) {
            throw new IllegalArgumentException(
                    "At least one of public id and system id must be provided");
        }
        if (resourceName == null) {
            throw new IllegalArgumentException(
                    "Resource name must be provided");
        }
        this.publicId = publicId;
        this.systemId = systemId;
        this.resourceName = resourceName;
    }

    /**
     * @see #publicId
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * @see #systemId
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @see #resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

}
