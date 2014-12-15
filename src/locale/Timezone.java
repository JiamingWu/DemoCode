package locale;

import java.util.Locale;
import java.util.TimeZone;

public class Timezone {

    public static void main(String[] args) {
        
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            TimeZone timeZone = TimeZone.getTimeZone(id);
            System.out.println(timeZone.getID() + "," + timeZone.getDisplayName(Locale.ENGLISH));
        }
        
//        Locale[] ls = Locale.getAvailableLocales();
//        for (Locale l : ls) {
//            System.out.println(l.getDisplayName() + "," + l.toString() + "," + l.toLanguageTag());
//            
//            Locale t = Locale.forLanguageTag(l.toLanguageTag());
//            System.out.println(l == t);
//        }
    }
    
}
