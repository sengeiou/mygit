package com.beanu.l3_shoppingcart.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.beanu.l3_shoppingcart.R;
import com.beanu.l3_shoppingcart.model.bean.AddressItem;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author lizhi
 * @date 2017/11/1.
 */

public class AddressViewBinder extends ItemViewBinder<AddressItem, AddressViewBinder.ItemViewHolder> {

    public AddressViewBinder(OnAddressListener addressListener) {
        this.mAddressListener = addressListener;
    }

    private OnAddressListener mAddressListener;

    public interface OnAddressListener {
        void deleteAddress(int position);

        void editAddress(int position);

        void chooseAddress(int position);

        void defaultAddress(int position);
    }

    @NonNull
    @Override
    protected ItemViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.cart_address_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, @NonNull AddressItem item) {
        final int position = holder.getAdapterPosition();
        holder.bind(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddressListener.chooseAddress(position);
            }
        });

        holder.llDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddressListener.deleteAddress(position);
            }
        });

        holder.llEditorAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddressListener.editAddress(position);
            }
        });

        holder.cbDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddressListener.defaultAddress(position);
            }
        });
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbDefault;
        TextView tvName;
        TextView tvPhoneNumber;
        TextView tvAddress;

        TextView llEditorAddress;
        TextView llDeleteAddress;

        ItemViewHolder(View view) {
            super(view);
            cbDefault = view.findViewById(R.id.cbDefault);
            tvName = view.findViewById(R.id.tvName);
            tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
            tvAddress = view.findViewById(R.id.tvAddress);
            llEditorAddress = view.findViewById(R.id.llEditorAddress);
            llDeleteAddress = view.findViewById(R.id.llDeleteAddress);
        }

        private void bind(AddressItem item) {

            tvName.setText(item.getLinkName());
            tvPhoneNumber.setText(item.getLinkPhone());
            tvAddress.setText(item.getProvinceName() + item.getCityName() + item.getCountyName() + item.getAddress());

//            cbDefault.setChecked(item.getIs_default() == 1);
        }
    }
}
