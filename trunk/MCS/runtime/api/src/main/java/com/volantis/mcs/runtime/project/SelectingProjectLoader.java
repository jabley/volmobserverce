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

import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import org.jibx.runtime.JiBXException;

import java.io.IOException;

/**
 * Selects the appriopriate project loader to use based on the URL.
 */
public class SelectingProjectLoader
        implements ProjectLoader {

    private final ProjectLoader urlProjectLoader;
    private final ProjectLoader fileProjectLoader;
    private final ProjectLoader remoteProjectLoader;

    public SelectingProjectLoader(
            ProjectLoader urlProjectLoader,
            ProjectLoader fileProjectLoader,
            ProjectLoader remoteProjectLoader) {

        this.urlProjectLoader = urlProjectLoader;
        this.fileProjectLoader = fileProjectLoader;
        this.remoteProjectLoader = remoteProjectLoader;
    }

    private ProjectLoader selectProjectLoader(String url) {
        if (url.startsWith("file:")) {
            return fileProjectLoader;
        } else if (url.startsWith("http:")) {
            return remoteProjectLoader;
        } else {
            return urlProjectLoader;
        }
    }

    public RuntimeProjectConfiguration loadProjectConfiguration(
            String url, ProjectLoadingOptimizer optimizer)
            throws IOException, JiBXException {

        ProjectLoader loader = selectProjectLoader(url);
        return loader.loadProjectConfiguration(url, optimizer);
    }
}
