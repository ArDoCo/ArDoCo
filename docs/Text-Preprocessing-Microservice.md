
Text preprocessing works locally, but there is also the option to host a **microservice** for this.
The benefit is that the models do not need to be loaded each time, saving some runtime (and local memory).

The microservice can be found at [ArDoCo/StanfordCoreNLP-Provider-Service](https://github.com/ArDoCo/StanfordCoreNLP-Provider-Service/).

The microservice is secured with credentials and the usage of the microservice needs to be activated and the URL of the microservice configured.
These settings can be provided to the execution via environment variables.
To do so, set the following variables:

```env
NLP_PROVIDER_SOURCE=microservice
MICROSERVICE_URL=[microservice_url]
SCNLP_SERVICE_USER=[your_username]
SCNLP_SERVICE_PASSWORD=[your_password]
```

The first variable `NLP_PROVIDER_SOURCE=microservice` activates the microservice usage.
The next three variables configure the connection, and you need to provide the configuration for your deployed microservice.