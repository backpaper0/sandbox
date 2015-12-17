package app;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class CdiStartup implements Extension {

    public void handle(@Observes BeforeBeanDiscovery event) {
        Logs.add("BeforeBeanDiscovery");
    }

    public void handle(@Observes AfterTypeDiscovery event) {
        Logs.add("AfterTypeDiscovery");
    }

    public void handle(@Observes AfterDeploymentValidation event) {
        Logs.add("AfterDeploymentValidation");
    }

    public void handle(@Observes AfterBeanDiscovery event) {
        Logs.add("AfterBeanDiscovery");
    }
}
