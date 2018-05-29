package sjsu.xuy87.cmpe277adproj.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import sjsu.xuy87.cmpe277adproj.BR;

/**
 * Created by Yu Xu
 */

public class ProductViewModel extends BaseObservable{

    private Product product;
    private int quantity;

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    @Bindable
    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    public void setProduct(Product product) {
        this.product = product;
        notifyPropertyChanged(BR.product);
    }
}













