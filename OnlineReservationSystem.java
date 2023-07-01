import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class OnlineReservationSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Database database = new Database();

    public static void main(String[] args) {
        loginForm();
    }

    public static void loginForm() {
        System.out.println("==== Login Form ====");
        System.out.print("Enter login ID: ");
        String loginId = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (isValidLogin(loginId, password)) {
            mainMenu();
        } else {
            System.out.println("Invalid login credentials. Please try again.");
            loginForm();
        }
    }

    public static void mainMenu() {
        System.out.println("==== Main Menu ====");
        System.out.println("1. Make a Reservation");
        System.out.println("2. Cancel a Reservation");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                reservationForm();
                break;
            case 2:
                cancellationForm();
                break;
            case 3:
                System.out.println("Thank you for using the Online Reservation System. Have a safe journey!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                mainMenu();
        }
    }

    public static void reservationForm() {
        System.out.println("==== Reservation Form ====");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter train number: ");
        int trainNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter class type: ");
        String classType = scanner.nextLine();
        System.out.print("Enter date of journey: ");
        String dateOfJourney = scanner.nextLine();
        System.out.print("Enter starting place: ");
        String startingPlace = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        String pnrNumber = generatePNR(); // Generate a PNR number
        Reservation reservation = new Reservation(pnrNumber, name, trainNumber, classType, dateOfJourney, startingPlace, destination);
        database.saveReservation(reservation);

        System.out.println("Reservation successful!");
        System.out.println("PNR Number: " + pnrNumber);
        System.out.println(reservation);
        System.out.println("Press Enter to return to the main menu.");
        scanner.nextLine();
        mainMenu();
    }

    public static void cancellationForm() {
        System.out.println("==== Cancellation Form ====");
        System.out.print("Enter PNR number: ");
        String pnrNumber = scanner.nextLine();

        Reservation reservation = database.getReservationByPNR(pnrNumber);
        if (reservation != null) {
            System.out.println("Reservation found:");
            System.out.println(reservation);
            System.out.print("Press 'OK' to confirm cancellation: ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("OK")) {
                database.cancelReservation(reservation);
                System.out.println("Reservation canceled successfully.");
            } else {
                System.out.println("Cancellation aborted.");
            }
        } else {
            System.out.println("No reservation found with the given PNR number.");
        }

        mainMenu();
    }

    public static boolean isValidLogin(String loginId, String password) {
        return loginId.equals("admin") && password.equals("987654321");
    }

    public static String generatePNR() {
        // Generate a random alphanumeric PNR number
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * alphanumeric.length());
            sb.append(alphanumeric.charAt(index));
        }
        return sb.toString();
    }
}

class Reservation {
    private String pnrNumber;
    private String name;
    private int trainNumber;
    private String classType;
    private String dateOfJourney;
    private String startingPlace;
    private String destination;

    public Reservation(String pnrNumber, String name, int trainNumber, String classType, String dateOfJourney, String startingPlace, String destination) {
        this.pnrNumber = pnrNumber;
        this.name = name;
        this.trainNumber = trainNumber;
        this.classType = classType;
        this.dateOfJourney = dateOfJourney;
        this.startingPlace = startingPlace;
        this.destination = destination;
    }
        public String getPnrNumber() {
        return pnrNumber;
    }


    @Override
    public String toString() {
        return "Reservation Details:\n" +
                "PNR Number: " + pnrNumber + "\n" +
                "Name: " + name + "\n" +
                "Train Number: " + trainNumber + "\n" +
                "Class Type: " + classType + "\n" +
                "Date of Journey: " + dateOfJourney + "\n" +
                "Starting Place: " + startingPlace + "\n" +
                "Destination: " + destination + "\n";
    }
}
class Database {
    private Map<String, Reservation> reservations;

    public Database() {
        this.reservations = new HashMap<>();
    }

    public void saveReservation(Reservation reservation) {
        reservations.put(reservation.getPnrNumber(), reservation);
        System.out.println("Saving reservation to the database: " + reservation);
    }

    public Reservation getReservationByPNR(String pnrNumber) {
        return reservations.get(pnrNumber);
    }

    public void cancelReservation(Reservation reservation) {
        reservations.remove(reservation.getPnrNumber());
        System.out.println("Reservation canceled successfully.");
    }
}

