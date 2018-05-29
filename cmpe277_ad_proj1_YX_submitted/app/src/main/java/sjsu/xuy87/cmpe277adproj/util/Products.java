package sjsu.xuy87.cmpe277adproj.util;

import java.math.BigDecimal;
import java.util.HashMap;

import sjsu.xuy87.cmpe277adproj.R;
import sjsu.xuy87.cmpe277adproj.models.Product;

/**
 * Created by Yu Xu
 */

public class Products {

    public Product[] PRODUCTS = {
            Girl_Skate1,
            Girl_Skate2,
            Girl_Skate3,
            Girl_Skate4,
            Girl_Skate5,
            Boy_Skate1,
            Boy_Skate2,
            Boy_Skate3,
            Boy_Skate4,
            Boy_Skate5,
            Women_Skate1,
            Women_Skate2,
            Women_Skate3,
            Women_Skate4,
            Women_Skate5,
    };


    public HashMap<String, Product> PRODUCT_MAP = new HashMap<>();

    public Products() {
        for(Product product : PRODUCTS){
            PRODUCT_MAP.put(String.valueOf(product.getSerial_number()), product);
        }

    }

    public static final Product Girl_Skate1 = new Product("Girl Skate #1", "Lake Placid Summit Girls Adjustable Ice Skate.",
            R.drawable.girl_skate1, new BigDecimal(55.99), new BigDecimal(49.50), 161,
            new BigDecimal(4.5), 1515611);

    public static final Product Girl_Skate2 = new Product("Girl Skate #2", "American Athletic Shoe Girl's Tricot Lined Ice Skates.",
            R.drawable.girl_skate2, new BigDecimal(75.99), new BigDecimal(0), 6,
            new BigDecimal(5), 7725277);

    public static final Product Girl_Skate3 = new Product("Girl Skate #3", "Lake Placid Girls Nitro 8.8 Adjustable Figure Ice Skate.",
            R.drawable.girl_skate3, new BigDecimal(95.99), new BigDecimal(0), 66, new BigDecimal(3.5), 2141515);

    public static final Product Girl_Skate4 = new Product("Girl Skate #4", "Jackson Ultima Girls Ice Skates JS1291 with Mark II Blade.",
            R.drawable.girl_skate4, new BigDecimal(126.99), new BigDecimal(109.99), 7, new BigDecimal(5), 9704833);

    public static final Product Girl_Skate5 = new Product("Girl Skate #5", "American Athletic Shoe Girl's Party Adjustable Figure Skates.",
            R.drawable.girl_skate5, new BigDecimal(48.99), new BigDecimal(0), 157, new BigDecimal(4.5), 9377376);

    public static final Product Boy_Skate1 = new Product("Boy Skate #1", "Lake Placid Boys Nitro 8.8 Adjustable Figure Ice Skate.",
            R.drawable.boy_skate1, new BigDecimal(60.99), new BigDecimal(0), 121, new BigDecimal(3.5), 6626622);

    public static final Product Boy_Skate2 = new Product("Boy Skate #2", "Riedell 615 Soar Boys Beginner Soft Figure Ice Skates.",
            R.drawable.boy_skate2, new BigDecimal(72.99), new BigDecimal(0), 67, new BigDecimal(4.5), 7837367);

    public static final Product Boy_Skate3 = new Product("Boy Skate #3", "Lake Placid Monarch Boys Adjustable Ice Skate.",
            R.drawable.boy_skate3, new BigDecimal(88.99), new BigDecimal(75.99), 88, new BigDecimal(2.5), 7695085);

    public static final Product Boy_Skate4 = new Product("Boy Skate #4", "American Athletic Shoe Boy's Ice Force Hockey Skates.",
            R.drawable.boy_skate4, new BigDecimal(93.99), new BigDecimal(0), 23, new BigDecimal(4), 9084728);

    public static final Product Boy_Skate5 = new Product("Boy Skate #5", "Lake Placid Cascade Boys Figure Ice Skate.",
            R.drawable.boy_skate5, new BigDecimal(45.99), new BigDecimal(0), 98, new BigDecimal(3.5)
            , 7265405);

    public static final Product Women_Skate1 = new Product("Women Skate #1", "Lake Placid Starglide Women Double Runner Figure Ice Skate.",
            R.drawable.women_skate1, new BigDecimal(126.99), new BigDecimal(0), 11, new BigDecimal(3)
            , 9575721);

    public static final Product Women_Skate2 = new Product("Women Skate #2", "Jackson Ultima Softec Classic Women ST2321 Kids Ice Skates.",
            R.drawable.women_skate2, new BigDecimal(125.99), new BigDecimal(0), 51, new BigDecimal(4.5)
            , 5776336);

    public static final Product Women_Skate3 = new Product("Women Skate #3", "American Athletic Shoe Women Soft Boot Ice Skates.",
            R.drawable.women_skate3, new BigDecimal(156.99), new BigDecimal(124.99), 616, new BigDecimal(5)
            , 1408483);

    public static final Product Women_Skate4 = new Product("Women Skate #4", "Riedell 10 Opal Beginnner Women Figure Ice Skates.",
            R.drawable.women_skate4, new BigDecimal(200), new BigDecimal(159.99), 37, new BigDecimal(4.5), 8830303);

    public static final Product Women_Skate5 = new Product("Women Skate #5", "PlayWheels Disney Frozen Women Convertible Ice Skates.",
            R.drawable.women_skate5, new BigDecimal(179.99), new BigDecimal(0), 3, new BigDecimal(4), 9082727);
}
















