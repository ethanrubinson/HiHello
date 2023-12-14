package input;

import java.util.Optional;
import java.util.regex.Pattern;

public enum Button {

	PLUS("\\+"), MINUS("-"), MULTIPLY("\\*"), DIVIDE("\\/"), EQUALS("="),

	NEGATE("!"), CLEAR("c"), PERCENT("%"),

	NUMBER("[0-9]"), DECIMAL("\\.");

	private final Pattern tokenMatcher;

	private Button(String regexp) {
		tokenMatcher = Pattern.compile(regexp);
	}

	public static Optional<Button> from(char token) {
		for (Button btn : Button.values()) {
			if (btn.tokenMatcher.matcher(String.valueOf(token)).matches()) {
				return Optional.of(btn);
			}
		}
		return Optional.empty();
	}

}
