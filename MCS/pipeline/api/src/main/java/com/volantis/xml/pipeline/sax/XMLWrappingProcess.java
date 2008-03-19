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
 * 31-03-03     Doug            VBM:2003030405 - Created. Simple XMLProcess
 *                              that wraps another XMLProcess and delegates to
 *                              it.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

/*
 * The XMLProcessImpl class already delegates to the next class in the
 * pipeline so this class extends it. The main difference between this class
 * and XMLProcessImpl is that this class owns the object being delegated to
 * and the delegate never changes. This class is abstract as it does not make
 * sense to wrap a process unless you override some of the XMLProcess methods
 */

public abstract class XMLWrappingProcess
        extends XMLProcessImpl {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The XMLProcess that that is being wrapped
     */
    private XMLProcess delegate;

    /**
     * Creates a new XMLWrappingProcess instance
     * @param delegate the process that is being wrapped
     */
    protected XMLWrappingProcess(XMLProcess delegate) {
        setDelegate(delegate);
    }

    /**
     * Creates a new XMLWrappingProcess instance.
     */
    protected XMLWrappingProcess() {
    }

    /**
     * Set the delegate.
     * This MUST only be called once and only if the default constructor has
     * been used.
     */
    protected void setDelegate(XMLProcess delegate) {
        this.delegate = delegate;
        if (null != delegate && null != next) {
            delegate.setNextProcess(next);
        }
    }

    /**
     * Get the XMLProcess wrapped by this process.
     */
    public XMLProcess getDelegate() {
        return delegate;
    }

    // javadoc inherited from superclass
    public void setNextProcess(XMLProcess next) {
        this.next = next;
        // if we are delegating then the delegate should be targeting the
        // next process.
        if (null != delegate) {
            delegate.setNextProcess(next);
        }
    }

    // javadoc inherited from superclass
    protected XMLProcess getConsumerProcess() {
        // if we are delegating then return the deleate else return any next
        // process that is set.
        return (null == delegate) ? next : delegate;
    }
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

 ===========================================================================
*/
