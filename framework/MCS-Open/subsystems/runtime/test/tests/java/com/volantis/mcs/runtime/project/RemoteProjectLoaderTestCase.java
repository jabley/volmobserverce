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
package com.volantis.mcs.runtime.project;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.InputStream;

/**
 * Verify that {@link RemoteProjectLoader} behaves as expected.
 */
public class RemoteProjectLoaderTestCase extends PathProjectLoaderTestAbstract {

    private static final String REMOTE_PROJECT_URL =
            "http://remoteHost:8080/resources/";

    protected ProjectLoader createLoader() {

        return new RemoteProjectLoader() {

            // Javadoc inherited.
            protected HttpMethod connectAndExecute(final String urlAsString) {

                GetMethod method = new GetMethod() {

                    public int getStatusCode() {
                        return 200;
                    }

                    public InputStream getResponseBodyAsStream() {
                        return this.getClass().getResourceAsStream(
                                "a/mcs-project.xml");
                    }

                    public Header getResponseHeader(String s) {
                        if (s.equals("x-mcs-project-config")) {
                            Header header = new Header();
                            header.setName("x-mcs-project-config");
                            header.setValue(REMOTE_PROJECT_URL);
                            return header;
                        } else {
                            return super.getResponseHeader(s);
                        }
                    }
                };
                return method;
            }
        };
    }
}
