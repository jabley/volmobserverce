initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            ref("servlet-api")
            ref("osgi")
            ref("osgi-services")
        }
    }
}
