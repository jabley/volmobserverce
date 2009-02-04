initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                product("devices") {
                    subsystem("repository")
                }
            }

            subsystem("common")
            subsystem("localization")
            subsystem("repository")

            product("devices") {
                subsystem("device")
            }

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
            ref("eclipse-jface")
            ref("eclipse-swt")
            ref("eclipse-ui-workbench")

            ref("volantis-jdom")
            ref("volantis-xerces")
        }

        tests {
            subsystem("testtools")

            ref("eclipse-osgi")
        }
    }
}

populateProject {
    project, module ->

    module.getTarget("validate").required << addTarget("unpack-eclipse-native") {

        def productModule = project.getModule("product")
        def unpackTarget = productModule.getTarget("unpack-eclipse-native")
        unpackTarget.invoke(module);
    }
}
