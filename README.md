# Axisrooms-ota Microservice

This microservice to interface CM Axisagent requests with the common format requests for channels who don't have their own API.

For reference visit:
[AxisAgent specifications.](http://axis-agent.api.axisrooms.com/#introduction)

[CM-docs (Common Format)](http://cm-docs.axisrooms.com/) 
 

For quick Set up in intellij use the debugConfiguration file stored at:  `otelzMircorservice/.idea/runConfigurations/OtelzApplication.xml`

[Familiarize yourself with Intellij ](https://github.com/sudipbhandari126/IntelliJSettings)

## Requirements:
#### Mongo database:

`sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927`

`echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mngodb-org/3.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.2.list`

`sudo apt-get update`

`sudo apt-get install -y mongodb-org`

`sudo systemctl start mongod`

[Digital Ocean Reference](https://www.digitalocean.com/community/tutorials/how-to-install-mongodb-on-ubuntu-16-04) 

##### (Starting console)
`mongo`
`show dbs` (list dbs)
`use db_name` (create new db)
`db.createCollection("collection_name")`
`db.collection_name.find()`  (Read the contents)

###### Note:
Collection:>Tables Documents:>Records

make the gradlew file executable: `sudo chmod +x gradlew`

create wrapper: `gradle wrapper --gradle-version (gradle_version on build.gradle file)`

Commands to Build and Run
 `./gradlew clean build` (-x test, to skip test cases)

` ./gradlew clean bootRun`