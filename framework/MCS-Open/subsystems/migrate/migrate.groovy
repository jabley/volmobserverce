initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("migrate-api")
        }

        impl {
            subsystem("repository")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }

            ref("regexp")
        }

        support {
            subsystem("testtools-runtime")
        }

        tests {
            product("synergetics") {
                subsystem("mock")
            }

            ref("volantis-xerces")

            execute {
                subsystem("architecture")
            }
        }
    }
}
