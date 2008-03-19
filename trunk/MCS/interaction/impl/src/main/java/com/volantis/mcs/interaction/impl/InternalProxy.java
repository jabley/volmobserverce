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

package com.volantis.mcs.interaction.impl;

import com.volantis.mcs.interaction.ParentProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.model.path.Step;

import java.util.List;

/**
 * @mock.generate base="Proxy"
 */
public interface InternalProxy
        extends Proxy {

    Object getModelObject(boolean create);

    /**
     * Fire the event to all the listeners, both on this proxy and any
     * listeners on ancestor proxies that have
     *
     * @param event
     */
    void fireEvent(InteractionEvent event);

    /**
     * Get the modification count of this proxy.
     *
     * <p>The modification count is used to determine whether a proxy is in
     * an appropriate state for an operation to be performed. It is a
     * monotonically increasing counter of the number of modifications that
     * have been made to the proxy.</p>
     *
     * @return The current modification count.
     */
    int getModificationCount();

    /**
     * Clear the diagnostics.
     */
    void prepareForValidation();

    void accept(ProxyVisitor visitor);

    void addDiagnostic(ProxyDiagnostic diagnostic);

    void finishValidation();

    Proxy traverse(Step step, boolean enclosing, List proxies);

    /**
     * Detach this proxy from its parent.
     */
    void detach();

    /**
     * Attach this proxy to its new parent.
     */
    void attach(ParentProxy parent);

    /**
     * Sets the model object.
     *
     * @param newModelObject The new model object
     * @param force If true, the model will be explicitly set even if it is
     *              the same as the current model object.
     * @return The old model object
     */
    Object setModelObject(Object newModelObject, boolean force);

    /**
     * Sets the model object.
     *
     * @param newModelObject The new model object
     * @param force If true, the model will be explicitly set even if it is
     *              the same as the current model object.
     * @param  originator Inicats this set is the orinator of the change.
     * @return The old model object
     */
    Object setModelObject(Object newModelObject, boolean force,
                          boolean originator);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/8	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
