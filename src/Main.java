import java.io.*;
import java.util.*;

public class Main {
    private static final int maximumStudents = 100;
    private static int currentStudentCount = 0;
    public static String[][] studentDetails = new String[100][2]; // Array to store student ID and student name
    public static Student[] studentClassObjectArray = new Student[100]; // Array to store Student objects
    public static Module[] moduleClassObjectArray = new Module[100]; // Array to store Module objects
    static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {

        while (true) {
            // Display the main menu
            System.out.println("\n__STUDENT MANAGEMENT SYSTEM MENU");
            System.out.println("\n1. Check available seats");
            System.out.println("2. Register student (with ID)");
            System.out.println("3. Delete student");
            System.out.println("4. Find student (with student ID)");
            System.out.println("5. Store student details into a file");
            System.out.println("6. Load student details from the file to the system");
            System.out.println("7. View the list of students based on their names");
            System.out.println("8. Additional options");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            int option;
            try{
                option = input.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a number");
                input.next();
                continue;
            }

            // Switch case to handle user selection
            switch (option) {
                case 1:
                    checkAvailableSeats();
                    break;
                case 2:
                    registerStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    findStudent();
                    break;
                case 5:
                    storeStudentDetails();
                    break;
                case 6:
                    loadStudentDetails();
                    break;
                case 7:
                    listOfStudents();
                    break;
                case 8:
                    System.out.println("\na. Add student name");
                    System.out.println("b. Enter Module marks");
                    System.out.println("c. Summary");
                    System.out.println("d. Complete Report");
                    System.out.print("Select an sub option: ");
                    String option2 = input.next();
                    if (option2.equals("a")) {
                        addStudentName();
                    } else if (option2.equals("b")) {
                        enterModuleMarks();
                    } else if (option2.equals("c")) {
                        summary();
                    } else if (option2.equals("d")) {
                        fullReport();
                    } else {
                        System.out.println("Invalid option");
                    }
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again");
            }
        }
    }

    // Method to check available seats
    public static void checkAvailableSeats() {
        System.out.println("\nAvailable seats: " + (maximumStudents - currentStudentCount));

    }
    // Method to register a new student
    public static void registerStudent() {
        if (currentStudentCount >= maximumStudents) {
            System.out.print("No available seats to register new students");
            return;
        }
        System.out.println("\nStudent Registration");
        System.out.print("\nEnter student ID: ");
        String studentId = input.next();
        System.out.print("Enter student name: ");
        String studentName = input.next();

        studentDetails[currentStudentCount][0] = studentId;
        studentDetails[currentStudentCount][1] = studentName;
        studentClassObjectArray[currentStudentCount] = new Student(studentId, studentName);
        moduleClassObjectArray[currentStudentCount] = new Module();
        currentStudentCount++;
        System.out.println("Student registered successfully ");

    }
    // Method to delete a student
    public static void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        String studentId = input.next();

        int index = findStudentIndexById(studentId);

        if (index == -1) {
            System.out.println("Student not found");
            return;
        }
        // Shift elements to fill the deleted student's position
        for (int i = index; i < currentStudentCount-1; i++) {
            studentDetails[i][0] = studentDetails[i+1][0];
            studentDetails[i][1] = studentDetails[i+1][1];
            studentClassObjectArray[i] = studentClassObjectArray[i + 1];
            moduleClassObjectArray[i] = moduleClassObjectArray[i + 1];
        }
        studentDetails[currentStudentCount-1][0] = "null";
        studentDetails[currentStudentCount-1][1] = "null";
        studentClassObjectArray[currentStudentCount - 1] = null;
        moduleClassObjectArray[currentStudentCount - 1] = null;

        currentStudentCount--;

