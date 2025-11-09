package com.financial_tracker.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
    private String name;
    private Map<String, SubCategory> subCategories = new HashMap<>();


    public Category(String name) {
        this.name = name;
        this.checkAddGeneralSubCategoryIfNotExist();
    }

    public Category(String name, SubCategory @NotNull [] subCategories) {
        this.name = name;
        for (SubCategory subCategory : subCategories) {
            this.subCategories.put(subCategory.getName(), subCategory);

        }
        this.checkAddGeneralSubCategoryIfNotExist();
    }

    private void checkAddGeneralSubCategoryIfNotExist() {
        if (this.subCategories.get("Default") == null) {
            this.addSubCategory("Default");
        }
    }

    public SubCategory addSubCategory(SubCategory subCategory) {
      return  this.subCategories.put(subCategory.getName(), subCategory);
    }

    public void addSubCategory(String name) {
        SubCategory newSubCategory = new SubCategory(name, this);
        this.addSubCategory(newSubCategory);
    }

    @Nullable
    public SubCategory removeSubCategory(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cant be empty");
        }
        return  this.subCategories.remove(name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<SubCategory> getSubCategories() {
        return new ArrayList<SubCategory>(subCategories.values());

    }
}
