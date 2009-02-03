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
 * (c) Volantis Systems Ltd 2008
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.servlet;

import com.volantis.map.ics.imageprocessor.writer.impl.ImageWriterFactoryImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import javax.media.jai.RenderedOp;
import java.util.Iterator;

import org.apache.log4j.Category;

/**
 * Perform tests related to processing of animated GIF source
 */
public class AnimatedGIFTestCase  extends TestCaseAbstract {

    protected void tearDown() throws Exception {
        // Required to prevent each message being logged once for each test
        // that has run in the past - this generates a huge log output and
        // seems to crash JUnit reporting with an OutOfMemoryError.
        // Maybe we should put this in the servlet destroy() but the version of
        // Servlet Unit we are using has no way to shut down servlets - later
        // versions do appear to have this.
        Category.shutdown();
        super.tearDown();
    }
    
    public void testConversionToAllFormats() throws Throwable {

        Iterator i = ImageWriterFactoryImpl.RULES.keySet().iterator();
        while (i.hasNext()) {
            String rule = (String)i.next();            
            RenderedOp image = TestUtilities.transcodeToImage(expectations, rule, "animated_indexed.gif", null, true);
            TestUtilities.checkConversionResult(rule, image);
        }
    }


}
