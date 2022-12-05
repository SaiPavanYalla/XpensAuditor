package com.xa.xpensauditor;

public class Transaction {
    private String tid, shopname, amountStr, category, dateStr, message;
    private int dateInt;

    public Transaction() {
    }

    public static int getDateInt(String year, String month, String date) {
        month = Integer.parseInt(month) < 10 ? "0" + month : month;
        int dateInt = Integer.parseInt(year + month + date);
        return dateInt;
    }

    public Transaction(Transaction obj) {
        this.tid = obj.tid;
        this.amountStr = obj.amountStr;
        this.shopname = obj.shopname;
        this.category = obj.category;
        this.dateStr = obj.dateStr;
        this.message = obj.message;
    }

    public Transaction(String tid, String t_amt, String t_cat, String t_shopname, String t_date, String t_msg, int dateInt) {
        this.tid = tid;
        this.amountStr = t_amt;
        this.category = t_cat;
        this.shopname = t_shopname;
        this.dateStr = t_date;
        this.message = t_msg;
        this.dateInt = dateInt;
    }

    public String getTid() {
        return tid;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public String getCategory() {
        return category;
    }

    public String getShopname() {
        return shopname;
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getMessage() {
        return message;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDateInt() {
        return this.dateInt;
    }
}
