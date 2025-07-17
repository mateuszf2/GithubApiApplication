# GithubApiApplication

## Opis

Aplikacja udostępnia endpoint do pobrania listy publicznych repozytoriów (nie-forków) użytkownika Github wraz z gałęziami i SHA ostatnich commitów.

## Endpoint

GET /repositories/{username}

## Jak uruchomić

```bash
mvn spring-boot:run
```

## Jak uruchomić test

```bash
mvn test
```

## Przykładowa odpowiedź

```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "..."
      }
    ]
  }
]
```