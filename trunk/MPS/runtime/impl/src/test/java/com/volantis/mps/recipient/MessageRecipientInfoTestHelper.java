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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

/**
 * A utility implementation of {@link MessageRecipientInfo} that is used for
 * the various recipient test cases.  These values are expected by the test
 * cases so can be hard coded without problem.
 */
public class MessageRecipientInfoTestHelper implements MessageRecipientInfo {

    // JavaDoc inherited
    public String resolveChannelName(MessageRecipient recipient) {
        return "smtp";
    }

    // JavaDoc inherited
    public String resolveDeviceName(MessageRecipient recipient) {
        return "Outlook";
    }

    public String resolveCharacterEncoding(MessageRecipient recipient) {
        return null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 04-May-05	651/1	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 24-Sep-04	140/1	claire	VBM:2004070704 Fixing testcases and replacing some hypersonic usage with stubs

 ===========================================================================
*/
