import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static class Student {
        int id;
        String name;
        int age;
        String course;

        Student(int id, String name, int age, String course) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.course = course;
        }

        void display() {
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Course: " + course);
            System.out.println("--------------------");
        }
    }

    static ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n-### Student Management System ###- ");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {

                System.out.print("Enter your ID: ");
                int id = sc.nextInt();

                System.out.print("Enter your Name: ");
                String name = sc.next();

                System.out.print("Enter your Age: ");
                int age = sc.nextInt();

                System.out.print("Enter your Course: ");
                String course = sc.next();

                students.add(new Student(id, name, age, course));

                System.out.println("Student added successfully :) !");

            } else if (choice == 2) {

                System.out.println("\n-### Student Details ###-");

                if (students.isEmpty()) {
                    System.out.println("No students found :( .");
                } else {
                    for (Student student : students) {
                        student.display();
                    }
                }

            } else if (choice == 3) {

                System.out.print("Enter Student ID: ");
                int id = sc.nextInt();

                boolean found = false;

                for (Student student : students) {
                    if (student.id == id) {

                        System.out.print("Enter New Name: ");
                        student.name = sc.next();

                        System.out.print("Enter New Age: ");
                        student.age = sc.nextInt();

                        System.out.print("Enter New Course: ");
                        student.course = sc.next();

                        System.out.println("Student updated successfully :) !");
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Student not found :( .");
                }

            } else if (choice == 4) {

                System.out.print("Enter Student ID: ");
                int id = sc.nextInt();

                boolean found = false;

                for (Student student : students) {
                    if (student.id == id) {
                        students.remove(student);
                        System.out.println("Student deleted successfully :( !");
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Student not found :( .");
                }

            } else if (choice == 5) {

                System.out.println("Thank you :) !");
                break;

            } else {

                System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }
}