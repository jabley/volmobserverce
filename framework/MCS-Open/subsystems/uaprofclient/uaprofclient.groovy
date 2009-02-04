initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("repository")

            ref("sun-ccpp")
        }
    }
}
