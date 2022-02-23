package org.eclipse.dataspaceconnector.plane.control;

import org.eclipse.dataspaceconnector.dataloading.AssetLoader;
import org.eclipse.dataspaceconnector.spi.WebService;
import org.eclipse.dataspaceconnector.spi.asset.AssetIndex;
import org.eclipse.dataspaceconnector.spi.asset.DataAddressResolver;
import org.eclipse.dataspaceconnector.spi.contract.offer.store.ContractDefinitionStore;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.Inject;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.observe.TransferProcessListener;
import org.eclipse.dataspaceconnector.spi.transfer.observe.TransferProcessObservable;
import org.eclipse.dataspaceconnector.spi.transfer.provision.ResourceDefinitionGenerator;
import org.eclipse.dataspaceconnector.spi.transfer.provision.ResourceManifestGenerator;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.ResourceDefinition;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.eclipse.dataspaceconnector.transfer.sync.provider.provision.HttpProviderProxyResourceDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static java.lang.String.format;

public class ControlPlaneExtension implements ServiceExtension {

    @Inject
    private WebService webService;

    @Inject
    private AssetLoader assetLoader;

    @Inject
    private ContractDefinitionStore contractDefinitionStore;

    @Inject
    private TransferProcessStore transferProcessStore;

    @Inject
    private TransferProcessObservable transferProcessObservable;

    @Inject
    private ResourceManifestGenerator resourceManifestGenerator;

    @Override
    public String name() {
        return "Control Plane";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        Monitor monitor = context.getMonitor();
        webService.registerResource("default", new AssetController(monitor, assetLoader));
        webService.registerResource("default", new ContractDefinitionApiController(monitor, contractDefinitionStore));
        webService.registerResource("default", new TransferController(monitor, transferProcessStore));

        transferProcessObservable.registerListener(new LoggerTransferProcessListener(monitor));

//        resourceManifestGenerator.registerConsumerGenerator(new HttpConsumerProxyResourceGenerator());
    }

    private static class LoggerTransferProcessListener implements TransferProcessListener {
        private final Monitor monitor;

        public LoggerTransferProcessListener(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public void created(TransferProcess process) {
            monitor.info(format("Transfer process %s created", process.getId()));
        }

        @Override
        public void provisioning(TransferProcess process) {
            monitor.info(format("Transfer process %s provisioning", process.getId()));
        }

        @Override
        public void provisioned(TransferProcess process) {
            monitor.info(format("Transfer process %s provisioned", process.getId()));
        }

        @Override
        public void inProgress(TransferProcess process) {
            monitor.info(format("Transfer process %s inProgress", process.getId()));
        }

        @Override
        public void completed(TransferProcess process) {
            monitor.info(format("Transfer process %s completed", process.getId()));
        }

        @Override
        public void deprovisioning(TransferProcess process) {
            monitor.info(format("Transfer process %s deprovisioning", process.getId()));
        }

        @Override
        public void deprovisioned(TransferProcess process) {
            monitor.info(format("Transfer process %s deprovisioned", process.getId()));
        }

        @Override
        public void ended(TransferProcess process) {
            monitor.info(format("Transfer process %s ended", process.getId()));
        }

        @Override
        public void error(TransferProcess process) {
            monitor.info(format("Transfer process %s error", process.getId()));
        }

        @Override
        public void requested(TransferProcess process) {
            monitor.info(format("Transfer process %s requested", process.getId()));
        }
    }

    private static class HttpConsumerProxyResourceGenerator implements ResourceDefinitionGenerator {
        @Override
        public @Nullable ResourceDefinition generate(TransferProcess process) {
            return HttpProviderProxyResourceDefinition.Builder.newInstance()
                    .id(UUID.randomUUID().toString())
                    .assetId(process.getDataRequest().getAssetId())
                    .contractId(process.getDataRequest().getContractId())
                    .build();
        }
    }
}
