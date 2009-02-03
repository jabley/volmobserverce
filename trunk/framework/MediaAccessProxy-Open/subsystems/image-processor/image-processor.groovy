initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("ics-configuration")
            subsystem("localization")
            subsystem("operation-engine")

            product("synergetics") {
                subsystem("osgi-boot")
                subsystem("runtime")
            }

            ref("jai")
            ref("osgi")
            ref("osgi-services")
        }

        impl {
            subsystem("retriever")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("net")
            }

            ref("imageio")
            ref("servlet-api")
        }

        tests {

            product("synergetics") {
                subsystem("mock")
                subsystem("testtools")
            }
        }
    }
}

populateProject {
    project, module ->

    def junit = module.properties.junit;
    junit.excludes << "com/volantis/map/ics/imageprocessor/servlet/*"
}