package com.example.realreal.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.realreal.Home.Fragment.Intro_first_fragment;
import com.example.realreal.Home.Fragment.Intro_five_fragment;
import com.example.realreal.Home.Fragment.Intro_fourth_fragment;
import com.example.realreal.Home.Fragment.Intro_second_fragment;
import com.example.realreal.Home.Fragment.Intro_third_fragment;

import java.util.ArrayList;

public class Intro_VPAdapter extends FragmentPagerAdapter {
  private ArrayList<Fragment> items;
  private ArrayList<String> itext = new ArrayList<String>();

  public Intro_VPAdapter(FragmentManager fm) {
    super(fm);
    items = new ArrayList<Fragment>();
    items.add(new Intro_first_fragment());
    items.add(new Intro_second_fragment());
    items.add(new Intro_third_fragment());
    items.add(new Intro_fourth_fragment());
    items.add(new Intro_five_fragment());

  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return itext.get(position);
  }


  @Override
  public Fragment getItem(int position) {
    return items.get(position);
  }

  @Override
  public int getCount() {
    return items.size();
  }
}

