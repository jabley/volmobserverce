initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("runtime")
            subsystem("servlet")

            pom(org: "com.atg", name: "dspjsp", rev: "5.1.1")

            ref("servlet-api")
            ref("jsp-api")
        }
    }
}
