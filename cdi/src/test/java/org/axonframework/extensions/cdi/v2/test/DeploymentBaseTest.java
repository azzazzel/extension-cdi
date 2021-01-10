package org.axonframework.extensions.cdi.v2.test;

import jakarta.enterprise.inject.spi.Extension;
import org.axonframework.extensions.cdi.v2.AxonCdiExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class DeploymentBaseTest {

    private static Logger LOGGER = LoggerFactory.getLogger(DeploymentBaseTest.class);

//    static File[] files = Maven.resolver().loadPomFromFile("pom.xml")
//            .importRuntimeDependencies().resolve().withTransitivity().asFile();

    @Deployment(name = "v1")
    public static WebArchive createDeployment() {
        Class extension = AxonCdiExtension.class;
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsServiceProvider(Extension.class, extension)
                .addPackages(true, extension.getPackage())
                .deletePackages(true, DeploymentBaseTest.class.getPackage())
//                .addAsLibraries(files)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        LOGGER.debug("Making archive with following content:\n" + archive.toString(Formatters.VERBOSE));

        return archive;
    }

}
