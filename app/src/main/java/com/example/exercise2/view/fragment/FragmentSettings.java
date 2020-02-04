package com.example.exercise2.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.exercise2.R;
import com.example.exercise2.adapter.SettingsAdapter;
import com.example.exercise2.view.activity.HomeActivity;
import com.example.exercise2.view.dialog.DialogLanguage;
import com.example.exercise2.view.dialog.DialogSort;
import com.example.exercise2.view.dialog.DialogTheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragmentSettings extends BaseFragment implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    private ExpandableListView mExpandableListView;

    private ExpandableListAdapter mSettingsAdapter;

    private List<String> mListSettings;

    private HashMap<String, List<String>> mListGroup;

    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListGroup = initSettings();
        mListSettings = new ArrayList<String>(mListGroup.keySet());
        mSettingsAdapter = new SettingsAdapter(getActivity(), mListSettings, mListGroup);
        mFragmentManager = getActivity().getSupportFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExpandableListView.setAdapter(mSettingsAdapter);
        mExpandableListView.setOnChildClickListener(this);
        mExpandableListView.setOnGroupClickListener(this);

        // Set lại vị trí của icon Indicator sang bên phải( - 200 là vì tính từ start )
//        Display newDisplay = getActivity().getWindowManager().getDefaultDisplay();
//        int width = newDisplay.getWidth();
//        mExpandableListView.setIndicatorBounds(width - 200, width);
        // Ẩn icon Indicator
        mExpandableListView.setGroupIndicator(null);
        // Set mặc định là sẽ expand tất cả các group settings.
        int amountGroup = mListGroup.size();
        for(int i = 0; i < amountGroup; i++){
            mExpandableListView.expandGroup(i);
        }

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String nameItem = mListGroup.get(mListSettings.get(groupPosition)).get(childPosition);
        initDialog(nameItem);

        return true;
    }

    public HashMap<String, List<String>> initSettings() {
        Resources resources = getResources();
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> settings = new ArrayList<String>();
        settings.add(resources.getString(R.string.setting_group_1_item1));
        settings.add(resources.getString(R.string.setting_group_1_item2));
        settings.add(resources.getString(R.string.setting_group_1_item3));
        settings.add(resources.getString(R.string.setting_group_1_item4));

        List<String> feature = new ArrayList<String>();
        feature.add(resources.getString(R.string.setting_group_2_item1));
        feature.add(resources.getString(R.string.setting_group_2_item2));
        feature.add(resources.getString(R.string.setting_group_2_item3));
        feature.add(resources.getString(R.string.setting_group_2_item4));

        List<String> info = new ArrayList<String>();
        info.add(resources.getString(R.string.setting_group_3_item1));
        info.add(resources.getString(R.string.setting_group_3_item2));
        info.add(resources.getString(R.string.setting_group_3_item3));
        info.add(resources.getString(R.string.setting_group_3_item4));

        expandableListDetail.put(resources.getString(R.string.setting_group_1), settings);
        expandableListDetail.put(resources.getString(R.string.setting_group_2), feature);
        expandableListDetail.put(resources.getString(R.string.setting_group_3), info);

        return expandableListDetail;
    }

    private void initDialog(String nameItem){
        if(nameItem.equalsIgnoreCase(getResources().getString(R.string.setting_group_1_item3))){
            DialogLanguage dialogLanguage = DialogLanguage.newInstance();
            dialogLanguage.show(mFragmentManager, DialogLanguage.class.getName());
        }

        if (nameItem.equalsIgnoreCase(getResources().getString(R.string.setting_group_1_item1))){
            DialogTheme dialogTheme = DialogTheme.newInstance();
            dialogTheme.show(mFragmentManager, DialogTheme.class.getName());
        }

        if (nameItem.equalsIgnoreCase(getResources().getString(R.string.setting_group_2_item1))){
            DialogSort dialogSort = DialogSort.newInstance();
            dialogSort.show(mFragmentManager, DialogSort.class.getName());
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        // Return true để tắt click vào group
        return true;
    }
}
