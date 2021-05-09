# wa-backend

## Table of Contents
1. [General Info](#general-info) 
2. [Technologies](#technologies)
3. [Data Model](#data-model)
4. [Collaboration](#collaboration)
5. [FAQs](#faqs)

## General Info
An application back end utilizing REST to transmit data project specific 
information to the front end client for users to view available projects, 
vendors, and resources for each.  

##Collaboration
This project was worked on in collaboration for the Interapt Hackathon on 
5/8/2021

###Technologies
Spring Web Framework was used to map the end points and handle the 
controller functions.
To allow easy implementation for departments, Google sheets API was 
leveraged as an easy to use back end allowing the average project manager 
the ability to leverage the system without interference from IT or developers.

###Data Model
Resources were fed to the Rest Controller where the raw data was manipulated 
into mapped objects. When fed through the Rest Controller end points, the 
POJO we're converted into easily consumable formats for the front end. 

```
{
"Accessibility": "",
"Prod End Date": "12/24/21",
"Available for Other areas": "Y",
"Education Track": "",
"Product": "Product 1",
"Gender": "M",
"resource product start date": "01/04/21",
"resource product end date": "12/24/21",
"Work Intake Scoping": "",
"Name": "Resource 2",
"Start Date": "11/29/17",
"Security Maven": "",
"Role Level": "Senior",
"Prod Build Location": "IL",
"Prod Start Date": "01/04/21",
"Vendor": "",
"Color (Y/N)": "N",
"Role": "Engr",
"Anchor": "",
"Skill 1": "",
"DevSecOps": "",
"Skill 2": "Cloud ",
"Interviewer": "",
"E/C": "E",
"Location": "IL"
}

```

##REST API Endpoints
| HTTP Method | Endpoint   |   Payload   | Response    |
| ----------- | ----------- | ----------- | ----------- |
| **Get**  | https://woay-backend.azurewebsites.net/projects          | Optional Request Param: startDate and endDate       |       Retrieve information on all projects and with start and end date query parameters retrieve projects that fit within a specific timeframe
| **Get**  | https://woay-backend.azurewebsites.net/projects/{id}     | Request Param: id       |       Retrieve project by id |
| **Get**  | https://woay-backend.azurewebsites.net/vendors           | n/a                              |     Get all vendors |
| **Get**  | https://woay-backend.azurewebsites.net/vendors/{id}      | Request Param: id                |     Vendor by ID |
| **Get**  | https://woay-backend.azurewebsites.net/resources/{id}    | Request Param: id                |     Resources by ID  |
| **Post** | https://woay-backend.azurewebsites.net/recommendations   | Send location, date and roles    |     Retrieve resources by location and roles as well as resources without projects |
| **Get**  | https://woay-backend.azurewebsites.net/senior            | n/a                              |     Retrieve engineer by role level Senior |
| **Get**  | https://woay-backend.azurewebsites.net/mid               | n/a                              |     Retrieve engineer by role level Mid |
| **Get**  | https://woay-backend.azurewebsites.net/junior            | n/a                              |     Retrieve engineer by role level Junior |
| **Get**  | https://woay-backend.azurewebsites.net/ux                | n/a                              |     Retrieve engineer by role UX |
| **Get**  | https://woay-backend.azurewebsites.net/pm                | n/a                              |     Retrieve engineer by role PM |


