initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            product("synergetics") {
                subsystem("runtime")
            }
        }

        tests {
            product("synergetics") {
                subsystem("testtools")
            }
        }
    }
}
