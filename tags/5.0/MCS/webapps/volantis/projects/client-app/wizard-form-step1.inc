<?xml version="1.0" encoding="UTF-8"?>
<xf:group
  xmlns="http://www.w3.org/2002/06/xhtml2"
  xmlns:xf="http://www.w3.org/2002/xforms">

  <div>Please enter your login:</div>
    <xf:input ref="login">
        <xf:label>Login</xf:label>
    </xf:input>
<!--
  This div below needed, because:
  1) MCS renders tables with align="left" attribute
    which causes them to be FLOATED to the left margin
  2) there's no space between form and buttons
-->
  <div class="buttons" style="clear: left;"></div>
</xf:group>


