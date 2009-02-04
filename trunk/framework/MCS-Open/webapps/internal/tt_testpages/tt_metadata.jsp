<!--
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
-->
<%@ include file="VolantisNoError-mcs.jsp" %>   
<%@ page import="com.volantis.mcs.imdapi.*, com.volantis.mcs.context.*, com.volantis.mcs.servlet.*" %>
<vt:canvas layoutName="error" pageTitle="Asset Metadata Test">
<vt:pane name="error">
<%
	MarinerRequestContext mrc = MarinerServletRequestContext.getCurrent(request);
	ScriptComponentElement sce = new ScriptComponentElement();
	ScriptComponentAttributes sca = new ScriptComponentAttributes();
	sca.setName("tester");
	sce.elementStart(mrc, sca);
	ScriptAssetElement sae = new ScriptAssetElement();
	ScriptAssetAttributes saa = new ScriptAssetAttributes();
	saa.setProgrammingLanguage("JavaScript");
	saa.setValue("Test javascript");
	saa.setDeviceName("PC");
	saa.setValueType("2");
	sae.elementStart(mrc, saa);
	sae.elementEnd(mrc, saa);
	sce.elementEnd(mrc, sca);
	TextComponentElement tce = new TextComponentElement();
	TextAssetElement tae = new TextAssetElement();
	TextComponentAttributes tca = new TextComponentAttributes();
	TextAssetAttributes taa = new TextAssetAttributes();
	tca.setName("testtext");
	tca.setFallbackTextComponentName(null);
	tce.elementStart(mrc, tca);
	taa.setDeviceName("PC");
	taa.setLanguage("-");
	taa.setValueType("2");
	taa.setEncoding("1");
	taa.setValue("This is some test text");
	tae.elementStart(mrc, taa);
	tae.elementEnd(mrc, taa);
	tce.elementEnd(mrc, tca);

	LinkComponentElement lce = new LinkComponentElement();
	LinkAssetElement lae = new LinkAssetElement();
	LinkComponentAttributes lca = new LinkComponentAttributes();
	LinkAssetAttributes laa = new LinkAssetAttributes();
	lca.setName("Linker");
	lca.setFallbackTextComponentName("FragmentOn");
	lce.elementStart(mrc, lca);
	laa.setDeviceName("PC");
	laa.setValue("www.testlink.com");
	lae.elementStart(mrc, laa);
	lae.elementEnd(mrc, laa);
	lce.elementEnd(mrc, lca);

	lca.setName("Linker1");
	lca.setFallbackTextComponentName("FragmentOn");
	lce.elementStart(mrc, lca);
	laa.setDeviceName("Tablet");
	laa.setValue("www.testlink.com");
	lae.elementStart(mrc, laa);
	lae.elementEnd(mrc, laa);
	lce.elementEnd(mrc, lca);

	ImageComponentElement ice = new ImageComponentElement();
	ImageComponentAttributes ica = new ImageComponentAttributes();
	GenericImageAssetElement die = new GenericImageAssetElement();
	GenericImageAssetAttributes dia = new GenericImageAssetAttributes();
	ica.setName("matimage");
	ica.setFallbackTextComponentName("testtext");
	ica.setFallbackImageComponentName("stars");
	ice.elementStart(mrc, ica);

	//dia.setDeviceName("Tablet");
	dia.setPixelsX("220");
	dia.setPixelsY("155");
	dia.setPixelDepth("24");
	dia.setRendering("2");
	dia.setEncoding("2");
	dia.setValue("cheetah.gif");
	dia.setWidthHint("0");
	die.elementStart(mrc, dia);
	die.elementEnd(mrc, dia);
	
	ice.elementEnd(mrc, ica);

%>
	
<vt:p>
	Should display cheetah image.
	<vt:img src="matimage" />
</vt:p>
<vt:p>In the page source, you should see a script tag with a body of "Test javascript"</vt:p>
<vt:script src="{tester}"/>
<vt:a href="{Linker}">I am a link, I should point to www.testlink.com</vt:a>
<vt:p>The following link should just be the text from the non-IMD text
component "FragmentOn", as there is no asset for the PC for the link component
"Linker1"</vt:p>
<vt:a href="{Linker1}">I am a link, I should point to www.testlink.com</vt:a>

</vt:pane>

</vt:canvas>
