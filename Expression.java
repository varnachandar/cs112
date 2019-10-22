package app;

import java.io.*;
import java.util.*;

import structures.Stack;


public class Expression {

    public static String delims = " \t*+-/()[]";
    
    private static boolean within(String expr, String regular) {
    	regular = regular.replaceAll("\\s+", "").trim();
		char[] arrayOfC = regular.toCharArray();
		
		for (int i = 0; i < arrayOfC.length; i++) {
			if (expr.contains(Character.toString(arrayOfC[i]))) {
				return true;
			}
		} 
		
		return false;
    }
    
    private static float compute(float first, float second, char op) {
		switch (op) {
			case '+': return first + second;
			case '-': return first - second;
			case '*': return first * second;
			case '/': return first / second;
		} throw new NullPointerException("Cannot compute.");
    }
			
    /**
     * Complete this method
     * 
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    		String editedExpr = null;
    		editedExpr = expr.trim();
    		editedExpr = expr.replaceAll("\\s+", "");
    		editedExpr = expr.replaceAll("\\t+", "");
    		
    		StringTokenizer split = new StringTokenizer(editedExpr, " \t*+-/()]" + "1234567890");
    		
    		while (split.hasMoreTokens()) {
    			
    			String thisSection = split.nextToken();
    			
    			while (true) {
    				
    				if (thisSection.contains("[")) {
        				int temp = thisSection.indexOf("[");
        				Array addArray = new Array(thisSection.substring(0, temp));
        				
        				if (arrays.contains(addArray)) { break; }
        				arrays.add(addArray);	
        				
        				try {
        					thisSection = thisSection.substring(temp + 1);
        					if (thisSection.equals("")) { break; }
        				}
        				catch (NullPointerException over) { break; }
            		}
            		else {
            			Variable addVar = new Variable(thisSection);
            			
            			if (vars.contains(addVar)) { break; }
            			vars.add(addVar);		
            			break;
            		}
    			}
    		}
    }
    
	/**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Complete this method
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    		
    	String editedExpr = null;
    	editedExpr = expr.trim();
		editedExpr = expr.replaceAll("\\s+", "");
		editedExpr = expr.replaceAll("\\t+", "");
		
		float returnThis = 0;
		
		while (within(editedExpr,"[]")) {
			
			StringTokenizer split = new StringTokenizer(editedExpr, " \t*+-/()]");
			String temp = "";
			String nameOf = "";
			
			while (split.hasMoreTokens()) {
				
				temp = split.nextToken();
				
				try {
					nameOf = temp.substring(0, temp.indexOf("["));
					break;
				}
				catch (IndexOutOfBoundsException over) { continue; }
			}	
			
			int beginOfBracket = editedExpr.indexOf("[");
			int endOfBracket = 0;
			int counter = 0;
			
			for (int i = beginOfBracket; counter >= 0; i++) {
				
				if (editedExpr.charAt(i) == ']') {
					counter -= 1;
					
					if (counter == 0) { endOfBracket = i; break; }		
				}
				else if (editedExpr.charAt(i) == '[') {
					counter += 1;
				}
			}
			
			float value = evaluate(editedExpr.substring(beginOfBracket+1, endOfBracket),vars,arrays);
			
			for (int i = 0; i < arrays.size(); i++) {
				
				if (arrays.get(i).name.equals(nameOf)) {
					value = arrays.get(i).values[(int) value];
				}
			}	
			
			editedExpr = editedExpr.substring(0,beginOfBracket - nameOf.length()) + Float.toString(value) + editedExpr.substring(endOfBracket+1);	
			
		}
		
		while (within(editedExpr,"()")) {
			
			int beginOfParen = editedExpr.indexOf("(");
			int endOfParen = 0;
			int counter = 0;
			
			for (int i = beginOfParen; counter >= 0; i++) {
				
				if (editedExpr.charAt(i) == ')') {
					counter -= 1;
					if (counter == 0) { endOfParen = i; break; }		
				}
				else if (editedExpr.charAt(i) == '(') {
					counter += 1;
				}
			}
			
			float z = evaluate(editedExpr.substring(beginOfParen+1, endOfParen), vars, arrays);	
			editedExpr = editedExpr.substring(0,beginOfParen) + Float.toString(z) + editedExpr.substring(endOfParen+1);	
		}
		
		if (!within(editedExpr,delims) || checkIfNegative(editedExpr)) {
			
			try { return Float.parseFloat(editedExpr); }			
			catch (NumberFormatException over) {
				
				for (int i = 0; i < vars.size(); i++) {		
					if (vars.get(i).name.equals(editedExpr)) {
						return vars.get(i).value;
					}
				}
			}
		}
		
		else if (within(editedExpr,"*+-/")) {
			editedExpr = twoMinus(editedExpr);
			
			Stack<Float> backNum = new Stack<Float>();				
			StringTokenizer split = new StringTokenizer(editedExpr, " \t*+/()[]");
			
			while(split.hasMoreTokens()) {
				String check2 = split.nextToken();
				
				if (within(check2,"-") && !checkIfNegative(check2)){		
					StringTokenizer split2 = new StringTokenizer(check2, delims);
					while(split2.hasMoreTokens()) {
						backNum.push(evaluate(split2.nextToken(),vars,arrays));
					} 
				} else {
					backNum.push(evaluate(check2,vars,arrays));
				}
			}	
			Stack<Float> numbersInExp = new Stack<Float>();		
			while(!backNum.isEmpty()) {
				numbersInExp.push(backNum.pop());
			}	
			
			Stack<Character> tempStack = new Stack<Character>();
			
			for (int i = 0; i < editedExpr.length(); i++) {
	    			if (editedExpr.charAt(i) == '+' || 
	    					editedExpr.charAt(i) == '*'|| editedExpr.charAt(i) == '/'){
	    					tempStack.push(editedExpr.charAt(i) );
	    			}
	    			try {
	    				if (editedExpr.charAt(i) == '-' && containsNegativeNum(editedExpr.substring(i-1)) == false) {
		    				tempStack.push(editedExpr.charAt(i) );
		    			}
	    			}
	    			catch (StringIndexOutOfBoundsException over) {
	    				continue;			
	    			}
			}	
			Stack<Character> operationsInExp = new Stack<Character>();
			while(!tempStack.isEmpty()) {
				operationsInExp.push(tempStack.pop());
			}	
			
			while (!numbersInExp.isEmpty()) { 
				
				char currentOperation = operationsInExp.pop();
				
				try {
					char check = operationsInExp.peek();
					
					if (doFirst(currentOperation, check)) {		
						float a = numbersInExp.pop(), b = numbersInExp.pop();
						returnThis = compute(a,b,currentOperation);	
						
						if (!operationsInExp.isEmpty()) {
							numbersInExp.push(returnThis);
						} 
					}
					else {		
						float temp = numbersInExp.pop(); 
						char tempOp = currentOperation; 
						currentOperation = operationsInExp.pop();
						float a = numbersInExp.pop(), b = numbersInExp.pop();
						returnThis = compute(a,b,currentOperation);			
						numbersInExp.push(returnThis); 
						numbersInExp.push(temp);
						operationsInExp.push(tempOp);		
					}
				}
				catch (NoSuchElementException over) {		
					float a = numbersInExp.pop(),b = numbersInExp.pop();
					returnThis = compute(a,b, currentOperation); 			
				}
			}
		}
		return returnThis;
    }
       
    private static boolean doFirst(char oper1, char oper2) {
		if (oper1 == '+' || oper1 == '-' ) {
			if (oper2 == '*' || oper2 == '/') {
				return false;
			}
		} return true;
    }
    
    private static boolean checkIfNegative(String expr) {
  		expr.replaceAll("\\s+", "").trim(); 
  		
  		if (expr.charAt(0) == '-') { 
  			try { 
  				Float.parseFloat(expr.substring(1));
  				return true;
  			}
  			catch (NumberFormatException over){ return false; }
  		} return false;
      }
    
    private static String twoMinus(String expr) {
    	
    		String editedExpr = expr;
    		int twoNegatives = editedExpr.indexOf("--");
    		int negativePositive = editedExpr.indexOf("-+");
    		int postiveNegative = editedExpr.indexOf("+-");

    		while (twoNegatives > 0) { 
    			editedExpr = editedExpr.substring(0, twoNegatives) + "+" + editedExpr.substring(twoNegatives+2);
    			twoNegatives = editedExpr.indexOf("--", twoNegatives+1);
    		}
    		
    		while (negativePositive > 0) {
    			editedExpr = editedExpr.substring(0, negativePositive) + "-" + editedExpr.substring(negativePositive+2);
    			negativePositive = editedExpr.indexOf("-+", negativePositive+1);
    		}
    		
    		while (postiveNegative > 0) {
    			editedExpr = editedExpr.substring(0, postiveNegative) + "-" + editedExpr.substring(postiveNegative+2);
    			postiveNegative = editedExpr.indexOf("+-", postiveNegative+1);
    		}
    		
    		return editedExpr;
    }
    
    
    private static boolean containsNegativeNum(String expr) {
    		expr.replaceAll("\\s+", "").trim();
    		
    		for (int i = 0; i < expr.length(); i++) {
    			if (expr.charAt(i) == '-') { 
    				try {
    					if (expr.charAt(i-1) == '*' || expr.charAt(i-1) == '/') {
    						StringTokenizer split = new StringTokenizer(expr.substring(i+1),delims);
    						Float.parseFloat(split.nextToken());
    						return true;
    					}
    				}
    				catch (NumberFormatException over) { return false; }
    				catch (StringIndexOutOfBoundsException over) {
    					StringTokenizer split = new StringTokenizer(expr.substring(i+1),delims);
    					Float.parseFloat(split.nextToken());
    				}
    			} 
    		} return false;
    }
}