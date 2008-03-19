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
import com.volantis.mcs.protocols.TimedRefreshInfo;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.xml.namespace.ImmutableExpandedName;

import java.net.URISyntaxException;

/**
 * Content processor for Refresh contents. Stores the result in a
 * TimedRefreshInfo object.
 */
public class RefreshContentProcessor  extends StringContentProcessor {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer LOCALIZER =
        LocalizationFactory.createMessageLocalizer(RefreshContentProcessor.class);

    public static final ImmutableExpandedName EXPANDED_NAME_REFRESH =
        new ImmutableExpandedName(XDIMESchemata.XDIME2_MCS_NAMESPACE,
                                  TimedRefreshInfo.NAME);

    public static final DataType REFRESH_TYPE = new DataType(EXPANDED_NAME_REFRESH);

    // javadoc inherited
    public DataType getType() {
        return RefreshContentProcessor.REFRESH_TYPE;
    }

    // javadoc inherited
    public Object getResult() throws XDIMEException {
        TimedRefreshInfo result = null;
        try {
            final String contents = super.getResult().toString();
            result= new TimedRefreshInfo(contents);
        } catch (NumberFormatException e) {
            // If string didn't parse to long property, throw an exception
            // reusing existing localised message, saying that Period was
            // expected, but String was encountered.
            throw new XDIMEException(
                    LOCALIZER.format("invalid-meta-content-type", new Object[]{
                        TimedRefreshInfo.class.getName(),
                        String.class.getName()}), e);
        } catch (ProtocolException e) {
            throw new XDIMEException(
                    LOCALIZER.format("invalid-meta-content-type",new Object[]{
                        TimedRefreshInfo.class.getName(),
                        String.class.getName()}), e);
        }
        return result;
    }



}