        System.out.println("Student deleted successfully");

    }
    // Method to find a student by ID
    public static void findStudent() {
        System.out.print("Enter student ID to find: ");
        String studentId = input.next();
        int index = findStudentIndexById(studentId);

        if (index == -1){
            System.out.println("Student not found");
            return;
        }
        System.out.println("\nStudent name is: " + studentDetails[index][1]);
        System.out.println("Student Id: " + studentDetails[index][0]);

    }
    // Method to find a student's index by their ID
    public static int findStudentIndexById(String studentID) {
        int index = -1;
        for (int i = 0; i < currentStudentCount; i++) {
            if (studentDetails[i][0].equals(studentID)){
                index = i;
            }
        }
        return index;
    }
    // Method to store student details into a file
    public static void storeStudentDetails() {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("student_details.txt"));

            for (int i = 0; i < currentStudentCount; i++){
                writer.write(studentDetails[i][0] + "," + studentDetails[i][1] + "," + moduleClassObjectArray[i].mark1 + "," + moduleClassObjectArray[i].mark2 + "," + moduleClassObjectArray[i].mark3);
                writer.newLine();
            }
            writer.close();
            System.out.println("\nStudent details stored successfully");
        } catch (IOException e) {
            System.out.println("Error storing student details " + e.getMessage());
        }

    }
    // Method to load student details from a file
    public static void loadStudentDetails() {
        try{
            BufferedReader reader = new BufferedReader(new FileReader("student_details.txt"));
            String line;
            currentStudentCount = 0;

            while ((line = reader.readLine()) != null) {
                String[] forEachLine = line.split(",");
                studentDetails[currentStudentCount][0] = forEachLine[0];
                studentDetails[currentStudentCount][1] = forEachLine[1];
                moduleClassObjectArray[currentStudentCount] = new Module(Float.parseFloat(forEachLine[2]), Float.parseFloat(forEachLine[3]), Float.parseFloat(forEachLine[4]));
                studentClassObjectArray[currentStudentCount] = new Student(forEachLine[0], forEachLine[1]);
                currentStudentCount++;
            }
            System.out.println("Student details loaded successfully");

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error loading student details: " + e.getMessage());
        }

    }

    // Method to list students sorted by their names
    public static void listOfStudents() {
        // Bubble sort to sort students by their names
        for (int i = 0; i < currentStudentCount - 1; i++) {
            for (int j = 0; j < currentStudentCount - i - 1; j++){
                if (studentDetails[j][1].compareTo(studentDetails[j+1][1]) > 0) {

                    // Swap student details
                    String id = studentDetails[j][0];
                    studentDetails[j][0] = studentDetails[j+1][0];
                    studentDetails[j+1][0] = id;

                    String name = studentDetails[j][1];
                    studentDetails[j][1] = studentDetails[j+1][1];
                    studentDetails[j+1][1] = name;

                    // Swap Student objects
                    Student Student_A_Sort = studentClassObjectArray[j];
                    studentClassObjectArray[j] = studentClassObjectArray[j + 1];
                    studentClassObjectArray[j + 1] = Student_A_Sort;

                    // Swap Module objects
                    Module Module_A_Sort = moduleClassObjectArray[j];
                    moduleClassObjectArray[j] = moduleClassObjectArray[j + 1];
                    moduleClassObjectArray[j + 1] = Module_A_Sort;

                }
            }
        }
        System.out.println("Students sorted by name: ");
        for (int i = 0; i < currentStudentCount; i++){
            System.out.println((i+1) + ")" + studentDetails[i][1]);
        }
    }

    // Method to add a student name
    public static void addStudentName() {
        System.out.print("Enter student ID: ");
        String studentId = input.next();
        System.out.print("Enter student name: ");
        String studentName = input.next();

        int index = findStudentIndexById(studentId);
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }

        studentDetails[index][1] = studentName;
        studentClassObjectArray[index].set_name(studentName);
        System.out.println("Student name added successfully");
    }

    // Method to enter module marks for a student
    public static void enterModuleMarks() {
        System.out.print("\nEnter student ID: ");
        String studentId = input.next();
        int index = findStudentIndexById(studentId);

        if (index == -1) {
            System.out.println("Student not found. ");
            return;
        }

        System.out.print("Enter Module 1 Mark : ");
        float mark1 = input.nextFloat();
        System.out.print("Enter Module 2 Mark : ");
        float mark2 = input.nextFloat();
        System.out.print("Enter Module 3 Mark : ");
        float mark3 = input.nextFloat();

        moduleClassObjectArray[index].setMark(mark1, mark2, mark3);
        System.out.println("Module marks entered successfully");
    }

    // Method to display a summary of students' performance
    public static void summary() {
        int totalRegisteredStudents = currentStudentCount;
        int students_Passed_Module1 = 0;
        int students_Passed_Module2 = 0;
        int students_Passed_Module3 = 0;

        for (int i = 0; i < currentStudentCount; i++) {
            if (moduleClassObjectArray[i].mark1 >= 40) {
                students_Passed_Module1++;
            }
            if (moduleClassObjectArray[i].mark2 >= 40) {
                students_Passed_Module2++;
            }
            if (moduleClassObjectArray[i].mark3 >= 40) {
                students_Passed_Module3++;
            }
        }

        System.out.println("Total student registrations: " + totalRegisteredStudents);
        System.out.println("Number of students who passed Module 1: " + students_Passed_Module1);
        System.out.println("Number of students who passed Module 2: " + students_Passed_Module2);
        System.out.println("Number of students who passed Module 3: " + students_Passed_Module3);
    }

    // Method to display a complete report of all students
    public static void fullReport() {
        Student[] students = Arrays.copyOf(studentClassObjectArray, currentStudentCount);
        Module[] modules = Arrays.copyOf(moduleClassObjectArray, currentStudentCount);


        // Bubble sort to sort students by their average marks
        for (int i = 0; i < currentStudentCount - 1; i++) {
            for (int j = 0; j < currentStudentCount - i - 1; j++) {
                if (modules[j].read_Average() < modules[j + 1].read_Average()) {

                    // Swap Student objects
                    Student Student_A = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = Student_A;

                    // Swap Module objects
                    Module Module_A = modules[j];
                    modules[j] = modules[j + 1];
                    modules[j + 1] = Module_A;
                }
            }
        }

        System.out.println("Complete Report:");
        for (int i = 0; i < currentStudentCount; i++) {
            System.out.println("ID: " + students[i].read_ID() + ", Name: " + students[i].read_name() +
                    ", Module 1: " + modules[i].mark1 +
                    ", Module 2: " + modules[i].mark2 +
                    ", Module 3: " + modules[i].mark3 +
                    ", Total: " + modules[i].read_Total() +
                    ", Average: " + modules[i].read_Average() +
                    ", Grade: " + modules[i].read_Grade());
        }
    }
}
// Module class to store module marks and calculate total, average, and grade
class Module {
    float mark1;
    float mark2;
    float mark3;
    float total;
    float average;
    String grade;

