package com.example.mongodb.repository;

import com.example.mongodb.model.GroceryItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
