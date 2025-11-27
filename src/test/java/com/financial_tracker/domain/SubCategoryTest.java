package com.financial_tracker.domain;

import com.financial_tracker.category_management.dto.Category;
import com.financial_tracker.category_management.SubCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SubCategoryTest {
    private Category parentCategory;
    private static final String VALID_NAME = "Test SubCategory";

    @BeforeEach
    void setUp() {
        parentCategory = new Category("Parent Category");
    }

    @Nested
    @DisplayName("Create subcategory tests")
    class CreateSubCategoryTests {

        @Test
        @DisplayName("✅ Should create subcategory with valid name and parent")
        void shouldCreateSubCategory_WithValidNameAndParent() {
            SubCategory subCategory = new SubCategory(VALID_NAME, parentCategory);

            assertEquals(VALID_NAME, subCategory.getName());
            assertEquals(parentCategory, subCategory.getCategory());
        }

        @Test
        @DisplayName("✅ Should create subcategory with different names")
        void shouldCreateSubCategory_WithDifferentNames() {
            SubCategory sub1 = new SubCategory("Fruits", parentCategory);
            SubCategory sub2 = new SubCategory("Vegetables", parentCategory);

            assertEquals("Fruits", sub1.getName());
            assertEquals("Vegetables", sub2.getName());
            assertEquals(parentCategory, sub1.getCategory());
            assertEquals(parentCategory, sub2.getCategory());
        }

        @Test
        @DisplayName("✅ Should allow same name for different parent categories")
        void shouldAllowSameName_ForDifferentParentCategories() {
            Category anotherParent = new Category("Another Parent");

            SubCategory sub1 = new SubCategory("Food", parentCategory);
            SubCategory sub2 = new SubCategory("Food", anotherParent);

            assertEquals("Food", sub1.getName());
            assertEquals("Food", sub2.getName());
            assertEquals(parentCategory, sub1.getCategory());
            assertEquals(anotherParent, sub2.getCategory());
        }
    }

    @Nested
    @DisplayName("Subcategory properties tests")
    class SubCategoryPropertiesTests {

        @Test
        @DisplayName("✅ Should get and set name")
        void shouldGetAndSetName() {
            SubCategory subCategory = new SubCategory(VALID_NAME, parentCategory);
            String newName = "New SubCategory Name";

            subCategory.setName(newName);

            assertEquals(newName, subCategory.getName());
            assertEquals(parentCategory, subCategory.getCategory());
        }

        @Test
        @DisplayName("✅ Should get parent category")
        void shouldGetParentCategory() {
            SubCategory subCategory = new SubCategory(VALID_NAME, parentCategory);

            Category retrievedParent = subCategory.getCategory();

            assertEquals(parentCategory, retrievedParent);
            assertEquals("Parent Category", retrievedParent.getName());
        }

        @Test
        @DisplayName("✅ Parent category reference should be consistent")
        void parentCategoryReference_ShouldBeConsistent() {
            SubCategory subCategory = new SubCategory(VALID_NAME, parentCategory);


            subCategory.setName("Changed Name");

            assertEquals(parentCategory, subCategory.getCategory());
            assertEquals("Parent Category", subCategory.getCategory().getName());
        }
    }

    @Nested
    @DisplayName("Comparator tests")
    class ComparatorTests {

        @Test
        @DisplayName("✅ Should compare subcategories by name alphabetically")
        void shouldCompareSubCategories_ByNameAlphabetically() {
            SubCategory apples = new SubCategory("Apples", parentCategory);
            SubCategory bananas = new SubCategory("Bananas", parentCategory);
            SubCategory cherries = new SubCategory("Cherries", parentCategory);

            SubCategory comparator = new SubCategory("Comparator", parentCategory);

            assertTrue(comparator.compare(apples, bananas) < 0);

            assertTrue(comparator.compare(bananas, apples) > 0);

            assertEquals(0, comparator.compare(apples, apples));
        }

        @ParameterizedTest
        @DisplayName("✅ Should compare subcategories with different cases")
        @CsvSource({
                "apples, Apples, 32",    // 'a' - 'A' = 32
                "Apples, apples, -32",   // 'A' - 'a' = -32
                "APPLES, apples, -32"    // 'A' - 'a' = -32
        })
        void shouldCompareSubCategories_WithDifferentCases(String name1, String name2, int expected) {
            SubCategory sub1 = new SubCategory(name1, parentCategory);
            SubCategory sub2 = new SubCategory(name2, parentCategory);
            SubCategory comparator = new SubCategory("Test", parentCategory);

            assertEquals(expected, comparator.compare(sub1, sub2));
        }

        @Test
        @DisplayName("✅ Should handle empty names in comparison")
        void shouldHandleEmptyNames_InComparison() {
            SubCategory empty = new SubCategory("", parentCategory);
            SubCategory nonEmpty = new SubCategory("Food", parentCategory);
            SubCategory comparator = new SubCategory("Test", parentCategory);

            // "" < "Food"
            assertTrue(comparator.compare(empty, nonEmpty) < 0);
            // "Food" > ""
            assertTrue(comparator.compare(nonEmpty, empty) > 0);
        }

        @Test
        @DisplayName("✅ Should ignore parent category in comparison")
        void shouldIgnoreParentCategory_InComparison() {
            Category anotherParent = new Category("Another Parent");

            SubCategory sub1 = new SubCategory("Food", parentCategory);
            SubCategory sub2 = new SubCategory("Food", anotherParent);
            SubCategory comparator = new SubCategory("Test", parentCategory);

            assertEquals(0, comparator.compare(sub1, sub2));
        }
    }

    @Nested
    @DisplayName("Edge cases and boundary tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("✅ Should handle very long names")
        void shouldHandleVeryLongNames() {
            String longName = "A".repeat(1000);
            SubCategory subCategory = new SubCategory(longName, parentCategory);

            assertEquals(longName, subCategory.getName());
            assertEquals(parentCategory, subCategory.getCategory());
        }

        @Test
        @DisplayName("✅ Should handle special characters in names")
        void shouldHandleSpecialCharacters_InNames() {
            SubCategory sub1 = new SubCategory("Food & Drinks", parentCategory);
            SubCategory sub2 = new SubCategory("Café", parentCategory);
            SubCategory sub3 = new SubCategory("💰 Expenses", parentCategory);

            assertEquals("Food & Drinks", sub1.getName());
            assertEquals("Café", sub2.getName());
            assertEquals("💰 Expenses", sub3.getName());
        }

        @Test
        @DisplayName("✅ Comparator should work with same instance")
        void comparator_ShouldWorkWithSameInstance() {
            SubCategory subCategory = new SubCategory("Test", parentCategory);

            assertEquals(0, subCategory.compare(subCategory, subCategory));
        }
    }

    @Nested
    @DisplayName("Null safety tests")
    class NullSafetyTests {

        @Test
        @DisplayName("❌ Should handle null in comparator")
        void shouldHandleNull_InComparator() {
            SubCategory subCategory = new SubCategory("Test", parentCategory);
            SubCategory nullSubCategory = null;


            assertThrows(IllegalArgumentException.class,
                    () -> subCategory.compare(subCategory, nullSubCategory));

            assertThrows(IllegalArgumentException.class,
                    () -> subCategory.compare(nullSubCategory, subCategory));
        }
    }

    @Nested
    @DisplayName("Integration with Category tests")
    class IntegrationTests {

        @Test
        @DisplayName("✅ Should maintain bidirectional relationship with parent")
        void shouldMaintainBidirectionalRelationship_WithParent() {
            SubCategory subCategory = new SubCategory("Fruits", parentCategory);


            assertEquals(parentCategory, subCategory.getCategory());

            parentCategory.addSubCategory(subCategory);
            assertTrue(parentCategory.getSubCategories().contains(subCategory));
        }

        @Test
        @DisplayName("✅ Should work in Category's subcategories collection")
        void shouldWorkInCategorySubCategoriesCollection() {
            SubCategory fruits = new SubCategory("Fruits", parentCategory);
            SubCategory vegetables = new SubCategory("Vegetables", parentCategory);
            SubCategory dairy = new SubCategory("Dairy", parentCategory);

            parentCategory.addSubCategory(vegetables);
            parentCategory.addSubCategory(dairy);
            parentCategory.addSubCategory(fruits);


            assertEquals(4, parentCategory.getSubCategories().size());
            assertTrue(parentCategory.getSubCategories().contains(fruits));
        }
    }
}