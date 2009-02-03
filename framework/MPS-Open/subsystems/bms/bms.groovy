initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            subsystem("runtime")

            product("synergetics") {
                subsystem("runtime")
            }

            ref("servlet-api")
            ref("mail")
        }
        impl {
            ref("jibx-run")        
        }
    }
}
