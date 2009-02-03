import org.apache.ivy.core.module.id.ModuleRevisionId

// Generate eclipse plugins

populateProject {
project, module ->
    def product = project.properties.product
    def ant = module.antBuilder;
    def srcdir = project.resolve("eclipse")
    def packs = project.properties.installer_packs
  
    def properties = [
        "version.plugins.mcs":product.version,
        "version.plugins.ucp":product.version,

        "build.version.eclipse":"3.0.1",
        "build.version.mcs":product.revision
    ]

   def plugins = ["common",
    "eclipse-common",
    "eclipse-validation",
    "eclipse-controls",
    "eclipse-builder",
    "eclipse-ab",]

    properties.each {
        ant.property(name: it.key, value: it.value)
    }

    module.getTarget("prepare-packs").body = {
        target, original ->

        // invoke original body:
        original.call()

        //--------------------------------------------------------------------------------------------------------------
        // Features
        //--------------------------------------------------------------------------------------------------------------
        srcdir.resolve("features").eachDir {
            dir ->
            smartAntCopy(ant,
                         dir,
                         packs.plugins.resolve("eclipse/features/${dir.name}_${properties["version.plugins.mcs"]}"))
        }
        // write empty .eclipseextension file so directory can be used as Eclipse Exxtension  
        packs.plugins.resolve("eclipse/.eclipseextension").withWriter{}

        //--------------------------------------------------------------------------------------------------------------
        // Plugins
        //--------------------------------------------------------------------------------------------------------------
        plugins.each {
        es ->
            def pluginloc =  "plugins/com.volantis.mcs.${es.replace("-", ".")}"
            def pluginsrc  = srcdir.resolve(pluginloc)
            def plugindest = packs.plugins.resolve("eclipse/${pluginloc}_${properties["version.plugins.mcs"]}")

            smartAntCopy(ant, pluginsrc, plugindest)
            smartAntCopy(ant, srcdir.resolve("${pluginloc}.nl1"), packs.plugins.resolve("eclipse/${pluginloc}.nl1_${properties["version.plugins.mcs"]}"))

        }       
        // eclipse-ucp is treated differently
        smartAntCopy(ant,
                srcdir.resolve("plugins/com.volantis.mcs.ibm.ucp"),
                packs.plugins.resolve("eclipse/plugins/com.volantis.mcs.ibm.ucp_${properties["version.plugins.ucp"]}"))        

        //Plugin Documentation
        def docdir = "com.volantis.mcs.eclipse.doc"
        smartAntCopy(ant,
                     project.resolve("documentation/volantis/eclipse/plugins/${docdir}"),
                     packs.plugins.resolve("eclipse/plugins/${docdir}_${properties["version.plugins.mcs"]}"))
        }
        

    module.getTarget("prepare-installer-mcs-installer").body = {
        target, original ->
        // invoke original body:
        original.call()

        module.getTarget("prepare-plugins-jars").invoke()

    }
    module.addTarget("prepare-plugins-jars") {

        //--------------------------------------------------------------------------------------------------------------
        // jars
        //--------------------------------------------------------------------------------------------------------------
        // order matters !
        def provided_jars = /org.eclipse/
        def installer_packs = project.properties.installer_copied_mcs_installer_packs 

        def generatePlugin = {
          pluginsrc, plugindest, es ->
          def xmlfrag    = ""
          def jars = getSubsystemsDependencies(module, provided_jars, [es])
          jars.each {
              provided_jars += "|" + it.name
              ant.copy(file:it,
                       toDir:plugindest)
              xmlfrag += """
      <library name="${it.name}">
          <export name="*"/>
      </library>
      """
          }
          ant.property(name:"libraries-${es}", value:"<runtime>\n${xmlfrag}\n</runtime>")
          smartAntCopy(ant, pluginsrc, plugindest)
        }

        plugins.each {
        pluginsubsystem ->
            def pluginloc =  "plugins/com.volantis.mcs.${pluginsubsystem.replace("-", ".")}"
            generatePlugin(packs.plugins.resolve("eclipse/${pluginloc}_${properties["version.plugins.mcs"]}"), 
                           installer_packs.plugins.resolve("eclipse/${pluginloc}_${properties["version.plugins.mcs"]}"),
                           pluginsubsystem)
        }
        
        // eclipse-ucp is treated differently        
        def pluginloc =  "plugins/com.volantis.mcs.ibm.ucp"        
        generatePlugin(packs.plugins.resolve("eclipse/${pluginloc}_${properties["version.plugins.mcs"]}"),
                installer_packs.plugins.resolve("eclipse/${pluginloc}_${properties["version.plugins.mcs"]}"),
                "eclipse-ucp")
    }

    module.addTarget("eclipse-plugins", visibility: "external",
            depends: ["+prepare-packs", "prepare-plugins-jars"],
            description: "Prepares full eclipse plugins pack with jars")
}

// it would be cool to move it somewhere and make it available outside from here
def smartAntCopy(ant, from, to) {
    ant.copy(toDir:to, overwrite: true) {
        fileset(dir:from) {
            exclude(name:"**/*.jpg")
            exclude(name:"**/*.gif")
            exclude(name:"**/*.png")
            exclude(name:"**/*.psd")
            exclude(name:"**/*.pdf")
        }
        filterchain{expandproperties()}
    }
    ant.copy(toDir:to, overwrite: true) {
        fileset(dir:from) {
            include(name:"**/*.jpg")
            include(name:"**/*.gif")
            include(name:"**/*.png")
            include(name:"**/*.psd")
            include(name:"**/*.pdf")
        }
    }
}