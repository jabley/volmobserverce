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
 * 31-03-03     Doug            VBM:2003030405 - Created. A Basic
 *                              implementation of the XMLPipelineProcess
 *                              interface.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * A Basic implementation of the XMLPipelineProcess interface.
 */
public class XMLPipelineProcessImpl extends XMLProcessImpl
        implements XMLPipelineProcess {

    /**
     * List that will contain all the processes added to this pipeline.
     * Process at the head of the list will recieve events first.
     * The process at the tail of the list will be chained to this
     * PipelineProcesses "next" process.
     */
    private final List processes;

    /**
     * Reference to the XMLPipelienContext associated with this pipeline.
     */
    private XMLPipelineContext pipelineContext;

    /**
     * Flag that indicated whether or not this pipeline should start its
     * nested processes when it is started, or not. This is needed because if
     * a pipeline is created with a context then nested processes are started
     * when they are added, otherwise they are not.
     */
    private final boolean startNestedProcessesOnStart;

    private ProcessState state;

    /**
     * Creates a new <code>XMLPipelineProcessImpl</code> instance.
     * You should use this constructor when you intend to add nest this
     * pipeline inside another pipeline. Any <code>XMLProcess<code> objects
     * added to this process will not be started until this pipeline has
     * been added to another pipeline.
     */
    public XMLPipelineProcessImpl() {
        this(null);
    }

    /**
     * Create a new XMLPipelineProcessImpl instance. This default pipeline
     * will not forward "nested" startDocument() and endDocument() events
     * to any process that has been set as the "next" process.
     * @param pipelineContext the associated XMLPipelineContext
     */
    public XMLPipelineProcessImpl(XMLPipelineContext pipelineContext) {
        this.pipelineContext = pipelineContext;
        processes = new ArrayList();
        startNestedProcessesOnStart = (pipelineContext == null);
        state = ProcessState.INITIALISED;
    }

    // javadoc inherited
    public XMLPipeline getPipeline() {
        return this;
    }

    /**
     * This will be invoked if this pipeline is being added to another
     * pipeline.
     */
    public void setPipeline(XMLPipeline pipeline) {
        // if context is null then retrieve context from the pipeline that
        // we are being added to.
        if (pipelineContext == null) {
            pipelineContext = pipeline.getPipelineContext();
        } else {
            throw new IllegalArgumentException(
                    "This pipeline cannot be nested");
        }
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        if (state != ProcessState.INITIALISED) {
            throw new IllegalStateException(
                    "Cannot start while in state " + state);
        }

        this.state = ProcessState.ACTIVE;

        // if this process is nested inside another pipeline then all
        // processes inside this process need to be started
        if (startNestedProcessesOnStart) {
            XMLProcess process;
            for (int i = 0; i < getProcessCount(); i++) {
                process = (XMLProcess)processes.get(i);
                process.startProcess();
            }
        }
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        // if this process is nested inside another pipeline then all
        // processes inside this process need to be stopped
        if (state != ProcessState.ACTIVE) {
            throw new IllegalStateException("Cannot stop in state " + state);
        }
        state = ProcessState.STOPPED;

        for (int i = 0; i < getProcessCount(); i++) {
            XMLProcess process = (XMLProcess)processes.get(i);
            process.stopProcess();
        }
    }

    // javadoc inherited
    public XMLPipelineContext getPipelineContext() {
        return pipelineContext;
    }

    // javadoc inherited
    public void addHeadProcess(XMLProcess process)
            throws SAXException {

        // Cannot add a null process
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }

        // call setPipeline() on the process being added
        process.setPipeline(this);

        // add the process to the pipeline
        addHeadProcessImpl(process);

        // start the process
        handleStartProcess(process);
        //process.startProcess();
    }

    /**
     * Add an adapter process to the pipeline
     * @param process the AdapterProcess that is to be added to the pipeline
     * @param attributes the Attributes associated with the process
     * @throws SAXException if an error occurs
     * @throws NullPointerException if the process being added is null
     * @todo later Remove this method dynamic rules replace adapters
     *
     */
    public void addHeadProcess(AdapterProcess process, Attributes attributes)
            throws SAXException {

        // Cannot add a null process
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }

        // call setPipeline() on the process being added
        process.setPipeline(this);

        // add the process to the pipeline
        addHeadProcessImpl(process);

        //XML pass the attributes to the process
        process.processAttributes(attributes);

        // start the process
        handleStartProcess(process);
    }

    // javadoc inherited
    public XMLProcess removeHeadProcess() throws SAXException {
        // get hold of the head process
        XMLProcess head = getHeadProcess();
        if (head == null) {
            throw new IllegalStateException(
                    "Process cannot be removed as pipeline is empty");
        }
        head.stopProcess();
        head.release();
        return removeHeadProcessImpl();
    }

    // javadoc inherited
    public XMLProcess getHeadProcess() {
        XMLProcess head = null;
        if (getProcessCount() > 0) {
            head = (XMLProcess)processes.get(0);
        }
        return head;
    }

    // javadoc inherited
    public void addTailProcess(XMLProcess process)
            throws SAXException {

        // Cannot add a null process
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }

        // call setPipeline() on the process being added
        process.setPipeline(this);

        // add the process to the pipeline
        addTailProcessImpl(process);

        // start the process
        handleStartProcess(process);
    }

    // javadoc inherited
    public XMLProcess removeTailProcess()
            throws SAXException {
        // get hold of the tail process
        XMLProcess tail = getTailProcess();
        if (tail == null) {
            throw new IllegalStateException(
                    "Process cannot be removed as pipeline is empty");
        }
        tail.stopProcess();
        tail.release();
        return removeTailProcessImpl();
    }

    // javadoc inherited
    public XMLProcess getTailProcess() {
        XMLProcess tail = null;
        int processCount = getProcessCount();
        if (processCount > 0) {
            tail = (XMLProcess)processes.get(processCount - 1);
        }
        return tail;
    }

    // Javadoc inherited
    public void setNextProcess(XMLProcess next) {
        // The process at the tail of the internal pipeline should
        // be linked to the next process passed in
        this.next = next;
        XMLProcess last = getTailProcess();
        if (null != last) {
            last.setNextProcess(next);
        }
    }

    // javadoc inherited
    public XMLProcess getPipelineProcess() {
        return this;
    }

    // javadoc inherited
    public void release() {
        super.release();
        this.pipelineContext = null;
    }

    /**
     * Returns the number of process that this pipeline contains.
     * @return the number of process in the pipeline.
     */
    protected int getProcessCount() {
        return processes.size();
    }

    /**
     * Add a process to the pipeline. The process is not started or passed
     * the pipeline via the <code>setPipeline</code> method.
     * @param process the XMLProcess that is to be added.
     */
    protected void addHeadProcessImpl(XMLProcess process) {
        // Cannot add a null process
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }

        // get hold of the process that is currently at the head.
        XMLProcess currentHead = getHeadProcess();
        XMLProcess nextProcess = (currentHead != null) ? currentHead : getNextProcess();
        if (nextProcess != null) {
            process.setNextProcess(nextProcess);
        }
        // add the process to the head of the list
        processes.add(0, process);
    }

    /**
     * Removes the head process from the pipeline. Does not stop the process.
     * @return The process removed or null if the pipeline was empty
     */
    protected XMLProcess removeHeadProcessImpl() {
        XMLProcess removed = null;
        if (getProcessCount() > 0) {
            // remove the process from the list
            removed = (XMLProcess)processes.remove(0);
            removed.setNextProcess(null);
        }
        return removed;
    }

    /**
     * Add a process to the tail of the pipeline. The process is not started
     * or passed the pipeline via the <code>setPipeline</code> method.
     * @param process the XMLProcess that is to be added.
     */
    protected void addTailProcessImpl(XMLProcess process) {
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }
        // need to get the current tail.
        XMLProcess currentTail = getTailProcess();

        // if there is a process at the tail then is needs to be chained
        // to the new tail that is being added
        if (currentTail != null) {
            currentTail.setNextProcess(process);
        }
        // if there is a next process then it should be chained to
        // the new tail process
        XMLProcess next = getNextProcess();
        if (next != null) {
            process.setNextProcess(next);
        }
        // add the process to the head of the list
        processes.add(getProcessCount(), process);
    }

    /**
     * Removes the head process from the pipeline. Does not stop the process.
     * @return The process removed or null if the pipeline was empty
     */
    protected XMLProcess removeTailProcessImpl() {
        XMLProcess removed = null;
        int processCount = getProcessCount();
        if (processCount > 0) {
            // remove the process from the list
            removed = (XMLProcess)processes.remove(processCount - 1);
            removed.setNextProcess(null);
            // ensure the new tail is chained to any next process that has been
            // registered with this pipeline.
            XMLProcess newTail = getTailProcess();
            XMLProcess next = getNextProcess();
            if (next != null & newTail != null) {
                newTail.setNextProcess(next);
            }
        }
        // return the process that was removed.
        return removed;
    }

    // javadoc inherited
    protected XMLProcess getConsumerProcess() {
        // events generated / forwarded via this process should be
        // targeted to the process at the head of this pipeline. If the
        // pipeline is empty then return the next process.
        return (getProcessCount() > 0) ? getHeadProcess() : getNextProcess();
    }

    /**
     * Starts the process passed in if either of the following conditions
     * are met.
     *
     * This pipeline cannot be nested inside another pipeline
     * This pipeline can be nested insied another pipeline and has been
     * nested
     * @param process the XMLProcess that may be started
     * @throws SAXException if an error occurs
     */
    private void handleStartProcess(XMLProcess process) throws SAXException {
        // start the process if the pipeline is not nestable or it is
        // nestable and has been nested.
        if (pipelineContext != null) {
            process.startProcess();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 31-Oct-03	440/1	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 11-Aug-03	275/4	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 22-Jul-03	225/2	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 14-Jul-03	185/1	steve	VBM:2003071402 Refactor exceptions into throwable package

 23-Jun-03	95/5	doug	VBM:2003061605 Removed inner class

 23-Jun-03	95/3	doug	VBM:2003061605 Document Event Filtering changes

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
