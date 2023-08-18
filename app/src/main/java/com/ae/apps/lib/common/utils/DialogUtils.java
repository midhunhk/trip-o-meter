/*
 * Copyright (c) 2015 Midhun Harikumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ae.apps.lib.common.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

/**
 * Utility class to show dialogs
 *
 * @author Midhun
 */
public class DialogUtils {

    /**
     * Displays a basic dialog with a button
     * <p>
     * Note: Do not pass getBaseContext() and instead this from an activity
     *
     * @param context           the context
     * @param titleResourceId   title resource id
     * @param messageResourceId message resource id
     */
    public static void showWithMessageAndOkButton(final Context context, int titleResourceId, int messageResourceId,
                                                  int buttonResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(titleResourceId)
                .setMessage(messageResourceId)
                .setPositiveButton(buttonResourceId, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We shall dismiss the dialog when the ok is clicked
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    /**
     * To display a Material Dialog box, with title, content and a button
     *
     * @param context                  the context
     * @param titleResourceId          title resource id
     * @param messageResourceId        message resource id
     * @param positiveButtonResourceId positive button resource id
     */
    public static void showMaterialInfoDialog(final Context context, int titleResourceId, int messageResourceId,
                                              int positiveButtonResourceId) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(titleResourceId)
                .setMessage(messageResourceId)
                .setPositiveButton(positiveButtonResourceId, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We shall dismiss the dialog when the positive button is clicked
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    /**
     * Shows a dialog with a custom view and ok button
     *
     * @param context         the context
     * @param layoutInflater  to inflate the custom view
     * @param viewResourceId  view resource id
     * @param titleResourceId title resource  id
     */
    public static void showCustomViewDialog(final Context context, LayoutInflater layoutInflater,
                                            int viewResourceId, int titleResourceId) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setView(layoutInflater.inflate(viewResourceId, null))
                .setTitle(titleResourceId)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }
}