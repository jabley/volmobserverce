initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("api")
            subsystem("charset")
            subsystem("common")
            subsystem("client")
            subsystem("localization")
            subsystem("runtime")
            subsystem("runtime-css")
            subsystem("service")
            subsystem("configuration")
            subsystem("cornerstone")
            subsystem("project")
            subsystem("repository")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("old-cache")
                subsystem("repository")
            }

            product("pipeline") {
                subsystem("pipeline")
            }

            ref("jsp-api")
            ref("servlet-api")
            ref("volantis-xerces")
            ref("our-httpclient")
            ref("volantis-jdom")
            ref("commons-lang")
            ref("commons-logging")
            ref("regexp")
            ref("jibx-run")
        }

        impl {
          execute {
          // XDIME is really part of the runtime dependencies but it is separate subsystem that depends on runtime 
          // so adding as a dependency of runtime would cause cycle. Therefore, runtime loads XDIME by reflection
          // which means that there is a dependency missing. Add it here as runtime is only ever used in servlet
          // so this will ensure that anything depending on servlet will also pick up xdime.
          subsystem("xdime")
          }
        }

        tests {
            subsystem("testtools")

            product("synergetics") {
                subsystem("mock")
            }
        }
    }
}

populateProject {
  project, module ->
    
    // Copy tld to output directory so it is easily available for installers
    module.getTarget("validate").depends << addTarget("copy-tld", visibility: "external", description: "Copies tld file") {
      def ant = module.antBuilder;
      def tldDir = project.resolve("built/output/tld")
      tldDir.mkdirs();
      ant.copy(toDir:tldDir.path, flatten:true) {
        fileset(dir:module.directory) {
          include(name:"**/*.tld")
        }
      }
    }
}
