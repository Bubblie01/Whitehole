import java.net.URL

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
}

val objectdb = file("objectdb.xml")
if (!objectdb.exists()) {
    objectdb.outputStream().use { objout ->
        URL("https://github.com/IonicPixels/Whitehole-Objectdb/raw/main/objectdb.xml").openConnection().run {
            connect()
            getInputStream().use { it.copyTo(objout) }
        }
    }
}

dependencies {
    implementation("com.google.guava", "guava", "30.1.1-jre")
    implementation("org.jogamp.jogl", "jogl-all", "2.3.2")
    implementation("org.jogamp.gluegen", "gluegen-rt", "2.3.2")
    implementation("org.jdom", "jdom2", "2.0.6.1")
    implementation("jaxen", "jaxen", "1.2.0")
    implementation("xerces", "xercesImpl", "2.12.2")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.1")
}

application {
    mainClass.set("com.thesuncat.whitehole.Whitehole")
}

sourceSets {
    main {
        java { srcDirs("src") }
        resources { srcDirs("resources") }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}