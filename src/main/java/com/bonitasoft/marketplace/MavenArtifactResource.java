package com.bonitasoft.marketplace;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bonitasoft.marketplace.bean.MavenArtifact;
import com.bonitasoft.marketplace.service.MavenArtifactService;

@Path("/maven-artifact")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MavenArtifactResource {

    @Inject
    MavenArtifactService mavenArtifactService;

    @POST
    public Response index(MavenArtifact mavenArtifact) throws IOException {
        if (mavenArtifact.id == null) {
            mavenArtifact.id = UUID.randomUUID().toString(); // TODO ensure unicity ? 
        }
        mavenArtifactService.index(mavenArtifact);
        return Response.created(URI.create("/maven-artifact/" + mavenArtifact.id)).build();
    }

    @GET
    @Path("/{id}")
    public MavenArtifact get(@PathParam("id") String id) throws IOException {
        return mavenArtifactService.get(id);
    }

    @GET
    @Path("/search")
    public List<MavenArtifact> search(@QueryParam("name") String name, @QueryParam("groupId") String groupId)
            throws IOException {
        if (name != null) {
            return mavenArtifactService.searchByName(name);
        } else if (groupId != null) {
            return mavenArtifactService.searchByGroupId(groupId);
        } else {
            throw new BadRequestException("Should provide name or groupId query parameter");
        }
    }
}
