package annotation1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {

    public static final String ABC = "A";
    
    @SuppressWarnings({"deprecation","unused"})
    private static String list;
    
    /**
     * @deprecated
     */
    @SuppressWarnings({"deprecation","static-access"})
    public void list() {
        System.out.println(this.ABC);
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    public void list2() {
        
    }
    
    @SuppressWarnings(value= {"unchecked", "unused"})
    public List getList() {
        List list = new ArrayList();
        return list;
    }
    
    public Date getDate() {
        return new Date();
    }
    
    public static void main(String[] args) {
        Test2 t = new Test2();
        java.sql.Date d = t.getDate();
        System.out.println(d);
    }
}

class Test2 extends Test {
    
    public void list() {
        
    }
    
    public void list2() {
        
    }
    
    public java.sql.Date getDate() {
        return new java.sql.Date((new Date()).getTime());
    }
}
