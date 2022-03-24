package com.example.mongodb.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("groceryitems")
public class GroceryItem {
    @Id
    private String id;

    private String name;
    private int quantity;
    private String category;
    @Version
    private Long version;

    @Builder
    public GroceryItem(String id, String name, int quantity, String category) {
        super();
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }
}
