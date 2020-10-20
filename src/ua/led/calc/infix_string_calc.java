package ua.led.calc;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Double_infix_calc implements ActionListener {

	// for Algorithm
	private static Stack<Double> valueStack = new Stack<Double>(); // initializing values stack integers not sure how to
																	// make this generic type
	private static Stack<String> operatorStack = new Stack<String>(); // initializing operators stack of strings
	private static ArrayList<String> expressionList = new ArrayList<String>();
	private static int index = 0;
	private static String last = "";

	private static DecimalFormat df = new DecimalFormat("#.#####");
	private static double algorithmResult = 0.0; // Result of calculations
	private String expression = ""; // user's expression parsed in algorithm
	static boolean SynError = false, MathError = false; // error to flag Syntax error and Math error

	// for GUI
	JFrame frame = new JFrame("Calculator"); // Previous names - Infix Calculator, Double Infix Calc

	private static JTextField expressionField = new JTextField("0"); // Field for holding an expression
	private JTextField M1Field = new JTextField("0");
	double dM1 = 0;
	private JTextField M2Field = new JTextField("0");
	double dM2 = 0;

	private JButton button_sqrt = new JButton("√");
	private JButton button_cbrt = new JButton("3^√");
	// TODO 1/x
	private JButton overx = new JButton("1/x");

	private JButton button_m1p = new JButton("M1+");
	private JButton button_m2p = new JButton("M2+");
	private JButton button_m1r = new JButton("M1R");
	private JButton button_m2r = new JButton("M2R");
	private JButton button_mc = new JButton("MC");
	private static JButton help = new JButton("Help");

	// History Text area for holding previous calculations
	private static JTextArea history = new JTextArea(14, 40);
	private static Font BigFontTR = new Font("TimesRoman", Font.PLAIN, 12);
	private static Font BigFontTR1 = new Font("TimesRoman", Font.PLAIN, 16);
	private static Font BigFontTR2 = new Font("TimesRoman", Font.PLAIN, 18);

	// Check boxes
	private static JRadioButtonMenuItem degOrRad = new JRadioButtonMenuItem("Deg");
	private static JRadioButtonMenuItem LargerText = new JRadioButtonMenuItem("Larger Font");

	public Double_infix_calc() {
		JPanel operations = new JPanel();
		JPanel ExprPanel = new JPanel();
		JPanel historyPanel = new JPanel();
		JPanel memoryPanel = new JPanel();

		ExprPanel.setLayout(new GridLayout(1, 5)); // the panel uses a GridLayout
		operations.setLayout(new GridLayout(6, 1));
		historyPanel.setLayout(new GridLayout(1, 1));
		memoryPanel.setLayout(new GridLayout(2, 6));

		ExprPanel.add(expressionField);
//		expressionField.setCaretPosition(0);

		operations.add(button_sqrt);
		operations.add(button_cbrt);
		operations.add(overx);

		historyPanel.add(history);

		memoryPanel.add(button_m1p);
		memoryPanel.add(button_m2p);
		memoryPanel.add(button_m1r);
		memoryPanel.add(button_m2r);
		memoryPanel.add(degOrRad);
		memoryPanel.add(M1Field);
		memoryPanel.add(M2Field);
		memoryPanel.add(button_mc);
		memoryPanel.add(help);
		memoryPanel.add(LargerText);

		M1Field.setEditable(false);
		M2Field.setEditable(false);
		history.setEditable(false);

		frame.setLayout(new BorderLayout()); // frame for zero and decimal
		frame.add(ExprPanel, BorderLayout.NORTH);
		frame.add(historyPanel, BorderLayout.WEST);
		frame.add(operations, BorderLayout.EAST);
		frame.add(memoryPanel, BorderLayout.SOUTH);
		frame.add(new JScrollPane(history));

		expressionField.addActionListener(this);
		button_sqrt.addActionListener(this);
		button_cbrt.addActionListener(this);
		overx.addActionListener(this);
		button_m1p.addActionListener(this);
		button_m2p.addActionListener(this);
		button_m1r.addActionListener(this);
		button_m2r.addActionListener(this);
		button_mc.addActionListener(this);
		degOrRad.addActionListener(this);
		LargerText.addActionListener(this);
		help.addActionListener(this);

		history.setFont(BigFontTR);

		frame.pack();
		frame.setSize(550, 450);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calc2.png")));
		frame.setVisible(true);

		expressionField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					System.out.println("Нажата вверх");

					if (index == expressionList.size())
						last = expressionField.getText();

					index--;
					if (index < 0)
						index = 0;
					System.out.println(index);

					if (!(expressionList.size() == 0))
						expressionField.setText(expressionList.get(index));
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					System.out.println("Нажата вниз");

					index++;
					if (index <= expressionList.size() - 1) {
						if (!expressionList.isEmpty())
							expressionField.setText(expressionList.get(index));
					} else if (index >= expressionList.size()) {
						index = expressionList.size();
						expressionField.setText(last);
					}
					System.out.println(index);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}
		});
	}

	public String insertText(String originalString, String stringToBeInserted, int caretPos) {
		// Create a new string
		String newString = new String();

		for (int i = 0; i < originalString.length(); i++) {
			// Insert the original string character
			// into the new string
			newString += originalString.charAt(i);
			if (i == caretPos - 1) {
				// Insert the string to be inserted
				// into the new string
				newString += stringToBeInserted;
			}
		}
		return newString;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource() == expressionField) {
				String result_text;

				expression = expressionField.getText(); // if enter is pressed we take expression from jtextfield
				String user_expr = expression;
				expressionList.add(expression);
				index = expressionList.size();

				expression = prepare(expression); // preparing expr (replacing math functions by shortened ones)
				algorithmResult = algorithm(expression); // then pass expr to our algorithm

				if (!SynError && !MathError) // if expression has no syntax errors
				{
					if (Math.abs(algorithmResult) > 0.0001 || algorithmResult == 0) {
						result_text = (df.format(algorithmResult)); // converting result which is double into a string
																	// to be displayed in history text area
						result_text = result_text.replace(",", ".");
					} else
						result_text = Double.toString(algorithmResult);

//					if (result_text.equals("Infinity"))
//						result_text = "Inf";
					expressionField.setText(result_text);
				} else if (MathError)
					result_text = "Math error";
				else
					result_text = "Syntax error";

				history.setText(history.getText() + ">  " + user_expr + " = " + result_text + "\n");

				System.out.println("");

				algorithmResult = 0.0;

				/*
				 * operator = null; operand1 = 0; operand2 = 0;
				 */

				if (valueStack.isEmpty() == false)
					valueStack.clear();
				operatorStack.clear();
			}

			if (event.getSource() == button_sqrt) {
				int caretPos = expressionField.getCaretPosition();
				String insertString = "√";
				String newString = insertText(expressionField.getText(), insertString, caretPos);
				expressionField.setText(newString);
				expressionField.setCaretPosition(caretPos + insertString.length());
			}

			if (event.getSource() == button_cbrt) {
				int caretPos = expressionField.getCaretPosition();
				String insertString = "cbrt(";
				String newString = insertText(expressionField.getText(), insertString, caretPos);
				expressionField.setText(newString);
				expressionField.setCaretPosition(caretPos + insertString.length());
			}

			if (event.getSource() == overx) {
				String result_text;
				expression = expressionField.getText(); // if enter is pressed we take expression from jtextfield
				String user_expr = "1/" + expression;
				expressionList.add(user_expr);
				index = expressionList.size();

				expression = prepare(user_expr); // preparing expr (replacing math functions by shortened ones)
				algorithmResult = algorithm(expression); // then pass expr to our algorithm
				
				if (!SynError && !MathError) // if expression has no syntax errors
				{
					if (Math.abs(algorithmResult) > 0.0001 || algorithmResult == 0) {
						result_text = (df.format(algorithmResult)); // converting result which is double into a string
																	// to be displayed in history text area
						result_text = result_text.replace(",", ".");
					} else
						result_text = Double.toString(algorithmResult);

//					if (result_text.equals("Infinity"))
//						result_text = "Inf";
					expressionField.setText(result_text);
				} else if (MathError)
					result_text = "Math error";
				else
					result_text = "Syntax error";

				history.setText(history.getText() + ">  " + user_expr + " = " + result_text + "\n");

				System.out.println("");

				algorithmResult = 0.0;

				/*
				 * operator = null; operand1 = 0; operand2 = 0;
				 */

				if (valueStack.isEmpty() == false)
					valueStack.clear();
				operatorStack.clear();
			}

			if (event.getSource() == button_m1p)
				try {
					M1Field.setText(Double.toString(
							Double.parseDouble(M1Field.getText()) + Double.parseDouble(expressionField.getText())));
				} catch (Exception e) {
				}

			if (event.getSource() == button_m2p)
				try {
					M2Field.setText(Double.toString(
							Double.parseDouble(M2Field.getText()) + Double.parseDouble(expressionField.getText())));
				} catch (Exception e) {
				}

			if (event.getSource() == button_m1r) {
				if (!(Double.parseDouble(M1Field.getText()) == 0))
					expressionField.setText(expressionField.getText() + M1Field.getText());
			}

			if (event.getSource() == button_m2r) {
				if (!(Double.parseDouble(M2Field.getText()) == 0))
					expressionField.setText(expressionField.getText() + M2Field.getText());
			}

			if (event.getSource() == button_mc) {
				M1Field.setText("0");
				M2Field.setText("0");
			}

			if (event.getSource() == degOrRad) {
				if (degOrRad.isSelected())
					degOrRad.setText("Rad");
				else
					degOrRad.setText("Deg");
			}

			if (event.getSource() == help) {
				history.setText(history.getText() + "\n"
						+ "  You can miss paranthesis after one digit operators, for example \"3+sin30\"\n"
						+ "  It can be used for:\n"
						+ "  √, 3^√, sin, cos, tan, cot, asin, acos, atan, actan, sinh, cosh, tanh, ctanh;\n"
						+ "  Use check box below to swap between radian and grad calculation (for trigonometric);\n\n"
						+ "  " + "Supported operators:\n" + "  +,   -,   *,   ×,   /,   ^\n"
						+ "  sqrt(x) - for square root, or button √\n" + "  3^√(x) - for cubic root, or button 3^√\n"
						+ "  Also you can use y-root by typing y^sqrt(x), instead of x^(1/y)\n"
						+ "  sin, asin, sinh - all kinds of trigonometric functions\n"
						+ "  cot, acot, coth - will be replaced with - 1/tan, 1/atan, 1/tanh\n"
						+ "  log, ln - logarithm functions\n" + "  % - percentage\n" + "  ! - factorial\n"
						+ "  abs - absolute\n" + "  <, >, =, <=, >=, <> - logical functions\n"
						+ "  diff, int - are not supported yet\n\n");
			}

			if (event.getSource() == LargerText) {
				if (!LargerText.isSelected()) {
					LargerText.setText("Larger Font");
					history.setFont(BigFontTR);
					expressionField.setFont(BigFontTR);
				} else {
					LargerText.setText("Smaller Font");
					history.setFont(BigFontTR1);
					expressionField.setFont(BigFontTR2);
				}
			}

			if (event.getSource() == history)
				expressionField.requestFocus();

			expressionField.requestFocus();
		} catch (Exception e) {
		}
	}

	// Calculator

	// boolean functions
	public static boolean isTrigFunc(String token) {
		if (token.equals("s") || token.equals("c") || token.equals("t") || token.equals("q") || token.equals("w")
				|| token.equals("r") || token.equals("h") || token.equals("j") || token.equals("k"))
			return true;
		else
			return false;
	}

	public static boolean isOperator(String token) {
		if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^")
				|| isTrigFunc(token) || token.equals("√") || token.equals("b") || token.equals("y") || token.equals("g")
				|| token.equals("n") || token.equals("!") || token.equals("%") || token.equals("a") || token.equals("<")
				|| token.equals(">") || token.equals("m") || token.equals("v") || token.equals("z")
				|| token.equals("u"))
			return true;
		else
			return false;
	}

	public static boolean isNumber(String token) {
		if ((!token.equals("(") && !token.equals(")") && !token.equals("+") && !token.equals("-") && !token.equals("*")
				&& !token.equals("/") && !token.equals("^") && !token.equals("√") && !token.equals("b")
				&& !token.equals("y") && !isTrigFunc(token) && !token.equals("g") && !token.equals("n")
				&& !token.equals("!") && !token.equals("%") && !token.equals("d") && !token.equals("i")
				&& !token.equals("a") && !token.equals("<") && !token.equals(">") && !token.equals("m")
				&& !token.equals("v") && !token.equals("z") && !token.equals("u")) || token.equals("~")) {
			return true;
		} else
			return false;
	}

	public static boolean oneDigit(String op0) {
		if (op0.equals("√") || op0.equals("b") || op0.equals("s") || op0.equals("c") || op0.equals("t")
				|| op0.equals("q") || op0.equals("w") || op0.equals("r") || op0.equals("h") || op0.equals("j")
				|| op0.equals("k") || op0.equals("g") || op0.equals("n") || op0.equals("!") || op0.equals("%")
				|| op0.equals("a"))
			return true;
		else
			return false;
	}

	public static boolean isComp(String op0) {
		if (op0.equals("<") || op0.equals(">") || op0.equals("m") || op0.equals("v") || op0.equals("z")
				|| op0.equals("u"))
			return true;
		else
			return false;
	}

	// Method to determine precedence, using order of operations
	// currently taking in a token, but might be better to use only operators.
	public static int priority(String token) {
		int precedence = 0;
		if (token.equals("(") || token.equals(")"))
			precedence = 0;
		else if (token.equals("%"))
			precedence = 7;
		else if (token.equals("√") || token.equals("b") || token.equals("y") || isTrigFunc(token) || token.equals("g")
				|| token.equals("n") || token.equals("!") || token.equals("a"))
			precedence = 6;
		else if (token.equals("^"))
			precedence = 5;
		else if (token.equals("*") || token.equals("/"))
			precedence = 4;
		else if (token.equals("+") || token.equals("-"))
			precedence = 3;
		else if (isComp(token))
			precedence = 2;
		else
			precedence = 1;
		// numbers have lowest precedence
		return precedence;
	}

	// Preparation of string to fit all requirements
	//
	public static String prepare(String expr) {
		try {
			expr = expr.replace(",", ".");
			expr = expr.replace("--", "-");
			expr = expr.replace("–", "-");
			expr = expr.replace("+-", "-");
			expr = expr.replace("-+", "-");
			expr = expr.replace("++", "+");
			expr = expr.replace("×", "*");

			// Math functions
			/*
			 * Quick information: a - absolute
			 * 
			 * h - sine hyperbolic j - cosine hyperbolic k - tangent hyperbolic 1/k -
			 * cotangent hyperbolic
			 * 
			 * q - arc sine w - arc cosine r - arc tan 1/r - arc cot
			 * 
			 * s - sin (a/b) c - cos (b/a) t - tan (sin/cos), cot (1/tan)
			 * 
			 * g - log ( tenth logarithm, log10) n - ln (natural logarithm, log)
			 *
			 * √ - square root b - cubic root y - y-root
			 * 
			 * % - percents ! - factorial
			 * 
			 * // TODO d - differential // TODO i - integral
			 *
			 * e - exponent (2.718281828459045) p - pi (3.141592653589793)
			 * 
			 */

			expr = expr.replace("abs", "a");
			expr = expr.replace("sinh", "h");
			expr = expr.replace("cosh", "j");
			expr = expr.replace("tanh", "k");
			expr = expr.replace("coth", "1/k");

			expr = expr.replace("asin", "q");
			expr = expr.replace("acos", "w");
			expr = expr.replace("atan", "r");
			expr = expr.replace("acot", "1/r");

			expr = expr.replace("sin", "s");
			expr = expr.replace("cos", "c");
			expr = expr.replace("tan", "t");
			expr = expr.replace("cot", "1/t");

			expr = expr.replace("log", "g");
			expr = expr.replace("ln", "n");

			expr = expr.replace("sqrt", "√");
			expr = expr.replace("cbrt", "b");
			expr = expr.replace("^√", "y");
			expr = expr.replace("^sqrt", "y");

			expr = expr.replace("<=", "m");
			expr = expr.replace(">=", "v");
			expr = expr.replace("==", "u");
			expr = expr.replace("<>", "z");

			// these two are not ready yet
			expr = expr.replace("diff", "d");
			expr = expr.replace("int", "o");

			// math constants
			expr = expr.replace("pi", "3.141592653589793");
			expr = expr.replace("E", "*10^");
			expr = expr.replace("e", "2.718281828459045");
			expr = expr.replace("Infinity", "~");
			expr = expr.replace("Inf", "~");
			expr = expr.replace("∞", "~");
			expr = expr.replace("~", "(1/0.0)");

			expr = expr.replace("[", "");
			expr = expr.replace("]", "");
			expr = expr.replace("null", "");
			expr = expr.replace(",", ".");
			expr = expr.replace(" ", "");

			System.out.println("prepare.expr = " + expr); // Printing prepared expression before sending to algorithm
			System.out.println("");
		} catch (Exception e) {
			SynError = true;
		}

		return expr;
	}

	// need to continue using generic type when using ArrayStacks but not sure how
	// to do this
	public static double algorithm(String expr) {
		// booleans checking for: minus sign, trigonometric function, root
		System.out.println(valueStack);
		boolean isPrevBraket = false, isPrevMinus = false;
		SynError = false;
		MathError = false;

		String token; // String for holding token

		expr = "(" + expr + ")"; // adding parenthesis to expression to find the end of the whole expression
		expr = expr.replace(" ", ""); // replacing all spaces in the expression

		StringTokenizer tokenizer = new StringTokenizer(expr, "()+-*/^√bysctqwrhjkgna!%<>mvzudo", true);
		// +, -, *, /, ^, √, b, s, c, t, q, w, r, h, j, k, g, n, a, !, %, <, <=, >=,
		// >, =, <> ready;
		// d, i symbolic TODO

		try {
			while (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken();
				System.out.println("tokenizer.nextToken=" + token);

				// Pushing operators and numbers,

				// pushing left parenthesis.
				if (token.equals("(") || token.equals("*") || token.equals("/") || token.equals("^")
						|| isTrigFunc(token) || token.equals("√") || token.equals("b") || token.equals("y")
						|| token.equals(">") || token.equals("<") || token.equals("m") || token.equals("v")
						|| token.equals("z") || token.equals("u")) { // to check if there is minus after (, *, /, ^,
																		// trig, roots

					if (token.equals("(")) {
						operatorStack.push(token);
					}

					isPrevBraket = true; // to check if there is minus after (, *, /, ^, trig, roots
				}

				// checking if there would be minus after *, /, ^, trigonometric function, root
				if (token.equals("-")) {
					if (isPrevBraket == true) {
						// if it is minus and previous token was paranthesis then next number wil be
						// negative
						isPrevMinus = true;
						isPrevBraket = false;
						token = tokenizer.nextToken();
						System.out.println("tokenizer.nextToken(-)=" + token);
					}
				}

				// if input is not an operator but instead a number do this
				double num = 0;
				if (isNumber(token)) {

					isPrevBraket = false;

					num = Double.parseDouble(token); // taking the string number and converting it to a double

					if (isPrevMinus) {
						// if previously input was (, *, /, ^ and after that was minus we have negative
						// number
						num = -num;
						isPrevMinus = false; // return to normal state
					}

					valueStack.push(num); // adding number to value stack
				}

				// Main calculation methods

				if (token.equals(")")) {
					if (isPrevBraket) {
						// expression has void brakets, exiting
						SynError = true;
						System.out.println("error = true, due to void brakets in expression");
						return -1;
					}

					isPrevBraket = false;
					// while thing on top of operator stack is not a left parenthesis do this
					while (operatorStack.peek().equals("(") == false) {
						System.out.println("paranthesis - send to calc");
						sendToCalc();
					}
					operatorStack.pop(); // discarding left parenthesis
				}

				if (isOperator(token)) { // if input is an operator do this
					if (token.equals("*") || token.equals("/") || token.equals("^") || token.equals("√")
							|| token.equals("b") || token.equals("y") || isTrigFunc(token))
						isPrevBraket = true;
					else
						isPrevBraket = false;

					// while the operator stack is not empty and the thing on top of
					// operator stack has the same or greater precedence as the input do this
					while (operatorStack.isEmpty() == false && priority(operatorStack.peek()) >= priority(token)) {
						System.out.println("operator - send to calc");
						sendToCalc();
					}
					operatorStack.push(token);
				}
			}

			if (operatorStack.empty() /* && valueStack.size() == 1 */) // after algorithm ends value stack should have
																		// one value and operator stack should be empty
				algorithmResult = (double) valueStack.peek();
			else {
				SynError = true;
				System.out.println(
						"err = true, because of last unused operator, operator.peek = " + operatorStack.peek());
			}
		} catch (Exception e) {
			System.out.println("Syntax Error, may be because of unregistered symbol, or code error");
			SynError = true;
		}

		if (algorithmResult == -0.0)
			algorithmResult = 0.0;

		return algorithmResult;
	}

	public static double sendToCalc() {

		String op0 = (String) operatorStack.pop();// storing operator before popping it
		double op1, op2;
		if (oneDigit(op0)) {
			System.out.println("test");
			op1 = (double) valueStack.pop(); // storing value before popping
			valueStack.push(eval(op0, op1));
			System.out.println("evaluate: " + op0 + ", " + op1 + " = " + valueStack.peek());
		} else {
			op1 = (double) valueStack.pop(); // storing value before popping
			if (op0.equals("y")) {
				op0 = "^";
				op2 = op1;
				op1 = 1 / (double) valueStack.pop(); // storing value before popping
			} else {
				op2 = (double) valueStack.pop(); // storing value before popping
			}

			valueStack.push(evaluate(op0, op1, op2));
			System.out.println("evaluate: " + op0 + ", " + op1 + ", " + op2 + " = " + valueStack.peek());
		}

		double sendToCalcResult = (double) valueStack.peek();

		return sendToCalcResult;

	}

	// want to pass operator and values from previous method to return result
	// of an evaluation of the expression
	public static double evaluate(String op0, double op1, double op2) {
		if (op1 == 0 && op2 == 0 && op0.equals("/"))
			MathError = true;

		if (op0.equals("^")) {
			algorithmResult = Math.pow(op2, op1); // op2^op1 order important first in first out
		}
		if (op0.equals("+")) {
			algorithmResult = op1 + op2; // order not important
		}
		if (op0.equals("-")) {
			algorithmResult = op2 - op1; // order important
		}
		if (op0.equals("*")) {
			algorithmResult = op1 * op2; // order not important
		}
		if (op0.equals("/")) {
			System.out.println(op2 + " / " + op1);
			algorithmResult = op2 / op1; // order important
		}
		System.out.println(algorithmResult);

		if (op0.equals("<")) {
			if (op2 < op1)
				return 1;
			else
				return 0;
		}

		if (op0.equals(">")) {
			if (op2 > op1)
				return 1;
			else
				return 0;
		}

		if (op0.equals("m")) {
			if (op2 <= op1)
				return 1;
			else
				return 0;
		}

		if (op0.equals("v")) {
			if (op2 >= op1)
				return 1;
			else
				return 0;
		}

		if (op0.equals("z")) {
			if (op2 != op1)
				return 1;
			else
				return 0;
		}

		if (op0.equals("u")) {
			if (op2 == op1)
				return 1;
			else
				return 0;
		}

		return algorithmResult;
	}

	public static double eval(String op0, double op1) {
		double rad = op1;
		if (!degOrRad.isSelected())
			rad = Math.toRadians(op1);

		if ((op0.equals("√") || op0.equals("b")) && op1 < 0)
			MathError = true;

		if (op0.equals("a"))
			algorithmResult = Math.abs(op1);

		if (op0.equals("%"))
			algorithmResult = op1 * 0.01;

		if (op0.equals("√"))
			algorithmResult = Math.sqrt(op1); // square root(op1)

		if (op0.equals("b"))
			algorithmResult = Math.cbrt(op1); // cubic root(op1)

		if (op0.equals("s")) {
			algorithmResult = Math.sin(rad); // sine(op1)
			if (algorithmResult == 2.65358979335273E-6 || algorithmResult == -5.307179586686775E-6
					|| algorithmResult == 1.2246467991473532E-16 /* s- 180 */
					|| algorithmResult == -1.2246467991473532E-16 /* s- 180 */
					|| algorithmResult == -2.4492935982947064E-16 /* s 360 */
					|| algorithmResult == 2.4492935982947064E-16 || algorithmResult == 0.05480361890294856
					|| algorithmResult == 0.10944251483185016)
				algorithmResult = Math.round(algorithmResult);
		}
		if (op0.equals("c")) {
			algorithmResult = Math.cos(rad); // cosine(op1)
			if (algorithmResult == 1.3267948966775328E-6 || algorithmResult == -3.9803846904673455E-6
					|| algorithmResult == 6.123233995736766E-17 || algorithmResult == -1.8369701987210297E-16
					|| algorithmResult == 0.9984986753938687 || algorithmResult == 0.9939931267102807)
				algorithmResult = Math.round(algorithmResult);
		}
		if (op0.equals("t")) {
			algorithmResult = Math.tan(rad); // tangent(op1)
			if (algorithmResult == -2.6535897933620727E-6 || algorithmResult == -5.3071795867615165E-6
					|| algorithmResult == -1.2246467991473532E-16 || algorithmResult == -2.4492935982947064E-16)
				algorithmResult = Math.round(algorithmResult);
			if (algorithmResult == 753695.9951408089 || algorithmResult == 251231.99835106044)
				algorithmResult = Math.sqrt(-1);
			if (algorithmResult == 1.633123935319537E16/* tan 90 */ || algorithmResult == -1.633123935319537E16 /*
																												 * tan
																												 * -90
																												 */
					|| algorithmResult == 5.443746451065123E15 /* tan 270 */
					|| algorithmResult == -5.443746451065123E15 /* tan -270 */)
				MathError = true;
		}

		if (op0.equals("g"))
			algorithmResult = Math.log10(op1); // log, log10(op1)
		// TODO test exceptions

		if (op0.equals("n"))
			algorithmResult = Math.log(op1); // ln, log(op1)
		// TODO test exceptions

		if (op0.equals("q"))
			algorithmResult = Math.asin(op1); // arc sine(op1)
		// TODO test exceptions

		if (op0.equals("w"))
			algorithmResult = Math.acos(op1); // arc cosine(op1)
		// TODO test exceptions

		if (op0.equals("r"))
			algorithmResult = Math.atan(op1); // arc tangent(op1)
		// TODO test exceptions

		if (op0.equals("h"))
			algorithmResult = Math.sinh(op1); // sinh(op1)
		// TODO test exceptions

		if (op0.equals("j"))
			algorithmResult = Math.cosh(op1); // cosh(op1)
		// TODO test exceptions

		if (op0.equals("k"))
			algorithmResult = Math.tanh(op1); // tanh(op1)
		// TODO test exceptions

		if (op0.equals("!"))
			algorithmResult = factorial((int) op1); // factorial(op1)
		// TODO test
		return algorithmResult;
	}

	public static int factorial(int n) {
		double x1 = 1, temp3 = 0;
		if (n < 0) {
			MathError = true;
			return 0;
		} else if (n == 0 || n == 1)
			return 1;
		else {
			for (int i = 2; i < n + 1; i++) {
				temp3 = x1 * i;
				System.out.println(x1 + " * " + i + " = " + temp3);
				x1 = temp3;
			}
			return (int) temp3;
		}
		// return n * factorial(n-1);
	}

	public static int fibonacci(int n) {
		int x1 = 1, x2 = 1, temp3 = 0;
		for (int i = 0; i < n - 3; i++) {
			temp3 = x2 + x1;
			System.out.println(x1 + " + " + x2 + " = " + temp3);
			x1 = x2;
			x2 = temp3;
		}
		return temp3;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}

		new Double_infix_calc();
	}
}

class Complex {
	double x;
	double y;
}
