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
package com.volantis.mcs.migrate.impl.framework.identification;

import com.volantis.mcs.migrate.api.framework.ContentRecogniser;
import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.Version;

import java.util.Iterator;

/**
 * A factory for creating internal identification framework objects.
 *
 * @mock.generate
 */
public interface IdentificationFactory {

    /**
     * Create a content identifier.
     *
     * @param version the version that this content identifier identifies.
     * @param contentRecogniser does the actual recognition.
     * @return the content identifier created.
     */
    ContentIdentifier createContentIdentifier(Version version,
            ContentRecogniser contentRecogniser);

    /**
     * Create a migration step.
     *
     * @param input the input version supported by the step.
     * @param output the output version supported by the step.
     * @param streamMigrator does the actual migration of content.
     * @return the step created.
     */
    Step createStep(Version input, Version output,
            StreamMigrator streamMigrator);

    /**
     * Create a builder object which builds {@link TypeIdentifier} objects.
     *
     * @return the builder created.
     */
    TypeIdentifierBuilder createTypeIdentifierBuilder();

    /**
     * Create a identification match object.
     *
     * @param typeName the name of the type which was matched.
     * @param versionName the name of the version which was matched.
     * @param sequence the sequence of steps to be used to perform the
     *      migration.
     * @return the match created.
     */
    Match createMatch(String typeName, String versionName, Iterator sequence);

    /**
     * Create a builder object which builds {@link ResourceIdentifier} objects.
     *
     * @return the builder created.
     */
    ResourceIdentifierBuilder createResourceIdentifierBuilder();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
