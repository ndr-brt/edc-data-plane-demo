package org.eclipse.dataspaceconnector.plane.control;

import org.eclipse.dataspaceconnector.dataloading.AssetLoader;
import org.eclipse.dataspaceconnector.spi.WebService;
import org.eclipse.dataspaceconnector.spi.contract.offer.store.ContractDefinitionStore;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.Inject;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;

public class ControlPlaneExtension implements ServiceExtension {

    @Inject
    private WebService webService;

    @Inject
    private AssetLoader assetLoader;

    @Inject
    private ContractDefinitionStore contractDefinitionStore;

    @Override
    public String name() {
        return "Control Plane";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        Monitor monitor = context.getMonitor();
        webService.registerResource("default", new AssetController(monitor, assetLoader));
        webService.registerResource("default", new ContractDefinitionApiController(monitor, contractDefinitionStore));
    }
}
