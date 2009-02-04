initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            ref("our-digester")
            ref("commons-lang")
        }
    }
}
