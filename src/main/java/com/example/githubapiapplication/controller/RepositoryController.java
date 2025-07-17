package com.example.githubapiapplication.controller;

import com.example.githubapiapplication.dto.RepositoryResponse;
import com.example.githubapiapplication.service.GithubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final GithubService githubService;

    public RepositoryController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryResponse>> getRepositories(@PathVariable String username) {
        List<RepositoryResponse> repos = githubService.getNonForkRepositories(username);
        return ResponseEntity.ok(repos);
    }
}

