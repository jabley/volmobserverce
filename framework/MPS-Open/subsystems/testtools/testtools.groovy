initializeProject {
    project, module ->

    dependencies {
        api {
            product("mcs") {
                subsystem("runtime")
                subsystem("testtools-runtime")
                subsystem("testtools")
            }

            product("synergetics") {
                subsystem("testtools")
            }
        }

    }
}
