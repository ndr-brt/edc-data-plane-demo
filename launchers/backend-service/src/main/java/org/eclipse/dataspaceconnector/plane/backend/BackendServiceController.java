package org.eclipse.dataspaceconnector.plane.backend;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.edr.EndpointDataReference;

import java.io.IOException;

import static java.lang.String.format;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/pull")
public class BackendServiceController {
    private final Monitor monitor;
    private final OkHttpClient httpClient;

    public BackendServiceController(Monitor monitor, OkHttpClient httpClient) {
        this.monitor = monitor;
        this.httpClient = httpClient;
    }

    @POST
    public String pullData(EndpointDataReference dataReference) {

        String url = dataReference.getAddress();
        monitor.debug("Endpoint Data Reference received, will call data plane at " + url);
        Request request = new Request.Builder().url(url).addHeader(dataReference.getAuthKey(), dataReference.getAuthCode()).build();

        try (var response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                monitor.info("Data plane responded correctly");
            } else {
                monitor.warning(format("Data plane responded with error: %s %s", response.code(), response.body().string()));
            }
        } catch (Exception e) {
            monitor.severe(format("Error in calling the data plane at %s", url), e);
        }



        return "a";
    }

}
