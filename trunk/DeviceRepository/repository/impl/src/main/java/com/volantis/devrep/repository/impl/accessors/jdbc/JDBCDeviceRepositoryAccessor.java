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

package com.volantis.devrep.repository.impl.accessors.jdbc;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.DeviceTACPair;
import com.volantis.devrep.repository.impl.TACValue;
import com.volantis.devrep.repository.impl.devices.category.DefaultCategoryDescriptor;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultBooleanPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultIntPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultOrderedSetPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultRangePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultSelectionPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultStructurePolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultTextPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultUnorderedSetPolicyType;
import com.volantis.mcs.accessors.CollectionRepositoryEnumeration;
import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;
import com.volantis.mcs.accessors.jdbc.JDBCTemplate;
import com.volantis.mcs.accessors.jdbc.PreparedStatementSetter;
import com.volantis.mcs.accessors.jdbc.RowProcessor;
import com.volantis.mcs.accessors.jdbc.StringEnumeration;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.BooleanPolicyType;
import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.mcs.devices.policy.types.OrderedSetPolicyType;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.RangePolicyType;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.devices.policy.types.SetPolicyType;
import com.volantis.mcs.devices.policy.types.SimplePolicyType;
import com.volantis.mcs.devices.policy.types.StructurePolicyType;
import com.volantis.mcs.devices.policy.types.TextPolicyType;
import com.volantis.mcs.devices.policy.types.UnorderedSetPolicyType;
import com.volantis.mcs.repository.LocalJDBCRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.AlternateNames;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.mcs.repository.jdbc.VolantisSchemaNamesFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * This class implements common behaviour for most JDBCDeviceRepositoryAccessor
 * classes.
 */
