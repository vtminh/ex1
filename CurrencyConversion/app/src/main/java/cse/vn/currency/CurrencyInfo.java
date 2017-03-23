package cse.vn.currency;

/**
 * Created by MINH on 3/23/2017.
 */

public class CurrencyInfo{
    public String name;
    public String code;
    public float buy;
    public float sell;
    public float transfer;


    public CurrencyInfo(String code, String name, float buy, float sell, float transfer){
        this.code = code;
        this.name =name;
        this.buy = buy;
        this.sell = sell;
        this.transfer = transfer;
    }


}