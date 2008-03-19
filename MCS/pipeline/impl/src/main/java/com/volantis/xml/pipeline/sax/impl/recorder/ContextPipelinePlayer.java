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

package com.volantis.xml.pipeline.sax.impl.recorder;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.sax.recorder.FlowController;
import com.volantis.xml.sax.recorder.SAXPlayer;
import com.volantis.xml.sax.recorder.SAXRecording;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A {@link PipelinePlayer} that updates the {@link XMLPipelineContext} while
 * playing back events.
 *
 * <p>Also integrates the flow control of the pipeline and the underlying
 * {@link SAXPlayer}.</p>
 */
public class ContextPipelinePlayer
        implements PipelinePlayer, FlowController {

    /**
     * The underlying player.
     */
    private final SAXPlayer player;

    /**
     * The {@link FlowControlManager}
     */
    private FlowControlManager manager;

    /**
     * Initialise.
     *
     * @param recording The underlying recording.
     */
    public ContextPipelinePlayer(SAXRecording recording) {
        this.player = recording.createPlayer();
    }

    // Javadoc inherited.
    public void play(XMLProcess process) throws SAXException {

        XMLPipeline pipeline = process.getPipeline();

        ContextManagerProcess cup = new ContextManagerProcess();
        cup.setPipeline(pipeline);

        cup.setNextProcess(process);
        cup.startProcess();

        XMLPipelineContext context = pipeline.getPipelineContext();
        Locator playLocator = player.getLocator();
        context.pushLocator(playLocator);
        manager = context.getFlowControlManager();
        try {
            player.setContentHandler(cup);
            player.setFlowController(this);
            player.play();
        } finally {
            manager = null;

            Locator poppedLocator = (Locator) context.popLocator();
            if (poppedLocator != playLocator) {
                throw new IllegalStateException(
                        "Popped locator does not match pushed locator");
            }
        }

        cup.stopProcess();
    }

    // Javadoc inherited.
    public boolean exitCurrentLevel() {
        return manager.shouldExitCurrentLevel();
    }
}
