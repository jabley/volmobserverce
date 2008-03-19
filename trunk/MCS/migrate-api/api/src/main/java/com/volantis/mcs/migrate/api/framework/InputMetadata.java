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
package com.volantis.mcs.migrate.api.framework;

/**
 * Encapsulates metadata for the input stream to a {@link ResourceMigrator}.
 * <p>
 * The input stream for resource migration can vary in its origin (eg. file, socket),
 * which can influence the processing of migration steps. For example, a file-based
 * input stream may use a {@link PathRecogniser} to help identify the type of
 * migration to perform, whereas a socket-based input stream
 * may rely on a {@link ContentRecogniser} only.
 * </p>
 *
 * @see ResourceMigrator#migrate(String, java.io.InputStream, OutputCreator)

 * @mock.generate
 *
 * @todo replace with an InputSource style object which contains data and metadata
 * @todo use the Content* interfaces for flexibility.
 */
public interface InputMetadata {

    /**
     * The URI of the input data, used for path recognition and error reporting.
     */
    String getURI();

    /**
     * True if the type of content should be deducted from the URI provided, 
     * false otherwise.
     */
    boolean applyPathRecognition();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 ===========================================================================
*/
