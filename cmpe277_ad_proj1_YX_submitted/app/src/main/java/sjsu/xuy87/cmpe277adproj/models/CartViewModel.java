package sjsu.xuy87.cmpe277adproj.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sjsu.xuy87.cmpe277adproj.BR;
import sjsu.xuy87.cmpe277adproj.util.BigDecimalUtil;
import sjsu.xuy87.cmpe277adproj.util.Prices;

/**
 * Created by Yu Xu
 */

public class CartViewModel extends BaseObservable {

    private static final String TAG = "CartViewModel";

    private List<CartItem> cart = new ArrayList<>();
    private boolean isCartVisible;


    @Bindable
    public boolean isCartVisible() {
        return isCartVisible;
    }

    @Bindable
    public List<CartItem> getCart() {
        return cart;
    }

    public void setCartVisible(boolean cartVisible) {
        isCartVisible = cartVisible;
        notifyPropertyChanged(BR.cartVisible);
    }

    public void setCart(List<CartItem> cart) {
        Log.d(TAG, "setCart: updating cart.");
        this.cart = cart;
        notifyPropertyChanged(BR.cart);
    }

    public String getProductQuantitiesString(){
        int totalItems = 0;
        for(CartItem cartItem : cart){
            totalItems += cartItem.getQuantity();
        }

        String s = "";
        if(totalItems > 1){
            s = "items";
        }
        else{
            s = "item";
        }
        return ("( " + String.valueOf(totalItems) + " " + s + "): ");
    }

    public String getTotalCostString(){
        double totalCost = 0;
        for(CartItem cartItem : cart){
            int productQuantity = cartItem.getQuantity();
            double cost;
            if (cartItem.getProduct().getSale_price().compareTo(BigDecimal.ZERO) == 0)
                cost = productQuantity * cartItem.getProduct().getPrice().doubleValue();
            else
                cost = productQuantity * cartItem.getProduct().getSale_price().doubleValue();

            totalCost += cost;
        }

        return "$" + BigDecimalUtil.getValue(new BigDecimal(totalCost));
    }

}



























