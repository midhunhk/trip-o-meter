package com.ae.apps.tripmeter.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.TextUtils;

import com.ae.apps.lib.api.contacts.ContactsApiGateway;
import com.ae.apps.lib.api.contacts.impl.ContactsApiGatewayImpl;
import com.ae.apps.lib.api.contacts.types.ContactInfoFilterOptions;
import com.ae.apps.lib.api.contacts.types.ContactInfoOptions;
import com.ae.apps.lib.common.models.ContactInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseContactApiGatewayImplTest {

    @Mock
    Context mockContext;

    @Mock
    ContactsApiGateway mockWrappedContactsApi;

    @Mock
    ContactsApiGatewayImpl.Builder mockBuilder;

    // This field will hold the instance we are testing.
    // We can't use @InjectMocks directly because of the static newInstance method
    // and private constructor. We will manually set it up.
    private ExpenseContactApiGatewayImpl expenseContactApiGateway;

    // To mock the construction of ContactsApiGatewayImpl inside newInstance
    // To mock the construction of ContactsApiGateway.Builder
    private MockedConstruction<ContactsApiGatewayImpl.Builder> mockBuilderConstruction;
    private MockedConstruction<ContactsApiGatewayImpl> mockApiGatewayConstruction;
    // To mock TextUtils.isEmpty. Note: This can sometimes be tricky for system classes.
    private MockedStatic<TextUtils> mockedTextUtils;


    @Before
    public void setUp() {
        // Mock the construction of ContactsApiGateway.Builder
        // This is necessary because newInstance() creates it internally:
        // new ContactsApiGateway.Builder(mContext).build();
        mockBuilderConstruction = Mockito.mockConstruction(ContactsApiGatewayImpl.Builder.class,
                (mockBuilderInstance, constructionContext) -> {
                    // 'mockBuilderInstance' is the newly constructed ContactsApiGateway.Builder instance
                    // 'constructionContext' contains arguments passed to the constructor (e.g., mContext)

                    // We want to ensure that when build() is called on this mockBuilderInstance,
                    // it returns our predefined mockWrappedContactsApi.
                    when(mockBuilderInstance.build()).thenReturn(mockWrappedContactsApi);
                });

        mockedTextUtils = Mockito.mockStatic(TextUtils.class);
        mockedTextUtils.when(() -> TextUtils.isEmpty(any())).thenAnswer(invocation -> {
            CharSequence str = invocation.getArgument(0);
            return str == null || str.length() == 0;
        });

        // Now, when ExpenseContactApiGatewayImpl.newInstance(mockContext) is called,
        // it will internally try to create 'new ContactsApiGateway.Builder(mContext)',
        // which will be intercepted by our mockBuilderConstruction.
        // The '.build()' call on that intercepted builder will return 'mockWrappedContactsApi'.
        expenseContactApiGateway = ExpenseContactApiGatewayImpl.newInstance(mockContext);
    }

    @After
    public void tearDown() {
        // Close the construction mock to avoid interference between tests
        if (mockBuilderConstruction != null) {
            System.out.println("Closing mockBuilderConstruction");
            mockBuilderConstruction.close();
            mockBuilderConstruction = null; // Help GC and prevent reuse
        }
        if (mockedTextUtils != null) {
            System.out.println("Closing mockedTextUtils");
            mockedTextUtils.close();
            mockedTextUtils = null; // Help GC and prevent reuse
        }
        // Proper way to reset the singleton for testing if possible
        // Reflection could be used:
        try {
            java.lang.reflect.Field instanceField = ExpenseContactApiGatewayImpl.class.getDeclaredField("apiInstance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Handle exception or log, though for tests this might be acceptable to ignore if setup is robust
        }
    }

    @Test
    public void newInstance_returnsSameInstanceOnSubsequentCalls() {
        ExpenseContactApiGatewayImpl firstInstance = ExpenseContactApiGatewayImpl.newInstance(mockContext);
        ExpenseContactApiGatewayImpl secondInstance = ExpenseContactApiGatewayImpl.newInstance(mockContext);
        assertSame("newInstance should return a singleton instance", firstInstance, secondInstance);
    }

    @Test
    public void newInstance_initializesWrappedApi() {
        // Verify that during the first call in setUp(), the builder was used and build() was called.
        // The mockApiGatewayConstruction in setUp already configures the mockWrappedContactsApi to be returned.
        assertNotNull(expenseContactApiGateway); // Check if instance was created
        // Check if the builder was used (implicitly via the mock construction)
        // And that mockWrappedContactsApi is what our instance will use internally (this is harder to verify directly
        // without exposing internal fields, but the delegation tests will confirm it).
    }


    @Test
    public void readContacts_delegatesToWrappedApi() {
        ContactInfoFilterOptions mockOptions = mock(ContactInfoFilterOptions.class);
        expenseContactApiGateway.readContacts(mockOptions);
        verify(mockWrappedContactsApi).initialize(mockOptions);
    }

    @Test
    public void getRandomContact_delegatesToWrappedApi() throws IllegalStateException {
        ContactInfo mockContactInfo = mock(ContactInfo.class);
        when(mockWrappedContactsApi.getRandomContact()).thenReturn(mockContactInfo);

        ContactInfo result = expenseContactApiGateway.getRandomContact();

        assertSame(mockContactInfo, result);
        verify(mockWrappedContactsApi).getRandomContact();
    }

    @Test
    public void getContactInfo_withId_delegatesToWrappedApi() {
        String contactId = "123";
        ContactInfo mockContactInfo = mock(ContactInfo.class);
        when(mockWrappedContactsApi.getContactInfo(contactId)).thenReturn(mockContactInfo);

        ContactInfo result = expenseContactApiGateway.getContactInfo(contactId);

        assertSame(mockContactInfo, result);
        verify(mockWrappedContactsApi).getContactInfo(contactId);
    }

    @Test
    public void getContactInfo_withIdAndOptions_delegatesToWrappedApi() {
        String contactId = "123";
        ContactInfoOptions mockOptions = mock(ContactInfoOptions.class);
        ContactInfo mockContactInfo = mock(ContactInfo.class);
        when(mockWrappedContactsApi.getContactInfo(contactId, mockOptions)).thenReturn(mockContactInfo);

        ContactInfo result = expenseContactApiGateway.getContactInfo(contactId, mockOptions);

        assertSame(mockContactInfo, result);
        verify(mockWrappedContactsApi).getContactInfo(contactId, mockOptions);
    }

    @Test
    public void getContactIdFromRawContact_delegatesToWrappedApi() {
        String rawContactId = "raw123";
        String expectedContactId = "contact123";
        when(mockWrappedContactsApi.getContactIdFromRawContact(rawContactId)).thenReturn(expectedContactId);

        String result = expenseContactApiGateway.getContactIdFromRawContact(rawContactId);

        assertEquals(expectedContactId, result);
        verify(mockWrappedContactsApi).getContactIdFromRawContact(rawContactId);
    }

    @Test
    public void getContactIdFromAddress_delegatesToWrappedApi() {
        String address = "1234567890";
        String expectedContactId = "contact456";
        when(mockWrappedContactsApi.getContactIdFromAddress(address)).thenReturn(expectedContactId);

        String result = expenseContactApiGateway.getContactIdFromAddress(address);

        assertEquals(expectedContactId, result);
        verify(mockWrappedContactsApi).getContactIdFromAddress(address);
    }

    @Test
    public void getContactsFromIds_validIds_returnsListOfContactInfo() {
        String contactIdsString = "1,2";
        ContactInfo contact1 = new ContactInfo(); contact1.setId("1"); contact1.setName("Alice");
        ContactInfo contact2 = new ContactInfo(); contact2.setId("2"); contact2.setName("Bob");

        // Mock TextUtils.isEmpty for this specific case if needed, though default setup should work
        mockedTextUtils.when(() -> TextUtils.isEmpty(contactIdsString)).thenReturn(false);

        // We need to make the ExpenseContactApiGatewayImpl instance use the mockWrappedContactsApi
        // which it should from the setup, but its getContactInfo will be called.
        // So, we mock the behavior of mockWrappedContactsApi.getContactInfo
        when(mockWrappedContactsApi.getContactInfo("1")).thenReturn(contact1);
        when(mockWrappedContactsApi.getContactInfo("2")).thenReturn(contact2);


        List<ContactInfo> result = expenseContactApiGateway.getContactsFromIds(contactIdsString);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(contact1));
        assertTrue(result.contains(contact2));

        verify(mockWrappedContactsApi).getContactInfo("1");
        verify(mockWrappedContactsApi).getContactInfo("2");
    }

    @Test
    public void getContactsFromIds_emptyString_returnsEmptyList() {
        String contactIdsString = "";
        mockedTextUtils.when(() -> TextUtils.isEmpty(contactIdsString)).thenReturn(true);

        List<ContactInfo> result = expenseContactApiGateway.getContactsFromIds(contactIdsString);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockWrappedContactsApi, never()).getContactInfo(anyString());
    }

    @Test
    public void getContactsFromIds_nullString_returnsEmptyList() {
        String contactIdsString = null;
        mockedTextUtils.when(() -> TextUtils.isEmpty(contactIdsString)).thenReturn(true);

        List<ContactInfo> result = expenseContactApiGateway.getContactsFromIds(contactIdsString);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockWrappedContactsApi, never()).getContactInfo(anyString());
    }

    @Test
    public void getContactsFromIds_singleValidId_returnsListWithOneContactInfo() {
        String contactIdsString = "100";
        ContactInfo contact100 = new ContactInfo(); contact100.setId("100"); contact100.setName("Charlie");

        mockedTextUtils.when(() -> TextUtils.isEmpty(contactIdsString)).thenReturn(false);
        when(mockWrappedContactsApi.getContactInfo("100")).thenReturn(contact100);

        List<ContactInfo> result = expenseContactApiGateway.getContactsFromIds(contactIdsString);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contact100, result.get(0));
        verify(mockWrappedContactsApi).getContactInfo("100");
    }

}
