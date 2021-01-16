package org.axonframework.extensions.cdi.javax.test;

import org.axonframework.extensions.cdi.javax.AxonProducers;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ArchiveTemplates {

    private static Logger LOGGER = LoggerFactory.getLogger(ArchiveTemplates.class);

//    static File[] files = Maven.resolver().loadPomFromFile("pom.xml")
//            .importRuntimeDependencies().resolve().withTransitivity().asFile();

    public static WebArchive webArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "axon-cdi-generated.war")
                .addPackages(true, AxonProducers.class.getPackage())
                .deletePackages(true, ArchiveTemplates.class.getPackage())
//                .addAsLibraries(files)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return archive;
    }

    public static WebArchive webArchiveWithCdiExtension() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "axon-cdi-generated-with-jar.war")
                .addAsLibrary(javaArchive());

//        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));

        return archive;
    }


    public static JavaArchive javaArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "axon-cdi-generated.jar")
                .addPackages(true, AxonProducers.class.getPackage())
                .deletePackages(true, ArchiveTemplates.class.getPackage())
                .addAsManifestResource("META-INF/beans.xml");

//        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));

        return archive;
    }

}