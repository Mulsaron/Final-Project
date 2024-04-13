package com.example;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/Project";
        String user = "postgres";
        String password = "xxx";

    try{
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(url, user, password);

        if (connection != null){
            System.out.println("Connected to Database\n");
            //Main menu--user starts here and continues based on whether they are a member,trainer or administrator
            Scanner scanner = new Scanner(System.in);
                int choice;
                do {
                    System.out.println("User Type:");
                    System.out.println("1. Member");
                    System.out.println("2. Trainer");
                    System.out.println("3. Administrator");

                    System.out.println("0. Exit");

                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    switch (choice) {
                        case 1:
                        memberFunctionality(connection, scanner);
                        
                        break;
                        case 2:
                        trainerFunctionality(connection, scanner);
                        break;
                        case 3:
                        adminFunctionality(connection, scanner);
                        break;
                        case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 0);

            scanner.close();
            connection.close();
        } else {
            System.out.println("Failed to connect to the database");
        }
 } catch(Exception e){
    System.out.println(e);
}     
}
//Menu for member
public static void memberFunctionality(Connection connection, Scanner scanner) throws SQLException {
    int choice;
    do {
        System.out.println("Member Menu:");
        System.out.println("1. Register as a member");
        System.out.println("2. Update profile");
        System.out.println("3. View dashboard");
        System.out.println("4. View schedule");
        System.out.println("5. Book a class");
        System.out.println("6. Cancel a class");
        System.out.println("7. Reschedule class");
        System.out.println("0. Back to User Type selection");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                registerMember(connection, scanner);
                break;

            case 2:
                updateMember(connection, scanner);
                break;

            case 3:
                displayDashboard(connection, scanner);
                break;

            case 4:
                viewAndSelectSchedule(connection, scanner);
                break;
            case 5: 
                bookClass(connection, scanner);
                break;
            case 6:
                cancelClass(connection, scanner);
                break;
            case 7:
                rescheduleClass(connection, scanner);
                break;

            case 0:
                System.out.println("Returning to User Type selection...");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    } while (choice != 0);
}

public static void registerMember(Connection connection, Scanner scanner) {
    try {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Insert member function based on input from user
        int memberId = insertMember(connection, firstName, lastName, email, password);

        if (memberId != -1) {
            // Insert fitness goals
            insertFitnessGoals(connection, memberId, scanner);

            // Insert health metrics
            insertHealthMetrics(connection, memberId, scanner);

            // Insert Exercise Routine
            insertExerciseRoutines(connection, memberId, scanner);

            //InsertFitness Achievement
            insertFitnessAchievements(connection, memberId, scanner);

            System.out.println("Member registered successfully.");
        } else {
            System.out.println("Failed to register member.");
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}

public static int insertMember(Connection connection, String firstName, String lastName, String email, String password) throws SQLException {
    String query = "INSERT INTO Member (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setString(4, password);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); 
            }
        }
    }
    return -1; 
}

public static void insertFitnessGoals(Connection connection, int memberId, Scanner scanner) throws SQLException {
    System.out.print("Please enter your fitness goals: \n");
    System.out.print("Enter fitness goal type: ");
    String goalType = scanner.nextLine();
    System.out.print("Enter number value for the goal: ");
    double numberValue = scanner.nextDouble();
    scanner.nextLine(); 
    System.out.print("Enter target date (yyyy-mm-dd): ");
    String targetDate = scanner.nextLine();

    String query = "INSERT INTO Fitness_Goals (member_id, goal_type, number_value, target_date) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        statement.setString(2, goalType);
        statement.setDouble(3, numberValue);
        statement.setDate(4, java.sql.Date.valueOf(targetDate));

        statement.executeUpdate();
    }
}

public static void insertHealthMetrics(Connection connection, int memberId, Scanner scanner) throws SQLException {
    System.out.print("Please enter your health metrics: \n");
    System.out.print("Enter weight (lbs): ");
    double weight = scanner.nextDouble();
    scanner.nextLine(); 
    System.out.print("Enter height (cm): ");
    double height = scanner.nextDouble();
    scanner.nextLine(); 
    System.out.print("Enter last checkup date (yyyy-mm-dd): ");
    String lastCheckupDate = scanner.nextLine();
    System.out.print("Enter blood pressure: ");
    String bloodPressure = scanner.nextLine();
    System.out.print("Enter cholesterol level (mg/dL): ");
    double cholesterolLevel = scanner.nextDouble();
    scanner.nextLine(); 

    String query = "INSERT INTO Health_Metrics (member_id, weight, height, last_checkup_date, blood_pressure, cholesterol_level) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        statement.setDouble(2, weight);
        statement.setDouble(3, height);
        statement.setDate(4, java.sql.Date.valueOf(lastCheckupDate));
        statement.setString(5, bloodPressure);
        statement.setDouble(6, cholesterolLevel);

        statement.executeUpdate();
    }
}
public static void insertExerciseRoutines(Connection connection, int memberId, Scanner scanner) throws SQLException {
    System.out.print("Please enter your exercise routine: \n");
    System.out.print("Enter Description of exercise routine: ");
    String routineDescription = scanner.nextLine();
    System.out.print("Enter amount of times you would like to do this exercise: ");
    int duration = scanner.nextInt();
    scanner.nextLine();
    String query = "INSERT INTO Exercise_Routines (member_id, exercise_description, duration) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        statement.setString(2, routineDescription);
        statement.setInt(3,duration);


        statement.executeUpdate();
    }
}

