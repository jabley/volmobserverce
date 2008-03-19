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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.marshaller;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.reporting.config.DataSourceType;
import com.volantis.synergetics.reporting.config.DatasourceConfiguration;
import com.volantis.synergetics.reporting.config.InternalPoolDatasource;
import com.volantis.synergetics.reporting.config.JDBCDatasource;
import com.volantis.synergetics.reporting.config.JNDIDatasource;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

/**
 * Datasource configuration jibx marshaller. It unmarshals one of the
 * datasource configuration.
 */
public abstract class DatasourceMarshaller implements IMarshaller,
        IUnmarshaller, IAliasable {

    /**
     * Localize the exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = 
        LocalizationFactory.createExceptionLocalizer(
                DatasourceMarshaller.class);   
    
    /**
     * Tag names.
     */
    private static final String JDBC_DATASOURCE_TAG_NAME = "jdbc-datasource";

    private static final String DRIVER_CLASS_TAG_NAME = "driver-class";

    private static final String CONNECTION_STRING_TAG_NAME = 
                                                "connection-string";

    private static final String URL_TAG_NAME = "url";

    private static final String USERNAME_TAG_NAME = "username";

    private static final String PASSWORD_TAG_NAME = "password";

    private static final String MAX_ACTIVE_TAG_NAME = "max-active";

    private static final String MAX_IDLE_TAG_NAME = "max-idle";

    private static final String MAX_WAIT_TAG_NAME = "max-wait";

    private static final String INTERNAL_POOL_DATASOURCE_TAG_NAME = 
                                                "internal-pool-datasource";

    private static final String JNDI_DATASOURCE_TAG_NAME = "jndi-datasource";

    private static final String JNDI_NAME_TAG_NAME = "jndi-name";    

    /**
     * Uri.
     */
    private Object uri;
    
    /**
     * Index.
     */
    private int index;
    
    /**
     * Name.
     */
    private String name;    
    
    /**
     * Default constructor
     */
    public DatasourceMarshaller() {
        this.uri = null;
        this.index = 0;
        this.name = null;
    }
    
    // javadoc unnecessary
    public DatasourceMarshaller(String uri, int index, 
            String name) {
        this.uri = uri;
        this.index = index;
        this.name = name;
    }    
    
    // javadoc inherited
    public boolean isExtension(int arg0) {
        return false;
    }
    
    // javadoc inherited
    public boolean isPresent(IUnmarshallingContext iUnmarshallingContext)
            throws JiBXException {
        boolean result = false;
        UnmarshallingContext ctx = (UnmarshallingContext) iUnmarshallingContext;
        if (!ctx.isEnd()
                || ctx.isAt(ctx.getNamespace(), DataSourceType.JDBC_DATASOURCE
                        .toString())
                || ctx.isAt(ctx.getNamespace(),
                        DataSourceType.INTERNAL_POOL_DATASOURCE.toString())
                || ctx.isAt(ctx.getNamespace(), DataSourceType.JNDI_DATASOURCE
                        .toString())) {
            result = true;
        }
        return result;
    }    
    
    // javadoc inherited
    public Object unmarshal(Object object, IUnmarshallingContext context)
            throws JiBXException {
        final DatasourceConfiguration datasource;
        final UnmarshallingContext ctx = (UnmarshallingContext) context;        
        
        final DataSourceType type = DataSourceType.literal(ctx.toStart());
        
        if (type == DataSourceType.JDBC_DATASOURCE) {
            datasource = unmarshalJDBCDatasource(ctx);
        } else if (type == DataSourceType.INTERNAL_POOL_DATASOURCE) {
            datasource = unmarshalInternalPoolDatasource(ctx);
        } else if (type == DataSourceType.JNDI_DATASOURCE) {
            datasource = unmarshalJNDIDatasource(ctx);
        } else {
            throw new JiBXException(EXCEPTION_LOCALIZER
                    .format("invalid-datasource-type"));
        }
        return datasource;
    }
    
    /**
     * Unmarshal a Internal Pool datasource
     * 
     * @param ctx umnarshalling context
     * @return a Internal Pool datasource
     * @throws JiBXException if unmarshalling failed
     */
    private DatasourceConfiguration unmarshalInternalPoolDatasource(
            UnmarshallingContext ctx) throws JiBXException {
        
        InternalPoolDatasource source = new InternalPoolDatasource();

        ctx.parsePastStartTag(ctx.getNamespace(),
                INTERNAL_POOL_DATASOURCE_TAG_NAME);

        source.setDriverClass(getTagsTextContent(ctx, DRIVER_CLASS_TAG_NAME));
        source.setUrl(getTagsTextContent(ctx, URL_TAG_NAME));
        source.setUsername(getTagsTextContent(ctx, USERNAME_TAG_NAME));
        source.setPassword(getTagsTextContent(ctx, PASSWORD_TAG_NAME));
        source.setMaxActive(getTagsTextContent(ctx, MAX_ACTIVE_TAG_NAME));
        source.setMaxIdle(getTagsTextContent(ctx, MAX_IDLE_TAG_NAME));
        source.setMaxWait(getTagsTextContent(ctx, MAX_WAIT_TAG_NAME));

        unmarshalName(ctx, source);
        
        ctx.toEnd();
        ctx.parsePastEndTag(ctx.getNamespace(),
                INTERNAL_POOL_DATASOURCE_TAG_NAME);

        return source;        
    }

    /**
     * Unmarshal a JNDI datasource
     * 
     * @param ctx umnarshalling context
     * @return a JNDI datasource
     * @throws JiBXException if unmarshalling failed
     */
    private DatasourceConfiguration unmarshalJNDIDatasource(
            UnmarshallingContext ctx) throws JiBXException {
        
        JNDIDatasource source = new JNDIDatasource();

        ctx.parsePastStartTag(ctx.getNamespace(),
                JNDI_DATASOURCE_TAG_NAME);

        source.setJndiName(getTagsTextContent(ctx, JNDI_NAME_TAG_NAME));

        unmarshalName(ctx, source);
        
        ctx.toEnd();
        ctx.parsePastEndTag(ctx.getNamespace(),
                JNDI_DATASOURCE_TAG_NAME);

        return source;        
    }

    /**
     * Unmarshal a JDBC datasource
     * 
     * @param ctx umnarshalling context
     * @return a JDBC datasource
     * @throws JiBXException if unmarshalling failed
     */
    private DatasourceConfiguration unmarshalJDBCDatasource(
            UnmarshallingContext ctx) throws JiBXException {
        
        JDBCDatasource source = new JDBCDatasource();
        ctx.parsePastStartTag(ctx.getNamespace(), 
                JDBC_DATASOURCE_TAG_NAME);
    
        source.setDriverClass(
                getTagsTextContent(ctx, DRIVER_CLASS_TAG_NAME));
        source.setConnectionString(
                getTagsTextContent(ctx, CONNECTION_STRING_TAG_NAME));
    
        unmarshalName(ctx, source);
        
        ctx.toEnd();
        ctx.parsePastEndTag(ctx.getNamespace(), 
                JDBC_DATASOURCE_TAG_NAME);   

        return source;        
    }

    
    // javadoc inherited
    public void marshal(Object arg0, IMarshallingContext context)
            throws JiBXException {
        MarshallingContext ctx = (MarshallingContext) context;
        DatasourceConfiguration dataSource = (DatasourceConfiguration) arg0;
        DataSourceType type = dataSource.getType();
        if (type == DataSourceType.JDBC_DATASOURCE) {
            JDBCDatasource jdbcDatasource = (JDBCDatasource) dataSource;
            ctx.startTag(index, JDBC_DATASOURCE_TAG_NAME);

            writeTagsTextContent(ctx, DRIVER_CLASS_TAG_NAME, 
                    jdbcDatasource.getDriverClass());
            writeTagsTextContent(ctx, CONNECTION_STRING_TAG_NAME, 
                    jdbcDatasource.getConnectionString());

            marshalName(index, ctx, jdbcDatasource.getName());
            
            ctx.endTag(index, JDBC_DATASOURCE_TAG_NAME);              
        } else if (type == DataSourceType.INTERNAL_POOL_DATASOURCE) {
            InternalPoolDatasource internalPoolDatasource = 
                (InternalPoolDatasource) dataSource;   
            ctx.startTag(index, INTERNAL_POOL_DATASOURCE_TAG_NAME);
            
            writeTagsTextContent(ctx, DRIVER_CLASS_TAG_NAME, 
                    internalPoolDatasource.getDriverClass());
            writeTagsTextContent(ctx, URL_TAG_NAME, 
                    internalPoolDatasource.getUrl());
            writeTagsTextContent(ctx, USERNAME_TAG_NAME, 
                    internalPoolDatasource.getUsername());
            writeTagsTextContent(ctx, PASSWORD_TAG_NAME, 
                    internalPoolDatasource.getPassword());
            writeTagsTextContent(ctx, MAX_ACTIVE_TAG_NAME, 
                    internalPoolDatasource.getMaxActive());
            writeTagsTextContent(ctx, MAX_IDLE_TAG_NAME, 
                    internalPoolDatasource.getMaxIdle());
            writeTagsTextContent(ctx, MAX_WAIT_TAG_NAME, 
                    internalPoolDatasource.getMaxWait());

            marshalName(index, ctx, internalPoolDatasource.getName());
            
            ctx.endTag(index, INTERNAL_POOL_DATASOURCE_TAG_NAME);            
        } else if (type == DataSourceType.JNDI_DATASOURCE) {
            JNDIDatasource jndiDatasource = (JNDIDatasource) dataSource;
            ctx.startTag(index, JNDI_DATASOURCE_TAG_NAME);
            
            writeTagsTextContent(ctx, JNDI_NAME_TAG_NAME, 
                    jndiDatasource.getJndiName());

            marshalName(index, ctx, jndiDatasource.getName());
            
            ctx.endTag(index, JNDI_DATASOURCE_TAG_NAME);            
        } else {
            throw new JiBXException(EXCEPTION_LOCALIZER
                    .format("invalid-datasource-type"));
        }
    }
    
    /**
     * This is a utility method. It will advance to the start tag, parse the 
     * stat tag, parse the text contents of the tag, trim it and parse the end
     * tag. The trimmed text is returned.
     * 
     * @param ctx the unmarshalling context
     * @param tagName the name of the tag to be parsed
     * @return trimmed text content of the tag
     * @throws JiBXException if parsing failed
     */
    private String getTagsTextContent(UnmarshallingContext ctx, String tagName) 
        throws JiBXException {        
        
        ctx.toStart();
        ctx.parsePastStartTag(ctx.getNamespace(), tagName);
        final String result = ctx.parseContentText();
        ctx.toEnd();
        ctx.parsePastEndTag(ctx.getNamespace(), tagName);
        
        return result.trim();
    }
    
    /**
     * This method marshalls a tag with a string as its contents.
     * 
     * @param ctx marshalling context
     * @param tagName name of the tag
     * @param text content of the tag
     * @throws JiBXException if marshalling failed
     */
    private void writeTagsTextContent(MarshallingContext ctx, String tagName,
            String text) throws JiBXException {

        ctx.startTag(index, tagName);
        ctx.content(text);
        ctx.endTag(index, tagName);
       
    }
    
    /**
     * Marshals datasource name.
     * 
     * @param index index
     * @param context marshalling context
     * @param datasource name
     * @throws JiBXException in case of error in marshalling process
     */
    protected abstract void marshalName(int index, MarshallingContext context,
            String name) throws JiBXException;
    
    /**
     * Unmarshal datasource name.
     * 
     * @param context unmarshalling context
     * @param datasource datasource configuration
     * @throws JiBXException in case of error in unmarshalling process
     */
    protected abstract void unmarshalName(UnmarshallingContext context,
            DatasourceConfiguration datasource) throws JiBXException;
}
