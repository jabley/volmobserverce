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

package com.volantis.mcs.interaction.impl.validation;

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;

public class ProxyDiagnosticImpl
        implements ProxyDiagnostic {

    private final DiagnosticLevel level;

    private final Proxy proxy;

    private final I18NMessage message;

    /**
     * Initialise.
     *
     * @param level
     * @param proxy
     * @param message
     */
    public ProxyDiagnosticImpl(
            DiagnosticLevel level, Proxy proxy, I18NMessage message) {
        this.level = level;
        this.proxy = proxy;
        this.message = message;
    }

    // Javadoc inherited.
    public DiagnosticLevel getLevel() {
        return level;
    }

    // Javadoc inherited.
    public Proxy getProxy() {
        return proxy;
    }

    // Javadoc inherited.
    public I18NMessage getMessage() {
        return message;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ProxyDiagnostic)) {
            return false;
        }

        ProxyDiagnostic other = (ProxyDiagnostic) obj;
        if (getLevel() != other.getLevel()) {
            return false;
        }
        if (getProxy() != other.getProxy()) {
            return false;
        }
        if (!getMessage().equals(other.getMessage())) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result = level.hashCode();
        result = result * 37 + proxy.hashCode();
        result = result * 37 + message.hashCode();
        return result;
    }

    public String toString() {
        return level.toString() + " " + proxy.getPathAsString() + " " +
                message;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
