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

package com.volantis.mcs.interaction;

import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.descriptor.TypeDescriptor;

import java.util.List;

/**
 * A proxy pre
 *
 * @mock.generate
 */
public interface Proxy {

    /**
     * Add an event listener.
     *
     * @param listener The listener to add.
     * @param deep     True if the listener wants to hear about deep events and
     *                 false if it does not.
     */
    void addListener(InteractionEventListener listener, boolean deep);

    /**
     * Remove an interaction listener.
     *
     * @param listener The listener to remove.
     */
    void removeListener(InteractionEventListener listener);

    /**
     * Set the model object.
     *
     * @param modelObject The new model object.
     * @return The old model object.
     */
    Object setModelObject(Object modelObject);

    /**
     * Sets the model object.
     *
     * <p>The ability to force explicit setting of the model is provided so
     * that this can be used in cases where the 'new' model object may be the
     * same as the existing model object, but externally modified (this can
     * happen after merges of policies within a collaborative working GUI, for
     * example).</p>
     *
     * @param newModelObject The new model object
     * @param force If true, the model will be explicitly set even if it is
     *              the same as the current model object.
     * @return The old model object
     */
    Object setModelObject(Object newModelObject, boolean force);

    /**
     * Get the model object associated with this proxy.
     *
     * @return The model object associated with the proxy.
     */
    Object getModelObject();

    /**
     * Prepare an operation to change the model object associated with this
     * proxy.
     *
     * @param newModelObject The model object to associate.
     * @return The newly created operation.
     * @see Operation
     */
    Operation prepareSetModelObjectOperation(Object newModelObject);

    /**
     * Get the diagnostics associated with this proxy.
     *
     * <p>The returned list contains {@link ProxyDiagnostic} objects.</p>
     *
     * @return The list of diagnostics, this will be null if there are no
     * diagnostics associated with this proxy.
     */
    List getDiagnostics();

    /**
     * Add a listener for changes to the diagnostics associated with this
     * proxy.
     *
     * <p>As diagnostics are added to a proxy and all its ancestors a
     * diagnostic listener will be invoked if diagnostics are associated with
     * it, or any of its descendants.</p>
     *
     * @param listener The listener to register.
     */
    void addDiagnosticListener(DiagnosticListener listener);

    /**
     * Remove a listener for changes to the diagnostics associated with this
     * proxy.
     *
     * <p>As diagnostics are added to a proxy and all its ancestors a
     * diagnostic listener will be invoked if diagnostics are associated with
     * it, or any of its descendants.</p>
     *
     * @param listener The listener to unregister.
     */
    void removeDiagnosticListener(DiagnosticListener listener);

    /**
     * Validate the proxy.
     */
    void validate();

    /**
     * Copy the underlying model object.
     *
     * <p>If there is no underlying model object then this method will return
     * null.</p>
     *
     * @return A copy of the underlying model object.
     */
    Object copyModelObject();

    /**
     * Get a description of the proxy as a path from the root.
     *
     * <p>The value returned by this is for debugging purposes only and must
     * not be relied upon in anyway.</p>
     *
     * @return The path from the root.
     */
    String getPathAsString();

    /**
     * Get the proxy given the path from this proxy.
     *
     * @param path The path from this proxy.
     *
     * @return The proxies.
     */
    Proxy getProxy(Path path);

    /**
     * Get the proxy at the specified path, or the closest enclosing one.
     *
     * @param path The path from the root.
     *
     * @return The proxy.
     */
    Proxy getEnclosingProxy(Path path);

    /**
     * Get all the proxies on the path from this proxy.
     *
     * <p>They are returned in order from this proxy.</p>
     *
     * @param path The path from this proxy.
     * @return All the proxies on the path from this proxy.
     */
    List getProxies(Path path);

    /**
     * Get the type descriptor for the proxy.
     *
     * @return The type descriptor.
     */
    TypeDescriptor getTypeDescriptor();

    /**
     * Get the parent proxy.
     *
     * @return The parent proxy.
     */
    ParentProxy getParentProxy();

    /**
     * Get the path from the root to this proxy.
     *
     * @return The path from the root.
     */
    Path getPathFromRoot();

    /**
     * Get the current read/write state.
     *
     * @return The current read/write state
     */
    ReadWriteState getReadWriteState();

    /**
     * Set the read/write state.
     *
     * @param newState The new read/write state
     */
    void setReadWriteState(ReadWriteState newState);

    /**
     * Checks whether this proxy is read-only.
     *
     * @return True if the policy is read-only, false otherwise
     */
    boolean isReadOnly();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/14	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/11	pduffin	VBM:2005101811 Added path support

 26-Oct-05	9961/9	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/7	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
