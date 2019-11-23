package com.cpen321.ubconnect.model.data;

import java.util.List;

public class SearchItem<T> {
    List<T> searchItem;
    String type;

    public List<T> getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(List<T> searchItem) {
        this.searchItem = searchItem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
