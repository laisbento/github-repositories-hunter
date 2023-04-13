# Github Repositories Hunter

### Attention: this code is not working properly. I will update it asap.
---

This API utilizes GitHub APIs to search repository with or without forks using Spring Webflux.


## Run Locally

Clone the project

```bash
  git clone https://github.com/laisbento/github-repositories-hunter.git
```

Go to the project directory

```bash
  cd github-repositories-hunter
```

Install dependencies

```bash
  gradle install
```

Start the server

```bash
  gradle bootRun
```


## Running Tests

To run tests, run the following command

```bash
  gradle test
```


## API Reference

Swagger: http://localhost:8080/swagger-ui/index.html

#### Get all repositories from an existing user

```http
  GET /github?username:githubUsername
```

| Parameter  | Type     | Description                   |
| :--------  | :------- | :-------------------------    |
| `username` | `string` | **Required**. Github username |
| `allowForks` | `boolean` | **Optional**. Should allowForks or not. The default value is **false** |

----
## Responses

##### Success

    HTTP/1.1 200 OK
    Status: 200 OK
    Content-Type: application/json
    [
        {
            "repositoryName": "amp-up-frontend",
            "ownerName": "laisbento",
            "branches": [
                {
                    "name": "master",
                    "commit": {
                        "sha": "dd659b6b4df5cb5e76c2be07059cbe16976edd6a"
                    }
                }
            ]
        }
    ]
----
##### Errors

    HTTP/1.1 404 Not Found
    
    {
        "statusCode": 404,
        "message": "laisbentoyfg was not found."
    }

----

    HTTP/1.1 406 Not Acceptable
    
    {
        "statusCode": 406,
        "message": "Invalid Accept header."
    }
