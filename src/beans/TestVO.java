package beans;

import java.math.BigDecimal;
import java.util.Date;

public class TestVO {
    private String id;
    private String name;
    private BigDecimal amt;
    private Date date;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "TestVO [id=" + id + ", name=" + name + ", amt=" + amt
                + ", date=" + date + "]";
    }
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
