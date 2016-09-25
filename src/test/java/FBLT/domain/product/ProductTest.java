package FBLT.domain.product;


import FBLT.domain.product.category.Category;
import FBLT.domain.product.category.ICategory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lukekramer on 20/09/2016.
 */
public class ProductTest {

    @Test
    public void testCategoryCreation(){

        ICategory category = new Category.Builder()
                .categoryName("Kids Toys")
                .categoryDescription("Short Description")
                .build();

        IProduct productTest = new Product.Builder()
                .id((long) 1)
                .productDescription("Cattle")
                .category((Category) category)
                .build();

        Assert.assertNotNull(productTest);
        Assert.assertEquals("Cattle",productTest.getProductDescription());
        Assert.assertSame((long)1,productTest.getId());
        Assert.assertNotNull(productTest.getCategory().getCategoryName());



    }
}
