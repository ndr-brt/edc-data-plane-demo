package org.eclipse.dataspaceconnector.plane.control;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/transfers")
public class TransferController {
    private final Monitor monitor;
    private final TransferProcessStore store;

    public TransferController(Monitor monitor, TransferProcessStore store) {
        this.monitor = monitor;
        this.store = store;
    }

    @GET
    @Path("{id}")
    public TransferProcess get(@PathParam("id") String id) {
        return store.find(id);
    }
}
