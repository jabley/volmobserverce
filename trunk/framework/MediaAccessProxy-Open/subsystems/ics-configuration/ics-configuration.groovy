initializeProject {
    project, module ->

    dependencies {
        api {
            product("synergetics") {
                subsystem("runtime")
            }

            ref("servlet-api")
        }

        impl {
            subsystem("localization")

            ref("jibx-run")
        }

        tests {
            subsystem("common")
        }
    }
}
