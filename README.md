# HiHello
HiHello Coding CHallenge

This project offers the functionality and behavior of a simple calculator (similar to that of the OSX calculator).

Buttons are entered in character by character (no spaces) and pressing `Enter` will execute the sequence of button presses on the calculator and output the resulting "on-screen" value.

## Compile and Run
1) `javac -d bin src/app/*.java`
2) `jar cfe Calculator.jar app.HiHelloSimpleCalculator -C bin app -C bin memory -C bin input`
3) `java -jar Calculator.java`

### Run in Debug Mode
The calculator can also be run in debug mode by passing `true` as the first argument to the JAR:
`java -jar Calculator.java true`
