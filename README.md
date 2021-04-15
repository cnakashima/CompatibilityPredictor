# CompatibilityPredictor

CompatibilityPredictor takes in a JSON file containing profiles of your current employees as well as applicants to generate a score 
for applicants from 0 to 1 predicting their compatibility with your current team. 

## Installation

[Download] (https://github.com/cnakashima/CompatibilityPredictor/archive/refs/heads/master.zip) and unpack the repository.

## Run

### On Mac

1. From command line, navigate to the CompatibilityPredictor directory 
2. move your .json file into the CompatibilityPredictor directory
3. run `java -cp bash:lib/json-simple-1.1.1.jar Main JSONFileName.json`

### On Linux or Windows

Out of a lack of familiarity I can give precise instructions to run on other systems. However, please feel free to open a new issue and I will try to help, as best I can.

## Input

Input should be in the form a .json file, which should be specified as the only command line argument.

*Note: your .json file should be formatted as follows:*
```
{
  "team": [
    {
      "name": "employeeOneName",
      "attributes": {
        "intelligence": 1,
        "strength": 5,
        "endurance": 3,
        "spicyFoodTolerance": 1
      }
    }
  ],
  "applicants": [
    {
      "name": "applicantOneName",
      "attributes": {
        "intelligence": 4,
        "strength": 5,
        "endurance": 2,
        "spicyFoodTolerance": 1
      }
    }
  ]
}
```

## Output

Output will be in the form a JSON file called scoredApplicants.json, which will be created within the CompatibilityPredictor directory.

## Example Usage

### Example Input

```
{
  "team": [
    {
      "name": "Eddie",
      "attributes": {
        "intelligence": 1,
        "strength": 5,
        "endurance": 3,
        "spicyFoodTolerance": 1
      }
    },
    {
      "name": "Will",
      "attributes": {
        "intelligence": 9,
        "strength": 4,
        "endurance": 1,
        "spicyFoodTolerance": 6
      }
    },
    {
      "name": "Mike",
      "attributes": {
        "intelligence": 3,
        "strength": 2,
        "endurance": 9,
        "spicyFoodTolerance": 5
      }
    }
  ],
  "applicants": [
    {
      "name": "John",
      "attributes": {
        "intelligence": 4,
        "strength": 5,
        "endurance": 2,
        "spicyFoodTolerance": 1
      }
    },
    {
      "name": "Jane",
      "attributes": {
        "intelligence": 7,
        "strength": 4,
        "endurance": 3,
        "spicyFoodTolerance": 2
      }
    },
    {
      "name": "Joe",
      "attributes": {
        "intelligence": 1,
        "strength": 1,
        "endurance": 1,
        "spicyFoodTolerance": 10
      }
    }
  ]
}
```

### Example Output

```
{"scoredApplicants":[{"score":0.9249999999999999,"name":"John"},{"score":0.8674999999999999,"name":"Jane"},{"score":0.895,"name":"Joe"}]}
```
