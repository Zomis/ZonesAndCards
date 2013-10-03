package net.zomis.fizzbuzz;

public class MainFizzBuzz {

	public static void main(String[] args) {
		FizzBuzz fb = new FizzBuzz();
		fb.addFizz(new DividableFizz(3, "Fizz"));
		fb.addFizz(new DividableFizz(5, "Buzz"));
		fb.addFizz(new ExactFizz("Tejpbit", 42));
		fb.perform(1, 100);
	}
	
}
