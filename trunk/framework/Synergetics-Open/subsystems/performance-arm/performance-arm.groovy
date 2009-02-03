initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("performance")

            ref("arm40")
            ref("openarm40")
        }
    }
}
