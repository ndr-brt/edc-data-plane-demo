package org.eclipse.dataspaceconnector.plane.backend;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.edr.EndpointDataReference;

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
        Request request = new Request.Builder()
                .url(url)
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode()).build();

        try (var response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (response.isSuccessful()) {
                monitor.info("Data received from the provider: " + body.string());
            } else {
                monitor.warning(format("Data plane responded with error: %s %s", response.code(), body.string()));
            }
        } catch (Exception e) {
            monitor.severe(format("Error in calling the data plane at %s", url), e);
        }

        return "a";
    }

    @GET
    public String getData() {
        var data = "this is a string but could be a file of everything else";
        monitor.info("Data is requested, will return: '" + data + "'");
        return data;
    }

}
