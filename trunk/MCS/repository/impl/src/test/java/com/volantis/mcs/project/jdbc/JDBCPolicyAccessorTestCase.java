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
package com.volantis.mcs.project.jdbc;

import com.volantis.mcs.accessors.chunker.ChunkedWriter;
import com.volantis.mcs.accessors.jdbc.JDBCAccessorHelper;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriterMock;
import com.volantis.mcs.accessors.xml.jibx.PolicyJiBXReaderMock;
import com.volantis.mcs.policies.InternalPolicyFactoryMock;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilderMock;
import com.volantis.mcs.policies.PolicyMock;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.impl.jdbc.JDBCPolicyBuilderAccessor;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.project.InternalProjectMock;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryEnumerationMock;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.AlternateNames;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryMock;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnectionMock;
import com.volantis.shared.content.ContentInput;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.java.sql.ConnectionMock;
import mock.java.sql.PreparedStatementMock;
import mock.java.sql.ResultSetMock;

/**
 * Verifies that {@link JDBCPolicyBuilderAccessor} behaves as expected.
 */

public class JDBCPolicyAccessorTestCase extends TestCaseAbstract {

    private InternalJDBCRepositoryMock repository;
    private ConnectionMock sqlConnection;
    private JDBCRepositoryConnectionMock jdbcConnection;
    private PolicyMock policyMock;
    private PreparedStatementMock stmt;
    private InternalPolicyFactoryMock factory;
    public PolicyJiBXReaderMock reader;
    private JiBXWriterMock writer;

    private static final PolicyType SUPPORTED_POLICY_TYPE = PolicyType.AUDIO;
    private final Class SUPPORTED_CLASS = Object.class;

    private final String tableName = "Test";
    private final String identityName = "/testID.mauc";
    private final String projectName = "testProject";
    private final String projectField = "PROJECT";
    private final String nameField = "NAME";
    private final String chunkField = "CHUNK";
    private final String chunkIDField = "CHUNKID";

    private JDBCPolicyBuilderAccessor accessor;
    private InternalProjectMock project;
    private PolicyBuilderMock policyBuilderMock;
    private JDBCPolicySource policySource;
    private AlternateNames nameFieldNames = new AlternateNames(nameField);
    private AlternateNames projectFieldNames = new AlternateNames(projectField);
    private AlternateNames chunkIDFieldNames = new AlternateNames(chunkIDField);
    private AlternateNames chunkFieldNames = new AlternateNames(chunkField);
    private AlternateNames tableNames = new AlternateNames("VMAUDIOCOMPONENT", "VMAUDIOCMPNT");

    // Javadoc inherited.
    public void setUp() throws Exception {
        super.setUp();
        // Create mock objects that are used in all tests.
        repository = new InternalJDBCRepositoryMock("repository", expectations);
        sqlConnection = new ConnectionMock("connection", expectations);
        jdbcConnection = new JDBCRepositoryConnectionMock("connection", expectations);
        policyMock = new PolicyMock("policyMock", expectations);
        policyBuilderMock = new PolicyBuilderMock("policyBuilderMock", expectations);
        stmt = new PreparedStatementMock("stmt", expectations);
        project = new InternalProjectMock("project", expectations);
        factory = new InternalPolicyFactoryMock("factory", expectations);
        reader = new PolicyJiBXReaderMock("policyReader", expectations,
                SUPPORTED_CLASS);
        writer = new JiBXWriterMock("writer", expectations);

        InternalProjectFactory projectFactory =
                InternalProjectFactory.getInternalInstance();

        policySource = projectFactory.createJDBCPolicySource(
                repository, projectName);

        // Set expectations.

        accessor = new JDBCPolicyBuilderAccessor(factory);

        policyMock.expects.getName().returns(identityName).any();
        policyBuilderMock.expects.getName().returns(identityName).any();
        policyBuilderMock.expects.getPolicyType().returns(PolicyType.AUDIO).any();

        project.expects.getPolicySource().returns(policySource).any();

        // Connection connection to repository.
        jdbcConnection.expects.getRepository().returns(repository).any();
    }

