package uz.opensale.shakh.models;

import java.util.Date;

/**
 * Created by shakh on 02.03.18.
 */

public class Cards {
    private int id;
    private int bank_id;
    private String bank_name;
    private int cash;
    private String number;
    private String exp_date;
    private String phone;
    private String name;
    private String key;

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public Cards(int id, int bank_id, String bank_name, String number, String exp_date, String phone, String name, String key, int cash){
        this.id = id;
        this.bank_id = bank_id;
        this.bank_name = bank_name;
        this.number = number;
        this.exp_date = exp_date;
        this.phone = phone;
        this.name = name;
        this.key = key;
        this.cash = cash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBank_id() {
        return bank_id;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
