initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("configuration")
            subsystem("repository")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("repository")
            }

            ref("jibx-run")
        }

        tests {
            product("synergetics") {
                subsystem("testtools")
            }
        }
    }
}
