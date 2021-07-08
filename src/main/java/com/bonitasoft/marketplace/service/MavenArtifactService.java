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

    public Response index(MavenArtifact mavenArtifact) throws IOException {
        Request request = new Request(
                "PUT",
                "/" + mavenArtifact.type + "/_doc/" + mavenArtifact.id);
        request.setJsonEntity(JsonObject.mapFrom(mavenArtifact).toString());
        return restClient.performRequest(request);
    }

    public void delete(MavenArtifact mavenArtifact) throws IOException {
        Request request = new Request(
                "DELETE",
                "/" + mavenArtifact.type + "/_doc/" + mavenArtifact.id);
        request.setJsonEntity(JsonObject.mapFrom(mavenArtifact).toString());
        restClient.performRequest(request);
    }

    public MavenArtifact get(String type, String id) throws IOException {
        Request request = new Request(
                "GET",
                "/" + type + "/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody);
        return json.getJsonObject("_source").mapTo(MavenArtifact.class);
    }

    public List<MavenArtifact> search(String type, String searchContent) throws IOException {
        Request request = createSearchRequest(type);
        JsonObject queryJson = createSearchWithContentQuery(searchContent);
        String responseBody = performRequest(request, queryJson);
        return parseResponseBody(responseBody);
    }

    protected Request createSearchRequest(String type) {
        Request request = new Request(
                "GET",
                "/" + type + "/_search");
        return request;
    }

    protected JsonObject createSearchWithContentQuery(String searchContent) {
        /**
         * Construct a JSON query like that:
         * {
         * "query": {
         * "multi_match" : {
         * "query": <user input>,
         * "fields": [ "name", "description" ]}}}
         */
        JsonArray fields = new JsonArray();
        fields.add("name");
        fields.add("description");
        JsonObject termJson = new JsonObject().put("query", searchContent);
        termJson.put("fields", fields);
        termJson.put("type", "best_fields");
        JsonObject matchJson = new JsonObject().put("multi_match", termJson);
        JsonObject queryJson = new JsonObject().put("query", matchJson);
        return queryJson;
    }

    public List<MavenArtifact> search(String type) throws IOException {
        Request request = createSearchRequest(type);

        JsonObject matchJson = new JsonObject().put("match_all", new JsonObject());
        JsonObject queryJson = new JsonObject().put("query", matchJson);
        String responseBody = performRequest(request, queryJson);
        return parseResponseBody(responseBody);
    }

    private List<MavenArtifact> parseResponseBody(String responseBody) {
        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<MavenArtifact> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            MavenArtifact artifact = hit.getJsonObject("_source").mapTo(MavenArtifact.class);
            results.add(artifact);
        }
        return results;
    }

    protected String performRequest(Request request, JsonObject queryJson) throws IOException {
        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        return EntityUtils.toString(response.getEntity());
    }

}
