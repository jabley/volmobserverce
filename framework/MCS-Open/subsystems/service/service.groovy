initializeProject {
    project, module ->

    dependencies {
        api {
            product("synergetics") {
                subsystem("metadata")
                subsystem("runtime")
            }

            ref("servlet-api")
        }

        impl {
            subsystem("localization")
        }
    }
}
