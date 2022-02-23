/*
 *  Copyright (c) 2020 - 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.plane.control;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.dataspaceconnector.spi.contract.offer.store.ContractDefinitionStore;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.query.QuerySpec;
import org.eclipse.dataspaceconnector.spi.query.SortOrder;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.offer.ContractDefinition;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/contractdefinitions")
public class ContractDefinitionApiController {
    private final Monitor monitor;
    private final ContractDefinitionStore contractDefinitionStore;

    public ContractDefinitionApiController(Monitor monitor, ContractDefinitionStore contractDefinitionStore) {
        this.monitor = monitor;
        this.contractDefinitionStore = contractDefinitionStore;
    }

    @GET
    public List<ContractDefinition> getAllContractDefinitions(@QueryParam("offset") Integer offset,
                                                              @QueryParam("limit") Integer limit,
                                                              @QueryParam("filter") String filterExpression,
                                                              @QueryParam("sort") SortOrder sortOrder,
                                                              @QueryParam("sortField") String sortField) {
        var spec = QuerySpec.Builder.newInstance()
                .offset(offset)
                .limit(limit)
                .sortField(sortField)
                .filter(filterExpression)
                .sortOrder(sortOrder).build();
        monitor.debug(format("get all contract definitions %s", spec));


        return new ArrayList<>(contractDefinitionStore.findAll());

    }

    @GET
    @Path("{id}")
    public ContractDefinition getContractDefinition(@PathParam("id") String id) {
        monitor.debug(format("get contract definition with ID %s", id));

        return contractDefinitionStore.findAll().stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Can't found definition with id " + id));
    }

    @POST
    public void createContractDefinition(ContractDefinition dto) {
        monitor.debug("Create contract definition: " + dto.getId());
        contractDefinitionStore.save(dto);
    }

    @DELETE
    @Path("{id}")
    public void deleteContractDefinition(@PathParam("id") String id) {
        monitor.debug(format("Attempting to delete contract definition with id %s", id));
    }


}
