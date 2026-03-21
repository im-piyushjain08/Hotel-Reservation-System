package Projects;

import java.sql.*;
import java.util.IllegalFormatException;
import java.util.IllegalFormatFlagsException;
import java.util.Scanner;
import java.util.TimerTask;

public class Hotel_Reservation {
    public static void main(String[] args) throws InterruptedException{
        String url = "jdbc:mysql://localhost:3306/hotel_db1";
        String username = "root";
        String password = "Your_password_here";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            while (true) {
                System.out.println();
                System.out.println("||🏨 Hotel Reservation System ||");
                System.out.println();
                System.out.println("1.Reserve a Room");
                System.out.println("2.View Reservations");
                System.out.println("3.Get Room Number");
                System.out.println("4.Update Reservations");
                System.out.println("5.Delete Reservations");
                System.out.println("0.Exit");
                System.out.println("Choose an option: ");
                Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();
                if (choice == 1) {
                    reserveRoom(connection, sc);
                } else if (choice == 2) {
                    viewReservations(connection);
                } else if (choice == 3) {
                    getRoomNumber(connection,sc);
                } else if (choice == 4) {
                    updateReservation(connection,sc);
                } else if (choice == 5) {
                    deleteReservation(connection,sc);
                } else if (choice == 0) {
                    exit();
                } else {
                    System.err.println("Invalid choice.Please Make sure you enter a valid choice");
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter guest name : ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "insert into reservations(guest_name,room_number,contact_number) values('" + guestName + "'," + roomNumber + ",'" + contactNumber + "');";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation successfull");
                } else {
                    System.out.println("Reservation failed.");
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void viewReservations(Connection connection) {
        String sql = "select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";

        try (Statement statement = connection.createStatement(); ResultSet rs=statement.executeQuery(sql)) {

            System.out.println("Current Reservation: ");
            System.out.println("+------------------+------------------+-------------------+------------------------+---------------------------+");
            System.out.println("| Reservation ID   | Guest            | Room Number       | Contact Number         | Reservation Date          |");
            System.out.println("+------------------+------------------+-------------------+------------------------+---------------------------+");

            while(rs.next()){
                int reservationId=rs.getInt("reservation_id");
                String guestName=rs.getString("guest_name");
                int roomNumber=rs.getInt("room_number");
                String contactNumber=rs.getString("contact_number");
                String reservationDate=rs.getTimestamp("reservation_date").toString();

          System.out.printf("| %-16d | %-16s | %-17d | %-22s | %-24s  |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);

            }
            System.out.println("+------------------+------------------+-------------------+------------------------+---------------------------+");


        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void getRoomNumber(Connection connection,Scanner sc){
        System.out.println("Enter reservation ID: ");
        int reservationId=sc.nextInt();
        System.out.println("Enter guest name: ");
        String guestName=sc.next();

        String sql="select room_number from reservations where reservation_id="+reservationId+" and guest_name='"+guestName+"'";

        try(Statement statement=connection.createStatement();ResultSet rs=statement.executeQuery(sql)){
            if(rs.next()){
                int roomNumber=rs.getInt("room_number");
                System.out.println("Room number for Reservation ID "+reservationId+" and Guest "+guestName+" is: "+roomNumber);
            } else{
                System.out.println("Reservation not found for the given ID and guest name.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
     private static void updateReservation(Connection connection,Scanner sc){
         System.out.println("Enter reservation ID to update: ");
         int reservationId=sc.nextInt();
         sc.nextLine();   // Consume the newLine character

         if(!reservationExists(connection,reservationId)){
             System.out.println("Reservation not found for the given ID.");
             return;
         }

         System.out.println("Enter new guest name: ");
         String newGuestName=sc.nextLine();
         System.out.println("Enter new room number: ");
         int newRoomNumber=sc.nextInt();
         System.out.println("Enter new contact number: ");
         String newContactNumber=sc.next();

         String sql="update reservations set guest_name='"+newGuestName+"',"+"room_number="+newRoomNumber+","+"contact_number='"+newContactNumber+"' "+"where reservation_id="+reservationId;

         try(Statement statement=connection.createStatement()){
             int affectedRows=statement.executeUpdate(sql);

             if(affectedRows>0){
                 System.out.println("Reservation updated successfully!");
             }
             else{
                 System.out.println("Reservation update failed.");
             }
         } catch (Exception e) {
             throw new RuntimeException(e);
         }


    }

    private static void deleteReservation(Connection connection,Scanner sc){
        System.out.println("Enter reservation ID to delete: ");
        int reservationId=sc.nextInt();

        if(!reservationExists(connection,reservationId)){
            System.out.println("Reservation not found for the given ID.");
            return;
        }

        String sql="Delete from reservations where reservation_id="+reservationId+";";
         try(Statement statement=connection.createStatement()){
             int affectedRows=statement.executeUpdate(sql);

             if(affectedRows>0){
                 System.out.println("Reservation deleted successfully!");
             }
             else{
                 System.out.println("Reservation deletion failed.");
             }
         } catch (Exception e) {
             throw new RuntimeException(e);
         }


    }
    private static boolean reservationExists(Connection connection,int reservationId){
        String sql="select reservation_id from reservations where reservation_id="+reservationId+";";

        try(Statement statement=connection.createStatement();ResultSet rs=statement.executeQuery(sql)){
             return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public static void exit() throws InterruptedException{
        System.out.println("Existing System");
        for(int i=0;i<=5;i++){
            System.out.print(".");
            Thread.sleep(500);

        }
        System.out.println("💁‍♂️ ThankYou for Using Hotel Reservation System");
        System.exit(0);
    }


}
