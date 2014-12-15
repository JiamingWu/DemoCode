package cron;

public class CronExpress {

    public static void main(String[] args) {
        System.out.println(getCronStringByDay(12, 30));
        System.out.println(getCronStringByMonth(1, 12, 30));
        System.out.println(getCronStringByWeek(5, 12, 30));
    }
    
    public static String getCronStringByDay(int hour, int minute) {
        return "0 " + minute + " " + hour + " * * ?";
    }
    
    public static String getCronStringByMonth(int day, int hour, int minute) {
        if (day > 28) {
            return "0 " + minute + " " + hour + " L * ?";
        }
        return "0 " + minute + " " + hour + " " + day + " * ?";
    }
    
    public static String getCronStringByWeek(int week, int hour, int minute) {
        final String[] weeks = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        return "0 " + minute + " " + hour + " ? * " + weeks[week];
    }
}
