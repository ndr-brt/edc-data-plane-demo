package org.eclipse.dataspaceconnector.plane.control;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.dataspaceconnector.dataloading.AssetLoader;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.asset.Asset;

import java.util.Map;

import static java.lang.String.format;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/assets")
public class AssetController {
    private final Monitor monitor;
    private final AssetLoader assetLoader;

    public AssetController(Monitor monitor, AssetLoader assetLoader) {
        this.monitor = monitor;
        this.assetLoader = assetLoader;
    }

    @POST
    public String createAsset(Map<String, Map<String, String>> properties) {
        var assetProperties = properties.get("asset");
        var asset = Asset.Builder.newInstance().properties(assetProperties).build();
        var dataAddressProperties = properties.get("dataAddress");
        var address = DataAddress.Builder.newInstance().properties(dataAddressProperties).build();
        assetLoader.accept(asset, address);

        monitor.debug(format("Asset created %s", asset.getId()));
        return asset.getId();
    }

}
