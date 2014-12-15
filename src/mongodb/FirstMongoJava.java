package mongodb;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class FirstMongoJava {

    public static void main(String[] args) throws Exception{
     
        MongoClient mongo = new MongoClient("localhost" , 27017);
     
        List<String> dbs = mongo.getDatabaseNames();
        System.out.println("==  DB ==");
        for(String db : dbs){
            System.out.println(db);
        }
        
        DB db = mongo.getDB("users");
        Set<String> tables = db.getCollectionNames();
        
        System.out.println("==  Collections ==");
        for(String coll : tables){
            System.out.println(coll);
        }
        
        DBCollection table = db.getCollection("users");
        
        System.out.println("== users count: " + table.count());
        
        BasicDBObject document = new BasicDBObject("name", "test")
                .append("age", 30)
                .append("createdDate", new Date());
        table.insert(document);
        
        System.out.println("== insert one data ==");
        
        BasicDBObject searchQuery = new BasicDBObject("name", "test")
                .append("age", new BasicDBObject("$gt", 35));
        DBCursor cursor = table.find(searchQuery);
     
        System.out.println("== query db==");
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
        
        // update
        BasicDBObject query = new BasicDBObject("name", "test");
     
        BasicDBObject newDocument = new BasicDBObject("age", 32);
     
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.append("$set", newDocument); //透過 $set 代表更新指定欄位，否則會是將找到的"替換"，其他欄位值會不見
     
        table.update(query, updateObj);   //更新一筆
        
        updateObj = new BasicDBObject();
        updateObj.append("$inc", new BasicDBObject().append("age", 5)); //透過 $inc 來增加欄位值
     
        table.updateMulti(query, updateObj);    //更新多筆符合的
        
        System.out.println("== update finish==");
        
        document = new BasicDBObject("name", "test2")
                .append("age", 30)
                .append("createdDate", new Date());
        table.insert(document);
        
        searchQuery = new BasicDBObject("name", "test2");
        table.remove(document);
        
        System.out.println("== remove finish==");
        
    }
    
}