    // Default constructor
    public Module () {

    }

    // Parameterized constructor
    public Module (float mark1, float mark2, float mark3) {
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
        cal_Total_Average();
        cal_Grade();
    }

    // Method to set marks and calculate total, average, and grade
    public void setMark(float mark1, float mark2, float mark3) {
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
        cal_Total_Average();
        cal_Grade();
    }

    // Method to calculate total and average marks
    private void cal_Total_Average() {
        this.total = mark1 + mark2 + mark3;
        this.average =total/3;
    }

    // Method to calculate grade based on average marks
    private void cal_Grade() {
        if (average >= 80) {
            grade = "Distinction";
        } else if (average >= 70) {
            grade = "Merit";
        } else if (average >= 40) {
            grade = "Pass";
        } else {
            grade = "Fail";
        }
    }

    // Getter method for total marks
    public  float read_Total() {
        return total;
    }

    // Getter method for average marks
    public float read_Average() {
        return average;
    }

    // Getter method for grade
    public String read_Grade() {
        return grade;
    }
}
// Student class to store student details
class Student {
    String name;
    String id;

    // Constructor
    public Student(String id ,String name) {
        this.id = id;
        this.name = name;
    }

    // Getter method for student ID
    public String read_ID() {
        return id;
    }

    // Getter method for student name
    public String read_name() {
        return name;
    }

    // Setter method for student name
    public void set_name(String name) {
        this.name =name;
    }
}