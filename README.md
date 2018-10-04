
# twitter-link-collector

This program periodically polls the twitter home timeline, filters the tweets containing an url,
and email the url and title in batches (of 10)


### Prerequisites

Maven 3.x
```
https://maven.apache.org/download.cgi
```

Java 8
```
https://www.java.com/en/download/
```

(optional) Docker
```
https://www.docker.com/
```


(optional) A GCE VM instance to host the service
```
https://cloud.google.com/compute/docs/gcloud-compute/
```


## Getting Started

### create a docker image

```
mvnw clean install dockerfile:build
```

### Optional - run the docket image locally

```
docker run -it --rm -p 8080:8080 megalit/twitter-link-collector:latest
```

### upload the image to gcr.io the Google container registry

```
gcloud container builds submit --tag=gcr.io/twitter-link-collector/latest .
```

### restart the docker container deployed on the GCP

```
gcloud compute instances stop <vm instance>
gcloud compute instances update <vm instance>
gcloud compute instances start <vm instance>
```






