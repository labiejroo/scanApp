/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.datacapture.matrixscanbubblessample.scan.bubble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import com.scandit.datacapture.matrixscanbubblessample.R;
import com.scandit.datacapture.matrixscanbubblessample.models.AppManager;
import com.scandit.datacapture.matrixscanbubblessample.scan.bubble.data.BubbleData;
import com.scandit.datacapture.matrixscanbubblessample.models.MyItem;
import com.scandit.datacapture.matrixscanbubblessample.Data.DataBaseHandler;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Bubble {

    Dialog myDialog;

    //private MyItem[] myItems = new MyItem[3];
    private List<MyItem> myItems = new ArrayList<>();
    public final View root;
    private final View containerShelfData;
    private final TextView textCode,textCode2;
    private final ImageView imgView;
    private final TextView textView;

    private Transition transition = new Fade();
    private boolean showShelfData = true;

    public Bubble(Context context, BubbleData data, String code) {

        myDialog = new Dialog(context);

        myItems =  AppManager.getInstance().getMyItems();

        root = View.inflate(context, R.layout.bubble, null);
        root.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        containerShelfData = root.findViewById(R.id.shelf_container);

        textCode2 = root.findViewById(R.id.text_code3);
        textCode = root.findViewById(R.id.text_code);
        imgView = root.findViewById(R.id.imageView);
        textView = root.findViewById(R.id.textView);

        TextView textShelfData = root.findViewById(R.id.text_shelf_data);
        TextView textShelfDataLabel = root.findViewById(R.id.text_shelf_data_label);

        transition.setDuration(100);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleText();
            }
        });

        String aboutMe = "UnKnown";

        for (MyItem myItem: myItems) {
            if (myItem.code.equals(code) && myItem.isScrap.equals("Y")) {
                aboutMe = "SCRAP : " + myItem.name;

                textShelfData.setBackgroundColor(0xffff0000);

                break;

            } else if (myItem.code.equals(code)) {
                aboutMe = myItem.name;
                textShelfData.setBackgroundColor(0xFF00FF00);

                break;
            }
        }

        textShelfData.setText(
                //context.getString(R.string.shelf_text, data.shelfCount, data.backroomCount)
                aboutMe
        );

        textCode.setText(code);

        showCurrentData();
    }

    public void ShowPopup() {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.custompopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void startTransition() {
        TransitionManager.beginDelayedTransition((ViewGroup) root, transition);
    }

    private void toggleText() {
        //Open PopUp
        ShowPopup();

        //Action
        showShelfData = !showShelfData;
        showCurrentData();
    }

    private void showCurrentData() {
        if (showShelfData) {
            showShelfDataInternal();
        } else {
            showCodeInternal();
        }
    }

    public void show() {
        startTransition();
        root.setVisibility(View.VISIBLE);
    }

    public void hide() {
        startTransition();
        root.setVisibility(View.GONE);
    }

    private void showShelfDataInternal() {
        containerShelfData.setVisibility(View.VISIBLE);
        textCode.setVisibility(View.GONE);
        textCode2.setVisibility(View.GONE);

        textView.setVisibility(View.GONE);
        imgView.setVisibility(View.GONE);
    }

    private void showCodeInternal() {
        containerShelfData.setVisibility(View.GONE);
        textCode.setVisibility(View.VISIBLE);
        textCode2.setVisibility(View.VISIBLE);

        textView.setVisibility(View.VISIBLE);
        imgView.setVisibility(View.VISIBLE);
    }
}


