import com.volantis.grease.build.subsystem.dependencies.DependencyMapImpl
import com.volantis.grease.build.subsystem.dependencies.builder.DependencyMapBuilder

populateProject {
    project, module ->

    def product = project.properties.product;

    // Add a target that will unpack the eclipse native libraries into the
    // test/tests/output/lib/ directory that will be added to the
    // java.library.path system property passed to the junit runner.
    module.addTarget("unpack-eclipse-native") {
        target, otherModule ->

        def subsystem = otherModule.properties.subsystem;
        def tests = subsystem["tests"]
        def libDir = tests.outputDir.resolve("lib");

        def localDependencies = new DependencyMapImpl();
        def builder = new DependencyMapBuilder(
                localDependencies, product, locator, null);

        def swtNativeLibrary = builder.collection(id: "swt.native") {
            pom(org:"org.eclipse", name:"swt.native.linux", rev:"3.0.1")
        }

        def path = swtNativeLibrary.resolveExecutePath(
                project.properties.ivy.builder,
                "org.eclipse", "swt-native");

        def ant = module.antBuilder;
        path.each {
            file ->

            if (file.path.endsWith(".zip")) {
                ant.unzip(src: file, dest: libDir)
            } else {
                throw new IllegalStateException("Do not know how to handle $file")
            }
        }
    }

    // Add a target to resolve the execution path for a dependency.
    module.addTarget("resolve-dependency-path",
            depends: "+product/configure-ivy-settings") {
            target, id ->

        def subsystems = project.properties.subsystems;

        def productDependencies = subsystems.productDependencies;
        def dependency = productDependencies.getDependencyById(id)

        if (dependency == null) {
            throw new IllegalStateException("Could not find dependency with id: $id")
        }

        return dependency.resolveExecutePath(
		project.properties.ivy.builder,
                "com.volantis.mcs", "resolve-$id")
    }
}
