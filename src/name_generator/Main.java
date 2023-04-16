package name_generator;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a names file name: ");
		String fileName = scanner.nextLine();
		
		Generator generator = new Generator(fileName);
		
		System.out.println();
        System.out.println("*********** Result : ************");
		System.out.println("Generated random name is : " + generator.generateRandomName());
		
		return;
	}
}