    /**
     * Verify that calling {@link JDBCPolicyBuilderAccessor#addPolicyBuilder} causes
     * the correct sql statement to be generated and the corresponding object
     * to be written to the
     * {@link com.volantis.mcs.accessors.common.ComponentWriter}.
     *
     * @throws RepositoryException if there is a problem running the test
     */
    public void testAddPolicyBuilder() throws RepositoryException {
        // Create test objects.
        final String sql = "insert into " + tableName
                + " (" + nameField + " , " + projectField + " , "
                + chunkIDField + " , " + chunkField + " ) "
                + "values (?, ?, ?, ?)";

        // The table name should be resolved.
        repository.expects.getAppropriateName(tableNames).returns(tableName);

        // The field names should be resolved.
        repository.expects.getAppropriateName(nameFieldNames)
                .returns(nameField);
        repository.expects.getAppropriateName(projectFieldNames)
                .returns(projectField);
        repository.expects.getAppropriateName(chunkIDFieldNames)
                .returns(chunkIDField);
        repository.expects.getAppropriateName(chunkFieldNames)
                .returns(chunkField);

        // Set expectations.
        factory.expects.createPolicyWriter().returns(writer);
        jdbcConnection.expects.getConnection().returns(sqlConnection);
        repository.expects.getChunkSize().returns(1024);
        sqlConnection.expects.prepareStatement(sql).returns(stmt);
        stmt.expects.setString(1, identityName);
        stmt.expects.setString(2, projectName);
        writer.fuzzy.write(mockFactory.expectsInstanceOf(
                ChunkedWriter.class), policyBuilderMock);
        stmt.expects.close();

        // Run test.
        accessor.addPolicyBuilder(jdbcConnection, project, policyBuilderMock);
    }

    /**
     * Verify that calling {@link JDBCPolicyBuilderAccessor#removePolicyBuilder} causes
     * the correct sql statement to be generated and executed.
     *
     * @throws RepositoryException if there is a problem running the test
     */
    public void testRemovePolicyBuilder() throws RepositoryException {
        // Create test objects.
        final String sql = "delete from Test where " +
                projectField +" = " +
                JDBCAccessorHelper.quoteValue(projectName) + " and " +
                nameField + " = " +
                JDBCAccessorHelper.quoteValue(identityName);

        // The table name should be resolved.
        repository.expects.getAppropriateName(tableNames).returns(tableName);

        // The field names should be resolved.
        repository.expects.getAppropriateName(nameFieldNames)
                .returns(nameField);
        repository.expects.getAppropriateName(projectFieldNames)
                .returns(projectField);

        // Set expectations.
        jdbcConnection.expects.getConnection().returns(sqlConnection);
        sqlConnection.expects.createStatement().returns(stmt);
        stmt.expects.executeUpdate(sql).returns(1);
        stmt.expects.close();

        // Run test.
        accessor.removePolicyBuilder(jdbcConnection, project, identityName);
    }

    /**
     * Verify that calling {@link JDBCPolicyBuilderAccessor#retrievePolicyBuilder}
     * causes the correct sql statement to be generated, executed and that the
     * result is passed to the
     * {@link com.volantis.mcs.accessors.xml.jibx.JiBXReader} to be read.
     *
     * @throws RepositoryException if there is a problem running the test
     */
    public void testRetrievePolicyBuilder() throws RepositoryException {

        // Create test objects.
        ResultSetMock rs = new ResultSetMock("rs", expectations);
        final String sql ="select " + nameField + " , " + projectField + " , "
                + chunkIDField + " , " + chunkField +
                " from " + tableName +
                " where " + projectField + " = "
                + JDBCAccessorHelper.quoteValue(projectName)
                + " and " + nameField + " = "
                + JDBCAccessorHelper.quoteValue(identityName)
                + " order by " + chunkIDField;

        // The table name should be resolved.
        repository.expects.getAppropriateName(tableNames).returns(tableName);

        // The field names should be resolved.
        repository.expects.getAppropriateName(nameFieldNames)
                .returns(nameField);
        repository.expects.getAppropriateName(projectFieldNames)
                .returns(projectField);
        repository.expects.getAppropriateName(chunkIDFieldNames)
                .returns(chunkIDField);
        repository.expects.getAppropriateName(chunkFieldNames)
                .returns(chunkField);

        // Set expectations.
        // todo: change this back to createPolicyReader when we fix validation
        factory.expects.createDangerousNonValidatingPolicyReader().returns(reader);
        jdbcConnection.expects.getConnection().returns(sqlConnection);
        sqlConnection.expects.createStatement().returns(stmt);
        stmt.expects.executeQuery(sql).returns(rs);
        rs.expects.next().returns(true);
        rs.expects.getString(4).returns("dummy result set");
        reader.fuzzy.read(
                mockFactory.expectsInstanceOf(ContentInput.class),
                mockFactory.expectsInstanceOf(String.class))
                .returns(policyBuilderMock);
        policyBuilderMock.expects.setName(identityName);
        stmt.expects.close();

        // Run test.
        PolicyBuilder retrieved = accessor.retrievePolicyBuilder(
                jdbcConnection, project, identityName);
        assertEquals(policyBuilderMock, retrieved);
    }

