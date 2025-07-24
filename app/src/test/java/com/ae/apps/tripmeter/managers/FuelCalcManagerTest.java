package com.ae.apps.tripmeter.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ae.apps.tripmeter.models.FuelCalcResult;

import com.ae.apps.tripmeter.utils.AppConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FuelCalcManagerTest {

    @Mock
    Context mockContext;

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    SharedPreferences.Editor mockEditor;

    private FuelCalcManager fuelCalcManager;

    @Before
    public void setUp() {
        // Mock the SharedPreferences and Editor behavior
        // when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor); // Important: return mockEditor for chaining

        fuelCalcManager = new FuelCalcManager(mockContext);
    }

    @Test
    public void calculateFuelAndPrice_validInputs_calculatesCorrectlyAndSavesPrefs() {
        // Arrange
        float distance = 100.0f;
        float fuelPrice = 5.0f;
        float mileage = 20.0f; // 100 / 20 = 5 units of fuel

        float expectedFuelNeeded = 5.0f;
        float expectedTotalPrice = 25.0f; // 5 units * 5 price/unit

        // Use try-with-resources for MockedStatic to ensure it's closed
        try (MockedStatic<PreferenceManager> mockedStaticPreferenceManager = Mockito.mockStatic(PreferenceManager.class)) {
            mockedStaticPreferenceManager.when(() -> PreferenceManager.getDefaultSharedPreferences(mockContext))
                    .thenReturn(mockSharedPreferences);

            // Act
            FuelCalcResult result = fuelCalcManager.calculateFuelAndPrice(distance, fuelPrice, mileage);

            // Assert
            assertNotNull(result);
            assertFalse("DataError should be false for valid inputs", result.isDataError());
            assertEquals("Fuel quantity needed calculation is incorrect", expectedFuelNeeded, result.getFuelQuantityNeeded(), 0.001);
            assertEquals("Total fuel price calculation is incorrect", expectedTotalPrice, result.getFuelPriceTotal(), 0.001);

            // Verify SharedPreferences interactions
            // ArgumentCaptor to capture the actual arguments passed to putString
            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

            verify(mockEditor, times(2)).putString(keyCaptor.capture(), valueCaptor.capture());
            verify(mockEditor).apply();

            // Check captured keys and values
            assertTrue("PREF_KEY_FUEL_PRICE was not saved or saved with wrong key", keyCaptor.getAllValues().contains(AppConstants.PREF_KEY_FUEL_PRICE));
            assertTrue("PREF_KEY_MILEAGE was not saved or saved with wrong key", keyCaptor.getAllValues().contains(AppConstants.PREF_KEY_MILEAGE));

            // Find the index of each key and check its corresponding value
            int fuelPriceIndex = keyCaptor.getAllValues().indexOf(AppConstants.PREF_KEY_FUEL_PRICE);
            Assert.assertEquals(String.valueOf(fuelPrice), valueCaptor.getAllValues().get(fuelPriceIndex));

            int mileageIndex = keyCaptor.getAllValues().indexOf(AppConstants.PREF_KEY_MILEAGE);
            Assert.assertEquals(String.valueOf(mileage), valueCaptor.getAllValues().get(mileageIndex));
        }
    }

    @Test
    public void calculateFuelAndPrice_mileageIsZero_returnsDataErrorAndDoesNotSavePrefs() {
        // Arrange
        float distance = 100.0f;
        float fuelPrice = 5.0f;
        float mileage = 0.0f;

        // Use try-with-resources for MockedStatic
        try (MockedStatic<PreferenceManager> mockedStaticPreferenceManager = Mockito.mockStatic(PreferenceManager.class)) {
            mockedStaticPreferenceManager.when(() -> PreferenceManager.getDefaultSharedPreferences(mockContext))
                    .thenReturn(mockSharedPreferences);

            // Act
            FuelCalcResult result = fuelCalcManager.calculateFuelAndPrice(distance, fuelPrice, mileage);

            // Assert
            assertNotNull(result);
            assertTrue("DataError should be true for zero mileage", result.isDataError());
            // Check that values are not set or are default (0.0f for float)
            assertEquals(0.0f, result.getFuelQuantityNeeded(), 0.001);
            assertEquals(0.0f, result.getFuelPriceTotal(), 0.001);


            // Verify SharedPreferences were NOT interacted with for saving price/mileage
            verify(mockEditor, never()).putString(AppConstants.PREF_KEY_FUEL_PRICE, String.valueOf(fuelPrice));
            verify(mockEditor, never()).putString(AppConstants.PREF_KEY_MILEAGE, String.valueOf(mileage));
            verify(mockEditor, never()).apply(); // Or verify it was called 0 times if it could be called for other reasons
        }
    }

    @Test
    public void calculateFuelAndPrice_mileageIsNegative_returnsDataErrorAndDoesNotSavePrefs() {
        // Arrange
        float distance = 100.0f;
        float fuelPrice = 5.0f;
        float mileage = -10.0f;

        // Use try-with-resources for MockedStatic
        try (MockedStatic<PreferenceManager> mockedStaticPreferenceManager = Mockito.mockStatic(PreferenceManager.class)) {
            mockedStaticPreferenceManager.when(() -> PreferenceManager.getDefaultSharedPreferences(mockContext))
                    .thenReturn(mockSharedPreferences);

            // Act
            FuelCalcResult result = fuelCalcManager.calculateFuelAndPrice(distance, fuelPrice, mileage);

            // Assert
            assertNotNull(result);
            assertTrue("DataError should be true for negative mileage", result.isDataError());
            assertEquals(0.0f, result.getFuelQuantityNeeded(), 0.001);
            assertEquals(0.0f, result.getFuelPriceTotal(), 0.001);

            // Verify SharedPreferences were NOT interacted with for saving price/mileage
            verify(mockEditor, never()).putString(anyString(), anyString()); // More general check
            verify(mockEditor, never()).apply();
        }
    }
}
