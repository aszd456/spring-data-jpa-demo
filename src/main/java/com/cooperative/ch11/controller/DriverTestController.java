package com.cooperative.ch11.controller;

import com.cooperative.ch11.entity.Baike;
import com.cooperative.ch11.entity.Comment;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/19 0:45
 */
@RestController
@RequestMapping("ch11")
public class DriverTestController {

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/baike/{name}")
    public Baike findUser(@PathVariable String name) {
        final String id = name;
        Baike baike = mongoTemplate.execute(db -> {

            MongoCollection<Document> collection = db.getCollection("test");
            MongoCursor<Document> cursor = collection.find(new Document("_id", id)).iterator();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }
            Document doc = collection.find(new Document("_id", id)).first();
            System.out.println(doc.toJson());
            Baike obj = new Baike();

            obj.setDesc(doc.getString("desc"));
            Comment comment = new Comment();
            Document docComment = doc.get("comment", Document.class);
            comment.setBad(docComment.getDouble("bad"));
            comment.setGood(docComment.getDouble("good"));
            obj.setComment(comment);
            return obj;
        });
        return baike;
    }
}
