# Service Order MGMT
## run
`mvn spring-boot:run`

## About the Service
The service is just a simple OS Management REST Service. It uses an in-memory database (H2) to store the data. You can call some REST endpoints defined on port 8080. (see below)

`http://localhost:8080/profile`

* Manages Employees (Receptionists and Technicians) on a unified registration.
* Manages Clients
* Manages Products
* Manages Service Orders
* Manages Service Orders Log

## play
Once the app is up and running access `http://localhost:8080/swagger-ui.html`

## payload examples
`{"name":"Receptionist","active":true,"new":true}`
`{"name":"Technician","active":true,"new":true}`
`{"description":"Product","type":"PC","brand":"Brand","new":true}`
`{"name":"Client","address":"123 Main Street","zipcode":89200000,"phone":"47999999999","email":"client@mail.com","new":true}`
`{"receptionist":{"name":"Receptionist","active":true,"new":true},"technician":{"name":"Technician","active":true,"new":true},"client":{"name":"Client","address":"123 Main Street","zipcode":89200000,"phone":"47999999999","email":"client@mail.com","new":true},"product":{"description":"Product","type":"PC","brand":"Brand","new":true},"defect":"Broken","status":"IN_PROGESS","new":true}`
`{"employee":{"name":"Technician","active":true,"new":true},"message":"Working on it!","new":true}`