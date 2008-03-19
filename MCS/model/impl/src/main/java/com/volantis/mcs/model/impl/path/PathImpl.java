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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.model.impl.path;

import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PathImpl
        implements Path {

    private final List steps;

    public PathImpl(List steps) {
        if (steps == null) {
            this.steps = Collections.EMPTY_LIST;
        } else {
            this.steps = new ArrayList(steps);
        }
    }

    public void addStep(Step step) {
        if (step == null) {
            throw new IllegalArgumentException("step cannot be null");
        }
        steps.add(step);
    }

    public String getAsString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < steps.size(); i++) {
            InternalStep step = (InternalStep) steps.get(i);
            buffer.append("/");
            buffer.append(step.getAsString());
        }
        return buffer.toString();
    }

    public int getStepCount() {
        return steps.size();
    }

    public Step getStep(int index) {
        return (Step) steps.get(index);
    }

    public String toString() {
        return getAsString();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/4	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Added path support

 ===========================================================================
*/
