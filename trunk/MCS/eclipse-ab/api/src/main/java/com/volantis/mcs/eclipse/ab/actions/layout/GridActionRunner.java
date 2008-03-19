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
package com.volantis.mcs.eclipse.ab.actions.layout;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;

/**
 * 
 * Runs grid creation action with progress monitor.
 *
 */
public class GridActionRunner implements IRunnableWithProgress {
    
    public static final int PROGRESS_SCALE = 5;
    
    private ODOMActionDetails details;
    private Dimension dimensions;
    private NewGridFormatActionCommand action;
    
    public GridActionRunner(ODOMActionDetails details, Dimension dimensions, NewGridFormatActionCommand action) {
        this.details = details;
        this.dimensions = dimensions;
        this.action = action;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException,
                                    InterruptedException {
        //It's hard to set max task value, because progress cannot be determined 
        //in LayoutActionCommand.replaceElement(Element element, Element replacement).
        //There's element added to list on specified index and in this place prgress cannot be counted.
        monitor.beginTask(action.getTaskName(), dimensions != null ? 
                                        (dimensions.height + dimensions.width)
                                        *PROGRESS_SCALE : 
                                            IProgressMonitor.UNKNOWN);        
        try {            
            action.replaceAndSelect(details, dimensions, monitor);            
        } finally {
            monitor.done();
        }        
    }

}
