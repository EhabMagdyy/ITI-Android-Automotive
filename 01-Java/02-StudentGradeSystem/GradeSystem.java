import java.util.Scanner;
import java.util.InputMismatchException;

public class GradeSystem {

    public static void main(String[] args){

        System.out.println(">>>>>>>>>> Student Grade System <<<<<<<<<<");

        try(Scanner scanner = new Scanner(System.in)){

            System.out.print("Enter number of students: ");
            int studentsCount = scanner.nextInt();

            String topPhysicsName = "";
            String topCSName = "";
            String topMathName = "";

            double topPhysics = -1;
            double topCS = -1;
            double topMath = -1;

            double physicsSum = 0;
            double csSum = 0;
            double mathSum = 0;

            for(int i = 0; i < studentsCount; i++){

                System.out.println("\n=============\nStudent " + (i + 1) + "\n=============");

                System.out.print("Enter name: ");
                String name = scanner.next();

                System.out.print("Enter ID: ");
                int id = scanner.nextInt();

                System.out.print("Enter Physics mark: ");
                double physics = scanner.nextDouble();

                System.out.print("Enter CS mark: ");
                double cs = scanner.nextDouble();

                System.out.print("Enter Math mark: ");
                double math = scanner.nextDouble();

                if(!isValid(physics) || !isValid(cs) || !isValid(math)){
                    System.out.println("Error: Invalid marks");
                    return;
                }

                physicsSum += physics;
                csSum += cs;
                mathSum += math;

                if(physics > topPhysics){
                    topPhysics = physics;
                    topPhysicsName = name;
                }

                if(cs > topCS){
                    topCS = cs;
                    topCSName = name;
                }

                if(math > topMath){
                    topMath = math;
                    topMathName = name;
                }

                double total = physics + cs + math;
                double average = total / 3;

                String grade = getGrade(average);

                System.out.println("------- Result -------");
                System.out.println("Name: " + name);
                System.out.println("ID: " + id);
                System.out.println("Total: " + total);
                System.out.println("Average: " + average);
                System.out.println("Grade: " + grade);
            }

            System.out.println("\n========== CLASS STATISTICS ==========");

            System.out.println("Physics Average: " + (physicsSum / studentsCount));
            System.out.println("CS Average: " + (csSum / studentsCount));
            System.out.println("Math Average: " + (mathSum / studentsCount));

            System.out.println("\nTop Physics Student: " + topPhysicsName + " (" + topPhysics + ")");
            System.out.println("Top CS Student: " + topCSName + " (" + topCS + ")");
            System.out.println("Top Math Student: " + topMathName + " (" + topMath + ")");
        }
        catch(InputMismatchException e){
            System.out.println("Error: Invalid input type");
        }
        catch(Exception e){
            System.out.println("Error: Unexpected error");
        }
    }

    private static boolean isValid(double mark){
        return mark >= 0 && mark <= 100;
    }

    private static String getGrade(double average){

        if(average >= 90) return "A";
        else if(average >= 80) return "B";
        else if(average >= 70) return "C";
        else if(average >= 60) return "D";
        else return "F";
    }
}