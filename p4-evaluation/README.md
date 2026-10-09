# P4 Evaluation Service

## Overview

The P4 Evaluation Service checks program outputs against expected outputs and returns the submission verdict.

## Requirements

* Java 17 or compatible JDK
* Maven Wrapper included in this project

## Run the API

Open Command Prompt in the `p4-evaluation` folder and run:

```bash
mvnw.cmd spring-boot:run
```

The API runs at `http://localhost:8080`.

## API Endpoint

**POST** `/internal/judge`

### Sample Request

```json
{
  "submissionId": 5001,
  "problemId": 101,
  "language": "Java",
  "actualOutputs": ["10", "15", "25"],
  "expectedOutputs": ["10", "15", "25"]
}
```

### Sample Response

```json
{
  "verdict": "ACCEPTED",
  "passedTests": 3,
  "totalTests": 3
}
```

## Verdicts

* `ACCEPTED`: All outputs match.
* `WRONG_ANSWER`: One or more outputs do not match.

## Notes

The P4 service evaluates outputs received from the submission service. It does not compile or execute submitted source code.
