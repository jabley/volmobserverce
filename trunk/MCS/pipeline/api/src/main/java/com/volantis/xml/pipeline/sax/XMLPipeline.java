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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2003030405 - Created. Interface for a
 *                              pipeline of XMLProcesses.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;

/**
 * An XMLPipeline consists of a set of XMLProcesses connected together in
 * sequence.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <h2>Use</h2>
 * <p>An {@link XMLPipeline} is simply a useful way of organising a sequence of
 * {@link XMLProcess}es. There is no guarantee that any two processes will
 * actually be in the same pipeline and so processes must not rely on this. e.g.
 * They should not use the XMLPipeline as a property key in the
 * {@link XMLPipelineContext}.</p>
 * <p>Although it is possible neither an {@link XMLPipeline} (or
 * {@link XMLProcess}) must be connected directly to an applications SAX
 * event handlers, e.g. {@link org.xml.sax.ContentHandler}. The applications
 * SAX event handlers must only be connected to either an
 * {@link XMLPipelineReader} or an {@link XMLPipelineFilter}</p>
 *
 * <h2>Adding Process</h2>
 * <p>Adding a process to the pipeline involves the following steps irrespective
 * of the location in the pipeline the process is being added.</p>
 * <ul>
 * <li>Invoke the {@link XMLProcess#setPipeline} method to indicate that
 * the process is owned by this pipeline.</li>
 * <li>Add the process to the appropriate part of the pipeline.</li>
 * <li>Start the process by calling {@link XMLProcess#startProcess}.</li>
 * </ul>
 *
 * <h2>Removing Process</h2>
 * <p>Removing a process from the pipeline involves the following steps
 * irrespective of the location in the pipeline that the process is being added.
 * </p>
 * <ul>
 * <li>Stop the process by calling {@link XMLProcess#stopProcess}.</li>
 * <li>Remove the process from the pipeline.</li>
 * <li>Invoke the {@link XMLProcess#setPipeline} method to indicate that
 * the process is no longer owned by the pipeline.</li>
 * </ul>
 * <p>After removal the caller should almost certainly invoke the
 * {@link XMLProcess#release} method to release any resources associated
 * with the process.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface XMLPipeline extends ResourceOwner {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Retrieve the XMLPipelineContext associated with this pipeline.
     * @return the XMLPipelineContext
     */
    public XMLPipelineContext getPipelineContext();

    /**
     * Get the XMLProcess that wraps the pipeline.
     *
     * <p>This method allows pipelines to be composed from lots of nested
     * pipelines simply by adding the returned process to other pipelines.</p>
     * <p>If the returned XMLProcess is added to another pipeline then it
     * should not call any methods on its contained processes.</p>
     *
     * @return The XMLProcess that wraps this pipeline.
     */
    public XMLProcess getPipelineProcess();

    /**
     * Add a process to the head of the pipeline.
     * @param process The XMLProcess to add to the head.
     */
    public void addHeadProcess(XMLProcess process)
            throws SAXException;

    /**
     * Remove a process from the head of the pipeline.
     * <p>If the pipeline is empty then this throws an
     * {@link IllegalStateException}.</p>
     * @return The process that was at the head of the pipeline
     * @throws IllegalStateException If the pipeline is empty.
     */
    public XMLProcess removeHeadProcess()
            throws SAXException;

    /**
     * Return the XMLProcess at the head of the pipeline.
     * @return the XMLProcess at the head or null if the pipeline is
     * empty
     */
    public XMLProcess getHeadProcess();

    /**
     * Add a process to the tail of the pipeline.
     * @param process The XMLProcess to add to the tail.
     */
    public void addTailProcess(XMLProcess process)
            throws SAXException;

    /**
     * Remove a process from the tail of the pipeline.
     * <p>If the pipeline is empty then this throws an
     * {@link IllegalStateException}.</p>
     * @throws IllegalStateException If the pipeline is empty.
     */
    public XMLProcess removeTailProcess()
            throws SAXException;

    /**
     * Return the XMLProcess at the head of the pipeline.
     * @return the XMLProcess at the head or null if the pipeline is
     * empty
     */
    public XMLProcess getTailProcess();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 ===========================================================================
*/
