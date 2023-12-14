package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import input.Command;
import memory.Memory;

/**
 * HiHello Simple Calculator - Mimics the functionality and behavior of the OSX
 * simple calculator.
 * 
 * @author Ethan Rubinson
 */
public class HiHelloSimpleCalculator {

	private final boolean debugMode;
	private final Memory memory;
	private final Scanner reader;

	private String output;

	/**
	 * Creates a new simple calculator in the specified debug mode.
	 * 
	 * @param debugMode Whether or not the calculator is running in debug mode.
	 */
	public HiHelloSimpleCalculator(boolean debugMode) {
		this.debugMode = debugMode;
		this.memory = new Memory();
		this.reader = new Scanner(System.in);
		this.output = memory.currentNumber().toPlainString();
	}

	/**
	 * Runs the simple calculator instance.
	 */
	public void run() {
		System.out.println("HiHello Simple Calculator. Enter 'q' to quit.");
		System.out.println();

		String line;
		printUserInputPrompt(output);
		while (!(line = reader.nextLine()).equals("q")) {
			final Optional<List<Command>> commandsRes = getCommands(line);
			if (commandsRes.isPresent()) {
				final List<Command> commands = commandsRes.get();
				if (debugMode) {
					System.out.println("Commands: " + Arrays.toString(commands.toArray()));
				}
				output = executeCommands(commands, memory, debugMode);
			} else {
				System.err.println("[ERROR] Invalid button detected! Calculator state is unchanged.");
			}
			printUserInputPrompt(output);
		}

		reader.close();
	}

	/**
	 * Entry point for the HiHello Simple Calculator application
	 */
	public static void main(String[] args) {
		boolean debugMode = false;
		if (args != null && args.length > 0) {
			debugMode = Boolean.parseBoolean(args[0]);
		}
		new HiHelloSimpleCalculator(debugMode).run();
	}

	/**
	 * Prints the current on-screen number and prompts the user for the next input.
	 * 
	 * @param output The current on-screen number.
	 */
	private static void printUserInputPrompt(String output) {
		System.out.println(output);
		System.out.print("> ");
	}

	/**
	 * Executes the series of commands and returns the on-screen number resulting
	 * from the execution of the last command.
	 * 
	 * @param commands  The list of commands to execute sequentially.
	 * @param memory    The calculator memory.
	 * @param debugMode Whether or not the calculator is running in debug mode.
	 * @return The current on-screen number resulting from the execution of the last
	 *         command.
	 */
	private static String executeCommands(List<Command> commands, Memory memory, boolean debugMode) {
		commands.stream().forEach(cmd -> {
			if (cmd.isNumber()) {
				memory.appendToCurrentNum(cmd.getToken());
			} else if (cmd.isDecimal() && !memory.currentNumIsDecimal()) {
				memory.appendToCurrentNum(cmd.getToken());
			} else if (cmd.isNegate()) {
				memory.negateCurrentNum();
			} else if (cmd.isPercent()) {
				memory.percentifyCurrentNum();
			} else if (cmd.isMultiply() || cmd.isDivide()) {
				if (memory.waitingOperationExistsAndIsHighPrecidence()) {
					memory.executeWaitingOp();
				}
				memory.storeOp(cmd);
				memory.storeCurrentNumber();
			} else if (cmd.isPlus() || cmd.isMinus()) {

				while (memory.waitingOperationExists()) {
					memory.executeWaitingOp();
				}
				memory.storeOp(cmd);
				memory.storeCurrentNumber();
			} else if (cmd.isEquals()) {
				while (memory.waitingOperationExists()) {
					memory.executeWaitingOp();
				}
			} else if (cmd.isClear()) {
				memory.allClear();
			}
			if (debugMode) {
				System.out.println(memory);
			}

		});
		return memory.currentNumber().toPlainString();
	}

	/**
	 * Parses an input line from the console into a list of commands if the entire
	 * line is valid. If any of the tokens in the line could not be parsed into
	 * Commands, an empty optional is returned.
	 */
	private static Optional<List<Command>> getCommands(String line) {
		final List<Command> cmds = new ArrayList<>();
		boolean valid = true;
		for (char token : line.toCharArray()) {
			final Optional<Command> cmd = Command.of(token);
			if (cmd.isPresent()) {
				cmds.add(cmd.get());
			} else {
				valid = false;
				break;
			}
		}
		if (valid) {
			return Optional.of(cmds);
		} else {
			return Optional.empty();
		}
	}

}
