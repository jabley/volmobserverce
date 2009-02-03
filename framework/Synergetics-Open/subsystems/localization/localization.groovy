initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                ref("log4j")
            }
            ref("servlet-api")
        }
    }
}
