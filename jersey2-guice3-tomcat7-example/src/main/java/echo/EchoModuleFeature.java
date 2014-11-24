package echo;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import net.hogedriven.jersey.guice.GuiceModuleFeature;

@Provider
public class EchoModuleFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new GuiceModuleFeature(EchoModule.class));
        return true;
    }
}
