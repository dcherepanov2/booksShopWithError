package com.example.MyBookShopApp.data.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum ContactType {
    PHONE(1),
    EMAIL(2);

    private final int code;

    public int getCode() {
        return code;
    }

    public Set<ContactType> getByCode(int code){
        ContactType[] contactTypes = ContactType.values();
        Set<ContactType> contactTypeList = new HashSet<>();
        for(ContactType contactType:contactTypes){
            if(contactType.getCode()==code){
                contactTypeList.add(contactType);
            }
        }
        return contactTypeList;
    }

    ContactType(int code) {
        this.code = code;
    }
}
