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
package com.volantis.mcs.policies.impl.jdbc;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.accessors.chunker.ChunkReadHandler;
import com.volantis.mcs.accessors.chunker.ChunkWriteHandler;
import com.volantis.mcs.accessors.chunker.ChunkedReader;
import com.volantis.mcs.accessors.chunker.ChunkedWriter;
import com.volantis.mcs.accessors.common.ComponentWriter;
import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;
import com.volantis.mcs.accessors.jdbc.JDBCChunkReadHandler;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.impl.jdbc.JDBCChunkWriteHandler;
import com.volantis.mcs.repository.jdbc.AlternateNames;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.mcs.repository.jdbc.VolantisSchemaNamesFactory;
import com.volantis.shared.content.ContentInput;
import com.volantis.shared.content.TextContentInput;
import com.volantis.shared.throwable.ExtendedIOException;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides access to objects stored in chunks in a JDBC based repository.
 */
public class JDBCPolicyBuilderAccessor
        implements PolicyBuilderAccessor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(JDBCPolicyBuilderAccessor.class);

    /**
     * The map from policy type to table name.
     */
    private static final Map POLICY_TYPE_2_TABLE_NAME;

    /**
     * Volantis specific factory for {@link AlternateNames}.
     */
    private static final VolantisSchemaNamesFactory FACTORY =
            VolantisSchemaNamesFactory.getDefaultInstance();

    /**
     * The names for the name field.
     */
    private static final AlternateNames NAME_FIELD_NAMES =
            FACTORY.createColumnNames("NAME");

    /**
     * The names for the project field.
     */
    private static final AlternateNames PROJECT_FIELD_NAMES =
            FACTORY.createColumnNames("PROJECT");

    /**
     * The names for the chunk id field.
     */
    private static final AlternateNames CHUNK_ID_FIELD_NAMES =
            FACTORY.createColumnNames("CHUNKID");

    /**
     * The names for the chunk field.
     */
    private static final AlternateNames CHUNK_FIELD_NAMES =
            FACTORY.createColumnNames("CHUNK");

    static {
        Map map = new HashMap();

        map.put(PolicyType.AUDIO,
                FACTORY.createTableNames("AUDIOCOMPONENT", "AUDIOCMPNT"));

        map.put(PolicyType.BASE_URL,
                FACTORY.createTableNames("ASSETGROUP", "ASSETGROUP"));

        map.put(PolicyType.BUTTON_IMAGE,
                FACTORY.createTableNames("BUTTONIMAGECOMPONENT", "BUTTONIMGCMPNT"));

        map.put(PolicyType.CHART,
                FACTORY.createTableNames("CHARTCOMPONENT", "CHARTCMPNT"));

        map.put(PolicyType.IMAGE,
                FACTORY.createTableNames("IMAGECOMPONENT", "IMGCMPNT"));

        map.put(PolicyType.LAYOUT, FACTORY.createTableNames("LAYOUT"));

        map.put(PolicyType.LINK,
                FACTORY.createTableNames("LINKCOMPONENT", "LINKCMPNT"));

        map.put(PolicyType.RESOURCE,
                FACTORY.createTableNames("RESOURCECOMPONENT", "RESOURCECMPNT"));

        map.put(PolicyType.ROLLOVER_IMAGE,
                FACTORY.createTableNames("ROLLOVERIMAGECOMPONENT", "ROLLOVERIMGCMPNT"));

        map.put(PolicyType.SCRIPT,
                FACTORY.createTableNames("SCRIPTCOMPONENT", "SCRIPTCMPNT"));

        map.put(PolicyType.TEXT,
                FACTORY.createTableNames("TEXTCOMPONENT", "TEXTCMPNT"));

        map.put(PolicyType.THEME, FACTORY.createTableNames("THEME"));

        map.put(PolicyType.VIDEO,
                FACTORY.createTableNames("DYNAMICVISUALCOMPONENT", "DYNVISCMPNT"));

        POLICY_TYPE_2_TABLE_NAME = map;
    }

    /**
     * Factory to use to create JiBX readers and writers.
     */
    private final InternalPolicyFactory factory;

    /**
     * Initialize a new instance using the given parameters.
     */
    public JDBCPolicyBuilderAccessor(InternalPolicyFactory factory) {
        this.factory = factory;
    }

    public JDBCPolicyBuilderAccessor() {
        this(InternalPolicyFactory.getInternalInstance());
    }

    /**
     * Extract the JDBC connection from the repository connection. This is a
     * convenience method which is overridden by the shared accessor to avoid
     * code duplication.
     *
     * @param connection    from which to extract the JDBC connection
     * @return JDBCRepositoryConnection with which to access the database
     */
    protected JDBCRepositoryConnection getJDBCConnection(
            RepositoryConnection connection) throws RepositoryException {
        // Cast the repository connection to a JDBCRepositoryConnection.
        return (JDBCRepositoryConnection) connection;
    }

    // Javadoc inherited.
    public void addPolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder) throws RepositoryException {

        // Get the repository from the project and use that to select the
        // appropriate names for the table and fields.
        InternalJDBCRepository repository = getRepository(connection, project);
        String tableName = getTableName(repository, builder.getPolicyType());
        String nameField = repository.getAppropriateName(NAME_FIELD_NAMES);
        String projectField = repository.getAppropriateName(PROJECT_FIELD_NAMES);
        String chunkIdField = repository.getAppropriateName(CHUNK_ID_FIELD_NAMES);
        String chunkField = repository.getAppropriateName(CHUNK_FIELD_NAMES);

        ComponentWriter componentWriter = factory.createPolicyWriter();

        // Cast the repository connection to a JDBCRepositoryConnection.
        Connection sqlConnection = getSQLConnection(connection);

        String projectName = getProjectName(project);

        String sql = "insert into " + tableName
                + " (" + nameField + " , " + projectField + " , "
                + chunkIdField + " , " + chunkField + " ) "
                + "values (?, ?, ?, ?)";

        PreparedStatement stmt = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(sql);
            }
            stmt = sqlConnection.prepareStatement(sql);
            JDBCAccessorHelper.setStringValue(stmt, 1, builder.getName());
            JDBCAccessorHelper.setStringValue(stmt, 2, projectName);

            ChunkWriteHandler chunkWriteHandler =
                    new JDBCChunkWriteHandler(stmt, 3, 4);

            Writer chunkedWriter = new ChunkedWriter(chunkWriteHandler,
                    repository.getChunkSize());

            componentWriter.write(chunkedWriter, builder);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                        ((JDBCChunkWriteHandler) chunkWriteHandler).getRows() +
                        " rows processed");
            }
        } catch (IOException e) {
            throw new JDBCRepositoryException(
                    "IOException in ChunkedWriter", e);
        } catch (SQLException e) {
            LOGGER.error("sql-exception", e);
            throw new JDBCRepositoryException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                LOGGER.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
        }
    }

    /**
     * Gets the repository from both the connection and the project, makes sure
     * that they are the same and returns it.
     *
     * @param connection The connection.
     * @param project The project.
     * @return The repository.
     * @throws IllegalStateException if the project and connection were
     * inconsistent.
     */
    private InternalJDBCRepository getRepository
            (RepositoryConnection connection, Project project) {

        // Get the repository from the connection.
        InternalJDBCRepository connectionRepository = (InternalJDBCRepository)
                connection.getRepository();

        // Get the repository from the project.
        InternalProject internalProject = (InternalProject) project;
        LocalPolicySource source = (LocalPolicySource)
                internalProject.getPolicySource();
        InternalJDBCRepository projectRepository = (InternalJDBCRepository)
                source.getRepository();

        if (connectionRepository != projectRepository) {
            throw new IllegalStateException(
                    "Inconsistent connection and project, " +
                    "connection references " + connectionRepository +
                    " and project references " + projectRepository);
        }

        return projectRepository;
    }

    // Javadoc inherited.
    public boolean updatePolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder)
            throws RepositoryException {

        boolean existed = removePolicyBuilder(connection, project,
                builder.getName());
        addPolicyBuilder(connection, project, builder);
        return existed;
    }

    // Javadoc inherited from super class.
    public boolean removePolicyBuilder(
            RepositoryConnection connection,
            Project project, String name)
            throws RepositoryException {

        // Get the repository from the project and use that to select the
        // appropriate names for the table and fields.
        InternalJDBCRepository repository = getRepository(connection, project);
        String tableName = getTableName(repository, name);
        String nameField = repository.getAppropriateName(NAME_FIELD_NAMES);
        String projectField = repository.getAppropriateName(PROJECT_FIELD_NAMES);

        // Get the JDBCRepository connection from the connection
        Connection sqlConnection = getSQLConnection(connection);

        String projectName = getProjectName(project);

        String sql = "delete from " + tableName
                + " where " + projectField + " = " +
                JDBCAccessorHelper.quoteValue(projectName) +
                " and "
                + nameField + " = "
                + JDBCAccessorHelper.quoteValue(name);

        Statement stmt = null;
        try {
            stmt = sqlConnection.createStatement();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(sql);
            }
            int rows = stmt.executeUpdate(sql);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(rows + " rows processed");
            }
            return rows != 0;
        } catch (SQLException e) {
            LOGGER.error("sql-exception", e);
            throw new JDBCRepositoryException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                LOGGER.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
        }
    }

    private Connection getSQLConnection(RepositoryConnection connection)
            throws RepositoryException {
        JDBCRepositoryConnection jdbcConnection = getJDBCConnection(connection);
        // Get the java.sql.Connection.
        Connection sqlConnection = jdbcConnection.getConnection();
        return sqlConnection;
    }

    // Javadoc inherited from super class.
    public PolicyBuilder retrievePolicyBuilder(
            RepositoryConnection connection,
            Project project, String name)
            throws RepositoryException {

        // Get the repository from the project and use that to select the
        // appropriate names for the table and fields.
        InternalJDBCRepository repository = getRepository(connection, project);
        String tableName = getTableName(repository, name);
        String nameField = repository.getAppropriateName(NAME_FIELD_NAMES);
        String projectField = repository.getAppropriateName(PROJECT_FIELD_NAMES);
        String chunkIdField = repository.getAppropriateName(CHUNK_ID_FIELD_NAMES);
        String chunkField = repository.getAppropriateName(CHUNK_FIELD_NAMES);

        // todo: re-enable validation by avoiding this overload once we have
        // sorted out validation.
        JiBXReader jibxReader = factory.createDangerousNonValidatingPolicyReader();

        // Get the SQL Connection.
        Connection sqlConnection = getSQLConnection(connection);

        String projectName = getProjectName(project);

        // get resolved field names for select string
        String sql = "select " + nameField + " , " + projectField + " , "
                + chunkIdField + " , " + chunkField +
                " from " + tableName +
                " where " + projectField + " = " +
                JDBCAccessorHelper.quoteValue(projectName) +
                " and " + nameField + " = " +
                JDBCAccessorHelper.quoteValue(name) +
                " order by " + chunkIdField;

        Statement stmt = null;
        try {
            stmt = sqlConnection.createStatement();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(sql);
            }
            ResultSet rs = stmt.executeQuery(sql);

            ChunkReadHandler chunkReadHandler = new JDBCChunkReadHandler(rs, 4);
            ChunkedReader chunkedReader = new ChunkedReader(chunkReadHandler);
            // If there is no object available...
            if (chunkedReader.isEndOfStream()) {
                // ... then return null straight away.
                return null;
            }
            ContentInput content = new TextContentInput(chunkedReader);
            PolicyBuilder policyBuilder = (PolicyBuilder) 
                    jibxReader.read(content, projectName + name);
            policyBuilder.setName(name);
            return policyBuilder;
        } catch (SQLException e) {
            LOGGER.error("sql-exception", e);
            throw new JDBCRepositoryException(e);
        } catch (ExtendedIOException e) {
            // Thrown from the JDBCChunkHandler, will always be a SQLException.
            SQLException e2 = (SQLException) e.getCause();
            LOGGER.error("sql-exception", e);            
            throw new JDBCRepositoryException(e2);
        } catch (IOException e) {
            LOGGER.error("io-exception", e);
            throw new JDBCRepositoryException("IO Exception");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                LOGGER.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
        }
    }

    // Javadoc inherited from super class.
    public void renamePolicyBuilder(
            RepositoryConnection connection,
            Project project, String name,
            String newName)
            throws RepositoryException {


        PolicyType oldPolicyType = FileExtension.getPolicyTypeForPolicy(name);

        // Get the repository from the project and use that to select the
        // appropriate names for the table and fields.
        InternalJDBCRepository repository = getRepository(connection, project);
        String tableName = getTableName(repository, oldPolicyType);
        String nameField = repository.getAppropriateName(NAME_FIELD_NAMES);
        String projectField = repository.getAppropriateName(PROJECT_FIELD_NAMES);

        // Get the SQL Connection.
        Connection sqlConnection = getSQLConnection(connection);

        String projectName = getProjectName(project);

        String sql = "update " + tableName
                + " set " + nameField + " = "
                + JDBCAccessorHelper.quoteValue(newName)
                + " where " + projectField + " = " +
                JDBCAccessorHelper.quoteValue(projectName) +
                " and " + nameField + " ="
                + JDBCAccessorHelper.quoteValue(name);

        Statement stmt = null;
        try {
            stmt = sqlConnection.createStatement();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(sql);
            }
            int rows = stmt.executeUpdate(sql);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(rows + " rows processed");
            }
        } catch (SQLException e) {
            LOGGER.error("sql-exception", e);
            throw new JDBCRepositoryException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                LOGGER.error("sql-exception", e);
                throw new JDBCRepositoryException(e);
            }
        }
    }

    public RepositoryEnumeration enumeratePolicyBuilderNames(
            RepositoryConnection connection, Project project,
            PolicyType policyType) throws RepositoryException {

        // Get the repository from the project and use that to select the
        // appropriate names for the table and fields.
        InternalJDBCRepository repository = getRepository(connection, project);
        String tableName = getTableName(repository, policyType);
        String nameField = repository.getAppropriateName(NAME_FIELD_NAMES);

        // Cast the repository connection to a JDBCRepositoryConnection.
        JDBCRepositoryConnection jdbcConnection = getJDBCConnection(connection);

        RepositoryEnumeration nameEnumeration =
                repository.selectUniqueValues(jdbcConnection, nameField,
                        tableName, getProjectName(project));

        return nameEnumeration;
    }

    private String getTableName(InternalJDBCRepository repository,
                                String name) {
        PolicyType policyType = FileExtension.getPolicyTypeForPolicy(name);
        return getTableName(repository, policyType);
    }

    private String getTableName(InternalJDBCRepository repository,
                                PolicyType policyType) {
        // Get the table names for the policy type.
        AlternateNames names = (AlternateNames) 
                POLICY_TYPE_2_TABLE_NAME.get(policyType);
        if (names == null) {
            throw new IllegalStateException(
                    "Unknown policy type " + policyType);
        }

        return repository.getAppropriateName(names);
    }

    /**
     * Return the project name associated with the identity provided.
     *
     * @param project
     * @return the project name.
     */
    private String getProjectName(Project project) {
        JDBCPolicySource policySource = (JDBCPolicySource)
                ((InternalProject) project).getPolicySource();
        return policySource.getName();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	9789/8	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 03-Nov-05	9789/6	emma	VBM:2005101113 Supermerge: Migrate JDBC Accessors to use chunked accessors

 23-Oct-05	9789/4	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 27-May-04	4511/3	tom	VBM:2004052005 Added support for short column names

 11-Mar-04	3376/3	adrian	VBM:2004030908 Rework to fix javadoc duplication

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 ===========================================================================
*/
