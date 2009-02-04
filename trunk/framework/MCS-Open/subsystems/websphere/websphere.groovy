initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("configuration")
            subsystem("localization")
            subsystem("project")
            subsystem("repository")
            subsystem("runtime")
            subsystem("servlet")

            product("pipeline") {
                subsystem("pipeline")
            }

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("repository")
            }

            pom(org: "our.w3c", name: "tidy", rev: "1.0")
            pom(org:"com.ibm", name:"ws", rev:"5.0.0.0")
            pom(org:"com.ibm", name:"wps", rev:"5.0.0.0")
            pom(org:"com.ibm", name:"jetspeed", rev:"1.5")

            ref("servlet-api")
        }
    }
}
