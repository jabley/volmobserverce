initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            ref("jibx-run")
            ref("jdo2-api")
            ref("volantis-jdom")
            ref("servlet-api")
        }
    }
}
