import java.util.Scanner;
/**
 * Here is a basic runner program to utilize some of the features of Moonrise.
 *
 * @author Krishna Rao
 * @version 1
 */
public class Main {
    public static void main(String[] args) {
        System.out.println();
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Last moonrise (HH:MM AM/PM): ");
        String time = sc.nextLine();
    
        System.out.println("Meridian distance (miles): ");
        double distance = sc.nextDouble();
        
        System.out.println();
        
        System.out.println("Next moonrise: " + Moonrise.getMoonrise(time, distance));
        
    }
}