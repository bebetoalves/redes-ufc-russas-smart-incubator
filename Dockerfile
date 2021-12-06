FROM eclipse-temurin:17
COPY src app
WORKDIR app
RUN mkdir -p output
RUN javac -d output ./com/Manager.java
RUN javac -d output ./com/Monitor.java
RUN javac -d output ./com/sensors/HeartBeatSensor.java
RUN javac -d output ./com/sensors/HumiditySensor.java
RUN javac -d output ./com/sensors/OxygenSensor.java
RUN javac -d output ./com/sensors/TemperatureSensor.java
RUN javac -d output ./com/actuators/AirCirculatorActuator.java
RUN javac -d output ./com/actuators/HeaterActuator.java
RUN javac -d output ./com/actuators/HumidifierActuator.java
