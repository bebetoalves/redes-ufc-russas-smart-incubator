# Smart Incubator - Redes de Computadores (UFC RUSSAS)
Incubators are used in hospitals to provide babies, usually premature, a thermoneutral environment (ie, the body does not expend energy compensating for changes in ambient temperature) controlled by airflow, humidity and temperature.

In this application, the incubator conditions are configured, monitored and controlled by a manager, which communicates with the sensors/actuators and can receive configurations or respond to queries from an external client.

This is a project proposed in the Computer Networks course at the Universidade Federal do Ceará.

## How to run?
This Java console application is dockerized and to run it you'll just need [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed on your machine, whether it's **Windows**, **Linux** or **MacOS**.

### Run all Services
To run all services (manager, monitor, all sensors and actuators), open your terminal in the project folder and run:
```sh
docker-compose up
```

### Start Manager
To run the manager *(server)*, open your terminal in the project folder and run:
```sh
docker-compose run manager
```

### Start Monitor
To client monitor, just run the command:
```sh
docker-compose run monitor
```

### Start Sensors
To start sensors, just run the command:
```sh
docker-compose run sensor_id
```
The available sensors ids are: `temperature`, `oxygen`, `humidity` and `heartbeat`.

### Start Actuators
To start actuators, just run the command:
```sh
docker-compose run actuator_id
```
The available actuators id are: `heater`, `humidifier` and `aircirculator`.