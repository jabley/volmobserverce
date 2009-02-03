import org.grease.path.Path
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.plugins.parser.ModuleDescriptorParserRegistry
import org.apache.tools.ant.BuildException
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.grease.groovy.ivy.IvyBuilder
import org.grease.ivy.IvyPattern

populateProject {
project, module ->

    def installer_name = "mps-installer"
    def packs = project.properties.installer_packs

    def ant = module.antBuilder;
    def product = project.properties.product    
    def packsdir = product.dirs.built.resolve("/output/packs");

    // filling body of target 'prepare-packs' is required 
    module.getTarget("prepare-packs").body = {
        target, original ->

        // invoke original body:
        original.call()

        ["mps", "samples", "docs"].each {
            pack ->
            def file  = packsdir.resolve("${pack}")
            file.mkdirs()
            packs[pack] = file
        }

      //---------------Main (MPS) pack---------------
      ant.copy(toDir:packs.mps.resolve("webapps/mcs/WEB-INF")) {
        fileset(dir:project.resolve("webapps/mcs/WEB-INF")) {
            include(name: "mss*.xml")
        }
      }

      //--------------- Samples Pack ----------------
      //copy HTML files to webapps
      ant.copy(toDir:packs.samples.resolve("webapps/mcs")) {
        fileset(dir:project.resolve("webapps/mcs")) {
            include(name: "**/*.html")
        }
      }
   
      //--------------- Documentation Pack ----------------      
      ant.copy(toDir:packs.docs.resolve("/documentation/volantis")){
        fileset(dir:product.dirs.built.resolve("docs-dist")) {
          include(name:"**")
        }
      }
      
    }

    module.getTarget("installer-$installer_name").required << addTarget("prepare-installer-$installer_name") {                        
        def outputdir = product.dirs.built.resolve("/installers/$installer_name")
        def installer_packs =[:]
        packs.each {
            pack ->
            installer_packs[pack.key] = outputdir.resolve("packs/$pack.key")
        }

        // copy artifact packs to this installer's directory
        ant.copy(toDir:outputdir.resolve("packs")) {
            fileset(dir:packsdir)
        }

        // set properties used for filtering by IzPack Plugin
        project.properties.installer_properties = [ 
          "installer.product.revision" : product.revision
        ]

        // regular expression used to find jars which should NOT be copied out when creating installer
        // in general: all the versions of servlet-api
        def provided_jars = /\/javax.servlet/       

        //--------------- Main MPS Pack ----------------
        // add jars to packs
        def mps_jars = getSubsystemsDependencies(module, provided_jars, ["sample"])        
        def mps_libdir = installer_packs.mps.resolve("webapps/mcs/WEB-INF/lib")
        mps_libdir.mkdirs()
        (mps_jars).each {
            ant.copy(file:it, toDir:mps_libdir)
        }

        //--------------- Documentation Pack ----------------
        // add pdfs to javadocs
        ant.copy(toDir:installer_packs.docs.resolve("/documentation")){
            fileset(dir:project.resolve("documentation")) {
                include(name:"volantis/manuals/**")
            }
        }
    }    
}
