import com.volantis.grease.build.subsystem.dependencies.DependencyMapImpl
import com.volantis.grease.build.subsystem.dependencies.builder.DependencyMapBuilder
import com.volantis.grease.build.subsystem.SubsystemScriptLoader

initializeProject {
    project, module ->
    def productModule = getModule("product")
    def product = project.properties.product
    def script = project.resolve("eclipse/eclipse.groovy");

    if (script.exists()) {
        println("Loading script: $script.path")
        def loader = new SubsystemScriptLoader(
                            workerAnt, pluginRegistry,
                            null,
                            null,
                            product, locator,
                            null);
        loader.loadScript(script.toURL(),
                          QName("eclipse-plugins"),
                          productModule);
    }
}

populateProject {                                                                           
project, module ->

    def installer_name = "mcs-installer"
    def packs = project.properties.installer_packs
    project.properties.installer_pack_list = ["main", "samples", "docs", "repository", "plugins"]

    def ant = module.antBuilder;
    def product = project.properties.product

    // regular expression used to find jars which should NOT be copied out when creating installer
    def provided_jars = /\/javax.servlet/    

    // filling body of target 'prepare-packs' is required
    module.getTarget("prepare-packs").body = {
        target, original ->

        // invoke original body:
        original.call()

        // ------------ Main pack -------------------
        // copy webapps
        ant.copy(toDir:packs.main.resolve("/webapps/mcs")) {
          fileset(dir:project.resolve("webapps/mcs"))
        }

        //copy taglibs
        ant.copy(toDir:packs.main.resolve("/webapps/mcs/WEB-INF/taglibs")){
          fileset(dir:product.dirs.built.resolve("/output/tld"))
        }

        // generate Scripts
        packs.main.resolve("/bin").mkdir()
        generateScripts(project, [name:"builder", subsystem: "packager", main:"com.volantis.mcs.builder.client.BuilderCLI", output:packs.main])
        generateScripts(project, [name:"mcsExport", subsystem:"cli", main:"com.volantis.mcs.cli.MarinerExport", output:packs.main, jdbc:true])
        generateScripts(project, [name:"mcsImport", subsystem:"cli", main:"com.volantis.mcs.cli.MarinerImport", output:packs.main, jdbc:true])
        generateScripts(project, [name:"mcsServerManager", subsystem:"cli", main:"com.volantis.mcs.cli.MarinerManager", output:packs.main])
        generateScripts(project, [name:"mcsVersion", subsystem:"common", main:"com.volantis.mcs.utilities.VolantisVersion", output:packs.main])
        generateScripts(project, [name:"prerenderer", subsystem:"packager", main:"com.volantis.mcs.prerenderer.client.PrerendererCLI", output:packs.main])

        //packager
        packs.main.resolve("/packager").mkdir()
        ant.copy(toDir:packs.main.resolve("/packager")) {
            fileset(dir:project.resolve("packager"))
        }
        //mss_store dir (for MPS) with empty create.me file
        packs.main.resolve("mss_store").mkdir()
        packs.main.resolve("mss_store/create.me").withWriter{}

        //--------------- Samples Pack ----------------
        ant.copy(toDir:packs.samples.resolve("samples")) {
            fileset(dir:project.resolve("samples"))
        }

        // ------------ Documentation Pack -------------------
        //java-docs
        ant.copy(toDir:packs.docs.resolve("documentation/volantis/java-docs")){
          fileset(dir:product.dirs.built.resolve("docs-dist")) {
            include(name:"**")
          }
        }

        //--------------- Repository ---------------        
        ant.copy(toDir:packs.repository.resolve("repository")){
          fileset(dir:project.resolve("/repository"))
        }
        ant.copy(toDir:packs.repository.resolve("repository/jdbc-repository")){
          fileset(dir:product.dirs.built.resolve("/output/dbschema/sql")) {
              include(name:"**/*.sql")              
          }
        }
    }

    module.getTarget("installer-mcs-installer").required << addTarget("prepare-installer-mcs-installer") {

        // set properties used for filtering by IzPack Plugin
        project.properties.installer_properties = [
          "installer.product.revision" : product.revision
        ]

        def installer_packs = module.getTarget("copy-packs-for-$installer_name").invoke()
        project.properties.installer_copied_mcs_installer_packs = installer_packs  
        project.properties.installer_file = "MCS"


        // copy only required jars to both webapp and lib directories         
        def webapp_jars = getSubsystemsDependencies(project, provided_jars, ["servlet", "weblogic"])
        webapp_jars.each {
          ant.copy(file:it, toDir :installer_packs.main.resolve("/webapps/mcs/WEB-INF/lib"))
        }
         
        def lib_jars = getSubsystemsDependencies(project, null, ["cli", "packager"])
        lib_jars.each {
          ant.copy(file:it, toDir :installer_packs.main.resolve("/lib"))
        }
       
        //--------------- Documentation ---------------
        ant.copy(toDir:installer_packs.docs.resolve("/documentation")){
          fileset(dir:project.resolve("documentation")) {
            include(name:"volantis/manuals/**")
          }
        }

        //retrieve devices.mdpr from Ivy repository
        def localDependencies = new DependencyMapImpl();
        def builder = new DependencyMapBuilder(localDependencies, null, locator, null);
        def devicesLibrary = builder.collection(id: "devices") {
          pom(org: "com.volantis", name: "devices.open", rev: product.revision)
        }
        def devices_path = devicesLibrary.resolveExecutePath(project.properties.ivy.builder, "com.volantis", "devices")
        devices_path.each {
          ant.copy(file:it, toFile:installer_packs.repository.resolve("/repository/devicerepository/devices.mdpr"))
        }
    }
}

def generateScripts(project, Map params) {
    generateScript(project, params)
    generateScriptBat(project, params)
}

def generateScript(project, Map params) {
    subsystem = project.properties.subsystems[params["subsystem"]]
    if (subsystem) {
        def script = params["output"].resolve("/bin/${params["name"]}")
        script.withWriter {
            out ->
            out.write("""#!/bin/bash
#-----------------------------------------------------------------------------

""")
            if (params["jdbc"]) {
                out.write("""JDBC_DRIVER=/path/to/jdbcdriver.jar

""")
            }
            out.write("""
CLASSPATH=${subsystem.mainRuntimePath.collect {file -> "../lib/${file.name}"}.join(":")}""")

            if (params["jdbc"]) {
                out.write(""":\$JDBC_DRIVER""")
            }
            out.write("""

if [ "\$1" == "-debug" ]; then
    DEBUG_ARGS="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y"
    shift
else
    DEBUG_ARGS=""
fi

java -cp \${CLASS_PATH}:\${CLASSPATH} \${DEBUG_ARGS} ${params.main} \${1+"\$@"}
""")
        println "Generated " + script.path
        }
    }
}

def generateScriptBat(project, Map params) {
    subsystem = project.properties.subsystems[params["subsystem"]]
    if (subsystem) {
        def script = params["output"].resolve("/bin/${params["name"]}.bat")
        script.withWriter {
            out ->
            if (params["jdbc"]) {
                out.write("""set JDBC_DRIVER=\\path\\to\\jdbcdriver.jar""")
            }    
            out.write("""
set CLASSPATH=${subsystem.mainRuntimePath.collect {file -> "..\\lib\\${file.name}"}.join(";")}""")
            if (params["jdbc"]) {
                out.write(""";%JDBC_DRIVER%""")
            }
            out.write("""
java ${params["main"]} %*
""")
        }                
        println "Generated " + script.path
    }
}
