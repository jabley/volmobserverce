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

import com.volantis.mcs.eclipse.controls.ItemContainer;
import com.volantis.mcs.eclipse.controls.ItemToolTipper;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import org.eclipse.swt.widgets.Item;

import java.util.List;

/**
 */
public class DiagnosticsToolTipper extends ItemToolTipper {
    private ItemMapper mapper;

    /**
     * Construct a new ItemToolTipper for items in the specified
     * ItemContainer.
     *
     * @param container The ItemContainer. Must not be null.
     * @param mapper The mapper from items to proxy objects. Must not be null.
     * @throws IllegalArgumentException If container is null.
     */
    public DiagnosticsToolTipper(ItemContainer container, ItemMapper mapper) {
        super(container);
        this.mapper = mapper;
    }

    protected String getToolTipText(Item item) {
        String toolTip = null;

        Proxy proxy = mapper.dataFromItem(item);

        if (proxy != null) {
            List diagnostics = proxy.getDiagnostics();
            if (diagnostics != null && !diagnostics.isEmpty()) {
                ProxyDiagnostic diag = (ProxyDiagnostic) diagnostics.get(0);
                toolTip = diag.getMessage().toString();
            }
        }

        return toolTip;
    }

    public interface ItemMapper {
        public Proxy dataFromItem(Item item);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 ===========================================================================
*/
