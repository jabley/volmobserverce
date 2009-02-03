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
<%@ page import="com.volantis.mcs.servlet.*, com.volantis.mcs.devices.*, com.volantis.mcs.devices.policy.*, com.volantis.shared.metadata.value.immutable.*, com.volantis.shared.metadata.type.immutable.*, java.util.*,
                 org.jdom.Element,
                 org.jdom.Document,
                 org.jdom.output.XMLOutputter,
                 com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitee,
                 com.volantis.shared.metadata.type.ImmutableMetaDataTypeVisitee,
                 com.volantis.mcs.accessors.xml.jdom.metadata.type.XMLWritingImmutableMetaDataTypeVisitor,
                 com.volantis.mcs.accessors.xml.jdom.metadata.value.XMLWritingImmutableMetaDataValueVisitor,
                 com.volantis.mcs.accessors.xml.jdom.XMLAccessorHelper,
                 java.io.PrintWriter,
                 java.io.StringWriter,
                 org.jdom.xpath.XPath,
                 com.volantis.mcs.accessors.xml.jdom.metadata.MetaDataAccessorHelper,
                 com.volantis.mcs.accessors.xml.jdom.metadata.MetaDataReader,
                 com.volantis.shared.metadata.type.MetaDataType,
                 com.volantis.shared.metadata.value.MetaDataValue,
                 com.volantis.mcs.devices.policy.values.PolicyValue,
                 com.volantis.mcs.devices.policy.types.PolicyType" %>

<%@ include file="MarinerMCSInitialize.jsp" %>

<html>
<body>
<table>
<tr>
<th>Policy Name</th>
<th>Policy Type</th>
<th>Policy Value</th>
<th>Type</th>
<th>Value</th>
<th>Markup</th>
<th>Read Type</th>
<th>Read Value</th>
<th>Read Type Matches</th>
<th>Read Value Matches</th>
</tr>
<%

    // MarinerServletRequestContext mrc = new MarinerServletRequestContext(config, request, response);
try{
    MarinerServletApplication mac = MarinerServletApplication.getInstance(application);
    DeviceRepository dr = mac.getRuntimeDeviceRepository();
    Device device = mac.getDevice(request);
    List policyNames = dr.getDevicePolicyNames();
    Element root = new Element("root");
    root.addNamespaceDeclaration(XMLAccessorHelper.METADATA_TYPE_NAMESPACE);
    root.addNamespaceDeclaration(XMLAccessorHelper.METADATA_VALUE_NAMESPACE);
    Document document = new Document(root);

    XMLWritingImmutableMetaDataValueVisitor valueVisitor = new XMLWritingImmutableMetaDataValueVisitor();
    XMLWritingImmutableMetaDataTypeVisitor typeVisitor = new XMLWritingImmutableMetaDataTypeVisitor(valueVisitor);

    final String NULL = "&lt;null&gt;";

    XMLOutputter outputter = new XMLOutputter();
    StringWriter writer;

    int c = 0;
    String typeString;
    String valueString;
    for (Iterator i = policyNames.iterator(); i.hasNext(); c += 1) {
        String name = (String) i.next();
        out.write("<tr>");
        out.write("<td>" + name + "</td>");
        PolicyDescriptor pd = dr.getPolicyDescriptor(name);
        PolicyValue policyValue = device.getRealPolicyValue(name);
        PolicyType policyType = pd.getPolicyType();
        typeString = String.valueOf(policyType);
        valueString = (policyValue == null ? NULL : policyValue.getAsString() + " - " + policyValue);
        out.write("<td>" + typeString + "</td>");
        out.write("<td>" + valueString + "</td>");

        ImmutableMetaDataType type = pd.getPolicyMetaDataType();
        ImmutableMetaDataValue value = device.getPolicyMetaDataValue(name);
        typeString = (type == null ? NULL : type.getClass().getName());
        valueString = (value == null ? NULL : value.getAsString() + " - " + value);
        out.write("<td>" + typeString + "</td>");
        out.write("<td>" + valueString + "</td>");

        Element policy = new Element("policy");
        root.addContent(policy);
        policy.setAttribute("name", name);

        Element typeElement = new Element("type");
        policy.addContent(typeElement);
        if (type != null) {
            ((ImmutableMetaDataTypeVisitee) type).accept(typeVisitor);
        }
        Element valueElement = new Element("value");
        policy.addContent(valueElement);
        if (value != null) {
            ((ImmutableMetaDataValueVisitee) value).accept(valueVisitor);
        }

        out.write("<td>");
        writer = new StringWriter();
        outputter.output(policy, writer);
        StringBuffer outputBuffer = writer.getBuffer();
        String output = outputBuffer.toString();
        for (int j = 0; j < output.length(); j += 1) {
            char ch = output.charAt(j);
            switch(ch) {
                case '<': out.write("&lt;"); break;
                case '&': out.write("&amp;"); break;
                default: out.write(ch); break;
            }
        }
        out.write("</td>");

        // Check that what has been written can be read.
        MetaDataReader reader;
        Element mdtElement = (Element) typeElement.getChildren().get(0);
        reader = MetaDataAccessorHelper.getMetaDataReader(
                mdtElement.getNamespaceURI(), mdtElement.getName());
        MetaDataType readType =
                (MetaDataType) reader.readObject(mdtElement);
        out.write("<td>" + readType + "</td>");

        List valueChildren = valueElement.getChildren();
        MetaDataValue readValue;
        if (valueChildren.size() > 0) {
            Element mdvElement = (Element) valueChildren.get(0);
            reader = MetaDataAccessorHelper.getMetaDataReader(
                    mdvElement.getNamespaceURI(), mdvElement.getName());
                    readValue = (MetaDataValue) reader.readObject(mdvElement);
            out.write("<td>" + readValue.getAsString() + " - " + readValue + "</td>");
        } else {
            readValue = null;
            out.write("<td>" + NULL + "</td>");
        }

        out.write ("<td>");
        if (type == null) {
            out.write(readType == null ? "true" : "false");
        } else {
            out.write(type.equals(readType) ? "true" : "false");
        }
        out.write ("</td><td>");
        if (value == null) {
            out.write(readValue == null ? "true" : "false");
        } else {
            out.write(value.equals(readValue) ? "true" : "false");
        }
        out.write("</td>");

        out.write("</tr>");
    }

    writer = new StringWriter();
    outputter.output(document, writer);
    out.println(writer.getBuffer().toString());
}
    catch (Throwable t) {
    t.printStackTrace(new PrintWriter(out));
}
%>
</table>
</body>
</html>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 ===========================================================================
--%>
