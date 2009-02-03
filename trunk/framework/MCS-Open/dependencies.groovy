// =============================================================================
//     Standard Java API Jars
// =============================================================================

pom(id: "mail", org: "javax", name: "mail", rev: "1.3.2")
pom(id: "activation", org: "javax", name: "activation", rev: "1.0.2")
pom(id: "servlet-api", org: "javax", name: "servlet", rev: "2.3")
pom(id: "sun-ccpp", org: "com.sun", name: "ccpp", rev: "1.0")
pom(id: "jsp-api", org: "javax", name: "servlet.jsp", rev: "1.2")

pom(id: "jai", org: "javax", name: "media.jai", rev: "1.1.3")
pom(id: "jai-codec", org: "com.sun", name: "media.jai.codec", rev: "1.1.3")

// =============================================================================
//     Volantis Repackaged 3rd Party Jars
// =============================================================================

collection(id: "volantis-xerces") {
    pom(org: "com.volantis.apache", name: "xerces", rev: "2.6.2")
}
pom(id: "our-httpclient", org: "our.apache", name: "commons.httpclient", rev: "2.0.2")
pom(id: "our-commons-logging", org: "our.apache", name: "commons.logging", rev: "1.0.2")
pom(id: "our-digester", org: "our.apache", name: "commons.digester", rev: "1.3")
pom(id: "our-beanutils", org: "our.apache", name: "commons.beanutils", rev: "1.5")
//
// =============================================================================
//     Volantis Modified 3rd Party Jars
// =============================================================================

pom(id: "volantis-jdom", org: "com.volantis.jdom", name: "api", rev: "0.9")

// =============================================================================
//     Apache 3rd Party Jars
// =============================================================================

pom(id: "ant", org: "org.apache", name: "ant", rev: "1.7.0")
pom(id: "log4j", org: "org.apache", name: "log4j", rev: "1.2.8")
pom(id: "apache-xerces", org:"org.apache", name:"xerces", rev:"2.8.1")
pom(id: "commons-collections", org: "org.apache", name: "commons.collections", rev: "2.1")

pom(id: "commons-cli", org: "org.apache", name: "commons.cli", rev: "1.0")
pom(id: "regexp", org: "org.apache", name: "jakarta.regexp", rev: "1.4")
pom(id: "commons-lang", org: "org.apache", name: "commons.lang", rev: "2.0")
pom(id: "commons-logging", org: "org.apache", name: "commons.logging", rev: "1.0.4")
pom(id: "commons-httpclient", org: "org.apache", name: "commons.httpclient", rev: "3.0.1")
pom(id: "commons-io", org: "org.apache", name: "commons.io", rev: "1.2")

// =============================================================================
//     Eclipse 3rd Party Jars
// =============================================================================

collection(org: "org.eclipse") {

    collection(rev: "3.0.1") {
        pom(id: "eclipse-core-resources", name: "core.resources")
        pom(id: "eclipse-core-runtime", name: "core.runtime")
        pom(id: "eclipse-swt", name: "swt")
        pom(id: "eclipse-ui-workbench", name: "ui.workbench")
        pom(id: "eclipse-ui-workbench-texteditor", name: "ui.workbench.texteditor")
        pom(id: "eclipse-ui-editors", name: "ui.editors")
        pom(id: "eclipse-ui-ide", name: "ui.ide")
        pom(id: "eclipse-jface-text", name: "jface.text")
        pom(id: "eclipse-text", name: "text")
        pom(id: "eclipse-search", name: "search")
        pom(id: "eclipse-osgi", name: "osgi")
    }

    collection(rev: "3.0.0") {
        pom(id: "eclipse-jface", name: "jface")
        pom(id: "eclipse-ui-forms", name: "ui.forms")
        pom(id: "eclipse-ui-views", name: "ui.views")
    }
}

// =============================================================================
//     Other 3rd Party Jars
// =============================================================================

pom(id: "jibx-run", org: "org.jibx", name: "api", rev: "1.1.5")
pom(id: "junit", org: "org.junit", name: "api", rev: "3.8.2")
pom(id: "junit.addons", org: "net.sourceforge", name: "junit.addons", rev: "1.4")
pom(id: "hsqldb", org: "org.hsqldb", name: "runtime", rev: "1.8.0.7")

pom(id: "jaxen", org: "org.codehaus", name: "jaxen", rev: "1.0-FCS")
pom(id:"ucp", org: "com.ibm", name: "ucp", rev: "2.0")

// =============================================================================
//     Commercial 3rd Party Jars
// =============================================================================


//
// =============================================================================
//     Build Collections
// =============================================================================

collection(id: "common-test-support") {
    ref("junit")
    ref("junit.addons")

    product("synergetics") {
        subsystem("testtools")
    }
}

collection(id: "volantis-mock-task") {
    product("testtools") {
        subsystem("mock-generator", id: "volantis-mock-generator")
    }
}

// =============================================================================
//     Custom Collections
// =============================================================================

