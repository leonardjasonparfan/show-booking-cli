## Show Booking CLI

### Assumptions
- Using the `Availability` command will show all available seats including those that were cancelled.
- The `Cancel` command will only cancel tickets that are not yet cancelled.
- In the `Setup` command, the cancellation window in minutes is of type BigDecimal to allow for decimal values.

### How to use
- This app is developed using Spring Shell. The list of available commands can be seen by typing `help` in the console.
- There are two types of commands: `Admin` and `User` commands.
- In order to use the `Admin` commands, the user must first authenticate using the `Auth` command. ex: `Auth user` or `Auth admin`
- You can get more information about a command by typing `help <command>`. ex: `help Setup`

### Prerequisites:
- GraalVM JDK 17

### How to run
#### Using terminal
- Run `./mvnw clean package`
- Run `mvn spring-boot:run`

#### Using IDE (ex. IntelliJ)
- Make sure JDK is set to GraalVM 17
- Right click Application.java and select `Run Application`

### How to generate executable
- Run `./mvnw clean package`
- Run `mvn -Pnative native:compile`
- Executable file should be inside target folder