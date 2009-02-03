initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("common")
            }
            subsystem("localization")

            product("synergetics") {
                subsystem("runtime")
            }
        }

        impl {
            subsystem("image-processor")

            product("synergetics") {
                subsystem("descriptor-store")
            }
        }

        tests {
            product("synergetics") {
                subsystem("testtools")
            }
        }
    }
}
