package cn.dark;

import com.mongodb.client.*;
import org.bson.Document;

/**
 * @author dark
 * @date 2019-10-24
 */
public class TestMongo {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://192.168.0.106:27017");
        MongoDatabase database = mongoClient.getDatabase("test1");
        MongoCollection<Document> coll = database.getCollection("coll");
        Document document = new Document();
        document.append("name", "lwj11").append("age", 11);
        coll.insertOne(document);

        FindIterable<Document> documents = coll.find();
        for (Document document1 : documents) {
            System.out.println(document1);
            System.out.println(document.get("name"));
            System.out.println(document.get("age"));
        }
    }

}