public static void insertFitnessAchievements(Connection connection, int memberId, Scanner scanner) throws SQLException {
    System.out.print("Please enter your fitness achievements: \n");
    System.out.print("Enter Description of your fitness achievement: ");
    String achievementDescription = scanner.nextLine();
    System.out.print("Enter number value for the achievement: ");
    double numberValue = scanner.nextDouble();
    scanner.nextLine(); 
    System.out.print("Enter achievement date (yyyy-mm-dd): ");
    String achievementDate = scanner.nextLine();
    scanner.nextLine();
    String query = "INSERT INTO Fitness_Achievements (member_id, achievement_type, value, achieved_date) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        statement.setString(2, achievementDescription);
        statement.setDouble(3,numberValue);
        statement.setDate(4, java.sql.Date.valueOf(achievementDate));


        statement.executeUpdate();
    }
}



public static void updateMember(Connection connection, Scanner scanner) {
    int choice;
    //Allows members to choose what part of their profile they would like to update
    do {
        System.out.println("1. Update personal information");
        System.out.println("2. Update health Metrics");
        System.out.println("3. Update fitness goals");
        System.out.println("0. Exit");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1:
                updatePersonalInformation(connection, scanner);
                break;
            case 2:
                updateHealthMetrics(connection, scanner);
                break;
            case 3:
                updateFitnessGoals(connection, scanner);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
} while (choice != 0);

}
public static void updatePersonalInformation(Connection connection, Scanner scanner) {
    try {
        System.out.print("Enter member ID to update: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new first name: ");
        String newFirstName = scanner.nextLine();
        System.out.print("Enter new last name: ");
        String newLastName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        String query = "UPDATE Member SET first_name = ?, last_name = ?, email = ?, password = ? WHERE member_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newFirstName);
            statement.setString(2, newLastName);
            statement.setString(3, newEmail);
            statement.setString(4, newPassword);
            statement.setInt(5, memberId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Personal information updated successfully.");
            } else {
                System.out.println("Failed to update personal information. Member ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}


public static void updateHealthMetrics(Connection connection, Scanner scanner) {
    try {
        System.out.print("Enter member ID to update: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new weight (lbs): ");
        double newWeight = scanner.nextDouble();
        scanner.nextLine(); 
        System.out.print("Enter new height (cm): ");
        double newHeight = scanner.nextDouble();
        scanner.nextLine(); 
        System.out.print("Enter new last checkup date (yyyy-mm-dd): ");
        String newLastCheckupDate = scanner.nextLine();
        System.out.print("Enter new blood pressure: ");
        String newBloodPressure = scanner.nextLine();
        System.out.print("Enter new cholesterol level (mg/dL): ");
        double newCholesterolLevel = scanner.nextDouble();

        String query = "UPDATE Health_Metrics SET weight = ?, height = ?, last_checkup_date = ?, blood_pressure = ?, cholesterol_level = ? WHERE member_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, newWeight);
            statement.setDouble(2, newHeight);
            statement.setDate(3, java.sql.Date.valueOf(newLastCheckupDate));
            statement.setString(4, newBloodPressure);
            statement.setDouble(5, newCholesterolLevel);
            statement.setInt(6, memberId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Health metrics updated successfully.");
            } else {
                System.out.println("Failed to update health metrics. Member ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}

public static void updateFitnessGoals(Connection connection, Scanner scanner) {
    try {
        System.out.print("Enter member ID to update: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter new fitness goal type: ");
        String newGoalType = scanner.nextLine();
        System.out.print("Enter new number value for the goal: ");
        double newNumberValue = scanner.nextDouble();
        scanner.nextLine(); 
        System.out.print("Enter new target date (yyyy-mm-dd): ");
        String newTargetDate = scanner.nextLine();

        String query = "UPDATE Fitness_Goals SET goal_type = ?, number_value = ?, target_date = ? WHERE member_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newGoalType);
            statement.setDouble(2, newNumberValue);
            statement.setDate(3, java.sql.Date.valueOf(newTargetDate));
            statement.setInt(4, memberId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Fitness goals updated successfully.");
            } else {
                System.out.println("Failed to update fitness goals. Member ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}

public static void displayDashboard (Connection connection, Scanner scanner) {
    System.out.print("Enter member ID: ");
    int memberId = scanner.nextInt();
    scanner.nextLine();
    try {
    System.out.println("Fitness Achievements: ");
    fitnessAchievements(connection, memberId);
    System.out.println("\nExercise Routines:");
    exerciseRoutines(connection, memberId);
    System.out.println("\nHealth Statistics:");
    healthStatistics(connection, memberId);
} catch (Exception e) {
    System.out.println(e);
}
}

public static void exerciseRoutines (Connection connection, int memberId) throws SQLException {
    String query = "SELECT * FROM Exercise_Routines WHERE member_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        try (ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-15s %-25s %-15s%n",  "Routine ID", "Exercise Description", "Duration");
            while (resultSet.next()) {
                int routineId = resultSet.getInt("routine_id");
                String exerciseDescription = resultSet.getString("exercise_description");
                int duration = resultSet.getInt("duration");
        System.out.printf("%-15s %-25s %-15s%n",routineId,exerciseDescription,duration);
    }
}
}
}

public static void fitnessAchievements(Connection connection, int memberId) throws SQLException {
    String query = "SELECT * FROM Fitness_Achievements WHERE member_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        try (ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-25s %-25s %-15s %-15s%n",  "Achievement ID", "Achievement Type", "Value", "Achieved Date");
            while (resultSet.next()) {
                int achievementId = resultSet.getInt("achievement_id");
                String achievementType = resultSet.getString("achievement_type");
                double value = resultSet.getDouble("value");
                String achievedDate = resultSet.getString("achieved_date");
                System.out.printf("%-25s %-25s %-15s %-15s%n",achievementId , achievementType, value, achievedDate);
            }
        }
    }
}

public static void healthStatistics(Connection connection, int memberId) throws SQLException {
    String query = "SELECT * FROM Health_Metrics WHERE member_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, memberId);
        try (ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-15s %-15s %-25s %-25s %-25s%n",  "Weight", "Height", "Last Checkup Date", "Blood Pressure", "Chloesterol Level");
        while (resultSet.next()) {
                double weight = resultSet.getDouble("weight");
                double height = resultSet.getDouble("height");
                String lastCheckupDate = resultSet.getString("last_checkup_date");
                String bloodPressure = resultSet.getString("blood_pressure");
                double cholesterolLevel = resultSet.getDouble("cholesterol_level");
                System.out.printf("%-15s %-15s %-25s %-25s %-25s%n",  weight, height, lastCheckupDate, bloodPressure, cholesterolLevel);
            }
    }
}
}



public static void trainerFunctionality(Connection connection, Scanner scanner) throws SQLException  {
    int choice;
    do {
        //Menu for trainers
        System.out.println("Trainer Menu:");
        System.out.println("1. Login as a trainer");
        System.out.println("2. Set Availabilty");
        System.out.println("3. Member Profile Viewing");
        System.out.println("0. Back to User Type selection");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
            trainerLogin(connection, scanner);
            break;
            case 2:
            setTrainerAvailability(connection, scanner);
            break;
            case 3:
            memberSearch(connection, scanner);
            break;
            case 0:
            System.out.println("Returning to User Type selection...");
            break;

        default:
            System.out.println("Invalid choice. Please try again.");
    }
} while (choice != 0);
}
static boolean isTrainerLoggedIn = false;
public static void trainerLogin (Connection connection, Scanner scanner)throws SQLException{
    System.out.println("Trainer Login");
    System.out.print("Enter Trainer ID: ");
    int trainerId = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Enter Password: ");
    String passwordInput = scanner.nextLine();

    //Make sure that login info is correct to be able to continue
    String query = "SELECT * FROM Trainer WHERE trainer_id = ? AND password = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, trainerId);
        statement.setString(2, passwordInput);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Login successful!");
                isTrainerLoggedIn = true;
            } else {
                System.out.println("Invalid trainer ID or password. Please try again.");
                isTrainerLoggedIn = false;
            }
        }
    }
    catch (Exception e) {
        System.out.println(e);
    }
}
public static void setTrainerAvailability(Connection connection, Scanner scanner) throws SQLException {
//Before being able to set Availabilty, trainer needs to be logged in
    if (!isTrainerLoggedIn) {
        System.out.println("Please log in as a trainer first.");
        return;
    }

        System.out.print("Enter Trainer ID: ");
        int trainerId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter date you're available (yyyy-mm-dd): ");
        String availableDateStr = scanner.nextLine();
        java.sql.Date availableDate = java.sql.Date.valueOf(availableDateStr);
        System.out.print("Enter start time (HH:mm:ss): ");
        String startTime = scanner.nextLine();
        System.out.print("Enter end time (HH:mm:ss): ");
        String endTime = scanner.nextLine();
        System.out.print("Enter session type (group or private): ");
        String sessionType = scanner.nextLine();
        System.out.print("Enter preferred room id: ");
        String preferredRoomId = scanner.nextLine();

String query = "INSERT INTO Trainer_Availability (trainer_id, start_time, end_time, availability_date, session_type, preferred_room_id) VALUES (?, ?, ?, ?, ?,?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, trainerId);
        statement.setTime(2, java.sql.Time.valueOf(startTime));
        statement.setTime(3, java.sql.Time.valueOf(endTime));
        statement.setDate(4, availableDate);
        statement.setString(5, sessionType);
        statement.setString(6, preferredRoomId);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Availability set successfully.");
        } else {
            System.out.println("Failed to set availability.");
        }
    }
}

