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
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.xdime.XDIMEProtocol;
import com.volantis.mcs.protocols.xdime.XDIMEProtocolConfiguration;
import com.volantis.mcs.protocols.html.HDML_Version3;
import com.volantis.mcs.protocols.html.HTMLLiberate;
import com.volantis.mcs.protocols.html.HTMLNetgem;
import com.volantis.mcs.protocols.html.HTMLPalmWCA;
import com.volantis.mcs.protocols.html.HTMLPalmWCAConfiguration;
import com.volantis.mcs.protocols.html.HTMLParagon;
import com.volantis.mcs.protocols.html.HTMLRootConfiguration;
import com.volantis.mcs.protocols.html.HTMLVersion3_2;
import com.volantis.mcs.protocols.html.HTMLVersion3_2Configuration;
import com.volantis.mcs.protocols.html.HTMLVersion3_2_PIE;
import com.volantis.mcs.protocols.html.HTMLVersion3_AG;
import com.volantis.mcs.protocols.html.HTMLVersion4_0;
import com.volantis.mcs.protocols.html.HTMLVersion4_0_IE;
import com.volantis.mcs.protocols.html.HTMLVersion4_0_IE6;
import com.volantis.mcs.protocols.html.HTMLVersion4_0_NS4;
import com.volantis.mcs.protocols.html.HTMLWebTV;
import com.volantis.mcs.protocols.html.HTML_iMode;
import com.volantis.mcs.protocols.html.HTML_iModeConfiguration;
import com.volantis.mcs.protocols.html.MHTML;
import com.volantis.mcs.protocols.html.MMLBasic;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.protocols.html.XHTMLBasicConfiguration;
import com.volantis.mcs.protocols.html.XHTMLBasic_MIB2_0;
import com.volantis.mcs.protocols.html.XHTMLBasic_MIB2_1;
import com.volantis.mcs.protocols.html.XHTMLBasic_Netfront3;
import com.volantis.mcs.protocols.html.XHTMLFull;
import com.volantis.mcs.protocols.html.XHTMLFullConfiguration;
import com.volantis.mcs.protocols.html.XHTMLMobile1_0;
import com.volantis.mcs.protocols.html.XHTMLMobile1_0Configuration;
import com.volantis.mcs.protocols.html.XHTMLMobile1_0Openwave6;
import com.volantis.mcs.protocols.html.XHTMLMobile1_0_SE_P;
import com.volantis.mcs.protocols.html.XHTMLTransitional;
import com.volantis.mcs.protocols.html.HTMLVersion3_AGConfiguration;
import com.volantis.mcs.protocols.html.HDML_Version3Configuration;
import com.volantis.mcs.protocols.mms.MMS_SMIL_2_0;
import com.volantis.mcs.protocols.text.SMS;
import com.volantis.mcs.protocols.vdxml.VDXMLVersion2_0;
import com.volantis.mcs.protocols.voicexml.VoiceXMLVersion1_0;
import com.volantis.mcs.protocols.wml.WMLEmptyOK1_3;
import com.volantis.mcs.protocols.wml.WMLOpenWave1_3;
import com.volantis.mcs.protocols.wml.WMLOpenWave1_3Configuration;
import com.volantis.mcs.protocols.wml.WMLPhoneDotCom;
import com.volantis.mcs.protocols.wml.WMLPhoneDotComConfiguration;
import com.volantis.mcs.protocols.wml.WMLRootConfiguration;
import com.volantis.mcs.protocols.wml.WMLVersion1_1;
import com.volantis.mcs.protocols.wml.WMLVersion1_1Configuration;
import com.volantis.mcs.protocols.wml.WMLVersion1_3;
import com.volantis.mcs.protocols.wml.WMLVersion1_3Configuration;
import com.volantis.mcs.protocols.wml.WapTV5_WMLVersion1_3;

/**
 * A registry for the various protocols that MCS supports.
 */
public class ProtocolRegistry {

    /**
     * Initialise.
     */
    public ProtocolRegistry() {
    }

