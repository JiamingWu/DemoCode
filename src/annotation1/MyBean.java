package annotation1;

public class MyBean {

    private String id;
    private String name;
    
    @MinLength(3)
    @MaxLength(10)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @MinLength(1)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
