import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class Main {

    static class Student {
        private int id;
        private String name;
        private int age;
        private String course;

        Student(int id, String name, int age, String course) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.course = course;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getCourse() {
            return course;
        }


        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public void display() {
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Course: " + course);
            System.out.println("--------------------");
        }
    }

    static class StudentManager {

        ArrayList<Student> students = new ArrayList<>();
        String fileName = "students.txt";

        StudentManager() {
            loadStudents();
        }

        public boolean idExists(int id) {
            for (Student s : students) {
                if (s.getId() == id) {
                    return true;
                }
            }
            return false;
        }

        public void addStudent(Student student) {
            if (idExists(student.getId())) {
                System.out.println("Student ID already exists!");
                return;
            }

            students.add(student);
            saveStudents();

            System.out.println("Student added successfully!");
        }

        public void viewStudents() {
            if (students.isEmpty()) {
                System.out.println("No students found.");
                return;
            }

            System.out.println("\n--- Student Details ---");

            for (Student s : students) {
                s.display();
            }
        }

        public void updateStudent(int id) {
            for (Student s : students) {
                if (s.getId() == id) {

                    s.setName(readText("Enter New Name: "));
                    s.setAge(readPositiveInt("Enter New Age: "));
                    s.setCourse(readText("Enter New Course: "));

                    saveStudents();
                    System.out.println("Student updated successfully!");
                    return;
                }
            }

            System.out.println("Student not found.");
        }

        public void deleteStudent(int id) {
            Iterator<Student> iterator = students.iterator();

            while (iterator.hasNext()) {
                Student s = iterator.next();

                if (s.getId() == id) {
                    iterator.remove();
                    saveStudents();

                    System.out.println("Student deleted successfully!");
                    return;
                }
            }

            System.out.println("Student not found.");
        }

        public void saveStudents() {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(fileName), StandardCharsets.UTF_8)) {

                for (Student s : students) {
                    writer.write(s.getId() + "|" + s.getName() + "|"
                            + s.getAge() + "|" + s.getCourse());
                    writer.newLine();
                }

            } catch (IOException e) {
                System.out.println("Error saving student records.");
            }
        }

        public void loadStudents() {
            Path path = Paths.get(fileName);

            if (!Files.exists(path)) {
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(
                    path, StandardCharsets.UTF_8)) {

                String line;

                while ((line = reader.readLine()) != null) {
                    try {
                        String[] data = line.split("\\|", -1);

                        if (data.length != 4) {
                            continue;
                        }

                        int id = Integer.parseInt(data[0]);
                        String name = data[1].trim();
                        int age = Integer.parseInt(data[2]);
                        String course = data[3].trim();

                        if (id > 0 && age > 0
                                && !name.isEmpty()
                                && !course.isEmpty()
                                && !name.contains("|")
                                && !course.contains("|")
                                && !idExists(id)) {

                            students.add(
                                    new Student(id, name, age, course)
                            );
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid record.");
                    }
                }

            } catch (IOException e) {
                System.out.println("Error loading student records.");
            }
        }
    }

    static Scanner sc = new Scanner(System.in);

    static int readPositiveInt(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                int number = Integer.parseInt(input);

                if (number > 0) {
                    return number;
                }

                System.out.println("Enter a number greater than zero.");

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    static String readText(String message) {
        while (true) {
            System.out.print(message);
            String text = sc.nextLine().trim();

            if (!text.isEmpty() && !text.contains("|")) {
                return text;
            }

            System.out.println("Input cannot be empty or contain |.");
        }
    }

    public static void main(String[] args) {

        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            String input = sc.nextLine().trim();

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Enter 1 to 5.");
                continue;
            }

            switch (choice) {

                case 1:
                    int id = readPositiveInt("Enter Student ID: ");

                    if (manager.idExists(id)) {
                        System.out.println("Student ID already exists!");
                        break;
                    }

                    String name = readText("Enter Student Name: ");
                    int age = readPositiveInt("Enter Student Age: ");
                    String course = readText("Enter Student Course: ");

                    Student student = new Student(id, name, age, course);
                    manager.addStudent(student);
                    break;

                case 2:
                    manager.viewStudents();
                    break;

                case 3:
                    id = readPositiveInt("Enter Student ID to update: ");
                    manager.updateStudent(id);
                    break;

                case 4:
                    id = readPositiveInt("Enter Student ID to delete: ");
                    manager.deleteStudent(id);
                    break;

                case 5:
                    System.out.println("Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Enter 1 to 5.");
            }
        }
    }
}
