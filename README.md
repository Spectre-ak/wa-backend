# wa-backend

## Table of Contents
1. [General Info](#general-info)
   A Spring Boot Rest Controller that deliver information from 
2. [Technologies](#technologies)
   Spring Boot
   Google Sheets Api
   
4. [Collaboration](#collaboration)
5. [FAQs](#faqs)

| HTTP Method | End Point   |   Payload   | Response    |
| ----------- | ----------- | ----------- | ----------- |
| Get  | https://woay-backend.azurewebsites.net/projects          | Optional Request Param: startDate and endDate       |       Projects and Project between start and end date |
| Get  | https://woay-backend.azurewebsites.net/projects/{id}     | Optional Request Param: id       |       Retrieve project by id |
| Get  | https://woay-backend.azurewebsites.net/vendors           | n/a                              |      Get all vendors |
| Get  | https://woay-backend.azurewebsites.net/vendors/{id}      | n/a                              |      Vendor by ID |
| Get  | https://woay-backend.azurewebsites.net/resources/{id}    | n/a                              |     Resources by ID  |
| Post | https://woay-backend.azurewebsites.net/recommendations   | Send location, date and roles    |     Retrieve resources by location and roles as well as resources without projects |
| Get  | https://woay-backend.azurewebsites.net/senior            | n/a                              |     Retrieve engineer by role level Senior |
| Get  | https://woay-backend.azurewebsites.net/mid               | n/a                              |     Retrieve engineer by role level Mid |
| Get  | https://woay-backend.azurewebsites.net/junior            | n/a                              |     Retrieve engineer by role level Junior |
| Get  | https://woay-backend.azurewebsites.net/ux                | n/a                              |     Retrieve engineer by role UX |
| Get  | https://woay-backend.azurewebsites.net/pm                | n/a                              |     Retrieve engineer by role PM |


