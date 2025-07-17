package com.example.githubapiapplication.service;

import com.example.githubapiapplication.dto.BranchResponse;
import com.example.githubapiapplication.dto.RepositoryResponse;
import com.example.githubapiapplication.exception.UserNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GithubService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<RepositoryResponse> getNonForkRepositories(String username) {
        String reposUrl = "https://api.github.com/users/" + username + "/repos";
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    reposUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<Map<String, Object>> repos = response.getBody();

            if (repos == null) return List.of();

            return repos.stream()
                    .filter(repo -> !(Boolean) repo.get("fork"))
                    .map(repo -> {
                        String repoName = (String) repo.get("name");
                        Map<String, Object> owner = (Map<String, Object>) repo.get("owner");
                        String ownerLogin = (String) owner.get("login");

                        String branchesUrl = "https://api.github.com/repos/" + ownerLogin + "/" + repoName + "/branches";

                        ResponseEntity<List<Map<String, Object>>> branchesResponse = restTemplate.exchange(
                                branchesUrl,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<>() {}
                        );

                        List<BranchResponse> branches = branchesResponse.getBody().stream()
                                .map(branch -> new BranchResponse(
                                        (String) branch.get("name"),
                                        (String) ((Map<String, Object>) branch.get("commit")).get("sha")
                                ))
                                .toList();

                        return new RepositoryResponse(repoName, ownerLogin, branches);
                    })
                    .toList();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new UserNotFoundException("Github user not found");
        }
    }
}
