package com.financial_tracker.category_management;

import com.financial_tracker.category_management.dto.Category;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class SubCategory implements Comparator<SubCategory> {
    private String name;
    private Category category;


    public SubCategory(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int compare(@NotNull SubCategory o1, @NotNull SubCategory o2) {
        return o1.name.compareTo(o2.name);

    }

}
