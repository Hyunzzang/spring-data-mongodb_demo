package com.example.mongodb.repository;

import com.example.mongodb.model.GroceryItem;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void saveAndFindTest() {
        GroceryItem groceryItem = GroceryItem.builder()
                .id(UUID.randomUUID().toString())
                .category("test_cate")
                .name("test_name")
                .quantity(100)
                .build();

        itemRepository.save(groceryItem);

        GroceryItem resGroceryItem = itemRepository.findItemByName("test_name");
        assertThat(resGroceryItem).isNotNull();
        assertThat(resGroceryItem.getCategory()).isEqualTo("test_cate");
        assertThat(resGroceryItem.getName()).isEqualTo("test_name");
        assertThat(resGroceryItem.getQuantity()).isEqualTo(100);

        itemRepository.delete(groceryItem);
    }


    /**
     * Retryable writes require a replica set or sharded cluster, and do not support standalone instances.
     * 테스트 환경의 몽고디비가 단독서버라 트랙잭션 테스트가 안됨
     */
    @Disabled
    @Test
    @Transactional
    public void transactionTest() {
        GroceryItem item_01 = GroceryItem.builder()
                .id(UUID.randomUUID().toString())
                .category("test_cate_01")
                .name("test_name_01")
                .quantity(100)
                .build();
        GroceryItem item_02 = GroceryItem.builder()
                .id(UUID.randomUUID().toString())
                .category("test_cate_02")
                .name("test_name_02")
                .quantity(200)
                .build();

        itemRepository.insert(item_01);
        itemRepository.insert(item_02);

        GroceryItem resItem = itemRepository.findItemByName("test_name_02");

        assertThat(resItem).isNotNull();
        assertThat(resItem.getCategory()).isEqualTo("test_cate_02");
        assertThat(resItem.getName()).isEqualTo("test_name_02");
        assertThat(resItem.getQuantity()).isEqualTo(200);
    }
}
