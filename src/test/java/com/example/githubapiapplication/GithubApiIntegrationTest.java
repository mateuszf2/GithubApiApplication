package com.example.githubapiapplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void shouldReturnRepositoriesFromGithubForKnownUser() throws JsonProcessingException {
        // given
        String username = "octocat";
        String url = "http://localhost:" + port + "/repositories/" + username;

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        List<Map<String, Object>> repositories = objectMapper.readValue(
                response.getBody(), new TypeReference<>() {}
        );

        assertThat(repositories).isNotEmpty();

        for (Map<String, Object> repo : repositories) {
            assertThat(repo).containsKeys("repositoryName", "ownerLogin", "branches");

            List<Map<String, Object>> branches = (List<Map<String, Object>>) repo.get("branches");
            assertThat(branches).isNotNull();

            for (Map<String, Object> branch : branches) {
                assertThat(branch).containsKeys("name", "lastCommitSha");
            }
        }

    }
}

