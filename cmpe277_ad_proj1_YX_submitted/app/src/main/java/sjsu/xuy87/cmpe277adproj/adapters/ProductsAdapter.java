package sjsu.xuy87.cmpe277adproj.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sjsu.xuy87.cmpe277adproj.BR;
import sjsu.xuy87.cmpe277adproj.IMainActivity;
import sjsu.xuy87.cmpe277adproj.R;
import sjsu.xuy87.cmpe277adproj.databinding.ProductItemBinding;
import sjsu.xuy87.cmpe277adproj.models.Product;

/**
 * Created by Yu Xu
 */

public class ProductsAdapter extends  RecyclerView.Adapter<ProductsAdapter.BindingViewHolder>{

    private static final String TAG = "ProductsAdapter";

    private List<Product> mProducts = new ArrayList<>();
    private Context mContext;

    public ProductsAdapter(Context context, List<Product> products) {
        mProducts = products;
        mContext = context;
    }

    public void refreshList(List<Product> products){
        mProducts.clear();
        mProducts.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ProductItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext), R.layout.product_item, parent, false);

        return new BindingViewHolder(binding.getRoot());
    }
    @Override
    public void onBindViewHolder(BindingViewHolder holder, final int position) {
        Product product = mProducts.get(position);

        holder.binding.setVariable(BR.iMainActivity, (IMainActivity)mContext);
        holder.binding.setVariable(BR.product, product);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class BindingViewHolder extends RecyclerView.ViewHolder{

        ProductItemBinding binding;

        public BindingViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }



}