public static void memberSearch (Connection connection, Scanner scanner) throws SQLException {

    if (!isTrainerLoggedIn) {
        System.out.println("Please log in as a trainer first.");
        return;
    }
    //Search for members based on first name and last name
    System.out.print("Enter member's first name: ");
    String firstName = scanner.nextLine();
    
    System.out.print("Enter member's last name: ");
    String lastName = scanner.nextLine();

    String query = "SELECT * FROM Member WHERE first_name = ? AND last_name = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, firstName);
        statement.setString(2, lastName);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No matching member found.");
            } else {
                System.out.println("Matching member profiles:");
                while (resultSet.next()) {
                    System.out.printf("%-15s %-15s %-15s %-15s%n",  "Member ID", "First Name", "Last Name", "Email");
                    int memberId = resultSet.getInt("member_id");
                    String email = resultSet.getString("email");
                    System.out.printf("%-15s %-15s %-15s %-15s%n",  memberId, firstName, lastName, email);
                }
            }
        }
    }




}



public static void createSchedule (Connection connection) throws SQLException {
    String query = "SELECT availability_id, trainer_id, session_type, availability_date, start_time, end_time, preferred_room_id FROM Trainer_Availability";
    
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

        String insertQuery = "INSERT INTO Schedule (trainer_id, session_type, date, start_time, end_time, room_id) " + 
                             "VALUES (?, ?, ?, ?, ?, ?)";

        while (resultSet.next()) {
            int trainerId = resultSet.getInt("trainer_id");
            String sessionType = resultSet.getString("session_type");
            java.sql.Date availabilityDate = resultSet.getDate("availability_date");
            java.sql.Time startTime = resultSet.getTime("start_time");
            java.sql.Time endTime = resultSet.getTime("end_time");
            Integer preferredRoomId = null;
            
            
            if (resultSet.getObject("preferred_room_id") != null) {
                preferredRoomId = resultSet.getInt("preferred_room_id");
            }try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, trainerId);
                insertStatement.setString(2, sessionType);
                insertStatement.setDate(3, availabilityDate);
                insertStatement.setTime(4, startTime);
                insertStatement.setTime(5, endTime);
                
               
                if (preferredRoomId != null) {
                    insertStatement.setInt(6, preferredRoomId);
                } else {
                    insertStatement.setNull(6, java.sql.Types.INTEGER);
                }
                
                insertStatement.executeUpdate();
            }
        }

    }
}
public static void viewAndSelectSchedule(Connection connection, Scanner scanner) throws SQLException {
    //select everything from schedule, so that members can see the schedule
    String query = "SELECT * FROM Schedule";
    
    try (Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query)) {

   System.out.println("Available schedules:");
   System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n",  "Session ID", "Trainer ID", "Session Type", "Date", "Start Time", "End Time", "Room ID", "Booked");
   while (resultSet.next()) {
    
       int sessionId = resultSet.getInt("session_id");
       int trainerId = resultSet.getInt("trainer_id");
       String sessionType = resultSet.getString("session_type");
       java.sql.Date date = resultSet.getDate("date");
       java.sql.Time startTime = resultSet.getTime("start_time");
       java.sql.Time endTime = resultSet.getTime("end_time");
       int roomId = resultSet.getInt("room_id");
       boolean isBooked = resultSet.getBoolean("is_booked");
       

       System.out.printf("%-15d %-15d %-15s %-15s %-15s %-15s %-15d %-15b%n", sessionId, trainerId, sessionType, date, startTime, endTime, roomId, isBooked);
        }

}
}
public static void bookClass(Connection connection, Scanner scanner) throws SQLException {
    System.out.print("Enter the Session ID you want to select: ");
    int sessionId = scanner.nextInt();

    System.out.print("Enter your Member ID: ");
    int memberId = scanner.nextInt();

    
    String query = "SELECT session_type, is_booked FROM Schedule WHERE session_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, sessionId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String sessionType = resultSet.getString("session_type");
                boolean isBooked = resultSet.getBoolean("is_booked");

                // Booking is handled differently depending on if the session is a private lesson or a group lesson.
                if (sessionType.equalsIgnoreCase("private")) {
                    if (isBooked) {
                        System.out.println("The private session is already booked. Please choose a different session.");
                        return; //if private session is booked, can't be booked again
                    }
                } else if (sessionType.equalsIgnoreCase("group")) {
                   //no need to print error message if booked, because group lessons have no limit
                }

               //Allow user to input pretend card details
                System.out.println("Please enter payment details.\n");
                System.out.print("Enter card number: ");
                String cardNumber = scanner.next(); 
                System.out.print("Enter expiration date (MM/YY): ");
                String expirationDate = scanner.next(); 
                System.out.print("Enter CVV: ");
                String cvv = scanner.next(); 

               
                String insertBillingQuery = "INSERT INTO Billing (member_id, session_id, amount, description, payment_status) " +
                                            "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement billingStatement = connection.prepareStatement(insertBillingQuery)) {
                    billingStatement.setInt(1, memberId);
                    billingStatement.setInt(2, sessionId);
                    billingStatement.setBigDecimal(3, new BigDecimal("80.00")); //set price so that every class is $80
                    billingStatement.setString(4, "fitness class"); //reason for bill is "fitness class"
                    billingStatement.setString(5, "Pending"); //set as pending, because admin needs to go in and change it once processed
                    billingStatement.executeUpdate();
                }

               //set is_book to true
                String updateQuery = "UPDATE Schedule SET is_booked = true WHERE session_id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, sessionId);
                    updateStatement.executeUpdate();
                }

                if (sessionType.equalsIgnoreCase("private")) {
                    System.out.println("You have successfully booked the private session.");
                } else {
                    System.out.println("You have successfully booked the group session.");
                }
            } else {
                System.out.println("Session ID not found. Please try again.");
            }
        }
    }
}


