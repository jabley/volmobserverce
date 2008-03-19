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

package com.volantis.mcs.xdime.cp.xslt.formsinstancedata;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the core attribute of the xforms-instance-data-module.xsl
 * stylesheet.
 */
public class XFormsInstanceDataModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that initial values are set correctly on xforms controls when a
     * model is specified.
     */
    public void testInitialValues() throws Exception {
        doTransform("Form's instance data transformations failed",
                getInputSourceForClassResource("xforms-instance-data-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("xforms-instance-data-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " " + XDIMECPConstants.XFORMS_XMLNS +
                " " + XDIMECPConstants.SI_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE +
                " ../../../../../../../../../architecture/build/xml-schema/xdime-cp/build/xdime-cp-mcs.xsd\"" +
                ">" +
                "<html><head><title>No title</title>" +
                "<xf:model id=\"model1\">" +
                "<xf:submission action=\"processit\" id=\"form1\" method=\"get\"/>" +
                "<xf:instance>" +
                "<si:instance>" +
                "<si:item name=\"myText\">Initial text</si:item>" +
                "<si:item name=\"myTextArea\">Initial textarea</si:item>" +
                "<si:item name=\"myhiddenfield\">This is hidden field-like data</si:item>" +
                "<si:item name=\"paymethod\">cccc</si:item>" +
                "<si:item name=\"novaluematch\">spanner</si:item>" +
                "<si:item name=\"choices\">cash99,ccc,yyy,zzz</si:item>" +
                "<si:item name=\"mysecret\">shhhh!</si:item>" +
                "</si:instance>" +
                "</xf:instance>" +
                "</xf:model>" +
                "</head>" +
                "<body>" +
                "<xf:input ref=\"myText\">" +
                "<xf:label>My input label</xf:label>" +
                "</xf:input>" +

                "<xf:textarea class=\"area\" ref=\"myTextArea\">" +
                "<xf:label>XDIME CP Area</xf:label>" +
                "</xf:textarea>" +

                "<xf:secret ref=\"mysecret\">" +
                "<xf:label>Secret label</xf:label>" +
                "</xf:secret>" +

                "<xf:select1 ref=\"paymethod\">" +
                "<xf:label>Select Payment Method:</xf:label>" +
                "<xf:item>" +
                "<xf:label>Cash</xf:label>" +
                "<xf:value>cash</xf:value>" +
                "</xf:item>" +
                "<xf:item>" +
                "<xf:label>Credit</xf:label>" +
                "<xf:value>cc</xf:value>" +
                "</xf:item>" +
                "</xf:select1>" +

                "<xf:select1 ref=\"novaluematch\">" +
                "<xf:label>Select Payment Method:</xf:label>" +
                "<xf:item>" +
                "<xf:label>Cash</xf:label>" +
                "<xf:value>nocash</xf:value>" +
                "</xf:item>" +
                "<xf:item>" +
                "<xf:label>Credit</xf:label>" +
                "<xf:value>nocc</xf:value>" +
                "</xf:item>" +
                "</xf:select1>" +

                "<xf:select ref=\"choices\">" +
                "<xf:label>Select Payment Method:</xf:label>" +
                "<xf:item>" +
                "<xf:label>Cash</xf:label>" +
                "<xf:value>cash</xf:value>" +
                "</xf:item>" +
                "<xf:item>" +
                "<xf:label>Credit</xf:label>" +
                "<xf:value>yyy</xf:value>" +
                "</xf:item>" +
                "</xf:select>" +

                "<xf:submit class=\"abc\" submission=\"form1\">" +
                "<xf:label>Press Me</xf:label>" +
                "</xf:submit>" +
                "</body></html>" +
                "</mcs:unit>"),

                getInputSourceForString(
                        "<unit><region name=\"Region0\"><canvas type=\"inclusion\" layoutName=\"/xdimecp/pane.mlyt\">" +
                "<xfform name=\"form\" action=\"processit\" method=\"get\">" +
                "<xftextinput initial=\"Initial text\" entryPane=\"myEPane\" name=\"myText\" caption=\"My input label\" />" +
                "<xftextinput initial=\"Initial textarea\" entryPane=\"myEPane\" class=\"area\" name=\"myTextArea\" caption=\"XDIME CP Area\" />" +
                "<xftextinput initial=\"shhhh!\" type=\"password\" name=\"mysecret\" caption=\"Secret label\"/>" +
                "<xfsiselect initial=\"cash\" entryPane=\"myEPane\" name=\"paymethod\" caption=\"Select Payment Method:\">" +
                "<xfoption caption=\"Cash\" value=\"cash\" /><xfoption caption=\"Credit\" value=\"cc\" />" +
                "</xfsiselect>" +
                "<xfsiselect initial=\"cash\" entryPane=\"myEPane\" name=\"novaluematch\" caption=\"Select Payment Method:\">" +
                "<xfoption caption=\"Cash\" value=\"nocash\" /><xfoption caption=\"Credit\" value=\"nocc\" />" +
                "</xfsiselect>" +
                "<xfmuselect entryPane=\"myEPane\" name=\"choices\" caption=\"Select Payment Method:\">" +
                "<xfoption caption=\"Cash\" value=\"cash\" /><xfoption selected=\"true\" caption=\"Credit\" value=\"yyy\" />" +
                "</xfmuselect>" +
                "<xfaction type=\"submit\" class=\"abc\" caption=\"Press Me\" />" +
                "<xfimplicit name=\"myhiddenfield\" value=\"This is hidden field-like data\" />" +
                "</xfform>" +
                "</canvas></region></unit>"
                ));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Sep-04	5380/23	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and reverted to interim si namespace

 27-Aug-04	5310/1	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 ===========================================================================
*/
