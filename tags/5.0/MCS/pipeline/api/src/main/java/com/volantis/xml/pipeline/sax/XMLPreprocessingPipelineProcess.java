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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. An
 *                              XMLPreprocessingPipelineProcess is an
 *                              XMLPipelineProcess that allows an XMLProcess
 *                              to behave as a preprocessing process. A
 *                              preprocessing process is one that will receive
 *                              events before any XMLProcesses that have been
 *                              pushed onto the pipeline using the
 *                              addHeadProcess(XMLProcess) method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import org.xml.sax.SAXException;

/**
 * An XMLPreprocessingPipelineProcess is an XMLPipelineProcess that can have
 * an XMLProcess that behaves as a preprocessor. A preprocessing process is
 * one that will receive events before any XMLProcesses that have been pushed
 * onto the pipeline using the addHeadProcess(XMLProcess) method. If no
 * preprocessor has been set then the process at the head of the pipeline
 * will be the first process to receive any sax events. An
 * XMLPreprocessingPipelineProcess also allows clients to change the
 * XMLProcessingMode.
 *
 * <pre>
 *
 * +------------------------------------------------------------------------+
 * +                                                                        +
 * +   +--------------+     +------+    +----+       +----+    +------+     +
 * +   | Preprocessor |     | head |    |    |  ...  |    |    | tail |     +
 * +   +--------------+     +------+    +----+       +----+    +------+     +
 * +                                                                        +
 * +------------------------------------------------------------------------+
 *
 * </pre>
 *
 * <p>{@link #getPipelineProcess()} returns a process that sends its events
 * through the preprocessor. {@link #getHeadProcess()} returns the head process
 * in the above diagram so any events sent will not be seen by the
 * preprocessor.</p>
 */
public interface XMLPreprocessingPipelineProcess extends XMLPipelineProcess {

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/2	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/
