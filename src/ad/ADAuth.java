package ad;

/**
*  testAD.java
*  連到Microsoft Active Directory取得資料
*/
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class ADAuth {
	//環境設定
	private Hashtable env = null;
	//目錄
	DirContext ctx = null;
	//是否能login
	boolean bLogin = false;

	public ADAuth(String strId ,String strPassword ){
		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://192.168.10.199/DC=apht2003,DC=mis,DC=apht,DC=net");
		env.put(Context.SECURITY_AUTHENTICATION,"Simple");
		env.put(Context.SECURITY_PRINCIPAL,"cn="+strId+",DC=apht2003,DC=mis,DC=apht,DC=net");
		env.put(Context.SECURITY_CREDENTIALS , strPassword );
	}//end ADAuth()

	public ADAuth(){
		this("吳家銘,OU=資訊部,OU=亞太高新", "apht2004ABC");
	}

	public boolean checkAuth(){
		try{
			ctx = new InitialDirContext(env);
			bLogin = true ;
		} catch(javax.naming.AuthenticationException authe) {
			bLogin = false ;
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			try{
				ctx.close();
			}catch(Exception Ignore){}
		}
		return bLogin ;
	}//end checkAuth

	public static void main(String args[]){
		ADAuth t = null;
		if(2 == args.length ){
			t = new ADAuth(args[0],args[1]);
			System.out.println("ID: "+args[0]);
			System.out.println("PW: "+args[1]);
		}else{
			t = new ADAuth();
			System.out.println("ID: Administrator");
			System.out.println("PW: xxxxx");
		}
		System.out.println("------------------");
		System.out.println("Return Value =>" + t.checkAuth());
	}
}//end class ADAuth