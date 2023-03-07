# Chat GPT API

This is a Kotlin and Spring-based API for generating text responses using GPT (Generative Pre-trained Transformer)
language models. The API allows users to submit text prompts and receive generated text responses in real-time.

# Getting Started

To use this API, you will need:

- JDK 17 or higher
- Gradle 7 or higher
- A text editor or IDE, such as IntelliJ IDEA or Eclipse

## Stacks

- Kotlin - 1.8.10
- Spring Boot - 3.0.4
- Gradle - 8.0.2
- ChatGPT - gpt-3.5-turbo

To get started, follow these steps:

1. Clone the repository to your local machine:
   `git clone https://github.com/hanrw/onegai-chatgpt.git`
2. Open the project in your preferred text editor or IDE.
3. Configure the API settings in the application.yml file. This file contains properties such as the ChatGPT
   bearer-token.

Build and run the API using Gradle:

./gradlew bootRun
Submit text prompts to the API using HTTP POST requests to the /api/chat endpoint. The API will respond with generated
text responses.

API Endpoints
The API provides the following endpoints:

GET /api/chat?question={question}
This endpoint generates a text response based on a user-provided text prompt.

Request Parameters
question (required): A string containing the text prompt for the GPT model.
SSE Response
The API will respond with a Server-Sent Event (SSE) stream containing the generated text response.

# Contributing

If you'd like to contribute to this project, you can follow these steps:

1. Fork the repository to your GitHub account.
2. Clone the forked repository to your local machine.
3. Create a new branch for your changes:
   ```
   git checkout -b my-feature
   ```

4. Make your changes and commit them:
   ```
   git add .
   git commit -m "Add my feature" 
   ```
5. Push the changes to your GitHub account:
   ```
   git push origin my-feature
   ```
6. Create a pull request from your branch to the main repository.

7. Wait for a maintainer to review your pull request and merge it into the main branch.

# License

This project is licensed under the MIT License. See the LICENSE file for details.

# Acknowledgements

This project was inspired by OpenAI's GPT language models.
Thanks to the Spring and Kotlin communities for their support and contributions.

Project structure Inspired by the work
of [lets-build-components-not-layers](https://speakerdeck.com/thombergs/lets-build-components-not-layers)