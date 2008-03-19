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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.integration;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.test.MockTestHelper;
import mock.javax.servlet.ServletContextMock;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WebAppManager {

    private static final WebAppManager MANAGER = new WebAppManager();


    public static WebAppManager getManager() {
        return MANAGER;
    }

    private final Map url2WebApp;

    public WebAppManager() {
        url2WebApp = new HashMap();
    }

    public WebApp getWebApp(Class clazz, String relativePathToConfig)
            throws Exception {

        URL url = clazz.getResource(relativePathToConfig);
        if (url == null) {
            throw new IllegalArgumentException(
                    "Could not find mcs-config.xml for webapp at path " +
                            relativePathToConfig + " relative to class " +
                            clazz);
        }

        WebApp webApp = (WebApp) url2WebApp.get(url);
        if (webApp == null) {
            webApp = createWebApp(url);
            url2WebApp.put(url, webApp);
        }

        return webApp;
    }

    private WebApp createWebApp(URL url) throws Exception {

        Logger.getDefaultHierarchy().setThreshold(Level.WARN);

        MockTestHelper.begin();
        try {
            MarinerServletApplication servletApplication =
                    createMarinerApplication(url.toExternalForm());
            WebApp webApp = new WebApp(servletApplication);
            return webApp;
        } finally {
            MockTestHelper.end(false);
        }

    }


    private MarinerServletApplication createMarinerApplication(String configURL)
            throws ServletException, IOException {

        MockFactory mockFactory = MockFactory.getDefaultInstance();
        ExpectationBuilder expectations = mockFactory.createUnorderedBuilder();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ServletContextMock servletContextMock =
                new ServletContextMock("servletContextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        servletContextMock.expects.getAttribute("volantis").returns(null);
        servletContextMock.fuzzy.setAttribute("volantis",
                mockFactory.expectsInstanceOf(Volantis.class));

        File webinfDir;
        String webapp;
        if (configURL.startsWith("file:")) {
            configURL = configURL.substring(5);
            File file = new File(configURL);
            webinfDir = file.getParentFile();
            webapp = webinfDir.getParent();
        } else {
            throw new IllegalStateException(
                    "Class relative resources 'webapp' not a file URL");
        }

        createDeviceRepository(
                new File(webinfDir, "test-devices"),
                new File(webinfDir, "test-devices.mdpr"));

        servletContextMock.expects.getRealPath("/").returns(webapp).any();
        servletContextMock.expects.getInitParameter("config.file")
                .returns("/WEBINF/mcs-config.xml").any();
        servletContextMock.expects.getInitParameter("mcs.log4j.config.file")
                .returns("/WEBINF/mcs-log4j.xml").any();

        servletContextMock.expects.getAttribute("marinerApplication")
                .returns(null);
        servletContextMock.fuzzy
                .setAttribute("marinerApplication",
                        mockFactory.expectsInstanceOf(
                                MarinerServletApplication.class));

        MarinerServletApplication servletApplication =
                MarinerServletApplication.getInstance(servletContextMock);

        servletContextMock.expects.getAttribute("marinerApplication")
                .returns(servletApplication).any();
        return servletApplication;
    }

    private void createDeviceRepository(File rootDir, File devices)
            throws IOException {

        ZipOutputStream zos =
                new ZipOutputStream(new FileOutputStream(devices));
        addToZip(zos, "", rootDir);
        zos.close();
    }

    private void addToZip(ZipOutputStream zos, String path, File dir)
            throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                addToZip(zos, path + file.getName() + "/", file);

            } else {

                String relative = path + file.getName();
                ZipEntry entry = new ZipEntry(relative);
                zos.putNextEntry(entry);

                InputStream is = new BufferedInputStream(
                        new FileInputStream(file));
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) > -1) {
                    zos.write(buffer, 0, read);
                }

                zos.closeEntry();
            }
        }
    }

}
