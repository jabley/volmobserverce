initializeProject {
    project, module ->

    dependencies {
        tests {
            subsystem("runtime")
            subsystem("servlet")

            product("devices") {
                subsystem("api")
            }

            product("synergetics") {
                subsystem("mock")
            }

            ref("commons-collections")

            execute {
                subsystem("xdime")
            }
        }
    }
}
