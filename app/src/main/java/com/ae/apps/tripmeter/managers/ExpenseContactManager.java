package com.ae.apps.tripmeter.managers;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.ae.apps.common.managers.ContactManager;
import com.ae.apps.common.vo.ContactVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper over ContactManager
 */
public class ExpenseContactManager extends ContactManager {
    public ExpenseContactManager(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public List<ContactVo> getAllContacts() {
        return super.getAllContacts();
    }

    /**
     * Returns a list of ContactVos from contactIds
     * @param memberIds comma separated member ids
     * @return
     */
    public List<ContactVo> getContactsFromIds(String memberIds) {
        List<ContactVo> contacts = new ArrayList<>();
        if(null != memberIds && !TextUtils.isEmpty(memberIds)){
            ContactVo contactVo;
            String [] contactIds = memberIds.split(",");
            for(String contactId : contactIds){
                contactVo = getContactInfo(contactId);
                contacts.add(contactVo);
            }
        }
        return contacts;
    }
}
