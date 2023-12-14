package input;

import java.util.Optional;

public final class Command {

	private final Button btn;

	private final char token;

	private Command(Button btn, char token) {
		this.btn = btn;
		this.token = token;
	}

	public static Optional<Command> of(char token) {
		final Optional<Button> btn = Button.from(token);
		if (btn.isPresent()) {
			return Optional.of(new Command(btn.get(), token));
		} else {
			return Optional.empty();
		}
	}

	public char getToken() {
		return token;
	}

	public boolean isNumber() {
		return btn == Button.NUMBER;
	}

	public boolean isDecimal() {
		return btn == Button.DECIMAL;
	}

	public boolean isNegate() {
		return btn == Button.NEGATE;
	}

	public boolean isPercent() {
		return btn == Button.PERCENT;
	}

	public boolean isMultiply() {
		return btn == Button.MULTIPLY;
	}

	public boolean isDivide() {
		return btn == Button.DIVIDE;
	}

	public boolean isPlus() {
		return btn == Button.PLUS;
	}

	public boolean isMinus() {
		return btn == Button.MINUS;
	}

	public boolean isEquals() {
		return btn == Button.EQUALS;
	}

	public boolean isClear() {
		return btn == Button.CLEAR;
	}

	@Override
	public String toString() {
		return "[" + btn.name() + " | " + token + "]";
	}

}