public static void cancelClass (Connection connection, Scanner scanner) throws SQLException {
    System.out.print("Enter the Session ID of the class you would like to cancel: ");
    int sessionId = scanner.nextInt();

    String query = "SELECT session_type, is_booked FROM Schedule WHERE session_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, sessionId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                boolean isBooked = resultSet.getBoolean("is_booked");
            
                if (isBooked) {
                    String updateQuery = "UPDATE Schedule SET is_booked = false WHERE session_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, sessionId);
                        updateStatement.executeUpdate();
                        System.out.println("You have successfully canceled the session. You now have an $80.00 credit on your account");//once class is canceled, user gets $80.00 set back to account
                    }
                } else {
                    System.out.println("The session is not currently booked, so it cannot be canceled.");
                }
            } else {
                System.out.println("Session ID not found. Please try again.");
            }

}
    }}
   
public static void rescheduleClass (Connection connection, Scanner scanner) throws SQLException {
    System.out.print("Enter the Session ID of the class you would like to reschedule: ");
    int oldSessionId = scanner.nextInt();

    String cancelQuery = "UPDATE Schedule SET is_booked = false WHERE session_id = ?";
    try (PreparedStatement cancelStatement = connection.prepareStatement(cancelQuery)) {
        cancelStatement.setInt(1, oldSessionId);
        int rowsAffected = cancelStatement.executeUpdate();
        
        if (rowsAffected == 0) {
            System.out.println("Session ID not found or already available. Please try again.");
            return;
        } else {
            System.out.println("The session has been canceled. Please select a new session.");
        }
    }

    viewAndSelectSchedule(connection, scanner);//allow member to see schedule

    bookClass(connection, scanner);//allow them to book a class with original bookclass function
}

