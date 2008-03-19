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

package com.volantis.mcs.interaction.sample.ui;

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.model.validation.Diagnostic;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DiagnosticModel
        extends AbstractTableModel {

    private static String [] columnNames = new String[] {
        "Message"
    };

    private Proxy proxy;

    public DiagnosticModel(Proxy proxy) {
        this.proxy = proxy;

        proxy.addDiagnosticListener(new DiagnosticListener() {
            public void diagnosticsChanged(DiagnosticEvent event) {
                fireTableDataChanged();
            }
        });
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        List diagnostics = proxy.getDiagnostics();
        return diagnostics == null ? 0 : diagnostics.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        List diagnostics = proxy.getDiagnostics();
        ProxyDiagnostic diagnostic = (ProxyDiagnostic) diagnostics.get(rowIndex);
        if (columnIndex == 0) {
            return diagnostic.getMessage();
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 ===========================================================================
*/
