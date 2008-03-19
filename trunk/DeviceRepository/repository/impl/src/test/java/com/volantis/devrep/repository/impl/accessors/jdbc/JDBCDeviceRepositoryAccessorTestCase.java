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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.accessors.jdbc;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.impl.DeviceRepositoryLocationImpl;
import com.volantis.devrep.repository.impl.DeviceTACPair;
import com.volantis.devrep.repository.impl.repository.jdbc.JDBCRepository;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessorTestAbstract;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultBooleanPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultIntPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultOrderedSetPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultRangePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultSelectionPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultStructurePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultTextPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultUnorderedSetPolicyType;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.BooleanPolicyType;
import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.mcs.devices.policy.types.OrderedSetPolicyType;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.RangePolicyType;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.devices.policy.types.StructurePolicyType;
import com.volantis.mcs.devices.policy.types.TextPolicyType;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCDriverVendor;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.testtools.Executor;
import com.volantis.synergetics.testtools.HypersonicManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;

public class JDBCDeviceRepositoryAccessorTestCase
        extends AbstractDeviceRepositoryAccessorTestAbstract {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(JDBCDeviceRepositoryAccessorTestCase.class);

    /**
     * The default project.
     */
    private static final String defaultProject = "#defaultProject";

    /**
     * One more project to case of many device repositories under
     * different project names.
     */
    private static final String oneMoreProject = "#oneMoreProject";

    /**
     * A RepositoryConnection.
     */
    RepositoryConnection connection;

    DeviceRepositoryLocation location;

    protected void setUp() throws Exception {
        super.setUp();

        location = new DeviceRepositoryLocationImpl("#defaultProject");
    }

    // ========================================================================
    //   POLICY DESCRIPTOR READ SUPPORT
    //   NOTE: this was coded separately to the write support due to severe
    //   time constraints. As such there probably refactoring opportunities to
    //   be had by factoring out common code between read and write.
    // ========================================================================

    /**
     * Test the retrieval of policy descriptors.
     */
    public void testRetrievePolicyDescriptor() throws Exception {
        try {
            InternalJDBCRepository repository = createRepository(defaultProject);
            JDBCDeviceRepositoryAccessor accessor =
                    new JDBCDeviceRepositoryAccessor(repository, location);
            Locale locale = new Locale("en", "GB");

            PolicyDescriptor policyDescriptor;
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "Policy1", locale);
            assertNull("No policy descriptor expected", policyDescriptor);

            // Test the retrieval of a policy descriptor from VMTYPES
            int id = populateWithSimpleTypes(0);
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "Policy1", locale);
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            PolicyType policyType = policyDescriptor.getPolicyType();
            assertTrue("Type should match",
                    policyType instanceof BooleanPolicyType);
            assertEquals("PolicyDescName1",
                policyDescriptor.getPolicyDescriptiveName());
            assertEquals("PolicyHelp1", policyDescriptor.getPolicyHelp());
            assertEquals("en_GB_", policyDescriptor.getLanguage());

            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "Policy2", locale);
            policyType = policyDescriptor.getPolicyType();
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            assertTrue("Type should match",
                    policyType instanceof IntPolicyType);
            assertEquals("PolicyDescName2",
                policyDescriptor.getPolicyDescriptiveName());
            assertEquals("PolicyHelp2", policyDescriptor.getPolicyHelp());
            assertEquals("en_GB_", policyDescriptor.getLanguage());

            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "Policy3", locale);
            policyType = policyDescriptor.getPolicyType();
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            assertTrue("Type should match",
                    policyType instanceof TextPolicyType);
            assertEquals("PolicyDescName3",
                policyDescriptor.getPolicyDescriptiveName());
            assertEquals("PolicyHelp3", policyDescriptor.getPolicyHelp());
            assertEquals("en_GB_", policyDescriptor.getLanguage());

            // Test the retrieval of a policy descriptor from VMTYPES_SELECTION
            id = populateWithSelections(id);
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "SelectionPolicy", locale);
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            policyType = policyDescriptor.getPolicyType();
            assertTrue("Type should match",
                    policyType instanceof SelectionPolicyType);
            assertEquals("SelectionPolicyDescName",
                policyDescriptor.getPolicyDescriptiveName());
            assertEquals("SelectionPolicyHelp", policyDescriptor.getPolicyHelp());
            assertEquals("en_GB_", policyDescriptor.getLanguage());

            SelectionPolicyType selectionType =
                    (SelectionPolicyType)policyType;
            assertEquals("Selections size should match", 2,
                    selectionType.getKeywords().size());
            assertEquals("Selections size should match",
                    "keyword1", selectionType.getKeywords().get(0));
            assertEquals("Selections size should match",
                    "keyword2", selectionType.getKeywords().get(1));

            // Test the retrieval of a policy descriptor from VMTYPES_RANGE
            id = populateWithRanges(id);
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "RangesPolicy", locale);
            policyType = policyDescriptor.getPolicyType();
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            assertTrue("Type should match: " + policyType,
                    policyType instanceof RangePolicyType);
            RangePolicyType rangeType =
                    (RangePolicyType)policyType;

            assertEquals("Selections size should match", 77,
                    rangeType.getMinInclusive());
            assertEquals("Selections size should match", 188,
                    rangeType.getMaxInclusive());

            // Test the retrieval of a policy descriptor from VMTYPES_STRUCTURE
            id = populateWithStructure(id);
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "StructurePolicy", locale);
            policyType = policyDescriptor.getPolicyType();
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            assertTrue("Type should match: " + policyType,
                    policyType instanceof StructurePolicyType);
            StructurePolicyType structureType =
                    (StructurePolicyType)policyType;

            assertTrue("Should contain key: " + "TestFieldname",
                    structureType.getFieldTypes().containsKey("TestFieldname"));
            policyType = (PolicyType)structureType.getFieldTypes().get("TestFieldname");
            assertTrue("Type should match: " + policyType,
                    policyType instanceof BooleanPolicyType);

            // Test the retrieval of a policy descriptor from VMTYPES_SET
            id = populateWithOrderedSet(id);
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "OrderedSetPolicy", locale);
            policyType = policyDescriptor.getPolicyType();
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            assertTrue("Type should match: " + policyType,
                    policyType instanceof OrderedSetPolicyType);
            OrderedSetPolicyType orderedSetType =
                    (OrderedSetPolicyType)policyType;

            policyType = orderedSetType.getMemberPolicyType();
            assertTrue("Type should match: " + policyType,
                    policyType instanceof IntPolicyType);

            repository = createRepository(oneMoreProject);
            accessor =
                    new JDBCDeviceRepositoryAccessor(repository,
                            new DeviceRepositoryLocationImpl(oneMoreProject));

            // Test the retrieval of a policy descriptor from VMTYPES
            populateWithSimpleTypes(0);
            policyDescriptor = accessor.retrievePolicyDescriptor(
                    connection, "Policy1", locale);
            assertNotNull("Policy Descriptor should be found", policyDescriptor);
            policyType = policyDescriptor.getPolicyType();
            assertTrue("Type should match",
                    policyType instanceof BooleanPolicyType);

        } finally {
            removeRepository();
        }
    }

    /**
     * Test the enumeration of policy names with a category name.
     */
    public void testRetrieveCategoryDescriptor() throws Exception {
        try {
            final InternalJDBCRepository repository = createRepository(defaultProject);
            final JDBCDeviceRepositoryAccessor accessor =
                new JDBCDeviceRepositoryAccessor(repository, location);
            final Locale locale = Locale.getDefault();
            CategoryDescriptor descriptor = accessor.
                retrieveCategoryDescriptor(connection, "unknown", locale);
            assertNull("Should find nothing", descriptor);

            // Populate with one value.
            Connection sqlConnection =
                    ((JDBCRepositoryConnection) connection).getConnection();

            Statement st = sqlConnection.createStatement();
            execute(st, "insert into VMPOLICY_CATEGORY values " +
                    "(" + "'" + defaultProject + "',0,'CategoryName', " +
                        "'<default>')");
            st.close();

            descriptor = accessor.retrieveCategoryDescriptor(connection,
                    "CategoryName", locale);
            assertNotNull("Should find descriptor", descriptor);
            assertEquals("CategoryName", descriptor.getCategoryDescriptiveName());


            st = sqlConnection.createStatement();
            execute(st, "insert into VMPOLICY_CATEGORY values " +
                "(" + "'" + defaultProject + "',0,'category name', 'en_GB_')");
            st.close();

            descriptor = accessor.retrieveCategoryDescriptor(connection,
                    "CategoryName", new Locale("en", "GB"));
            assertNotNull("Should find descriptor", descriptor);
            assertEquals("category name",
                descriptor.getCategoryDescriptiveName());
            assertEquals("en_GB_", descriptor.getLanguage());

            descriptor = accessor.retrieveCategoryDescriptor(connection,
                    "CategoryName", new Locale("en", "GB", "WIN"));
            assertNotNull("Should find descriptor", descriptor);
            assertEquals("category name",
                descriptor.getCategoryDescriptiveName());
            assertEquals("en_GB_", descriptor.getLanguage());

            descriptor = accessor.retrieveCategoryDescriptor(connection,
                    "CategoryName", new Locale("en"));
            assertNotNull("Should find descriptor", descriptor);
            assertEquals("CategoryName", descriptor.getCategoryDescriptiveName());
            assertNull(descriptor.getLanguage());

            descriptor = accessor.retrieveCategoryDescriptor(connection,
                    "CategoryName", new Locale("de"));
            assertNotNull("Should find descriptor", descriptor);
            assertEquals("CategoryName", descriptor.getCategoryDescriptiveName());
            assertNull(descriptor.getLanguage());
        } finally {
            removeRepository();
        }
    }

    /**
     * Test the enumeration of policy names with a category name.
     */
    public void testEnumerateCategoryNames() throws Exception {
        try {
            final InternalJDBCRepository repository = createRepository(defaultProject);
            final JDBCDeviceRepositoryAccessor accessor =
                new JDBCDeviceRepositoryAccessor(repository, location);
            RepositoryEnumeration enumeration =
                accessor.enumerateCategoryNames(connection);
            assertFalse("Should find nothing", enumeration.hasNext());

            // Populate with one value.
            Connection sqlConnection =
                    ((JDBCRepositoryConnection) connection).getConnection();
            final Statement st = sqlConnection.createStatement();
            execute(st, "insert into VMPOLICY_CATEGORY values " +
                    "(" + "'" + defaultProject + "',0,'CategoryName', " +
                        "'<default>')");
            execute(st, "insert into VMPOLICY_CATEGORY values " +
                    "(" + "'" + defaultProject + "',0,'Category name', 'en_GB_')");
            st.close();

            // Retrieve the unknown value shouldn't find a match.
            enumeration = accessor.enumerateCategoryNames(connection);
            assertTrue("Should find category", enumeration.hasNext());
            assertEquals("CategoryName", enumeration.next());
            assertFalse(enumeration.hasNext());
            final CategoryDescriptor categoryDescriptor =
                accessor.retrieveCategoryDescriptor(
                    connection, "CategoryName", new Locale("en", "GB"));
            assertEquals("Category name",
                categoryDescriptor.getCategoryDescriptiveName());
            assertEquals("en_GB_", categoryDescriptor.getLanguage());
        } finally {
            removeRepository();
        }
    }

    /**
     * Test the enumeration of policy names.
     */
    public void testEnumeratePolicyNames() throws Exception {
        try {
            InternalJDBCRepository repository = createRepository(defaultProject);
            JDBCDeviceRepositoryAccessor accessor =
                    new JDBCDeviceRepositoryAccessor(repository, location);
            RepositoryEnumeration repositoryEnumeration =
                    accessor.enumeratePolicyNames(connection);
            assertNotNull("Repository enumeration expected", repositoryEnumeration);
            assertFalse("No values expected in enumeration.",
                    repositoryEnumeration.hasNext());

            // Populate with one value.
            Connection sqlConnection =
                    ((JDBCRepositoryConnection) connection).getConnection();

            Statement st = sqlConnection.createStatement();
            execute(st, "insert into VMPOLICY_TYPE values " +
                    "(" + "'" + defaultProject + "', 'PolicyName1', 0, 0)");
            st.close();

            repositoryEnumeration = accessor.enumeratePolicyNames(connection);
            assertNotNull("Values expected", repositoryEnumeration);
            assertTrue("Values expected in enumeration.",
                    repositoryEnumeration.hasNext());
            String result = (String)repositoryEnumeration.next();
            assertEquals("Result should match", "PolicyName1", result);

            assertFalse("Only one value expected.",
                    repositoryEnumeration.hasNext());
        } finally {
            removeRepository();
        }
    }

    /**
     * Test the enumeration of policy names with a category name.
     */
    public void testEnumeratePolicyNamesWithCategory() throws Exception {
        try {
            InternalJDBCRepository repository = createRepository(defaultProject);
            JDBCDeviceRepositoryAccessor accessor =
                    new JDBCDeviceRepositoryAccessor(repository, location);
            RepositoryEnumeration repositoryEnumeration =
                    accessor.enumeratePolicyNames(connection, "unknown");
            assertNotNull("Repository enumeration expected", repositoryEnumeration);
            assertFalse("No values expected in enumeration.",
                    repositoryEnumeration.hasNext());

            // Populate with one value.
            Connection sqlConnection =
                    ((JDBCRepositoryConnection) connection).getConnection();

            Statement st = sqlConnection.createStatement();
            execute(st, "insert into VMPOLICY_TYPE values " +
                "(" + "'" + defaultProject + "', 'PolicyName1', 0, 0)");
            execute(st, "insert into VMPOLICY_CATEGORY values " +
                "(" + "'" + defaultProject + "',0,'CategoryName', " +
                    "'<default>')");
            st.close();

            // Retrieve the unknown value shouldn't find a match.
            repositoryEnumeration = accessor.enumeratePolicyNames(connection,
                    "unknown");
            assertNotNull("Repository enumeration expected", repositoryEnumeration);
            assertFalse("No values expected in enumeration.",
                    repositoryEnumeration.hasNext());

            // Retrieve the PolicyName with the matching category name should
            // find a match.
            repositoryEnumeration = accessor.enumeratePolicyNames(connection,
                    "CategoryName");
            assertNotNull("Values expected", repositoryEnumeration);
            assertTrue("Values expected in enumeration.",
                    repositoryEnumeration.hasNext());
            String result = (String)repositoryEnumeration.next();
            assertEquals("Result should match", "PolicyName1", result);

            assertFalse("Only one value expected.",
                    repositoryEnumeration.hasNext());
        } finally {
            removeRepository();
        }
    }


    /**
     * Populate the database with ordered set.
     */
    private int populateWithOrderedSet(int id)
            throws Exception {
        Connection sqlConnection =
                ((JDBCRepositoryConnection) connection).getConnection();

        Statement st = sqlConnection.createStatement();

        int instanceID = id;
        execute(st, "insert into VMPOLICY_TYPE values " +
                "(" + "'" + defaultProject + "','OrderedSetPolicy'," +
                instanceID + ", 0)");

        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'OrderedSetPolicy', 'en_GB_', " +
                "'OrderedSetPolicyDescName', 'OrderedSetPolicyHelp')");

        execute(st, "insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                JDBCDeviceRepositoryAccessor.ORDERED_SET_TYPE_ID + ")");

        execute(st, "insert into VMTYPES_SET" +
                " (PROJECT,TYPE_INSTANCE_ID,MEMBER_INSTANCE_ID,ORDERED)" +
                " values (" + "'" + defaultProject + "'," + instanceID + "," +
                "1, 1)"); // Int, true
        st.close();

        return instanceID  + 1;
    }

    /**
     * Populate the database with structure data.
     */
    private int populateWithStructure(int id)
            throws Exception {
        Connection sqlConnection =
                ((JDBCRepositoryConnection) connection).getConnection();

        Statement st = sqlConnection.createStatement();

        int instanceID = id;
        execute(st, "insert into VMPOLICY_TYPE values " +
                "(" + "'" + defaultProject + "','StructurePolicy'," +
                instanceID + ", 0)");

        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'StructurePolicy', 'en_GB_', " +
                "'StructurePolicyDescName', 'StructurePolicyHelp')");

        execute(st, "insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                JDBCDeviceRepositoryAccessor.STRUCTURE_TYPE_ID + ")");

        execute(st, "insert into VMTYPES_STRUCTURE values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                "'TestFieldname', 0)"); // Boolean
        st.close();
        return instanceID  + 1;
    }

    /**
     * Populate the database with range data.
     */
    private int populateWithRanges(int id)
            throws Exception {
        Connection sqlConnection =
                ((JDBCRepositoryConnection) connection).getConnection();

        Statement st = sqlConnection.createStatement();

        int instanceID = id;
        execute(st, "insert into VMPOLICY_TYPE values " +
            "(" + "'" + defaultProject + "', 'RangesPolicy'," + instanceID +
                ", 0)");

        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'RangesPolicy', 'en_GB_', " +
                "'RangesPolicyDescName', 'RangesPolicyHelp')");

        execute(st, "insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                JDBCDeviceRepositoryAccessor.RANGE_TYPE_ID + ")");

        execute(st, "insert into VMTYPES_RANGE values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                "77, 188)");
        st.close();
        return instanceID  + 1;
    }

    /**
     * Execute the sql string on using the statement.
     * @param st the SQL statement.
     * @param sql the sql string.
     */
    private void execute(Statement st, String sql)
            throws Exception {
        st.execute(sql);
    }

    /**
     * Populate the database with selections data.
     */
    private int populateWithSelections(int id)
            throws Exception {
        Connection sqlConnection =
                ((JDBCRepositoryConnection) connection).getConnection();

        Statement st = sqlConnection.createStatement();

        int instanceID = id;
        execute(st,"insert into VMPOLICY_TYPE values " +
            "(" + "'" + defaultProject + "', 'SelectionPolicy' ," +
                instanceID + ", 0)");

        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'SelectionPolicy', 'en_GB_', " +
                "'SelectionPolicyDescName', 'SelectionPolicyHelp')");

        execute(st,"insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                JDBCDeviceRepositoryAccessor.SELECTION_TYPE_ID + ")");

        execute(st,"insert into VMTYPES_SELECTION values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                "'keyword1')");
        execute(st,"insert into VMTYPES_SELECTION values " +
                "(" + "'" + defaultProject + "'," + instanceID + "," +
                "'keyword2')");
        st.close();
        return instanceID  + 1;
    }

    /**
     * Populate the database with simple types data.
     */
    private int populateWithSimpleTypes(int id)
            throws Exception {
        Connection sqlConnection =
                ((JDBCRepositoryConnection) connection).getConnection();

        Statement st = sqlConnection.createStatement();

        int instanceID = id;
        execute(st,"insert into VMPOLICY_TYPE values " +
            "(" + "'" + defaultProject + "', 'Policy1' , " + (instanceID++) +
                ", 0)");
        execute(st,"insert into VMPOLICY_TYPE values " +
            "(" + "'" + defaultProject + "', 'Policy2' , " + (instanceID++) +
                ", 0)");
        execute(st,"insert into VMPOLICY_TYPE values " +
            "(" + "'" + defaultProject + "', 'Policy3' , " + (instanceID++) +
                ", 0)");

        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'Policy1', 'en_GB_', " +
                "'PolicyDescName1', 'PolicyHelp1')");
        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'Policy2', 'en_GB_', " +
                "'PolicyDescName2', 'PolicyHelp2')");
        execute(st,"insert into VMPOLICY_LANG values " +
            "(" + "'" + defaultProject + "' , 'Policy3', 'en_GB_'," +
                "'PolicyDescName3', 'PolicyHelp3')");

        instanceID = id;
        execute(st,"insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + (instanceID++) + "," +
                JDBCDeviceRepositoryAccessor.BOOLEAN_TYPE_ID + ")");
        execute(st,"insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + (instanceID++) + "," +
                JDBCDeviceRepositoryAccessor.INTEGER_TYPE_ID + ")");
        execute(st,"insert into VMTYPES values " +
                "(" + "'" + defaultProject + "'," + (instanceID++) + "," +
                JDBCDeviceRepositoryAccessor.TEXT_TYPE_ID + ")");

        //In another device repository project policies may have the same name
        // but the generated instance ids for them may be different.
        // So just change them for this project
        instanceID = id + 1;
        execute(st,"insert into VMPOLICY_TYPE values " +
                "(" + "'" + oneMoreProject + "','Policy1'," + (instanceID++) + ", 0)");
        execute(st,"insert into VMPOLICY_TYPE values " +
                "(" + "'" + oneMoreProject + "','Policy2'," + (instanceID++) + ", 0)");
        execute(st,"insert into VMPOLICY_TYPE values " +
                "(" + "'" + oneMoreProject + "','Policy3'," + (instanceID++) + ", 0)");

        instanceID = id + 1;
        execute(st,"insert into VMTYPES values " +
                "(" + "'" + oneMoreProject + "'," + (instanceID++) + "," +
                JDBCDeviceRepositoryAccessor.BOOLEAN_TYPE_ID + ")");
        execute(st,"insert into VMTYPES values " +
                "(" + "'" + oneMoreProject + "'," + (instanceID++) + "," +
                JDBCDeviceRepositoryAccessor.INTEGER_TYPE_ID + ")");
        execute(st,"insert into VMTYPES values " +
                "(" + "'" + oneMoreProject + "'," + (instanceID++) + "," +
                JDBCDeviceRepositoryAccessor.TEXT_TYPE_ID + ")");

        st.close();
        return instanceID;
    }

    /**
     * Build a JDBC repository
     */
    private InternalJDBCRepository createRepository(String project) throws Exception {
        InternalJDBCRepository repository = null;

        Class.forName("org.hsqldb.jdbcDriver");

        Connection conn = null;
        try {

            HashMap properties = new HashMap();
            properties.put(JDBCRepository.VENDOR_PROPERTY,
                    JDBCRepository.VENDOR_HYPERSONIC);
            properties.put(JDBCRepository.SOURCE_PROPERTY,
                    HypersonicManager.IN_MEMORY_SOURCE);
            properties.put(JDBCRepository.USERNAME_PROPERTY,
                    HypersonicManager.DEFAULT_USERNAME);
            properties.put(JDBCRepository.PASSWORD_PROPERTY,
                    HypersonicManager.DEFAULT_PASSWORD);
            properties.put(JDBCRepository.DEFAULT_PROJECT_NAME_PROPERTY,
                    project);
            properties.put(JDBCRepository.STANDARD_DEVICE_PROJECT_NAME_PROPERTY,
                    project);

            repository = (InternalJDBCRepository)
                    JDBCRepository.createRepository(properties)
                    .getLocalRepository();
            connection = repository.connect();

            JDBCRepositoryConnection jdbcConnection =
                    (JDBCRepositoryConnection) connection;

            conn = jdbcConnection.getConnection();

            createTables(conn);
        } catch (SQLException e) {
            handleException(e);
        } catch (RepositoryException e) {
            handleException(e);
        }
        return repository;
    }

    /**
     * Helper method to handle the exceptions.
     */
    private void handleException(Exception e) {
        e.printStackTrace();
        logger.error("unexpected-exception", e);
    }

    /**
     * Helper method that returns the hypersonic connection.
     * @return the hypersonic connection as a SQL connection.
     * @throws SQLException if any SQL exception is condition is raised.
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:hsqldb:" + HypersonicManager.IN_MEMORY_SOURCE,
                HypersonicManager.DEFAULT_USERNAME,
                HypersonicManager.DEFAULT_PASSWORD);
    }

    /**
     * Delete the Hypersonic SQL database from disk
     */
    private void removeRepository() {
        Connection conn;

        try {
            conn = getConnection();

            Statement st = conn.createStatement();
            st.execute("shutdown");
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore this
            }
        }
        catch(Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error removing database", e);
            }
        }
    }

    /**
     * Create the tables.
     */
    private void createTables(Connection conn) throws Exception {
        String[] tables = {
            "create table VMDEVICE_PATTERNS (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   NAME                 VARCHAR(255)      not null," +
            "   PATTERN              VARCHAR(255)      not null," +
            "   REVISION             NUMERIC(9)        not null" +
            ")",
            "create table VMPOLICY_VALUES  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   NAME                 VARCHAR(255)      not null," +
            "   POLICY               VARCHAR(200)      not null," +
            "   VALUE                VARCHAR(255)      not null," +
            "   REVISION             NUMERIC(9)        not null" +
            ")",

            "create table VMPOLICY_CATEGORY  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   CATEGORY_ID          NUMERIC(9)        not null," +
            "   CATEGORY_NAME        VARCHAR(255)      not null," +
            "   LANGUAGE             VARCHAR(32)                " +
            ")",

            "create table VMPOLICY_TYPE  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   POLICY               VARCHAR(200)      not null," +
            "   TYPE_INSTANCE_ID     NUMERIC(9)        not null," +
            "   CATEGORY_ID          NUMERIC(9)        not null" +
            ")",

            "create table VMPOLICY_LANG  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   POLICY               VARCHAR(200)      not null," +
            "   LANGUAGE             VARCHAR(255)              ," +
            "   POLICY_DESC_NAME     VARCHAR(255)              ," +
            "   POLICY_HELP          VARCHAR(255)              " +
            ")",

            "create table VMTYPES  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   TYPE_INSTANCE_ID     NUMERIC(9)        not null," +
            "   TYPE_ID              NUMERIC(3)        not null" +
            ")",

            "create table VMTYPES_RANGE  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   TYPE_INSTANCE_ID     NUMERIC(9)        not null," +
            "   MIN_VALUE            NUMERIC(9)        not null," +
            "   MAX_VALUE            NUMERIC(9)        not null" +
            ")",

            "create table VMTYPES_SELECTION  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   TYPE_INSTANCE_ID     NUMERIC(9)        not null," +
            "   KEYWORD              VARCHAR(255)      not null" +
            ")",

            "create table VMTYPES_SET  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   TYPE_INSTANCE_ID     NUMERIC(9)        not null," +
            "   MEMBER_INSTANCE_ID   NUMERIC(9)        not null," +
            "   ORDERED              NUMERIC(1)        default 0" +
            ")",


            "create table VMTYPES_STRUCTURE  (" +
            "   PROJECT              VARCHAR(255)      not null," +
            "   TYPE_INSTANCE_ID     NUMERIC(9)        not null," +
            "   FIELDNAME            VARCHAR(255)      not null," +
            "   FIELD_INSTANCE_ID    NUMERIC(9)        not null" +
            ")",

            "CREATE TABLE VMDEVICE_TACS  (" +
            "   PROJECT              VARCHAR(255)    NOT NULL," +
            "   NAME                 VARCHAR(20)     NOT NULL," +
            "   TAC                  NUMERIC(8)      NOT NULL" +
            ")"

        };

        // This is commented out since Hypersonic doesn't seem to support
        // constraints (unless the sql is not SQL_92, or something).
/*
        String[] constraints = {
            "alter table VMPOLICY_CATEGORY" +
            "   add constraint PK_VMPOLICY_CATEGORY primary key (PROJECT, CATEGORY_ID)",

            "alter table VMPOLICY_TYPE" +
            "   add constraint PK_VMPOLICY_TYPE primary key (PROJECT, POLICY)",

            "alter table VMTYPES" +
            "   add constraint PK_VMTYPES primary key (PROJECT, TYPE_INSTANCE_ID)",

            "alter table VMTYPES_RANGE" +
            "   add constraint PK_VMTYPES_RANGE primary key (PROJECT, TYPE_INSTANCE_ID)",

            "alter table VMTYPES_SELECTION" +
            "   add constraint PK_VMTYPES_SELECTION primary key (PROJECT, TYPE_INSTANCE_ID, KEYWORD)",

            "alter table VMTYPES_SET" +
            "   add constraint PK_VMTYPES_SET primary key (PROJECT, TYPE_INSTANCE_ID)",

            "alter table VMTYPES_STRUCTURE" +
            "   add constraint PK_VMTYPES_STRUCTURE primary key (PROJECT, TYPE_INSTANCE_ID, FIELDNAME)"
        };
*/

        Statement st = conn.createStatement();
        for (int i = 0; i < tables.length; i++) {
            st.execute(tables[i]);
        }

/*
        for (int i = 0; i < constraints.length; i++) {
            System.out.println("Executing: " + constraints[i]);
            st.execute(constraints[i]);
        }
*/
    }

    // ========================================================================
    //   POLICY DESCRIPTOR WRITE SUPPORT
    //   NOTE: this was coded separately to the read support due to severe
    //   time constraints. As such there probably refactoring opportunities to
    //   be had by factoring out common code between read and write.
    // ========================================================================

    private int policyCategoryCount;
    private int policyTypeCount;
    private int typesCount;
    private int typesSelectionCount;
    private int typesSetCount;
    private int typesRangeCount;
    private int typesStructureCount;

    public void testCreateBoolean() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {
                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                PolicyType type = new DefaultBooleanPolicyType();
                descriptor.setPolicyType(type);

                accessor.addPolicyDescriptor(connection, "boolean", descriptor);

                // Check the database contents
                checkPolicyTypeRow(conn, null, "boolean", 0, 0);
                checkTypesRow(conn, null, 0, 0);
                checkCategoryRow(conn, null, 0, "category");

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testRemoveBoolean() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                descriptor.setPolicyType(new DefaultBooleanPolicyType());
                accessor.addPolicyDescriptor(connection, "boolean", descriptor);
                accessor.removePolicyDescriptor(connection, "boolean");

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testCreateSelection() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultSelectionPolicyType type =
                        new DefaultSelectionPolicyType();
                type.addKeyword("k1");
                type.addKeyword("k2");
                type.complete();
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "selection", descriptor);

                // Check the database contents
                checkPolicyTypeRow(conn, null, "selection", 0, 0);
                checkTypesRow(conn, null, 0, 4);
                checkCategoryRow(conn, null, 0, "category");
                checkTypesSelectionRow(conn, "KEYWORD='k1'", 0, "k1");
                checkTypesSelectionRow(conn, "KEYWORD='k2'", 0, "k2");

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testRemoveSelection() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultSelectionPolicyType selection =
                        new DefaultSelectionPolicyType();
                selection.addKeyword("k1");
                selection.addKeyword("k2");
                selection.complete();
                descriptor.setPolicyType(selection);
                accessor.addPolicyDescriptor(connection, "selection",
                        descriptor);
                accessor.removePolicyDescriptor(connection, "selection");

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testCreateRange() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                PolicyType type = new DefaultRangePolicyType(1, 100);
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "range", descriptor);

                // Check the database contents
                checkPolicyTypeRow(conn, null, "range", 0, 0);
                checkTypesRow(conn, null, 0, 3);
                checkCategoryRow(conn, null, 0, "category");
                checkTypesRangeRow(conn, null, 0, 1, 100);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testRemoveRange() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                descriptor.setPolicyType(new DefaultRangePolicyType(3, 4));
                accessor.addPolicyDescriptor(connection, "range", descriptor);
                accessor.removePolicyDescriptor(connection, "range");

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testCreateStructure() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultStructurePolicyType type =
                        new DefaultStructurePolicyType();
                type.addFieldType("f1", new DefaultBooleanPolicyType());
                type.addFieldType("f2", new DefaultRangePolicyType(2, 99));
                type.complete();
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "structure", descriptor);

                // Check the database contents
                checkCategoryRow(conn, null, 0, "category");
                checkPolicyTypeRow(conn, null, "structure", 0, 0);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=0", 0, 7);
                // structure row and dependent boolean type
                checkTypesStructureRow(conn, "FIELDNAME='f1'", 0, "f1", 1);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=1", 1, 0);
                // structure row and dependent range type
                checkTypesStructureRow(conn, "FIELDNAME='f2'", 0, "f2", 2);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=2", 2, 3);
                checkTypesRangeRow(conn, null, 2, 2, 99);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testRemoveStructure() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultStructurePolicyType structure =
                        new DefaultStructurePolicyType();
                structure.addFieldType("f1", new DefaultBooleanPolicyType());
                structure.addFieldType("f2", new DefaultIntPolicyType());
                structure.complete();
                descriptor.setPolicyType(structure);
                accessor.addPolicyDescriptor(connection, "structure", descriptor);
                accessor.removePolicyDescriptor(connection, "structure");

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testCreateOrderedSet() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultOrderedSetPolicyType type =
                        new DefaultOrderedSetPolicyType(
                                new DefaultBooleanPolicyType());
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "oset", descriptor);

                // Check the database contents
                checkCategoryRow(conn, null, 0, "category");
                checkPolicyTypeRow(conn, null, "oset", 0, 0);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=0", 0, 5);
                // set row and dependent boolean type
                checkTypesSetRow(conn, null, 0, 1, true);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=1", 1, 0);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testRemoveOrderedSet() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultOrderedSetPolicyType type =
                        new DefaultOrderedSetPolicyType(
                                new DefaultBooleanPolicyType());
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "oset", descriptor);
                accessor.removePolicyDescriptor(connection, "oset");

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testCreateUnorderedSet() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultUnorderedSetPolicyType type =
                        new DefaultUnorderedSetPolicyType(
                                new DefaultBooleanPolicyType());
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "uset", descriptor);

                // Check the database contents
                checkCategoryRow(conn, null, 0, "category");
                checkPolicyTypeRow(conn, null, "uset", 0, 0);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=0", 0, 6);
                // set row and dependent boolean type
                checkTypesSetRow(conn, null, 0, 1, true);
                checkTypesRow(conn, "TYPE_INSTANCE_ID=1", 1, 0);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testRemoveUnorderedSet() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();
                descriptor.setCategory("category");
                DefaultUnorderedSetPolicyType type =
                        new DefaultUnorderedSetPolicyType(
                                new DefaultBooleanPolicyType());
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "uset", descriptor);
                accessor.removePolicyDescriptor(connection, "uset");

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    public void testMany() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, "#dp");
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                DefaultPolicyDescriptor descriptor =
                        new DefaultPolicyDescriptor();

                // boolean
                descriptor.setCategory("category1");
                descriptor.setPolicyType(new DefaultBooleanPolicyType());
                accessor.addPolicyDescriptor(connection, "bool1", descriptor);
                accessor.addPolicyDescriptor(connection, "bool2", descriptor);
                policyCategoryCount+=1;
                policyTypeCount+=2;
                typesCount+=2;
                checkCounts(conn);

                // int
                descriptor.setPolicyType(new DefaultIntPolicyType());
                accessor.addPolicyDescriptor(connection, "int1", descriptor);
                accessor.addPolicyDescriptor(connection, "int2", descriptor);
                policyTypeCount+=2;
                typesCount+=2;
                checkCounts(conn);

                // range
                descriptor.setPolicyType(new DefaultRangePolicyType(6, 9));
                accessor.addPolicyDescriptor(connection, "range1", descriptor);
                accessor.addPolicyDescriptor(connection, "range2", descriptor);
                policyTypeCount+=2;
                typesCount+=2;
                typesRangeCount+=2;
                checkCounts(conn);

                // selection
                descriptor.setCategory("category2");
                DefaultSelectionPolicyType selection =
                        new DefaultSelectionPolicyType();
                selection.addKeyword("keyword1");
                selection.addKeyword("keyword2");
                selection.complete();
                descriptor.setPolicyType(selection);
                accessor.addPolicyDescriptor(connection, "selection1", descriptor);
                accessor.addPolicyDescriptor(connection, "selection2", descriptor);
                policyCategoryCount+=1;
                policyTypeCount+=2;
                typesCount+=2;
                typesSelectionCount+=4;
                checkCounts(conn);

                // text
                descriptor.setPolicyType(new DefaultTextPolicyType());
                accessor.addPolicyDescriptor(connection, "text1", descriptor);
                accessor.addPolicyDescriptor(connection, "text2", descriptor);
                policyTypeCount+=2;
                typesCount+=2;
                checkCounts(conn);

                // ordered set
                PolicyType oset = new DefaultUnorderedSetPolicyType(
                                new DefaultBooleanPolicyType());
                descriptor.setPolicyType(oset);
                accessor.addPolicyDescriptor(connection, "oset1", descriptor);
                accessor.addPolicyDescriptor(connection, "oset2", descriptor);
                policyTypeCount+=2;
                typesCount+=4;
                typesSetCount+=2;
                checkCounts(conn);

                // unordered set
                descriptor.setCategory("category3");
                PolicyType uset = new DefaultUnorderedSetPolicyType(
                                new DefaultIntPolicyType());
                descriptor.setPolicyType(uset);
                accessor.addPolicyDescriptor(connection, "uset1", descriptor);
                accessor.addPolicyDescriptor(connection, "uset2", descriptor);
                policyCategoryCount+=1;
                policyTypeCount+=2;
                typesCount+=4;
                typesSetCount+=2;
                checkCounts(conn);

                // structure
                DefaultStructurePolicyType type =
                        new DefaultStructurePolicyType();
                type.addFieldType("field1", new DefaultBooleanPolicyType());
                type.addFieldType("field2", new DefaultRangePolicyType(2, 99));
                type.complete();
                descriptor.setPolicyType(type);
                accessor.addPolicyDescriptor(connection, "structure1", descriptor);
                accessor.addPolicyDescriptor(connection, "structure2", descriptor);
                policyTypeCount+=2;
                typesCount+=6;
                typesStructureCount+=4;
                typesRangeCount+=2;
                checkCounts(conn);

                accessor.removePolicyDescriptor(connection, "bool1");
                accessor.removePolicyDescriptor(connection, "bool2");
                accessor.removePolicyDescriptor(connection, "int1");
                accessor.removePolicyDescriptor(connection, "int2");
                accessor.removePolicyDescriptor(connection, "range1");
                accessor.removePolicyDescriptor(connection, "range2");
                accessor.removePolicyDescriptor(connection, "selection1");
                accessor.removePolicyDescriptor(connection, "selection2");
                accessor.removePolicyDescriptor(connection, "text1");
                accessor.removePolicyDescriptor(connection, "text2");
                accessor.removePolicyDescriptor(connection, "oset1");
                accessor.removePolicyDescriptor(connection, "oset2");
                accessor.removePolicyDescriptor(connection, "uset1");
                accessor.removePolicyDescriptor(connection, "uset2");
                accessor.removePolicyDescriptor(connection, "structure1");
                accessor.removePolicyDescriptor(connection, "structure2");

                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    private void checkCounts(Connection conn) throws SQLException {
        checkPolicyCategoryCount(conn, null, policyCategoryCount);
        checkPolicyTypeCount(conn, null, policyTypeCount);
        checkTypesCount(conn, null, typesCount);
        checkTypesSelectionCount(conn, null, typesSelectionCount);
        checkTypesSetCount(conn, null, typesSetCount);
        checkTypesRangeCount(conn, null, typesRangeCount);
        checkTypesStructureCount(conn, null, typesStructureCount);
    }

    private void checkPolicyTypeRow(Connection conn, String criteria,
            String policyName, int typeInstanceId, int categoryId)
            throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        int policyId = -1;
        try {
            statement = conn.createStatement();
            String sql = "select * from VMPOLICY_TYPE";
            if (criteria != null) {
                sql = sql + " where " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", typeInstanceId, rs.getInt("TYPE_INSTANCE_ID"));
                assertEquals("", categoryId, rs.getInt("CATEGORY_ID"));
                assertEquals("", policyName, rs.getString("POLICY"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkTypesRow(Connection conn, String criteria,
            int typeInstanceId, int typeId)
            throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "SELECT * from VMTYPES";
            if (criteria != null) {
                sql = sql + " WHERE " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", typeInstanceId, rs.getInt("TYPE_INSTANCE_ID"));
                assertEquals("", typeId, rs.getInt("TYPE_ID"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkCategoryRow(Connection conn, String criteria,
            int categoryId, String categoryName)
            throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "select * from VMPOLICY_CATEGORY";
            if (criteria != null) {
                sql = sql + " WHERE " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", categoryId, rs.getInt("CATEGORY_ID"));
                assertEquals("", categoryName, rs.getString("CATEGORY_NAME"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkTypesSelectionRow(Connection conn, String criteria,
            int typeInstanceId, String keyword) throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "select * from VMTYPES_SELECTION";
            if (criteria != null) {
                sql = sql + " WHERE " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", typeInstanceId, rs.getInt("TYPE_INSTANCE_ID"));
                assertEquals("", keyword, rs.getString("KEYWORD"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkTypesRangeRow(Connection conn, String criteria,
            int typeInstanceId, int min, int max) throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "select * from VMTYPES_RANGE";
            if (criteria != null) {
                sql = sql + " WHERE " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", typeInstanceId, rs.getInt("TYPE_INSTANCE_ID"));
                assertEquals("", min, rs.getInt("MIN_VALUE"));
                assertEquals("", max, rs.getInt("MAX_VALUE"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkTypesStructureRow(Connection conn, String criteria,
            int typeInstanceId, String fieldName, int fieldInstanceId)
            throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "select * from VMTYPES_STRUCTURE";
            if (criteria != null) {
                sql = sql + " WHERE " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", typeInstanceId, rs.getInt("TYPE_INSTANCE_ID"));
                assertEquals("", fieldName, rs.getString("FIELDNAME"));
                assertEquals("", fieldInstanceId, rs.getInt("FIELD_INSTANCE_ID"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkTypesSetRow(Connection conn, String criteria,
            int typeInstanceId, int memberInstanceId, boolean ordered)
            throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "select * from VMTYPES_SET";
            if (criteria != null) {
                sql = sql + " WHERE " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals("", "#defaultProject", rs.getString("PROJECT"));
                assertEquals("", typeInstanceId, rs.getInt("TYPE_INSTANCE_ID"));
                assertEquals("", memberInstanceId, rs.getInt("MEMBER_INSTANCE_ID"));
                assertEquals("", ordered, rs.getInt("MEMBER_INSTANCE_ID") > 0);
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void checkAllEmpty(Connection conn) throws SQLException {
        checkPolicyCategoryCount(conn, null, 0);
        checkPolicyTypeCount(conn, null, 0);
        checkTypesCount(conn, null, 0);
        checkTypesSelectionCount(conn, null, 0);
        checkTypesSetCount(conn, null, 0);
        checkTypesRangeCount(conn, null, 0);
        checkTypesStructureCount(conn, null, 0);
    }

    private void checkPolicyCategoryCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMPOLICY_CATEGORY", criteria, count);
    }

    private void checkPolicyTypeCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMPOLICY_TYPE", criteria, count);
    }

    private void checkTypesCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMTYPES", criteria, count);
    }

    private void checkTypesSelectionCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMTYPES_SELECTION", criteria, count);
    }

    private void checkTypesSetCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMTYPES_SET", criteria, count);
    }

    private void checkTypesRangeCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMTYPES_RANGE", criteria, count);
    }

    private void checkTypesStructureCount(Connection conn, String criteria,
            int count) throws SQLException {

        checkTableCount(conn, "VMTYPES_STRUCTURE", criteria, count);
    }

    private void checkTableCount(Connection conn, String tableName,
            String criteria, int count) throws SQLException {

        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            String sql = "SELECT COUNT(*) AS ROWCOUNT FROM " + tableName;
            if (criteria != null) {
                sql = sql + " where " + criteria;
            }
            rs = statement.executeQuery(sql);
            if (rs.next() == false) {
                fail("No rows found");
            } else {
                assertEquals(tableName, count, rs.getInt("ROWCOUNT"));
            }
            if (rs.next() != false) {
                fail("More than one row found.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    /** Build a JDBC repository */
    private InternalJDBCRepository createRepository(String source,
            String defaultProject, String defaultDeviceProject)
            throws Exception {

        InternalJDBCRepository repository = null;

        JDBCRepositoryFactory factory =
                JDBCRepositoryFactory.getDefaultInstance();
        MCSDriverConfiguration driverConfiguration =
                factory.createMCSDriverConfiguration();
        driverConfiguration.setDriverVendor(JDBCDriverVendor.HYPERSONIC);
        driverConfiguration.setSource(source);
        DataSource dataSource =
                factory.createMCSDriverDataSource(driverConfiguration);

        JDBCRepositoryConfiguration configuration =
                factory.createJDBCRepositoryConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setUsername(HypersonicManager.DEFAULT_USERNAME);
        configuration.setPassword(HypersonicManager.DEFAULT_PASSWORD);

//        HashMap properties = new HashMap();
//        properties.put(JDBCRepository.VENDOR_PROPERTY,
//                       JDBCRepository.VENDOR_HYPERSONIC);
//        properties.put(JDBCRepository.SOURCE_PROPERTY,
//                       source);
//        properties.put(JDBCRepository.USERNAME_PROPERTY,
//                       HypersonicManager.DEFAULT_USERNAME);
//        properties.put(JDBCRepository.PASSWORD_PROPERTY,
//                       HypersonicManager.DEFAULT_PASSWORD);
//        properties.put(JDBCRepository.DEFAULT_PROJECT_NAME_PROPERTY,
//                defaultProject);
//        properties.put(JDBCRepository.STANDARD_DEVICE_PROJECT_NAME_PROPERTY,
//                defaultDeviceProject);
//        repository = (InternalJDBCRepository)
//                JDBCRepository.createRepository(properties).getLocalRepository();

        repository = (InternalJDBCRepository)
                factory.createJDBCRepository(configuration);

        return repository;
    }

    /**
     * Test the enumerating of all TAC numbers.
     */
    public void testEnumerateDeviceNamesTACNoneFound() throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, defaultProject);
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);

                Connection conn = connection.getConnection();
                createTables(conn);

                RepositoryEnumeration enumeration =
                        accessor.enumerateDeviceTACs(connection);
                assertFalse("No TACs expected", enumeration.hasNext());

                // Now test with lots of tacs.
                int tacCount = populateWithTACs(conn);
                enumeration = accessor.enumerateDeviceTACs(connection);
                int actual = 0;
                while(enumeration.hasNext()) {
                    DeviceTACPair pair =
                            (DeviceTACPair) enumeration.next();
                    if (pair.getTAC() == 350612) {
                        assertEquals("Device 350612 should be Nokia-6210",
                                "Nokia-6210", pair.getDeviceName());
                    } else {
                        assertEquals("Name should match: ",
                              "device-" + pair.getTAC(), pair.getDeviceName());
                    }
                    ++actual;
                }
                assertEquals("Number of tacs should  match", tacCount, actual);

                // Check the database contents are empty
                checkAllEmpty(conn);

                connection.disconnect();
                repository.terminate();
            }
        });
    }

    /**
     * Add device policies.
     */
    private void populateWithPolicies(Connection connection) throws Exception {

        Statement st = connection.createStatement();
        execute(st, "INSERT INTO VMPOLICY_VALUES VALUES ('" + defaultProject +
                "', 'PC', 1, 'apolicyvalue', 1)");
        execute(st, "INSERT INTO VMPOLICY_LANG VALUES ('" + defaultProject +
                "', 1, null, 'apolicy', null)");
        st.close();
    }

    /**
     * Add device patterns.
     */
    private void populateWithPatterns(Connection connection) throws Exception {

        Statement st = connection.createStatement();
        execute(st, "INSERT INTO VMDEVICE_PATTERNS VALUES ('" + defaultProject +
                "', 'PC', 'apattern', 1)");
        st.close();
    }
    /**
     * Populate the database with structure data.
     */
    private int populateWithTACs(Connection connection) throws Exception {

        Statement st = connection.createStatement();

        int tacs = 1054;
        for (int i = 0; i < tacs; i++) {
            execute(st, "insert into VMDEVICE_TACS values " +
                    "(" + "'" + defaultProject + "','device-" + i + "'," +
                    i + ")");
        }
        execute(st, "INSERT INTO VMDEVICE_TACS VALUES ('" + defaultProject +
                "', 'Nokia-6210', '350612')");
        st.close();
        return tacs + 1;
    }

    // Javadoc inherited
    protected void runDeviceRepositoryAccessorTest(
            final AbstractDeviceRepositoryAccessorTestAbstract.
            DeviceRepositoryAccessorTest test)
            throws Exception {
        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {
                InternalJDBCRepository repository = createRepository(
                        hypersonicMgr.getSource(), null, defaultProject);
                JDBCRepositoryConnection connection =
                        (JDBCRepositoryConnection) repository.connect();
                JDBCDeviceRepositoryAccessor accessor =
                        new JDBCDeviceRepositoryAccessor(repository, location);
                Connection conn = connection.getConnection();
                createTables(conn);
                populateWithPolicies(conn);
                populateWithPatterns(conn);
                populateWithTACs(conn);
                test.runTest(accessor, connection);
            }
        });
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 02-Mar-05	7130/2	rgreenall	VBM:2005011201 Fixed bug where the mapping of a user agent pattern to a device name would fail if the pattern was one character greater than the device name.

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 23-Dec-04	6472/3	allan	VBM:2004121003 Intern device names and device policies

 23-Dec-04	6472/1	allan	VBM:2004121003 Intern device names and device policies

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/5	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 30-Sep-04	4511/1	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 06-Aug-04	5121/1	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 05-Aug-04	5072/5	byron	VBM:2004080304 JDBC foundation accessors for device identification by TAC - rework issues

 04-Aug-04	5072/3	byron	VBM:2004080304 JDBC foundation accessors for device identification by TAC

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 28-Jul-04	4964/3	geoff	VBM:2004072602 Public API for Device Repository: JDBC metadata write support

 27-Jul-04	4954/1	byron	VBM:2004072304 Public API for Device Repository: implement JDBC metadata read support

 ===========================================================================
*/
