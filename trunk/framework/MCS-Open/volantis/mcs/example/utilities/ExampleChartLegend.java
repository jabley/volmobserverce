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
/*
 *
 * $Header: /src/voyager/volantis/mcs/example/utilities/ExampleChartLegend.java,v 1.1 2001/01/25 15:25:03 aboyd Exp $
 *
 * (c) Volantis Systems Ltd 2000.
 */
package volantis.mcs.example.utilities;

import com.volantis.mcs.utilities.ChartData;
import com.volantis.mcs.utilities.ChartValues;

public class ExampleChartLegend implements ChartData {
 private static String mark = "(c) Volantis Systems Ltd 2001. ";
    ChartValues labels = new ChartValues("one, two");

    public ChartValues getData() {
	return null;
    }

    public ChartValues getLabels() {
	return labels;
    }
}
