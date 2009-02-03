// =============================================================================
//     Standard Java API Jars
// =============================================================================

pom(id: "servlet-api", org: "javax", name: "servlet", rev: "2.3")
pom(id: "jdo2-api", org: "javax", name: "jdo", rev: "2.0")
pom(id: "osgi", org: "org.eclipse", name: "equinox.osgi", rev: "3.3.1.1")
pom(id: "osgi-services", org: "org.eclipse", name: "equinox.osgi.services", rev: "3.1.200")

// =============================================================================
//     Volantis Repackaged 3rd Party Jars
// =============================================================================

pom(id: "volantis-xerces", org: "com.volantis.apache", name: "xerces", rev: "2.6.2")
pom(id: "volantis-xalan", org: "com.volantis.apache", name: "xalan", rev: "2.6.0")

// =============================================================================
//     Volantis Modified 3rd Party Jars
// =============================================================================

pom(id: "volantis-jdom", org: "com.volantis.jdom", name: "api", rev: "0.9")

// =============================================================================
//     Apache 3rd Party Jars
// =============================================================================

pom(id: "apache-xerces", org:"org.apache", name:"xerces", rev:"2.8.1")
pom(id: "bcel", org: "org.apache", name: "bcel", rev: "5.2");
pom(id: "commons-httpclient", org: "org.apache", name: "commons.httpclient", rev: "3.0.1")
pom(id: "commons-dbcp", org: "org.apache", name: "commons.dbcp", rev: "1.2.1")
pom(id: "log4j", org: "org.apache", name: "log4j", rev: "1.2.8")

// =============================================================================
//     Other 3rd Party Jars
// =============================================================================

pom(id: "openarm40", org: "net.m2technologies", name: "openarm40", rev: "0.009")
pom(id: "jibx-run", org: "org.jibx", name: "api", rev: "1.1.5")
pom(id: "junit", org: "org.junit", name: "api", rev: "3.8.2")
pom(id: "junit.addons", org: "net.sourceforge", name: "junit.addons", rev: "1.4")
pom(id: "arm40", org: "com.volantis.opengroup", name: "arm40", rev: "4.0")
pom(id: "httpunit", org: "com.meterware", name: "httpunit", rev: "1.4.5")
pom(id: "xmlunit", org: "org.custommonkey", name: "xmlunit", rev: "1.0")
pom(id: "hsqldb", org: "org.hsqldb", name: "runtime", rev: "1.8.0.7")
pom(id: "jpox", org: "org.jpox", name: "runtime", rev: "1.1.7.patched")

// =============================================================================
//     Build Dependencies
// =============================================================================

collection(id: "common-test-support") {
    ref("junit")
    ref("junit.addons")

    product("testtools") {
        subsystem("mock", id: "volantis-mock")
    }
}

collection(id: "jpox-enhancer-library") {
    pom(org: "org.jpox", name: "enhancer", rev: "1.1.7")
}

collection(id: "volantis-mock-task") {
    product("testtools") {
        subsystem("mock-generator", id: "volantis-mock-generator")
    }
}

// =============================================================================
//     Custom Modules
// =============================================================================
