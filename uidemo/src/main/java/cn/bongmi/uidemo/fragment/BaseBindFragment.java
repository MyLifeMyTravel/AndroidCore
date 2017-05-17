package cn.bongmi.uidemo.fragment;

import android.support.v4.app.Fragment;

import cn.bongmi.uidemo.OnStepFinishListener;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class BaseBindFragment extends Fragment {
  protected OnStepFinishListener onStepFinishListener;

  public void setOnStepFinishListener(
      OnStepFinishListener onStepFinishListener) {
    this.onStepFinishListener = onStepFinishListener;
  }
}
