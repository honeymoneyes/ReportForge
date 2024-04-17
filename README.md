# General microservice application for generating any types of reports in services.

The application is a microservice system consisting of main and worker services. It solves the problem of receiving any form of reports; in this case, the implementation of a telephone call database is used, for example. The system allows you to **launch several application instances and scale it horizontally**. **To synchronize access to the database**, locking methods are not used, but instead **Kafka** and keys are used, **where each key will be placed in a separate partition**, which will make it possible to **solve the problem of competition for resources**.

# Technologies Used:
+ Java 
+ Spring Boot
+ Spring Data JPA
+ Spring Cloud
+ PostgreSQL
+ Lombok
+ Kafka
+ Minio

# Configuration Steps:

### Clone the Application:

Clone the repository using the following command:

```
git clone https://github.com/honeymoneyes/ReportForge
```

### Run the Application Using Maven:
Open a terminal, navigate to the project root directory, and run the following command:

```
docker-compose up
```

The application will start running at http://localhost:8080.
## Program execution steps
### Step 1
Endpoint for creating a report - http://localhost:8080/master-service/report/create

The request is executed in the format:
```
{
    "phoneNumber": "+7(918)548-00-11",
    "startDate": "2024-02-12",
    "endDate": "2024-02-18"
}
```

### Step 2
A response is received in the format of a generated link to the report:
```
{
    "description": "Your report will be ready at this link after some time",
    "reference": "http://localhost:8080/master-service/report/files/73f2d4d2-18c7-46ae-958a-4753ec71b850"
}
```

### Step 3
Ready report
```
[
    {
        "number": "+7(918)548-00-11",
        "statusCall": "OUTCALL",
        "duration": 3.24,
        "dateCall": "2024-02-12"
    },
    {
        "number": "+7(918)548-00-11",
        "statusCall": "INCALL",
        "duration": 2.34,
        "dateCall": "2024-02-13"
    },
    {
        "number": "+7(918)548-00-11",
        "statusCall": "OUTCALL",
        "duration": 4.24,
        "dateCall": "2024-02-14"
    },
    {
        "number": "+7(918)548-00-11",
        "statusCall": "INCALL",
        "duration": 3.54,
        "dateCall": "2024-02-15"
    },
    {
        "number": "+7(918)548-00-11",
        "statusCall": "OUTCALL",
        "duration": 1.39,
        "dateCall": "2024-02-14"
    },
    {
        "number": "+7(918)548-00-11",
        "statusCall": "INCALL",
        "duration": 0.24,
        "dateCall": "2024-02-13"
    }
]
```
