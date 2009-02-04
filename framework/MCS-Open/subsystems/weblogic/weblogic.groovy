initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("runtime")
        }
    }
}
