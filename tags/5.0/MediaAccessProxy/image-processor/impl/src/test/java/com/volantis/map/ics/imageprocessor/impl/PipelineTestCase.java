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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.impl;

import junit.framework.TestCase;

import com.volantis.map.ics.imageprocessor.tool.Tool;
import com.volantis.map.ics.imageprocessor.tool.ToolException;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.operation.ObjectParameters;

import javax.media.jai.RenderedOp;

public class PipelineTestCase extends TestCase {

    public void testProcess() throws Exception {
        final boolean[] processed = new boolean[]{false, false};
        Tool tool1 = new Tool() {
            public RenderedOp[] process(RenderedOp[] ops, ObjectParameters params)
                throws ToolException {
                processed[0] = true;
                return null;
            }
        };
        Tool tool2 = new Tool() {
            public RenderedOp[] process(RenderedOp[] ops, ObjectParameters params)
                throws ToolException {
                processed[1] = true;
                return null;
            }
        };

        Pipeline pipeline = new Pipeline();
        pipeline.addTool(tool1);
        assertEquals("Pipeline doesn't contain the tool1 after adding",
                     pipeline.listTools()[0], tool1);
        pipeline.addTool(tool2);
        assertTrue("Pipeline doesn't contain the tool2 after adding",
                   pipeline.listTools()[0] == tool2 ||
                   pipeline.listTools()[1] == tool2);

        pipeline.process(null, null);
        assertTrue("The tools haven't been invoked during processing",
                   processed[0] && processed[1]);

        pipeline.removeTool(tool1);
        assertEquals("Invalid tool was removed",
                     pipeline.listTools()[0],
                     tool2);

        pipeline.removeTool(tool2);
        assertEquals("Tool2 wasn't removed", pipeline.listTools().length, 0);
    }
}
