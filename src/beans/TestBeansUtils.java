package beans;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;

public class TestBeansUtils {

    public static void main(String[] args) throws Exception {
        
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new SqlDateConverter(null), Date.class);
        
        TestVO vo = new TestVO();
        vo.setId("Test");
        vo.setName(null);
        vo.setDate(null);
        
        TestVO vo1 = new TestVO();
        vo1.setId("VO1 ID");
        vo1.setName("VO1 Name");
        vo1.setAmt(new BigDecimal("100"));
        vo1.setDate(new java.sql.Date(new Date().getTime()));
        BeanUtils.copyProperties(vo1, vo);
        
        System.out.println(vo);
        System.out.println(vo1);
    }
}

