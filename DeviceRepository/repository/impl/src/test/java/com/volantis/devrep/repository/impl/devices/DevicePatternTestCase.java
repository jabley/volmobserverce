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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.xml.DeviceRepositoryConstants;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DevicesHelper;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryFactory;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provide some test cases to test the regular expression device pattern matching.
 *
 * NOTE: This testcase (mostly) depends on a external resource (database) and is
 * therefore (mostly) disabled. However, it is very useful to be able to run
 * this test if there are problems with the device pattern matching algorithm.
 *
 * @todo better this test should be enabled with the latest device XML
 * device repository. This test could be enhanced fairly simply by reading
 * in the latest device repositories patterns and making 'User Agent'
 * strings out of the regular expressions and using them to test that the
 * device pattern cache is working for all devices in the repository. This may
 * be a kind of "user acceptance test" for a new device repository?
 *
 */
public class DevicePatternTestCase extends TestCaseAbstract {

    /**
     * List all device names and device pattern names extracted from the database.
     */
    private String[][] deviceNamePatterns = {
        {"MMEF300-GenericLarge", "MobileExplorer/3\\.00 (Mozilla/1\\.22; compatible; MMEF300; Microsoft; Windows; GenericLarge).*"},
        {"MMEF300-GenericSmall", "MobileExplorer/3\\.00 (Mozilla/1\\.22; compatible; MMEF300; Microsoft; Windows; GenericSmall).*"},
        {"MMEF300-GenericXP", "MobileExplorer/3\\.00 (Mozilla/1\\.22; compatible; MMEF300; Microsoft; Windows; GenericXP).*"},
        {"Palm-PSWeb2-HR", "Mozilla/4\\.0 (compatible; MSIE 6\\.0; Windows 95; PalmSource) NetFront/3.0; 16; 320x320).*"},
        {"Bush-Alba-InternetTV", "Mozilla/3\\.04 (compatible; NCBrowser/2.35; ANTFresco/2.17; RISC OS-NC 5.13 Laz1UK1309).*"},
        {"Palm-PSWeb2-HR", "Mozilla/4\\.0 (compatible; MSIE 6\\.0; Windows 95; PalmSource) NetFront/3.0; .*; 320x.*"},
        {"SprintPCS-HSTR-600", "X-WAP-PROFILE: http://device.sprintpcs.com/Handspring/HSTR600HK/Blazer300306.rdf"},
        {"RIM-950-Go.Web", "Mozilla/2\\.0 (compatible; Go\\.Web/6\\.0; HandHTTP 1\\.1; Elaine/1\\.0; RIM950 .*"},
        {"Palm-PSWeb2", "Mozilla/4\\.0 (compatible; MSIE 6\\.0; Windows 95; PalmSource) NetFront/3\\.0.*"},
        {"Nokia-9210i-WWW", "Mozilla/4\\.1 (compatible; MSIE 5\\.0; EPOC) Opera 6\\.0.*Nokia/Series-9200.*"},
        {"Blazer-3_0", "Mozilla/4\\.0 (compatible; MSIE 6\\.0; Windows 95; PalmSource; Blazer 3\\.0.*"},
        {"Palm-Go.Web", "Mozilla/1\\.0[en] ; Go.Web/.* (compatible MSIE 2\\.0; HandHTTP 1\\.1; Palm).*"},
        {"Nokia-3650-Opera6", "Mozilla/4\\.1 (compatible; MSIE 5\\.0; Symbian OS Series 60 42) Opera 6\\..*"},
        {"Nokia-9210i-WWW", "Mozilla/4\\.1 (compatible; MSIE 5\\.0; EPOC) Opera 6\\.0.*Nokia/Series9200.*"},
        {"Nokia-6600-Opera6", "Mozilla/4\\.1 (compatible; MSIE 5\\.0; Symbian OS; Nokia 6600.*) Opera 6.*"},
        {"Palm-Eudora", "Mozilla/1\\.22 (compatible; EudoraWeb 2\\.0; pdQbrowser; PalmOS 2\\.0).*"},
        {"Sharp-SL5000-Opera5", "Mozilla/4\\.0 (compatible; MSIE 5\\.0; Linux.*embedix .*Opera 5\\.0.*"},
        {"Sharp-SL5000-Opera6", "Mozilla/4\\.0 (compatible; MSIE 5\\.0; Linux.*embedix .*Opera 6\\.0.*"},
        {"PocketPC2002-PIE", "Mozilla/2\\.0 (compatible; MSIE 3\\.02; Windows CE; 240x320; PPC).*"},
        {"Samsung-SPH-A500", "X-WAP-PROFILE: http://device\\.sprintpcs\\.com/Samsung/SPH-A500/.*"},
        {"Samsung-SPH-A600", "X-WAP-PROFILE: http://device\\.sprintpcs\\.com/Samsung/SPH-A600/.*"},
        {"Samsung-SPH-A620", "X-WAP-PROFILE: http://device\\.sprintpcs\\.com/Samsung/SPH-A620/.*"},
        {"Samsung-SPH-N400", "X-WAP-PROFILE: http://device\\.sprintpcs\\.com/Samsung/SPH-N400/.*"},
        {"MS-SP2002-PIE", "Mozilla/2\\.0 (compatible; MSIE 3\\.02; Windows CE; Smartphone;.*"},
        {"Palm-Eudora", "Mozilla/1\\.22 (compatible; MSIE 5\\.01; PalmOS .*) EudoraWeb 2.*"},
        {"Gmate-Yopy", "Mozilla/4\\.0 (MSIE 5\\.0 compatible; Spyglass DM 4\\.0; Linux).*"},
        {"Sendo-J520", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sendo J520.*).*"},
        {"Sendo-J530", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sendo J530.*).*"},
        {"Sendo-S230", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sendo S230.*).*"},
        {"SE-P800-Opera6", "Mozilla/4\\.1 (compatible; MSIE 5\\.0; Symbian OS) Opera 6\\..*"},
        {"Sony-CMD-J5-WML", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sony CMD-J5).*"},
        {"Nokia-9110i-WWW", "Nokia-Communicator-WWW-Browser/3\\.0 (Geos 3\\.0 Nokia-9110).*"},
        {"Sony-CMD-Z5-WML", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sony CMD-Z5).*"},
        {"Sanyo-SCP-8100", "Mozilla/4\\.0 (MobilePhone SCP-8100/US/1\\.*) NetFront/3\\.0.*"},
        {"Sony-CMD-Z7-WML", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sony CMD-Z7.*"},
        {"Sony-CMD-J7X-WML", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sony CMD-J7.*"},
        {"Benefon-Q-WML", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Benefon Q).*"},
        {"PocketPC-PIE-mono", "Mozilla/2\\.0 (compatible; MSIE 3\\.02; Windows CE;.*mono4.*"},
        {"Series60-NetFront3", "Mozilla/4\\.0 (SmartPhone; Symbian OS/.*) NetFront/3\\.0.*"},
        {"Sanyo-SCP-4900", "Mozilla/4\\.0 (MobilePhone SCP-4900/1\\.*) NetFront/3\\.0.*"},
        {"Sanyo-SCP-5300", "Mozilla/4\\\\.0 (MobilePhone SCP-5300/1\\.*) NetFront/3\\.0.*"},
        {"Sanyo-SCP-7200", "Mozilla/4\\.0 (MobilePhone SCP-7200/1\\.*) NetFront/3\\.0.*"},
        {"Psion-Revo-Opera", "Mozilla/4\\.51 (compatible; Opera 3\\.62; EPOC; 480x160).*"},
        {"Sanyo-SCP-6400", "Mozilla/4\\.0 (MobilePhone SCP-6400/1\\.*) NetFront/3\\.0.*"},
        {"Sanyo-SCP-5400", "Mozilla/4\\.0 (MobilePhone SCP-5400/1\\.*) NetFront/3\\.0.*"},
        {"Sendo-WAP", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sendo.*).*"},
        {"PocketPC2002-PIE", "Mozilla/2\\.0 (compatible; MSIE 3\\.02; Windows CE; PPC.*"},
        {"PocketPC2003-PIE", "Mozilla/4\\.0 (compatible; MSIE 4\\.01; Windows CE; PPC.*"},
        {"Sony-WAP", "Mozilla/1\\.22 (compatible; MMEF20; Cellphone; Sony .*"},
        {"Bush-Alba-InternetTV", "Mozilla/3\\.04 (compatible; NCBrowser/.*; ANTFresco/.*"},
        {"WC-Thunderstone", "Mozilla/2\\.0 (compatible; T-H-U-N-D-E-R-S-T-O-N-E).*"},
        {"PocketPC-PIE", "Mozilla/2\\.0 (compatible; MSIE 3\\.02; Windows CE;.*"},
        {"WinCE2-PDA", "Mozilla/1\\.1 (compatible; MSPIE 1\\.1; Windows CE).*"},
        {"PC-MacOS-IE5", "Mozilla/4\\.0 (compatible; MSIE 5\\..*; Mac_PowerPC.*"},
        {"MS-HPC2000", "Mozilla/4\\.0 (compatible; MSIE 4\\.01.*Windows CE.*"},
        {"Sony-CLIE-NetFront3", "Mozilla/4\\.0 (PDA; PalmOS/sony/.*) NetFront/3\\.0.*"},
        {"WC-MSIECrawler", "Mozilla/4\\.0 (compatible; MSIE 4\\.0; MSIECrawler.*"},
        {"PocketPC-NetFront3", "Mozilla/4\\.0 (PDA; Windows CE/.*) NetFront/3\\.0.*"},
        {"RIM-957-Go.Web", "Mozilla/2\\.0 (compatible; Go\\.Web/6\\.0.*RIM957 .*"},
        {"PC-MacOS-Safari", "Mozilla/5\\.0 (Macintosh; U; PPC Mac OS.*Safari.*"},
        {"PC-Win32-Opera3", "Mozilla/3.* (compatible; Opera/3\\..*; Windows.*"},
        {"XHTML-Handset", "accept: .*application/vnd\\.wap\\.xhtml\\+xml.*"},
        {"Movistar-TSM-4", "Vitelcom-Feature Phone 1\\.0 UP\\.Browser/5.*"},
        {"Sendo-WAP", "MobileExplorer/3.00 (MMEF300; Sendo; Wap).*"},
        {"BT-Multiphone", "Mozilla/4\\.0 (compatible; MSIE .*; QNX .*)"},
        {"PC-MacOS-IE", "Mozilla/4\\.0 (compatible; MSIE 4\\..* Mac.*"},
        {"PC-Win32-Mozilla", "Mozilla/5\\.0 (Windows.* rv:1\\..*) Gecko/.*"},
        {"WinCE2-PDA", "Microsoft Pocket Internet Explorer/0\\.6.*"},
        {"Palm-OmniSky", "Mozilla/2\\.0 (compatible; Elaine/1\\.1).*"},
        {"WC-EuroFerret", "Mozilla/3\\.0 (compatible; MuscatFerret.*"},
        {"WC-AskJeeves", "Mozilla/2\\.0 (compatible; Ask Jeeves).*"},
        {"Palm-WebPro-2", "Mozilla/4\\.76.* (PalmOS; U; WebPro/2.*)"},
        {"Palm-WebPro-3", "Mozilla/4\\.76.* (PalmOS; U; WebPro/3.*)"},
        {"Pogo", "Mozilla/3\\.0 (Netbox-compatible; Pogo.*"},
        {"Nokia-9210-WWW", "EPOC32-WTL/.*Crystal/6\\.0 STNC-WTL/6.*"},
        {"Palm-WebPro-1", "Mozilla/4\\.76 .* (PalmOS; U; WebPro).*"},
        {"Psion-Revo-Web", "EPOC32-WTL/2\\.0 (RVGA) STNC-WTL/2\\.0.*"},
        {"Palm-AvantGo", "Mozilla/3\\.0 (compatible; .*AvantGo.*"},
        {"Palm-Wapman", "WAPman Version 1\\.8:Build P2001020800"},
        {"PC-Win32-IE5.5", "Mozilla/4\\.0 (compatible; MSIE 5\\.5.*"},
        {"Nokia-9210i-WAP", "Nokia 9210i/1\\.0 Symbian Crystal/6.*"},
        {"Palm-Blazer", "UPG1 UP/4\\.0 (compatible; Blazer .*)"},
        {"PC-Win32-IE5", "Mozilla/4\\.0 (compatible; MSIE 5\\..*"},
        {"PC-Win32-IE4", "Mozilla/4\\.0 (compatible; MSIE 4\\..*"},
        {"Nokia-9210i-WAP", "Nokia 9210i/1\\.0 Symbian-Crystal/6.*"},
        {"PC-Win32-IE6", "Mozilla/4\\.0 (compatible; MSIE 6\\..*"},
        {"PC-Win32-Opera7", "Mozilla/4\\.0 .*Windows.*Opera 7\\.0.*"},
        {"Netgem-Netbox", "Mozilla/3\\.01 (compatible; Netgem/.*"},
        {"Netgem-Netbox", "Mozilla/3\\.01 (compatible; Netbox/.*"},
        {"Liberate-1.3", "Mozilla/3\\.0 ( Liberate; DTV; 1_3;.*"},
        {"PC-Win32-Opera7", "Mozilla/5\\.0 .*Windows.*Opera 7\\.0.*"},
        {"PC-Win32-Opera4", "Mozilla/4\\.73.*Windows.*Opera 4\\..*"},
        {"Nokia-9210-WAP", "Nokia 9210/1\\.0 Symbian Crystal/6.*"},
        {"PC-UNIX-Netscape6", "Mozilla/5\\.0 (X11; .* Netscape6/6.*"},
        {"Palm-PocketLink", "Mozilla/4\\.5 (compatible; PLink .*)"},
        {"Nokia-9210i-WAP", "Nokia9210i/1\\.0 Symbian-Crystal/6.*"},
        {"XHTML-Handset", "accept: .*application/xhtml\\+xml.*"},
        {"Nokia-9210-WAP", "Nokia9210/1\\.0 Symbian Crystal/6.*"},
        {"PC-UNIX-Netscape7", "Mozilla/5\\.0 (X11; .* Netscape/7.*"},
        {"Nokia-9210-WAP", "Nokia9210/1\\.0 Symbian-Crystal/6.*"},
        {"PC-Win32-Oligo", "Mozilla/4\\.0 (compatible; Oligo .*"},
        {"Liberate-1.2", "Mozilla/3\\.0 (Liberate DTV 1\\.2).*"},
        {"Liberate-1.1", "Mozilla/3\\.0 (Liberate DTV 1\\.1.*"},
        {"PocketPC-EZWAP-2.1", "EZOS - EzWAP 2\\.1 for Pocket PC.*"},
        {"Samsung-SGH-T100-v2", "SAMSUNG-SGH-T100/.*UP.Browser/5.*"},
        {"Openwave-SDK-5_1", "OPWV-GEN-99/UNI10.*UP\\.Browser.*"},
        {"Pogo", "Mozilla/3\\.0 (compatible; Pogo.*"},
        {"PC-UNIX-Opera5", "Mozilla/5\\.0.*Linux.*Opera 5\\..*"},
        {"PC-UNIX-Opera6", "Mozilla/5\\.0.*Linux.* Opera 6\\.*"},
        {"Samsung-SGH-T100-v2", "SAMSUNG-SGHT100/.*UP.Browser/5.*"},
        {"PC-Win32-Opera6", "Mozilla/4\\.0.*Windows.*Opera 6.*"},
        {"PC-Win32-Opera6", "Mozilla/5\\.0.*Windows.*Opera 6.*"},
        {"WC-WebCollector", "Mozilla/2\\.0 (compatible; NEWT.*"},
        {"Alcatel-OT-331", "Alcatel-BG3/1\\.0 UP.Browser/5.*"},
        {"Hitachi-SH-P300", "Hitachi-P300 UP\\.Browser/6\\.1.*"},
        {"PC-UNIX-Netscape6", "Mozilla/5 .*X11; .*Netscape6/.*"},
        {"Telit-GM822", "Telit_Mobile_Terminals-GM822.*"},
        {"PC-UNIX-Opera4", "Mozilla/4\\.0.*X11.* Opera 4\\.*"},
        {"PC-UNIX-Opera5", "Mozilla/4\\.0.*X11.* Opera 5\\.*"},
        {"PC-UNIX-Opera7", "Mozilla/5\\.0.*Linux.*Opera 7.*"},
        {"PC-UNIX-Opera7", "Mozilla/4\\.0.*Linux.*Opera 7.*"},
        {"PC-UNIX-Opera6", "Mozilla/5\\.0.*X11.* Opera 6\\.*"},
        {"PC-UNIX-Opera5", "Mozilla/5\\.0.*X11.*Opera 5\\..*"},
        {"Samsung-SPH-A460", "SEC-spha460 UP\\.Browser/4\\.1.*"},
        {"Telit-GM825", "Telit_Mobile_Terminals-GM825.*"},
        {"Nokia-9290-WAP", "Nokia9290/Symbian-Crystal/6.*"},
        {"PC-Win32-Netscape6", "Mozilla/5\\.0 .* Netscape6/6.*"},
        {"WC-NationalDirectory", "NationalDirectory-WebSpider.*"},
        {"PC-Win32-Netscape7", "Mozilla/5\\.0 .* Netscape/7.*"},
        {"PC-Win32-Opera5", "Mozilla/.*Windows.*Opera 5.*"},
        {"Kyocera-QCP-3035", "UP\\.Browser/3\\.1\\...-QC07.* "},
        {"PC-UNIX-Netscape4", "Mozilla/4.*X11; .*Netscape.*"},
        {"PC-MacOS-Omniweb", "Mozilla/4\\.0.*Mac.*OmniWeb.*"},
        {"ME-Emulator", "Mozilla/1\\.22 (compatible;*)"},
        {"3com-Ergo-Audrey", "Mozilla/4\\.7 (Win98; Audrey)"},
        {"Openwave-SDK-6_2", "OPWV-SDK/62.*UP\\.Browser/6.*"},
        {"Openwave-SDK-6_1", "OPWV-SDK/61.*UP\\.Browser/6.*"},
        {"Openwave-SDK-5_1-UP6", "OPWV-SDK/51.*UP\\.Browser/6.*"},
        {"Openwave-SDK-5_1", "OPWV-SDK/51.*UP\\.Browser/5.*"},
        {"Nokia-MIT3_1-NMB", "Nokia Mobile Browser 3\\.01.*"},
        {"Denso-PCS-120", "UP\\.Browser/3\\.1\\...-DS12.*"},
        {"Denso-PCS-2200", "UP\\.Browser/3\\.1\\...-DS13.*"},
        {"Kyocera-QCP-2035", "UP\\.Browser/3\\.1\\...-QC06.*"},
        {"Denso-PCS-2100", "UP\\.Browser/3\\.1\\...-DS15.*"},
        {"LG-DM510", "UP\\.Browser/3\\.1\\...-LG13.*"},
        {"LG-Sprint-TP1100", "UP\\.Browser/3\\.1\\...-LG06.*"},
        {"LG-DM110", "UP\\.Browser/3\\.1\\...-LG05.*"},
        {"Motorola-V.8162", "UP\\.Browser/3\\.1\\...-MCC9.*"},
        {"Sanyo-SCP-4500", "UP\\.Browser/3\\.1\\...-SY02.*"},
        {"Sanyo-SCP-4000", "UP\\.Browser/3\\.1\\...-SY01.*"},
        {"Samsung-SCH-8500", "UP\\.Browser/3\\.1\\...-SC02.*"},
        {"Samsung-SCH-6100", "UP\\.Browser/3\\.1\\...-SC03.*"},
        {"Samsung-SCH-3500", "UP\\.Browser/3\\.1\\...-SC01.*"},
        {"Samsung-M100", "UP\\.Browser/3\\.1\\...-SC13.*"},
        {"Openwave-SDK-6", "OPWV-SDK/6.*UP\\.Browser/6.*"},
        {"Openwave-SDK-5_0", "OPWV-GEN-02/UNI10 UP/5\\.0.*"},
        {"Motorola-ST7860", "UP\\.Browser/3\\.1...-MC01.*"},
        {"Motorola-V2260", "UP\\.Browser/3\\.1...-MCCC.*"},
        {"TuKa-TS01 ", "UP\\.Browser/3\\...-SYT1  .*"},
        {"Nokia-6180", "UP\\.Browser/3\\.1...-NK00.*"},
        {"au-A3011SA ", "KDDI-SA21  UP\\.Browser/6.*"},
        {"au-A3012CA ", "KDDI-CA21  UP\\.Browser/6.*"},
        {"Netgem-Netbox", "Mozilla/4.*Netbox.*Linux.*"},
        {"AOLTV", "Mozilla/3\\.0 NAVIO.*AOL.*"},
        {"Sega-Dreamcast", "Mozilla/3\\.0 (DreamKey/.*"},
        {"Motorola-V2260", "UP\\.Browser/3\\.0.-MCCC .*"},
        {"TuKa-TK11 ", "UP\\.Browser/3\\...-KCT7 .*"},
        {"TuKa-TK05 ", "UP\\.Browser/3\\...-KCT6 .*"},
        {"TuKa-TK04 ", "UP\\.Browser/3\\...-KCT5 .*"},
        {"TuKa-TK03 ", "UP\\.Browser/3\\...-KCT4 .*"},
        {"TuKa-TK02 ", "UP\\.Browser/3\\...-KCT3 .*"},
        {"TuKa-TK02 ", "UP\\.Browser/3\\...-KCT2 .*"},
        {"TuKa-TK01 ", "UP\\.Browser/3\\...-KCT1 .*"},
        {"TuKa-TD11 ", "UP\\.Browser/3\\...-MIT1 .*"},
        {"BT-Multiphone", "Mozilla/4\\.0 BT Multiphon"},
        {"au-C1001SA ", "UP\\.Browser/3\\...-SY15 .*"},
        {"au-A1014ST", "UP\\.Browser/3\\...-ST14 .*"},
        {"au-A1013K", "UP\\.Browser/3\\...-KC15 .*"},
        {"au-A1012K ", "UP\\.Browser/3\\...-KC14 .*"},
        {"au-A1011ST ", "UP\\.Browser/3\\...-ST13 .*"},
        {"TuKa-TT21 ", "UP\\.Browser/3\\...-TST5 .*"},
        {"TuKa-TT11 ", "UP\\.Browser/3\\...-TST4 .*"},
        {"TuKa-TT03 ", "UP\\.Browser/3\\...-TST3 .*"},
        {"TuKa-TT02 ", "UP\\.Browser/3\\...-TST2 .*"},
        {"PC-UNIX-Konqueror", "Mozilla/5\\.0.*Konqueror.*"},
        {"Nokia-7110-no_WBMP", "Nokia7110/1\\.0 (04\\.67).*"},
        {"au-D306S", "UP\\.Browser/3\\...-SNI1 .*"},
        {"au-D306S", "UP\\.Browser/3\\...-SNC1 .*"},
        {"au-D305P", "UP\\.Browser/3\\...-MAC1 .*"},
        {"au-D305P", "UP\\.Browser/3\\...-MAC2 .*"},
        {"au-D305P", "UP\\.Browser/3\\...-MAI2 .*"},
        {"au-D305P", "UP\\.Browser/3\\...-MAI1 .*"},
        {"au-D304K", "UP\\.Browser/3\\...-KCI2 .*"},
        {"au-D304K", "UP\\.Browser/3\\...-KCC2 .*"},
        {"au-D303K", "UP\\.Browser/3\\...-KCC1 .*"},
        {"au-D303K", "UP\\.Browser/3\\...-KCI1 .*"},
        {"au-D302T", "UP\\.Browser/3\\...-TSC1 .*"},
        {"au-D302T", "UP\\.Browser/3\\...-TSI1 .*"},
        {"au-D301SA ", "UP\\.Browser/3\\...-SYC1 .*"},
        {"au-C452CA ", "UP\\.Browser/3\\...-HI14 .*"},
        {"au-C451H ", "UP\\.Browser/3\\...-TS14 .*"},
        {"au-C415T ", "UP\\.Browser/3\\...-TS14 .*"},
        {"au-C414K ", "UP\\.Browser/3\\...-KC13 .*"},
        {"au-C413S ", "UP\\.Browser/3\\...-SN16 .*"},
        {"au-C413S ", "UP\\.Browser/3\\...-SN15 .*"},
        {"au-C412SA ", "UP\\.Browser/3\\...-SY14 .*"},
        {"au-C411ST ", "UP\\.Browser/3\\...-ST12 .*"},
        {"au-C410T ", "UP\\.Browser/3\\...-TS13 .*"},
        {"au-C409CA ", "UP\\.Browser/3\\...-CA13 .*"},
        {"au-C408P ", "UP\\.Browser/3\\...-MA13 .*"},
        {"au-C407H ", "UP\\.Browser/3\\...-HI13 .*"},
        {"au-C406S ", "UP\\.Browser/3\\...-SN13 .*"},
        {"au-C405SA ", "UP\\.Browser/3\\...-SY13 .*"},
        {"au-C404S ", "UP\\.Browser/3\\...-SN12 .*"},
        {"au-C404S ", "UP\\.Browser/3\\...-SN14 .*"},
        {"au-C403ST ", "UP\\.Browser/3\\...-ST11 .*"},
        {"au-C402DE ", "UP\\.Browser/3\\...-DN11 .*"},
        {"au-C401SA ", "UP\\.Browser/3\\...-SY12 .*"},
        {"au-C313K ", "UP\\.Browser/3\\...-KC12 .*"},
        {"au-C311CA ", "UP\\.Browser/3\\...-CA12 .*"},
        {"au-C310T ", "UP\\.Browser/3\\...-TS12 .*"},
        {"au-C309H ", "UP\\.Browser/3\\...-HI12 .*"},
        {"au-C308P ", "UP\\.Browser/3\\...-MA11 .*"},
        {"au-C308P ", "UP\\.Browser/3\\...-MA12 .*"},
        {"au-C307K ", "UP\\.Browser/3\\...-KC11 .*"},
        {"au-C305S ", "UP\\.Browser/3\\...-SN11 .*"},
        {"au-C304SA ", "UP\\.Browser/3\\...-SY11 .*"},
        {"au-C303CA ", "UP\\.Browser/3\\...-CA11 .*"},
        {"au-C302H ", "UP\\.Browser/3\\...-TS11 .*"},
        {"au-C301T ", "UP\\.Browser/3\\...-TS11 .*"},
        {"au-C202DE ", "UP\\.Browser/3\\...-DN01 .*"},
        {"au-C201H ", "UP\\.Browser/3\\...-HI01 .*"},
        {"au-C1002S ", "UP\\.Browser/3\\...-SN17 .*"},
        {"TuKa-TT01 ", "UP\\.Browser/3\\...-TST1 .*"},
        {"TuKa-TS11 ", "UP\\.Browser/3\\...-SYT3 .*"},
        {"TuKa-TS02 ", "UP\\.Browser/3\\...-SYT2 .*"},
        {"TuKa-TP11 ", "UP\\.Browser/3\\...-MAT3 .*"},
        {"TuKa-TP01 ", "UP\\.Browser/3\\...-MAT2 .*"},
        {"TuKa-TP01 ", "UP\\.Browser/3\\...-MAT1 .*"},
        {"TuKa-TK21", "UP\\.Browser/3\\...-KCT9 .*"},
        {"TuKa-TK12 ", "UP\\.Browser/3\\...-KCT8 .*"},
        {"au-C5001T ", "KDDI-TS21 UP\\.Browser/6.*"},
        {"au-C3003P ", "KDDI-MA21 UP\\.Browser/6.*"},
        {"au-C3002K ", "KDDI-KC21 UP\\.Browser/6.*"},
        {"au-C3001H ", "KDDI-HI21 UP\\.Browser/6.*"},
        {"au-A5304T", "KDDI-TS24 UP\\.Browser/6.*"},
        {"au-A5303H", "KDDI-HI23 UP\\.Browser/6.*"},
        {"au-A5302CA", "KDDI-CA22 UP\\.Browser/6.*"},
        {"au-A5301T", "KDDI-TS23 UP\\.Browser/6.*"},
        {"Trium-Eclipse", "Mitsu/1\\.2\\.A (Eclipse).*"},
        {"Trium-Eclipse-b", "Mitsu/1\\.2\\.B (Eclipse).*"},
        {"au-A3013T", "KDDI-TS22 UP\\.Browser/6.*"},
        {"au-A3015SA", "KDDI-SA22 UP\\.Browser/6.*"},
        {"au-A3014S", "KDDI-SN21 UP\\.Browser/6.*"},
        {"au-A1101S", "KDDI-SN22 UP\\.Browser/6.*"},
        {"Ericsson-R280", "UP\\.Browser/3\\..*-ERK0.*"},
        {"Nokia-WAP-Toolkit", "Nokia-WAP-Toolkit/2\\.0.*"},
        {"PC-UNIX-Nautilus", "Mozilla/5\\.0.*Nautilus.*"},
        {"QCP-860-1960", "UP\\.Browser/3\\...-QC31.*"},
        {"QCP-2760", "UP\\.Browser/3\\...-QC32.*"},
        {"QCP-1920-2700", "UP\\.Browser/3\\...-QC12.*"},
        {"Palm-Kbrowser", "4thpass\\.com KBrowser .*"},
        {"Nokia-WAP-Toolkit-21", "Nokia-WAP-Toolkit/2\\.1.*"},
        {"Nokia-MIT-3-WML", "Nokia-MIT-Browser/3\\.0.*"},
        {"NeoPoint-PCS-1000", "UP\\.Browser/3\\...-IG01.*"},
        {"Motorola-ST7860", "UP\\.Browser/3\\.0.-MC01.*"},
        {"Alcatel-OT-531", "Mitsu/1\\.2\\.B (OT531).*"},
        {"Siemens-SL45", "YourWap Siemens SL45/.*"},
        {"RIM-950-Blackberry", "BlackBerry/2\\.5 (950).*"},
        {"RIM-957-Blackberry", "BlackBerry/2\\.5 (957).*"},
        {"RIM-857-Blackberry", "BlackBerry/2\\.5 (857).*"},
        {"RIM-850-Blackberry", "BlackBerry/2\\.5 (850).*"},
        {"Psion-Revo-WAP", ".*Catspaw/.*(RV) War/.*"},
        {"Ericsson-R380", "YourWap Ericsson 380/.*"},
        {"Mitsubishi-M320", "Mitsu/1\\.2\\.B (M320).*"},
        {"PC-UNIX-Galeon", "Mozilla/5\\.0.*Galeon.*"},
        {"SAGEM-WA3020", "SAGEM-3XXX/0\\.0 UP/4.*"},
        {"SAGEM-MY3052", "SAGEM-3XXX/0\\.0 UP/5.*"},
        {"J-Phone-SH010", "J-PHONE/3\\.0/J-SH010.*"},
        {"Movistar-TSM-30", "portalmmm/2\\.0 TSM30.*"},
        {"Toshiba-TS21i", "portalmmm/1\\.0 ts21i.*"},
        {"DoCoMo-SO502iWM", "DoCoMo/1\\.0/SO502iWM.*"},
        {"Toshiba-TS21i", "portalmmm/1\\.0 TS21i.*"},
        {"Samsung-SprintPCS", "AU-MIC/2\\.0 MMP/2\\.0.*"},
        {"Trium-Mondo-WML", "Mitsu/1\\.1\\.A.*Mondo.*"},
        {"WC-HotBot", "Mozilla/3\\.0 (Slurp/.*"},
        {"PC-Win32-Opera7", "Opera/7\\.0.*Windows.*"},
        {"Microsoft-WebTV", "Mozilla/3\\.0.*WebTV.*"},
        {"J-Phone-SH51 ", "J-PHONE/4\\.0/J-SH51.*"},
        {"J-Phone-SH04 ", "J-PHONE/3\\.0/J-SH04.*"},
        {"J-Phone-SA05", "J-PHONE/3\\.0/J-SA05.*"},
        {"J-Phone-SA04 ", "J-PHONE/3\\.0/J-SA04.*"},
        {"J-Phone-SA03 ", "J-PHONE/3\\.0/J-SA03.*"},
        {"J-Phone-PE03 ", "J-PHONE/3\\.0/J-PE03.*"},
        {"J-Phone-NM02 ", "J-PHONE/3\\.0/J-NM02.*"},
        {"J-Phone-NM01 ", "J-PHONE/3\\.0/J-NM01.*"},
        {"J-Phone-DN03 ", "J-PHONE/3\\.0/J-DN03.*"},
        {"J-Phone-SA02 ", "J-PHONE/2\\.0/J-SA02.*"},
        {"NEC-N22i", "portalmmm/1\\.0 N22i.*"},
        {"NEC-N21i", "portalmmm/1\\.0 n21i.*"},
        {"NEC-N21i", "portalmmm/1\\.0 N21i.*"},
        {"J-Phone-SA51", "J-PHONE/4\\.0/J-SA51.*"},
        {"J-Phone-SH09", "J-PHONE/3\\.0/J-SH09.*"},
        {"J-Phone-SH08 ", "J-PHONE/3\\.0/J-SH08.*"},
        {"J-Phone-SH07 ", "J-PHONE/3\\.0/J-SH07.*"},
        {"J-Phone-SH06 ", "J-PHONE/3\\.0/J-SH06.*"},
        {"J-Phone-SH05 ", "J-PHONE/3\\.0/J-SH05.*"},
        {"DoCoMo-SH2101V", "DoCoMo/2\\.0 SH2101V.*"},
        {"NEC-N341i", "portalmmm/2\\.0 N34i.*"},
        {"NEC-N341i", "portalmmm/2\\.0 n34i.*"},
        {"NEC-N22i", "portalmmm/1\\.0 n22i.*"},
        {"J-Phone-DN02 ", "J-PHONE/2\\.0/J-DN02.*"},
        {"Mitsubishi-m21i", "portalmmm/1\\.0 m21i.*"},
        {"Mitsubishi-m21i", "portalmmm/1\\.0 M21i.*"},
        {"Samsung-SprintPCS", "AU-MIC2\\.0 MMP/2\\.0.*"},
        {"Samsung-SprintPCS", "AU-MIC/1.* MMP/2\\.0.*"},
        {"Nokia-6590-Refresh", "Nokia6590/1.0 (40.41)"},
        {"Trium-Mars", "Mitsu/1\\.1\\.A.*Mars.*"},
        {"Nokia-6210", "YourWap Nokia 6210/.*"},
        {"Palm-WCA", "Mozilla/2.* Elaine/.*"},
        {"DoCoMo-F671iS", "DoCoMo/1\\.0/F6751iS.*"},
        {"DoCoMo-SO503iS", "DoCoMo/1\\.0/SO503iS.*"},
        {"DoCoMo-SH251iS", "DoCoMo/1\\.0/SH251iS.*"},
        {"J-Phone-SH03 ", "J-PHONE/2\\.0/J-SH03.*"},
        {"J-Phone-SH02 ", "J-PHONE/2\\.0/J-SH02.*"},
        {"J-Phone-SH52", "J-PHONE/4\\.0/J-SH52.*"},
        {"J-Phone-T51", "J-PHONE/4\\.0/J-T51.*"},
        {"J-Phone-P02 ", "J-PHONE/2\\.0/J-P02.*"},
        {"J-Phone-D03 ", "J-PHONE/3\\.0/J-D03.*"},
        {"J-Phone-D05 ", "J-PHONE/3\\.0/J-D05.*"},
        {"J-Phone-D07", "J-PHONE/3\\.0/J-D07.*"},
        {"DoCoMo-N2102V", "DoCoMo/2\\.0 N2102V.*"},
        {"DoCoMo-F2102V", "DoCoMo/2\\.0 F2102V.*"},
        {"DoCoMo-D2101V", "DoCoMo/2\\.0 P2101V.*"},
        {"J-Phone-P51", "J-PHONE/4\\.0/J-P51.*"},
        {"J-Phone-N51", "J-PHONE/4\\.0/J-N51.*"},
        {"J-Phone-K51 ", "J-PHONE/4\\.0/J-K51.*"},
        {"J-Phone-T09", "J-PHONE/3\\.0/J-T09.*"},
        {"J-Phone-T08", "J-PHONE/3\\.0/J-T08.*"},
        {"J-Phone-T07", "J-PHONE/3\\.0/J-T07.*"},
        {"J-Phone-T06 ", "J-PHONE/3\\.0/J-T06.*"},
        {"DoCoMo-NM502i", "DoCoMo/1\\.0/NM502i.*"},
        {"DoCoMo-N502it", "DoCoMo/1\\.0/N502it.*"},
        {"DoCoMo-KO210i", "DoCoMo/1\\.0/KO210i.*"},
        {"DoCoMo-KO209i", "DoCoMo/1\\.0/KO209i.*"},
        {"DoCoMo-F502it", "DoCoMo/1\\.0/F502it.*"},
        {"DoCoMo-ER209i", "DoCoMo/1\\.0/ER209i.*"},
        {"DoCoMo-T2101V", "DoCoMo/2\\.0 T2101V.*"},
        {"DoCoMo-P2102V", "DoCoMo/2\\.0 P2102V.*"},
        {"DoCoMo-P2101V", "DoCoMo/2\\.0 P2101V.*"},
        {"DoCoMo-F503iS", "DoCoMo/1\\.0/F503iS.*"},
        {"DoCoMo-D503iS", "DoCoMo/1\\.0/D503iS.*"},
        {"DoCoMo-SO502i", "DoCoMo/1\\.0/SO502i.*"},
        {"DoCoMo-SH821i", "DoCoMo/1\\.0/SH821i.*"},
        {"DoCoMo-P209iS", "DoCoMo/1\\.0/P209iS.*"},
        {"J-Phone-T05 ", "J-PHONE/3\\.0/J-T05.*"},
        {"J-Phone-N05", "J-PHONE/3\\.0/J-N05.*"},
        {"J-Phone-N04 ", "J-PHONE/3\\.0/J-N04.*"},
        {"J-Phone-N03 ", "J-PHONE/3\\.0/J-N03.*"},
        {"J-Phone-K05 ", "J-PHONE/3\\.0/J-K05.*"},
        {"J-Phone-K04 ", "J-PHONE/3\\.0/J-K04.*"},
        {"J-Phone-K03 ", "J-PHONE/3\\.0/J-K03.*"},
        {"J-Phone-D31 ", "J-PHONE/3\\.0/J-D31.*"},
        {"PC-UNIX-Opera6", "Opera/6\\.0.*Linux .*"},
        {"Samsung-SGH-R210S", "SAMSUNG-SGH-R210S/.*"},
        {"Samsung-SGH-R200S", "SAMSUNG-SGH-R200S/.*"},
        {"Philips-Azalis-288", "PHILIPS-Az@lis288/.*"},
        {"DoCoMo-SO505i", "DoCoMo/1\\.0/SO505i.*"},
        {"DoCoMo-SH505i", "DoCoMo/1\\.0/SH505i.*"},
        {"DoCoMo-SO504i", "DoCoMo/1\\.0/SO504i.*"},
        {"DoCoMo-SO212i", "DoCoMo/1\\.0/SO212i.*"},
        {"DoCoMo-P504iS", "DoCoMo/1\\.0/P504iS.*"},
        {"DoCoMo-P251iS", "DoCoMo/1\\.0/P251iS.*"},
        {"DoCoMo-N504iS", "DoCoMo/1\\.0/N504iS.*"},
        {"DoCoMo-F504iS", "DoCoMo/1\\.0/F504iS.*"},
        {"DoCoMo-D251iS", "DoCoMo/1\\.0/D251iS.*"},
        {"DoCoMo-SO503i", "DoCoMo/1\\.0/SO503i.*"},
        {"DoCoMo-SO211i", "DoCoMo/1\\.0/SO211i.*"},
        {"DoCoMo-SO210i", "DoCoMo/1\\.0/SO210i.*"},
        {"DoCoMo-SH251i", "DoCoMo/1\\.0/SH251i.*"},
        {"DoCoMo-P503iS", "DoCoMo/1\\.0/P503iS.*"},
        {"DoCoMo-N503iS", "DoCoMo/1\\.0/N503iS.*"},
        {"J-Phone-D06", "J-PHONE/3\\.0/J-D06.*"},
        {"J-Phone-D04 ", "J-PHONE/3\\.0/J-D04.*"},
        {"J-Phone-T04 ", "J-PHONE/2\\.0/J-T04.*"},
        {"J-Phone-P03 ", "J-PHONE/2\\.0/J-P03.*"},
        {"Samsung-SGH-D700", "SAMSUNG-SGH-D700/.*"},
        {"SonyEricsson-P900", "SonyEricssonP900/.*"},
        {"SonyEricsson-T68i-R3", "SonyEricssonT68/R3*"},
        {"SonyEricsson-T68i-R5", "SonyEricssonT68/R5*"},
        {"DoCoMo-P2002", "DoCoMo/2\\.0 P2002.*"},
        {"DoCoMo-N2701", "DoCoMo/2\\.0 N2701.*"},
        {"DoCoMo-N2051", "DoCoMo/2\\.0 N2051.*"},
        {"DoCoMo-N2002", "DoCoMo/2\\.0 N2002.*"},
        {"DoCoMo-N2001", "DoCoMo/2\\.0 N2001.*"},
        {"DoCoMo-F2051", "DoCoMo/2\\.0 F2051.*"},
        {"SonyEricsson-T616", "SonyEricssonT616/.*"},
        {"Samsung-SGH-X608", "SAMSUNG-SGH-X608/.*"},
        {"Samsung-SGH-X600", "SAMSUNG-SGH-X600/.*"},
        {"DoCoMo-P502i", "DoCoMo/1\\.0/P502i.*"},
        {"DoCoMo-P210i", "DoCoMo/1\\.0/P210i.*"},
        {"DoCoMo-P209i", "DoCoMo/1\\.0/P209i.*"},
        {"DoCoMo-N821i", "DoCoMo/1\\.0/N821i.*"},
        {"DoCoMo-N502i", "DoCoMo/1\\.0/N502i.*"},
        {"DoCoMo-N210i", "DoCoMo/1\\.0/N210i.*"},
        {"DoCoMo-N209i", "DoCoMo/1\\.0/N209i.*"},
        {"DoCoMo-F671i", "DoCoMo/1\\.0/F671i.*"},
        {"DoCoMo-F502i", "DoCoMo/1\\.0/F502i.*"},
        {"DoCoMo-N211i", "DoCoMo/1\\.0/N211i.*"},
        {"DoCoMo-F503i", "DoCoMo/1\\.0/F503i.*"},
        {"DoCoMo-F211i", "DoCoMo/1\\.0/F211i.*"},
        {"DoCoMo-D503i", "DoCoMo/1\\.0/D503i.*"},
        {"DoCoMo-D211i", "DoCoMo/1\\.0/D211i.*"},
        {"DoCoMo-D210i", "DoCoMo/1\\.0/D210i.*"},
        {"DoCoMo-R691i", "DoCoMo/1\\.0/R691i.*"},
        {"DoCoMo-R209i", "DoCoMo/1\\.0/R209i.*"},
        {"DoCoMo-P821i", "DoCoMo/1\\.0/P821i.*"},
        {"Samsung-SGH-A400", "SAMSUNG-SGH-A400/.*"},
        {"Samsung-SGH-A300", "SAMSUNG-SGH-A300/.*"},
        {"Samsung-SGH-A110", "SAMSUNG-SGH-A110/.*"},
        {"Philips-Fisio-825", "PHILIPS-FISIO 825.*"},
        {"Philips-Fisio-820", "PHILIPS-FISIO 820.*"},
        {"Philips-Fisio-620", "PHILIPS-FISIO 620.*"},
        {"Nokia-7110", "Nokia7110 (DeckIt.*"},
        {"PocketPC-Neomar", "Rover.*Windows CE.*"},
        {"DoCoMo-N505i", "DoCoMo/1\\.0/N505i.*"},
        {"PC-Unix-Mozilla", "Mozilla/5\\.0.*X11.*"},
        {"PC-UNIX-Opera7", "Opera/7\\..*Linux .*"},
        {"Samsung-SGH-V205", "SAMSUNG-SGH-V205/.*"},
        {"Samsung-SGH-V200", "SAMSUNG-SGH-V200/.*"},
        {"Samsung-SGH-V100", "SAMSUNG-SGH-V100/.*"},
        {"Samsung-SGH-T108", "SAMSUNG-SGH-T108/.*"},
        {"Samsung-SGH-T100", "SAMSUNG-SGH-T100/.*"},
        {"Samsung-SGH-S307", "SAMSUNG-SGH-S307/.*"},
        {"Samsung-SGH-S300", "SAMSUNG-SGH-S300/.*"},
        {"Samsung-SGH-S100", "SAMSUNG-SGH-S100/.*"},
        {"Samsung-SGH-R220", "SAMSUNG-SGH-R220/.*"},
        {"Samsung-SGH-R210", "SAMSUNG-SGH-R210/.*"},
        {"Samsung-SGH-R200", "SAMSUNG-SGH-R200/.*"},
        {"Samsung-SGH-Q200", "SAMSUNG-SGH-Q200/.*"},
        {"Samsung-SGH-Q100", "SAMSUNG-SGH-Q100/.*"},
        {"Samsung-SGH-P400", "SAMSUNG-SGH-P400/.*"},
        {"Samsung-SGH-N620", "SAMSUNG-SGH-N620/.*"},
        {"Samsung-SGH-N600", "SAMSUNG-SGH-N600/.*"},
        {"Samsung-SGH-N500", "SAMSUNG-SGH-N500/.*"},
        {"Samsung-SGH-N400", "SAMSUNG-SGH-N400/.*"},
        {"Samsung-SGH-N105", "SAMSUNG-SGH-N105/.*"},
        {"Samsung-SGH-N100", "SAMSUNG-SGH-N100/.*"},
        {"Samsung-SGH-A500", "SAMSUNG-SGH-A500/.*"},
        {"DoCoMo-F505i", "DoCoMo/1\\.0/F505i.*"},
        {"DoCoMo-D505i", "DoCoMo/1\\.0/D505i.*"},
        {"DoCoMo-P504i", "DoCoMo/1\\.0/P504i.*"},
        {"DoCoMo-N504i", "DoCoMo/1\\.0/N504i.*"},
        {"DoCoMo-N251i", "DoCoMo/1\\.0/N251i.*"},
        {"DoCoMo-F661i", "DoCoMo/1\\.0/F661i.*"},
        {"DoCoMo-F504i", "DoCoMo/1\\.0/F504i.*"},
        {"DoCoMo-F251i", "DoCoMo/1\\.0/F251i.*"},
        {"DoCoMo-F212i", "DoCoMo/1\\.0/F212i.*"},
        {"DoCoMo-D504i", "DoCoMo/1\\.0/D504i.*"},
        {"DoCoMo-D251i", "DoCoMo/1\\.0/D251i.*"},
        {"DoCoMo-R692i", "DoCoMo/1\\.0/R692i.*"},
        {"DoCoMo-R211i", "DoCoMo/1\\.0/R211i.*"},
        {"DoCoMo-P503i", "DoCoMo/1\\.0/P503i.*"},
        {"DoCoMo-P211i", "DoCoMo/1\\.0/P211i.*"},
        {"DoCoMo-N503i", "DoCoMo/1\\.0/N503i.*"},
        {"DoCoMo-F210i", "DoCoMo/1\\.0/F210i.*"},
        {"DoCoMo-F209i", "DoCoMo/1\\.0/F209i.*"},
        {"DoCoMo-D502i", "DoCoMo/1\\.0/D502i.*"},
        {"DoCoMo-D209i", "DoCoMo/1\\.0/D209i.*"},
        {"DoCoMo-P501i", "DoCoMo/1\\.0/P501i.*"},
        {"DoCoMo-N501i", "DoCoMo/1\\.0/N501i.*"},
        {"DoCoMo-F501i", "DoCoMo/1\\.0/F501i.*"},
        {"DoCoMo-D501i", "DoCoMo/1\\.0/D501i.*"},
        {"Samsung-SGH-X108", "SAMSUNG-SGH-X108/.*"},
        {"Samsung-SGH-X100", "SAMSUNG-SGH-X100/.*"},
        {"Samsung-SGH-E710", "SAMSUNG-VGH-E710/.*"},
        {"Samsung-SGH-E708", "SAMSUNG-SGH-E708/.*"},
        {"Samsung-SGH-E700", "SAMSUNG-SGH-E700/.*"},
        {"Samsung-SGH-E108", "SAMSUNG-SGH-E108/.*"},
        {"Samsung-SGH-E100", "SAMSUNG-SGH-E100/.*"},
        {"NEC-515", "ATTWS/2\\.0 N515i-.*"},
        {"Trium-Mars", "Mitsu/1\\.1A.*Mars.*"},
        {"SonyEricsson-T68i-R6", "SonyEricssonT68/R6*"},
        {"SonyEricsson-T68i-R4", "SonyEricssonT68/R4*"},
        {"SonyEricsson-T68i-R2", "SonyEricssonT68/R2*"},
        {"PC-Win32-Netscape3", "Mozilla/3\\.0.*Win.*"},
        {"PC-Win32-Netscape4", "Mozilla/4\\..*Win.*"},
        {"Microsoft-WebTV", "Mozilla/4.*WebTV.*"},
        {"TV", "Mozilla/3\\.0.*TV.*"},
        {"Lexibook-TAB4000", "LEXIBOOK_TAB4000.*"},
        {"SonyEricsson-P800", "SonyEricssonP800.*"},
        {"Audiovox-CDM9100SP", "AUDIOVOX-CDM9100.*"},
        {"Audiovox-CDM9155SP", "AUDIOVOX-CDM9155.*"},
        {"PC-Unix-Mozilla", "Mozilla5\\.0.*X11.*"},
        {"PC-UNIX-Netscape4", "Mozilla/4.*X11; .*"},
        {"PC-MacOS-Netscape4", "Mozilla/4\\..*Mac.*"},
        {"SonyEricsson-T206", "SonyEricssonT206.*"},
        {"SonyEricsson-T200", "SonyEricssonT200.*"},
        {"SonyEricsson-T105", "SonyEricssonT105.*"},
        {"SonyEricsson-T100", "SonyEricssonT100.*"},
        {"Samsung-SGH-T500", "SAMSUNG-SGH-T500.*"},
        {"Samsung-SGH-T410", "SAMSUNG-SGH-T410.*"},
        {"Samsung-SGH-T400", "SAMSUNG-SGH-T400.*"},
        {"Samsung-SGH-N300", "SAMSUNG-SGH-N300.*"},
        {"Samsung-SGH-C100", "SAMSUNG-SGH-C100.*"},
        {"Samsung-SGH-A800", "SAMSUNG-SGH-A800.*"},
        {"SonyEricsson-T616", "SonyEricssonZ600.*"},
        {"SonyEricsson-T610", "SonyEricssonT610.*"},
        {"SonyEricsson-T608", "SonyEricssonT608.*"},
        {"Klondike-WAP", "Klondike/.*Win32.*"},
        {"SonyEricsson-T62", "SonyEricssonT62/.*"},
        {"SonyEricsson-T600", "SonyEricssonT600.*"},
        {"SonyEricsson-T310", "SonyEricssonT310.*"},
        {"SonyEricsson-T306", "SonyEricssonT306.*"},
        {"SonyEricsson-T300", "SonyEricssonT300.*"},
        {"SonyEricsson-T68i", "SonyEricssonT68/.*"},
        {"Ericsson-T65", "SonyEricssonT65.*"},
        {"Ericsson-T66", "SonyEricssonT66.*"},
        {"RIM-Blackberry-6710", "BlackBerry6710/.*"},
        {"RIM-Blackberry-6750", "BlackBerry6750/.*"},
        {"RIM-Blackberry-6510", "BlackBerry6510/.*"},
        {"RIM-Blackberry-6210", "BlackBerry6210/.*"},
        {"RIM-Blackberry-6230", "BlackBerry6230/.*"},
        {"RIM-Blackberry-3_6", "BlackBerry/3\\.6.*"},
        {"RIM-Blackberry-3_3", "BlackBerry/3\\.3.*"},
        {"RIM-Blackberry-3_2", "BlackBerry/3\\.2.*"},
        {"Panasonic-GD93", "Panasonic-GAD93.*"},
        {"RIM-957-Go.Web", "Go\\.Web.*RIM957.*"},
        {"RIM-Blackberry-7230", "BlackBerry7230/.*"},
        {"Panasonic-GD76", "Panasonic-GAD76.*"},
        {"Panasonic-GD75", "Panasonic-GAD75.*"},
        {"Panasonic-GD68", "Panasonic-GAD68.*"},
        {"Panasonic-GD67", "Panasonic-GAD67.*"},
        {"Panasonic-GD55", "Panasonic-GAD55.*"},
        {"Panasonic-GD35", "Panasonic-GAD35.*"},
        {"Palm-Wapman", "WAPman Version 1."},
        {"Palm-AUS-WAP", "AUR PALM WAPPER.*"},
        {"Samsung-SGH-T108", "SAMSUNG-SGHT108.*"},
        {"Samsung-SGH-T100", "SAMSUNG-SGHT100.*"},
        {"Panasonic-GD96", "Panasonic-GAD96.*"},
        {"Panasonic-GD95", "Panasonic-GAD95.*"},
        {"Sharp-GX-21", "SHARP-TQ-GX-21/.*"},
        {"Panasonic-GU87", "Panasonic-GAU87.*"},
        {"Panasonic-GD88", "Panasonic-GAD88.*"},
        {"Panasonic-GD87", "Panasonic-GAD87.*"},
        {"WinWap-30-PRO", "WinWAP 3\\.0 PRO.*"},
        {"Openwave-SDK-5_0", "OPWV1/4\\.0 UP/5.*"},
        {"WC-Excite", "ArchitextSpider.*"},
        {"WC-FAST", "FAST-WebCrawler.*"},
        {"Pixo-Handset", "Pixo-Browser/2.*"},
        {"Sharp-GX22", "SHARP-TQ-GX22/.*"},
        {"RIM-Blackberry", "BlackBerry/3\\..*"},
        {"Philips-Xenium9@9", "PHILIPS-Xenium.*"},
        {"Philips-Azalis-238", "PHILIPS-V21WAP.*"},
        {"Philips-Azalis-238", "PHILIPS-Azalis.*"},
        {"Nokia-7110", "Nokia7110/1\\.0.*"},
        {"PocketPC-Klondike", "Klondike/.*PPC.*"},
        {"ReqwirelessWeb", "RewirelessWeb/.*"},
        {"HDML-Handset", "UP\\.Browser/3\\.*"},
        {"Sharp-GX30", "SHARP-TQ-GX30/.*"},
        {"Sharp-GX20", "SHARP-TQ-GX20/.*"},
        {"Nokia-3300-2", "Nokia3300/2\\.0.*"},
        {"Nokia-3300-1", "Nokia3300/1\\.0.*"},
        {"Panasonic-GU87", "Panasonic-GU87.*"},
        {"Sharp-GX10", "SHARP-TQ-GX10/.*"},
        {"Ericsson-A2628", "EricssonA2628.*"},
        {"Nokia-8270", "NOKIA-NSD-5FX.*"},
        {"Panasonic-G50", "Panasonic-G50.*"},
        {"SAGEM-myX-5m", "SAGEM-myX-5m/.*"},
        {"SAGEM-myV-65", "SAGEM-myV-65/.*"},
        {"Philips-Fisio-311", "PHILIPS-Fisio.*"},
        {"Panasonic-GD93", "Panasonic WAP.*"},
        {"Palm-Neomar", "Neomar.*Palm;.*"},
        {"Nokia-9110i-WAP", "Nokia9110/1.0.*"},
        {"Sharp-GX1", "SHARP-TQ-GX1/.*"},
        {"Panasonic-X70", "Panasonic-X70.*"},
        {"Panasonic-X60", "Panasonic-X60.*"},
        {"Sanyo-SCP-6200", "Sanyo-SCP6200.*"},
        {"Sanyo-SCP-6000", "Sanyo-SCP6000.*"},
        {"Sanyo-SCP-5150", "Sanyo-SCP5150.*"},
        {"Sanyo-SCP-5000", "Sanyo-SCP5000.*"},
        {"Sanyo-SCP-4700", "Sanyo-SCP4700.*"},
        {"ccWAP-Browser", "ccWAP-Browser.*"},
        {"Motorola-XHTML", "Mototorola-A830"},
        {"Ericsson-R380", "Ericsson R380.*"},
        {"Trium-WAP", "Mitsu/1\\\\.1\\\\.A.*"},
        {"Ericsson-T39", "EricssonT39m/.*"},
        {"Ericsson-A2618", "EricssonA2618.*"},
        {"Ericsson-R320", "Ericsson R320.*"},
        {"WC-W3C_Validator", "W3C_Validator.*"},
        {"WC-CherryPicker", "CherryPicker.*"},
        {"WC-Lycos", "Lycos_Spider.*"},
        {"Nokia-N-Gage", "NokiaN-Gage/.*"},
        {"Samsung-SGH-C100", "SEC-SGHC100/.*"},
        {"SAGEM-myX-6", "SAGEM-myX-6/.*"},
        {"PocketPC-EZWAP", "EzWAPBrowser.*"},
        {"J-Phone-N02", "J-PHONE/1\\\\.0.*"},
        {"ME-Emulator", "Mozilla/1\\\\.22*"},
        {"Nokia-7250i", "Nokia-7250i/.*"},
        {"Nokia-7250i", "Nokia-7250I/.*"},
        {"Nokia-MIT-3-XHTML", "Rainbow/3\\\\.0.*"},
        {"Telit-GM910i", "Telit-GM910i.*"},
        {"Ericsson-T60d", "EricssonT60d.*"},
        {"Ericsson-T39", "EricssonT39/.*"},
        {"SonyEricsson-T200", "EricssonT200.*"},
        {"SonyEricsson-WAP", "SonyEricsson.*"},
        {"SAGEM-myX-5", "SAGEM-myX-5/.*"},
        {"Philips-Ozeo-8@8", "PHILIPS-Ozeo.*"},
        {"Panasonic-WAP", "PanasonicWAP.*"},
        {"Ericsson-T20", "EricssonT20R.*"},
        {"Ericsson-R600", "EricssonR600.*"},
        {"Ericsson-R520", "EricssonR520.*"},
        {"Ericsson-R320", "EricssonR320.*"},
        {"Alcatel-OT-511", "Alcatel-BF3/.*"},
        {"Alcatel-OT-501", "Alcatel-BE5/.*"},
        {"Alcatel-OT-303", "Alcatel-BE4/.*"},
        {"Alcatel-OT-512", "Alcatel-BF4.*"},
        {"Palm-Neomar", "Rover.*Palm.*"},
        {"Samsung-SGH-X608", "SEC-SGHX608.*"},
        {"Samsung-SGH-X600", "SEC-SGHX600.*"},
        {"Samsung-SGH-X108", "SEC-SGHX108.*"},
        {"Samsung-SGH-X100", "SEC-SGHX100.*"},
        {"Samsung-SGH-E710", "SEC-SGHE710.*"},
        {"Samsung-SGH-E708", "SEC-SGHE708.*"},
        {"Samsung-SGH-E700", "SEC-SGHE700.*"},
        {"Samsung-SGH-E108", "SEC-SGHE108.*"},
        {"Samsung-SGH-E100", "SEC-SGHE100.*"},
        {"Nokia-7250i", "Nokia7250I/.*"},
        {"LG-LX5350", "LGE-LX5350/.*"},
        {"Trium-WAP", "Mitsu/1\\.1A.*"},
        {"Samsung-SGH-S300", "SEC-SGHS300.*"},
        {"Samsung-SGH-S105", "SEC-SGHS105.*"},
        {"Samsung-SGH-S100", "SEC-SGHS100.*"},
        {"Samsung-SGH-Q200", "SEC-SGHQ200.*"},
        {"Samsung-SGH-P400", "SEC-SGHP400.*"},
        {"Samsung-SGH-N500", "SEC-SGHN500.*"},
        {"Samsung-SGH-N350", "SEC-SGHN350.*"},
        {"Samsung-SGH-A500", "SEC-SGHA500.*"},
        {"Samsung-SGH-A310", "SEC-scha310.*"},
        {"RIM-957-Neomar", "Rover .*RIM.*"},
        {"RIM-957-Neomar", "Neomar.*RIM.*"},
        {"Samsung-SPH-N300", "SEC-SPHN300.*"},
        {"Samsung-SPH-N200", "SEC-SPHN200.*"},
        {"Samsung-SPH-A400", "SEC-spha400.*"},
        {"Samsung-SPH-A400", "SEC-SPHA400.*"},
        {"Samsung-SGH-V205", "SEC-SGHV205.*"},
        {"Samsung-SGH-V200", "SEC-SGHV200.*"},
        {"Samsung-SGH-T500", "SEC-SGHT500.*"},
        {"Samsung-SGH-T400", "SEC-SGHT400.*"},
        {"Samsung-SGH-S307", "SEC-SGHS307.*"},
        {"Telit-GM822", "Telit-GM822.*"},
        {"Nokia-8910i", "Nokia8910i/.*"},
        {"Nokia-6310i", "Nokia6310i/.*"},
        {"Motorola-T720c", "MOT-1\\.2\\.2.*"},
        {"Ericsson-T68", "EricssonT68.*"},
        {"Ericsson-T66", "EricssonT66.*"},
        {"Ericsson-T65", "EricssonT65.*"},
        {"Ericsson-T29", "EricssonT29.*"},
        {"Alcatel-OT-715", "Alcatel-BF5.*"},
        {"Ericsson-A1228", "ERICY-A1228.*"},
        {"Alcatel-OT-DB@", "Alcatel-BE3.*"},
        {"Alcatel-OT-535", "Alcatel-BH4.*"},
        {"Alcatel-OT-525", "Alcatel-BG3.*"},
        {"WC-BaiDu", "BaiDuSpider.*"},
        {"WC-Euroseek", "Arachnoidea.*"},
        {"WC-NetNames", "Namecrawler.*"},
        {"WC-Alexa", "ia_archive.*"},
        {"WC-WebCrawler", "WebCrawler.*"},
        {"Nokia-3600", "Nokia3600/.*"},
        {"Nokia-3620", "Nokia3620/.*"},
        {"Nokia-3660", "Nokia3660/.*"},
        {"Nokia-8390", "Nokia8390/.*"},
        {"Nokia-8310", "Nokia8310/.*"},
        {"Nokia-5510", "Nokia5510/.*"},
        {"Nokia-5210", "Nokia5210/.*"},
        {"Nokia-3360", "Nokia3360/.*"},
        {"Nokia-3330", "Nokia3330/.*"},
        {"NEC-DB7000", "NEC-DB7000.*"},
        {"Motorola-V120c", "MOT-P2K-C/.*"},
        {"Motorola-T720i", "MOT-T720i/.*"},
        {"Nokia-7190", "Nokia7190/.*"},
        {"Nokia-7160", "Nokia7160/.*"},
        {"Nokia-7110", "Nokia 7110.*"},
        {"J-Phone-V4", "J-PHONE/4\\.*"},
        {"J-Phone-V3", "J-PHONE/3\\.*"},
        {"J-Phone-V2", "J-PHONE/2\\.*"},
        {"J-Phone-V1", "J-PHONE/1\\.*"},
        {"Nokia-6230", "Nokia6230/.*"},
        {"Nokia-7600", "Nokia7600/.*"},
        {"Nokia-7200", "Nokia7200/.*"},
        {"Nokia-6820", "Nokia6820/.*"},
        {"Nokia-6810", "Nokia6810/.*"},
        {"Nokia-6220", "Nokia6220/.*"},
        {"Nokia-6200", "Nokia6200/.*"},
        {"Nokia-6108", "Nokia6108/.*"},
        {"Nokia-3108", "Nokia3108/.*"},
        {"Wapalizer", "Wapalizer/.*"},
        {"Nokia-6250", "Nokia6250/.*"},
        {"Nokia-6210", "Nokia 6210.*"},
        {"Nokia-6210", "Nokia6210/.*"},
        {"Nokia-7250", "Nokia7250/.*"},
        {"Nokia-6800", "Nokia6800/.*"},
        {"Nokia-6100", "Nokia6100/.*"},
        {"Nokia-5100", "Nokia5100/.*"},
        {"Nokia-6510", "Nokia6510/.*"},
        {"Nokia-6340", "Nokia6340/.*"},
        {"Nokia-6310", "Nokia6310/.*"},
        {"Nokia-3585i", "Nokia3585i.*"},
        {"Nokia-3530", "Nokia3530/.*"},
        {"Nokia-3510i", "Nokia3510i.*"},
        {"Nokia-8910", "Nokia8910/.*"},
        {"Motorola-T280i", "MOT-T280M/.*"},
        {"LG-TP5250", "LGE-TP5250.*"},
        {"Ericsson-T60c", "ERICY-T60c.*"},
        {"Ericsson-R278", "ERICY-R278.*"},
        {"Nokia-6600", "Nokia6600/.*"},
        {"Telme-T919", "TELME_T919.*"},
        {"Nokia-7650", "Nokia7650/.*"},
        {"Nokia-3650", "Nokia3650/.*"},
        {"LG-4NE", "LGE-DB230.*"},
        {"LG-DM120", "LGE-DM120.*"},
        {"LG-TP5250", "LGE-DB520.*"},
        {"Motorola-C333", "MOT-ta02/.*"},
        {"Nokia-3350", "Nokia3350.*"},
        {"Nokia-3510", "Nokia3510.*"},
        {"Nokia-6610", "Nokia6610.*"},
        {"XHTML-Handset", "MML-Handset"},
        {"PC", "Mozilla/1.*"},
        {"Siemens-IC35", "SIE-IC35/.*"},
        {"Nokia-6650", "Nokia6650.*"},
        {"Nokia-3300", "Nokia3300.*"},
        {"Nokia-3200", "Nokia3200.*"},
        {"Nokia-3100", "Nokia3100.*"},
        {"Nokia-6590", "Nokia6590.*"},
        {"Nokia-3595", "Nokia3595.*"},
        {"Nokia-3590", "Nokia3590.*"},
        {"Motorola-V600", "MOT-V600/.*"},
        {"Motorola-V525", "MOT-V525/.*"},
        {"Motorola-E365", "MOT-E365/.*"},
        {"PC", "Mozilla/2.*"},
        {"PC", "Mozilla/4.*"},
        {"PC", "Mozilla/5.*"},
        {"PC", "Mozilla/3.*"},
        {"Siemens-SL45i", "SIE-SLIK/.*"},
        {"Siemens-SL45", "SIE-SL45/.*"},
        {"Siemens-ME45", "SIE-ME45/.*"},
        {"Siemens-6618", "SIE-6618/.*"},
        {"Siemens-MT50", "SIE-MT50/.*"},
        {"Siemens-M50i", "SIE-M50I/.*"},
        {"Siemens-3118", "SIE-3118/.*"},
        {"Siemens-CL50", "SIE-CL50/.*"},
        {"Siemens-2128", "SIE-2128/.*"},
        {"Siemens-MC60", "SIE-MC60/.*"},
        {"Panasonic-WAP", "Panasonic.*"},
        {"Palm-Palmscape", "Palmscape.*"},
        {"Palm-Kbrowser", "KBrowser .*"},
        {"Nokia-7210", "Nokia7210.*"},
        {"Nokia-3610", "Nokia3610.*"},
        {"Nokia-3410", "Nokia3410.*"},
        {"Motorola-T720", "MOT-T720/.*"},
        {"Motorola-E360", "MOT-E360/.*"},
        {"LG-DM515", "LGE-DM515.*"},
        {"PC-Win32-Opera5", "Opera/5\\..*"},
        {"WC-SlySearch", "SlySearch.*"},
        {"WC-Infoseek", "Ultraseek.*"},
        {"WC-Google", "Googlebot.*"},
        {"PC-Win32-Opera6", "Opera/6\\..*"},
        {"waptv-6", "WapTV/6\\..*"},
        {"waptv-5", "WapTV/5\\..*"},
        {"waptv-4", "WapTV/4\\..*"},
        {"WC-Infoseek", "Infoseek.*"},
        {"WC-WiseWire", "WiseWire.*"},
        {"BeVocal", "BeVocal/.*"},
        {"BeVocal", "Java1\\.3.*"},
        {"Movistar-TSM-100", "TSM-100/.*"},
        {"Ericsson-WAP", "Ericsson.*"},
        {"Siemens-S46", "SIE-S46/.*"},
        {"Siemens-M46", "SIE-M46/.*"},
        {"Siemens-C45", "SIE-C45/.*"},
        {"Siemens-S45", "SIE-S45/.*"},
        {"PocketPC-MTDS", "MTDS Wap.*"},
        {"Motorola-C450", "MOT-C450.*"},
        {"NEC-e525", "NEC-e525.*"},
        {"Motorola-V.66i", "MOT-V66M.*"},
        {"Motorola-V.66", "MOT-SAP4.*"},
        {"Motorola-V.60i", "MOT-V60M.*"},
        {"Motorola-V.60c", "MOT-2000.*"},
        {"Motorola-V.60", "MOT-PHX4.*"},
        {"Motorola-T280", "MOT-PAN4.*"},
        {"Motorola-C350", "MOT-c350.*"},
        {"Motorola-C200", "MOT-C200.*"},
        {"Siemens-A50", "SIE-A50/.*"},
        {"Siemens-M50", "SIE-M50/.*"},
        {"Siemens-C35", "SIE-C3I/.*"},
        {"Siemens-SL55", "SIE-SL55.*"},
        {"Sanyo-SCP-4700", "SCP-4700.*"},
        {"Motorola-V600", "MOT-V600.*"},
        {"Motorola-V525", "MOT-V500.*"},
        {"Motorola-V525", "MOT-V525.*"},
        {"LG-G8000", "LG-G8000.*"},
        {"LG-G7010", "LG-G7010.*"},
        {"LG-G7000", "LG-G7000.*"},
        {"LG-G5200", "LG-G5200.*"},
        {"Siemens-SX1", "SIE-SX1.*"},
        {"Alcatel-WAP", "Alcatel.*"},
        {"Kyocera-QCP-2345", "QC-2345.*"},
        {"Kyocera-QCP-2255", "QC-2255.*"},
        {"Samsung-WAP", "SAMSUNG.*"},
        {"SAGEM-WA3020", "SAGEM-3.*"},
        {"SAGEM-MW939", "SAGEM-9.*"},
        {"Philips-WAP", "PHILIPS.*"},
        {"PocketPC-AnnyWay", "AnnyWAP.*"},
        {"Motorola-A008", "MOT-F8/.*"},
        {"Motorola-A008", "MOT-F6/.*"},
        {"Motorola-A6188", "MOT-CF/.*"},
        {"NEC-515", "NEC-515.*"},
        {"Siemens-C56", "SIE-C56.*"},
        {"Siemens-C55", "SIE-C55.*"},
        {"Siemens-A55", "SIE-A55.*"},
        {"Siemens-S56", "SIE-S56.*"},
        {"Siemens-S55", "SIE-S55.*"},
        {"Siemens-M55", "SIE-M55.*"},
        {"Yospace-WAP", "YOSPACE.*"},
        {"Motorola-i95cl", "MOT-85/.*"},
        {"Motorola-i30sx", "MOT-87/.*"},
        {"Motorola-V70", "MOT-V70.*"},
        {"Motorola-V.50", "MOT-F0/.*"},
        {"Motorola-V.2288", "MOT-C4/.*"},
        {"Motorola-V.100", "MOT-C2/.*"},
        {"Motorola-Ti260", "MOT-AF/.*"},
        {"RIM-5800-4thpass", "nopattern"},
        {"Siemens-S40", "SIE-S40.*"},
        {"Siemens-S35", "SIE-S35.*"},
        {"MML-Handset", "J-PHONE.*"},
        {"Motorola-A009", "MOT-BC/.*"},
        {"i-mode-Handset", "DoCoMo/.*"},
        {"Motorola-Ti250", "MOT-D8/.*"},
        {"Motorola-P7389", "MOT-CB/.*"},
        {"Motorola-C330", "MOT-C33.*"},
        {"Motorola-120X", "mot-p2k.*"},
        {"WC-ASPSeek", "ASPSeek.*"},
        {"WC-AltaVista", "Scooter.*"},
        {"WC-AltaVista", "scooter.*"},
        {"Kyocera-QCP-2119", "QC2119.*"},
        {"Motorola-A388", "MOT-FE.*"},
        {"Motorola-T192", "MOT-F5.*"},
        {"Motorola-T191", "MOT-D5.*"},
        {"Sanyo-WAP", "Sanyo-.*"},
        {"Sharp-XHTML", "SHARP-.*"},
        {"WinWAP", "WinWAP.*"},
        {"Ericsson-WapIDE", "WapIDE.*"},
        {"Nokia-3280", "NOK-01.*"},
        {"Motorola-i85s", "MOT-32.*"},
        {"Nokia-WAP", "Nokia.*"},
        {"Telit-GM822", "GM822.*"},
        {"Telit-WAP", "Telit.*"},
        {"Nokia-3610", "N3610.*"},
        {"Nokia-3350", "N3350.*"},
        {"Telit-GM832", "GM832.*"},
        {"PC-UNIX-Lynx", "Lynx/.*"},
        {"Siemens-SL45", "SL-45.*"},
        {"Samsung-SPH-I300", "SEC03.*"},
        {"SAGEM-WAP", "SAGEM.*"},
        {"UP-Simulator-Generic", "OPWV1.*"},
        {"Trium-WAP", "Mitsu.*"},
        {"WC-HotBot", "Slurp.*"},
        {"WC-PlanetSearch", "fido/.*"},
        {"Sky-waptv", "WapTV.*"},
        {"Ericsson-R320", "R320.*"},
        {"Kyocera-WAP", "QCP-.*"},
        {"Ericsson-R600", "R600.*"},
        {"Siemens-WAP", "SIE-.*"},
        {"UP-Simulator-Generic", "UPG1.*"},
        {"UP-Simulator-Generic", "OWG1.*"},
        {"Motorola-WAP", "MOT-.*"},
        {"Ericsson-R380", "R380.*"},
        {"Ericsson-R520", "R520.*"},
        {"LG-WAP", "LGE-*"},
        {"NDS-Browser", "NDS.*"},
        {"Kyocera-WAP", "QC.*"}

    };

    private String[] userAgents = {
        "Mozilla/3.0 (Liberate DTV 1.1)",
        "Mozilla/3.0 (Liberate DTV 1.2)",
        "Mozilla/3.0 ( Liberate; DTV; 1_3; WIN32; GenericProvider )",
        "Mozilla/3.04 (compatible; NCBrowser/2.35; ANTFresco/2.17; RISC OS-NC 5.13 Laz1UK1309)",
        "Mozilla/2.0 (compatible; Go.Web/6.0; HandHTTP 1.1; Elaine/1.0; RIM957 )",
        "Mozilla/2.0 (compatible; Go.Web/6.0; HandHTTP 1.1; Elaine/1.0; RIM950 )",
        "Mozilla/1.0[en] ; Go.Web.* (compatible MSIE 2.0; HandHTTP 1.1; Palm).",
        "Mozilla/1.22 (compatible; EudoraWeb 2.0; pdQbrowser; PalmOS 2.0)",
        "Mozilla/1.22 (compatible; MSIE 5.01; PalmOS .*) EudoraWeb 2)",
        "Mozilla/1.22 (compatible; MMEF20; Cellphone; Sony CMD-Z5)",
        "Mozilla/1.22 (compatible; MMEF20; Cellphone; Sony CMD-J5)",
        "Mozilla/1.22 (compatible; MMEF20; Cellphone; Sony CMD-Z5)",
        "Mozilla/4.0 (MSIE 5.0 compatible; Spyglass DM 4.0; Linux)",
        "Nokia-Communicator-WWW-Browser/3.0 (Geos 3.0 Nokia-9110)",
        "Mozilla/2.0 (compatible; MSIE 3.02; Windows CE; 240x320)",
        "Mozilla/1.22 (compatible; MMEF20; Cellphone; Benefon Q)",
        "Mozilla/4.51 (compatible; Opera 3.62; EPOC; 480x160)",
        "Mozilla/3.04 (compatible; NCBrowser/.*; ANTFresco.xx",
        "Mozilla/4.0 (compatible; MSIE 4.01; .* Windows CE)",
        "Mozilla/1.1 (compatible; MSPIE 1.1; Windows CE)",
        "Mozilla/4.0 (compatible; MSIE 5..*; Mac_PowerPC",
        "Mozilla/.* (compatible; Opera/3..*; Windows",
        "Mozilla/4.0 (compatible; MSIE 4..* Mac",
        "Microsoft Pocket Internet Explorer/0.6",
        "WAPman Version 1.8:Build P2001020800",
        "Mozilla/2.0 (compatible; Elaine/1.1)",
        "UPG1 UP/4.0 (compatible; Blazer .*)",
        "EPOC32-WTL/.*Crystal/6.0 STNC-WTL/6.xx",
        "Mozilla/4.0 (compatible; MSIE 5.5.xx",
        "EPOC32-WTL/2.0 (RVGA) STNC-WTL/2.0.xx",
        "Mozilla/3.0 (compatible; .*AvantGo.xx",
        "Mozilla/3.0 ( Liberate; DTV; 1_3;.xx",
        "Mozilla/4.0 (compatible; MSIE 6..xx",
        "Mozilla/4.0 (compatible; MSIE 5..xx",
        "Mozilla/4.0 (compatible; MSIE 4..xx",
        "Mozilla/3.01 (compatible; Netgem/.xx",
        "Mozilla/3.01 (compatible; Netbox/.xx",
        "Mozilla/3.0 (Liberate DTV 1.2).xx",
        "Mozilla/4.73.*Windows.*Opera 4..xx",
        "Mozilla/5.0 (X11; .* Netscape6/6.xx",
        "Nokia9210/1.0 Symbian-Crystal/6.xx",
        "Mozilla/4.0 (compatible; Oligo .x",
        "Mozilla/3.0 (Liberate DTV 1.1.x",
        "Mozilla/5 .*X11; .*Netscape6/.x",
        ".*Catspaw/0.98b(RV) War/0.78b.x",
        "Mozilla/4.0.*X11.* Opera 5.x",
        "Mozilla/3.0 (Slurp/.*inktomi.x",
        "Mozilla/4.0.*X11.* Opera 4.x",
        "Mozilla/4.7 (Win98; Audrey).xxxxx",
        "Mozilla/1.22 (compatible;*).x",
        "Mozilla/.*Windows.*Opera 5.x",
        "Mozilla/5.0 .* Netscape6/6.x",
        "Mozilla/4.*X11; .*Netscape.x",
        "AUR PALM WAPPER (WAP 1.1).x",
        "Mozilla/4.0 BT Multiphon.x",
        "Mozilla5.0.*X11;.*Gecko.x",
        "YourWap Motorola 7389/.x",
        "Mozilla/3.0 NAVIO.*AOL.x",
        "Mozilla/3.0 (DreamKey/.xx",
        "Nokia-WAP-Toolkit/2.0.xx",
        "EzWAPBrowserCE1.0-WAP.xx",
        "Nokia-WAP-Toolkit/2.1.xx",
        "YourWap Ericsson 380/.xx",
        "4thpass.com KBrowser .xx",
        "YourWap Siemens SL45/.xx",
        "Nokia7110/1.0 (04.67).xx",
        ".*Catspaw/.*(RV) War/.xx",
        "Mitsu/1.1.A.*Mondo.xx",
        "EzWAPBrowser2.0-WAP.xx",
        "Mitsu/1.1.A.*Mars.xx",
        "YourWap Nokia 6210/.xx",
        "EzWAPBrowser1.0-WAP.xx",
        "Mozilla/.* Elaine/.xx",
        "Mitsu/1.1.A.*Geo.xx",
        "WAPman Version 1..xx",
        "SAMSUNG-SGH-N100/.x",
        "SAMSUNG-SGH-A300/.x",
        "Mozilla/4..* Mac.x",
        "SAMSUNG.SGH-A110/.x",
        "Nokia7110 (DeckIt.x",
        "Ericsson T20/R2A.x",
        "Mitsu/1.1A.*Mars.x",
        "Mozilla/4..*Win.x",
        "Mozilla/4.*X11; .x",
        "AUR PALM WAPPER.x",
        "R380 2.0 WAP1.1.x",
        "Mitsu/1.1A.*Geo.x",
        "Mozilla/.*WebTV.x",
        "EricssonT20/R2A.x",
        "Mozilla/3.0.*TV.x",
        "PHILIPS-Azalis.x",
        "PHILIPS-V21WAP.x",
        "PHILIPS-Xenium.x",
        "WinWAP 3.0 PRO.x",
        "Pixo-Browser/2.x",
        "Mozilla/1.22*.x",
        "Ericsson R380.x",
        "Neomar.*Palm;.x",
        "Mitsu/1.1.A.x",
        "EricssonA2628.x",
        "Ericsson R320.x",
        "Panasonic WAP.x",
        "ccWAP-Browser.x",
        "Nokia7110/1.0.x",
        "Nokia9110/1.0.x",
        "BeVocal/1.0.x",
        "SIE-IC35/1.0.x",
        "Alcatel-BE5/.x",
        "PanasonicWAP.x",
        "Telit-GM910i.x",
        "Mozilla/3.0.x",
        "EricssonR520.x",
        "PHILIPS-Ozeo.x",
        "Alcatel-BE4/.x",
        "Mitsu/1.1.A.x",
        "Neomar.*RIM.x",
        "Java1.2.1.x",
        "Nokia 8310/.x",
        "Nokia 6250/.x",
        "Rover .*RIM.x",
        "Java1.3.1.x",
        "Alcatel-BE3.x",
        "Mitsu/1.1A.x",
        "Nokia6210/.x",
        "Nokia 6210.x",
        "Nokia 3330.xx",
        "Nokia6250/.xx",
        "Palmscape.xx",
        "Opera/5..xx",
        "WapTV/3..xx",
        "Nokia3330.xx",
        "SIE-SL45/.xx",
        "KBrowser .xx",
        "Mozilla/5.xx",
        "Mozilla/4.xx",
        "SIE-S45/.xx",
        "MTDS Wap.xx",
        "SIE-S40.xx",
        "DoCoMo/.xx",
        "MOT-C4/.xx",
        "MOT-D8/.xx",
        "MOT-CB/.xx",
        "SIE-S35.xx",
        "MOT-F0/.xx",
        "WapTV 1.xx",
        "WapTV 2.xx",
        "SAGEM-9.xx",
        "MOT-F6/.xx",
        "MOT-DC/.xx",
        "MOT-CF/.xx",
        "MOT-BC/.xx",
        "SAGEM-3.xx",
        "MOT-C2/.xx",
        "MOT-AF/.xx",
        "WinWAP.xx",
        "SIE-C3.xx",
        "Tellme.xx",
        "GM832.xx",
        "ERK0.xx",
        "UPG1.xx",
        "ALAZ.xx",
        "OWG1.xx",
        "NDS.xx"
    };
    private static final DeviceRepositoryAccessorFactory REPOSITORY_ACCESSOR_FACTORY = DeviceRepositoryAccessorFactory.getDefaultInstance();

    /**
     * Test the constructor.
     */
    public void testDevicePattern() throws Exception {

        DevicePattern devicePattern;
        try {
            devicePattern = new DevicePattern(null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

    }

    public void testDevicePatternConstructorWhenDeviceNameNull() {
        DevicePattern devicePattern;
        try {
            devicePattern = new DevicePattern("", null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testDevicePatternConstructorWhenPatternNull() {
        DevicePattern devicePattern;
        try {
            devicePattern = new DevicePattern(null, "");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }


    /**
     * Given the current list of known patterns, create a DevicePattern for each
     * of them. No exceptions should be thrown.
     * @throws Exception
     */
    // SEE NOTE ABOVE AS TO WHY THIS WAS DISABLED.
    public void NOtestDevicePatternsValid() throws Exception {
        int invalid = 0;
        for (int i = 0; i < deviceNamePatterns.length; i++) {
            String deviceName = deviceNamePatterns[i][0];
            String deviceNamePattern = deviceNamePatterns[i][1];
            try {
                new DevicePattern(deviceNamePattern, deviceName);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid pattern: " + deviceNamePattern);
                ++invalid;
            }
        }
        if (invalid > 0) {
            System.out.println(invalid + " out of " + deviceNamePatterns.length +
                    " patterns are invalid.");
            fail("Invalid patterns found.");
        }
    }

    /**
     * Ensure that the latest device repository has duplicate secondary
     * header ID regular expressions for each one starting with X-WAP-PROFILE.
     */
    // SEE NOTE ABOVE AS TO WHY THIS WAS DISABLED.
    public void NOtestLatestDeviceRepository() throws Exception {
        DeviceRepositoryFactory factory =
                DeviceRepositoryFactory.getDefaultInstance();

        // NOTE: THIS IS VERY SLOW AS THE LATEST DEVICES FILE IS HUGE.
        // IT HAS BEEN DELETED FOR THE MOMENT. IF YOU WANT TO TEST THIS YOU
        // NEED TO ADD YOUR OWN MANUALLY.
        File deviceRepositoryFile = extractTempZipFromJarFile(
                DevicePatternTestCase.class,
                "latest-devices.mdpr",
                DeviceRepositoryConstants.REPOSITORY_EXTENSION);

        URL deviceRepositoryUrl = deviceRepositoryFile.toURL();

        DeviceRepository deviceRepository =
                factory.getDeviceRepository(deviceRepositoryUrl, null);

        List devicesWithXWAPProfileSecondaryIDs = new ArrayList();
        devicesWithXWAPProfileSecondaryIDs.add("Palm-Blazer");
        devicesWithXWAPProfileSecondaryIDs.add("Blazer-3_0");
        devicesWithXWAPProfileSecondaryIDs.add("XHTML-Handset");
        devicesWithXWAPProfileSecondaryIDs.add("Samsung-SprintPCS");

        List devices = deviceRepository.getDevices("*");
        for (int i = 0; i < devices.size(); i++) {
            DefaultDevice device = (DefaultDevice) deviceRepository.getDevice(
                    (String)devices.get(i));
            if (device.getSecondaryIDHeaderName() != null) {
                if (DevicesHelper.X_WAP_PROFILE.equals(
                        device.getSecondaryIDHeaderName())) {
                    assertTrue("Secondary device ID should match",
                            devicesWithXWAPProfileSecondaryIDs.contains(
                                    device.getName()));
                 }
            }
            // The map of is keyed on the regular expression which maps to the
            // revision number. This always seems to be 0 in an XML repository.
            Map patterns = device.getPatterns();
            if (patterns != null) {
                Set keys = patterns.keySet();
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String regularExpression = (String) iterator.next();
                    if (regularExpression.startsWith(
                            DevicesHelper.X_WAP_PROFILE)) {
                        // if this device has a pattern starting with X-WAP-PROFILE
                        // then it should have an the same regular expression
                        // starting with 'Profile: xxxx'.
                        String expr = DevicesHelper.UAPROF_PREFIX +
                                regularExpression.substring(
                                        DevicesHelper.X_WAP_PROFILE.length());
                        assertTrue("Expression should exist",
                                keys.contains(expr));
                    }
                }
            }
        }
    }

    /**
     * Enable this test to test the matching of device patterns and user agent
     * strings using the specific repository.
     */
    // SEE NOTE ABOVE AS TO WHY THIS WAS DISABLED.
    public void NOtestDeviceCacheRepository() {
//        Map properties = new HashMap();
//        properties.put("host", "mackerel");
//        properties.put("port", "1526");
//        properties.put("vendor", "Oracle");
//        properties.put("source", "TTDB1");
//        properties.put("user", "supportdsb");
//        properties.put("password", "supportdsb");
//        properties.put("standardDeviceProjectName", "mobile-portal");
//
//        LocalRepositoryConnection connection1 = null;
//        LocalRepositoryConnection connection2 = null;
//        LocalRepository repository1 = null;
//        LocalRepository repository2 = null;
//
//        DeviceRepositoryLocation location =
//                new DeviceRepositoryLocationImpl("mobile-portal");
//
//        try {
//            repository1 = JDBCRepository.createRepository(properties).getLocalRepository();
//            repository2 = JDBCRepository.createRepository(properties).getLocalRepository();
//
//            connection1 = (LocalRepositoryConnection) repository1.connect();
//            connection2 = (LocalRepositoryConnection) repository2.connect();
//
//            connection1.beginOperationSet();
//            connection2.beginOperationSet();
//
//            doTest(connection1, connection2, location, location);
//
//            connection1.endOperationSet();
//            connection2.endOperationSet();
//        } catch (RepositoryException re) {
//            re.printStackTrace();
//            try {
//                if (connection1 != null) {
//                    connection1.abortOperationSet();
//                }
//                if (connection2 != null) {
//                    connection2.abortOperationSet();
//                }
//            } catch (RepositoryException re2) {
//                System.err.println("Nested Exception");
//                re2.printStackTrace();
//            }
//        } finally {
//            try {
//                if (repository1 != null) {
//                    repository1.disconnect(connection1);
//                }
//                if (repository2 != null) {
//                    repository2.disconnect(connection2);
//                }
//            } catch (RepositoryException re3) {
//                re3.printStackTrace();
//            }
//            System.err.flush();
//            System.out.flush();
//        }
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException ie) {
//        }
//
//        System.exit(1);
    }

    // tries to match a user agent string against devices in the devicePattern
    // cache and then the repository.
    protected void doTest(
            LocalRepositoryConnection connection1,
            LocalRepositoryConnection connection2,
            DeviceRepositoryLocation location1,
            DeviceRepositoryLocation location2)
            throws RepositoryException {

        String ua, cDeviceName, rDeviceName;
        boolean successful = true;

        DeviceRepositoryAccessor cacheAccessor =
            REPOSITORY_ACCESSOR_FACTORY.createDeviceRepositoryAccessor(
                connection1.getLocalRepository(), location1, null);

        cacheAccessor.initializeDevicePatternCache(connection1);

        DeviceRepositoryAccessor repositoryAccessor =
            REPOSITORY_ACCESSOR_FACTORY.createDeviceRepositoryAccessor(
                connection2.getLocalRepository(), location2, null);

        try {
            System.out.println("Summary");
            System.out.println("-------");
            for (int i = 0; i < userAgents.length; i++) {
                ua = userAgents[i];
                if ("".equals(ua.trim()) || ua.trim().startsWith("#")) {
                    continue;
                }

                cDeviceName = cacheAccessor
                        .retrieveMatchingDeviceName(ua);

                rDeviceName = repositoryAccessor
                        .retrieveMatchingDeviceName(ua);

                System.out.println("\n");
                if (((cDeviceName != null && rDeviceName != null) &&
                        !cDeviceName.equals(rDeviceName)) ||
                        cDeviceName == null && rDeviceName != null ||
                        rDeviceName == null && cDeviceName != null) {
                    successful = false;
                    System.out.println("***********************************************");
                    System.out.println("ALERT ALERT ALERT CACHE DIFFERS FROM REPOSITORY");
                    System.out.println("***********************************************");
                }

                if (cDeviceName == null) {
                    System.out.println("CACHE:      " + ua + " does not match any device");
                } else {
                    System.out.println("CACHE:      " + ua + " matches " + cDeviceName);
                }

                if (rDeviceName == null) {
                    System.out.println("REPOSITORY: " + ua + " does not match any device");
                } else {
                    System.out.println("REPOSITORY: " + ua + " matches " + rDeviceName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n\n\n********************************************************");
        if (successful) {
            System.out.println("***************** The test SUCCEEDED ********************");

        } else {
            System.out.println("***************** The test FAILED **********************");
        }
        System.out.println("********************************************************");

    }

    /**
     * Test the replace method in the DevicePattern class.
     */
    public void testReplace() throws Exception {
        DevicePattern pattern = new DevicePattern("", "");
        String result;

        result = pattern.replace("", '(', "new");
        assertEquals("Value should match", "", result);

        result = pattern.replace("(", '(', "77");
        assertEquals("Value should match", "77", result);

        result = pattern.replace("((", '(', "--");
        assertEquals("Value should match", "----", result);

        result = pattern.replace("(a(", '(', "--");
        assertEquals("Value should match", "--a--", result);

        result = pattern.replace("(a(b", '(', "--");
        assertEquals("Value should match", "--a--b", result);

        result = pattern.replace("c(a(b", '(', "--");
        assertEquals("Value should match", "c--a--b", result);

        result = pattern.replace("c(a(b", '(', "");
        assertEquals("Value should match", "cab", result);

        result = pattern.replace("abcdefghijklmnopqrstuvwxyz", 'g', "G");
        assertEquals("Value should match", "abcdefGhijklmnopqrstuvwxyz", result);

        result = pattern.replace("The fox jumped over the red fence", 'e', "");
        assertEquals("Value should match", "Th fox jumpd ovr th rd fnc", result);

        result = pattern.replace("c\\(a\\(b", '(', "");
        assertEquals("Value should match: " + result, "c\\(a\\(b", result);

        result = pattern.replace("\\(", '(', "zz");
        assertEquals("Value should match: " + result, "\\(", result);

        result = pattern.replace("\\((", '(', "zz");
        assertEquals("Value should match: " + result, "\\(zz", result);

    }
    /**
     * Extract the zip file from a jar file (copy zip file to temporary
     * directory). Note that the returned file provides access to this
     * temporary file, which is itself marked for deletion on JVM exit.
     * <p>
     * NOTE: ResourceTemporaryFileCreator does the same thing, but does not rely
     * on deleteOnExit().
     *
     * @param clazz the class who's <code>getResource</code> method will be
     *              invoked to access the named zip file
     * @param zipFilename The name of the zip file to extract.
     * @param suffix The file suffix for the extracted zip file (e.g. zip).
     * @return the file that points to the copied zip file.
     */
    public static File extractTempZipFromJarFile(Class clazz,
                                                 String zipFilename,
                                                 String suffix)
            throws Exception {
        URL url = clazz.getResource(zipFilename);

        // Copy the zip file to a local temporary location.
        InputStream in = url.openConnection().getInputStream();

        File file = File.createTempFile("testZipFile", "." + suffix,
                new File(System.getProperty("java.io.tmpdir")));

        FileOutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();

        file.deleteOnExit();

        return file;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9860/1	rgreenall	VBM:2005092211 Fixed identification of DoCoMo devices.

 10-Oct-05	9712/2	rgreenall	VBM:2005092211 Fixed identification of DoCoMo devices

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 02-Mar-05	7130/3	rgreenall	VBM:2005011201 Further changes post review

 02-Mar-05	7130/1	rgreenall	VBM:2005011201 Post review corrections

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Sep-04	5315/6	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 13-Sep-04	5315/4	geoff	VBM:2004082404 Improve testsuite device repository test speed. (merge conflicts)

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 06-Sep-04	5420/1	allan	VBM:2004081802 Restore StandardElementHandler state on restore

 06-Sep-04	5415/1	allan	VBM:2004081802 Restore StandardElementHandler state on restore

 06-Aug-04	5088/2	byron	VBM:2004080301 Public API for device lookup: getDeviceNameByUAProfURL JDBC&XML

 02-Jan-04	2339/1	byron	VBM:2003123104 Replace rex.jar with Jakarta regexp

 ===========================================================================
*/
