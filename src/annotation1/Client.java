package annotation1;


public class Client {

    private int a = 0;
    private String myBean1 = "";
    
    public static void main(String[] args) {
        MyBean myBean2 = new MyBean();
        myBean2.setId("111111111000000000");
        myBean2.setName(null);
        System.out.println("result:" + VaildatorUtil.validateBean(myBean2));
        
        String abc = "D:\\D:\\abc\\def\\dfa\r\n" + 
                "dsfasdfabc\\def\\dfa";
        System.out.println("~~~~~~~~~~");
        System.out.println("!!!!!!");
        System.out.println("!!!!!!");
        System.out.println("!!!!!!");
        
        
    }
    
    
}
