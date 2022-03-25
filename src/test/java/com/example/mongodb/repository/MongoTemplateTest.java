package com.example.mongodb.repository;

import com.example.mongodb.model.GroceryItem;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@SpringBootTest
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoClient mongoClient;

    @Test
    public void saveAndFindTest() {
        GroceryItem groceryItem = GroceryItem.builder()
                .id(UUID.randomUUID().toString())
                .category("test_cate_01")
                .name("test_name_01")
                .quantity(100)
                .build();

        // 추가
        mongoTemplate.insert(groceryItem);

        GroceryItem resItem = mongoTemplate.findById(groceryItem.getId(), GroceryItem.class);
        assertThat(resItem).isNotNull();
        assertThat(resItem.getCategory()).isEqualTo("test_cate_01");
        assertThat(resItem.getName()).isEqualTo("test_name_01");
        assertThat(resItem.getQuantity()).isEqualTo(100);

        // 수정
        mongoTemplate.updateFirst(query(where("name").is("test_name_01")), update("quantity", 50), GroceryItem.class);
        resItem = mongoTemplate.findById(groceryItem.getId(), GroceryItem.class);
        assertThat(resItem).isNotNull();
        assertThat(resItem.getName()).isEqualTo("test_name_01");
        assertThat(resItem.getQuantity()).isEqualTo(50);

        resItem = mongoTemplate.update(GroceryItem.class)
                .matching(new Query(Criteria.where("name").is("test_name_01")))
                .apply(new Update().inc("quantity", 10))
                .withOptions(FindAndModifyOptions.options().upsert(true).returnNew(true))
                .findAndModifyValue();
        assertThat(resItem).isNotNull();
        assertThat(resItem.getName()).isEqualTo("test_name_01");
        assertThat(resItem.getQuantity()).isEqualTo(60);

        // 삭제
        mongoTemplate.remove(groceryItem);
    }


    @Test
    public void queryTest() {
        GroceryItem groceryItem = GroceryItem.builder()
                .id(UUID.randomUUID().toString())
                .category("test_cate_02")
                .name("test_name_02")
                .quantity(100)
                .build();

        // 추가
        mongoTemplate.insert(groceryItem);

        // 검색 find 메소드(json 사용한 검색)
        BasicQuery query = new BasicQuery("{ quantity : { $gt : 50 }}");
        List<GroceryItem> resFindList = mongoTemplate.find(query, GroceryItem.class);
        assertThat(resFindList).isNotNull();
        assertThat(resFindList.get(0).getName()).isEqualTo("test_name_02");
        assertThat(resFindList.get(0).getQuantity()).isEqualTo(100);

        // 검색 query 메소드
        List<GroceryItem> resQueryList = mongoTemplate.query(GroceryItem.class)
                .matching(query(where("quantity").gt(50)))
                .all();
        assertThat(resQueryList).isNotNull();
        assertThat(resQueryList.get(0).getName()).isEqualTo("test_name_02");
        assertThat(resQueryList.get(0).getQuantity()).isEqualTo(100);


        // 삭제
        mongoTemplate.remove(groceryItem);
    }

    @Test
    public void transactionTest() {

    }
}
