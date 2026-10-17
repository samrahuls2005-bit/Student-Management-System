import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class Main {

    static class Student {
        private final int id; // ID cannot be changed
        private String name;
        private int age;
        private String course;

        Student(int id, String name, int age, String course) {
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive.");
            }

            validateAge(age);
            validateText(name, "Name");
            validateText(course, "Course");

            this.id = id;
            this.name = name;
            this.age = age;
            this.course = course;
        }

        private void validateAge(int age) {
            if (age < 1 || age > 120) {
                throw new IllegalArgumentException(
                        "Age must be between 1 and 120."
                );
            }
        }

        private void validateText(String text, String field) {
            if (text == null || text.trim().isEmpty()
                    || text.contains("|")) {
                throw new IllegalArgumentException(
                        field + " cannot be empty or contain |."
                );
            }
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
            validateText(name, "Name");
            this.name = name.trim();
        }

        public void setAge(int age) {
            validateAge(age);
            this.age = age;
        }

        public void setCourse(String course) {
            validateText(course, "Course");
            this.course = course.trim();
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

        private final ArrayList<Student> students = new ArrayList<>();
        private final String fileName = "students.txt";

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

        private Student findStudent(int id) {
            for (Student s : students) {
                if (s.getId() == id) {
                    return s;
                }
            }
            return null;
        }

        public void addStudent(Student student) {
            if (idExists(student.getId())) {
                System.out.println("Student ID already exists!");
                return;
            }

            students.add(student);

            if (saveStudents()) {
                System.out.println("Student added successfully!");
            } else {

                students.remove(student);
                System.out.println(
                        "Student was not added because saving failed."
                );
            }
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

        public void updateStudent(
                int id, String name, int age, String course) {

            Student s = findStudent(id);

            if (s == null) {
                System.out.println("Student not found.");
                return;
            }

            String oldName = s.getName();
            int oldAge = s.getAge();
            String oldCourse = s.getCourse();

            s.setName(name);
            s.setAge(age);
            s.setCourse(course);

            if (saveStudents()) {
                System.out.println("Student updated successfully!");
            } else {

                s.setName(oldName);
                s.setAge(oldAge);
                s.setCourse(oldCourse);

                System.out.println(
                        "Update failed. Old details have been restored."
                );
            }
        }

        public void deleteStudent(int id) {
            Iterator<Student> iterator = students.iterator();

            while (iterator.hasNext()) {
                Student s = iterator.next();

                if (s.getId() == id) {

                    int index = students.indexOf(s);

                    iterator.remove();

                    if (saveStudents()) {
                        System.out.println(
                                "Student deleted successfully!"
                        );
                    } else {

                        students.add(index, s);
                        System.out.println(
                                "Delete failed. Student was restored."
                        );
                    }
                    return;
                }
            }

            System.out.println("Student not found.");
        }

        public boolean saveStudents() {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(fileName), StandardCharsets.UTF_8)) {

                for (Student s : students) {
                    writer.write(
                            s.getId() + "|" + s.getName() + "|"
                                    + s.getAge() + "|" + s.getCourse()
                    );
                    writer.newLine();
                }

                return true;

            } catch (IOException | SecurityException e) {
                System.out.println(
                        "Error saving student records: " + e.getMessage()
                );
                return false;
            }
        }

        public void loadStudents() {
            Path path = Paths.get(fileName);

            try {

                if (!Files.exists(path)) {
                    return;
                }

                try (BufferedReader reader = Files.newBufferedReader(
                        path, StandardCharsets.UTF_8)) {

                    String line;
                    int lineNumber = 0;

                    while ((line = reader.readLine()) != null) {
                        lineNumber++;

                        try {
                            String[] data = line.split("\\|", -1);

                            if (data.length != 4) {
                                throw new IllegalArgumentException(
                                        "Invalid record format."
                                );
                            }

                            int id = Integer.parseInt(data[0].trim());
                            String name = data[1].trim();
                            int age = Integer.parseInt(data[2].trim());
                            String course = data[3].trim();

                            Student student = new Student(
                                    id, name, age, course
                            );

                            if (!idExists(id)) {
                                students.add(student);
                            } else {
                                System.out.println(
                                        "Skipping duplicate ID at line "
                                                + lineNumber
                                );
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println(
                                    "Skipping invalid record at line "
                                            + lineNumber + ": "
                                            + e.getMessage()
                            );
                        }
                    }
                }

            } catch (IOException | SecurityException e) {
                System.out.println(
                        "Error loading student records: " + e.getMessage()
                );
            }
        }
    }

    static Scanner sc = new Scanner(System.in);

    // Read a positive integer
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

    static int readAge(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                int age = Integer.parseInt(input);

                if (age >= 1 && age <= 120) {
                    return age;
                }

                System.out.println("Age must be between 1 and 120.");

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

                case 1: {
                    int id = readPositiveInt("Enter Student ID: ");

                    if (manager.idExists(id)) {
                        System.out.println("Student ID already exists!");
                        break;
                    }

                    String name = readText("Enter Student Name: ");
                    int age = readAge("Enter Student Age: ");
                    String course = readText("Enter Student Course: ");

                    try {
                        Student student = new Student(
                                id, name, age, course
                        );
                        manager.addStudent(student);

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case 2:
                    manager.viewStudents();
                    break;

                case 3: {
                    int id = readPositiveInt(
                            "Enter Student ID to update: "
                    );

                    if (!manager.idExists(id)) {
                        System.out.println("Student not found.");
                        break;
                    }

                    String name = readText("Enter New Name: ");
                    int age = readAge("Enter New Age: ");
                    String course = readText("Enter New Course: ");

                    manager.updateStudent(id, name, age, course);
                    break;
                }

                case 4: {
                    int id = readPositiveInt(
                            "Enter Student ID to delete: "
                    );
                    manager.deleteStudent(id);
                    break;
                }

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
