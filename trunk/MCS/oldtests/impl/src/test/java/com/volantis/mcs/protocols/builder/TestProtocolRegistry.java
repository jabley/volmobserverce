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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TestDOMProtocol;
import com.volantis.mcs.protocols.TestVolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.voicexml.TestVoiceXMLVersion1_0;
import com.volantis.mcs.protocols.html.TestHTMLVersion3_2;
import com.volantis.mcs.protocols.html.TestHTMLPalmWCA;
import com.volantis.mcs.protocols.html.TestXHTMLTransitional;
import com.volantis.mcs.protocols.html.XHTMLBasicTestCase;
import com.volantis.mcs.protocols.html.TestXHTMLBasic;
import com.volantis.mcs.protocols.html.TestXHTMLFull;
import com.volantis.mcs.protocols.html.TestXHTMLBasic_MIB2_0;
import com.volantis.mcs.protocols.html.TestXHTMLBasic_MIB2_1;
import com.volantis.mcs.protocols.html.TestXHTMLBasic_Netfront3;
import com.volantis.mcs.protocols.html.TestXHTMLMobile1_0;
import com.volantis.mcs.protocols.html.TestXHTMLMobile1_0_SE_P;
import com.volantis.mcs.protocols.html.TestXHTMLMobile1_0Openwave6;
import com.volantis.mcs.protocols.html.TestHDML_Version3;
import com.volantis.mcs.protocols.html.TestHTML_iMode;
import com.volantis.mcs.protocols.html.TestMMLBasic;
import com.volantis.mcs.protocols.html.TestMHTML;
import com.volantis.mcs.protocols.html.TestHTMLVersion4_0;
import com.volantis.mcs.protocols.html.TestHTMLVersion4_0_IE6;
import com.volantis.mcs.protocols.mms.TestMMS_SMIL_2_0;
import com.volantis.mcs.protocols.text.TestSMS;
import com.volantis.mcs.protocols.wml.TestWMLRoot;
import com.volantis.mcs.protocols.wml.TestWMLVersion1_1;
import com.volantis.mcs.protocols.wml.TestWMLPhoneDotCom;
import com.volantis.mcs.protocols.wml.TestWMLVersion1_3;
import com.volantis.mcs.protocols.wml.TestWapTV5_WMLVersion1_3;
import com.volantis.mcs.protocols.wml.TestWMLEmptyOK1_3;
import com.volantis.mcs.protocols.wml.TestWMLOpenWave1_3;

public class TestProtocolRegistry extends ProtocolRegistry {

    public void register(NamedProtocolBuilder builder) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class TestVolantisProtocolFactory extends VolantisProtocolFactory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestVolantisProtocol(configuration);
        }
    }

    public static class TestDOMProtocolFactory extends DOMProtocolFactory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestDOMProtocol(supportFactory, configuration);
        }
    }

    public static class TestHTMLVersion3_2Factory extends HTMLVersion3_2Factory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            // todo: rename this to Test to avoid confusion.
            return new TestHTMLVersion3_2(supportFactory, configuration);
        }
    }

    public static class TestHTMLPalmWCAFactory extends HTMLPalmWCAFactory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestHTMLPalmWCA(supportFactory, configuration);
        }
    }

    public static class TestWMLRootFactory extends WMLRootFactory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWMLRoot(supportFactory, configuration);
        }
    }

    public static class TestSMSFactory extends SMSFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestSMS(supportFactory, configuration);
        }
    }

    public static class TestMMS_SMIL_2_0Factory extends MMS_SMIL_2_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestMMS_SMIL_2_0(supportFactory, configuration);
        }
    }

    public static class TestWMLVersion1_1Factory extends WMLVersion1_1Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWMLVersion1_1(supportFactory, configuration);
        }
    }

    public static class TestWMLPhoneDotComFactory extends WMLPhoneDotComFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWMLPhoneDotCom(supportFactory, configuration);
        }
    }

    public static class TestWMLVersion1_3Factory extends WMLVersion1_3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWMLVersion1_3(supportFactory, configuration);
        }
    }

    public static class TestWapTV5_WMLVersion1_3Factory
            extends WapTV5_WMLVersion1_3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWapTV5_WMLVersion1_3(supportFactory, configuration);
        }
    }

    public static class TestHTMLRootFactory extends HTMLRootFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            // NOTE: there is no test HTMLRoot instance at the moment.
            return new TestXHTMLTransitional(supportFactory, configuration);
        }
    }

    public static class TestXHTMLBasicFactory extends XHTMLBasicFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLBasic(supportFactory, configuration);
        }
    }

    public static class TestXHTMLFullFactory extends XHTMLFullFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLFull(supportFactory, configuration);
        }
    }

    public static class TestXHTMLTransitionalFactory extends XHTMLTransitionalFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLTransitional(supportFactory, configuration);
        }
    }

    public static class TestWMLEmptyOKFactory extends WMLEmptyOK1_3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWMLEmptyOK1_3(supportFactory, configuration);
        }
    }

    public static class TestWMLOpenWave1_3Factory extends WMLOpenWave1_3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestWMLOpenWave1_3(supportFactory, configuration);
        }
    }

    public static class TestXHTMLBasic_MIB2_0Factory extends XHTMLBasic_MIB2_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLBasic_MIB2_0(supportFactory, configuration);
        }
    }

    public static class TestXHTMLBasic_MIB2_1Factory extends XHTMLBasic_MIB2_1Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLBasic_MIB2_1(supportFactory, configuration);
        }
    }

    public static class TestXHTMLBasic_Netfront3Factory extends XHTMLBasic_Netfront3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLBasic_Netfront3(supportFactory, configuration);
        }
    }

    public static class TestXHTMLMobile1_0Factory extends XHTMLMobile1_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLMobile1_0(supportFactory, configuration);
        }
    }

    public static class TestXHTMLMobile1_0_SE_PFactory extends XHTMLMobile1_0_SE_PFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLMobile1_0_SE_P(supportFactory, configuration);
        }
    }

    public static class TestXHTMLMobile1_0Openwave6Factory extends XHTMLMobile1_0Openwave6Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestXHTMLMobile1_0Openwave6(supportFactory, configuration);
        }
    }

    public static class TestHDML_Version3Factory extends HDML_Version3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestHDML_Version3(supportFactory, configuration);
        }
    }

    public static class TestHTML_iModeFactory extends HTML_iModeFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestHTML_iMode(supportFactory, configuration);
        }
    }

    public static class TestMMLBasicFactory extends MMLBasicFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestMMLBasic(supportFactory, configuration);
        }
    }

    public static class TestMHTMLFactory extends MHTMLFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestMHTML(supportFactory, configuration);
        }
    }

    public static class TestHTMLVersion4_0Factory extends HTMLVersion4_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestHTMLVersion4_0(supportFactory, configuration);
        }
    }

    public static class TestHTMLVersion4_0_IE6Factory extends HTMLVersion4_0_IE6Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestHTMLVersion4_0_IE6(supportFactory, configuration);
        }
    }

    public static class TestVoiceXMLVersion1_0Factory extends VoiceXMLVersion1_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new TestVoiceXMLVersion1_0(supportFactory, configuration);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
