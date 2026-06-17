import java.util.Scanner;
import java.util.InputMismatchException;

public class Calculator {
    public static void main(String[] args){
        System.out.println(">>>>>>>>>> Simple Calculator <<<<<<<<<<");

        // instead of closing it manully
        try(Scanner scanner = new Scanner(System.in)){
            System.out.print("Enter operation (space separated): ");

            double firstNumber = scanner.nextDouble();
            char op = scanner.next().charAt(0);
            double secondNumber = scanner.nextDouble();

            double result = 0;

            switch(op){
                case '+':  result = add(firstNumber, secondNumber);      break;
                case '-':  result = subtract(firstNumber, secondNumber); break;
                case '*':  result = multiply(firstNumber, secondNumber); break;
                case '/':
                    if(secondNumber == 0){
                        System.out.println("Error: Division by zero");
                        return;
                    }

                    result = divide(firstNumber, secondNumber);
                    break;
                case '%':  result = modulus(firstNumber, secondNumber); break;
                default:
                    System.out.println("Error: Invalid operation you dump!!");
                    return;
            }

            System.out.println("Result: " + firstNumber + " " + op + " " + secondNumber + " = " + result);
        }
        catch(InputMismatchException e){
            System.out.println("Error: Invalid input type!!");
        }
        catch(Exception e){
            System.out.println("Error: Invalid operation you dump!!");
        }
    }

    private static double add(double firstNumber, double secondNumber){
        return firstNumber + secondNumber;
    }

    private static double subtract(double firstNumber, double secondNumber){
        return firstNumber - secondNumber;
    }

    private static double multiply(double firstNumber, double secondNumber){
        return firstNumber * secondNumber;
    }

    private static double divide(double firstNumber, double secondNumber){
        return firstNumber / secondNumber;
    }

    private static double modulus(double firstNumber, double secondNumber){
        return firstNumber % secondNumber;
    }
}
