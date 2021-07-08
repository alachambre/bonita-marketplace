package com.bonitasoft.marketplace.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bonitasoft.marketplace.bean.MavenArtifact;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;

@QuarkusTest
public class MavenArtifactServiceTest {

    private static final String CONNECTOR_TYPE = "connector";
    private static final String NAME = "connector-email";
    private static final String DESC = "Amazing connector to send emails";

    @InjectSpy
    MavenArtifactService service;

    @Test
    public void should_return_expected_maven_artifact() throws Exception {
        var artifact = new MavenArtifact();
        artifact.name = NAME;

        var request = service.createSearchRequest(CONNECTOR_TYPE);
        var query = service.createSearchWithContentQuery(NAME);
        request.setJsonEntity(query.encode());

        Mockito.when(service.createSearchRequest(CONNECTOR_TYPE)).thenReturn(request);
        Mockito.when(service.performRequest(request, query)).thenReturn(
                "{\"hits\":{\"hits\":[{\"_source\":{\"id\":\"ef16039b-f42e-4739-b422-35701b4a2bb8\",\"name\":\"connector-email\",\"description\":\"Amazing connector to send emails\",\"type\":\"connector\",\"groupId\":\"com.bonitasoft.connectors\",\"artifactId\":\"connector-email\",\"version\":\"1.0.0\"}}]}}");

        List<MavenArtifact> results = service.search(CONNECTOR_TYPE, NAME);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).name).isEqualTo(NAME);
        assertThat(results.get(0).description).isEqualTo(DESC);
    }

}
