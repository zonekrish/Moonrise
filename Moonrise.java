/**
 * This program currently uses pure physics and orbital approximations in order to predict moonrise. 
 * Eventually, the tilt of our moon's orbit will be considered in the calculation. As of now, there will be ~15 minute inaccuracy for the prediction.
 * Also, I will use a better sinosudioal model (probably with parametrics!) to make good predictions. The Git repository should have a PDF of my notes
 * in developing the algorithm to predict moonrise. You can correct my shit physics if needed, and hopefully the PDF will give better insight as to why
 * I chose to do what I did.
 *
 * @author Krishna
 * @version 3/31/2024
 */
public class Moonrise {
    // Constants for calculation
    private static final double siderealDay = 86164.0905;
    private static final double semiMajorAxis = 364505343.3;
    private static final double earthMass = 5.97219 * Math.pow(10, 24);
    private static final double gravity = 6.67 * Math.pow(10, -11);
    
    private static int[] convertHumanTime(String time) {
        // Array for [hour, minute, AM/PM]
        int[] times = new int[3];
        
        int colon = time.indexOf(":");
        int space = time.indexOf(" ");
        if (colon == -1) { 
            // No colon - invalid time
            times[0] = -1;
            times[1] = -1;
        } else {
            // Parsing int from String
            int hour = Integer.parseInt(time.substring(0, colon));
            int minutes = Integer.parseInt(time.substring(colon+1, space));
            times[0] = hour;
            times[1] = minutes;
            
            // Determining AM/PM
            
            String period = time.substring(space+1);
            
            // Note: AM = 0 and PM = 1
            if (period.equals("AM")) {
                times[2] = 0;
            } else if (period.equals("PM")) {
                times[2] = 1;
                times[0] += 12; // Shifting over to 24-hour format
            } else {
                times[2] = -1;
            }
            
        }
        
        
        
        return times;
    }
    
    private static String convertMathTime(int[] times) {
        // Make sure time is valid
        for (int i: times) {
            if (i < 0) {
                return "Invalid time";
            }
        }
        
        // Set up proper AM/PM indicator
        String indicator = "AM";
        if (times[2] == 1) {
            indicator = "PM";
            times[0] -= 12;
        }
        
        String hour = "" + times[0];
        
        String time = hour + ":";
        
        String minutes = "" + times[1];
        
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        
        time += minutes;
        
        time += " " + indicator;
        
        return time;
    }
    
    public static String getMoonrise(String lastTime, double meriDistance) {
        String time = "";
        
        // Convert miles to meters
        meriDistance*=1609.34;
        
        // Get old times + set up new times
        int[] oldTimes = convertHumanTime(lastTime);
        int[] newTimes = new int[3];
        
        // Get "circumference" portion of calculation
        double circum = 2 * Math.PI * meriDistance;
        
        // Calculate the length of a lunar day for particular meridian distance
        double lunarDay = circum * siderealDay / (circum - siderealDay*Math.sqrt(gravity * earthMass * (2/meriDistance - 1/semiMajorAxis)));
        
        //System.out.println("Time: " + lunarDay + " s");
        
        // Convert seconds to hours
        double toHours = lunarDay / 60 / 60;
        int hours = (int)toHours;
        
        // Get minutes based off hours
        double minutes = toHours - hours;
        minutes = minutes * 60;
        minutes = (int)Math.round(minutes);
        
        if (minutes >= 60) {
            minutes-=60;
            hours ++;
        }
        
        // Add hour and minute counts to the old times
        int newHour = oldTimes[0] + (int)hours%24;
        int newMin = oldTimes[1] + (int)minutes;
        
        // Correct to proper formatting
        if (newMin >= 60) {
            newMin-=60;
            newHour++;
        }
        
        newTimes[0] = newHour;
        newTimes[1] = newMin;
        
        if (newTimes[0] > 12) {
            // Set to PM if needed
            newTimes[2] = 1;
        }
        
        time = convertMathTime(newTimes);
        
        return time;
    }
}