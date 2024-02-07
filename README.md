# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


### Phase 2 Chess Design
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBkInIZSqjxDKbPkQArmGHKQY3BMbTmM7mADiwALYouelUbVN4UFPxTW7jg6smuAFIQSGjeKL6G9AGm8AgeUACC2NjIIPxIgpH+JqQAIvzAiSAGXFyYACaFAEbAXCgwFdWYmOxQ2tgwAMRowFQAnqwcFnwCaN0A7gAWSGBiiKikALQAfORKlABcMADaAAoA8mQAKgC6MAD0WvVQADpoAN7XlL12ADQwuGUT0BUfKDZgEgEABfTB6SgwVZDTg8UaZRQiAzbdywuZQAAUzygrxQHy+XB+UD+MABQIQAEpMGw0fCskpRFC1gUwEUSvYuNt2CgwABVG5Ym64qkstmlLhMxrVbZkACiMllcGOMGxuJgADN2jYVTdMKLiuKodCaSMMvSkShtmgtAgENThnCzYj9A1ofr2WVtiB3J5+ZRBS9bHjPnVCb8RYUDRzJU1tgBJABycpYytVQfxoaJJLJwJgieOBx1tBNjrGkUl7vFXp9c0SOimAZxQYjrKjZRj0rzSdlKaLTfeMGA9eOEAA1uguwXB-W9ZGPRKlsaHbwnZFtkOwFMR+O0PbaauGQYjWsIVB18Ox+hwRtYIu1gt0GBtgAmAAMr-uDw3W8vaDB6AqFo2g6bpoGkbkYDkBRJhmOZMAfZYTxvbZ9iOM5LnqMoEU-NMBwJLMwVPSUoNCNcYAQeRQkbXEM2+cNMAuC44CmFAQFHT4EGAfpIRQAAPCxyhI51GTdOcqxgbk+QFXCUBbMVo2hWNyHlRVUyFIMNS1PtZ1bedj0gyjhORPtRQYi4KPYGBNwBGBQg+QFxxgNAUAmadNys39MCE8tRN08TvQ8Wt62o5sdPk9tFM7RNkzUwMB2-bcJ3zQtvzCtsF2hbzD0tNyfx3LzDJ86FT3PTdEt3Ii7ziRYnxgN8P0eGSPgS39-zQQDWnaLROi6NoUAnKDtGYLpplmeZkEfSUSt2PIVOOWVzguTCuGw3oL3yyq1kGnQyIsoaMRancqUY5jWPYpB1Vyjyd1JfieAlYAEB9CpBgxCzuQqWy0CpbbdGyisxI5LkeTrTcDvW9A5PSjsZRUpVcvKzSIG1VLKwUzLNB27LSqmUzGLYGwIBoK7IHy36fOZQHPUaFB4jmcGyt-KG9Mi7ZZoVeaEd-JGUZncn-uK5CrKgLQGgxLgtHnKlKuhBDavqzAAKArqevcT7zB4GBwglEbYPGmqpqFnZLFldCLnYIN7kOq9No1qwgwcbHyIsbWGbyyGzNOtjbMu7EYCmOpcugJAAC8nS8l2HYpmA0epyTQYba3vrSlm1iUuUFXhpOedylPDSqu3tbI788YuO2YCGwdbQkh2I54IuBcpvyged+uHYxZn87TzsWFlRI8irhAc4tuw8-RrbI7sR2LS5INRR2U46-tqeiqQ6gthrux58XmX7wmjAX3fT8YAAIhH+wT5Q8-59fU4PgAOkf042o64Duu6bAtCgVIGjgGsGnCDBMa8F97MEFuvM8uxDgnEWufK2EM0CfnPgmIMhEbySj-oFLwQZdqT3sG7cqHxkGhROixb2F0+z+0Dt+YOYcxjwX-uEaOsdOQSRBsFJOncFLd1hpnZU2dNTI1ziw-SmDPBMKdiXQoZkACywBxwSmsk5Fym8GhVFZP7SgwZUhcUoBKAAvE5G0CAiHoEoJ4VRcY8gMKwRIi0GDGE4Kdg5bBdgMTnysUQoMKC7AUhgLPLe0ixFzDsS6AGzdqYuPCO4uehQuERR4V2GKqjRSTkLOfGx4inH2KqtNDx1jd7VUfIfBqDwz5BisZffJL9lYgS6NxW0EBXLBFCFrDSXRqicTYiAg24CaCQP2LyM2cC1qMx3J+OWcAIAUSgF4uwVi0EQMlC0sI2SXTbAAFYhFWW4pOHxJnTOgHMlAVjjpMTIedX2NwqEPXrLQ8OKzQkiSbuFVh8cOEIPiQuRJGdVJcxuoI3mm4x4RWhI8tZogcaly9pc1Rt0BKYHBT4RuMcqZvJ5NE-JXyYbKT4ZYgegLVEgoymsJFEQnbX2kaQs6mj3A+ysixGAIBDmwHqPEcAKBPrcEHFQckwBOnBgoVwCA9l5ENCUSAAOaAIKtKUeo4Ag52owHcGAL+4xZgeSlIi7ZTyjxgp1RC4yiQKgVB0dxTElLWT7NAVMmZHx4GblFPfGSfiAkoFMmS5haLtjdXUa4lAMTAmsmxazXkew8iJE5pahVyUiWepRdNXgosYDi0luKaW6CqpyxKYrdqtT369T4IMRACRBwpDSE6IBcE5aGwgShdmpsFqXCaNeJZst4h4GSKkQgB4Z7kUxmAAhTNPYXPpX7AOtzNz3PoSWzt5ae1lhRSw4GYAE5DqOsSnFvys4IJzqjNFoiO1JHnekRdfapGsjMgTImDQUD9P6JuUI7AQEJC7RWs9YTfKvK9Eejum7WYxzmrKQee6ZwiIAw2zmj0h6EuxP+xJkHgPQeHkGF9c7u2noRNHRNIsxYSylq2-ph6ao5qVkAA