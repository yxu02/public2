package sjsu.xuy87.cmpe277adproj;

import sjsu.xuy87.cmpe277adproj.models.CartItem;
import sjsu.xuy87.cmpe277adproj.models.Product;

/**
 * Created by Yu Xu
 */

public interface IMainActivity {

    void inflateViewProductFragment(Product product);

    void showQuantityDialog();

    void setQuantity(int quantity);

    void addToCart(Product product, int quantity);

    void inflateViewCartFragment();

    void setCartVisibility(boolean visibility);

    void updateQuantity(Product product, int quantity);

    void removeCartItem(CartItem cartItem);

}
