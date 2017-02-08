package com.ae.apps.tripmeter.managers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.ae.apps.common.managers.ContactManager;
import com.ae.apps.common.vo.ContactVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper over ContactManager
 */
public class ExpenseContactManager extends ContactManager {

    private static ExpenseContactManager instance;

    /**
     * Return a reference to the ExpenseContactManager implementation
     *
     * @param contentResolver
     * @return
     */
    public static ExpenseContactManager newInstance(ContentResolver contentResolver){
        if(null == instance){
            instance = new ExpenseContactManager(contentResolver);
        }
        return instance;
    }

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

    /**
     * Finds and returns the default contact (current user)
     *
     * Owner details are stored in ContactsContract.Profile for ICS and up
     *
     * @return
     */
    public ContactVo getDefaultContact(){
        ContactVo contactVo = new ContactVo();

        boolean userFound = false;
        Cursor cursor = contentResolver.query( ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst()){
            contactVo.setName(cursor.getString(cursor.getColumnIndex( ContactsContract.Profile.DISPLAY_NAME)));
            contactVo.setId(cursor.getString(cursor.getColumnIndex( ContactsContract.Profile._ID)));
            userFound = true;
        }
        cursor.close();

        if(!userFound){
            contactVo.setName("You");
            contactVo.setId("0");
        }

        return contactVo;
    }
}
