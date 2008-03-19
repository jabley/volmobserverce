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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.template;

import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This process can be used to substitute the current counter value from
 * the given Counter implementation when the process is started. For use in
 * the template model integration tests.
 */
public class CounterAdapterProcess extends AbstractAdapterProcess {
    /**
     * @supplierRole counter
     * @supplierCardinality 1
     */
    private Counter counter;

    public CounterAdapterProcess(Counter counter) {
        this.counter = counter;
    }

    public void processAttributes(Attributes attributes) throws SAXException {
    }

    /**
     * Inserts the current counter value as a characters event.
     */
    public void startProcess() throws SAXException {
        super.startProcess();
        String value = String.valueOf(counter.get());

        characters(value.toCharArray(), 0, value.length());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Jun-03	13/2	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/