//Administrator Functions
public static void adminFunctionality(Connection connection, Scanner scanner) throws SQLException  {
    int choice;
    do {
        System.out.println("Administrator Menu:");
        System.out.println("1. Login as a administrator");
        System.out.println("2. Room Booking Management");
        System.out.println("3. Equipment Maintenance Management");
        System.out.println("4. Class schedule updating");
        System.out.println("5. Billing and payment processing");
        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
            adminLogin(connection, scanner);
            break;
            case 2:
            manageRooms(connection, scanner);
            case 3:
            manageEquipment(connection, scanner);
            break;
            case 4:
            manageSchedule(connection, scanner);
            break;
            case 5: 
            updateBilling(connection, scanner);
            break;
            case 0:
            System.out.println("Returning to User Type selection...");
            break;
            

        default:
            System.out.println("Invalid choice. Please try again.");
    }
} while (choice != 0);
}
static boolean isAdminLoggedIn = false;

public static void adminLogin (Connection connection, Scanner scanner)throws SQLException{
    System.out.println("Administrator Login");
    System.out.print("Enter Administrator ID: ");
    int adminId = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Enter Password: ");
    String passwordInput = scanner.nextLine();

    String query = "SELECT * FROM Administrator WHERE administrator_id = ? AND password = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, adminId);
        statement.setString(2, passwordInput);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Login successful!");
                isAdminLoggedIn = true;
            } else {
                System.out.println("Invalid Administrator ID or password. Please try again.");
                isAdminLoggedIn = false;
            }
        }
    }
    catch (Exception e) {
        System.out.println(e);
        isAdminLoggedIn = false;
    }
}
public static void manageRooms(Connection connection, Scanner scanner) throws SQLException {
    //Manage rooms menu 
    int choice;
    do {
        System.out.println("Room Booking Management Menu:");
        System.out.println("1. Update room bookings");
        System.out.println("2. Add a new room booking");
        System.out.println("3. Delete a room booking");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
            updateRooms(connection, scanner);
            break;
            case 2:
            addRooms(connection, scanner);
            break;
            case 3:
            deleteRooms(connection, scanner);
            break;
            case 0:
            System.out.println("Returning to User Type selection...");
            break;
            

        default:
            System.out.println("Invalid choice. Please try again.");
    }
} while (choice != 0);
}
public static void updateRooms (Connection connection, Scanner scanner) throws SQLException {
    //Make sure admin is logged in first
 if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }

    try {
        System.out.print("Enter Administrator ID: ");
        int adminId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter room ID to update: ");
        int roomId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new room number: ");
        String newRoomNum = scanner.nextLine();
        System.out.print("Enter new date(yyyy-mm-dd): ");
        String newDateStr = scanner.nextLine();
        java.sql.Date newDate = java.sql.Date.valueOf(newDateStr);
        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter new start time (HH:mm:ss): ");
        String newStartTime = scanner.nextLine();
        System.out.print("Enter new end time (HH:mm:ss): ");
        String newEndTime = scanner.nextLine();

        String query = "UPDATE Room SET room_number = ?, Date= ?, description = ?, start_time = ?, end_time = ?, managed_by = ? WHERE room_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newRoomNum);
            statement.setDate(2, newDate);
            statement.setString(3, newDescription);
            statement.setTime(4, java.sql.Time.valueOf(newStartTime));
            statement.setTime(5, java.sql.Time.valueOf(newEndTime));
            statement.setInt(6,roomId);
            statement.setInt(7, adminId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Room information updated successfully.");
            } else {
                System.out.println("Failed to update room information. Room ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void addRooms (Connection connection, Scanner scanner) throws SQLException {
    if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }
    try {
        System.out.print("Enter Administrator ID: ");
        int adminId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Adding a new room:");

        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Enter date (yyyy-mm-dd): ");
        String dateStr = scanner.nextLine();
        java.sql.Date date = java.sql.Date.valueOf(dateStr);
        System.out.print("Enter start time (HH:mm:ss): ");
        String startTimeStr = scanner.nextLine();
        java.sql.Time startTime = java.sql.Time.valueOf(startTimeStr);
        System.out.print("Enter end time (HH:mm:ss): ");
        String endTimeStr = scanner.nextLine();
        java.sql.Time endTime = java.sql.Time.valueOf(endTimeStr);
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        //INSERT to add a room
        String query = "INSERT INTO Room (room_number, Date, start_time, end_time, description, managed_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomNumber);
            statement.setDate(2, date);
            statement.setTime(3, startTime);
            statement.setTime(4, endTime);
            statement.setString(5, description);
            statement.setInt(6, adminId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New room added successfully.");
            } else {
                System.out.println("Failed to add new room.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}

public static void deleteRooms(Connection connection, Scanner scanner) throws SQLException {

    if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }
    try {
        System.out.print("Enter room ID to delete: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();
    
        String query = "DELETE FROM Room WHERE room_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomId);   
        
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Room deleted successfully.");
            } else {
                System.out.println("Failed to delete room. Room ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}


public static void manageEquipment(Connection connection, Scanner scanner) throws SQLException {
    int choice;
    do {
        System.out.println("Equipment Management Menu:");
        System.out.println("1. Update equipment table");
        System.out.println("2. Add a new piece of equipment");
        System.out.println("3. Delete a piece of equipment");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
            updateEquipment(connection, scanner);
            break;
            case 2:
            addEquipment(connection, scanner);
            break;
            case 3:
            deleteEquipment(connection, scanner);
            break;
            case 0:
            System.out.println("Returning to User Type selection...");
            break;
            

        default:
            System.out.println("Invalid choice. Please try again.");
    }
} while (choice != 0);
}

public static void updateEquipment (Connection connection, Scanner scanner) throws SQLException {

    if (!isAdminLoggedIn) {
           System.out.println("Please log in as an administrator first.");
           return;
       }
   
       try {

            System.out.print("Enter Administrator ID: ");
            int adminId = scanner.nextInt();
            scanner.nextLine();

           System.out.print("Enter equipment ID to update: ");
           int equipmentId = scanner.nextInt();
           scanner.nextLine(); 
   
           System.out.print("Enter new equipment name: ");
           String newEquipmentName = scanner.nextLine();
           System.out.print("Enter new description: ");
           String newDescription = scanner.nextLine();
           System.out.print("Enter new maintence date(yyyy-mm-dd): ");
           String newDateStr = scanner.nextLine();
           java.sql.Date newDate = java.sql.Date.valueOf(newDateStr);
          
   
           String query = "UPDATE Equipment SET name = ?,description = ?, maintenance_date= ?, managed_by = ? WHERE equipment_id = ?";
           try (PreparedStatement statement = connection.prepareStatement(query)) {
               statement.setString(1, newEquipmentName);
               statement.setString(2, newDescription);
               statement.setDate(3, newDate);
               statement.setInt(4,equipmentId);
               statement.setInt(5, adminId); 
   
               int rowsUpdated = statement.executeUpdate();
               if (rowsUpdated > 0) {
                   System.out.println("Equipment information updated successfully.");
               } else {
                   System.out.println("Failed to update equipment information.Equipment ID not found.");
               }
           }
       } catch (Exception e) {
           System.out.println(e);
       }
   }



   public static void addEquipment(Connection connection, Scanner scanner) throws SQLException {
    if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }
    try {
        System.out.print("Enter Administrator ID: ");
        int adminId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Adding a new piece of equipment:");

        System.out.print("Enter new equipment name: ");
           String newEquipmentName = scanner.nextLine();
           System.out.print("Enter new description: ");
           String newDescription = scanner.nextLine();
           System.out.print("Enter new maintence date(yyyy-mm-dd): ");
           String newDateStr = scanner.nextLine();
           java.sql.Date newDate = java.sql.Date.valueOf(newDateStr);

        String query = "INSERT INTO Equipment (name, description, maintenance_date, managed_by) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newEquipmentName);
            statement.setString(2, newDescription);
            statement.setDate(3, newDate);
            statement.setInt(4, adminId); 

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New equipment added successfully.");
            } else {
                System.out.println("Failed to add new equipment.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}

public static void deleteEquipment(Connection connection, Scanner scanner) throws SQLException {

    if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }
    try {
        System.out.print("Enter equipment ID to delete: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();
    
        String query = "DELETE FROM Equipment WHERE equipment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomId);   
        
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Selected equipment deleted successfully.");
            } else {
                System.out.println("Failed to delete equipment. Equipment ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}



public static void manageSchedule(Connection connection, Scanner scanner) throws SQLException {
    int choice;
    do {
        System.out.println("Schedule Management Menu:");
        System.out.println("1. Update Schedule");
        System.out.println("2. Add information to schedule table");
        System.out.println("3. Delete class from schedule");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
            updateSchedule(connection, scanner);
            break;
            case 2:
            addSchedule(connection, scanner);
            break;
            case 3:
            deleteSchedule(connection, scanner);
            case 0:
            System.out.println("Returning to User Type selection...");
            break;
            

        default:
            System.out.println("Invalid choice. Please try again.");
    }
} while (choice != 0);
}



public static void updateSchedule (Connection connection, Scanner scanner) throws SQLException {

    if (!isAdminLoggedIn) {
           System.out.println("Please log in as an administrator first.");
           return;
       }
   
       try {
            System.out.print("Enter Administrator ID: ");
            int adminId = scanner.nextInt();
            scanner.nextLine();

           System.out.print("Enter session ID to update: ");
           int sessionId = scanner.nextInt();
           scanner.nextLine(); 
   
           System.out.print("Enter new trainer ID: ");
           int newTrainerId = scanner.nextInt();
           scanner.nextLine(); 

            System.out.print("Enter new session type (group or private): ");
            String newSessionType = scanner.nextLine();
            System.out.print("Enter new date (yyyy-mm-dd): ");
            String newDateStr = scanner.nextLine();
            java.sql.Date newDate = java.sql.Date.valueOf(newDateStr);
            System.out.print("Enter new start time (HH:mm:ss): ");
            String newStartTime = scanner.nextLine();
            System.out.print("Enter new end time (HH:mm:ss): ");
            String newEndTime = scanner.nextLine();
            System.out.print("Enter new room ID: ");
            int newRoomId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Is the session booked? (true/false): ");
            boolean newIsBooked = scanner.nextBoolean();


   
            String query = "UPDATE Schedule SET trainer_id = ?, session_type = ?, date = ?, start_time = ?, end_time = ?, room_id = ?, is_booked = ?, managed_by = ? WHERE session_id = ?";
        
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, newTrainerId);
                statement.setString(2, newSessionType);
                statement.setDate(3, newDate);
                statement.setTime(4, java.sql.Time.valueOf(newStartTime));
                statement.setTime(5, java.sql.Time.valueOf(newEndTime));
                statement.setInt(6, newRoomId);
                statement.setBoolean(7, newIsBooked);
                statement.setInt(8, sessionId);
                statement.setInt(9, adminId); 
    
   
               int rowsUpdated = statement.executeUpdate();
               if (rowsUpdated > 0) {
                   System.out.println("Schedule updated successfully.");
               } else {
                   System.out.println("Failed to update schedule.Session ID not found.");
               }
           }
       } catch (Exception e) {
           System.out.println(e);
       }
   }



   public static void addSchedule(Connection connection, Scanner scanner) throws SQLException {
    if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }
    try {
            System.out.print("Enter Administrator ID: ");
            int adminId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Adding a new class to the schedule:");

            System.out.print("Enter new trainer ID: ");
            int newTrainerId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Enter new session type (group or private): ");
            String newSessionType = scanner.nextLine();
            System.out.print("Enter new date (yyyy-mm-dd): ");
            String newDateStr = scanner.nextLine();
            java.sql.Date newDate = java.sql.Date.valueOf(newDateStr);
            System.out.print("Enter new start time (HH:mm:ss): ");
            String newStartTime = scanner.nextLine();
            System.out.print("Enter new end time (HH:mm:ss): ");
            String newEndTime = scanner.nextLine();
            System.out.print("Enter new room ID: ");
            int newRoomId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Is the session booked? (true/false): ");
            boolean newIsBooked = scanner.nextBoolean();

        String query = "INSERT INTO Schedule (trainer_id , session_type, date,start_time, end_time, room_id, is_booked, managed_by ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newTrainerId);
            statement.setString(2, newSessionType);
            statement.setDate(3, newDate);
            statement.setTime(4, java.sql.Time.valueOf(newStartTime));
            statement.setTime(5, java.sql.Time.valueOf(newEndTime));
            statement.setInt(6, newRoomId);
            statement.setBoolean(7, newIsBooked);
            statement.setInt(8, adminId); 

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New schedule added successfully.");
            } else {
                System.out.println("Failed to add session to schedule.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}
public static void deleteSchedule(Connection connection, Scanner scanner) throws SQLException {

    if (!isAdminLoggedIn) {
        System.out.println("Please log in as an administrator first.");
        return;
    }
    try {
        System.out.print("Enter session ID to delete: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();
    
        String query = "DELETE FROM Schedule WHERE session_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, sessionId);   
        
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Selected session deleted successfully.");
            } else {
                System.out.println("Failed to delete session. session ID not found.");
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
}


public static void updateBilling (Connection connection, Scanner scanner) throws SQLException {

    if (!isAdminLoggedIn) {
           System.out.println("Please log in as an administrator first.");
           return;
       }
       //Allow admin to update payment status
       try {
            System.out.print("Enter Administrator ID: ");
            int adminId = scanner.nextInt();
            scanner.nextLine();
        
           System.out.print("Enter Billing ID to update:" );
           int billingId = scanner.nextInt();
           scanner.nextLine(); 

            System.out.print("Update payment status: ");
            String paymentStatus = scanner.nextLine();

            String query = "UPDATE Billing SET payment_status = ?, managed_by = ? WHERE billing_id = ?";
        
            try (PreparedStatement statement = connection.prepareStatement(query)) {
          
                statement.setString(1, paymentStatus);
                statement.setInt(2, billingId);
                statement.setInt(3, adminId); 
    
   
               int rowsUpdated = statement.executeUpdate();
               if (rowsUpdated > 0) {
                   System.out.println("Billing table updated successfully.");
               } else {
                   System.out.println("Failed to update Billing table.Billing ID not found.");
               }
           }
       } catch (Exception e) {
           System.out.println(e);
       }
   }





    }




