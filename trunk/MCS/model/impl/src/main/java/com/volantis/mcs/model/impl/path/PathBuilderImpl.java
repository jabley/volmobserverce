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
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.ArrayList;
import java.util.List;

public class PathBuilderImpl
        implements PathBuilder {

    private List steps;

    private Step addStep(Step step) {
        if (step == null) {
            throw new IllegalArgumentException("step cannot be null");
        }
        if (steps == null) {
            steps = new ArrayList();
        }
        steps.add(step);
        return step;
    }

    public Path getPath() {
        return new PathImpl(steps);
    }

    public Step addIndexedStep(int index) {
        return addStep(new IndexedStepImpl(index));
    }

    public Step addPropertyStep(PropertyIdentifier property) {
        return addStep(new PropertyStepImpl(property.getName()));
    }

    public Step addPropertyStep(String property) {
        return addStep(new PropertyStepImpl(property));
    }

    public Step removeStep() {
        if (steps == null || steps.isEmpty()) {
            throw new IllegalStateException("Cannot remove step as list is empty");
        }

        Step step = (Step) steps.remove(steps.size() - 1);
        return step;
    }

    public void clear() {
        if (steps != null) {
            steps.clear();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 ===========================================================================
*/
