# Game Of Three

##Build
1. After cloning the repository, go to the `gameofthree` directory
2. Checkout `git checkout master` branch
3. Execute `gradle clean build` from terminal to build the source

## Configurations
1. There are two properties in `src/main/resources` directory for each player
2. `application.properties` is for player 1
3. `application2.properties` is for player 2
4. By default, player 1 service run on 8080 port. Which can be change by changing value of `server.port` in application.properties.
5. By default, player 2 service run on 8090 port. Which can be change by changing value of `server.port` in application2.properties.
6. In each player properties need to configure other player service base url via `second.player.base.url` 
7. In case of automatic play each player will generate a random number. Random number bound can be 
configured by `beginning.random.number.upper.limit` property.

## Run
1. Go to the `gameofthree` directory and open terminal for each player
2. Execute `./app1-start.sh` to run player 1 service 
3. Execute `./app2-start.sh` to run player 2 service

## Test
1. Open any web browser and open `http://localhost:8080/index` this URL to view player 1 interface
2. Open any web browser and open `http://localhost:8090/index` this URL to view player 2 interface
3. If any player wants to start the game automatically then just click on the `Start the game` button
and then each player terminal log to check game status
4. If any player wants to start the game manually fill up the `Starting number(Manual)` input field
then just click on the `Start the game` button. Then check each player terminal log to check game status.

## Assumptions
1. If the second player is not available then if the other player
start the game then the game will be ended
2. During the game if any player become unreachable then game will be ended
3. Each player send it calculated number to other player via REST api call
4. In each player user interface if `Start the game` clicked without filling up `Starting number(Manual)`
input field then the game will be considered automatic

## Improvement Scope
1. Currently if second player is not available and if first player started the game the game is being
ended forcefully. It could be improved so that each player wait for other player to be available. It 
can be done via messaging.
2. Currently each player send its calculated number via REST call. But it could be improved to send number
via messaging protocol.
3. From user interfaces player has no way of knowing game status. Using web socket player game status like when 
game ended or winner found could be sent to user interface.

## Technology Used

- Java (JDK 11)
- Springboot (For API and testing)
- Gradle (Build tool)
- Embedded Tomcat (Application Server)
- Thymeleaf (template engine)