    /**
     * Verify that calling {@link JDBCPolicyBuilderAccessor#renamePolicyBuilder}
     * causes the correct sql statement to be generated and executed.
     *
     * @throws RepositoryException if there is a problem running the test
     */
    public void testRenamePolicyBuilder() throws RepositoryException {
        // Create test objects.
        final String newName = "newName";

        // The table name should be resolved.
        repository.expects.getAppropriateName(tableNames).returns(tableName);

        // The field names should be resolved.
        repository.expects.getAppropriateName(nameFieldNames)
                .returns(nameField);
        repository.expects.getAppropriateName(projectFieldNames)
                .returns(projectField);

        // Set expectations.
        jdbcConnection.expects.getConnection().returns(sqlConnection);
        sqlConnection.expects.createStatement().returns(stmt);
        stmt.fuzzy.executeUpdate(
                mockFactory.expectsInstanceOf(String.class)).returns(1);
        stmt.expects.close();

        // Run test.
        accessor.renamePolicyBuilder(jdbcConnection, project, identityName, newName);
    }

     /**
     * Verify that calling {@link JDBCPolicyBuilderAccessor#enumeratePolicyBuilderNames}
     * with a null parent policy name causes
      * {@link InternalJDBCRepository#selectUniqueValues}
      * to be called, and the results to be converted into a
      * {@link com.volantis.mcs.accessors.StringToIdentityEnumeration}.
     *
     * @throws RepositoryException if there is a problem running the test
     */
    public void testEnumeratePolicyBuildersWithNullParent()
            throws RepositoryException {

         // The table name should be resolved.
         repository.expects.getAppropriateName(tableNames).returns(tableName);

         // The field names should be resolved.
         repository.expects.getAppropriateName(nameFieldNames)
                 .returns(nameField);

        // Create test objects.
        RepositoryEnumerationMock enumeration =
                new RepositoryEnumerationMock("enumeration", expectations);

        // Set expectations.
        repository.expects.selectUniqueValues(jdbcConnection,
                nameField, tableName, projectName).returns(enumeration);
        enumeration.expects.close();

        // Run test.
        RepositoryEnumeration results =
                accessor.enumeratePolicyBuilderNames(jdbcConnection, project,
                        SUPPORTED_POLICY_TYPE);
         // verify that the returned enumeration is wrapping our mocked
         // enumeration by calling close, which will call close on the
         // underlying enumeration.
        results.close();
    }

    /**
     * Verify that calling {@link JDBCPolicyBuilderAccessor#updatePolicyBuilder} has
     * the same effect as calling {@link JDBCPolicyBuilderAccessor#removePolicyBuilder}
     * and then calling {@link JDBCPolicyBuilderAccessor#addPolicyBuilder}.
     *
     * @throws RepositoryException if there is a problem running the test
     */
    public void testUpdatePolicyBuilder() throws RepositoryException {

        // Set expectations for #removeObjectImpl
        final String sql = "delete from Test where " +
                        projectField +" = " +
                        JDBCAccessorHelper.quoteValue(projectName) + " and " +
                        nameField + " = " +
                        JDBCAccessorHelper.quoteValue(identityName);

        // The table name should be resolved.
        repository.expects.getAppropriateName(tableNames).returns(tableName);

        // The field names should be resolved.
        repository.expects.getAppropriateName(nameFieldNames)
                .returns(nameField);
        repository.expects.getAppropriateName(projectFieldNames)
                .returns(projectField);

        jdbcConnection.expects.getConnection().returns(sqlConnection);
        repository.expects.getChunkSize().returns(1024);
        sqlConnection.expects.createStatement().returns(stmt);
        stmt.expects.executeUpdate(sql).returns(1);
        stmt.expects.close();

        // Set expectations for #addObjectImpl

        // The table name should be resolved.
        repository.expects.getAppropriateName(tableNames).returns(tableName);

        // The field names should be resolved.
        repository.expects.getAppropriateName(nameFieldNames)
                .returns(nameField);
        repository.expects.getAppropriateName(projectFieldNames)
                .returns(projectField);
        repository.expects.getAppropriateName(chunkIDFieldNames)
                .returns(chunkIDField);
        repository.expects.getAppropriateName(chunkFieldNames)
                .returns(chunkField);

        factory.expects.createPolicyWriter().returns(writer);
        jdbcConnection.expects.getConnection().returns(sqlConnection);
        sqlConnection.fuzzy.prepareStatement(
                mockFactory.expectsInstanceOf(String.class)).returns(stmt);
        stmt.expects.setString(1, identityName);
        stmt.expects.setString(2, projectName);
        writer.fuzzy.write(
                mockFactory.expectsInstanceOf(ChunkedWriter.class),
                policyBuilderMock);
        stmt.expects.close();

        // Run test
        accessor.updatePolicyBuilder(jdbcConnection, project, policyBuilderMock);
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

 ===========================================================================
*/
