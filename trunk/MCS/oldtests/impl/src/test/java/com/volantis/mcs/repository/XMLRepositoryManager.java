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
package com.volantis.mcs.repository;

import com.volantis.mcs.repository.xml.XMLRepository;
import com.volantis.synergetics.testtools.io.NonExistantTemporaryFileCreator;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class which allows a test case to be executed with
 * a pre-configured XML repository
 */
public class XMLRepositoryManager{

    public void executeWith(final RepositoryCommand command) throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new NonExistantTemporaryFileCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {
                // set up
                Map properties = new HashMap();
                XMLRepository repository;
                RepositoryConnection repositoryConnection;

                if (!file.mkdirs()) {
                    throw new Exception("Could not create temporary directory structure for " +
                            file.getPath());
                }

                properties.put(XMLRepository.DEFAULT_PROJECT_DIRECTORY_PROPERTY,
                               file.getAbsolutePath());
                properties.put(XMLRepository.REPLACE_DIR_PROPERTY,
                               new Boolean(true));
                repository = XMLRepository.createRepository(properties);
                repositoryConnection = repository.connect();

                try {
                    command.execute(repositoryConnection,
                                    repository,
                                    file.getAbsolutePath());
                } finally {
                    // tear down
                    repositoryConnection.disconnect();
                    repository.terminate();
                }
            }
        });
    }

    /**
     * Should be implemented by the test in order to
     * carry out the test specific work
     */
    public interface RepositoryCommand {
        void execute(RepositoryConnection connection, XMLRepository repository, String repositoryDir)
                throws Exception;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Feb-05	6986/1	emma	VBM:2005021411 Changes merged from MCS3.3

 17-Feb-05	6974/1	emma	VBM:2005021411 Reworking tests to use testtools TempFileManager mechanism

 ===========================================================================
*/
