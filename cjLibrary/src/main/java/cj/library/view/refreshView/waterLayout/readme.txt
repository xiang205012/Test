  mRefreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
        mList = (ListView) findViewById(R.id.list);
        mStop = (Button) findViewById(R.id.stop_refresh);

        String[] strs = {
            "The",
            "Canvas",
            "class",
            "holds",
            "the",
            "draw",
            "calls",
            ".",
            "To",
            "draw",
            "something,",
            "you",
            "need",
            "4 basic",
            "components",
            "Bitmap",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        mList.setAdapter(adapter);

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshLayout.finishRefreshing();
            }
        });

        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void refreshing() {
                // do something when refresh starts
            }

            @Override
            public void completeRefresh() {
                // do something when refresh complete
            }
        });

<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.tuesda.walker.circlerefresh.CircleRefreshLayout
        xmlns:app="http://schemas.android.com/apk/res-auto"
        app:AniBackColor="#ff8b90af"
        app:AniForeColor="#ffffffff"
        app:CircleSmaller="6"
        android:id="@+id/refresh_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <ListView
            android:background="#ffffffff"
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"></ListView>
    </com.tuesda.walker.circlerefresh.CircleRefreshLayout>
    <Button
        android:id="@+id/stop_refresh"
        android:text="Stop Refreshing"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />



</RelativeLayout>
