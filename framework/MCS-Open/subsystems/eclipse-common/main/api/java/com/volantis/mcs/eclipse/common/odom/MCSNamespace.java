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
package com.volantis.mcs.eclipse.common.odom;

import org.jdom.Namespace;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.devrep.device.api.xml.DeviceSchemas;

/**
 * Provides an enumeration of known namespaces
 */
final public class MCSNamespace {

    /**
     * The marlin lpdm namespace.
     */
    public static final Namespace LPDM = Namespace.getNamespace(
            "lpdm", PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL());

    /**
     * The marlin lpdm namespace.
     */
    public static final Namespace LPDM_2006_02 = Namespace.getNamespace(
            "lpdm", PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL());

    /**
     * The device definitions namespace.
     */
    public static final Namespace DEVICE_DEFINITIONS = Namespace.getNamespace(
            "defs", DeviceSchemas.POLICY_DEFINITIONS_CURRENT.getNamespaceURL());

    /**
     * The device namespace.
     */
    public static final Namespace DEVICE = Namespace.getNamespace(
            "device", DeviceSchemas.DEVICE_CURRENT.getNamespaceURL());

    /**
     * The device hierarchy namespace.
     */
    public static final Namespace DEVICE_HIERARCHY = Namespace.getNamespace(
            "hrchy", DeviceSchemas.HEIRARCHY_CURRENT.getNamespaceURL());
    /**
     * The device identification namespace
     */
    public static final Namespace DEVICE_IDENTIFICATION = Namespace.getNamespace(
            "ident",
            DeviceSchemas.IDENTIFICATION_CURRENT.getNamespaceURL());

    /**
     * The device tac-identification namespace
     */
    public static final Namespace DEVICE_TAC_IDENTIFICATION =
            Namespace.getNamespace("tac",
                    DeviceSchemas.TAC_IDENTIFICATION_CURRENT.getNamespaceURL());
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 11-Apr-05	7376/2	allan	VBM:2005031101 SmartClient bundler - commit for testing

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/6	allan	VBM:2004051018 Undo/Redo in device editor.

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 19-Aug-04	5264/3	allan	VBM:2004081008 Remove invalid plugin dependencies

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 10-May-04	4239/1	allan	VBM:2004042207 SaveAs on DeviceEditor.

 10-May-04	4068/4	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/2	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
