/**
 * Copyright (C) 2021 Bonitasoft S.A.
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
package com.bonitasoft.marketplace;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bonitasoft.marketplace.bean.MavenArtifact;
import com.bonitasoft.marketplace.service.MavenArtifactService;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MavenArtifactResourceTest {

    @Inject
    MavenArtifactService service;

    private MavenArtifact artifact;

    @BeforeEach
    public void setup() throws Exception {
        artifact = new MavenArtifact();
        artifact.id = UUID.randomUUID().toString();
        artifact.type = "connector";
        artifact.name = "Connector Test";
        artifact.description = "Amazing connector to send emails";
        artifact.groupId = "com.bonitasoft.connectors";
        artifact.artifactId = "connector-email";
        artifact.version = "1.0.0";

        service.index(artifact);

        // TODO find a way to properly wait for index creation ?
        Thread.sleep(1000);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        service.delete(artifact);
    }

    @Test
    public void testSearchEndpoint() throws Exception {
        given()
                .when().get("/maven-artifact/search/connector")
                .then()
                .statusCode(200)
                .body(containsString("Amazing connector to send emails"));
    }

}
