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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.stubs;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * Stub for the ISchedulingRule interface.
 * @since Eclipse v3.0 port
 */
public class SchedulingRuleStub implements ISchedulingRule {
    public boolean contains(ISchedulingRule iSchedulingRule) {
        return false;
    }

    public boolean isConflicting(ISchedulingRule iSchedulingRule) {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 ===========================================================================
*/
