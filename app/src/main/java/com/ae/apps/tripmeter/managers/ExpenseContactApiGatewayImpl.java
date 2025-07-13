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

import android.content.Context;
import android.text.TextUtils;

import com.ae.apps.lib.api.contacts.ContactsApiGateway;
import com.ae.apps.lib.api.contacts.impl.AbstractContactsApiGateway;
import com.ae.apps.lib.api.contacts.impl.ContactsApiGatewayImpl;
import com.ae.apps.lib.api.contacts.types.ContactInfoFilterOptions;
import com.ae.apps.lib.api.contacts.types.ContactInfoOptions;
import com.ae.apps.lib.common.models.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper over ContactManager
 */
class ExpenseContactApiGatewayImpl extends AbstractContactsApiGateway {

    private static ExpenseContactApiGatewayImpl apiInstance;

    private final ContactsApiGateway contactsApi;

    /**
     * Return a reference to the ExpenseContactApiGatewayImpl implementation
     *
     * @param context content resolver
     * @return instance of {@link ExpenseContactApiGatewayImpl}
     */
    public static ExpenseContactApiGatewayImpl newInstance(final Context context) {
        if (null == apiInstance) {
            ContactsApiGateway api = new ContactsApiGatewayImpl.Builder(context).build();
            apiInstance = new ExpenseContactApiGatewayImpl(api);
        }
        return apiInstance;
    }

    private ExpenseContactApiGatewayImpl(ContactsApiGateway contactsApi) {
        super();
        this.contactsApi = contactsApi;
    }

    @Override
    protected void readContacts(ContactInfoFilterOptions contactInfoFilterOptions) {
        contactsApi.initialize(contactInfoFilterOptions);
    }


    @Override
    public ContactInfo getRandomContact() throws IllegalStateException {
        return contactsApi.getRandomContact();
    }

    @Override
    public ContactInfo getContactInfo(String id) {
        return contactsApi.getContactInfo(id);
    }

    @Override
    public ContactInfo getContactInfo(String id, ContactInfoOptions contactInfoOptions) {
        return contactsApi.getContactInfo(id, contactInfoOptions);
    }

    @Override
    public String getContactIdFromRawContact(String s) {
        return contactsApi.getContactIdFromRawContact(s);
    }

    @Override
    public String getContactIdFromAddress(String s) {
        return contactsApi.getContactIdFromAddress(s);
    }

    public List<ContactInfo> getContactsFromIds(String contactIds) {
        List<ContactInfo> list = new ArrayList<>();
        if (null != contactIds && !TextUtils.isEmpty(contactIds)) {
            String[] ids = contactIds.split(",");
            for(String contactId: ids){
                list.add(getContactInfo(contactId));
            }
        }
        return list;
    }
}
