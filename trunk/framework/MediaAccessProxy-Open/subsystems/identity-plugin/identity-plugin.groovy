initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("operation-engine")

            ref("servlet-api")
        }
    }
}
