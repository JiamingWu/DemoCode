package decimal;

import java.math.BigDecimal;

public class TestDecimal {

	public static void main(String[] args) {
		System.out.println(new BigDecimal(1.2));
		System.out.println(BigDecimal.valueOf(1.2));
		
		//�����ɭY�S BigDecimal.ROUND_HALF_UP �|�X exception
		//setScale �ݳ]�b �Q���Ƥ~�|�ͮ�.
		BigDecimal db = (new BigDecimal("100").setScale(3)).divide(new BigDecimal(3), BigDecimal.ROUND_HALF_UP);
		System.out.println(db);

		BigDecimal db2 = (new BigDecimal("100").setScale(3)).divide(new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP));
		System.out.println(db2);

		System.out.println(new BigDecimal(Double.toString(712312312327.42325)));
		System.out.println(new BigDecimal(Double.toString(712312312327.42326)));
		System.out.println(new BigDecimal(Double.toString(712312312327.42327)));
		System.out.println(new BigDecimal(Double.toString(712312312327.42328)));
	}
}
