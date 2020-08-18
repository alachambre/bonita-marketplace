/**
 * Copyright (C) 2020 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bonitasoft.marketplace.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.bonitasoft.marketplace.bean.MavenArtifact;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class MavenArtifactService {

    @Inject
    RestClient restClient;

    public void index(MavenArtifact mavenArtifact) throws IOException {
        Request request = new Request(
                "PUT",
                "/maven-artifact/_doc/" + mavenArtifact.id);
        request.setJsonEntity(JsonObject.mapFrom(mavenArtifact).toString());
        restClient.performRequest(request);
    }

    public MavenArtifact get(String id) throws IOException {
        Request request = new Request(
                "GET",
                "/maven-artifact/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody);
        return json.getJsonObject("_source").mapTo(MavenArtifact.class);
    }

    public List<MavenArtifact> searchByName(String name) throws IOException {
        return search("name", name);
    }

    public List<MavenArtifact> searchByGroupId(String groupId) throws IOException {
        return search("groupId", groupId);
    }

    private List<MavenArtifact> search(String term, String match) throws IOException {
        Request request = new Request(
                "GET",
                "/maven-artifact/_search");
        //construct a JSON query like {"query": {"match": {"<term>": "<match"}}
        JsonObject termJson = new JsonObject().put(term, match);
        JsonObject matchJson = new JsonObject().put("match", termJson);
        JsonObject queryJson = new JsonObject().put("query", matchJson);
        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<MavenArtifact> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            MavenArtifact fruit = hit.getJsonObject("_source").mapTo(MavenArtifact.class);
            results.add(fruit);
        }
        return results;
    }

}
