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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2.meta.datatype;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.shared.net.http.HttpDateParser;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.xml.namespace.ImmutableExpandedName;

import java.util.Date;

/**
 * Content processor for Text contents. Stores the result in a Time object.
 */
public class DateTimeContentProcessor extends StringContentProcessor {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer LOCALIZER =
        LocalizationFactory.createMessageLocalizer(
            DateTimeContentProcessor.class);

    public static final ImmutableExpandedName EXPANDED_NAME_DATE_TIME =
        new ImmutableExpandedName(XDIMESchemata.XDIME2_MCS_NAMESPACE, "dateTime");

    public static final DataType DATE_TIME_TYPE =
        new DataType(EXPANDED_NAME_DATE_TIME);


    // javadoc inherited
    public DataType getType() {
        return DATE_TIME_TYPE;
    }

    // javadoc inherited
    public Object getResult() throws XDIMEException {

        final String dateStr = super.getResult().toString();
        Date date = HttpDateParser.parse(dateStr);
        if (date == null) {
            // If string didn't parse to date property, throw an exception
            // reusing existing localised message, saying that Date was
            // expected, but String was encountered.
            throw new XDIMEException(DateTimeContentProcessor.LOCALIZER.format(
                "invalid-meta-content-type", new Object[] {
                    Date.class.getName(), String.class.getName() }));
        }
        return Time.inMilliSeconds(date.getTime());
    }
}
