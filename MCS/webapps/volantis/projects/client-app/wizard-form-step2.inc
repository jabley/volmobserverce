<?xml version="1.0" encoding="UTF-8"?>
	<xf:group
		xmlns="http://www.w3.org/2002/06/xhtml2"
		xmlns:xf="http://www.w3.org/2002/xforms">

        <div>Please enter your password:</div>
        <!-- Use styles and layouts to lay these out rather than tables. -->
        <xf:input ref="password">
            <xf:label>Password</xf:label>
        </xf:input>
        <xf:input ref="repeat">
            <xf:label>Repeat</xf:label>
        </xf:input>
        <xf:submit submission="myForm" id="submit-button">
            <xf:label>Finish</xf:label>
        </xf:submit>

    <!--
      This div below needed, because:
      1) MCS renders tables with align="left" attribute
        which causes them to be FLOATED to the left margin
      2) there's no space between form and buttons
    -->
	  <div class="buttons" style="clear: left;"></div>
	</xf:group>