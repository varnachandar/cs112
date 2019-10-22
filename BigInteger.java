package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		String trimmedString = integer.trim();
		int StringLength = trimmedString.length();
		int counter = 0;
		int digitCounter = 0;
		int zeroCounter = 0;
		int temp;
		char current; 
		BigInteger returnThis = new BigInteger();
		DigitNode add = new DigitNode(-1, null);

		if(trimmedString.isEmpty()) {return null;}

		for(int loopIter = 0; loopIter<StringLength; loopIter++){
			counter ++;

			current = trimmedString.charAt(loopIter);

			if(current == '+'&& counter==1) {continue;}
			else if(current == '-' && counter==1){returnThis.negative = true; continue;}
			else if(current == '0'&& digitCounter==0) {zeroCounter ++; continue;}
			else if(Character.isDigit(current)){
				digitCounter++;

				temp = Character.getNumericValue(current);
				if(digitCounter==1) {add = new DigitNode(temp, null); returnThis.numDigits ++;}
				else {add = new DigitNode(temp, add); returnThis.numDigits ++;}

			} else {throw new IllegalArgumentException();}
			}
		
		if(digitCounter==0&&zeroCounter>0) {
			returnThis.front = new DigitNode(0, null);
			returnThis.numDigits ++; 
			System.out.println("number of digits: " + returnThis.numDigits);
			return returnThis;
			}
		
		returnThis.front = add;
		System.out.println("number of digits: " + returnThis.numDigits);
		return returnThis;
		}
	
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		BigInteger returnThis = new BigInteger();
		
		DigitNode linkedList1 = first.front;
		DigitNode linkedList2 = second.front;
		
		if(linkedList1==null && linkedList2==null) {return null;}
		else if(linkedList1==null) {return second;}
		else if(linkedList2==null) {return first;}
		
		int addedNum = 0;
		int add = 0;
		int temp = 0;
		int count = 0;
		
		DigitNode addedNode = new DigitNode(-1, null);
		DigitNode addedNodeContinued = new DigitNode(-1,null);
		
		
		if((first.negative==true&&second.negative==false)||(first.negative==false&&second.negative==true)){
			int count1 = 0;
			int count2 = 0;
			
			for(DigitNode countNums = first.front; countNums!=null; countNums=countNums.next) {count1 ++;}
			for(DigitNode countNums = second.front; countNums!=null; countNums=countNums.next) {count2 ++;}
			
			if(count1==count2) {
				
				DigitNode check1 = first.front;
				DigitNode check2 = second.front;
	
				
				while(count1==count2&&check1!=null&&check2!=null) {
					if(check1.digit==check2.digit) {
						check1 = check1.next;
						check2 = check2.next;
					}
					else if(check1.digit>check2.digit) {count1 = 1; count2 = 0;}
					else if(check1.digit<check2.digit) {count1 = 0; count2 = 1;}	
				}
				
				if(check1==null&&check2==null) {
					DigitNode zeroFront = new DigitNode(0, null);
					returnThis.front = zeroFront;
					returnThis.numDigits ++;
					System.out.println("number of digits: " + returnThis.numDigits);
					return returnThis;
				}
			}
			
			if(count1>count2) {
				
				while(linkedList1!=null||linkedList2!=null){
					count++;

					if(linkedList1 == null){addedNum = linkedList2.digit;}
					else if(linkedList2 == null){addedNum = linkedList1.digit;}
					else{addedNum = linkedList1.digit - linkedList2.digit;}
					

					if(addedNum<0){
						
						if(linkedList1.next!=null) {linkedList1.next.digit = linkedList1.next.digit-1;}
						add = addedNum+10;

						if(count == 1) {addedNode = new DigitNode(add, null); returnThis.numDigits ++;}
						else if(count == 2){
							addedNode.next = new DigitNode(add, null);
							returnThis.numDigits ++;
							addedNodeContinued = addedNode.next;
						}
						else{
							addedNodeContinued.next = new DigitNode(add, null);
							returnThis.numDigits ++;
							addedNodeContinued = addedNodeContinued.next;

						}
					} else{

						if(count == 1) {addedNode = new DigitNode(addedNum, null); returnThis.numDigits ++;}
						else if(count == 2){
							addedNodeContinued = new DigitNode(addedNum, null);
							returnThis.numDigits ++;
							addedNode.next = addedNodeContinued;
						}
						else{
							addedNodeContinued.next = new DigitNode(addedNum, null);
							returnThis.numDigits ++;
							addedNodeContinued = addedNodeContinued.next;
						}
					}
					
					if(linkedList1!=null) {linkedList1 = linkedList1.next;}
					if(linkedList2!=null) {linkedList2 = linkedList2.next;}
				}	
			}
			
			else if(count2>count1) {
				
				while(linkedList1!=null||linkedList2!=null){
					count++;

					if(linkedList1 == null){addedNum = linkedList2.digit;}
					else if(linkedList2 == null){addedNum = linkedList1.digit;}
					else{addedNum = linkedList2.digit - linkedList1.digit;}
					

					if(addedNum<0){
						
						if(linkedList2.next!=null) {linkedList2.next.digit = linkedList2.next.digit-1;}
						add = addedNum+10;

						if(count == 1) {addedNode = new DigitNode(add, null); returnThis.numDigits ++;}
						else if(count == 2){
							addedNode.next = new DigitNode(add, null);
							returnThis.numDigits ++;
							addedNodeContinued = addedNode.next;
						}
						else{
							addedNodeContinued.next = new DigitNode(add, null);
							returnThis.numDigits ++;
							addedNodeContinued = addedNodeContinued.next;

						}
					} else{

						if(count == 1) {addedNode = new DigitNode(addedNum, null); returnThis.numDigits ++;}
						else if(count == 2){
							addedNodeContinued = new DigitNode(addedNum, null);
							returnThis.numDigits ++;
							addedNode.next = addedNodeContinued;
						}
						else{
							addedNodeContinued.next = new DigitNode(addedNum, null);
							returnThis.numDigits ++;
							addedNodeContinued = addedNodeContinued.next;
						}
					}
					
					if(linkedList1!=null) {linkedList1 = linkedList1.next;}
					if(linkedList2!=null) {linkedList2 = linkedList2.next;}
					
				}	
			}
			
			if((count1>count2)&&second.negative==false) {returnThis.negative=true;} 
			else if((count2>count1)&&second.negative==true) {returnThis.negative=true;}
		}
		
		
		if(first.negative == true && second.negative==true) {returnThis.negative = true;}
		

		while(linkedList1!=null||linkedList2!=null||temp!=0){
			count++;

			if(linkedList1==null&&linkedList2==null) {addedNum = temp;}
			else if(linkedList1 == null){addedNum = temp + linkedList2.digit;}
			else if(linkedList2 == null){addedNum = temp + linkedList1.digit;}
			else{addedNum = temp + linkedList1.digit + linkedList2.digit;}
			

			if(linkedList1!=null) {linkedList1 = linkedList1.next;}
			if(linkedList2!=null) {linkedList2 = linkedList2.next;}

			if(addedNum>=10){
				 temp = 1;
				 add = addedNum-10;

				if(count == 1) {addedNode = new DigitNode(add, null); returnThis.numDigits ++;}
				else if(count == 2){
					addedNode.next = new DigitNode(add, null);
					returnThis.numDigits ++;
					addedNodeContinued = addedNode.next;
				}
				else{
					addedNodeContinued.next = new DigitNode(add, null);
					returnThis.numDigits ++;
					addedNodeContinued = addedNodeContinued.next;

				}

			} else{
				temp = 0;

				if(count == 1) {addedNode = new DigitNode(addedNum, null); returnThis.numDigits ++;}
				else if(count == 2){
					addedNodeContinued = new DigitNode(addedNum, null);
					returnThis.numDigits ++;
					addedNode.next = addedNodeContinued;
				}
				else{
					addedNodeContinued.next = new DigitNode(addedNum, null);
					returnThis.numDigits ++;
					addedNodeContinued = addedNodeContinued.next;
				}
			}
		}	
		
		returnThis.front = addedNode;
		System.out.println("number of digits: " + returnThis.numDigits);
		return returnThis;
	}
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	
	public static BigInteger multiply(BigInteger first, BigInteger second){
		
		boolean negative = first.negative != second.negative;
		
		BigInteger returnThis = new BigInteger();
		
		if (first.front == null || second.front==null) {
			return returnThis;
		}
		returnThis.front = new DigitNode(0, null);
		
		int trailingZeros = 0;
		int addedNum = 0;
		
		DigitNode linkedList2 = second.front;
		
		while (linkedList2 != null) {
			
			BigInteger one = new BigInteger();
			one.front = new DigitNode(0, null);
			DigitNode name = one.front;
			
			for (int traverse=0; traverse<trailingZeros; traverse++) {
				name.digit = 0;
				name.next = new DigitNode(0, null);
				name = name.next;
			}
			int temp = 0;
			
			DigitNode linkedList1 = first.front;
			
			while (linkedList1 != null) {
				
				addedNum = temp + (linkedList1.digit*linkedList2.digit);
				temp = addedNum/10;
				name.digit = addedNum%10;
				
				if(linkedList1.next != null) {
					name.next = new DigitNode(0, null);
					name = name.next;
				} else {
					if(temp != 0)
						name.next = new DigitNode(temp,null);
					break;
				}
				
				linkedList1= linkedList1.next;
			}
			
			linkedList2 = linkedList2.next;
			trailingZeros++;
			returnThis = add(returnThis, one);
		}
		
		returnThis.negative = negative;
		
		int lastIndex = 0;
		int counter = 0;
		DigitNode reference = returnThis.front;
		
		while (reference != null) {
			if (reference.digit != 0) {
				lastIndex = counter;
			}
	
			counter++;
			reference = reference.next;
		}
		
		int count = 0;
		DigitNode returnNode = returnThis.front;
		while (count != lastIndex) {
			returnNode = returnNode.next;
			count++;
		}
		
		returnNode.next = null;

		//res.front = removeTrailingZeros(res.front);
		//res.numDigits = getNumDigits(res.front);
//		traverseNodes(res.front);
		return returnThis;
		
	}
		
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
