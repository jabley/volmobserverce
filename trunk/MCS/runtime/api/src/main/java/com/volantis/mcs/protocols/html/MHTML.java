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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/MHTML.java,v 1.4 2003/01/29 12:03:16 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Oct-02    Chris W         VBM:2002092306 - Created
 * 10-Jan-03    Phil W-S        VBM:2002110402 - This protocol does not support
 *                              format optimization.
 * 29-Jan-03    Steve           VBM:2003010710  Derive from HTML3.2 instead
 *                              of XHTMLFull.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * This class provide support for the MHTML proposed standard. MHTML is only a
 * proposed standard because IETF requires two independent and co-working
 * implementations of every feature of a standard before progressing it to
 * DRAFT.<p>
 *
 * The document referred to below (produced in May 2000) appears to be the
 *     <pre>http://dsv.su.se/jpalme/ietf/mhtml-test/mhtml-status.txt</pre>
 * only one that has been written about MHTML compliance in messaging user
 * agents. Microsoft Outlook Express 5, Netscape Messenger 4.7, Qualcomm
 * Eudora Pro 4.2, Hotmail, Yahoo! Mail and a few other minor email clients
 * (e.g. Pine) were tested. MHTML compliance was patchy, with Outlook Express
 * coming out on top! As nothing else has been written since then I think it
 * is safe to assume that MHTML compliance is not top of browser vendor's
 * agendas. This class is written on the basis of whatever works rather than
 * close adherence to the spec.<p>
 *
 * RFC 2557 which defines MHTML specifies the use of HTML 2.0. The closest
 * protocol to HTML 2.0 that Volantis supports is XHTMLFull so we extend
 * that class.<p>
 *
 * Whilst the MHTML spec allows for all HTML 2.0 features, it is suggested that
 * style sheets, tables or background colors or images should not be used.
 *     <pre>http://dsv.su.se/jpalme/ietf/web-email.html</pre>
 * However informal testing with Microsoft Outlook 2000, Microsoft Outlook
 * Express 5.5, Mozilla 1.0.1, Evolution 1.0.8, Hotmail, Yahoo! Mail suggests
 * that tables are supported and that stylesheets are only supported in
 * Outlook and Outlook Express.<p>
 *
 * As this protocol must work on all email clients, it has been decided to
 * implement tables but not Mariner themes.
 */
public class MHTML extends HTMLVersion3_2 {

    /**
     * Constructor for MHTML.
     */
    public MHTML(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);

        // Only MS Outlook and MS Outlook Express support stylesheets.
        supportsExternalStyleSheets = false;
        supportsInlineStyles = false;

        // We don't have an xml namespace
        xmlNamespace = null;

        // Format optimization is not supported (nested table reduction)
        supportsFormatOptimization = false;
    }

    // Javadoc inherited.
    protected void doProtocolString(Document document) {
        // An XML DOCTYPE is not specified in RFC 2557.
    }

    // Javadoc inherited
    protected void addCoreAttributes(Element element,
                                     MCSAttributes attributes,
                                     boolean title) {
        String value;
        // Calls to add id and class attributes removed.
        if (title && (value = attributes.getTitle()) != null) {
            element.setAttribute("title", value);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 09-Dec-03	2180/1	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2162/1	mat	VBM:2003120504 Add style attributes to format cells

 ===========================================================================
*/
