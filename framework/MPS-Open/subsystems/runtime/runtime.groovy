initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
	    
            product("mcs") {
                subsystem("charset")
                subsystem("runtime")
                subsystem("repository")
                subsystem("cornerstone")
                subsystem("servlet")
                subsystem("common")
                subsystem("configuration")
            }

            product("synergetics") {
                subsystem("repository")
            }

            product("pipeline") {
                subsystem("pipeline")
            }
	    
            ref("servlet-api")
	          ref("commons-pool")
	          ref("commons-collections")
            ref("smpp")
            ref("jaxen")
            ref("mail")
            ref("activation")
            ref("our-httpclient")
            ref("volantis-jdom")
            ref("volantis-xerces")

            execute{
                ref("hsqldb")
            }
        }

        tests {
      
            subsystem("testtools")

            product("synergetics") {
                subsystem("mock")
                subsystem("testtools")
            }

            product("mcs") {
                subsystem("testtools")
                subsystem("testtools-runtime")
            }
            execute {
                ref("jdom")
            }
        }
    }
}
