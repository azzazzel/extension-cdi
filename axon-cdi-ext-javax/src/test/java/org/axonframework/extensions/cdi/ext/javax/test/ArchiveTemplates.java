package org.axonframework.extensions.cdi.ext.javax.test;

import javax.enterprise.inject.spi.Extension;

import org.axonframework.extensions.cdi.ext.javax.AxonCdiExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ArchiveTemplates {

    private static Logger LOGGER = LoggerFactory.getLogger(ArchiveTemplates.class);

//    static File[] files = Maven.resolver().loadPomFromFile("pom.xml")
//            .importRuntimeDependencies().resolve().withTransitivity().asFile();

    public static WebArchive webArchive() {
        Class extension = AxonCdiExtension.class;
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "axon-cdi-generated.war")
                .addAsServiceProvider(Extension.class, extension)
                .addPackages(true, extension.getPackage())
                .deletePackages(true, ArchiveTemplates.class.getPackage())
//                .addAsLibraries(files)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return archive;
    }

    public static WebArchive webArchiveWithCdiExtension() {
        Class extension = AxonCdiExtension.class;
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "axon-cdi-generated-with-jar.war")
                .addAsLibrary(javaArchive());

//        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));

        return archive;
    }


    public static JavaArchive javaArchive() {
        Class extension = AxonCdiExtension.class;
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "axon-cdi-generated.jar")
                .addAsServiceProvider(Extension.class, extension)
                .addPackages(true, extension.getPackage())
                .deletePackages(true, ArchiveTemplates.class.getPackage())
                .addAsManifestResource("META-INF/beans.xml");

//        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));

        return archive;
    }

    public static void main(String[] args) {
        webArchiveWithCdiExtension();
    }

}