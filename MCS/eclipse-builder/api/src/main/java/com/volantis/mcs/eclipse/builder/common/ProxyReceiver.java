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
package com.volantis.mcs.eclipse.builder.common;

import com.volantis.mcs.interaction.Proxy;

/**
 * General purpose interface for accepting a new value for an interaction model
 * proxy object.
 */
public interface ProxyReceiver {
    /**
     * Passes in the proxy.
     *
     * @param proxy The proxy
     */
    public void setProxy(Proxy proxy);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10589/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 ===========================================================================
*/
