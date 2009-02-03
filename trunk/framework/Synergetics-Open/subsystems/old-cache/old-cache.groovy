initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
        }

        tests {
            subsystem("runtime")
        }
    }
}
