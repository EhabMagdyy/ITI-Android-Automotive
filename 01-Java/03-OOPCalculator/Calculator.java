import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args){
        Calculator calc = new Calculator();
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter first number: ");
        double a = scanner.nextDouble();
        
        System.out.print("Enter operator(+, -, *, /): ");
        String op = scanner.next();
        
        System.out.print("Enter second number: ");
        double b = scanner.nextDouble();
        
        try {
            double result = calc.compute(a, b, op);
            System.out.println("Result: " + result);
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        
        scanner.close();
    }

    public double compute(double a, double b, String operator){
        if(!OperationFactory.isValidOperator(operator)){
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        Operation operation = OperationFactory.getOperation(operator);
        return operation.calculate(a, b);
    }
}

interface Operation {
    double calculate(double a, double b);
}

class AddOperation implements Operation {
    @Override
    public double calculate(double a, double b){
        return a + b;
    }
}

class SubOperation implements Operation {
    @Override
    public double calculate(double a, double b){
        return a - b;
    }
}

class MultiplyOperation implements Operation {
    @Override
    public double calculate(double a, double b){
        return a * b;
    }
}

class DivideOperation implements Operation {
    @Override
    public double calculate(double a, double b){
        if(b == 0)
            throw new ArithmeticException("Divide by zero");
        return a / b;
    }
}

class OperationFactory {
    private static final Map<String, Operation> operationMap = new HashMap<>();
    
    // static initializer block
    static {
        operationMap.put("+", new AddOperation());
        operationMap.put("-", new SubOperation());
        operationMap.put("*", new MultiplyOperation());
        operationMap.put("/", new DivideOperation());
    }
    
    public static Operation getOperation(String operator){
        Operation op = operationMap.get(operator);
        if(op == null){
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }
        return op;
    }
    
    public static boolean isValidOperator(String operator){
        return operationMap.containsKey(operator);
    }
}