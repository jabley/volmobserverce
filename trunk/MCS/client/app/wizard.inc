<?xml version="1.0" encoding="UTF-8"?>
<widget:wizard class="widget" id="myWizard"
  xmlns="http://www.w3.org/2002/06/xhtml2"
  xmlns:widget="http://www.volantis.com/xmlns/2006/05/widget"
  xmlns:xf="http://www.w3.org/2002/xforms">

        <xf:group>
          <div>Please enter your login:</div>
          <table>
            <tr>
              <td>Login</td>
              <td><xf:input ref="login"/></td>
            </tr>
          </table>
          <!-- Remove the next line if MCS stops rendering tables with align="left" attribute
                which causes them to be FLOATED to the left margin -->
          <div style="clear: left;"></div>
        </xf:group>

        <xf:group >
          <div>Please enter your password:</div>
          <table>
            <tr>
              <td>Password</td>
              <td><xf:input ref="password"/></td>
            </tr>
            <tr>
              <td>Repeat</td>
              <td><xf:input ref="rpt"/></td>
            </tr>
            <tr>
              <td></td>
              <td><xf:submit submission="myForm" id="submit-button"><xf:label>OK</xf:label></xf:submit></td>
            </tr>
          </table>
          <!-- Remove the next line when MCS stops rendering tables with align="left" attribute
                which causes them to be FLOATED to the left margin -->
          <div style="clear: left;"></div>
        </xf:group>
</widget:wizard>
