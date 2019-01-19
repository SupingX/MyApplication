package com.laputa.zeejp.lib_common.http.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 产品型号下的列表
 */
public class ProductResponse implements Parcelable {
    private String productType;
    private String productKey;
    private String productName;


    public ProductResponse() {
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    protected ProductResponse(Parcel in) {
        productType = in.readString();
        productKey = in.readString();
        productName = in.readString();
    }

    public static final Creator<ProductResponse> CREATOR = new Creator<ProductResponse>() {
        @Override
        public ProductResponse createFromParcel(Parcel in) {
            return new ProductResponse(in);
        }

        @Override
        public ProductResponse[] newArray(int size) {
            return new ProductResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productType);
        dest.writeString(productKey);
        dest.writeString(productName);
    }
}