    /**
     * Initialize the supplued protocol builder with appropriate factories for
     * all supported protocols.
     *
     * @param builder the builder to initialise.
     */
    public void register(NamedProtocolBuilder builder) {

        // HINT: there were 37 protocols at the point this was refactored.

        // WML protocols.
        builder.register("WMLVersion1_1", new WMLVersion1_1Factory());
        // NOTE: WML Version 1.2 (WML1.2) no longer supported
        builder.register("WMLVersion1_3", new WMLVersion1_3Factory());
        builder.register("WMLOpenWave1_3", new WMLOpenWave1_3Factory());
        builder.register("WMLEmptyOK1_3", new WMLEmptyOK1_3Factory());
        builder.register("WapTV5_WMLVersion1_3", new WapTV5_WMLVersion1_3Factory());
        builder.register("WMLPhoneDotCom", new WMLPhoneDotComFactory());

        // XHTMLBasic protocols.
        builder.register("XHTMLBasic", new XHTMLBasicFactory());
        builder.register("XHTMLBasic_MIB2_0", new XHTMLBasic_MIB2_0Factory());
        builder.register("XHTMLBasic_MIB2_1", new XHTMLBasic_MIB2_1Factory());
        builder.register("XHTMLBasic_Netfront3", new XHTMLBasic_Netfront3Factory());

        // XHTMLMobile
        builder.register("XHTMLMobile1_0", new XHTMLMobile1_0Factory());
        builder.register("XHTMLMobile1_0_SE_P", new XHTMLMobile1_0_SE_PFactory());
        builder.register("XHTMLMobile1_0Openwave6", new XHTMLMobile1_0Openwave6Factory());

        // XHTMLFull protocols.
        builder.register("XHTMLFull", new XHTMLFullFactory());
        builder.register("XHTMLTransitional", new XHTMLTransitionalFactory());

        // HTMLRoot protocols.
        builder.register("HDMLVersion3", new HDML_Version3Factory());

        // HTML 3.2 protocols.
        builder.register("HTMLVersion3_2", new HTMLVersion3_2Factory());
        builder.register("HTMLNetgem", new HTMLNetgemFactory());
        builder.register("HTMLPalmWCA", new HTMLPalmWCAFactory());
        builder.register("HTMLLiberate", new HTMLLiberateFactory());
        builder.register("HTMLWebTV", new HTMLWebTVFactory());
        builder.register("HTMLVersion3_2_PIE", new HTMLVersion3_2_PIEFactory());
        builder.register("HTMLVersion3_AG", new HTMLVersion3_AGFactory());
        builder.register("MHTML", new MHTMLFactory());

        // HTML_iMode protocols
        builder.register("HTMLiMode", new HTML_iModeFactory());
        builder.register("MMLBasic", new MMLBasicFactory());

        // HTML 4 protocols.
        builder.register("HTMLVersion4_0", new HTMLVersion4_0Factory());
        builder.register("HTMLVersion4_0_NS4", new HTMLVersion4_0_NS4Factory());
        builder.register("HTMLVersion4_0_IE", new HTMLVersion4_0_IEFactory());
        builder.register("HTMLVersion4_0_IE6", new HTMLVersion4_0_IE6Factory());
        builder.register("HTMLParagon", new HTMLParagonFactory());

        // Non WML or HTML protocols.
        builder.register("MMS_SMIL_2_0", new MMS_SMIL_2_0Factory());
        builder.register("VDXMLVersion2_0", new VDXMLVersion2_0Factory());
        builder.register("VoiceXMLVersion1_0", new VoiceXMLVersion1_0Factory());
        builder.register("SMS", new SMSFactory());

        // Integration testing only protocol.
        builder.register("-XDIME-Protocol-", new XDIMEProtocolFactory());
    }

    public abstract static class VolantisProtocolFactory implements ProtocolFactory {
        public ProtocolConfiguration createConfiguration() {
            return new ProtocolConfigurationImpl();
        }
    }

    public abstract static class DOMProtocolFactory extends VolantisProtocolFactory {
    }

    public abstract static class WMLRootFactory extends DOMProtocolFactory {
        public ProtocolConfiguration createConfiguration() {
            return new WMLRootConfiguration();
        }
    }


    public static class WMLVersion1_1Factory extends WMLRootFactory {
        public ProtocolConfiguration createConfiguration() {
            return new WMLVersion1_1Configuration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new WMLVersion1_1(supportFactory, configuration);
        }
    }

    public static class WMLVersion1_3Factory extends WMLVersion1_1Factory {
        public ProtocolConfiguration createConfiguration() {
            return new WMLVersion1_3Configuration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new WMLVersion1_3(supportFactory, configuration);
        }
    }

    public static class WMLOpenWave1_3Factory extends WMLVersion1_3Factory {
        public ProtocolConfiguration createConfiguration() {
            return new WMLOpenWave1_3Configuration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new WMLOpenWave1_3(supportFactory,
                    configuration);
        }
    }

