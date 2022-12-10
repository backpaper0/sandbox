package app;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

/*
 * CDIのライフサイクルイベントで起動処理。
 * JSR 346の11.5. Container lifecycle eventsを参照。
 *
 */
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
