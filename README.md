# General microservice application for generating any types of reports in services.
### Description:
The application is a microservice system consisting of main and worker services. It solves the problem of receiving any form of reports; in this case, the implementation of a telephone call database is used, for example. The system allows you to **launch several application instances and scale it horizontally**. **To synchronize access to the database**, locking methods are not used, but instead **Kafka** and keys are used, **where each key will be placed in a separate partition**, which will make it possible to **solve the problem of competition for resources**.
**Addressed issue of data inconsistency** between sender and recipient due to **potential message loss** or **out-of-order delivery** that can occur when messages are sent **before database update operations** have completed or because delivery cannot be guaranteed.
Delivery semantics **achieved exactly once**. **Deduplication** implemented on the consumer side.

### Scheme 
<p><img src="https://github.com/honeymoneyes/ReportForge/assets/108457023/37416315-4b71-4679-971a-84d6de9a9e50"></p>


## Technologies Used:
+ Java 21
+ Spring Boot 3.2.4
+ Spring Data JPA
+ Spring Cloud
+ PostgreSQL
+ Lombok
+ Kafka 3.1.3
+ Minio

## Configuration Steps:

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
