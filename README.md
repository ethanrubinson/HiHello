# HiHello
HiHello Coding CHallenge

This project offers the functionality and behavior of a simple calculator (similar to that of the OSX calculator).

Buttons are entered in character by character (no spaces) and pressing `Enter` will execute the sequence of button presses on the calculator and output the resulting "on-screen" value.

## Download, Compile and Run
1) `git clone https://github.com/ethanrubinson/HiHello`
2) `cd HiHello`
3) `javac -d bin src/**/*.java`
4) `jar cfe Calculator.jar app.HiHelloSimpleCalculator -C bin app -C bin memory -C bin input`
5) `java -jar Calculator.jar`

### Run in Debug Mode
The calculator can also be run in debug mode by passing `true` as the first argument to the JAR:
`java -jar Calculator.java true`

## Sample Execution
```
0
> 2+45
45
> =
47
> *3=
141
> -8+12=
145
> c
0
```
