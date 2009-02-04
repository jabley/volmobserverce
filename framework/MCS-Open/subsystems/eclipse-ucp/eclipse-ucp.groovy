initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("repository")

            product("devices") {
                subsystem("repository")
            }

            product("synergetics") {
                subsystem("repository")
            }

            ref("ucp")
            ref("servlet-api")
        }

        tests {
            subsystem("testtools")

            product("synergetics") {
                subsystem("cornerstone")
            }

            execute {
                ref("apache-xerces")
                ref("hsqldb")
            }
        }
    }
}
