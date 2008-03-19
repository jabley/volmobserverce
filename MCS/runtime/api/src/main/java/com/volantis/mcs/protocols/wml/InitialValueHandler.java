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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/InitialValueHandler.java,v 1.4 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- ----------------------------------------------- * 20-Jan-03    Doug            VBM:2002120213 - Created. Class that
 *                              calculates the String to use for the "ivalue"
 *                              for WML select controls
 * 27-Jan-03    Doug            VBM:2003012801 - Ensured that 
 *                              the getInitialValue() method of the inner
 *                              MultiSelectInitialValueVisitor class returns
 *                              null if no options are selected. 
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.Option;
import com.volantis.mcs.protocols.OptionVisitor;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.utilities.StringConvertor;

import java.util.List;

/**
 * This class will traverse a list of Option objects and calculate the
 * set of initial values
 */
public final class InitialValueHandler {


    private final SingleSelectInitialValueVisitor singleSelectVisitor =
            new SingleSelectInitialValueVisitor();

    private final MultiSelectInitialValueVisitor multiSelectVisitor =
            new MultiSelectInitialValueVisitor();

    public String getInitialValue(XFSelectAttributes attributes)
            throws ProtocolException {
        if (attributes.isMultiple()) {
            return multiSelectVisitor.getInitialValue(attributes);
        } else {
            return singleSelectVisitor.getInitialValue(attributes);
        }
    }

    private class SingleSelectInitialValueVisitor implements OptionVisitor {

        private int optionIndex;

        private boolean valueFound;

        public String getInitialValue(XFSelectAttributes attributes)
                throws ProtocolException {

            optionIndex = 0;
            valueFound = false;

            visitOptions(attributes.getOptions(), null);

            return (valueFound) ? StringConvertor.valueOf(optionIndex) : null;
        }

        private void visitOptions(List options, Object object)
                throws ProtocolException {
            Option option;

            for (int i = 0; i < options.size() && !valueFound; i++) {
                option = (Option) options.get(i);
                option.visit(this, object);
            }
        }

        public void visit(
                SelectOption selectOption,
                Object object) {

            optionIndex++;
            if (selectOption.isSelected()) {
                // this option is the one that should be selected
                valueFound = true;
            }
        }

        public void visit(
                SelectOptionGroup selectOptionGroup,
                Object object) throws ProtocolException {
            // Option Groups do not count as part of the indexing. However
            // nested Options/Option Groups need to be processed.
            visitOptions(selectOptionGroup.getSelectOptionList(), object);
        }
    }

    /**
     * Class that calculates the ivalue for a WML select element
     */
    private class MultiSelectInitialValueVisitor implements OptionVisitor {

        private int optionIndex;

        /**
         * return the initial value for a multiple select control
         *
         * @param attributes the XFSelectAttributes
         * @return the initial value as a colon seperated String or null of no
         *         Options are selected.
         */
        public String getInitialValue(XFSelectAttributes attributes)
                throws ProtocolException {

            optionIndex = 0;

            StringBuffer valueBuffer = new StringBuffer();

            visitOptions(attributes.getOptions(), valueBuffer);

            return (0 == valueBuffer.length()) ? null : valueBuffer.toString();
        }

        private void visitOptions(List options, Object object)
                throws ProtocolException {
            Option option;
            for (int i = 0; i < options.size(); i++) {
                option = (Option) options.get(i);
                option.visit(this, object);
            }
        }

        public void visit(
                SelectOption selectOption,
                Object object) {
            optionIndex++;
            if (selectOption.isSelected()) {
                StringBuffer sb = (StringBuffer) object;
                if (sb.length() > 0) {
                    // this is not the first option so prepend a colon
                    sb.append(';');
                }
                // append the index of the selected option
                sb.append(optionIndex);
            }
        }

        public void visit(
                SelectOptionGroup selectOptionGroup,
                Object object) throws ProtocolException {
            // Option Groups do not count as part of the indexing. However
            // nested Options/Option Groups need to be processed.
            visitOptions(selectOptionGroup.getSelectOptionList(), object);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
