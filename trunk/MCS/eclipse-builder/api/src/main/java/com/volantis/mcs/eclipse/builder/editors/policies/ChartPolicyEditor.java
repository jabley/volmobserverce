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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.eclipse.builder.editors.common.ChartTypeLabelProvider;
import com.volantis.mcs.eclipse.builder.editors.common.ComboDescriptor;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.VariantType;
import org.eclipse.jface.viewers.ILabelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Editor for chart policies.
 */
public class ChartPolicyEditor extends VariablePolicyEditor {
    // Javadoc inherited
    protected Map getComboDescriptors() {
        Map comboDescriptors = new HashMap();
        List chartTypeList = new ArrayList();
        chartTypeList.add(ChartType.BAR);
        chartTypeList.add(ChartType.COLUMN);
        chartTypeList.add(ChartType.LEGEND);
        chartTypeList.add(ChartType.LINE);
        chartTypeList.add(ChartType.PIE);

        ILabelProvider labelProvider = new ChartTypeLabelProvider();

        ComboDescriptor chartTypes = new ComboDescriptor(chartTypeList, labelProvider);

        comboDescriptors.put(PolicyModel.CHART_TYPE, chartTypes);

        return comboDescriptors;
    }

    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return VariablePolicyType.CHART;
    }

    // Javadoc inherited
    protected VariantType getDefaultVariantType() {
        return VariantType.CHART;
    }
}
