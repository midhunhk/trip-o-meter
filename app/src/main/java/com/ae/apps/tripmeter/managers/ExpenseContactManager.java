/*
 * MIT License
 * Copyright (c) 2016 Midhun Harikumar
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.managers;

import android.content.ContentResolver;
import android.content.res.Resources;
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
class ExpenseContactManager extends ContactManager {

    private static ExpenseContactManager instance;

    /**
     * Return a reference to the ExpenseContactManager implementation
     *
     * @param contentResolver content resolver
     * @return instance of {@link ExpenseContactManager}
     */
    public static ExpenseContactManager newInstance(ContentResolver contentResolver, Resources resources) {
        if (null == instance) {
            ContactManager.Builder builder = new ContactManager.Builder(contentResolver, resources);
            builder.addContactsWithPhoneNumbers(false);
            instance = new ExpenseContactManager(builder);
        }
        return instance;
    }

    private ExpenseContactManager(ContactManager.Builder builder) {
        super(builder);
    }

    @Override
    public List<ContactVo> getAllContacts() {
        return super.getAllContacts();
    }

    /**
     * Returns a list of ContactVos from contactIds
     *
     * @param memberIds comma separated member ids
     * @return contactsList
     */
    List<ContactVo> getContactsFromIds(String memberIds) {
        List<ContactVo> contacts = new ArrayList<>();
        if (null != memberIds && !TextUtils.isEmpty(memberIds)) {
            ContactVo contactVo;
            String[] contactIds = memberIds.split(",");
            for (String contactId : contactIds) {
                contactVo = getContactInfo(contactId);
                contacts.add(contactVo);
            }
        }

        return contacts;
    }

    /**
     * Finds and returns the default Profile (current user)
     * <p>
     * Owner details are stored in ContactsContract.Profile for ICS and up
     *
     * @return ContactVO
     */
    ContactVo getDefaultContact() {
        ContactVo contactVo = new ContactVo();

        boolean userFound = false;
        Cursor cursor = contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            contactVo.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME)));
            contactVo.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.Profile._ID)));
            userFound = true;
            cursor.close();
        }

        if (!userFound) {
            contactVo.setName("You");
            contactVo.setId("0");
        }

        return contactVo;
    }
}
