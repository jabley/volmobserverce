/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package mock.java.sql;

import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.method.Occurrences;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.Time;
import java.sql.Timestamp;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;

/**
 * An enumeration of the supported SQL types.
 */
public abstract class SQLMockType {

    private static MockFactory mockFactory = MockFactory.getDefaultInstance();

    /**
     * Add an expectation to call the indexed getter for this type on the
     * {@link ResultSet}.
     *
     * @param mock The mock {@link ResultSet}
     * @param columnIndex The column index.
     * @param returnedValue The value returned by the getter.
     */
    abstract Occurrences expectsGetter(
            ResultSetMock mock, int columnIndex, Object returnedValue);

    /**
     * Add an expectation to call the named getter for this type on the
     * {@link ResultSet}.
     *
     * @param mock The mock {@link ResultSet}
     * @param columnName The column name.
     * @param returnedValue The value returned by the getter.
     */
    abstract Occurrences expectsGetter(
            ResultSetMock mock, String columnName, Object returnedValue);

    /**
     * Add an expectation to call the indexed setter for this type on the
     * {@link PreparedStatement}.
     *
     * @param mock The mock {@link PreparedStatement}
     * @param columnIndex The column index.
     * @param expectedValue The value expected by the setter.
     */
    abstract Occurrences expectsSetter(
            PreparedStatementMock mock, int columnIndex,
            Object expectedValue);

    /**
     * Represents an SQL Array type.
     */
    public static final SQLMockType ARRAY = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getArray(columnIndex)
                    .returns((Array) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getArray(columnName)
                    .returns((Array) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setArray(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Ascii Stream type.
     */
    public static final SQLMockType ASCII_STREAM = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getAsciiStream(columnIndex)
                    .returns((InputStream) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getAsciiStream(columnName)
                    .returns((InputStream) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setAsciiStream(new Integer(columnIndex), expectedValue,
                                               mockFactory.expectsAny()).returns();
        }
    };

    /**
     * Represents an SQL Big Decimal type.
     */
    public static final SQLMockType BIG_DECIMAL = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getBigDecimal(columnIndex)
                    .returns((BigDecimal) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getBigDecimal(columnName)
                    .returns((BigDecimal) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setBigDecimal(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Binary Stream type.
     */
    public static final SQLMockType BINARY_STREAM = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getBinaryStream(columnIndex)
                    .returns((InputStream) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getBinaryStream(columnName)
                    .returns((InputStream) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setBinaryStream(new Integer(columnIndex), expectedValue,
                                                mockFactory.expectsAny())
                    .returns();
        }
    };

    /**
     * Represents an SQL Blob type.
     */
    public static final SQLMockType BLOB = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getBlob(columnIndex)
                    .returns((Blob) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getBlob(columnName)
                    .returns((Blob) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setBlob(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Boolean type.
     */
    public static final SQLMockType BOOLEAN = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getBoolean(columnIndex)
                    .returns(((Boolean) returnedValue).booleanValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getBoolean(columnName)
                    .returns(((Boolean) returnedValue).booleanValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setBoolean(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Byte type.
     */
    public static final SQLMockType BYTE = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getByte(columnIndex)
                    .returns(((Byte) returnedValue).byteValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getByte(columnName)
                    .returns(((Byte) returnedValue).byteValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setByte(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Bytes type.
     */
    public static final SQLMockType BYTES = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getBytes(columnIndex)
                    .returns((byte[]) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getBytes(columnName)
                    .returns((byte[]) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setBytes(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Character Stream type.
     */
    public static final SQLMockType CHARACTER_STREAM = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getCharacterStream(columnIndex)
                    .returns((Reader) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getCharacterStream(columnName)
                    .returns((Reader) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setCharacterStream(new Integer(columnIndex),
                                                   expectedValue,
                                                   mockFactory.expectsAny())
                    .returns();
        }
    };

    /**
     * Represents an SQL Clob type.
     */
    public static final SQLMockType CLOB = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getClob(columnIndex)
                    .returns((Clob) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getClob(columnName)
                    .returns((Clob) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setClob(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Date type.
     */
    public static final SQLMockType DATE = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getDate(columnIndex)
                    .returns((Date) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getDate(columnName)
                    .returns((Date) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setDate(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Double type.
     */
    public static final SQLMockType DOUBLE = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getDouble(columnIndex)
                    .returns(((Double) returnedValue).doubleValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getDouble(columnName)
                    .returns(((Double) returnedValue).doubleValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setDouble(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Float type.
     */
    public static final SQLMockType FLOAT = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getFloat(columnIndex)
                    .returns(((Float) returnedValue).floatValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getFloat(columnName)
                    .returns(((Float) returnedValue).floatValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setFloat(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Int type.
     */
    public static final SQLMockType INT = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getInt(columnIndex)
                    .returns(((Integer) returnedValue).intValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getInt(columnName)
                    .returns(((Integer) returnedValue).intValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setInt(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Long type.
     */
    public static final SQLMockType LONG = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getLong(columnIndex)
                    .returns(((Long) returnedValue).longValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getLong(columnName)
                    .returns(((Long) returnedValue).longValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setLong(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Object type.
     */
    public static final SQLMockType OBJECT = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getObject(columnIndex)
                    .returns(returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getObject(columnName)
                    .returns(returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setObject(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Ref type.
     */
    public static final SQLMockType REF = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getRef(columnIndex)
                    .returns((Ref) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getRef(columnName)
                    .returns((Ref) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setRef(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Short type.
     */
    public static final SQLMockType SHORT = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getShort(columnIndex)
                    .returns(((Short) returnedValue).shortValue());
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getShort(columnName)
                    .returns(((Short) returnedValue).shortValue());
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setShort(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL String type.
     */
    public static final SQLMockType STRING = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getString(columnIndex)
                    .returns((String) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getString(columnName)
                    .returns((String) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setString(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Time type.
     */
    public static final SQLMockType TIME = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getTime(columnIndex)
                    .returns((Time) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getTime(columnName)
                    .returns((Time) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setTime(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Time type.
     */
    public static final SQLMockType TIMESTAMP = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getTimestamp(columnIndex)
                    .returns((Timestamp) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getTimestamp(columnName)
                    .returns((Timestamp) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setTimestamp(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };

    /**
     * Represents an SQL Unicode Stream type.
     */
    public static final SQLMockType UNICODE_STREAM = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getUnicodeStream(columnIndex)
                    .returns((InputStream) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getUnicodeStream(columnName)
                    .returns((InputStream) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setUnicodeStream(new Integer(columnIndex),
                                                 expectedValue,
                                                 mockFactory.expectsAny())
                    .returns();
        }
    };

    /**
     * Represents an SQL URL type.
     */
    public static final SQLMockType URL = new SQLMockType() {

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, int columnIndex, Object returnedValue) {

            return mock.expects.getURL(columnIndex)
                    .returns((URL) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsGetter(
                ResultSetMock mock, String columnName, Object returnedValue) {

            return mock.expects.getURL(columnName)
                    .returns((URL) returnedValue);
        }

        // Javadoc inherited.
        Occurrences expectsSetter(
                PreparedStatementMock mock, int columnIndex,
                Object expectedValue) {

            return mock.fuzzy.setURL(new Integer(columnIndex), expectedValue)
                    .returns();
        }
    };
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/3	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
