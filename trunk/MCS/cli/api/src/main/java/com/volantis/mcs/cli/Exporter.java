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
 * (c) Volantis Systems Ltd 2000-2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.cli;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocationFactory;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.ProjectConfiguration;
import com.volantis.mcs.project.ProjectFactory;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.DefaultConfigurator;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Export/Import command object for moving policies and devices in and out of
 * repositories.
 * <p>
 * Usage example, to export from a JDBC repository;
 * <pre>
 * UserInterface ui = new UserInterface() { .... }; // Implement your own
 * String[] params = new String[] { .... };  // Parameters, same as command line
 *
 * Exporter exp = new Exporter(params, ui);
 * exp.transfer(Exporter.JDBC_REPOSITORY, Exporter.XML_REPOSITORY);
 * </pre>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class Exporter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(Exporter.class);

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer messageLocalizer =
                LocalizationFactory.createMessageLocalizer(Exporter.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(Exporter.class);

    /**
     * Constant used to tell the exporter initialise method that the
     * source or destination repository is an XML repository.
     */
    public static final int XML_REPOSITORY = 0;

    /**
     * Constant used to tell the exporter initialise method that the
     * source or destination repository is a JDBC repository.
     */
    public static final int JDBC_REPOSITORY = 1;

    /**
     *
     */
    private static final int TRANSFER_ALL = -1;
    private static final int TRANSFER_NONE = 0;
    private static final int TRANSFER_SPECIFIC = 1;

    // JDBC Repository args
    /**
     * Argument identifier for vendor
     */
    private static final String VENDOR = "-vendor";

    /**
     * Argument identifier for hostname
     */
    private static final String HOST = "-host";

    /**
     * Argument identifier for port
     */
    private static final String PORT = "-port";

    /**
     * Argument identifier for source
     */
    private static final String SOURCE = "-source";

    /**
     * Argument identifier for user
     */
    private static final String USER = "-user";

    /**
     * Argument identifier for password
     */
    private static final String PASSWORD = "-password";

    /**
     * Argument identifier for the source xml directory
     */
    private static final String SOURCE_DIR = "-srcdir";

    /**
     * Argument identifier for the destiantion xml repository
     */
    private static final String DESTINATION_DIR = "-destdir";

    /**
     * Argument identifier for the jdbc project
     */
    private static final String PROJECT = "-project";

    /**
     * Argument identifier for replace xml file if it already exists
     */
    private static final String REPLACE = "-replace";

    /**
     * Argument identifier for transfer all policies
     */
    private static final String ALL = "-all";

    /**
     * Argument identifier for transfer all commponents
     */
    private static final String ALL_COMPONENTS = "-allcomponents";

    /**
     * Argument identifier for transfer asset groups
     */
    private static final String ASSET_GROUPS = "-assetgroup";

    /**
     * Argument identifier for transfer audio
     */
    private static final String AUDIO = "-audio";

    /**
     * Argument identifier for transfer buttons
     */
    private static final String BUTTON = "-button";

    /**
     * Argument identifier for transfer charts
     */
    private static final String CHART = "-chart";

    /**
     * Argument identifier for transfer dynamic visuals
     */
    private static final String DYNVIS = "-dynvis";

    /**
     * Argument identifier for transfer images
     */
    private static final String IMAGE = "-image";

    /**
     * Argument identifier for transfer rollover
     */
    private static final String ROLLOVER = "-rollover";

    /**
     * Argument identifier for transfer script
     */
    private static final String SCRIPT = "-script";

    /**
     * Argument identifier for transfer text
     */
    private static final String TEXT = "-text";

    /**
     * Argument identifier for transfer links
     */
    private static final String LINK = "-link";

    /**
     * Argument identifier for transfer themes
     */
    private static final String THEME = "-theme";

    /**
     * Argument identifier for transfer layouts
     */
    private static final String LAYOUT = "-layout";

    /**
     * Argument identifier for transfer devices
     */
    private static final String DEVICE = "-device";

    /**
     * Argument identifier for transfer devices repository
     */
    private static final String DEVICE_REPOSITORY = "-devicerepository";

    /**
     * Argument identifier for transfer of system repository objects
     */
    private static final String SYSTEM = "-system";

    /**
     * Argument identifier to switch on verbose output
     */
    private static final String VERBOSE = "-verbose";

    /**
     * Argument identifier to update duplicates without confimation.
     */
    private static final String UPDATEALL = "-updateall";

    /**
     * Argument identifier to prevent repository changes being commited
     * until the transfer is complete rather than after each repository
     * object is transfered.
     */
    private static final String ENABLE_UNDO = "-enableundo";

    /**
     * Argument identifier to enable use of short table and column names.
     */
    private static final String USE_SHORT_NAMES = "-useshortnames";

    /**
     * Factory to create {@link DeviceRepositoryLocation} objects.
     */
    private static final DeviceRepositoryLocationFactory REPOSITORY_LOCATION_FACTORY =
        DeviceRepositoryLocationFactory.getDefaultInstance();

    /**
     * The repository to transfer from
     */
    private LocalRepository srcRepository;

    /**
     * The connection to the source repository.
     */
//    private LocalRepositoryConnection srcConnection;

    /**
     * The repository to transfer to.
     */
    private LocalRepository dstRepository;

    private Project srcProject;
    private Project dstProject;

    private PolicyTransferAccessor srcPolicyAccessor;
    private PolicyTransferAccessor dstPolicyAccessor;

    private DeviceRepositoryLocation srcDeviceLocation;
    private DeviceRepositoryLocation dstDeviceLocation;

    /**
     * The connection to the destination repository
     */
//    private LocalRepositoryConnection dstConnection;

    /**
     * Interface object with user, used to report errors, status and
     * ask for input.
     */
    private UserInterface userInterface;

    /**
     * Flag to indicate that duplicates in the destination repository
     * can be replaced without the client having confirm
     */
    private boolean needReplaceConfirmation;

    /**
     * Flag to indicate that a commit operation should not take place
     * until the transfer is completed.
     */
    private boolean undoable = false;

    /**
     * Flag to enable use of short table and column names.
     */
    private boolean useShortNames;

    /**
     * Flag to indicate whether client wishes verbose output
     */
    private boolean verbose;

    /**
     * Arguments object that manages the arguments
     */
    private Arguments args;

    /**
     * Set that contains arguments that refer to components
     */
    private static Set componentArgs;

    /**
     * Set that contains arguments that refer to components
     */
    private static Set systemArgs;

    /**
     * Mapping of policy types to the classes that encapsulte them
     */
    private static Map ARGUMENT_2_POLICY_TYPE;

    /**
     * Mapping of verbose descriptions for each policy type
     */
    private static Map POLICY_TYPE_2_DESCRIPTION;

    static {
        componentArgs = new HashSet();
        componentArgs.add(ASSET_GROUPS);
        componentArgs.add(AUDIO);
        componentArgs.add(BUTTON);
        componentArgs.add(CHART);
        componentArgs.add(DYNVIS);
        componentArgs.add(IMAGE);
        componentArgs.add(ROLLOVER);
        componentArgs.add(TEXT);
        componentArgs.add(SCRIPT);
        componentArgs.add(LINK);

        systemArgs = new HashSet();
        systemArgs.add(DEVICE);

        POLICY_TYPE_2_DESCRIPTION = new HashMap();
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.BASE_URL, "asset group component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.AUDIO, "audio component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.BUTTON_IMAGE, "button image component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.CHART, "chart component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.VIDEO, "dynamic visual component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.IMAGE, "image component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.ROLLOVER_IMAGE, "rollover image component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.TEXT, "text component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.SCRIPT, "script component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.LINK, "link component");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.THEME, "theme");
        POLICY_TYPE_2_DESCRIPTION.put(PolicyType.LAYOUT, "layout");

        ARGUMENT_2_POLICY_TYPE = new HashMap();
        ARGUMENT_2_POLICY_TYPE.put(ASSET_GROUPS, PolicyType.BASE_URL);
        ARGUMENT_2_POLICY_TYPE.put(AUDIO, PolicyType.AUDIO);
        ARGUMENT_2_POLICY_TYPE.put(BUTTON, PolicyType.BUTTON_IMAGE);
        ARGUMENT_2_POLICY_TYPE.put(CHART, PolicyType.CHART);
        ARGUMENT_2_POLICY_TYPE.put(DYNVIS, PolicyType.VIDEO);
        ARGUMENT_2_POLICY_TYPE.put(IMAGE, PolicyType.IMAGE);
        ARGUMENT_2_POLICY_TYPE.put(ROLLOVER, PolicyType.ROLLOVER_IMAGE);
        ARGUMENT_2_POLICY_TYPE.put(TEXT, PolicyType.TEXT);
        ARGUMENT_2_POLICY_TYPE.put(SCRIPT, PolicyType.SCRIPT);
        ARGUMENT_2_POLICY_TYPE.put(LINK, PolicyType.LINK);
        ARGUMENT_2_POLICY_TYPE.put(THEME, PolicyType.THEME);
        ARGUMENT_2_POLICY_TYPE.put(LAYOUT, PolicyType.LAYOUT);
    }

    /**
     * Creates a new Exporter instance.
     *
     * @param arguments used to configure the Exporter.
     * @param user interface object to which all user IO will be directed.
     */
    public Exporter(String[] arguments, UserInterface user) {

        if (user == null) {
            throw new NullPointerException();
        }

        // create the arguments object
        args = new Arguments(arguments);

        // can we overwite duplicates without prompting user for (y or n).
        needReplaceConfirmation = !args.contains(UPDATEALL);

        // does the client want verbose output
        verbose = args.contains(VERBOSE);

        undoable = args.contains(ENABLE_UNDO);
        useShortNames = args.contains(USE_SHORT_NAMES);

        userInterface = user;

        DefaultConfigurator.configure(verbose);
    }

    /**
     * Perform transfer of policies from source to destination repository.
     *
     * @param from source repository type
     * @param to destination repository type
     * @throws RepositoryException
     * @throws MissingArgumentException 
     */
    public void transfer(int from, int to) 
        throws RepositoryException, MissingArgumentException  {

        // Perform a quick check to see if all compulsory arguments 
        // have been provided.
        checkForCompulsoryArgs(from, to);

        if (from == JDBC_REPOSITORY) {
            srcRepository = createJDBCRepository();
            srcProject = createJDBCProject(srcRepository);
            srcDeviceLocation = createJDBCDeviceLocation();
        } else if (from == XML_REPOSITORY) {
            srcRepository = createXMLRepository();
            srcProject = createSourceXMLProject(srcRepository);
            srcDeviceLocation = createXMLDeviceLocation();
        } else {
            throw new IllegalArgumentException();
        }

        if (to == JDBC_REPOSITORY) {
            dstRepository = createJDBCRepository();
            dstProject = createJDBCProject(dstRepository);
            dstDeviceLocation = createJDBCDeviceLocation();
        } else if (to == XML_REPOSITORY) {
            dstRepository = createXMLRepository();
            dstProject = createDestinationXMLProject(dstRepository);
            dstDeviceLocation = createXMLDeviceLocation();
        } else {
            throw new IllegalArgumentException();
        }

        transfer();
    }

    /**
     * Check to see if all comlpulsory command line arguments have been
     * provided.
     * @param to 
     * @param from 
     * @throws MissingArgumentException in case of a missing argument
     */
    private void checkForCompulsoryArgs(int from, int to) 
        throws MissingArgumentException {

        // Database parameters required if at least one
        // repository is JDBC
        if (from == JDBC_REPOSITORY ||
            to == JDBC_REPOSITORY)  {

            if (!args.contains(VENDOR))
                throw new MissingArgumentException(VENDOR);
            if (!args.contains(HOST))
                throw new MissingArgumentException(HOST);
            if (!args.contains(PORT))
                throw new MissingArgumentException(PORT);
            if (!args.contains(SOURCE))
                throw new MissingArgumentException(SOURCE);
            if (!args.contains(USER))
                throw new MissingArgumentException(USER);            
            if (!args.contains(PROJECT))
                throw new MissingArgumentException(PROJECT);            
        }
        
        // Required parameters for export to XML
        if (to == XML_REPOSITORY) {
            if (!args.contains(DESTINATION_DIR))
                throw new MissingArgumentException(DESTINATION_DIR);
        }

        // Required parameters for import from XML
        if (from == XML_REPOSITORY)  {
            if (!args.contains(SOURCE_DIR))
                throw new MissingArgumentException(SOURCE_DIR);
        }
        
        // Required parameters for import to database
        if (to == JDBC_REPOSITORY) {
            // Check to see if the -devicerepository flag has been specified if it
            // is required because of the other flags specified.
            if ((args.contains(ALL) || args.contains(DEVICE) ||
                    args.contains(SYSTEM))
                    && !args.contains(DEVICE_REPOSITORY))
                throw new MissingArgumentException(DEVICE_REPOSITORY);                
        }
    }


    /**
     * Method to prompt user to see if a duplicate policy is to be overwritten.
     *
     * @param policyType the policy type
     * @param name
     * @return true if policy is to be overwritten, false otherwise
     */
    private boolean confirmReplace(PolicyType policyType, String name) {
        String policyDescription = (String)POLICY_TYPE_2_DESCRIPTION.get(policyType);
        return confirmReplace(policyDescription, name);
    }

    /**
     * Method to prompt user to see if a duplicate policy is to be overwritten.
     *
     * @param description the description of policy
     * @param name the name of the policy
     * @return true if policy is to be overwritten, false otherwise
     */
    private boolean confirmReplace(String description, String name) {

        if(!needReplaceConfirmation) {
            // no need to prompt, command line argument was present to specify
            // that we should always overwrite
            return true;
        }

        String action = "Replace";

        info("duplicate-name",
                       new Object[]{description, name, action});

        String response;
        while(true) {
            response = userInterface.getInputLine().trim().toLowerCase();

            if("y".equals(response) || "n".equals(response) ||
               "yes".equals(response) || "no".equals(response)) {
                break;
            }

            info("yes-or-no", null);
        }

        return response.startsWith("y");
    }

    /**
     * Set the undoable flag.
     *
     * @param flag
     */
    void setUndoable(boolean flag) {

        undoable = flag;
    }

    /**
     * Transfer the specified policies from the source repository into
     * the destination repository
     *
     * @exception RepositoryException if an error occurs
     */
    private void transfer()
            throws RepositoryException {
        try {
            LocalRepositoryConnection srcConnection
                    = (LocalRepositoryConnection) srcRepository.connect();
            LocalRepositoryConnection dstConnection
                    = (LocalRepositoryConnection) dstRepository.connect();
            srcPolicyAccessor = new PolicyTransferAccessor(
                    srcConnection, srcProject);
            dstPolicyAccessor = new PolicyTransferAccessor(
                    dstConnection, dstProject);

            boolean abort = false;
            try {
                info("transfer-info", null);

                if(undoable) {
                    abort = dstPolicyAccessor.beginTransaction();
                }

                // Transfer the device data separately. It is necessary to do
                // this differently to the other types of data because there is
                // no RepositoryObjectAccessor for devices. In future
                // this will supposedly be split out into it's own separate
                // device repository import tool.
                transferDevices(srcConnection, dstConnection);

                for (Iterator i = ARGUMENT_2_POLICY_TYPE.entrySet().iterator();
                     i.hasNext();) {
                    Map.Entry entry = (Map.Entry) i.next();
                    String policyArgument = (String) entry.getKey();
                    PolicyType policyType = (PolicyType) entry.getValue();

                    transferPolicies(policyArgument, policyType);
                }

                if(undoable) {
                    abort = (!dstPolicyAccessor.endTransaction());
                    info("commit-details", null);
                }
                info("transfer-Complete", null);
            }
            catch (RepositoryException e) {
                if (verbose) {
                    exception("unexpected-exception", e);
                }
                throw e;
            }
            finally {
                if (abort) {
                    if (verbose) {
                        info("rollback-details", null);
                    }
                    try {
                        dstPolicyAccessor.abortTransaction();
                    } catch (Exception e) {
                        exception("unexpected-exception", e);
                    }
                }
            }
        } finally {
            DefaultConfigurator.shutdown();
        }
    }

    /**
     * Helper method to determine what type of transfer is required for a given
     * policy type.
     *
     * <p><strong>NOTE:</strong> this method will always return
     * <code>TRANSFER_NONE</code> if a device transfer is queried against an
     * XML repository.</p>
     *
     * @return the type of policy transfer:
     *
     * <dl>
     *
     * <dt>TRANSFER_ALL</dt>
     *
     * <dd>transfer all policies of the given type</dd>
     *
     * <dt>TRANSFER_NONE</dt>
     *
     * <dd>transfer no policies of the given type</dd>
     *
     * <dt>TRANSFER_SPECIFIC</dt>
     *
     * <dd>transfer the policies speciefied by name via the command line of the
     * given type.</dd>
     *
     * </dl>
     *
     * @todo later handle device transfers to XML repository
     */
    private int getTransferType(String policyArgument) {
        int transferType = TRANSFER_NONE;

        boolean isXMLRepository = false;
        InternalProject dstProject = (InternalProject) this.dstProject;
        if (dstProject.getPolicySource() instanceof XMLPolicySource) {
            isXMLRepository = true;
        }
        if (policyArgument.equals(DEVICE) &&
            (args.contains(SYSTEM) ||
             args.contains(ALL) ||
             args.contains(DEVICE)) && isXMLRepository) {

            // We don't support transfer of devices to an XML repository
            warn("xml-device-transfer-not-supported", null);

        } else if ((args.contains(ALL)) ||
            (args.contains(ALL_COMPONENTS) && componentArgs.contains(policyArgument)) ||
            (args.contains(SYSTEM) && systemArgs.contains(policyArgument))) {
            transferType = TRANSFER_ALL;
        } else if (args.contains(policyArgument)) {
            if (args.hasValuesFor(policyArgument)) {
                transferType = TRANSFER_SPECIFIC;
            } else {
                transferType = TRANSFER_ALL;
            }
        }

        return transferType;
    }

    /**
     * Transfer policies of a given type
     * @param policyType the policy type
     * @exception RepositoryException if an error occurs
     */
    private void transferPolicies(String policyArgument,
                                  final PolicyType policyType)
        throws RepositoryException {

        if (verbose) {
            info("policy-transfer-info",
                new Object[] {(String)POLICY_TYPE_2_DESCRIPTION.get(policyType)});
        }

        int transferType = getTransferType(policyArgument);
        if(transferType == TRANSFER_NONE) {
            if (verbose) {
                info("no-transfer-requested", null);
            }
            return;
        }

        RepositoryEnumeration e;
        if(transferType == TRANSFER_ALL) {
            e = srcPolicyAccessor.enumeratePolicyNames(policyType);
        } else {
            // Create an enumeration of identities from the list of names.
            e = args.getValueEnumerator(policyArgument);
        }
        try {
            while(e.hasNext ()) {
                String name = (String) e.next ();

                transferPolicy(policyType, name);
            }
        }
        finally {
            e.close ();
        }
    }


    /**
     * Transfer a policy and any dependant children. Note
     * objects in both source and destination repository are not
     * locked.
     * @param policyType
     * @param name the RepositoryObjectIdentity for the policy
     * @exception RepositoryException if an error occurs
     */
    private void transferPolicy(PolicyType policyType, String name)
        throws RepositoryException {

        Policy policy;
        try {
          policy = srcPolicyAccessor.retrievePolicy(name);
        } catch (RepositoryException e) {
            // Try to report the name of the policy we couldn't read
            throw new RepositoryException(
                    exceptionLocalizer.format("policy-read-failure",
                                              name),
                    e);
        }
        if (policy == null) {
            warn("migration-policy-missing", new Object[] {name});
            return;
        }

        if (verbose) {
            info("transfering", new Object[]{name});
        }

        // Make sure the object has the correct PolicySource
        boolean abort = false;

        try {
            if(!undoable) {
                abort = dstPolicyAccessor.beginTransaction();
            }
            if(!needReplaceConfirmation) {
                updatePolicy(dstPolicyAccessor, policy);
            } else {
                // need to prompt user to see if duplicates are to be replaced
                // Attempt to add first
                try {
                    dstPolicyAccessor.addPolicy(policy);
                }
                catch(RepositoryException re) {
                    // Has the exception been thrown due to a duplicate object?
                    if(policyExistsInRepository(dstPolicyAccessor, name)) {
                        if(confirmReplace(policyType, name)) {
                            updatePolicy(dstPolicyAccessor, policy);
                        } else {
                            // client does not want to replace the object
                            // nothing else to do return;
                        }
                    } else {
                        throw re;
                    }
                }
            }
            if(!undoable) {
                abort = !dstPolicyAccessor.endTransaction();
            }
        }
        finally {
            try {
                if(abort) {
                    dstPolicyAccessor.abortTransaction();
                }
            }
            catch(RepositoryException re) {
                if (verbose) {
                    exception("unexpected-exception", re);
                }
            }
        }
    }

    private void updatePolicy(
            PolicyTransferAccessor accessor, Policy policy)
            throws RepositoryException {
        try {
            accessor.updatePolicy(policy);
        } catch (RepositoryException e) {
            // Try and report the object we failed to update.
            throw new RepositoryException(
                    exceptionLocalizer.format("policy-update-failure",
                    policy.getName()),
                    e);
        }
    }

    /**
     * Returns true if the supplied policy exists in the repository.
     *
     * @param accessor the repository policy manager.
     * @param name to be tested for existence in the repository.
     *
     * @return true if the supplied policy exists in the repository; otherwise
     * false.
     */
    private boolean policyExistsInRepository(PolicyTransferAccessor accessor,
                                             String name) {

        // attempt to retrieve the supplied policy from the repository.

        try {
            Policy retrievedPolicy = accessor.retrievePolicy(name);
            return retrievedPolicy != null;
        } catch (RepositoryException e) {
            // If there was an exception here, we do not know if the policy
            // exists in the repository or not, and if we let the exception
            // go up the stack, then it obscures the original exception which
            // lead to the calling of this method.
            // This means that this implementation (which was added as an L3
            // fix) is bogus. I have raised VBM:2005102515 to fix this properly
            // in MCS and previous releases.

            // For now, we just do our best and assume that the policy did
            // exist, but it is knackered (eg invalid). At least this will
            // not cause the original exception to be thrown away, but will
            // possibly end up with the user seeing false positive "confirm
            // replacement" messages.
            return true;
        }
    }

    /**
     * Returns true if the supplied object exists in the repository.
     *
     * @param accessor the device repository accessor.
     * @param deviceName to be tested for existence in the repository.
     *
     * @return true if the supplied object exists in the repository; otherwise
     * false.
     */
    private boolean deviceExistsInRepository(DeviceTransferAccessor accessor,
                                             String deviceName) {

        // attempt to retrieve the supplied object from the repository.

        try {
            Device device = accessor.retrieveDevice(deviceName);
            return device != null;
        } catch (RepositoryException e) {
            // If there was an exception here, we do not know if the object
            // exists in the repository or not, and if we let the exception
            // go up the stack, then it obscures the original exception which
            // lead to the calling of this method.
            // This means that this implementation (which was added as an L3
            // fix) is bogus. I have raised VBM:2005102515 to fix this properly
            // in MCS and previous releases.

            // For now, we just do our best and assume that the object did
            // exist, but it is knackered (eg invalid). At least this will
            // not cause the original exception to be thrown away, but will
            // possibly end up with the user seeing false positive "confirm
            // replacement" messages.
            return true;
        }
    }


    /**
     * Create a JDBC repository from the command line arguments
     *
     * @return a Repostory
     * @exception RepositoryException if an error occurs
     */
    private LocalRepository createJDBCRepository()
        throws RepositoryException {

        JDBCRepositoryFactory factory =
                JDBCRepositoryFactory.getDefaultInstance();
        MCSDriverConfiguration driverConfiguration =
                factory.createMCSDriverConfiguration();
        driverConfiguration.setHost(args.getValue(HOST));
        String port = args.getValue(PORT);
        if (port != null) {
            driverConfiguration.setPort(Integer.parseInt(port));
        }
        JDBCRepositoryType repositoryType =
                JDBCRepositoryType.getTypeForVendor(args.getValue(VENDOR));
        driverConfiguration.setDriverVendor(repositoryType.getVendor());
        driverConfiguration.setSource(args.getValue(SOURCE));

        DataSource dataSource =
                factory.createMCSDriverDataSource(driverConfiguration);

        JDBCRepositoryConfiguration configuration =
                factory.createJDBCRepositoryConfiguration();
        configuration.setUsername(args.getValue(USER));
        configuration.setPassword(args.getValue(PASSWORD));
        configuration.setShortNames(useShortNames);
        configuration.setDataSource(dataSource);

        return factory.createJDBCRepository(configuration);
//        // A repository is specified via the following args
//        // -vendor -host -port -source -user
//        Map properties = new HashMap();
//        properties.put (JDBCRepository.HOST_PROPERTY, args.getValue(HOST));
//        properties.put (JDBCRepository.PORT_PROPERTY, args.getValue(PORT));
//        properties.put (JDBCRepository.VENDOR_PROPERTY, args.getValue(VENDOR));
//        properties.put (JDBCRepository.SOURCE_PROPERTY, args.getValue(SOURCE));
//        properties.put (JDBCRepository.USERNAME_PROPERTY, args.getValue(USER));
//        properties.put (JDBCRepository.PASSWORD_PROPERTY, args.getValue(PASSWORD));
//        properties.put (JDBCRepository.DEFAULT_PROJECT_NAME_PROPERTY,
//                args.getValue(PROJECT));
//        properties.put (JDBCRepository.STANDARD_DEVICE_PROJECT_NAME_PROPERTY,
//                args.getValue(PROJECT));
//        properties.put (JDBCRepository.USE_SHORT_NAMES,
//                new Boolean(useShortNames));
//
//        return JDBCRepository.createRepository (properties);
    }

    private Project createJDBCProject(LocalRepository repository) {
        ProjectFactory factory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration configuration = factory.createProjectConfiguration();
        configuration.setRepository(repository);
        configuration.setPolicyLocation(args.getValue(PROJECT));
        return factory.createProject(configuration);
    }

    private DeviceRepositoryLocation createJDBCDeviceLocation() {
        return REPOSITORY_LOCATION_FACTORY.createDeviceRepositoryLocation(
            args.getValue(PROJECT));
    }

    /**
     * Create a source XML repository
     *
     * @return the Repository
     * @exception RepositoryException if an error occurs
     */
    private LocalRepository createXMLRepository()
        throws RepositoryException {

        XMLRepositoryFactory factory =
                XMLRepositoryFactory.getDefaultInstance();
        return factory.createXMLRepository(null);
//
//        Map properties = new HashMap();
//        String dirName = args.getValue(SOURCE_DIR);
//        if(dirName != null) {
//            File dir = new File(dirName);
//            if(!dir.exists()) {
//                RepositoryException re =
//                    new RepositoryException(
//                            exceptionLocalizer.format(
//                                    "directory-non-existant",
//                                    dir.getAbsolutePath()));
//                logger.error("unexpected-exception", re);
//                throw re;
//            }
//            if(!dir.canRead()) {
//                RepositoryException re =
//                    new RepositoryException(
//                            exceptionLocalizer.format(
//                                    "directory-cannot-be-read",
//                                    dir.getAbsolutePath()));
//                logger.error("unexpected-exception", re);
//                throw re;
//            }
//            // Import / Export fixes the default project directory manually.
//            // ensure that the default directory path passed to createJDBCRepository is absolute
//            properties.put (XMLRepository.DEFAULT_PROJECT_DIRECTORY_PROPERTY,
//                    dir.getAbsolutePath());
//
//            String deviceLocation = args.getValue(DEVICE_REPOSITORY);
//            // Ensure there is a value set
//            if (deviceLocation != null) {
//                // Check for correct file extension...
//                if (!deviceLocation.endsWith(
//                        FileExtension.DEVICE_REPOSITORY.getExtension())) {
//                    // Niave approach of just adding it on to the end!
//                    deviceLocation += FileExtension.DEVICE_REPOSITORY.getExtension();
//                }
//                // ensure that the device repository file passed to createJDBCRepository is absolute
//                properties.put(XMLRepository.DEVICE_REPOSITORY_PROPERTY,
//                               new File(deviceLocation).getAbsolutePath());
//            }
//        }
//
//        return XMLRepository.createRepository(properties);
    }

    /**
     * Create a source XML project
     *
     * @return the project
     * @exception RepositoryException if an error occurs
     */
    private Project createSourceXMLProject(LocalRepository repository)
        throws RepositoryException {

        String dirName = args.getValue(SOURCE_DIR);
            File dir = new File(dirName);
            if(!dir.exists()) {
                RepositoryException re =
                    new RepositoryException(
                            exceptionLocalizer.format(
                                    "directory-non-existant",
                                    dir.getAbsolutePath()));
                if (verbose) {
                    exception("unexpected-exception", re);
                }
                throw re;
            }
            if(!dir.canRead()) {
                RepositoryException re =
                    new RepositoryException(
                            exceptionLocalizer.format(
                                    "directory-cannot-be-read",
                                    dir.getAbsolutePath()));
                if (verbose) {
                    exception("unexpected-exception", re);
                }
                throw re;
            }

        ProjectFactory factory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration configuration = factory.createProjectConfiguration();
        configuration.setRepository(repository);
        configuration.setPolicyLocation(dir.getAbsolutePath());

        return factory.createProject(configuration);
    }

    /**
     * Create a destination XML repository
     * @return the Repository
     */
    private Project createDestinationXMLProject(LocalRepository repository) {

        String dirName = args.getValue(DESTINATION_DIR);
        File dir = new File(dirName);

        ProjectFactory factory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration configuration = factory.createProjectConfiguration();
        configuration.setRepository(repository);
        configuration.setDeleteProjectContents(args.contains(REPLACE));
        configuration.setPolicyLocation(dir.getAbsolutePath());

        return factory.createProject(configuration);
    }

    private DeviceRepositoryLocation createXMLDeviceLocation() {
        return REPOSITORY_LOCATION_FACTORY.createDeviceRepositoryLocation(
                            args.getValue(DEVICE_REPOSITORY));
    }

    /**
     * Exception class to specify that arguments were missing from the
     * command line
     */
    public static class MissingArgumentException extends Exception {
        /**
         * Creates a new MissingArgumentsException instance.
         * @param argument name of missing arg
         */
        public MissingArgumentException(String argument) {
            super(argument);
        }

        /**
         * Do not allow creation of a new MissingArgumentsException instance
         * without argument name.
         */
    }

    /**
     * Transfers the device policy descriptors (metadata) from the source
     * repository into the destination repository.
     * <p>
     * NOTE: the current implementation finds all existing metadata in the
     * destination repository and deletes it before adding any new metadata.
     * This is required since we use synthetic keys in the metadata database
     * structures which are generated and stored in memory, without bothering
     * to use a sequence table, so we cannot add any new data to existing data.
     * This was the quickest way we could think of to implement this at the
     * time. Sorry!
     *
     * @param srcConnection
     * @param dstConnection
     * @exception RepositoryException if an error occurs
     * @todo should be split out to a new "device repository import tool"
     */
    private void transferDevices(LocalRepositoryConnection srcConnection,
            LocalRepositoryConnection dstConnection) throws RepositoryException {

        // Say what we are about to do.
        // TODO: fix these error messages - no english in parameters
        if (verbose) {
            info("policy-transfer-info",
                new Object[]{"device policy descriptor"});
        }

        // Ensure that we have some devices to transfer.
        int transferType = getTransferType(DEVICE);
        if (transferType == TRANSFER_NONE) {
            if (verbose) {
                info("no-transfer-requested", null);
            }
            return;
        }

        // Create the "fake" device transfer accessors as required.
        // Note that the src one will NPE if the location is null so we
        // assume that this has already been validated to be present.
        final DeviceTransferAccessor srcDeviceAccessor = new DeviceTransferAccessor(
                srcConnection, srcDeviceLocation);
        final DeviceTransferAccessor dstDeviceAccessor = new DeviceTransferAccessor(
                dstConnection, dstDeviceLocation);

        // Quick hack to allow the dumping of device meta data
        // this is useful so that we can compare meta data in textual
        // form between XML and JDBC after an import to ensure it
        // was correct. Really this should be part of the future
        // "separate device import tool".
        if (args.contains("-dumpsrcdevicemetadata")) {
            dumpDeviceMetaData(srcDeviceAccessor);
            return;
        }
        if (args.contains("-dumpdstdevicemetadata")) {
            dumpDeviceMetaData(dstDeviceAccessor);
            return;
        }

        removePolicyDescriptors(dstDeviceAccessor);

        removeCategoryDescriptors(dstDeviceAccessor);

        copyCategoryDescriptors(srcDeviceAccessor, dstDeviceAccessor);

        copyPolicyDescriptors(srcDeviceAccessor, dstDeviceAccessor);

        // Transfer the devices.
        RepositoryEnumeration deviceNames;
        if (transferType == TRANSFER_ALL) {
            deviceNames = srcDeviceAccessor.enumerateDevicesChildren(null);
        } else {
            deviceNames = args.getValueEnumerator(DEVICE);
        }

        transferDevices(deviceNames, srcDeviceAccessor, dstDeviceAccessor);
    }

    /**
     * Loop over all the existing destination policy names,
     * removing ALL the related policy descriptors.
     *
     * @param accessor device transfer accessor
     * @throws RepositoryException
     */
    private static void removePolicyDescriptors(DeviceTransferAccessor accessor)
            throws RepositoryException {
        // Delete all the policy descriptors.
        try {
            accessor.removeAllPolicyDescriptors();
        } catch (RepositoryException e) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "cannot-remove-all-policy-descriptors"),
                    e);
        }
    }

    /**
     * Loop over all the source policy names, copying the related
     * policy descriptors from the source to the destination.
     *
     * @param srcAccessor source device transfer accessor
     * @param dstAccessor destination device transfer accessor
     * @throws RepositoryException
     */
    private static void copyPolicyDescriptors(DeviceTransferAccessor srcAccessor,
            DeviceTransferAccessor dstAccessor)
            throws RepositoryException {
        // Now
        RepositoryEnumeration names = srcAccessor.enumerateDevicePolicyNames();

        try {
            while (names.hasNext()) {
                String policyName = (String) names.next();
                if (logger.isDebugEnabled()) {
                    logger.debug("Copying policy descriptor " +
                            "for '" + policyName + "'");
                }

                // Read the descriptors from the source for this policy.
                List policyDescriptors;
                try {
                    policyDescriptors =
                        srcAccessor.retrievePolicyDescriptors(policyName);
                } catch (RepositoryException e) {
                    throw new RepositoryException(
                            exceptionLocalizer.format(
                                    "cannot-read-policy-descriptor",
                        policyName), e);
                }

                for (Iterator iter = policyDescriptors.iterator();
                     iter.hasNext();) {

                    final PolicyDescriptor policyDescriptor =
                        (PolicyDescriptor) iter.next();
                    // Write the descriptors to the destination for this policy.
                    try {
                        dstAccessor.addPolicyDescriptor(policyName,
                                policyDescriptor);
                    } catch (RepositoryException e) {
                        throw new RepositoryException(
                                exceptionLocalizer.format(
                                        "cannot-write-policy-descriptor",
                            new Object[]{policyName, policyDescriptor}), e);
                    }
                }
            }
        } finally {
            names.close();
        }
    }

    /**
     * Loop over all the existing destination category names,
     * removing ALL the related policy descriptors.
     *
     * @param accessor device transfer accessor
     * @throws RepositoryException
     */
    private static void removeCategoryDescriptors(DeviceTransferAccessor accessor)
            throws RepositoryException {
        // Delete the descriptor in the destination for this category.
        try {
            accessor.removeAllCategoryDescriptors();
        } catch (RepositoryException e) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "cannot-remove-all-category-descriptors"),
                    e);
        }
    }

    /**
     * Loop over all the source category names, copying the related
     * category descriptors from the source to the destination.
     *
     * @param srcAccessor source device transfer accessor
     * @param dstAccessor destination device transfer accessor
     * @throws RepositoryException
     */
    private static void copyCategoryDescriptors(DeviceTransferAccessor srcAccessor,
            DeviceTransferAccessor dstAccessor)
            throws RepositoryException {
        // Now loop over all the source category names, copying the related
        // category descriptors from the source to the destination.
        RepositoryEnumeration names = srcAccessor.enumerateCategoryNames();

        try {
            while (names.hasNext()) {
                String categoryName = (String) names.next();
                if (logger.isDebugEnabled()) {
                    logger.debug("Copying category descriptor " +
                            "for '" + categoryName + "'");
                }

                // Read the descriptors from the source for this policy.
                List categoryDescriptors;
                try {
                    categoryDescriptors =
                        srcAccessor.retrieveCategoryDescriptors(
                            categoryName);
                } catch (RepositoryException e) {
                    throw new RepositoryException(
                        exceptionLocalizer.format(
                            "cannot-read-category-descriptor", categoryName),
                        e);
                }

                for (Iterator iter = categoryDescriptors.iterator();
                     iter.hasNext();) {

                    final CategoryDescriptor categoryDescriptor =
                        (CategoryDescriptor) iter.next();
                    // Write the descriptors to the destination for this
                    // category.
                    try {
                        dstAccessor.addCategoryDescriptor(
                            categoryName, categoryDescriptor);
                    } catch (RepositoryException e) {
                        throw new RepositoryException(
                                exceptionLocalizer.format(
                                        "cannot-write-policy-descriptor",
                            new Object[]{categoryName, categoryDescriptor}), e);
                    }
                }
            }
        } finally {
            names.close();
        }
    }

    private void transferDevices(
            RepositoryEnumeration deviceNames,
            DeviceTransferAccessor srcDeviceAccessor, DeviceTransferAccessor dstDeviceAccessor)
            throws RepositoryException {

        try {
            while(deviceNames.hasNext()) {
                String name = (String) deviceNames.next();

                transferDevice(name, srcDeviceAccessor, dstDeviceAccessor);
            }
        }
        finally {
            deviceNames.close();
        }
    }

    private void transferDevice(String deviceName,
            DeviceTransferAccessor srcDeviceAccessor,
            DeviceTransferAccessor dstDeviceAccessor) throws RepositoryException {

        Device device;
        try {
            device = srcDeviceAccessor.retrieveDevice(deviceName);

        } catch (RepositoryException e) {
            // Try to report the name of the policy we couldn't read
            throw new RepositoryException(
                    exceptionLocalizer.format("policy-read-failure",
                            deviceName),
                    e);
        }

        if (device == null) {
            warn("migration-policy-missing", new Object[]{deviceName});

            return;
        }

        if (verbose) {
            info("transfering", new Object[]{deviceName});
        }

        boolean abort = false;
        try {
            if(!undoable) {
                abort = dstDeviceAccessor.beginTransaction();
            }
            if(!needReplaceConfirmation) {
                updateDevice(dstDeviceAccessor, device);
            } else {
                // need to prompt user to see if duplicates are to be replaced
                // Attempt to add first
                try {
                    dstDeviceAccessor.addDevice(device);
                }
                catch(RepositoryException re) {
                    // Has the exception been thrown due to a duplicate object?
                    if(deviceExistsInRepository(dstDeviceAccessor, deviceName)) {
                        if(confirmReplace("device", deviceName )) {
                            updateDevice(dstDeviceAccessor, device);
                        } else {
                            // client does not want to replace the object
                            // nothing else to do return;
                        }
                    } else {
                        throw re;
                    }
                }
            }
            if(!undoable) {
                abort = !dstDeviceAccessor.endTransaction();
            }
        }
        finally {
            try {
                if(abort) {
                    dstDeviceAccessor.abortTransaction();
                }
            }
            catch(RepositoryException re) {
                if (verbose) {
                    exception("unexpected-exception", re);
                }
            }
        }

        RepositoryEnumeration children =
                srcDeviceAccessor.enumerateDevicesChildren(deviceName);

        transferDevices(children, srcDeviceAccessor, dstDeviceAccessor);
    }

    private void updateDevice(
            DeviceTransferAccessor accessor,
            Device device) throws RepositoryException {

        final String deviceName = device.getName();
        try {
            accessor.updateDevice(device);
        } catch (RepositoryException e) {
            // Try and report the object we failed to update.
            throw new RepositoryException(exceptionLocalizer.format("policy-update-failure",
                    deviceName),
                    e);
        }
    }

    /**
     * Dump the device metadata in the repository associated with the
     * repository connection supplied.
     * <p>
     * This is useful for debugging and testing.
     *
     * @param accessor the connection to read the device metadata from.
     * @throws RepositoryException if there was a problem.
     */
    private void dumpDeviceMetaData(DeviceTransferAccessor accessor)
            throws RepositoryException {

        Map descriptors = new HashMap();

        // Now loop over all the source policy names, collecting the descriptor
        // for each policy namee.
        RepositoryEnumeration names = accessor.enumerateDevicePolicyNames();
        try {
            while (names.hasNext()) {
                String policyName = (String) names.next();
                PolicyDescriptor policyDescriptor;
                try {
                    policyDescriptor = accessor.retrievePolicyDescriptor(
                            policyName, Locale.getDefault());
                } catch (RepositoryException e) {
                    throw new RepositoryException(
                            exceptionLocalizer.format(
                                    "cannot-read-policy-descriptor",
                                    policyName),
                            e);
                }
                descriptors.put(policyName, policyDescriptor);
            }
        } finally {
            names.close();
        }

        // Now get a list of all the policies we found a descriptor for...
        List policyList = new ArrayList(descriptors.keySet());
        // And sort it for easy comparison purposes.
        Collections.sort(policyList);

        // Finally iterate over the sorted list of policies, dumping the
        // related descriptor for each one.
        Iterator itr = policyList.iterator();
        while (itr.hasNext()) {
            String policy = (String) itr.next();
            PolicyDescriptor descriptor = (PolicyDescriptor)
                    descriptors.get(policy);

            info("localized-message",
                         new Object[] {policy + "->" + descriptor});
        }
    }

    /**
     * Report a localized status or information message to user.
     *
     * @param message key of message to display
     * @param parameters parameters to add to message
     */
     private void info(String message, Object[] parameters) {
         userInterface.reportStatus(format(message, parameters));
     }

    /**
     * Report a localized warning message to user.
     *
     * @param message key of message to display
     * @param parameters parameters to add to message
     */
     private void warn(String message, Object[] parameters) {
         userInterface.reportError(format(message, parameters));
     }

    /**
     * Report a localized error message to user.
     *
     * @param message key of message to display
     * @param parameters parameters to add to message
     */
     private void error(String message, Object[] parameters) {
         userInterface.reportError(format(message, parameters));
     }

    /**
     * Report an excpetion to the user.
     *
     * @param message key of message to display
     * @param e exception object to display
     */
    private void exception(String message, Exception e) {
        error(message, null);
        userInterface.reportException(e);
    }

    /**
     * Obtain the localized message, if the localize fails returns the
     * key.
     *
     * @param key used to look up the localized message.
     * @param parameters added to the localized message.
     * @return localized message or key of message not found.
     */
    private String format(String key, Object[] parameters) {
        String message = messageLocalizer.format(key, parameters);

        if (message == null) {
            throw new NullPointerException("No localized message found for :"+key);
        }

        return message;
    }

    /**
     * Class to manage the command line arguments
     */
    protected class Arguments {

        /**
         * Map to strore the arguments in
         */
        Map args;

        /**
         * Creates a new Arguments instance.
         * @param arguments the array of arguments
         */
        public Arguments(String[] arguments) {
            args = new HashMap();
            String argName;
            for(int i=0; i<arguments.length; i++) {
                if(arguments[i].startsWith("-")) {
                    argName = arguments[i].toLowerCase();
                    Argument arg = new Argument();
                    while(i+1 < arguments.length && !arguments[i+1].startsWith("-")) {
                        arg.addValue(arguments[++i]);
                    }
                    args.put(argName, arg);
                }
            }
        }

        /**
         * Provide a means of enumerating over the values of a named argument
         * @param arg name of the argument
         * @return an ArgumentEnumerator
         */
        public ArgumentEnumerator getValueEnumerator(String arg) {
            Argument argument = (Argument)args.get(arg);
            if (argument != null) {
                return argument.getValueEnumerator();
            }
            return null;
        }

        /**
         * Was the argument present at the command line
         * @param arg the name of the arg
         * @return true if the argument was present.
         */
        public boolean contains(String arg) {
            return args.containsKey(arg);
        }

        /**
         * Does the named argument have a value associated with it.
         * @param arg the name of the argument
         * @return true if a value exists false otherwise
         */
        public boolean hasValuesFor(String arg) {
            Argument argument = (Argument)args.get(arg);
            return argument != null && argument.getValues() != null;
        }

        /**
         * Get the value for a named argument
         * @param arg name of the argument
         * @return the value
         */
        public String getValue(String arg) {
            Argument argument = (Argument)args.get(arg);
            if (argument == null || argument.getValues() == null) {
                return null;
            }
            Iterator i = argument.getValues().iterator();
            if (i.hasNext()) {
                return (String)i.next();
            }
            return null;
        }

        /**
         * Class to enumerate the values of an argument.
         * implements the RepositoryEnumeration interface so that the
         * transferPolicies method can treat repository enumerations and
         * argument enumerations as identical.
         */
        protected class ArgumentEnumerator
            implements RepositoryEnumeration {

            /**
             * Iterator
             */
            Iterator iter = null;

            /**
             * Creates a new ArgumentEnumerator instance.
             * @param values the values to enumerate
             */
            public ArgumentEnumerator(List values) {
                if (values != null) {
                    iter = values.iterator();
                }
            }

            /**
             * Get the next object in the enumeration
             * @return the next object
             */
            public Object next () {
                if (iter == null) {
                    return null;
                }
                return iter.next();
            }

            /**
             * I there another object.
             * @return true if there is another object, otherwise false.
             */
            public boolean hasNext() {
                if (iter == null) {
                    return false;
                }
                return iter.hasNext();
            }

            /**
             * Close this enumeration
             */
            public void close() {
                // do nothing
            }
        }

        /**
         * Class to encapsulate an argument and any associated values
         */
        protected class Argument {

            /**
             * List to store the arguments values
             */
            private List values;

            /**
             * Add a value for this argument
             * @param value the value
             */
            public void addValue(String value) {
                if (values == null) {
                    values = new ArrayList();
                }
                values.add(value);
            }

            /**
             * Get all the values for this argument
             * @return the list of values
             */
            public List getValues() {
                return values;
            }

            /**
             * Retrive an enumerator to enumerate over this arguments values
             * @return an ArgumentEnumerator.
             */
            public ArgumentEnumerator getValueEnumerator() {
                return new ArgumentEnumerator(values);
            }
        }
    }
}
