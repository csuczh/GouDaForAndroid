package com.dg.app.adapter;

import android.os.Bundle;

public final class ViewPageInfo {

	public final String tag;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, String _tag, Bundle _args) {
    	title = _title;
        tag = _tag;

        args = _args;
    }
}