public class JDBCDeviceRepositoryAccessor
        extends AbstractDeviceRepositoryAccessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JDBCDeviceRepositoryAccessor.class);
    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    JDBCDeviceRepositoryAccessor.class);

    /**
     * Volantis specific factory for {@link AlternateNames}.
     */
    private static final VolantisSchemaNamesFactory FACTORY =
            VolantisSchemaNamesFactory.getDefaultInstance();

    /**
     * Constant for the name of the DEVICE_PATTERN table in the database.
     */
    private static final AlternateNames DEVICE_PATTERNS_TABLE_NAME =
            FACTORY.createTableNames("DEVICE_PATTERNS");

    /**
     * Constant for the name of the DEVICE_TACS table in the database.
     */
    private static final AlternateNames DEVICE_TACS_TABLE_NAME =
            FACTORY.createTableNames("DEVICE_TACS");

    /**
     * Constant for the name of the POLICY_VALUES table in the database.
     */
    private static final AlternateNames DEVICE_POLICIES_TABLE_NAME =
        FACTORY.createTableNames("POLICY_VALUES", "PLCY_VALUES");

    // todo these constants should possibly move to an alternative location (BEGIN).
    /**
     * The ranges table name.
     */
    private static final AlternateNames VMTYPES_RANGE =
            FACTORY.createTableNames("TYPES_RANGE");

    /**
     * The selection table name.
     */
    private static final AlternateNames VMTYPES_SELECTION =
            FACTORY.createTableNames("TYPES_SELECTION");

    /**
     * The set table name.
     */
    private static final AlternateNames VMTYPES_SET =
            FACTORY.createTableNames("TYPES_SET");

    /**
     * The structure table name.
     */
    private static final AlternateNames VMTYPES_STRUCTURE =
            FACTORY.createTableNames("TYPES_STRUCTURE");

    /**
     * The types table name.
     */
    private static final AlternateNames VMTYPES =
            FACTORY.createTableNames("TYPES");

    /**
     * The policy type table name.
     */
    private static final AlternateNames VMPOLICY_TYPE =
            FACTORY.createTableNames("POLICY_TYPE", "PLCY_TYPE");

    /**
     * The policy category table name.
     */
    private static final AlternateNames VMPOLICY_CATEGORY =
            FACTORY.createTableNames("POLICY_CATEGORY", "PLCY_CATEGORY");

    /**
     * The policy names/descriptions table name.
     */
    private static final AlternateNames VMPOLICY_LANG =
            FACTORY.createTableNames("POLICY_LANG");

    private static final AlternateNames TYPE_INSTANCE_ID_COLUMN_NAMES =
            FACTORY.createColumnNames("TYPE_INSTANCE_ID");
    private static final AlternateNames TYPE_ID_COLUMN_NAMES =
            FACTORY.createColumnNames("TYPE_ID");
    private static final AlternateNames PROJECT_COLUMN_NAMES =
            FACTORY.createColumnNames("PROJECT");
    private static final AlternateNames NAME_COLUMN_NAMES =
            FACTORY.createColumnNames("NAME");
    private static final AlternateNames PATTERN_COLUMN_NAMES =
            FACTORY.createColumnNames("PATTERN");
    private static final AlternateNames POLICY_COLUMN_NAMES =
            FACTORY.createColumnNames("POLICY");
    private static final AlternateNames VALUE_COLUMN_NAMES =
            FACTORY.createColumnNames("VALUE");
    private static final AlternateNames TAC_COLUMN_NAMES =
            FACTORY.createColumnNames("TAC");
    private static final AlternateNames CATEGORY_NAME_COLUMN_NAMES =
            FACTORY.createColumnNames("CATEGORY_NAME");
    private static final AlternateNames CATEGORY_ID_COLUMN_NAMES =
            FACTORY.createColumnNames("CATEGORY_ID");
    private static final AlternateNames LANGUAGE_COLUMN_NAMES =
            FACTORY.createColumnNames("LANGUAGE");
    private static final AlternateNames POLICY_DESC_NAMES =
            FACTORY.createColumnNames("POLICY_DESC_NAME");
    private static final AlternateNames POLICY_HELP_COLUMN_NAMES =
            FACTORY.createColumnNames("POLICY_HELP");
    private static final AlternateNames FIELD_NAME_COLUMN_NAMES =
            FACTORY.createColumnNames("FIELDNAME");
    private static final AlternateNames FIELD_INSTANCE_ID_COLUMN_NAMES =
            FACTORY.createColumnNames("FIELD_INSTANCE_ID");
    private static final AlternateNames ORDERED_COLUMN_NAMES =
            FACTORY.createColumnNames("ORDERED");
    private static final AlternateNames MEMBER_INSTANCE_ID_COLUMN_NAMES =
            FACTORY.createColumnNames("MEMBER_INSTANCE_ID");
    private static final AlternateNames MIN_VALUE_COLUMN_NAMES =
            FACTORY.createColumnNames("MIN_VALUE");
    private static final AlternateNames MAX_VALUE_COLUMN_NAMES =
            FACTORY.createColumnNames("MAX_VALUE");
    private static final AlternateNames KEYWORD_COLUMN_NAMES =
            FACTORY.createColumnNames("KEYWORD");

    /**
     * The boolean type id.
     */
    public static final int BOOLEAN_TYPE_ID = 0;

    /**
     * The integer type id.
     */
    public static final int INTEGER_TYPE_ID = 1;

    /**
     * The text type id.
     */
    public static final int TEXT_TYPE_ID = 2;

    /**
     * The range type id.
     */
    public static final int RANGE_TYPE_ID = 3;

    /**
     * The selection type id.
     */
    public static final int SELECTION_TYPE_ID = 4;

    /**
     * The ordered set type id.
     */
    public static final int ORDERED_SET_TYPE_ID = 5;

    /**
     * The unordered set type id.
     */
    private static final int UNORDERED_SET_TYPE_ID = 6;

    /**
     * The structure type id.
     */
    public static final int STRUCTURE_TYPE_ID = 7;
    // todo these constants should move to an alternative location (END).

    /**
     * The language value which represents the default langauge.
     * <p>
     * This cannot be "" as on Oracle "" == null and it forms part of a key.
     */
    private static final String DEFAULT_LANGAUGE = "<default>";

    /**
     * The resolved Device Patterns table, from the repository in use.
     */
    private String resolvedDevicePatternsTableName;

    /**
     * The resolved Device TACs table, from the repository in use
     */
    private String resolvedDeviceTACsTableName;

    /**
     * The resolved Device Policies table, from the repository in use.
     */
    private final String resolvedDevicePoliciesTableName;
    private final LocalJDBCRepository repository;
    private final DeviceRepositoryLocation location;

    /**
     * Create an instance of this object with the specified repository.
     * @param repository the JDBC repository.
     * @param location
     */
    public JDBCDeviceRepositoryAccessor(
            LocalJDBCRepository repository,
            DeviceRepositoryLocation location) {
        super(repository);

        this.repository = repository;
        this.location = location;

        // Fake up a project for use with some other methods.
        String deviceProjectName = location.getDeviceRepositoryName();
        if (deviceProjectName == null) {
            // This should never happen.
            throw new IllegalStateException("attempt to create implicit " +
                    "JDBC project for standard device without specifying the " +
                    "project name");
        }

        resolvedDevicePatternsTableName =
            repository.getAppropriateName(DEVICE_PATTERNS_TABLE_NAME);
        resolvedDevicePoliciesTableName =
            repository.getAppropriateName(DEVICE_POLICIES_TABLE_NAME);
        resolvedDeviceTACsTableName =
            repository.getAppropriateName(DEVICE_TACS_TABLE_NAME);
    }

    /**
     * Use the repository to resolve the correct field name.
     *
     * @param connection The connection to get the repository from
     * @param fieldName The field name to resolve
     * @return The resolved field nname.
     */
    private static String resolveFieldName(JDBCRepositoryConnection connection,
                                           AlternateNames fieldName) {

        InternalJDBCRepository repository = (InternalJDBCRepository)
                connection.getRepository();
        return repository.getAppropriateName(fieldName);
    }

    /**
     * Use the repository to resolve the correct table name.
     *
     * @param connection The connection to get the repository from
     * @param tableName The tablename to resolve
     * @return The resolved tablename.
     */
    private static String resolveTableName(JDBCRepositoryConnection connection,
                                           AlternateNames tableName) {

        InternalJDBCRepository repository = (InternalJDBCRepository)
                connection.getRepository();
        return repository.getAppropriateName(tableName);
    }

  // Javadoc inherited from super class.
  public
  RepositoryEnumeration enumerateDevicePatterns (RepositoryConnection connection)
    throws RepositoryException {

    // Cast the repository connection to a JDBC Connection.
    JDBCRepositoryConnection jdbcConnection
      = (JDBCRepositoryConnection) connection;

    // Get the java.sql.Connection out of the JDBC Connection.
    Connection sqlConnection = jdbcConnection.getConnection ();

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = sqlConnection.createStatement ();

      String projectName = getProjectName();
      String projectQuoted = JDBCAccessorHelper.quoteValue(projectName);

      // Resolve field names
      String name = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
      String pattern = resolveFieldName(jdbcConnection, PATTERN_COLUMN_NAMES);
      String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);

      String sql
              = "select "
              + name + " , "
              + pattern
              + " from "
              + resolvedDevicePatternsTableName
              + " where "
              + projectField + " = " + projectQuoted;

      if (logger.isDebugEnabled()){
          logger.debug (sql);
      }

      rs = stmt.executeQuery (sql);

      return new StringPairEnumeration (rs);
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      try {
        if (stmt != null) {
          stmt.close ();
        }
      }
      catch (SQLException e) {
        logger.error("sql-exception", e);
        throw new JDBCRepositoryException (e);
      }
      throw new JDBCRepositoryException (sqle);
    }
  }

  // Javadoc inherited from super class.
  protected void addDeviceImpl (
          RepositoryConnection connection,
          DefaultDevice device)
      throws RepositoryException {

    // Cast the repository connection to a JDBC Connection in
    // order to get the java.sql.Connection out.
    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
        getConnection ();

    String projectName = getProjectName();

    PreparedStatement pstmt = null;

    // Resolve field names

    // Cast the repository connection to a JDBC Connection.
    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
    String nameField = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
    String policyField = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
    String valueField = resolveFieldName(jdbcConnection, VALUE_COLUMN_NAMES);
    String patternField = resolveFieldName(jdbcConnection, PATTERN_COLUMN_NAMES);
    String tacField = resolveFieldName(jdbcConnection, TAC_COLUMN_NAMES);

    try {
      String sql
              = "insert into " + resolvedDevicePoliciesTableName
              + "("
              + projectField + " , "
              + nameField + " , "
              + policyField + " , "
              + valueField + ")"
              + " values ( ? , ? , ? , ? )";

      pstmt = sqlConnection.prepareStatement (sql);

      String deviceName = device.getName ();

      JDBCAccessorHelper.setStringValue(pstmt, 1, projectName );
      JDBCAccessorHelper.setStringValue(pstmt, 2, deviceName);

      for (Iterator i = device.getPolicyNames(); i.hasNext ();) {
        String policy = (String) i.next();
        String value = device.getPolicyValue(policy);

        JDBCAccessorHelper.setStringValue(pstmt, 3, policy);
        JDBCAccessorHelper.setStringValue(pstmt, 4, value);

        if(logger.isDebugEnabled()){
          logger.debug ("Inserting into "
              + resolvedDevicePoliciesTableName + " , "
              + " NAME: " + deviceName
              + " POLICY: " + policy
              + " VALUE: " + value);
        }

        pstmt.executeUpdate ();
      }

      // Close the statement and set it to null to prevent the finally clause
      // from trying to close it again.
      pstmt.close ();
      pstmt = null;

      // Now add the device patterns if any.
      Map patterns = device.getPatterns ();

      if (patterns != null) {
        sql = "insert into " + resolvedDevicePatternsTableName
            + "("
            + projectField + " , "
            + nameField + " , "
            + patternField + ")"

            + " values ( ? , ? , ? )";

        pstmt = sqlConnection.prepareStatement (sql);
        JDBCAccessorHelper.setStringValue(pstmt, 1, projectName );
        JDBCAccessorHelper.setStringValue(pstmt, 2, deviceName);

        for (Iterator i = patterns.entrySet ().iterator (); i.hasNext ();) {
          Map.Entry entry = (Map.Entry) i.next ();
          String pattern = entry.getKey ().toString ();

          JDBCAccessorHelper.setStringValue(pstmt, 3, pattern);

          if(logger.isDebugEnabled()){
            logger.debug ("Inserting into " + resolvedDevicePatternsTableName
                + " NAME: " + deviceName
                + " PATTERN: " + pattern);
          }
          pstmt.executeUpdate ();
        }
        // Close the statement and set it to null to prevent the finally clause
        // from trying to close it again.
        pstmt.close ();
        pstmt = null;
      }
      // Now add the TACs if any.
      Set tacs = device.getTACValues();

      if (tacs != null) {
        sql = "insert into " + resolvedDeviceTACsTableName
            + " ( "
            + projectField + " , "
            + nameField + " , "
            + tacField
            + " ) "
            + " values ( ? , ? , ? )";

        pstmt = sqlConnection.prepareStatement(sql);
        JDBCAccessorHelper.setStringValue(pstmt, 1, projectName);
        JDBCAccessorHelper.setStringValue(pstmt, 2, deviceName);

        Iterator it = tacs.iterator();
        while (it.hasNext()) {
          TACValue tac = (TACValue) it.next();
          pstmt.setLong(3, tac.getLongTAC());

          if (logger.isDebugEnabled()) {
            logger.debug("Inserting into "
                + resolvedDeviceTACsTableName
                + " NAME: " + deviceName
                + " TAC: " + tac.getLongTAC());
          }

          pstmt.executeUpdate();
        }
        // Close the statement and set it to null to prevent the finally clause
        // from trying to close it again.
        pstmt.close ();
        pstmt = null;
      }
    } catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException(sqle);
    } finally {
      if (pstmt != null) {
        try {
          pstmt.close();
        } catch (SQLException e) {
          logger.error("sql-exception", e);
          throw new JDBCRepositoryException(e);
        }
      }
    }
  }


  // Javadoc inherited from super class.
  protected void removeDeviceImpl (RepositoryConnection connection,
                                   String deviceName)
      throws RepositoryException {

    // Cast the repository connection to a JDBC Connection in
    // order to get the java.sql.Connection out.
    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
        getConnection ();

    String projectName = getProjectName();

    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    String projectQuoted = JDBCAccessorHelper.quoteValue(projectName);
    String deviceQuoted = JDBCAccessorHelper.quoteValue(deviceName);

    Statement stmt = null;
    try {
      stmt = sqlConnection.createStatement ();

      // Resolve field names
      String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String name = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);

      String sql;

      // Delete the device policies.
      sql = "delete from " + resolvedDevicePoliciesTableName
          + " where "
          + project + " = " + projectQuoted
          + " and " + name + " = " + deviceQuoted;

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }
      stmt.executeUpdate (sql);

      // Delete the device patterns.
      sql = "delete from " + resolvedDevicePatternsTableName
          + " where "
          + project + " = " + projectQuoted
          + " and " + name + " = " + deviceQuoted;

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }
      stmt.close();
      stmt = sqlConnection.createStatement ();
      stmt.executeUpdate (sql);
      // Delete the TACs.
      sql = "delete from " + resolvedDeviceTACsTableName
          + " where "
          + project + " = "  + projectQuoted
          + " and "
          + name + " = " + deviceQuoted;
      stmt.close();
      stmt = sqlConnection.createStatement();
      stmt.executeUpdate(sql);
    } catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException(sqle);
    } finally {
      close(stmt);
    }
  }

    // javadoc inherited
    protected void initializeConnection(RepositoryConnection connection)
            throws RepositoryException {
        ((JDBCRepositoryConnection)connection).getConnection ();
    }

    // javadoc inherited
    protected void releaseConnection(RepositoryConnection connection)
            throws RepositoryException {
        ((JDBCRepositoryConnection)connection).releaseConnection();
    }


  // Javadoc inherited from super class.
  protected DefaultDevice retrieveDeviceImpl (RepositoryConnection connection,
                                               String deviceName)
      throws RepositoryException {

    // Cast the repository connection to a JDBC Connection in
    // order to get the java.sql.Connection out.
    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
        getConnection ();

    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    String projectName = getProjectName();
    String projectQuoted = JDBCAccessorHelper.quoteValue(projectName);
    String deviceQuoted = JDBCAccessorHelper.quoteValue(deviceName);

    // resolve table name
    String vmPolicyLangTable = resolveTableName(jdbcConnection, VMPOLICY_LANG);

    Statement stmt = null;
    ResultSet rs;
    try {
      stmt = sqlConnection.createStatement ();

      String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String nameField = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
      String policyField = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
      String valueField = resolveFieldName(jdbcConnection, VALUE_COLUMN_NAMES);
      String patternField = resolveFieldName(jdbcConnection, PATTERN_COLUMN_NAMES);
      String tacField = resolveFieldName(jdbcConnection, TAC_COLUMN_NAMES);

      String sql
          = "select "
          + policyField + " , "
          + valueField
          + " from " + resolvedDevicePoliciesTableName
          + " where "
          + projectField + " = " + projectQuoted
          + " and " + nameField + " = " + deviceQuoted;

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }

      rs = stmt.executeQuery (sql);

      // A device must have at least one policy set, even if it is only the
      // fallback.
      if (!rs.next ()) {
        return null;
      }

      Map policies = new HashMap ();
      do {
        String policy = rs.getString (1);
        String value = rs.getString (2);
        policies.put (policy.intern(), value == null ? null : value.intern());
      } while (rs.next ());

      // Close the result set.
      rs.close ();

      // Retrieve the patterns for the device.
      sql = "select "
          + patternField
          + " from " + resolvedDevicePatternsTableName
          + " where "
          + projectField + " = " + projectQuoted
          + " and " + nameField + " = " + deviceQuoted;

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }

      stmt.close();
      stmt = sqlConnection.createStatement ();
      rs = stmt.executeQuery (sql);

      Map patterns = null;
      if (rs.next ()) {
        patterns = new HashMap();
        do {
          patterns.put(rs.getString(1), null);
        } while (rs.next ());
      }

      // Retrieve the TACs for the device.
      sql = "select " + tacField + " from "
          + resolvedDeviceTACsTableName
          + " where "
          + projectField + " = " + projectQuoted
          + " and "
          + nameField + " = " + deviceQuoted;

      if (logger.isDebugEnabled()) {
        logger.debug(sql);
      }

      stmt.close();
      stmt = sqlConnection.createStatement();
      rs = stmt.executeQuery(sql);

      Set tacs = null;
      if (rs.next()) {
        tacs = new HashSet();
        do {
          tacs.add(new TACValue(rs.getLong(1)));
        } while (rs.next());
      }

      DefaultDevice device =
          new DefaultDevice(deviceName, policies, getPolicyValueFactory());
      device.setPatterns(patterns);
      device.setTACValues(tacs);

      return device;
    } catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException(sqle);
    } finally {
      close(stmt);
    }
  }

  // javadoc inherited
  public void renameDeviceImpl(RepositoryConnection connection,
                               String deviceName, String newName)
      throws RepositoryException {

    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
        getConnection ();

    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    String projectName = getProjectName();

    Statement stmt = null;
    try {
      stmt = sqlConnection.createStatement ();

      String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String name = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
      String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
      String value = resolveFieldName(jdbcConnection, VALUE_COLUMN_NAMES);

      String sql;

      sql = "update "
          + resolvedDevicePoliciesTableName + " set "
          + name + " = " + JDBCAccessorHelper.quoteValue(newName)
          + " where "
          + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
          + " and " + name + " = " + JDBCAccessorHelper.quoteValue(deviceName);

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }
      stmt.executeUpdate(sql);

      sql = "update " + resolvedDevicePoliciesTableName + " set "
          + value + " = " + JDBCAccessorHelper.quoteValue(newName)
          + " where "
          + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
          + " and "
          + policy + " = " + JDBCAccessorHelper.quoteValue("fallback")
          + " and "
          + value + " = " + JDBCAccessorHelper.quoteValue(deviceName);

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }
      stmt.close();
      stmt = sqlConnection.createStatement ();
      stmt.executeUpdate(sql);

      sql = "update " + resolvedDevicePatternsTableName + " set "
          + name + " = " + JDBCAccessorHelper.quoteValue(newName)
          + " where "
          + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
          + " and " + name + " = " + JDBCAccessorHelper.quoteValue(deviceName);

      if(logger.isDebugEnabled()){
        logger.debug (sql);
      }
      stmt.close();
      stmt = sqlConnection.createStatement ();
      stmt.executeUpdate(sql);

      stmt.close ();
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException(sqle);
    }
    finally {
      close(stmt);
    }
  }

    // Javadoc inherited from super class.
    public RepositoryEnumeration enumerateDeviceNames(RepositoryConnection connection)
            throws RepositoryException {

        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        String name = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);

        return repository.selectUniqueValues(jdbcConnection, name,
                resolvedDevicePoliciesTableName,
                getProjectName());
    }

  // Javadoc inherited from super class.
  public RepositoryEnumeration enumerateDeviceFallbacks (
          RepositoryConnection connection)
    throws RepositoryException {

    // Cast the repository connection to a JDBC Connection in
    // order to get the java.sql.Connection out.
    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
                                    getConnection ();

    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    String projectName = getProjectName();

    Statement stmt = null;
    try {
      stmt = sqlConnection.createStatement ();

      String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String name = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
      String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
      String value = resolveFieldName(jdbcConnection, VALUE_COLUMN_NAMES);

      String sql
              = "select " + name + " , "
              + value
              + " from " + resolvedDevicePoliciesTableName
              + " where "
              + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
              + " and "
              + policy + "  = 'fallback'"
              + " order by " + value + ", " + name;

      if(logger.isDebugEnabled()){
          logger.debug (sql);
      }

      ResultSet rs = stmt.executeQuery (sql);

      RepositoryEnumeration e = new StringPairEnumeration (rs);

      // From this point on the statement should not be closed as it is owned
      // by the RepositoryEnumeration.
      stmt = null;

      return e;
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException (sqle);
    }
    finally {
        close(stmt);
    }
  }

    // javadoc inherited.
    public RepositoryEnumeration enumerateDeviceTACs(
            final RepositoryConnection connection)
            throws RepositoryException {

        JDBCRepositoryConnection jdbcConnection =
                (JDBCRepositoryConnection) connection;

        final String projectName = getProjectName();

        // resolve field names
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        String nameField = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
        String tacField = resolveFieldName(jdbcConnection, TAC_COLUMN_NAMES);

        String sql = "select "
                + nameField + " , "
                + tacField
                + " from "
                + resolvedDeviceTACsTableName
                + " where "
                + projectField
                + " = ?";

        RowProcessor rowProcessor = new RowProcessor() {
            public Object processRow(ResultSet rs) throws SQLException {
                String deviceName = rs.getString(1);
                long tac = rs.getLong(2);
                return new DeviceTACPair(tac, deviceName);
            }
        };
        PreparedStatementSetter setter = new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps)
                    throws SQLException {

                if (logger.isDebugEnabled()) {
                    logger.debug("Selecting from "
                            + DEVICE_TACS_TABLE_NAME + ","
                            + " PROJECT: " + projectName);
                }
                JDBCAccessorHelper.setStringValue(ps, 1, projectName);
            }
        };
        return new CollectionRepositoryEnumeration(
                template.query(jdbcConnection, sql, rowProcessor,setter));
    }

    // Javadoc inherited from super class.
  protected
  RepositoryEnumeration enumerateDevicesChildrenImpl (
            RepositoryConnection connection,
            String deviceName)
    throws RepositoryException {

    // Cast the repository connection to a JDBC Connection in
    // order to get the java.sql.Connection out.
    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
                                    getConnection ();

    JDBCRepositoryConnection jdbcConnection
            = (JDBCRepositoryConnection) connection;

    String projectName = getProjectName();

    Statement stmt = null;
    try {

      stmt = sqlConnection.createStatement ();

      // resolve field names
      String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String name = resolveFieldName(jdbcConnection, NAME_COLUMN_NAMES);
      String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
      String value = resolveFieldName(jdbcConnection, VALUE_COLUMN_NAMES);

      String sql
        = "select " + name + " from " + resolvedDevicePoliciesTableName
          + " where "
          + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
          + " and " + policy + " = 'fallback'"
          + " and "
          + value + " " + (deviceName == null ? "is " : "= ")
          + JDBCAccessorHelper.quoteValue(deviceName);

      if(logger.isDebugEnabled()){
          logger.debug (sql);
      }

      ResultSet rs = stmt.executeQuery (sql);

      RepositoryEnumeration e = new StringEnumeration(rs);

      // From this point on the statement should not be closed as it is owned
      // by the RepositoryEnumeration.
      stmt = null;

      return e;
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException (sqle);
    }
    finally {
        close(stmt);
    }
  }

    // javadoc inherited
  public void removePolicy(RepositoryConnection connection,
                           String policyName)
    throws RepositoryException {

    String projectName = getProjectName();

    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    Statement stmt = null;
    try {
      // remove from VMPOLICY_VALUES
      String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);

      final String quotedPolicyname = JDBCAccessorHelper.quoteValue(policyName);
      final String quotedProjectName = JDBCAccessorHelper.quoteValue(projectName);
      String sql =
                  "delete from " + resolvedDevicePoliciesTableName
                  + " where "
                  + project + " = " + quotedProjectName
                  + " and " + policy + " = " + quotedPolicyname;

      if(logger.isDebugEnabled()){
          logger.debug (sql);
      }

       // Temporarily cast the repository connection to a JDBC Connection
       // in order to get the java.sql.Connection out.
      Connection sqlConnection = ((JDBCRepositoryConnection) connection).
                                    getConnection ();

      stmt = sqlConnection.createStatement();

      stmt.executeUpdate(sql);
      close(stmt);

        // remove from VMPOLICY_LANG
        // Resolve table name.
        final String vmPolicyLangTable =
            resolveTableName(jdbcConnection, VMPOLICY_LANG);

        sql = "delete from " + vmPolicyLangTable
            + " where "
            + project + " = " + quotedProjectName
            + " and " + policy + " = " + quotedPolicyname;

        if(logger.isDebugEnabled()){
            logger.debug (sql);
        }

        stmt = sqlConnection.createStatement();

        stmt.executeUpdate(sql);
        close(stmt);

        // remove from VMPOLICY_TYPE
        // Resolve table name.
        final String vmPolicyTypeTable =
            resolveTableName(jdbcConnection, VMPOLICY_TYPE);

        sql = "delete from " + vmPolicyTypeTable
            + " where "
            + project + " = " + quotedProjectName
            + " and " + policy + " = " + quotedPolicyname;

        if(logger.isDebugEnabled()){
          logger.debug (sql);
        }

        stmt = sqlConnection.createStatement();

        stmt.executeUpdate(sql);
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException(sqle);
    }
    finally {
        close(stmt);
    }
  }

  /**
   * Update all policy columns that match oldPolicyName with newPolicyName
   * in the VMPolicy_values table.
   *
   * @param connection The connection to use to access the repository
   * @param oldPolicyName The old policy name
   * @param newPolicyName The new policy name
   * @exception RepositoryException if an error occurs
   */
  public void updatePolicyName(RepositoryConnection connection,
                               String oldPolicyName, String newPolicyName)
    throws RepositoryException {

    // Temporarily cast the repository connection to a JDBC Connection in
    // order to get the java.sql.Connection out.
    Connection sqlConnection = ((JDBCRepositoryConnection) connection).
                                    getConnection ();


    JDBCRepositoryConnection jdbcConnection
        = (JDBCRepositoryConnection) connection;

    String projectName = getProjectName();

    Statement stmt = null;
    try {
      stmt = sqlConnection.createStatement();

      // resolve field names
      String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
      String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);

      String sql =
          "update " + resolvedDevicePoliciesTableName
          + " set " + policy + " = " + JDBCAccessorHelper.quoteValue(newPolicyName)
          + " where "
          + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
          + " and " + policy + "  = " + JDBCAccessorHelper.quoteValue(oldPolicyName);

      if(logger.isDebugEnabled()){
          logger.debug (sql);
      }
      stmt.executeUpdate (sql);
    }
    catch (SQLException sqle) {
      logger.error("sql-exception", sqle);
      throw new JDBCRepositoryException (sqle);
    }
    finally {
        close(stmt);
    }

      // update VMPOLICY_LANG
      // Resolve table name.
      String vmPolicyLangTable = resolveTableName(jdbcConnection, VMPOLICY_LANG);
      try {
          stmt = sqlConnection.createStatement();

          // resolve field names
          String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
          String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);

          String sql =
              "update " + vmPolicyLangTable
              + " set " + policy + " = " + JDBCAccessorHelper.quoteValue(newPolicyName)
              + " where "
              + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
              + " and " + policy + "  = " + JDBCAccessorHelper.quoteValue(oldPolicyName);

          if(logger.isDebugEnabled()){
              logger.debug (sql);
          }
          stmt.executeUpdate (sql);
      }
      catch (SQLException sqle) {
        logger.error("sql-exception", sqle);
        throw new JDBCRepositoryException (sqle);
      }
      finally {
          close(stmt);
      }

      // update VMPOLICY_TYPE
      // Resolve table name.
      String vmPolicyTypeTable = resolveTableName(jdbcConnection, VMPOLICY_TYPE);
      try {
          stmt = sqlConnection.createStatement();

          // resolve field names
          String project = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
          String policy = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);

          String sql =
              "update " + vmPolicyTypeTable
              + " set " + policy + " = " + JDBCAccessorHelper.quoteValue(newPolicyName)
              + " where "
              + project + " = " + JDBCAccessorHelper.quoteValue(projectName)
              + " and " + policy + "  = " + JDBCAccessorHelper.quoteValue(oldPolicyName);

          if(logger.isDebugEnabled()){
              logger.debug (sql);
          }
          stmt.executeUpdate (sql);
      }
      catch (SQLException sqle) {
        logger.error("sql-exception", sqle);
        throw new JDBCRepositoryException (sqle);
      }
      finally {
          close(stmt);
      }
  }


    /**
     * Return the project name for use by the device accessor.
     * <p>
     * In this case the devices are stored in their own project, which is
     * (potentially) separate to the main project used for all the policies.
     *
     * @return the project name.
     */
    private String getProjectName() {

        String name = location.getDeviceRepositoryName();
        if (name == null) {
            throw new IllegalStateException("JDBC Standard Device Project " +
                    "Name not set");
        }
        return name;
    }

    // ========================================================================
    //   Policy And Policy Descriptor Methods
    // ========================================================================

    // ========================================================================
    //   POLICY DESCRIPTOR READ SUPPORT
    //   NOTE: this was coded separately to the write support due to severe
    //   time constraints. As such there probably refactoring opportunities to
    //   be had by factoring out common code between read and write.
    // ========================================================================

    // javadoc inherited.
    public RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection) throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        // Get the java.sql.Connection out of the JDBC Connection.
        Connection sqlConnection = jdbcConnection.getConnection();

        // resolve field names
        String policyField = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);

        // resolve table name
        String vmPolicyTypeTable = resolveTableName(jdbcConnection, VMPOLICY_TYPE);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String projectName = getProjectName();
            String sql
                    = "select " + policyField + " from " + vmPolicyTypeTable +
                    " where " + projectField + " = ?";

            pstmt = sqlConnection.prepareStatement(sql);
            JDBCAccessorHelper.setStringValue(pstmt, 1, projectName);

            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            rs = pstmt.executeQuery();

            return new StringEnumeration(rs);

        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            close(pstmt);
            throw new JDBCRepositoryException(sqle);
        }
    }

    // javadoc inherited.
    public RepositoryEnumeration enumeratePolicyNames(
            RepositoryConnection connection, String categoryName)
            throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        // Get the java.sql.Connection out of the JDBC Connection.
        Connection sqlConnection = jdbcConnection.getConnection();

        // resolve field names
        String policyField = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        String categoryNameField = resolveFieldName(jdbcConnection, CATEGORY_NAME_COLUMN_NAMES);
        String categoryIDField = resolveFieldName(jdbcConnection, CATEGORY_ID_COLUMN_NAMES);
        String languageField = resolveFieldName(jdbcConnection, LANGUAGE_COLUMN_NAMES);

        // resolve table names
        String vmPolicyTypeTable =
            resolveTableName(jdbcConnection, VMPOLICY_TYPE);
        String vmPolicyCategoryTable =
            resolveTableName(jdbcConnection, VMPOLICY_CATEGORY);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String projectName = getProjectName();
            String sql = "select "
                    + vmPolicyTypeTable + "." + policyField
                    + " from "
                    + vmPolicyTypeTable + " , "
                    + vmPolicyCategoryTable
                    + " where ( "
                    + vmPolicyTypeTable + "." + projectField + " = ? "
                    + " and "
                    + vmPolicyCategoryTable + "." + categoryNameField + " = ? "
                    + " ) "
                    + " and "
                    + vmPolicyTypeTable + "." + categoryIDField + " = "
                    + vmPolicyCategoryTable + "." + categoryIDField
                    + " and "
                    + vmPolicyCategoryTable + "." + languageField + " = ? ";



            pstmt = sqlConnection.prepareStatement(sql);
            JDBCAccessorHelper.setStringValue(pstmt, 1, projectName);
            JDBCAccessorHelper.setStringValue(pstmt, 2, categoryName);
            JDBCAccessorHelper.setStringValue(pstmt, 3, DEFAULT_LANGAUGE);

            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            rs = pstmt.executeQuery();

            return new StringEnumeration(rs);

        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            close(pstmt);
            throw new JDBCRepositoryException(sqle);
        }
    }

    // javadoc inherited.
    public PolicyDescriptor retrievePolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            Locale locale)
            throws RepositoryException {

        DefaultPolicyDescriptor descriptor = null;

         // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        // Cast the repository connection to a JDBC Connection in
        // order to get the java.sql.Connection out.
        Connection sqlConnection = ((JDBCRepositoryConnection) connection).
                getConnection();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // resolve field names
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        String policyDescNameField = resolveFieldName(jdbcConnection, POLICY_DESC_NAMES);
        String policyHelpField = resolveFieldName(jdbcConnection, POLICY_HELP_COLUMN_NAMES);
        String policyField = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);
        String languageField = resolveFieldName(jdbcConnection, LANGUAGE_COLUMN_NAMES);
        String typeIDField = resolveFieldName(jdbcConnection, TYPE_ID_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);


        // resolve table names
        String vmTypesTable = resolveTableName(jdbcConnection, VMTYPES);
        String vmPolicyTypeTable = resolveTableName(jdbcConnection, VMPOLICY_TYPE);
        String vmPolicyLangTable = resolveTableName(jdbcConnection, VMPOLICY_LANG);

        try {
            String projectName = getProjectName();
            // Do a join over VMTYPES and VMPOLICY_TYPE to get the type for
            // the given policy name and project.
            String sql =
                    "select "
                    + typeIDField + " , "
                    + vmTypesTable + "." + typeInstanceIDField +
                    " from "
                    + vmTypesTable + " , "
                    + vmPolicyTypeTable +
                    " where "
                    + vmTypesTable + "." + typeInstanceIDField
                    + " = "
                    + vmPolicyTypeTable + "." + typeInstanceIDField +
                    " and "
                    + vmPolicyTypeTable + "." + projectField + " = ? " +
                    " and "
                    + vmTypesTable + "." + projectField + " = ? "
                    + " and " + vmPolicyTypeTable + "." + policyField + " = ?";

            pstmt = sqlConnection.prepareStatement(sql);
            JDBCAccessorHelper.setStringValue(pstmt, 1, projectName);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);
            JDBCAccessorHelper.setStringValue(pstmt, 3, policyName);

            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }

            rs = pstmt.executeQuery();

            // There should only be zero or one match.
            if (rs.next()) {

                int type = rs.getInt(1);
                int typeID = rs.getInt(2);
                // Close the result set.
                rs.close();

                PolicyType policyType = getPolicyType(
                        jdbcConnection, projectName, type, typeID);

                descriptor = new DefaultPolicyDescriptor();
                descriptor.setPolicyType(policyType);
            }
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        if (descriptor != null) {
            Statement stmt = null;
            try {
                stmt = sqlConnection.createStatement();
                final String sql =
                    "select " + languageField + " , " + policyDescNameField + " , " +
                        policyHelpField +
                    " from " + vmPolicyLangTable +
                    " where " +
                    policyField + " = " +
                        JDBCAccessorHelper.quoteValue(policyName) +
                    " and " +
                    projectField + " = " +
                        JDBCAccessorHelper.quoteValue(getProjectName());

                if (logger.isDebugEnabled()){
                    logger.debug(sql);
                }

                rs = stmt.executeQuery(sql);
                final String[] values = getBestMatchingValues(jdbcConnection,
                    rs, new String[]{policyDescNameField, policyHelpField}, locale);
                descriptor.setPolicyDescriptiveName(values[0]);
                descriptor.setPolicyHelp(values[1]);
                descriptor.setLanguage(values[2]);
                rs.close();
            }
            catch (SQLException sqle) {
                logger.error("sql-exception", sqle);
                throw new JDBCRepositoryException(sqle);
            }
            finally {
                close(stmt);
            }
        }
        return descriptor;
    }

    // javadoc inherited
    public List retrievePolicyDescriptors(final RepositoryConnection connection,
                                          final String policyName)
            throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    // javadoc inherited
    public List retrieveCategoryDescriptors(final RepositoryConnection connection,
                                            final String categoryName)
            throws RepositoryException {

        throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                "unsupported-operation"));
    }

    /**
     * Get the policy type using the connection, project name and type instance
     * id.
     *
     * @param connection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeID     the type ID of this policy
     * @param typeInstanceID
     *                   the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception condition
     *                                 is encountered.
     * @todo better this switch statement isn't great but cannot be avoided due to time constraints.
     */
    private PolicyType getPolicyType(JDBCRepositoryConnection connection,
                                     String projectName,
                                     int typeID,
                                     int typeInstanceID)
            throws RepositoryException {

        PolicyType policyType = null;
        switch (typeID) {
            case BOOLEAN_TYPE_ID:
                policyType = new DefaultBooleanPolicyType();
                break;

            case INTEGER_TYPE_ID:
                policyType = new DefaultIntPolicyType();
                break;

            case TEXT_TYPE_ID:
                policyType = new DefaultTextPolicyType();
                break;

            case ORDERED_SET_TYPE_ID:
                policyType = getOrderedSetPolicyType(connection,
                        projectName, typeInstanceID);
                break;

            case UNORDERED_SET_TYPE_ID:
                policyType = getUnOrderedSetPolicyType(connection,
                        projectName, typeInstanceID);
                break;

            case RANGE_TYPE_ID:
                policyType = getRangesPolicyType(connection, projectName,
                        typeInstanceID);
                break;

            case STRUCTURE_TYPE_ID:
                policyType = getStructurePolicyType(connection,
                        projectName, typeInstanceID);
                break;

            case SELECTION_TYPE_ID:
                policyType = getSelectionPolicyType(connection,
                        projectName, typeInstanceID);
                break;
            default:
                throw new IllegalStateException("Unknown type: " + typeID);
        }
        return policyType;
    }

    /**
     * Get the structure policy type using the connection, project name and
     * type instance id.
     *
     * @param jdbcConnection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeInstanceID
     *                   the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception condition
     *                                 is encountered.
     */
    private PolicyType getStructurePolicyType(
            JDBCRepositoryConnection jdbcConnection, String projectName,
            int typeInstanceID) throws RepositoryException {

        DefaultStructurePolicyType policyType = null;

        Connection connection = jdbcConnection.getConnection();

        // resolve field names
        String fieldNameField = resolveFieldName(jdbcConnection, FIELD_NAME_COLUMN_NAMES);
        String fieldInstanceIDField = resolveFieldName(jdbcConnection, FIELD_INSTANCE_ID_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);

        // resolve table name
        String vmTypesStructurTable = resolveTableName(jdbcConnection, VMTYPES_STRUCTURE);

        String sql = "select "
                + fieldNameField + " , "
                + fieldInstanceIDField
                + " from "
                + vmTypesStructurTable
                + " where "
                + typeInstanceIDField + " = ? "
                + " and "
                + projectField + " = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, typeInstanceID);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                policyType = new DefaultStructurePolicyType();
                do {
                    String fieldName = rs.getString(1);
                    int fieldTypeID = rs.getInt(2);
                    policyType.addFieldType(fieldName, getReferencedType(
                            jdbcConnection, projectName, fieldTypeID));
                } while (rs.next());

                // We have obtained all the data so mark it as complete.
                policyType.complete();
            }
            // Close the result set.
            rs.close();

        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        return policyType;
    }

    /**
     * Get the referenced simple policy type using the connection, project
     * name and type instance id.
     *
     * @param jdbcConnection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeInstanceID     the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception
     *                                 condition is encountered.
     */
    private PolicyType getReferencedType(
            JDBCRepositoryConnection jdbcConnection, String projectName,
            int typeInstanceID) throws RepositoryException {

        PolicyType policyType = null;

        Connection connection = jdbcConnection.getConnection();

        // Resolve field names.
        String typeIDField = resolveFieldName(jdbcConnection, TYPE_ID_COLUMN_NAMES);
        String typeIDInstanceField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesTable = resolveTableName(jdbcConnection, VMTYPES);


        String sql = "select " + typeIDField +
                " from " + vmTypesTable +
                " where " + typeIDInstanceField + " = ?" +
                " and " + projectField + " = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, typeInstanceID);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                int type = rs.getInt(1);
                // Close the result set.
                rs.close();

                switch (type) {
                    case BOOLEAN_TYPE_ID:
                    case INTEGER_TYPE_ID:
                    case RANGE_TYPE_ID:
                    case SELECTION_TYPE_ID:
                    case TEXT_TYPE_ID:
                        // Simple types.
                        // this is OK, fall through and keep going.
                        break;

                    case ORDERED_SET_TYPE_ID:
                    case UNORDERED_SET_TYPE_ID:
                    case STRUCTURE_TYPE_ID:
                        // Compound types.
                        // These may not be referenced by other compound types.
                        throw new IllegalStateException("Illegal referenced " +
                                "type: " + type);

                    default:
                        throw new IllegalStateException("Unknown type: " + type);
                }

                policyType = getPolicyType(jdbcConnection, projectName, type,
                        typeInstanceID);
            }
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        return policyType;
    }

    /**
     * Get the unordered set policy type using the connection, project
     * name and type instance id.
     *
     * @param jdbcConnection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeInstanceID     the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception
     *                                 condition is encountered.
     */
    private PolicyType getUnOrderedSetPolicyType(
            JDBCRepositoryConnection jdbcConnection, String projectName,
            int typeInstanceID) throws RepositoryException {

        PolicyType policyType = null;

        Connection connection = jdbcConnection.getConnection();

        // Resolve field names.
        String memberInstanceIDField = resolveFieldName(jdbcConnection,
                MEMBER_INSTANCE_ID_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        String orderedField = resolveFieldName(jdbcConnection, ORDERED_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesSetTable = resolveTableName(jdbcConnection, VMTYPES_SET);

        String sql = "select " + memberInstanceIDField
                + " from " + vmTypesSetTable
                + " where " + typeInstanceIDField + " = ?"
                + " and "
                + projectField + " = ?"
                + " and " + orderedField + " = 0";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, typeInstanceID);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                int memberTypeID = rs.getInt(1);
                // Close the result set.
                rs.close();

                policyType = new DefaultUnorderedSetPolicyType(
                        getReferencedType(jdbcConnection, projectName,
                                memberTypeID));
            }
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        return policyType;
    }

    /**
     * Get the ordered set policy type using the connection, project
     * name and type instance id.
     *
     * @param jdbcConnection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeInstanceID     the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception
     *                                 condition is encountered.
     */
    private PolicyType getOrderedSetPolicyType(
            JDBCRepositoryConnection jdbcConnection, String projectName,
            int typeInstanceID) throws RepositoryException {

        PolicyType policyType = null;

        Connection connection = jdbcConnection.getConnection();

        // Resolve field names.
        String memberInstanceIDField = resolveFieldName(jdbcConnection,
                MEMBER_INSTANCE_ID_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        String orderedField = resolveFieldName(jdbcConnection, ORDERED_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesSetTable = resolveTableName(jdbcConnection, VMTYPES_SET);

        String sql = "select "
                + memberInstanceIDField
                + " from " + vmTypesSetTable
                + " where "
                + typeInstanceIDField + " = ?"
                + " and "
                + projectField + " = ?"
                + " and "
                + orderedField + " = 1";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, typeInstanceID);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                int memberTypeID = rs.getInt(1);
                // Close the result set.
                rs.close();

                policyType = new DefaultOrderedSetPolicyType(
                        getReferencedType(jdbcConnection, projectName,
                                memberTypeID));
            }
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        return policyType;
    }

    /**
     * Get the ranges simple policy type using the connection, project
     * name and type instance id.
     *
     * @param jdbcConnection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeInstanceID     the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception
     *                                 condition is encountered.
     */
    private PolicyType getRangesPolicyType(
            JDBCRepositoryConnection jdbcConnection, String projectName,
            int typeInstanceID) throws RepositoryException {

        PolicyType policyType = null;

        Connection connection = jdbcConnection.getConnection();

        // Resolve field names.
        String minValueField = resolveFieldName(jdbcConnection,
                MIN_VALUE_COLUMN_NAMES);
        String maxValueField = resolveFieldName(jdbcConnection,
                MAX_VALUE_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesRangeTable = resolveTableName(jdbcConnection, VMTYPES_RANGE);

        String sql = "select "
                + minValueField + " , "
                + maxValueField
                + " from "
                + vmTypesRangeTable
                + " where "
                + typeInstanceIDField + " = ?"
                + " and "
                + projectField + " = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, typeInstanceID);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);

            rs = pstmt.executeQuery();
            if (rs.next()) {

                int min = rs.getInt(1);
                int max = rs.getInt(2);
                // Close the result set.
                rs.close();

                policyType = new DefaultRangePolicyType(min, max);
            }
        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        return policyType;
    }

    /**
     * Get the selection policy type using the connection, project
     * name and type instance id.
     *
     * @param jdbcConnection the connection to the repository.
     * @param projectName
     *                   the name of the project.
     * @param typeInstanceID     the type instance ID.
     * @return the PolicyType, or null if not found.
     * @throws JDBCRepositoryException if an JDBCRepository exception
     *                                 condition is encountered.
     */
    private PolicyType getSelectionPolicyType(
            JDBCRepositoryConnection jdbcConnection, String projectName,
            int typeInstanceID) throws RepositoryException {

        DefaultSelectionPolicyType policyType = null;

        Connection connection = jdbcConnection.getConnection();

        // Resolve field names.
        String keywordField = resolveFieldName(jdbcConnection,
                KEYWORD_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(jdbcConnection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesSelectionTable = resolveTableName(jdbcConnection, VMTYPES_SELECTION);

        String sql = "select "
                + keywordField
                + " from "
                + vmTypesSelectionTable
                + " where "
                + typeInstanceIDField + " = ?"
                + " and "
                + projectField + " = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, typeInstanceID);
            JDBCAccessorHelper.setStringValue(pstmt, 2, projectName);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                policyType = new DefaultSelectionPolicyType();
                do {
                    policyType.addKeyword(rs.getString(1));
                } while (rs.next());

                // We have obtained all the data so mark it as complete.
                policyType.complete();
            }
            // Close the result set.
            rs.close();

        } catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            throw new JDBCRepositoryException(sqle);
        } finally {
            close(pstmt);
        }
        return policyType;
    }

    public CategoryDescriptor retrieveCategoryDescriptor(
                final RepositoryConnection connection,
                final String categoryName,
                final Locale locale) throws RepositoryException {

        DefaultCategoryDescriptor descriptor = null;

        // at first we have to check if the category name is valid

        // Cast the repository connection to a JDBC Connection.
        final JDBCRepositoryConnection jdbcConnection
               = (JDBCRepositoryConnection) connection;

        // Resolve field names.
        final String categoryNameField =
            resolveFieldName(jdbcConnection, CATEGORY_NAME_COLUMN_NAMES);
        final String projectField =
            resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        final String categoryIDField =
            resolveFieldName(jdbcConnection, CATEGORY_ID_COLUMN_NAMES);
        final String languageIDField =
            resolveFieldName(jdbcConnection, LANGUAGE_COLUMN_NAMES);

        // Resolve table name.
        final String vmPolicyCategoryTable =
            resolveTableName(jdbcConnection, VMPOLICY_CATEGORY);

        // Get the java.sql.Connection out of the JDBC Connection.
        final Connection sqlConnection = jdbcConnection.getConnection();

        Statement stmt = null;
        String categoryId = null;
        try {
            stmt = sqlConnection.createStatement();

            final String sql =
                "select " + categoryNameField + " , " + categoryIDField +
                " from " + vmPolicyCategoryTable + " where " +
                projectField + " = " +
                JDBCAccessorHelper.quoteValue(getProjectName()) + " and " +
                categoryNameField + " = " +
                JDBCAccessorHelper.quoteValue(categoryName) +
                " and " + languageIDField + " = " +
                    JDBCAccessorHelper.quoteValue(DEFAULT_LANGAUGE);

            if (logger.isDebugEnabled()){
                logger.debug(sql);
            }

            final ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                categoryId = rs.getString(categoryIDField);
                descriptor = new DefaultCategoryDescriptor();
            }
        }
        catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }
            catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
            throw new JDBCRepositoryException(sqle);
        }
        if (descriptor != null) {
            final String languageField =
                resolveFieldName(jdbcConnection, LANGUAGE_COLUMN_NAMES);
            // find the best mathing descriptor
            stmt = null;
            try {
                stmt = sqlConnection.createStatement();
                final String sql =
                    "select " + categoryNameField + " , " + languageField +
                    " from " + vmPolicyCategoryTable + " where " +
                    categoryIDField + " = " + categoryId + " and " +
                    projectField + " = " +
                    JDBCAccessorHelper.quoteValue(getProjectName());

                if (logger.isDebugEnabled()){
                    logger.debug(sql);
                }

                final ResultSet rs = stmt.executeQuery(sql);
                final String[] values = getBestMatchingValues(jdbcConnection,
                    rs, new String[]{categoryNameField}, locale);
                descriptor.setCategoryDescriptiveName(values[0]);
                descriptor.setLanguage(values[1]);
            }
            catch (SQLException sqle) {
                logger.error("sql-exception", sqle);
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
                catch (SQLException e) {
                    logger.error("sql-exception", e);
                    throw new JDBCRepositoryException(e);
                }
                throw new JDBCRepositoryException(sqle);
            }
        }
        return descriptor;
    }

    /**
     * Iterates through the result set and select the row that best matches the
     * specified locale.
     *
     * <p>If the best matching row is found, it reads the values from the given
     * columns as strings and store them in the result array. Additionally to
     * the values the result array contains the best matching language.</p>
     *
     * <p>If no matching row is found it returns the default values (values that
     * are assinged to the null language.</p>
     *
     * <p>If no default values are found and array with nulls is returned.</p>
     *
     * <p>Never returns null. The length of the returned array is always equals
     * to the length of the columns array + 1 (because of the added language
     * element).</p>
     *
     * @param connection the connection to use
     * @param rs the result set to read from
     * @param columns the columns to save
     * @param locale the locale
     * @return the array of values for the best matching language.
     * @throws SQLException
     */
    private String[] getBestMatchingValues(
                final JDBCRepositoryConnection connection, final ResultSet rs,
                final String[] columns, final Locale locale)
            throws SQLException {

        String[] result = new String[columns.length + 1];
        final String lang = locale.getLanguage() + "_";
        final String langAndCountry = lang + locale.getCountry() + "_";
        final String[] levels = new String[]{
            DEFAULT_LANGAUGE, "__", lang + "_", langAndCountry,
            langAndCountry + locale.getVariant()};
        int maxLevel = -1;
        final String langField = resolveFieldName(connection, LANGUAGE_COLUMN_NAMES);
        while (rs.next()) {
            final String language = rs.getString(langField);
            int level = -1;
            for (int i = maxLevel + 1; i < levels.length && level == -1; i++) {
                if (levels[i].equals(language)) {
                    level = i;
                }
            }
            if (level != -1) {
                maxLevel = level;
                // copy the values
                for (int i = 0; i < columns.length; i++) {
                    result[i] = rs.getString(columns[i]);
                }
                result[columns.length] = DEFAULT_LANGAUGE.equals(language) ?
                        null: language;
            }
        }
        return result;
    }

    // javadoc inherited
    public RepositoryEnumeration enumerateCategoryNames(
                final RepositoryConnection connection)
            throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        final JDBCRepositoryConnection jdbcConnection
               = (JDBCRepositoryConnection) connection;

        // Resolve field name.
        final String projectField =
            resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        final String categoryNameField =
            resolveFieldName(jdbcConnection, CATEGORY_NAME_COLUMN_NAMES);
        final String languageField =
            resolveFieldName(jdbcConnection, LANGUAGE_COLUMN_NAMES);

        // Resolve table name.
        final String vmPolicyCategoryTable =
            resolveTableName(jdbcConnection, VMPOLICY_CATEGORY);

        // Get the java.sql.Connection out of the JDBC Connection.
        final Connection sqlConnection = jdbcConnection.getConnection();

        Statement stmt = null;
        try {
            stmt = sqlConnection.createStatement();

            final String sql = "select distinct " + categoryNameField +
                " from " + vmPolicyCategoryTable + " where " +
                projectField + " = " +
                    JDBCAccessorHelper.quoteValue(getProjectName()) +
                " and " + languageField + " = " +
                    JDBCAccessorHelper.quoteValue(DEFAULT_LANGAUGE);

            if (logger.isDebugEnabled()){
                logger.debug(sql);
            }

            final ResultSet rs = stmt.executeQuery(sql);
            return new StringEnumeration(rs);
        }
        catch (SQLException sqle) {
            logger.error("sql-exception", sqle);
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }
            catch (SQLException e) {
                logger.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
            throw new JDBCRepositoryException(sqle);
        }
    }

    /**
     * Close the statement and log an exception if the close itself resulted
     * in an exception.
     * @param stmt the statement to close.
     * @throws JDBCRepositoryException
     */
    private void close(Statement stmt) throws JDBCRepositoryException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.error("sql-exception", e);
            throw new JDBCRepositoryException(e);
        }
    }

    // ========================================================================
    //   POLICY DESCRIPTOR WRITE SUPPORT
    //   NOTE: this was coded separately to the read support due to severe
    //   time constraints. As such there probably refactoring opportunities to
    //   be had by factoring out common code between read and write.
    // ========================================================================

    /**
     * Utility class to do low level JDBC handling for us.
     */
    private static final JDBCTemplate template = new JDBCTemplate();

    /**
     * Sequence counter for category ids.
     * <p>
     * NOTE: sequence counts are currently held only in memory. This means that
     * users (i.e. the import tool) of these write methods must write in one
     * "batch" and delete every existing entry before attempting to write again
     * in a new "batch".
     */
    private int categoryIdCounter;

    /**
     * Sequence counter for type instance ids.
     * <p>
     * NOTE: sequence counts are currently held only in memory. This means that
     * users (i.e. the import tool) of these write methods must write in one
     * "batch" and delete every existing entry before attempting to write again
     * in a new "batch".
     */
    private int typeInstanceIdCounter;

    /**
     * Returns the next synthetic category ID to use.
     *
     * @return the next category id.
     * @see #categoryIdCounter
     */
    private int getNextCategoryId() {
        return categoryIdCounter++;
    }

    /**
     * Returns the next synthetic type instance ID to use.
     *
     * @see #typeInstanceIdCounter
     * @return the next synthetic type instance ID.
     */
    private int getNextTypeInstanceId() {
        return typeInstanceIdCounter++;
    }

    // Javadoc inherited.
    public void addPolicyDescriptor(
            RepositoryConnection connection,
            String policyName,
            PolicyDescriptor descriptor)
            throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        // First, see if we have a Category we can use.
        String categoryName = descriptor.getCategoryName();
        Integer categoryId = selectPolicyCategoryId(jdbcConnection,
                categoryName);
        if (categoryId == null) {
            categoryId = new Integer(getNextCategoryId());
            insertPolicyCategory(jdbcConnection, categoryId.intValue(),
                    categoryName);
        }

        insertPolicyDescriptor(jdbcConnection, policyName, descriptor);

        if (selectPolicyType(jdbcConnection, policyName) == null) {
            int typeInstanceId = getNextTypeInstanceId();

            // Insert a row into VMPOLICY_TYPE for the link between policy and
            // policy type.
            insertPolicyType(jdbcConnection, policyName, categoryId.intValue(),
                    typeInstanceId);

            final PolicyType policyType = descriptor.getPolicyType();

            addPolicyTypeInstance(jdbcConnection, typeInstanceId,
                    policyType);
        }
    }

    // Javadoc inherited.
    public void addCategoryDescriptor(
            RepositoryConnection connection,
            String categoryName,
            CategoryDescriptor descriptor)
            throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        Integer categoryId =
            selectPolicyCategoryId(jdbcConnection, categoryName);
        if (categoryId == null) {
            categoryId = new Integer(getNextCategoryId());
            insertPolicyCategory(jdbcConnection, categoryId.intValue(),
                    categoryName);
        }

        insertCategoryDescriptor(
            jdbcConnection, categoryId.intValue(), descriptor);
    }

    /**
     * Add a policy type into the repostitory with the given instance id.
     * <p>
     * As a minimum, this will add a row into the VMTYPES table. It may also
     * add a row or rows into one of the VMTYPES_XXX tables for complex types.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the synthetic instance of of the policy type.
     * @param policyType the policy type data.
     * @throws RepositoryException if there was a problem.
     */
    private void addPolicyTypeInstance(
            JDBCRepositoryConnection connection,
            int typeInstanceId, final PolicyType policyType)
            throws RepositoryException {

        // Calculate the numeric id of this policy type.
        int policyTypeId = getPolicyTypeId(policyType);

        // Insert a row into VMTYPES for the simple type definition.
        insertTypeInstance(connection, typeInstanceId,
                policyTypeId);

        // Insert a row (or rows) into the related VM_TYPES_XXX tables for
        // the detail of the type instance, if necessary.
        if (policyType instanceof SetPolicyType) {
            SetPolicyType set = (SetPolicyType) policyType;
            addTypeInstanceSet(connection, typeInstanceId, set);
        } else if (policyType instanceof StructurePolicyType) {
            StructurePolicyType structure = (StructurePolicyType) policyType;
            addTypeInstanceStructure(connection, typeInstanceId,
                    structure);
        } else if (policyType instanceof RangePolicyType) {
            RangePolicyType range = (RangePolicyType) policyType;
            insertTypeInstanceRange(connection, typeInstanceId,
                    range.getMinInclusive(), range.getMaxInclusive());
        } else if (policyType instanceof SelectionPolicyType) {
            SelectionPolicyType selection = (SelectionPolicyType) policyType;
            insertTypeInstanceSelection(connection, typeInstanceId,
                    selection.getKeywords());
        }
    }

    /**
     * Returns the policy type id for the policy type supplied.
     *
     * @param policyType the policy type.
     * @return the policy type id for the policy type.
     */
    private int getPolicyTypeId(PolicyType policyType) {
        if (policyType instanceof UnorderedSetPolicyType) {
            return UNORDERED_SET_TYPE_ID;
        } else if (policyType instanceof OrderedSetPolicyType) {
            return ORDERED_SET_TYPE_ID;
        } else if (policyType instanceof StructurePolicyType) {
            return STRUCTURE_TYPE_ID;
        } else if (policyType instanceof BooleanPolicyType) {
            return BOOLEAN_TYPE_ID;
        } else if (policyType instanceof IntPolicyType) {
            return INTEGER_TYPE_ID;
        } else if (policyType instanceof RangePolicyType) {
            return RANGE_TYPE_ID;
        } else if (policyType instanceof SelectionPolicyType) {
            return SELECTION_TYPE_ID;
        } else if (policyType instanceof TextPolicyType) {
            return TEXT_TYPE_ID;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Select a category id from the VMPOLICY_CATEGORY table, by category name.
     *
     * @param connection the database connect to add data to.
     * @param categoryName the category name to query an id for.
     * @return the category id, or null if one was not found.
     * @throws RepositoryException if there was a problem.
     */
    private Integer selectPolicyCategoryId(JDBCRepositoryConnection connection,
                                           String categoryName) throws RepositoryException {

        // Resolve field names.
        String categoryIDField = resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);
        String categoryNameField = resolveFieldName(connection, CATEGORY_NAME_COLUMN_NAMES);
        String languageField = resolveFieldName(connection, LANGUAGE_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyCategoryTable =
            resolveTableName(connection, VMPOLICY_CATEGORY);

        String projectName = getProjectName();
        Integer categoryId = template.queryForInteger(connection,
                "select "
                + categoryIDField
                + " from "
                + vmPolicyCategoryTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + categoryNameField
                + " = " + JDBCAccessorHelper.quoteValue(categoryName)
                + " and "
                + languageField + " = " +
                    JDBCAccessorHelper.quoteValue(DEFAULT_LANGAUGE));

        return categoryId;
    }

    /**
     * Insert a policy descriptor into the VMPOLICY_LANG table.
     *
     * @param connection the database connect to add data to.
     * @param policyName the name of the policy to insert.
     * @param descriptor the policy descriptor to insert
     * @throws RepositoryException if there was a problem.
     */
    private void insertPolicyDescriptor(
            final JDBCRepositoryConnection connection,
            final String policyName,
            final PolicyDescriptor descriptor )
            throws RepositoryException {

        if (descriptor != null &&
            (descriptor.getPolicyDescriptiveName() != null ||
             descriptor.getPolicyHelp() != null ||
             descriptor.getLanguage() != null)) {

            // Resolve table name.
            final String vmPolicyLangTable =
                resolveTableName(connection, VMPOLICY_LANG);

            // Resolve field names.
            final String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
            final String policyField = resolveFieldName(connection, POLICY_COLUMN_NAMES);
            final String policyDescNameField =
                resolveFieldName(connection, POLICY_DESC_NAMES);
            final String policyHelpField =
                resolveFieldName(connection, POLICY_HELP_COLUMN_NAMES);
            final String languageField =
                resolveFieldName(connection, LANGUAGE_COLUMN_NAMES);
            template.update(connection,
                "insert into "
                + vmPolicyLangTable
                + " ( "
                + projectField + " , "
                + policyField + " , "
                + policyDescNameField + " , "
                + policyHelpField + " , "
                + languageField
                + " ) "
                + " values ( ? , ? , ? , ? , ? )",

                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        final String projectName = getProjectName();
                        final String descriptiveName =
                            descriptor.getPolicyDescriptiveName();
                        final String help = descriptor.getPolicyHelp();
                        final String language = descriptor.getLanguage();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                + VMPOLICY_LANG + ","
                                + " PROJECT: " + projectName
                                + " POLICY: " + policyName
                                + " POLICY_DESC_NAME: " + descriptiveName
                                + " POLICY_HELP: " + help
                                + " LANGUAGE: " + language);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        JDBCAccessorHelper.setStringValue(ps, 2, policyName);
                        JDBCAccessorHelper.setStringValue(
                            ps, 3, descriptiveName);
                        JDBCAccessorHelper.setStringValue(ps, 4, help);
                        JDBCAccessorHelper.setStringValue(ps, 5, language);
                    }
                });
        }
    }

    /**
     * Insert a category descriptor into the VMPOLICY_CATEGORY table.
     *
     * @param connection the database connect to add data to.
     * @param categoryId the id of the category to insert.
     * @param descriptor the category descriptor to insert
     * @throws RepositoryException if there was a problem.
     */
    private void insertCategoryDescriptor(
            final JDBCRepositoryConnection connection,
            final int categoryId,
            final CategoryDescriptor descriptor )
            throws RepositoryException {

        if (descriptor != null && descriptor.getLanguage() != null) {

            // Resolve table name.
            final String vmPolicyCategoryTable =
                resolveTableName(connection, VMPOLICY_CATEGORY);

            // Resolve field names.
            final String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
            final String categoryIDField =
                resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);
            final String categoryDescNameField =
                resolveFieldName(connection, CATEGORY_NAME_COLUMN_NAMES);
            final String languageField =
                resolveFieldName(connection, LANGUAGE_COLUMN_NAMES);
            template.update(connection,
                "insert into "
                + vmPolicyCategoryTable
                + " ( "
                + projectField + " , " + categoryIDField + " , "
                + categoryDescNameField + " , " + languageField
                + " ) "
                + " values ( ? , ? , ? , ? )",

                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        final String descriptiveName =
                            descriptor.getCategoryDescriptiveName();
                        final String language =
                            descriptor.getLanguage();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                + VMPOLICY_LANG + ","
                                + " PROJECT: " + projectName
                                + " CATEGORY_ID: " + categoryId
                                + " CATEGORY _NAME: " + descriptiveName
                                + " LANGUAGE: " + language);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, categoryId);
                        JDBCAccessorHelper.setStringValue(
                            ps, 3, descriptiveName);
                        JDBCAccessorHelper.setStringValue(ps, 4, language);
                    }
                });
        }
    }

    /**
     * Insert a category into the VMPOLICY_CATEGORY table.
     *
     * @param connection the database connect to add data to.
     * @param categoryId  the id of the category to insert.
     * @param categoryName the name of the category to insert.
     * @throws RepositoryException if there was a problem.
     */
    private void insertPolicyCategory(
            final JDBCRepositoryConnection connection,
            final int categoryId,
            final String categoryName)
            throws RepositoryException {

        // Resolve field names.
        String categoryIDField = resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);
        String categoryNameField = resolveFieldName(connection, CATEGORY_NAME_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String languageField = resolveFieldName(connection, LANGUAGE_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyCategoryTable =
            resolveTableName(connection, VMPOLICY_CATEGORY);

        template.update(connection,
                "insert into "
                + vmPolicyCategoryTable
                + " ( "
                + projectField + " , "
                + categoryIDField + " , "
                + categoryNameField + " , "
                + languageField
                + " ) "
                + " values ( ? , ? , ? , ? )",

                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                    + VMPOLICY_CATEGORY + ","
                                    + " PROJECT: " + projectName
                                    + " CATEGORY_ID: " + categoryId
                                    + " CATEGORY_NAME: " + categoryName);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, categoryId);
                        JDBCAccessorHelper.setStringValue(ps, 3, categoryName);
                        ps.setString(4, DEFAULT_LANGAUGE);
                    }
                });
    }


    /**
     * Insert the category and policy type instance for a policy into the
     * VMPOLICY_TYPE table.
     *
     * @param connection the database connect to add data to.
     * @param policyName the name of the policy to insert.
     * @param categoryId the category id of the policy to insert.
     * @param typeInstanceId the type instance id of the policy to insert.
     * @throws RepositoryException if there was a problem.
     */
    private void insertPolicyType(
            final JDBCRepositoryConnection connection,
            final String policyName,
            final int categoryId,
            final int typeInstanceId) throws RepositoryException {

        // Resolve field names.
        String categoryIDField = resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String policyField = resolveFieldName(connection, POLICY_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyTypeTable = resolveTableName(connection, VMPOLICY_TYPE);

        template.update(connection,
                "insert into "
                + vmPolicyTypeTable
                + " ( "
                + projectField + " , "
                + policyField + " , "
                + typeInstanceIDField + " , "
                + categoryIDField
                + " ) "
                + " values ( ? , ? , ? , ? )",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                    + VMPOLICY_TYPE + ","
                                    + " PROJECT: " + projectName
                                    + " POLICY: " + policyName
                                    + " TYPE_INSTANCE_ID: " + typeInstanceId
                                    + " CATEGORY_ID: " + categoryId);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        JDBCAccessorHelper.setStringValue(ps, 2, policyName);
                        ps.setInt(3, typeInstanceId);
                        ps.setInt(4, categoryId);
                    }
                }
        );
    }

    /**
     * Insert the type instance id and type id for a policy type into the
     * VMTYPES table.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the synthetic instance id of the type.
     * @param typeId the id of the policy type for this type instance.
     * @throws RepositoryException if there was a problem.
     */
    private void insertTypeInstance(
            final JDBCRepositoryConnection connection,
            final int typeInstanceId, final int typeId)
            throws RepositoryException {

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String typeIDField = resolveFieldName(connection, TYPE_ID_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesTable = resolveTableName(connection, VMTYPES);

        template.update(connection,
                "insert into "
                + vmTypesTable
                + " ( "
                + projectField + " , "
                + typeInstanceIDField + " , "
                + typeIDField
                + " ) "
                + " values ( ? , ? , ? )",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                    + VMTYPES + ","
                                    + " PROJECT: " + projectName
                                    + " TYPE_INSTANCE_ID: " + typeInstanceId
                                    + " TYPE_ID: " + typeId);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, typeInstanceId);
                        ps.setInt(3, typeId);
                    }
                }
        );
    }

    /**
     * Add set specific type instance data into the repository with the type
     * instance id provided.
     * <p>
     * At a minimum, this will add a row into the VMTYPES_SET table, and a row
     * into VMTYPES and one of the VMTYPES_XXX tables for the contained member
     * type.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the instance id of the set type.
     * @param set the set policy type data.
     * @throws RepositoryException if there was a problem.
     */
    private void addTypeInstanceSet(
            JDBCRepositoryConnection connection,
            int typeInstanceId, SetPolicyType set)
            throws RepositoryException {

        int memberTypeInstanceId = getNextTypeInstanceId();

        // First add the contained member policy type instance that the
        // set is about to refer to.
        PolicyType memberPolicyType = set.getMemberPolicyType();
        if (!(memberPolicyType instanceof SimplePolicyType)) {
            throw new IllegalStateException();
        }
        addPolicyTypeInstance(connection, memberTypeInstanceId,
                memberPolicyType);

        boolean ordered;
        if (set instanceof OrderedSetPolicyType) {
            ordered = true;
        } else if (set instanceof UnorderedSetPolicyType) {
            ordered = false;
        } else {
            throw new IllegalStateException();
        }

        // Then add a row in VMTYPES_SET for the set type instance.
        insertTypeInstanceSet(connection, typeInstanceId,
                memberTypeInstanceId, ordered);
    }

    /**
     * Insert the type instance id, member instance id and ordered flag
     * for a set policy type into the VMTYPES_SET table.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the type instance id of the set.
     * @param memberInstanceId the instance id of the type of the set members.
     * @param ordered true if this is an ordered set or false if not.
     * @throws RepositoryException if there was a problem.
     */
    private void insertTypeInstanceSet(
            final JDBCRepositoryConnection connection,
            final int typeInstanceId, final int memberInstanceId,
            final boolean ordered) throws RepositoryException {

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String memberInstanceIDField = resolveFieldName(connection, MEMBER_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String orderedField = resolveFieldName(connection, ORDERED_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesSetTable = resolveTableName(connection, VMTYPES_SET);

        template.update(connection,
                "insert into " + vmTypesSetTable +
                " ( "
                + projectField + " , "
                + typeInstanceIDField + " , "
                + memberInstanceIDField + " , "
                + orderedField
                + " ) " +
                " values ( ? , ? , ? , ? )",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                    + VMTYPES_SET + ","
                                    + " PROJECT: " + projectName
                                    + " TYPE_INSTANCE_ID: " + typeInstanceId
                                    + " MEMBER_INSTANCE_ID: " + memberInstanceId
                                    + " ORDERED: " + (ordered ? 1 : 0));
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, typeInstanceId);
                        ps.setInt(3, memberInstanceId);
                        ps.setInt(4, ordered ? 1 : 0);
                    }
                }
        );
    }

    /**
     * Add structure specific type instance data into the repository with the
     * type instance id provided.
     * <p>
     * At a minimum, this will add a row into the VMTYPES_STRUCTURE table, the
     * VMTYPES table and one of the VMTYPES_XXX tables for each of the
     * contained fields.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the instance id of the structure type.
     * @param structure the structure policy type data.
     * @throws RepositoryException if there was a problem.
     */
    private void addTypeInstanceStructure(
            JDBCRepositoryConnection connection,
            int typeInstanceId,
            StructurePolicyType structure) throws RepositoryException {

        Map fields = structure.getFieldTypes();
        // sort in natural order to avoid depending on hashmap keyset ordering
        // makes testing and reading the data easier.
        List nameList = new ArrayList(fields.keySet());
        Collections.sort(nameList);

        Iterator names = nameList.iterator();
        while (names.hasNext()) {
            String fieldName = (String) names.next();
            PolicyType fieldPolicyType = (PolicyType) fields.get(fieldName);
            addTypeInstanceStructureField(connection,
                    typeInstanceId, fieldName, fieldPolicyType);
        }
    }

    /**
     * Add an individual field of a structure into the repository.
     * <p>
     * At a minimum, this will add a row into the VMTYPES_STRUCTURE table, the
     * VMTYPES and one of the VMTYPES_XXX tables.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the instance id of the set type.
     * @param fieldName the name of the structure field.
     * @param fieldPolicyType the policy type of the structure field.
     * @throws RepositoryException if there was a problem.
     */
    private void addTypeInstanceStructureField(
            JDBCRepositoryConnection connection,
            int typeInstanceId,
            String fieldName,
            PolicyType fieldPolicyType)
            throws RepositoryException {

        int fieldTypeInstanceId = getNextTypeInstanceId();

        // First add the contained member policy type instance that the
        // set is about to refer to.
        if (!(fieldPolicyType instanceof SimplePolicyType)) {
            throw new IllegalStateException();
        }
        addPolicyTypeInstance(connection, fieldTypeInstanceId,
                fieldPolicyType);

        // Then add a row in VMTYPES_STRUCTURE for the field type instance.
        insertTypeInstanceStructureField(connection,
                typeInstanceId, fieldName, fieldTypeInstanceId);

    }

    /**
     * Insert the type instance id, field name and field instance id
     * for field of a structure policy type into the VMTYPES_STRUCTURE table.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the instance id of the structure.
     * @param fieldName the name of the structure field.
     * @param fieldInstanceId the type instance id of the structure field.
     * @throws RepositoryException if there was a problem.
     */
    private void insertTypeInstanceStructureField(
            final JDBCRepositoryConnection connection,
            final int typeInstanceId,
            final String fieldName,
            final int fieldInstanceId)
            throws RepositoryException {

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String fieldNameField = resolveFieldName(connection, FIELD_NAME_COLUMN_NAMES);
        String fieldInstanceIDField = resolveFieldName(connection, FIELD_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesStructureTable = resolveTableName(connection, VMTYPES_STRUCTURE);

        template.update(connection,
                "insert into "
                + vmTypesStructureTable
                + " ( "
                + projectField + " , "
                + typeInstanceIDField + " , "
                + fieldNameField + " , "
                + fieldInstanceIDField
                + " ) "
                + " values ( ? , ? , ?  , ? )",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                    + VMTYPES_STRUCTURE + ","
                                    + " PROJECT: " + projectName
                                    + " TYPE_INSTANCE_ID: " + typeInstanceId
                                    + " FIELDNAME: " + fieldName
                                    + " FIELD_INSTANCE_ID: " + fieldInstanceId);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, typeInstanceId);
                        JDBCAccessorHelper.setStringValue(ps, 3, fieldName);
                        ps.setInt(4, fieldInstanceId);
                    }
                }
        );
    }

    /**
     * Insert the type instance id, minimum value and maximum value for a
     * range policy type into the VMTYPES_RANGE table.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the type instance id of the set.
     * @param minInclusive the minimum value of the range, inclusive.
     * @param maxInclusive the maximum value of the range, inclusive.
     * @throws RepositoryException if there was a problem.
     */
    private void insertTypeInstanceRange(
            final JDBCRepositoryConnection connection,
            final int typeInstanceId,
            final int minInclusive,
            final int maxInclusive)
            throws RepositoryException {

        // Resolve field names.
        String minValueField = resolveFieldName(connection,
                MIN_VALUE_COLUMN_NAMES);
        String maxValueField = resolveFieldName(connection,
                MAX_VALUE_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesRangeTable = resolveTableName(connection, VMTYPES_RANGE);

        template.update(connection,
                "insert into "
                + vmTypesRangeTable
                + " ( "
                + projectField + " , "
                + typeInstanceIDField + " , "
                + minValueField + " , "
                + maxValueField
                + " )"
                + " values ( ? , ? , ? , ? )",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {

                        String projectName = getProjectName();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Inserting into "
                                    + VMTYPES_RANGE + ","
                                    + " PROJECT: " + projectName
                                    + " TYPE_INSTANCE_ID: " + typeInstanceId
                                    + " MIN_VALUE: " + minInclusive
                                    + " MAX_VALUE: " + maxInclusive);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, typeInstanceId);
                        ps.setInt(3, minInclusive);
                        ps.setInt(4, maxInclusive);
                    }
                }
        );
    }

    /**
     * Insert the type instance id, and keywords for a selection policy type
     * into the VMTYPES_RANGE table.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the type instance id of the set.
     * @param keywordList the list of keywords of the selection.
     * @throws RepositoryException if there was a problem.
     */
    private void insertTypeInstanceSelection(
            final JDBCRepositoryConnection connection,
            final int typeInstanceId,
            List keywordList) throws RepositoryException {

        Iterator keywords = keywordList.iterator();
        while (keywords.hasNext()) {
            final String keyword = (String) keywords.next();

             // Resolve field names.
            String keywordField = resolveFieldName(connection, KEYWORD_COLUMN_NAMES);
            String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
            String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

            // Resolve table name.
            String vmTypesSelectionTable = resolveTableName(connection, VMTYPES_SELECTION);

            template.update(connection,
                    "insert into "
                    + vmTypesSelectionTable
                    + " ( "
                    + projectField + " , "
                    + typeInstanceIDField + " , "
                    + keywordField
                    + " ) "
                    + " values ( ? , ? , ? )",
                    new PreparedStatementSetter() {
                        public void setValues(PreparedStatement ps)
                                throws SQLException {
                            String projectName = getProjectName();
                            if (logger.isDebugEnabled()) {
                                logger.debug("Inserting into "
                                        + VMTYPES_SELECTION + ","
                                        + " PROJECT: " + projectName
                                        + " TYPE_INSTANCE_ID: " + typeInstanceId
                                        + " KEYWORD: " + keyword);
                            }
                            JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                            ps.setInt(2, typeInstanceId);
                            JDBCAccessorHelper.setStringValue(ps, 3, keyword);
                        }
                    });
        }
    }

    // Javadoc inherited.
    public void removePolicyDescriptor(
            RepositoryConnection connection,
            String policyName)
            throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        // Select the type instance id and category from VMPOLICY_TYPE.
        PolicyTypeData policyType = selectPolicyType(jdbcConnection,
                policyName);

        // Delete the instance (including any contained instances)
        removePolicyTypeInstance(jdbcConnection,
                policyType.typeInstanceId);

        // Delete the type
        deletePolicyType(jdbcConnection, policyName);


        int categoryUsages = countPolicyTypeCategories(jdbcConnection,
                policyType.categoryId);

        // If there are none left, remove the category.
        if (categoryUsages == 0) {
            deleteCategory(jdbcConnection, policyType.categoryId);
        }

        // delete the descriptors
        // Resolve field names.
        final String projectField = resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
        final String policyField = resolveFieldName(jdbcConnection, POLICY_COLUMN_NAMES);

        // Resolve table name.
        final String vmPolicyLangTable = resolveTableName(jdbcConnection, VMPOLICY_LANG);

        final String projectName = getProjectName();
        template.update(jdbcConnection,
            "delete from "
            + vmPolicyLangTable
            + " where "
            + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
            + " and "
            + policyField + " = " + JDBCAccessorHelper.quoteValue(policyName));

    }

    public void removeAllPolicyDescriptors(RepositoryConnection connection)
            throws RepositoryException {
        // Cast the repository connection to a JDBC Connection.
        JDBCRepositoryConnection jdbcConnection
                = (JDBCRepositoryConnection) connection;

        removeAllByTableName(jdbcConnection, VMTYPES_SET);
        removeAllByTableName(jdbcConnection, VMTYPES_STRUCTURE);
        removeAllByTableName(jdbcConnection, VMTYPES_RANGE);
        removeAllByTableName(jdbcConnection, VMTYPES_SELECTION);
        removeAllByTableName(jdbcConnection, VMTYPES);

        removeAllByTableName(jdbcConnection, VMPOLICY_TYPE);
        removeAllByTableName(jdbcConnection, VMPOLICY_LANG);
    }

    // javadoc inherited
    public void removeCategoryDescriptor(final RepositoryConnection connection,
                                         final String categoryName)
        throws RepositoryException {

        // Cast the repository connection to a JDBC Connection.
        final JDBCRepositoryConnection jdbcConnection =
            (JDBCRepositoryConnection) connection;

        final Integer categoryID =
            selectPolicyCategoryId(jdbcConnection, categoryName);
        if (categoryID != null) {
            // delete the descriptors
            // Resolve field names.
            final String projectField =
                resolveFieldName(jdbcConnection, PROJECT_COLUMN_NAMES);
            final String categoryIDField =
                resolveFieldName(jdbcConnection, CATEGORY_ID_COLUMN_NAMES);

            // Resolve table name.
            final String vmPolicyCategoryTable =
                resolveTableName(jdbcConnection, VMPOLICY_CATEGORY);

            final String projectName = getProjectName();
            template.update(jdbcConnection,
                "delete from "
                + vmPolicyCategoryTable
                + " where "
                + projectField + " = " +
                    JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + categoryIDField + " = " + categoryID);
        }
    }

    public void removeAllCategoryDescriptors(RepositoryConnection connection)
            throws RepositoryException {
        // Cast the repository connection to a JDBC Connection.
        final JDBCRepositoryConnection jdbcConnection =
            (JDBCRepositoryConnection) connection;

        removeAllByTableName(jdbcConnection, VMPOLICY_CATEGORY);
    }

    /**
     * Select the data from VMPOLICY_TYPE by policy name.
     *
     * @param connection the database connection to read from.
     * @param policyName the name of the policy to query by.
     * @return the data associated with the policy name, or null if not found.
     * @throws RepositoryException if there was a problem.
     */
    private PolicyTypeData selectPolicyType(
            JDBCRepositoryConnection connection,
            String policyName) throws RepositoryException {

        // Resolve field names.
        String categoryIDField = resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String policyField = resolveFieldName(connection, POLICY_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyTypeTable = resolveTableName(connection, VMPOLICY_TYPE);

        String projectName = getProjectName();
        PolicyTypeData policyType = (PolicyTypeData) template.queryForObject(
                connection,
                "select "
                + typeInstanceIDField + " , "
                + categoryIDField
                + " from "
                + vmPolicyTypeTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + policyField + " = " + JDBCAccessorHelper.quoteValue(policyName),
                new RowProcessor() {
                    public Object processRow(ResultSet rs)
                            throws SQLException {

                        PolicyTypeData policyType = new PolicyTypeData();
                        policyType.typeInstanceId = rs.getInt(1);
                        policyType.categoryId = rs.getInt(2);
                        return policyType;
                    }
                });
        return policyType;
    }


    /**
     * Remove the policy type with the instance id provided from the repository.
     * <p>
     * As a minimum, this will remove a row from VMTYPES, and a row from one of
     * the VMTYPES_XXX tables.
     *
     * @param jdbcConnection the database connection to remove data from.
     * @param typeInstanceId the instance id of the type to remove.
     * @throws RepositoryException if there was a problem.
     */
    private void removePolicyTypeInstance(
            JDBCRepositoryConnection jdbcConnection,
            int typeInstanceId)
            throws RepositoryException {

        // Select the type id from VMTYPES.
        Integer typeId = selectTypesId(jdbcConnection, typeInstanceId);
        if (typeId == null) {
            return;
        }

        // Remove a row (or rows) from the related VM_TYPES_XXX tables for
        // the detail of the type instance, if necessary.
        switch (typeId.intValue()) {
            case BOOLEAN_TYPE_ID:
                // no extra table for boolean.
                break;
            case INTEGER_TYPE_ID:
                // no extra table for int.
                break;
            case RANGE_TYPE_ID:
                deleteByTypeInstance(jdbcConnection,
                        VMTYPES_RANGE, typeInstanceId);
                break;
            case SELECTION_TYPE_ID:
                deleteByTypeInstance(jdbcConnection,
                        VMTYPES_SELECTION, typeInstanceId);
                break;
            case TEXT_TYPE_ID:
                // no extra table for text.
                break;
            case ORDERED_SET_TYPE_ID:
                // fall through to unordered set as the handling is the same.
            case UNORDERED_SET_TYPE_ID:
                removeTypeInstanceSet(jdbcConnection, typeInstanceId);
                break;
            case STRUCTURE_TYPE_ID:
                removeTypeInstanceStructure(jdbcConnection, typeInstanceId);
                break;
            default:
                throw new IllegalStateException();
        }

        // Remove a row from VMTYPES for the simple type definition.
        deleteByTypeInstance(jdbcConnection, VMTYPES, typeInstanceId);
    }

    /**
     * Remove a row or rows from the the table provided, by TYPE_INSTANCE_ID.
     *
     * @param connection the database connection to remove data from.
     * @param tableNames the name of the table to delete rows from.
     * @param typeInstanceId the value of the TYPE_INSTANCE_ID field.
     * @throws RepositoryException if there was a problem.
     */
    private void deleteByTypeInstance(
            JDBCRepositoryConnection connection,
            AlternateNames tableNames, int typeInstanceId) throws RepositoryException {

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String resolvedTableName = resolveTableName(connection, tableNames);

        String projectName = getProjectName();
        template.update(connection,
                "delete from "
                + resolvedTableName
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + typeInstanceIDField + " = " + typeInstanceId);
    }

    /**
     * Removes all the rows from the table provided.
     *
     * @param connection the database connection to remove data from.
     * @param tableName the name of the table to delete rows from.
     * @throws RepositoryException if there was a problem.
     */
    private void removeAllByTableName(
            JDBCRepositoryConnection connection,
            AlternateNames tableName) throws RepositoryException {

        // Resolve field names.
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String resolvedTableName = resolveTableName(connection, tableName);

        String projectName = getProjectName();
        template.update(connection,
                "delete from "
                + resolvedTableName
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName));
    }

    /**
     * Select a type id from the VMTYPES table, by type instance id.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the type instance id to query a type id for.
     * @return the type id, or null if one was not found.
     * @throws RepositoryException if there was a problem.
     */
    private Integer selectTypesId(JDBCRepositoryConnection connection,
                                  int typeInstanceId) throws RepositoryException {

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String typeIDField = resolveFieldName(connection, TYPE_ID_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesTable = resolveTableName(connection, VMTYPES);

        String projectName = getProjectName();
        Integer typeId = template.queryForInteger(connection,
                "select "
                + typeIDField
                + " from "
                + vmTypesTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + typeInstanceIDField + " = " + typeInstanceId);
        return typeId;
    }

    /**
     * Remove structure specific type instance data from the repository with
     * the type instance id provided.
     * <p>
     * At a minimum, this will remove a row from the VMTYPES_STRUCTURE table,
     * the VMTYPES table and one of the VMTYPES_XXX tables for each of the
     * contained fields.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the instance id of the structure type.
     * @throws RepositoryException if there was a problem.
     */
    private void removeTypeInstanceStructure(
            JDBCRepositoryConnection connection, int typeInstanceId)
            throws RepositoryException {

        int [] fieldTypeInstanceIds = selectTypesStructureFieldIds(
                connection, typeInstanceId);

        if (fieldTypeInstanceIds == null) {
            return;
        }

        for (int i=0; i<fieldTypeInstanceIds.length; i++) {
            removePolicyTypeInstance(connection, fieldTypeInstanceIds[i]);
        }

        deleteByTypeInstance(connection, VMTYPES_STRUCTURE,
                typeInstanceId);
    }


    /**
     * Select field instance ids from the VMTYPES_STRUCTURE table, by the type
     * instance id of the structure.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the type instance id of the structure.
     * @return the instance ids of the fields of the structure, or null if
     *      none were found.
     * @throws RepositoryException if there was a problem.
     */
    private int[] selectTypesStructureFieldIds(
            final JDBCRepositoryConnection connection,
            final int typeInstanceId) throws RepositoryException {

        final String projectName = getProjectName();

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String fieldInstanceIDField = resolveFieldName(connection, FIELD_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesStructureTable = resolveTableName(connection, VMTYPES_STRUCTURE);

        List list = template.query(connection,
                "select "
                + fieldInstanceIDField
                + " from "
                + vmTypesStructureTable
                + " where "
                + projectField + " = ?"
                + " and "
                + typeInstanceIDField + " = ? ",
                new RowProcessor() {
                    public Object processRow(ResultSet rs) throws SQLException {
                        return new Integer(rs.getInt(1));
                    }
                },
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps)
                            throws SQLException {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Selecting from "
                                        + VMTYPES_STRUCTURE + ","
                                    + " PROJECT: " + projectName
                                    + " TYPE_INSTANCE_ID: " + typeInstanceId);
                        }
                        JDBCAccessorHelper.setStringValue(ps, 1, projectName);
                        ps.setInt(2, typeInstanceId);
                    }
                });

        int[] fieldInstanceIds = null;
        if (list.size() > 0) {
            fieldInstanceIds = new int[list.size()];
            for (int i=0; i<list.size(); i++) {
                fieldInstanceIds[i] = ((Integer) list.get(i)).intValue();
            }
        }
        return fieldInstanceIds;
    }


    /**
     * Remove set specific type instance data from the repository with
     * the type instance id provided.
     * <p>
     * At a minimum, this will remove a row from the VMTYPES_SET table, and a
     * row from VMTYPES and one of the VMTYPES_XXX tables for the contained
     * member type.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the instance id of the set type.
     * @throws RepositoryException if there was a problem.
     */
    private void removeTypeInstanceSet(JDBCRepositoryConnection connection,
                                       int typeInstanceId) throws RepositoryException {

        Integer memberTypeInstanceId = selectTypesSetMemberId(
                connection, typeInstanceId);

        if (memberTypeInstanceId == null) {
            return;
        }

        removePolicyTypeInstance(connection, memberTypeInstanceId.intValue());

        deleteByTypeInstance(connection, VMTYPES_SET,
                typeInstanceId);
    }

    /**
     * Select the member instance id from the VMTYPES_SET table, by the type
     * instance id of the set.
     *
     * @param connection the database connect to add data to.
     * @param typeInstanceId the type instance id of the set.
     * @return the instance id of the member of the set, or null if one was not
     *      found.
     * @throws RepositoryException if there was a problem.
     */
    private Integer selectTypesSetMemberId(
            JDBCRepositoryConnection connection, int typeInstanceId)
            throws RepositoryException {

        // Resolve field names.
        String typeInstanceIDField = resolveFieldName(connection, TYPE_INSTANCE_ID_COLUMN_NAMES);
        String memberInstanceIDField = resolveFieldName(connection, MEMBER_INSTANCE_ID_COLUMN_NAMES);
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);

        // Resolve table name.
        String vmTypesSetTable = resolveTableName(connection, VMTYPES_SET);

        String projectName = getProjectName();
        Integer memberInstanceId = template.queryForInteger(connection,
                "select "
                + memberInstanceIDField
                + " from "
                + vmTypesSetTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + typeInstanceIDField + " = " + typeInstanceId);
        return memberInstanceId;
    }

    /**
     * Delete a row from the VMPOLICY_TYPE table, by policy name.
     *
     * @param connection the database connect to add data to.
     * @param policyName the name of the policy to insert.
     * @throws RepositoryException if there was a problem.
     */
    private void deletePolicyType(
            JDBCRepositoryConnection connection,
            String policyName) throws RepositoryException {

        // Resolve field names.
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String policyField = resolveFieldName(connection, POLICY_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyTypeTable = resolveTableName(connection, VMPOLICY_TYPE);

        String projectName = getProjectName();
        template.update(connection,
                "delete from "
                + vmPolicyTypeTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + policyField + " = " + JDBCAccessorHelper.quoteValue(policyName));
    }

    /**
     * Count the number of usages of the category id provided in the
     * VMPOLICY_TYPE table.
     *
     * @param connection the database connection to query in.
     * @param categoryId the id of the category to search for
     * @return the number of rows in VMPOLICY_TYPE which use the category id.
     * @throws RepositoryException if there was a problem.
     */
    private int countPolicyTypeCategories(
            JDBCRepositoryConnection connection,
            int categoryId) throws RepositoryException {

        // Resolve field names.
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String categoryIDField = resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyTypeTable = resolveTableName(connection, VMPOLICY_TYPE);

        String projectName = getProjectName();
        int count = template.queryForInteger(connection,
                "select COUNT(*) from "
                + vmPolicyTypeTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + categoryIDField + " = " + categoryId).intValue();
        return count;
    }

    /**
     * Delete a row from the VMPOLICY_CATEGORY table, by category id.
     *
     * @param connection the database connection to delete data from.
     * @param categoryId the id of the category to delete.
     * @throws RepositoryException if there was a problem.
     */
    private void deleteCategory(
            JDBCRepositoryConnection connection,
            int categoryId) throws RepositoryException {

        // Resolve field names.
        String projectField = resolveFieldName(connection, PROJECT_COLUMN_NAMES);
        String categoryIDField = resolveFieldName(connection, CATEGORY_ID_COLUMN_NAMES);

        // Resolve table name.
        String vmPolicyCategoryTable = resolveTableName(connection, VMPOLICY_CATEGORY);

        String projectName = getProjectName();
        template.update(connection,
                "delete from "
                + vmPolicyCategoryTable
                + " where "
                + projectField + " = " + JDBCAccessorHelper.quoteValue(projectName)
                + " and "
                + categoryIDField + " = " + categoryId);
    }

    /**
     * Simple inner class to represent the data in a row of the VMPOLICY_TYPE
     * table.
     */
    private static class PolicyTypeData {
        int typeInstanceId;
        int categoryId;
    }
}
