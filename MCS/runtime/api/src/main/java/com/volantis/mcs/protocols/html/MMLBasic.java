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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MMLBasic.java,v 1.19 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Mar-02    Steve           VBM:2002021119 - Basic J-Sky MML protocol
 * 19-Mar-02    Steve           VBM:2002021119 - Added inputMode attribute
 * 25-Mar-02    Ian             VBM:2002031203 - Changed log4j to pass string
 *                              instead of class.
 * 26-Mar-02    Allan           VBM:2002022007 - generateContentTree() now
 *                              takes an Object.generateContents() returns an
 *                              Object.
 * 25-Apr-02    Paul            VBM:2002042202 - Added import for
 *                              HTMLActionFieldHandler.
 * 28-Apr-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 07-May-02    Adrian          VBM:2002042302 - Removed method getContent..
 *                              ..Extension() as the cached file extension is
 *                              defined in the device policies.
 * 10-May-02    Adrian          VBM:2002040808 - Check if theme formActionStyle
 *                              has the value "link"
 * 05-Jun-02    Adrian          VBM:2002021103 - Open KEEPTOGETHER_ELEMENT on
 *                              openDiv and close on closeDiv
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 19-Jul-02    Sumit           VBM:2002021119 - Minor protocol corrections
 * 20-Aug-02    Adrian          VBM:2002081316 - Updated doImage to output
 *                              altText to OutputBuffer if the image source is
 *                              null.
 * 10-Sep-02    Steve           VBM:2002040809 - Get pane style from 
 *                              VolantisProtocol when checking if the pane 
 *                              needs a table wrapper.
 * 18-Nov-02    Geoff           VBM:2002111504 - Refactored to use new fallback
 *                              methods in the page context, clean up unused
 *                              imports and locals, and javadoc.
 * 20-Nov-02    Geoff           VBM:2002111504 - commented out unused methods
 *                              (generateContents, generateContentTree).
 * 15-Jan-03    Byron           VBM:2002111812 - Set preprocess to be true in
 *                              constructor. Uncommented generateContentTree
 *                              and generateContents methods.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 01-May-03    Mat             VBM:2003042912 - Changed
 *                              getHorizontalMenuItemSeparator() to return 
 *                              NBSP literal.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * MMLBasic now extends the HTML_iMode protocol. This is because MML gateways
 * now take HTML and convert them to MML on the fly, the MML is then sent to
 * the device to display. In testing, MML phones seem to have a problem with 
 * entities in that they do not recognise codes, they do recognise the symbolic 
 * names for the entities however. For this reason we convert the commonly used
 * codes into their entity names using the 'quoteTable' below. This is basically 
 * the standard quote table with nbsp added in. MML does not support style 
 * sheets at the time of writing, tables are supported.   
 */
public class MMLBasic extends HTML_iMode {
    
    /**
     * Create an MMLBasic protocol. 
     */
    public MMLBasic(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {
        super(protocolSupportFactory, protocolConfiguration);
        
        quoteTable[0x22] = "&quot;";        
        quoteTable[0x26] = "&amp;";        
        quoteTable[0x3c] = "&lt;";        
        quoteTable[0x3e] = "&gt;";        
        quoteTable[0xa0] = "&nbsp;";        
        quoteTable[0xa9] = "&copy";        
        quoteTable[0xae] = "&reg;";    
        
        supportsExternalStyleSheets = false;
        supportsInlineStyles = false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 23-Apr-04	3272/2	philws	VBM:2004021117 Fix merge issues on MMLBasic

 22-Apr-04	3973/4	steve	VBM:2004042002 Encode #nbsp;

 21-Apr-04	3973/2	steve	VBM:2004042002 Encode #nbsp;

 16-Apr-04	3834/3	steve	VBM:2004041306 deprecate StringProtocol fragmentation

 14-Apr-04	3834/1	steve	VBM:2004041306 Converted MMLBasic to a DOMProtocol derived protocol

 15-Mar-04  3422/1  geoff   VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04  3403/1  geoff   VBM:2004030907 name attribute not rendered on a tag

 12-Feb-04  2789/2  tony    VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04  2958/1  philws  VBM:2004012715 Add protocol.content.type device policy

 03-Nov-03  1760/1  philws  VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03  1751/1  philws  VBM:2003031710 Permit image alt text to be component reference

 17-Sep-03  1412/2  geoff   VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 10-Sep-03  1301/2  byron   VBM:2003082107 Support Openwave GUI Browser extensions

 21-Aug-03  1240/1  chrisw  VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03  1219/4  chrisw  VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 20-Aug-03  1152/1  chrisw  VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 ===========================================================================
 */

