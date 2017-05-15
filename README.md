# CMPE281-Hackathon
A Multi-Cloud Starbucks Drink Ordering portal based on the Restbucks CRUD REST API design 
![ScreenShot](https://raw.github.com/khurana3773/FrontendStarbucks/master/starbucks_stores.png)

View demo for this project :- https://www.youtube.com/watch?v=1DqcBS7eUiI

This Repository contains has Backend Starbucks Store 2 information. To know more about Frontend, visit the following repo: https://github.com/khurana3773/FrontendStarbucks.git

Summary: 

- It's a team project of four people where 3 team members individually worked on backend Starbucks store and 1 team member worked on middleware Kong API Gateway and all 4 members contributed to frontend.

- Created a backened Starbucks Store 2, deployed on AWS EC2 instance, that receives REST API calls from frontend portal, deployed in Heroku. The Kong API Gateway is a middleware deployed on AWS with a 3-Node Cassandra DB Cluster that routes calls from frontend portal to backend tenant's Starbucks store.

- The backend is written in Java using a restlet framework and NoSQL Mongo Database.

- The Starbucks server handles routing of REST calls and Starbucks API handles the connection to the DB and performs the query operations.

- Implementation of APIs is backed by a 3-Node NoSQL Mongo database cluster

- Deployed the Starbucks store (backend) on AWS ec2 instance.


