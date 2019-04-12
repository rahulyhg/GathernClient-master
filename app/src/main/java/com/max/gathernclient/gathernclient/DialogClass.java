package com.max.gathernclient.gathernclient;


public class DialogClass {
    private String itemName , itemMask;
    private boolean imageChecked;

    public DialogClass(String itemName,String itemMask, boolean imageChecked) {
        this.itemName = itemName;
        this.imageChecked = imageChecked;
        this.itemMask = itemMask ;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isImageChecked() {
        return imageChecked;
    }

    public void setImageChecked(boolean imageChecked) {
        this.imageChecked = imageChecked;
    }

    public String getItemMask() {
        return itemMask;
    }

    public void setItemMask(String itemMask) {
        this.itemMask = itemMask;
    }
}