package com.financial_tracker.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private Category category;
    private static final String VALID_NAME = "Test Category";

    @BeforeEach
    void setUp() {
        category = new Category(VALID_NAME);
    }

    @Nested
    @DisplayName("Create category tests")
    class CreateCategoryTests {

        @Test
        @DisplayName("✅ Should create category with valid name")
        void shouldCreateCategory_WhenNameIsValid() {
            Category category = new Category("Food");
            assertEquals("Food", category.getName());
            assertFalse(category.getSubCategories().isEmpty());
        }

        @Test
        @DisplayName("✅ Should create category with subcategories array")
        void shouldCreateCategory_WithSubCategoriesArray() {
            SubCategory[] subCategories = {
                    new SubCategory("Sub1", new Category("Parent")),
                    new SubCategory("Sub2", new Category("Parent"))
            };

            Category category = new Category("Food", subCategories);

            assertEquals("Food", category.getName());
            assertEquals(3, category.getSubCategories().size());
            assertNotNull(category.getSubCategories().get(0));
        }

        @Test
        @DisplayName("✅ Should automatically add Default subcategory")
        void shouldAutomaticallyAddDefaultSubCategory() {
            Category category = new Category("Test");
            List<SubCategory> subCategories = category.getSubCategories();

            assertEquals(1, subCategories.size());
            assertEquals("Default", subCategories.get(0).getName());
        }
    }

    @Nested
    @DisplayName("Add subcategory tests")
    class AddSubCategoryTests {

        @Test
        @DisplayName("✅ Should add subcategory with SubCategory object")
        void shouldAddSubCategory_WithObject() {
            SubCategory subCategory = new SubCategory("Fruits", category);

            SubCategory result = category.addSubCategory(subCategory);

            assertTrue(result instanceof SubCategory);
            assertEquals(2, category.getSubCategories().size());
            assertTrue(category.getSubCategories().stream()
                    .anyMatch(sub -> sub.getName().equals("Fruits")));
        }

        @Test
        @DisplayName("✅ Should add subcategory with name")
        void shouldAddSubCategory_WithName() {
            category.addSubCategory("Vegetables");

            assertEquals(2, category.getSubCategories().size());
            assertTrue(category.getSubCategories().stream()
                    .anyMatch(sub -> sub.getName().equals("Vegetables")));
        }

        @Test
        @DisplayName("✅ Should replace existing subcategory when adding duplicate")
        void shouldReplaceSubCategory_WhenAddingDuplicate() {
            SubCategory original = new SubCategory("Fruits", category);
            SubCategory replacement = new SubCategory("Fruits", category);

            category.addSubCategory(original);
            SubCategory newSubCategory = category.addSubCategory(replacement);

            assertNotEquals(original, newSubCategory);
            assertEquals(2, category.getSubCategories().size());
        }
    }

    @Nested
    @DisplayName("Remove subcategory tests")
    class RemoveSubCategoryTests {

        @Test
        @DisplayName("✅ Should remove existing subcategory")
        void shouldRemoveExistingSubCategory() {

            SubCategory subCategory = new SubCategory("Fruits", category);
            category.addSubCategory(subCategory);


            SubCategory removed = category.removeSubCategory("Fruits");


            assertEquals(subCategory, removed);
            assertEquals(1, category.getSubCategories().size());
            assertEquals("Default", category.getSubCategories().get(0).getName());
        }

        @Test
        @DisplayName("✅ Should return null when removing non-existent subcategory")
        void shouldReturnNull_WhenRemovingNonExistentSubCategory() {
            SubCategory result = category.removeSubCategory("Unknown");
            assertNull(result);
        }

        @Test
        @DisplayName("❌ Should throw when removing with empty name")
        void shouldThrow_WhenRemovingWithEmptyName() {
            assertThrows(IllegalArgumentException.class,
                    () -> category.removeSubCategory(""));
        }

        @Test
        @DisplayName("❌ Should throw when removing with whitespace name")
        void shouldThrow_WhenRemovingWithWhitespaceName() {
            assertThrows(IllegalArgumentException.class,
                    () -> category.removeSubCategory("   "));
        }

        @Test
        @DisplayName("❌ Should throw when removing with null name")
        void shouldThrow_WhenRemovingWithNullName() {
            assertThrows(IllegalArgumentException.class,
                    () -> category.removeSubCategory(null));
        }

        @Test
        @DisplayName("✅ Should not remove Default subcategory")
        void shouldNotRemoveDefaultSubCategory() {
            category.removeSubCategory("Unknown");
            assertEquals(1, category.getSubCategories().size());
            assertEquals("Default", category.getSubCategories().get(0).getName());
        }
    }

    @Nested
    @DisplayName("Category properties tests")
    class CategoryPropertiesTests {

        @Test
        @DisplayName("✅ Should get and set name")
        void shouldGetAndSetName() {
            String newName = "New Category Name";
            category.setName(newName);
            assertEquals(newName, category.getName());
        }

        @Test
        @DisplayName("✅ Should return copy of subcategories list")
        void shouldReturnCopyOfSubCategories_ProtectingInternalState() {

            category.addSubCategory("Fruits");
            category.addSubCategory("Vegetables");


            List<SubCategory> subCategories = category.getSubCategories();
            subCategories.clear();

            assertEquals(3, category.getSubCategories().size());
        }

        @Test
        @DisplayName("✅ Should maintain subcategory uniqueness by name")
        void shouldMaintainSubCategoryUniquenessByName() {
            SubCategory sub1 = new SubCategory("Fruits", category);
            SubCategory sub2 = new SubCategory("Fruits", category);

            category.addSubCategory(sub1);
            SubCategory previous = category.addSubCategory(sub2);

            assertEquals(sub1.getName(), previous.getName());
            assertEquals(2, category.getSubCategories().size());
        }
    }

    @Nested
    @DisplayName("Edge cases and business rules")
    class EdgeCasesTests {

        @Test
        @DisplayName("✅ Should handle multiple subcategories")
        void shouldHandleMultipleSubCategories() {
            category.addSubCategory("Fruits");
            category.addSubCategory("Vegetables");
            category.addSubCategory("Dairy");

            assertEquals(4, category.getSubCategories().size());
            assertTrue(category.getSubCategories().stream()
                    .map(SubCategory::getName)
                    .allMatch(name -> List.of("Default", "Fruits", "Vegetables", "Dairy").contains(name)));
        }

        @Test
        @DisplayName("✅ Default subcategory should have correct parent reference")
        void defaultSubCategory_ShouldHaveCorrectParentReference() {
            List<SubCategory> subCategories = category.getSubCategories();
            SubCategory defaultSub = subCategories.get(0);

            assertEquals("Default", defaultSub.getName());
            assertEquals(category, defaultSub.getCategory());
        }

        @Test
        @DisplayName("✅ Should not duplicate Default subcategory when creating with array")
        void shouldNotDuplicateDefault_WhenCreatingWithArray() {
            SubCategory[] subCategories = {
                    new SubCategory("Sub1", new Category("Parent")),
                    new SubCategory("Default", new Category("Parent"))
            };

            Category category = new Category("Test", subCategories);


            long defaultCount = category.getSubCategories().stream()
                    .filter(sub -> sub.getName().equals("Default"))
                    .count();
            assertEquals(1, defaultCount);
        }
    }

    @Nested
    @DisplayName("Null safety tests")
    class NullSafetyTests {

        @Test
        @DisplayName("❌ Should handle null in constructor array")
        void shouldHandleNullInConstructorArray() {
            SubCategory[] subCategories = {
                    new SubCategory("Valid", new Category("Parent")),
                    null
            };

            assertThrows(NullPointerException.class,
                    () -> new Category("Test", subCategories));
        }
    }
}