    public static class WMLEmptyOK1_3Factory extends WMLVersion1_3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new WMLEmptyOK1_3(supportFactory, configuration);
        }
    }

    public static class WapTV5_WMLVersion1_3Factory extends WMLVersion1_3Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new WapTV5_WMLVersion1_3(supportFactory,
                    configuration);
        }
    }

    public static class WMLPhoneDotComFactory extends WMLVersion1_1Factory {
        public ProtocolConfiguration createConfiguration() {
            return new WMLPhoneDotComConfiguration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new WMLPhoneDotCom(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLBasicFactory extends DOMProtocolFactory {
        public ProtocolConfiguration createConfiguration() {
            return new XHTMLBasicConfiguration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLBasic(supportFactory, configuration);
        }
    }

    public static class XHTMLBasic_MIB2_0Factory extends XHTMLBasicFactory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLBasic_MIB2_0(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLBasic_MIB2_1Factory extends XHTMLBasicFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLBasic_MIB2_1(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLBasic_Netfront3Factory extends XHTMLBasicFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLBasic_Netfront3(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLMobile1_0Factory extends XHTMLBasicFactory {
        public ProtocolConfiguration createConfiguration() {
            return new XHTMLMobile1_0Configuration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLMobile1_0(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLMobile1_0_SE_PFactory extends XHTMLMobile1_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLMobile1_0_SE_P(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLMobile1_0Openwave6Factory extends XHTMLMobile1_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLMobile1_0Openwave6(supportFactory,
                    configuration);
        }
    }

    public static class XHTMLFullFactory extends XHTMLBasicFactory {
        public ProtocolConfiguration createConfiguration() {
            return new XHTMLFullConfiguration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLFull(supportFactory, configuration);
        }
    }

    public static class XHTMLTransitionalFactory extends XHTMLFullFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XHTMLTransitional(supportFactory,
                    configuration);
        }
    }

    public abstract static class HTMLRootFactory extends XHTMLTransitionalFactory {
        public ProtocolConfiguration createConfiguration() {
            return new HTMLRootConfiguration();
        }
    }

    public static class HDML_Version3Factory extends HTMLRootFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HDML_Version3(supportFactory, configuration);
        }

        public ProtocolConfiguration createConfiguration() {
            return new HDML_Version3Configuration();
        }
    }

    public static class HTMLVersion3_2Factory extends HTMLRootFactory {
        public ProtocolConfiguration createConfiguration() {
            return new HTMLVersion3_2Configuration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion3_2(supportFactory,
                    configuration);
        }
    }

    private static class HTMLNetgemFactory extends HTMLVersion3_2Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLNetgem(supportFactory, configuration);
        }
    }

    public static class HTMLPalmWCAFactory extends HTMLVersion3_2Factory {
        public ProtocolConfiguration createConfiguration() {
            return new HTMLPalmWCAConfiguration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLPalmWCA(supportFactory, configuration);
        }
    }

    private static class HTMLLiberateFactory extends HTMLVersion3_2Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLLiberate(supportFactory, configuration);
        }
    }

    private static class HTMLWebTVFactory extends HTMLVersion3_2Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLWebTV(supportFactory, configuration);
        }
    }

    private static class HTMLVersion3_2_PIEFactory extends HTMLVersion3_2Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion3_2_PIE(supportFactory,
                    configuration);
        }
    }

    private static class HTMLVersion3_AGFactory extends HTMLVersion3_2Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion3_AG(supportFactory,
                    configuration);
        }

        public ProtocolConfiguration createConfiguration() {
            return new HTMLVersion3_AGConfiguration();
        }
    }

    public static class MHTMLFactory extends HTMLVersion3_2Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new MHTML(supportFactory, configuration);
        }
    }

    public static class HTML_iModeFactory extends HTMLRootFactory {
        public ProtocolConfiguration createConfiguration() {
            return new HTML_iModeConfiguration();
        }

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTML_iMode(supportFactory, configuration);
        }
    }

    public static class MMLBasicFactory extends HTML_iModeFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new MMLBasic(supportFactory, configuration);
        }
    }

    public static class HTMLVersion4_0Factory extends HTMLRootFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion4_0(supportFactory,
                    configuration);
        }
    }

    private static class HTMLVersion4_0_NS4Factory extends HTMLVersion4_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion4_0_NS4(supportFactory,
                    configuration);
        }
    }

    private static class HTMLVersion4_0_IEFactory extends HTMLVersion4_0Factory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion4_0_IE(supportFactory,
                    configuration);
        }
    }

    public static class HTMLVersion4_0_IE6Factory extends HTMLVersion4_0_IEFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLVersion4_0_IE6(supportFactory,
                    configuration);
        }
    }

    public static class HTMLParagonFactory extends HTMLVersion4_0Factory{
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new HTMLParagon(supportFactory, configuration);
        }
    }

    public static class MMS_SMIL_2_0Factory extends DOMProtocolFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new MMS_SMIL_2_0(supportFactory, configuration);
        }
    }

    public static class VDXMLVersion2_0Factory extends DOMProtocolFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new VDXMLVersion2_0(supportFactory,
                    configuration);
        }
    }

    public static class VoiceXMLVersion1_0Factory extends DOMProtocolFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new VoiceXMLVersion1_0(supportFactory,
                    configuration);
        }
    }

    public static class SMSFactory extends VolantisProtocolFactory {
        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new SMS(supportFactory, configuration);
        }
    }

    /**
     * Create a protocol that passes the XDIME straight through in order to
     * support integration level testing.
     */
    public static class XDIMEProtocolFactory extends DOMProtocolFactory {

        public VolantisProtocol createProtocol(
                ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            return new XDIMEProtocol(supportFactory, configuration);
        }

        public ProtocolConfiguration createConfiguration() {
            return new XDIMEProtocolConfiguration();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 03-Oct-05	9522/3	ibush	VBM:2005091502 no_save on images

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
