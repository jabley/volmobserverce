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
package com.volantis.mcs.ibm.ucp;

import com.ibm.ucp.Component;
import com.ibm.ucp.Dimension;
import com.ibm.ucp.IProvider;
import com.ibm.ucp.Profile;
import com.ibm.ucp.Property;
import com.ibm.ucp.UCPException;
import com.ibm.ucp.engine.Adapter;
import com.ibm.ucp.util.Environment;
import com.ibm.ucp.util.ResourceReference;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.apache.log4j.BasicConfigurator;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

/**
 * An extension of {@link Adapter} which is an entry point for a "plugin" 
 * which provides MCS device definitions from the MCS device repository for 
 * the IBM UCP 2.0 Framework.
 * <p>
 * This is intended for use by IBM to allow their GUI tools to access the
 * MCS device repository via the standard IBM device definition framework, UCP. 
 * <p>
 * This was created as per Requirement 926 and Assignment 808.
 * <p>
 * NOTE: Adapter is an abstract class which provides a set of services which
 * we are not particularly interested in, for example initialisation from an
 * XML file containing device definitions. We only want to implement it's 
 * parent interface, {@link IProvider}. However, we must extend Adapter in
 * order to be loaded into the provider heirarchy by the UCP configuration
 * parser. Thus this class includes a load of Adapter methods which have been
 * stubbed out, and we ignore it as much as possible apart from this. Hopefully
 * this will not cause too many problems when this is deployed in the real 
 * world.
 */ 
