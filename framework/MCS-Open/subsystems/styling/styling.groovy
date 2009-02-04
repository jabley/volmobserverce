initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("repository")
            }

            subsystem("cornerstone")
            subsystem("localization")
            subsystem("model")

            product("synergetics") {
                subsystem("runtime")
            }
        }

        impl {
            subsystem("architecture")

            product("synergetics") {
                subsystem("cornerstone")
            }
        }

        tests {
            product("synergetics") {
                subsystem("testtools")
            }
        }
    }
}
