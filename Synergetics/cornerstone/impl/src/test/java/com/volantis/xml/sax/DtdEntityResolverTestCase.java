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

import com.volantis.shared.resource.ResourceLoaderMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.java.io.InputStreamMock;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * A unit test case for {@link DtdEntityResolver}.
 */
public class DtdEntityResolverTestCase extends TestCaseAbstract {

    /**
     * Ensure that an external entity consisting of a public and system id that
     * exactly match one known to the entity resolver is resolved to the
     * expected local resource.
     *
     * @throws IOException
     * @throws SAXException
     */
    public void testPublicAndSystemMatchSuccess()
            throws IOException, SAXException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        ResourceLoaderMock resourceLoader = new ResourceLoaderMock(
                "resourceLoader", expectations);
        InputStreamMock inputSteam = new InputStreamMock(
                "inputStream", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        resourceLoader.expects.getResourceAsStream("resource").returns(
                inputSteam);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DtdEntityResolver resolver = new DtdEntityResolver(resourceLoader);
        resolver.addExternalEntity(new LocalExternalEntity("public", "system",
                "resource"));
        InputSource source = resolver.resolveEntity("public", "system");

        assertEquals("", "public", source.getPublicId());
        assertEquals("", "system", source.getSystemId());
        assertEquals("", inputSteam, source.getByteStream());
    }

    /**
     * Ensure that an external entity consisting of a public id that matches
     * and a system id that does not match one known to the entity resolver is
     * resolved to the expected local resource, regardless of the system id
     * mismatch.
     *
     * @throws IOException
     * @throws SAXException
     */
    public void testPublicMatchAndSystemMismatchSuccess()
            throws IOException, SAXException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        ResourceLoaderMock resourceLoader = new ResourceLoaderMock(
                "resourceLoader", expectations);
        InputStreamMock inputSteam = new InputStreamMock(
                "inputStream", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        resourceLoader.expects.getResourceAsStream("resource").returns(
                inputSteam);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DtdEntityResolver resolver = new DtdEntityResolver(resourceLoader);
        resolver.addExternalEntity(new LocalExternalEntity("public", "system",
                "resource"));
        InputSource source = resolver.resolveEntity("public", "alt-system");

        assertEquals("", "public", source.getPublicId());
        assertEquals("", "system", source.getSystemId());
        assertEquals("", inputSteam, source.getByteStream());
    }

    /**
     * Ensure that an external entity consisting of a public id that does not
     * match and a system id that does match one known to the entity resolver
     * is not resolved to a local resource, regardless of the system id
     * match.
     *
     * @throws IOException
     * @throws SAXException
     */
    public void testPublicMismatchAndSystemMatchFailure()
            throws IOException, SAXException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        ResourceLoaderMock resourceLoader = new ResourceLoaderMock(
                "resourceLoader", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        DtdEntityResolver resolver = new DtdEntityResolver(resourceLoader);
        resolver.addExternalEntity(new LocalExternalEntity("public", "system",
                "resource"));
        InputSource source = resolver.resolveEntity("alt-public", "system");
        assertNull("", source);

    }

    /**
     * Ensure that an external entity consisting of only a system id that does
     * match one known to the entity resolver is resolved to the expected
     * local resource.
     *
     * @throws IOException
     * @throws SAXException
     */
    public void testSystemOnlyMatchSuccess()
            throws IOException, SAXException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        ResourceLoaderMock resourceLoader = new ResourceLoaderMock(
                "resourceLoader", expectations);
        InputStreamMock inputSteam = new InputStreamMock(
                "inputStream", expectations);


        // ==================================================================
        // Create expectations.
        // ==================================================================

        resourceLoader.expects.getResourceAsStream("resource").returns(
                inputSteam);

        // ==================================================================
        // Do the test.
        // ==================================================================

        DtdEntityResolver resolver = new DtdEntityResolver(resourceLoader);
        resolver.addExternalEntity(new LocalExternalEntity("system",
                "resource"));
        InputSource source = resolver.resolveEntity(null, "system");

        assertEquals("", null, source.getPublicId());
        assertEquals("", "system", source.getSystemId());
        assertEquals("", inputSteam, source.getByteStream());
    }

    /**
     * Ensure that an external entity consisting of only a system id that does
     * not match one known to the entity resolver is not resolved to a local
     * resource.
     *
     * @throws IOException
     * @throws SAXException
     */
    public void testSystemOnlyMismatchFailure()
            throws IOException, SAXException {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        ResourceLoaderMock resourceLoader = new ResourceLoaderMock(
                "resourceLoader", expectations);


        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        DtdEntityResolver resolver = new DtdEntityResolver(resourceLoader);
        resolver.addExternalEntity(new LocalExternalEntity("system",
                "resource"));
        InputSource source = resolver.resolveEntity(null, "alt-system");

        assertNull("", source);
    }

}
