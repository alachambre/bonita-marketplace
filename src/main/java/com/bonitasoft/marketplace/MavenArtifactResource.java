package com.bonitasoft.marketplace;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
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
            mavenArtifact.id = UUID.randomUUID().toString();
        }
        mavenArtifactService.index(mavenArtifact);
        return Response.created(URI.create("/" + mavenArtifact.type + "/" + mavenArtifact.id)).build();
    }

    @GET
    @Path("/{type}/{id}")
    public MavenArtifact get(@PathParam("type") String type, @PathParam("id") String id) throws IOException {
        return mavenArtifactService.get(id, type);
    }

    @GET
    @Path("/search/{type}")
    public List<MavenArtifact> search(@PathParam("type") String type, @QueryParam("searchContent") String searchContent)
            throws IOException {
        if (searchContent != null && !searchContent.isEmpty()) {
            return mavenArtifactService.search(type, searchContent);
        }
        return mavenArtifactService.search(type);
    }
}
