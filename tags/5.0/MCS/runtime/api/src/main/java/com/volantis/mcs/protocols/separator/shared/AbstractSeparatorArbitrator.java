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

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.separator.ArbitratorDecision;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

public abstract class AbstractSeparatorArbitrator
        implements SeparatorArbitrator {

    public void decide(SeparatorManager manager,
                       ArbitratorDecision decision) {

        SeparatedContent triggeringContent = decision.getTriggeringContent();
        SeparatorRenderer triggeringSeparator = decision.getTriggeringSeparator();

        // The trigger is determined as follows.
        // 1) If a triggering separator was specified then it was the trigger.
        // 2) If some triggering content was specified then it was the trigger.
        // 3) Otherwise it was as a result of a flush.
        if (triggeringSeparator != null) {
            // Triggered by separator.
            separatorTriggered(decision);
        } else if (triggeringContent != null) {
            // Triggered by content.
            contentTriggered(decision);
        } else {
            // Triggered by flush.
            flushTriggered(decision);
        }
    }

    protected abstract void separatorTriggered(ArbitratorDecision decision);

    protected abstract void flushTriggered(ArbitratorDecision decision);

    protected abstract void contentTriggered(ArbitratorDecision decision);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
