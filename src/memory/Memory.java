package memory;

import java.math.BigDecimal;
import java.util.Optional;

import input.Command;

/**
 * Simple calculator memory that supports four operations (plus, minus,
 * multiply, divide).
 */
public class Memory {

	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

	/** true if the current number should be made negative, false otherwise. */
	private boolean currentNumNegated;

	/**
	 * The string representation of the (absolute value of) the current on-screen
	 * number.
	 */
	private String currentNum;

	/**
	 * true if the current on-screen number should start as a new number on the next
	 * append.
	 */
	boolean resetCurrentNumOnNextAppend;

	/** The operation waiting to be performed (most recent). */
	private Optional<Command> waitingOp;

	/** The buffered operation of lower precedence. */
	private Optional<Command> bufferedOp;

	/** The (most recent) stored number. */
	private Optional<BigDecimal> waitingNum;

	/** The (less recent) stored number. */
	private Optional<BigDecimal> bufferedNum;

	/*
	 * Due to the operation supported, there can only ever be three numbers in
	 * memory: - The on-screen number currently being entered - The number waiting
	 * to be operated on (the last input) - The running total waiting to be operated
	 * on from a lower precedence operation.
	 */

	/**
	 * Creates new memory in the default state.
	 */
	public Memory() {
		allClear();
	}

	/**
	 * Resets the memory to the default state.
	 */
	public void allClear() {
		currentNumNegated = false;
		currentNum = "0";
		resetCurrentNumOnNextAppend = false;
		waitingOp = Optional.empty();
		bufferedOp = Optional.empty();
		waitingNum = Optional.empty();
		bufferedNum = Optional.empty();
	}

	/**
	 * Stores the current number on the screen in memory.
	 */
	public void storeCurrentNumber() {
		if (bufferedNum.isPresent()) {
			throw new IllegalStateException();
		}

		bufferedNum = waitingNum;
		waitingNum = Optional.of(currentNumber());
		resetCurrentNumOnNextAppend = true;
	}

	/**
	 * Stores the current operation in memory.
	 */
	public void storeOp(Command cmd) {
		if (bufferedOp.isPresent()) {
			throw new IllegalStateException();
		}
		bufferedOp = waitingOp;
		waitingOp = Optional.of(cmd);

	}

	/**
	 * Appends a new digit or decimal to the number currently on the screen.
	 * Duplicate decimals are ignored.
	 */
	public void appendToCurrentNum(char token) {
		if (resetCurrentNumOnNextAppend) {
			resetCurrentNumOnNextAppend = false;
			currentNum = "0";
			currentNumNegated = false;
		}

		if (currentNum.equals("0")) {
			if (token == '0') {
				// do nothing
			} else if (token == '.') {
				currentNum = "0.";
			} else {
				currentNum = String.valueOf(token);
			}
		} else {
			currentNum = currentNum + token;
		}
	}

	/**
	 * Indicates whether the current on-screen number is a decimal.
	 */
	public boolean currentNumIsDecimal() {
		return currentNum.contains(".");
	}

	/**
	 * Negates the current on-screen number.
	 */
	public void negateCurrentNum() {
		// Special case where the number is to be reset, and the first operation is a
		// negation.
		// This works differently than the OSX calculator but IMO makes more sense.
		// (eg: 1+!5) should equal -4
		if (resetCurrentNumOnNextAppend) {
			currentNum = "0";
			currentNumNegated = true;
			resetCurrentNumOnNextAppend = false;
		} else {
			currentNumNegated = !currentNumNegated;
		}
	}

	/**
	 * Takes the percent value of the current on-screen number (ie: divide by 100).
	 */
	public void percentifyCurrentNum() {
		updateCurrentNumber(currentNumber().divide(ONE_HUNDRED));
	}

	/**
	 * Updates the current on screen number from a BigDecimal.
	 */
	private void updateCurrentNumber(BigDecimal newNum) {
		currentNum = newNum.toPlainString();
		if (currentNum.startsWith("-")) {
			currentNum = currentNum.substring(1);
			currentNumNegated = true;
		} else {
			currentNumNegated = false;
		}
	}

	/**
	 * Computes a usable BigDecimal from the current on-screen number.
	 */
	public BigDecimal currentNumber() {
		if (currentNumNegated) {
			return new BigDecimal("-" + currentNum);
		}
		return new BigDecimal(currentNum);
	}

	/**
	 * Indicates if there exists a waiting operation.
	 */
	public boolean waitingOperationExists() {
		return waitingOp.isPresent();
	}

	/**
	 * Indicates if the waiting operation is of high precedence.
	 */
	public boolean waitingOperationExistsAndIsHighPrecidence() {
		return waitingOperationExists() && (waitingOp.get().isMultiply() || waitingOp.get().isDivide());
	}

	/**
	 * Indicates if the waiting operation is of low precedence.
	 */
	public boolean waitingOperationExistsAndIsLowPrecidence() {
		return waitingOperationExists() && (waitingOp.get().isPlus() || waitingOp.get().isMinus());
	}

	/**
	 * Executes the waiting operation and updates the on-screen number.
	 */
	public void executeWaitingOp() {
		BigDecimal second = currentNumber();
		BigDecimal first = waitingNum.get();
		Command op = waitingOp.get();
		if (op.isDivide()) {
			updateCurrentNumber(first.divide(second));
		} else if (op.isMultiply()) {
			updateCurrentNumber(first.multiply(second));
		} else if (op.isPlus()) {
			updateCurrentNumber(first.add(second));
		} else if (op.isMinus()) {
			updateCurrentNumber(first.subtract(second));
		} else {
			throw new RuntimeException("Unknown op");
		}

		waitingNum = bufferedNum;
		bufferedNum = Optional.empty();
		waitingOp = bufferedOp;
		bufferedOp = Optional.empty();
	}

	@Override
	public String toString() {
		return "[Mem: cn=" + (currentNumNegated ? '-' : "") + currentNum + "; waitingNum=" + waitingNum
				+ "; bufferedNum=" + bufferedNum + " | waitingOp=" + waitingOp + "; bufferedOp=" + bufferedOp + "]";
	}

}
