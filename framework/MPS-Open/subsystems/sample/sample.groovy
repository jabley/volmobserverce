initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("bms")
            subsystem("localization")

            subsystem("runtime")

            product("mcs") {
                subsystem("runtime")
                subsystem("servlet")
            }
            ref("mail")
            ref("servlet-api")
        }
       
    }
}
