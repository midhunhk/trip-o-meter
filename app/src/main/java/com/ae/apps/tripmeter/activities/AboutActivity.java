/**
 * MIT License

 Copyright (c) 2016 Midhun Harikumar

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.ae.apps.tripmeter.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ae.apps.common.activities.ToolBarBaseActivity;
import com.ae.apps.common.utils.DialogUtils;
import com.ae.apps.tripmeter.R;

/**
 * The About Activity
 */
public class AboutActivity extends ToolBarBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayHomeAsUp();

        // Navigate to GitHub Page
        Button viewSourceCodeBtn = (Button) findViewById(R.id.viewSourceCode);
        viewSourceCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.github_source_visible_url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Context context = this;

        // Show the License as a dialog
        Button viewLicenseBtn = (Button) findViewById(R.id.viewLicense);
        viewLicenseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DialogUtils.showMaterialInfoDialog(context, R.string.menu_license,
                        R.string.str_license, android.R.string.ok);
            }
        });
    }

    @Override
    protected int getToolbarResourceId() {
        return R.id.toolbar;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about;
    }
}
