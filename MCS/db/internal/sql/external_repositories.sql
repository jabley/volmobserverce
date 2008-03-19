insert into EXTERNALREPOSITORYDEFINITION ( REPOSITORYNAME, ATTRIBUTENAME,
ATTRIBUTEVALUE, REPOSITORYTYPE, REVISION ) values ( 'DynamoPrefs', 'PluginClassName', '
com.volantis.mcs.repository.plugins.DynamoPolicyPreferencesRepositoryPlugin', 2, 0 );
insert into 
EXTERNALREPOSITORYDEFINITION ( REPOSITORYNAME, ATTRIBUTENAME,
ATTRIBUTEVALUE, REPOSITORYTYPE, REVISION ) values ( 'WLPSPrefs', 'PluginClassName', '
com.volantis.mcs.repository.plugins.WeblogicPolicyPreferencesRepositoryPlugin', 2, 0 ); 
insert into volantis_tabs values ('ExternalRepositoryDefinition');
commit;
