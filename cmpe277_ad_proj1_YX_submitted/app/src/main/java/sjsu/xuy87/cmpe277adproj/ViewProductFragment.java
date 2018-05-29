package sjsu.xuy87.cmpe277adproj;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sjsu.xuy87.cmpe277adproj.databinding.FragmentViewProductBinding;
import sjsu.xuy87.cmpe277adproj.models.Product;
import sjsu.xuy87.cmpe277adproj.models.ProductViewModel;

/**
 * Created by Yu Xu
 */

public class ViewProductFragment extends Fragment{

    private static final String TAG = "ViewProductFragment";

    // Data binding
    FragmentViewProductBinding mBinding;

    //vars
    private Product mProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mProduct = bundle.getParcelable(getString(R.string.intent_product));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewProductBinding.inflate(inflater);
        mBinding.setIMainActivity((IMainActivity)getActivity());

        ProductViewModel viewProductModel = new ProductViewModel();
        viewProductModel.setProduct(mProduct);
        viewProductModel.setQuantity(1);

        mBinding.setProductView(viewProductModel);

        return mBinding.getRoot();
    }

}














