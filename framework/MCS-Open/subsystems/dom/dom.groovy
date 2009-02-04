initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("styling")
            }

            subsystem("cornerstone")
            subsystem("localization")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }
        }

        tests {
            ref("volantis-xerces")
        }
    }
}
