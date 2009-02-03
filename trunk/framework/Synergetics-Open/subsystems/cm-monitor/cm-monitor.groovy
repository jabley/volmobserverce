initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("directory-monitor")
            subsystem("localization")

            ref("osgi")
            ref("osgi-services")
        }
    }
}
