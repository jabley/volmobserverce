initializeProject {
    project, module ->

    dependencies {
        api {
            pom(org: "org.junit", name: "api", rev: "3.8.2")
        }
    }
}

populateProject {
    project, module ->

    def subsystems = project.properties.subsystems;
    def generatorSubsystem = subsystems["mock-generator"]
    def generatorModule = generatorSubsystem.module;

    // Make sure that the mock generator is compiled before the mock task is
    // defined.
    def generateSupport = module.getTarget("define-volantis-mock-task");
    generateSupport.depends << generatorModule.getTarget("prepare-impl")
}