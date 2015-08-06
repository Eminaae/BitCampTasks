import java.math.BigInteger;

public class RSA {

	public static void main(String[] args) {

		BigInteger num = new BigInteger("400050471579413");
		
		for (int i = 2; num.divide(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(i)) > 0; i++) {
			
			if (num.mod(BigInteger.valueOf(i)) == BigInteger.valueOf(0)) {
				System.out.println(i + "\n" + num.divide(BigInteger.valueOf(i)));
				
				break;
			}
		}
	}
}