public class MCSDeviceRepositoryProvider extends Adapter {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            MCSDeviceRepositoryProvider.class);

    private static final DeviceRepositoryLocationFactory REPOSITORY_LOCATION_FACTORY =
        DeviceRepositoryLocationFactory.getDefaultInstance();

    private static final DeviceRepositoryAccessorFactory REPOSITORY_ACCESSOR_FACTORY =
        DeviceRepositoryAccessorFactory.getDefaultInstance();

    /**
     * The name of this provider, as specified in the UCP config file.
     */ 
    private String name;
    
    /**
     * The repository accessor for MCS devices.
     */ 
    private DeviceRepositoryAccessor accessor;

    /**
     * The repository connection for retrieving devices via the accessor.
     */ 
    private RepositoryConnection connection;

    /**
     * Schema used to construct Profiles.
     */ 
    private String profileSchema;
    
    /**
     * Name used to construct Profiles.
     */ 
    private String profileName;

    /**
     * NOTE: this constructor is not supported, and is provided only for 
     * compatibility with the other IProvider instances.
     */ 
    public MCSDeviceRepositoryProvider(String name) {
        
        // Call the most minimal parent constructor available.
        // NOTE: This requires we have an Environment set up already. 
        super(name);
        
        throw new UnsupportedOperationException(
                "Must provide at least one configuration property");
        
    }

    /**
     * Construct an instance of this class, with the name and configuration
     * element provided.
     * <p>
     * This takes the following parameters from the configuration element, as
     * per the similarly named parameters in mcs-config.xml: 
     * <ul>
     *   <li>repository-type: the type of the repository to access, either 
     *      jdbc or xml.
     *   <li>XML repository properties:
     *     <ul>
     *       <li>file: the MDPR device repository file.
     *     </ul>
     *   </li>
     *   <li>JDBC repository properties:
     *     <ul>
     *       <li>odbc-user: the JDBC user name.
     *       <li>odbc-password: the JDBC password.
     *       <li>odbc-source: the JDBC source.
     *       <li>odbc-vendor: the JDBC vendor.
     *       <li>odbc-host: the JDBC host.
     *       <li>odbc-port: the JDBC port.
     *       <li>project: the project name.
     *     </ul>
     *   </li>
     *   <li>profile-schema: the schema to use when creating the Profile 
     *      (optional), defaults to "http://www.wapforum.org/profiles/UAPROF/
     *      ccppschema-20010430#" otherwise.
     *   <li>profile-name: the name to use when creating the Profile 
     *      (optional), defaults to null otherwise.
     * </ul>
     * 
     * @param name the name of the provider, as specified in the UCP config 
     *      file.
     * @param config the configuration element (parameter) containing param 
     *      tags, as specified in the UCP config file.
     */ 
    public MCSDeviceRepositoryProvider(String name, Element config) {

        // Call the most minimal parent constructor available.
        // NOTE: This requires we have an Environment set up already. 
        super(name);
        
        // Save the name from the config file to return via getName().
        this.name = name;
        
        // Populate the optional profile and component properties.
        // These are externally configurable, with "guessed" default values, 
        // because I wasn't sure exactly what the values should be.
        String profileSchema = Environment.getParameter(config, 
                "profile-schema");
        if (profileSchema != null) {
            this.profileSchema = profileSchema;
        } else {
            // I have chosen the profile schema that the ProfileMgr used.
            // See profileMgr.jar!/ucp.properties and 
            // ucp.jar!com/ibm/ucp/engine/ResourceBundleAdaptor.
            this.profileSchema = "http://www.wapforum.org/profiles/" +
            "UAPROF/ccppschema-20010430#";
        }
        String profileName = Environment.getParameter(config, 
                "profile-name");
        if (profileName != null) {
            this.profileName = profileName;
        } else {
            // I have chosen the null profile name that the ProfileMgr used.
            // See profileMgr.jar!/ucp.properties and 
            // ucp.jar!com/ibm/ucp/engine/ResourceBundleAdaptor.
            this.profileName = null;
        }
        
        // Set up log4j to collect the logging from the rest of the 
        // MCS code that we use.
        // The architecture does not mention logging and the architect
        // has declined my request to define how it should work. 
        // So, we just set up some basic stdout output for now.
        BasicConfigurator.configure();
        
        // Connect to the MCS device repository. 
        try {
            String repositoryType = 
                Environment.getParameter(config, "repository-type");
            LocalRepository repository;

            DeviceRepositoryLocation location;

            // NOTE: this configuration mirrored the "old" way of configuring
            // local repositories, presumably it should be updated to
            // mirror the new way?
            if (repositoryType != null && repositoryType.equals("xml")) {

                XMLRepositoryFactory factory =
                        XMLRepositoryFactory.getDefaultInstance();
                repository = factory.createXMLRepository(null);

                location = REPOSITORY_LOCATION_FACTORY.createDeviceRepositoryLocation(
                        Environment.getParameter(config, "file"));
            } else {
                // Default to JDBC.

                JDBCRepositoryFactory factory =
                        JDBCRepositoryFactory.getDefaultInstance();

                MCSDriverConfiguration driverConfiguration =
                        factory.createMCSDriverConfiguration();

                driverConfiguration.setSource(
                        Environment.getParameter(config, "odbc-source"));
                String vendor = Environment.getParameter(config, "odbc-vendor");
                JDBCRepositoryType jdbcRepositoryType =
                        JDBCRepositoryType.getTypeForVendor(vendor);
                if (jdbcRepositoryType != null) {
                    driverConfiguration.setDriverVendor(
                            jdbcRepositoryType.getVendor());
                }

                driverConfiguration.setHost(Environment.getParameter(config,
                        "odbc-host"));
                String port = Environment.getParameter(config, "odbc-port");
                if (port != null) {
                    driverConfiguration.setPort(Integer.parseInt(port));
                }

                DataSource dataSource = factory.createMCSDriverDataSource(
                        driverConfiguration);

                JDBCRepositoryConfiguration configuration =
                        factory.createJDBCRepositoryConfiguration();
                configuration.setDataSource(dataSource);
                configuration.setUsername(Environment.getParameter(config,
                        "odbc-user"));
                configuration.setPassword(Environment.getParameter(config,
                        "odbc-password"));

                repository = factory.createJDBCRepository(configuration);

                location = REPOSITORY_LOCATION_FACTORY.createDeviceRepositoryLocation(
                        Environment.getParameter(config, "project"));
            }

            accessor =
                REPOSITORY_ACCESSOR_FACTORY.createDeviceRepositoryAccessor(
                    repository,  location, null);
            connection = repository.connect();
            // NOTE: we may need to enable caching for performance.
            // I'm not sure if we need to because the implementation of the
            // device heirarchy is just about to completely change... so
            // I'll leave this like so for now and we'll add caching if we
            // have to later.
        } catch (RepositoryException e) {
            throw new RuntimeException(
                        exceptionLocalizer.format(
                                    "device-repository-connection-failure"),
                        e);
        }
        
    }

    // Javadoc inherited.
    public String getName() {
        
        // NOTE: this is at variance with the current architecture, but I have
        // verbal approval from the architect for this.
        return name;
        
    }

    // Javadoc inherited.
    public Set getProfileKeys() {
        
        Set outputKeys = null;
        try {
            
            outputKeys = new HashSet(); 
            // NOTE: might be slow; this may require caching to be enabled
            // as mentioned in the constructor.
            RepositoryEnumeration inputKeys =
                    accessor.enumerateDeviceNames(connection);
            while (inputKeys.hasNext()) {
                String deviceName = (String) inputKeys.next();
                outputKeys.add(deviceName);
            }
            // Translate empty back to null as per the contract.
            if (outputKeys.size() == 0) {
                outputKeys = null;
            }
        
        } catch (Exception e) {
            throw new RuntimeException(
                        exceptionLocalizer.format("profile-keys-missing"), e);
        }
        return outputKeys;
        
    }

    // Javadoc inherited.
    public String getProfileDescription(String profileKey) {
        
        String description = null;
        try {
            // Retrive our device;
            DefaultDevice device = accessor.retrieveDevice(connection, profileKey);
            if (device != null) {
                // Describe our device.
                description = describe(device);
            }
        
        } catch (Exception e) {
            throw new RuntimeException(
                        exceptionLocalizer.format("profile-description-error",
                                                  profileKey),
                        e);
        }
        return description;
        
    }

    // Javadoc inherited.
    public Profile getProfile(String profileKey) {
        
        Profile profile = null;
        try {
            // Retrive our device
            DefaultDevice device = accessor.retrieveDevice(connection, profileKey);
            if (device != null) {
                // Adapt our device into a UCP profile.
                profile = adapt(device);
            }
        
        } catch (Exception e) {
            throw new RuntimeException(
                        exceptionLocalizer.format("profile-description-error",
                                                  profileKey), 
                        e);
        }
        return profile;
        
    }

    // Javadoc inherited.
    public Profile getProfile(HttpServletRequest httpServletRequest) {
        
        throw new UnsupportedOperationException();
        
    }

    /**
     * Create a textual description of a device.
     * 
     * @param device the device to describe.
     * @return the textual description.
     */ 
    private String describe(DefaultDevice device) {
        
        // We just return the device name, since we don't really have
        // a better description at the moment.
        return device.getName();
    }

    /**
     * Create a Profile from a InternalDevice.
     * 
     * @param device the device to adapt to a profile.
     * @return the profile which adapts the device created.
     * @throws UCPException if there was a problem inside UCP.
     */ 
    private Profile adapt(DefaultDevice device) throws UCPException {

        Profile profile = null;

        // NOTE: The values passed to the constructors of the Profile
        // and Component objects below were an educated guess by me on the
        // basis of the Javadoc for UCP and the contents of the ProfileMgr 
        // and UCP jars from Websphere Everyplace Access (which uses UCP).

        // I assume we do not require validation.
        boolean validating = false;

        // Create the Profile. 
        profile = new Profile(profileSchema, profileName, validating);
        
        // 
        // Component: HardwarePlatform
        //

        // Create the HardwarePlatform component.
        Component hardwarePlatformComponent = 
                createComponent("HardwarePlatform");

        // pixeldepth -> BitsPerPixel
        int pixelDepth = getPolicyValueAsInt(device, "pixeldepth", 0);
        if (pixelDepth > 0) {
            Property bitsPerPixelProperty = new Property("BitsPerPixel",
                    new Integer(pixelDepth));
            hardwarePlatformComponent.addProperty(bitsPerPixelProperty);
        }
            
        // rendermode -> ColorCapable
        String renderMode = device.getComputedPolicyValue("rendermode");
        if (renderMode != null) {
            Boolean colorCapable = Boolean.FALSE;
            if ((renderMode.equals("rgb") ||
                    renderMode.equals("pallette")) && pixelDepth > 1) {
                colorCapable = Boolean.TRUE;
            }
            Property colorCapableProperty = new Property("ColorCapable",
                    colorCapable);
            hardwarePlatformComponent.addProperty(colorCapableProperty);
        }
            
        // modelnum -> Model
        String modelNum = device.getComputedPolicyValue("modelnum");
        if (modelNum != null) {
            Property modelProperty = new Property("Model", modelNum);
            hardwarePlatformComponent.addProperty(modelProperty);
        }
            
        // pixelsx,y -> ScreenSize
        int pixelsX = getPolicyValueAsInt(device, "pixelsx", 0);
        int pixelsY = getPolicyValueAsInt(device, "pixelsy", 0);
        if (pixelsX > 0 && pixelsY > 0) {
            Dimension screenSize = new Dimension(pixelsX, pixelsY);
            Property screenSizeProperty = new Property("ScreenSize",
                    screenSize);
            hardwarePlatformComponent.addProperty(screenSizeProperty);
        }
            
        // charactersx,y -> ScreenSizeChar
        Dimension screenSizeChar;
        int charactersX = getPolicyValueAsInt(device, "charactersx", 0);
        int charactersY = getPolicyValueAsInt(device, "charactersy", 0);
        if (charactersX > 0 && charactersY > 0) {
            screenSizeChar = new Dimension(charactersX, charactersY);
            Property screenSizeCharProperty = new Property("ScreenSizeChar",
                    screenSizeChar);
            hardwarePlatformComponent.addProperty(screenSizeCharProperty);
        }
            
        // mfg -> Vendor
        String manufacturer = device.getComputedPolicyValue("mfg");
        if (manufacturer != null) {
            Property vendorProperty = new Property("Vendor", manufacturer);
            hardwarePlatformComponent.addProperty(vendorProperty);
        }

        // HardwarePlatform component is complete, so add it to the Profile. 
        profile.addComponent(hardwarePlatformComponent);

        // 
        // Component: SoftwarePlatform
        //

        Component softwarePlatformComponent = 
                createComponent("SoftwarePlatform");

        // UAProf.CcppAccept -> CcppAccept
        String ccppAccept = device.getComputedPolicyValue("UAProf.CcppAccept");
        if (ccppAccept != null) {
            Set ccppAcceptSet = new HashSet();
            ccppAcceptSet.add(ccppAccept);
            Property ccppAcceptProperty = new Property("CcppAccept", 
                    ccppAcceptSet);
            softwarePlatformComponent.addProperty(ccppAcceptProperty);
        }
        
        // SoftwarePlatform component is complete, so add it to the Profile. 
        profile.addComponent(softwarePlatformComponent);
        
        // 
        // Component: BrowserUA
        //

        Component browserUAComponent = 
                createComponent("BrowserUA");

        // brwsrname -> BrowserName
        String brwsrName = device.getComputedPolicyValue("brwsrname");
        if (brwsrName != null) {
            Property browserNameProperty = new Property("BrowserName",
                    brwsrName);
            browserUAComponent.addProperty(browserNameProperty);
        }

        // brwsrvers -> BrowserVersion
        String brwsrVers = device.getComputedPolicyValue("brwsrvers");
        if (brwsrVers != null) {
            Property browserVersionProperty = new Property("BrowserVersion",
                    brwsrVers);
            browserUAComponent.addProperty(browserVersionProperty);
        }

        // BrowserUA component is complete, so add it to the Profile. 
        profile.addComponent(browserUAComponent);

        // 
        // Component: WapCharacteristics
        //

        Component wapCharacteristicsComponent = 
                createComponent("WapCharacteristics");

        // UAProf.WmlVersion -> WmlVersion
        String wmlVersion = device.getComputedPolicyValue("UAProf.WmlVersion");
        if (wmlVersion != null) {
            Set wmlVersionSet = new HashSet();
            wmlVersionSet.add(wmlVersion);
            Property wmlVersionProperty = new Property("WmlVersion",
                    wmlVersionSet);
            wapCharacteristicsComponent.addProperty(wmlVersionProperty);
        }

        // WapCharacteristics component is complete, so add it to the Profile. 
        profile.addComponent(wapCharacteristicsComponent);

        // Return the created profile.
        return profile;
        
    }

    private int getPolicyValueAsInt(final DefaultDevice device,
                                    final String policyName,
                                    final int defaultValue) {
        final String policyValue = device.getComputedPolicyValue(policyName);
        int value = defaultValue;
        if (policyValue != null) {
            value = Integer.parseInt(policyValue);
        }
        return value;
    }

    /**
     * Factory method to create a component from a CCPP name.
     * <p>
     * NOTE: I am not sure how to create one of these correctly, and 
     * I added this method because I thought it might make it easier to 
     * document now (and fix later) if I split it out as a separate method.
     * 
     * @param ccppName the CCPP name for this component.
     * @return the created component.
     */ 
    private Component createComponent(String ccppName) {
        
        // Choose a type for the component.
        // Apparently the component type must be of the form <schema>#<name> 
        // for the XMLSchema that we tested with.   
        // Unfortunately the schema package javadoc is not very informative, 
        // so I am not sure if this is "correct" in a general sense.
        String type = this.profileSchema + ccppName;
        
        // Choose a name for the component.
        // I have chosen the null component name that the ProfileMgr used.
        // See ucp.jar!com/ibm/ucp/engine/ResourceBundleAdaptor.
        String name = null;
        
        // I have turned off validation.
        // Note: turning off validation does somehow not prevent us from
        // requiring a schema in the UCP configuration. Seems strange...
        boolean validating = false;
        
        // Create and return the component with the parameters chosen.
        return new Component(type, name, validating);
        
    }

    //
    // Methods of Adapter that we do not use.
    //
    
    public short getValidationMode() {
        return 0; // hardcoded to none.
    }

    public void setValidationMode(short validationMode) {
        // do nothing.
    }

    public String getRepositoryIndex() {
        return null;
    }

    public void openRepository(ResourceReference resource)
            throws Exception {
        // Do nothing. 
        // We open the repository at object instantiation creation time.
    }

    public boolean isReadOnly() {
        return true;
    }

    public void closeRepository()
            throws Exception {
        // Do nothing.
        // We rely on the VM shutting down to close the repository :-). 
    }

    public void addProfile(String key, String ref, String description)
            throws Exception {
        // We are read only.
        throw new IllegalStateException();
    }

    public void removeProfile(String key)
            throws Exception {
        // We are read only.
        throw new IllegalStateException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 25-Feb-04	3136/1	philws	VBM:2004021908 Remove accessor manager singletons and make MCSDeviceRepositoryProvider and its test case use the runtime device accessor correctly

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 30-Jan-04	2807/2	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 13-Jan-04	2573/3	andy	VBM:2003121907 renamed file variables to directory

 13-Jan-04	2573/1	andy	VBM:2003121907 removed remnants of single file support

 23-Dec-03	2252/2	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 03-Nov-03	1529/7	geoff	VBM:2003100904 Implement IBM UCP IProvider to read MCS devices for Eclipse (fix bug in rendermode support)

 17-Oct-03	1529/5	geoff	VBM:2003100904 Implement IBM UCP IProvider to read MCS devices for Eclipse (fix small rework things from mat)

 14-Oct-03	1529/3	geoff	VBM:2003100904 Implement IBM UCP Adaptor/IProvider to read MCS devices for Eclipse (finished apart from test case infrastructure)

 ===========================================================================
*/
