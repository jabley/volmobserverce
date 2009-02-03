initializeProject {
    project, module ->

    dependencies {
        api {
            ref("jpox")
            ref("osgi")
        }
    }
}
