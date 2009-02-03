/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package mock.java.sql;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;

import java.sql.ResultSet;

/**
 * An class that provides method to help with using mock objects within this
 * library.
 */
public class SQLHelper {

    /**
     * Add rows to the resultSet {@link ResultSet}, providing access to it in as
     * flexible a manner as possible.
     *
     * <p>This enforces some of the portability rules defined within
     * the {@link ResultSet}, such as columns should be read from left to right
     * and each column should only be read once per row.</p>
     *
     * <p>The array of column types specifies the expected types for the data
     * in the column. This must be the same length as the contents of each row
     * and its order must match the order of values within the row.</p>
     *
     * <p>The array of column names allows the user of the result set access to
     * the columns by name. If it is provided then it must be the same length
     * as the array of column types and is assumed to be in the same order.</p>
     *
     * @param expectations The builder into which the expectations will be
     *                     added.
     * @param resultSet    The resultSet {@link ResultSet}.
     * @param columnTypes  The types of the columns.
     * @param columnNames  The names of the columns. If null then access using
     *                     named columns is not allowed.
     * @param rows         The array of rows, where each row is an array.
     */
    public static void setExpectedResults(
            final ExpectationBuilder expectations,
            final ResultSetMock resultSet,
            final SQLMockType[] columnTypes,
            final String[] columnNames,
            final Object[][] rows) {

        if (expectations == null) {
            throw new IllegalArgumentException("expectations cannot be null");
        }
        if (resultSet == null) {
            throw new IllegalArgumentException("resultSet cannot be null");
        }
        if (columnTypes == null) {
            throw new IllegalArgumentException("columnTypes cannot be null");
        }
        if (rows == null) {
            throw new IllegalArgumentException("rows cannot be null");
        }

        if (columnNames != null && columnNames.length != columnTypes.length) {
            throw new IllegalArgumentException("Column name array length does not match length of" +
                    " column types array");
        }

        expectations.add(new UnorderedExpectations() {

            public void add() {
                // Allow the indeces for the column names to be found as many
                // times as possible.
                if (columnNames != null) {
                    expectations.add(new UnorderedExpectations() {
                        public void add() {
                            for (int c = 0; c < columnNames.length; c++) {
                                String name = columnNames[c];
                                resultSet.expects.findColumn(name)
                                        .returns(c + 1)
                                        .any();
                            }
                        }
                    });
                }

                expectations.add(new OrderedExpectations() {
                    public void add() {

                        for (int r = 0; r < rows.length; r++) {
                            final Object[] row = rows[r];

                            // Add an expectation to move to the next row.
                            resultSet.expects.next().returns(true).optional();

                            // Make sure that the column names are of the correct length.
                            if (columnNames != null &&
                                    columnNames.length != row.length) {
                                throw new IllegalArgumentException("Column types array length does not match length of" +
                                        " row contents");
                            }

                            // Add the row data.
                            addRow(expectations, resultSet, columnTypes,
                                    columnNames, row);
                        }

                        resultSet.expects.next().returns(false).optional();
                    }
                });
            }

        });

    }

    /**
     * Add the results for an individual row.
     *
     * @param expectations
     * @param resultSet
     * @param columnTypes
     * @param columnNames
     * @param row
     */
    private static void addRow(
            final ExpectationBuilder expectations,
            final ResultSetMock resultSet,
            final SQLMockType[] columnTypes,
            final String[] columnNames,
            final Object[] row) {

        expectations.add(new UnorderedExpectations() {
            public void add() {
                // Add expectations for each value in the row.
                for (int i = 0; i < row.length; i++) {
                    Object o = row[i];
                    SQLMockType type = columnTypes[i];

                    if (columnNames != null) {
                        type.expectsGetter(resultSet, columnNames[i], o)
                                .optional();
                    }
                    type.expectsGetter(resultSet, i + 1, o).optional();
                }
            }
        });
    }

    public static PreparedStatementMock createPreparedStatementMock(
            final String identifier, ExpectationBuilder expectations,
            ConnectionMock connectionMock) {
        
        PreparedStatementMock statementMock = new PreparedStatementMock(
                identifier, expectations);
        statementMock.expects.getConnection().returns(connectionMock).any();
        return statementMock;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/7	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 02-Jun-05	7995/5	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 31-May-05	7995/3	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
