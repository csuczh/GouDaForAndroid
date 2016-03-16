package  com.dg.app.ui;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dg.app.AppContext;
import com.dg.app.R;
import com.dg.app.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NavigationDrawerFragment extends BaseFragment implements
        View.OnClickListener{
//选择项目的位置
    private static final String STATE_SELECTED_POSITION="selected_navigation_drawer_position";

    //回调接口
    private NavigationDrawerCallbacks mCallbacks;
    /*
    帮助绑定action bar到这导航抽屉
    * */
    private ActionBarDrawerToggle mDrawerToggle;

    //包含该抽屉的布局的DrawerLayouut
    private DrawerLayout mDrawerLayout;
    //导航抽屉中菜单项的视图
    private  View mDrawerListView;

    //在绑定UI中,导航抽屉的布局Fragment
    private View mFragmentContainerView;

    //目前的选择菜单项的ID
    private int mCurrentSelectedPosition=0;

    //是否保存当前状态，当关闭时打开当前状态
    private boolean mFromSavedInstanceState;
//    //问答的菜单项
//    @InjectView(R.id.menu_item_quests)
//    View mMenu_item_quests;
//    //开放软件的菜单项
//    @InjectView(R.id.menu_item_opensoft)
//    View mMenu_item_opensoft;
//    //博客的菜单项
//    @InjectView(R.id.menu_item_blog)
//    View mMenu_item_blog;
//    //gitapp客户端
//    @InjectView(R.id.menu_item_gitapp)
//    View mMenu_item_gitapp;
//    //设置菜单项
//    @InjectView(R.id.menu_item_setting)
//    View mMenu_item_setting;
//    //设置主题
//    @InjectView(R.id.menu_item_theme)
//    View mMenu_item_theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
            mCurrentSelectedPosition=savedInstanceState.getInt(STATE_SELECTED_POSITION);
        mFromSavedInstanceState=true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        以前一般都是在Activity中添加menu菜单，一般是重写onCreateOptionsMenu和onOptionsItemSelected方法。
        现在用fragment用的多了，就在fragment里面添加menu菜单，也是重写了onCreateOptionsMenu和onOptionsItemSelected方法，但是发现没有效果。
        * */
        setHasOptionsMenu(true);
    }
   //覆盖父类的方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerListView =inflater.inflate(R.layout.fragment_navigation_drawer,container,false);
        mDrawerListView.setOnClickListener(this);
        ButterKnife.inject(this, mDrawerLayout);
//        initView(mDrawerListView);
//        initData();
        return  mDrawerListView;
    }
//点击不同的菜单项是做出相应的跳转
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {

        }
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 800);
    }
    //切换昼夜主题
    private  void swithchTheme()
    {}
//    @Override
//    public void initView(View view) {
//        super.initView(view);
//        TextView night=(TextView)view.findViewById(R.id.tv_night);
//        if(AppContext.getNightModeSwitch())
//        {
//             night.setText("日间");
//        }else{
//            night.setText("夜间");
//        }
//        mMenu_item_quests.setOnClickListener(this);
//        mMenu_item_blog.setOnClickListener(this);
//        mMenu_item_opensoft.setOnClickListener(this);
//        mMenu_item_setting.setOnClickListener(this);
//        mMenu_item_theme.setOnClickListener(this);
//
//        mMenu_item_gitapp.setOnClickListener(this);
//    }
//
//    @Override
//    public void initData() {
//        super.initData();
//    }
    //判断抽屉是否打开
    public  boolean isDrawerOpen(){
        return mDrawerLayout!=null&&mDrawerLayout.isDrawerOpen(mFragmentContainerView);

    }
//    /*
//    * 该fragment的用户必须调用setup方法来启用该抽屉菜单
//    * @param fragmentId 为该fragment在activity中的fragment ID
//    * @param drawerLayout 为包含该Fragment的UI
//    * */
    public void setUp(int fragmentId,DrawerLayout drawerLayout)
    {
        mFragmentContainerView=getActivity().findViewById(fragmentId);
        mDrawerLayout=drawerLayout;

        //当抽屉打开时，设置一个在客户区设置一个阴影覆盖层在主内容
        // GravityCompat.start为左边
        mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);
       /*自定义的actionbar布局文件*/
        ActionBar actionBar =getActionBar();
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View titleView = inflater.inflate(R.layout.action_bar_title, null);
        actionBar.setCustomView(titleView, lp);

        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);


///*
//        //v7支持包中的，在应用程序的设置主题
//        //用于和抽屉绑定
//        ActionBar actionBar =getActionBar();
//        //点击返回时返回上一层的，而不是app的顶层
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        /**
//         * Enable or disable the "home" button in the corner of the action bar. (Note that this
//         * is the application home/up affordance on the action bar, not the systemwide home
//         * button.)
//         */
//        actionBar.setHomeButtonEnabled(true);
//
//        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
//
//
//
//        mDrawerToggle=new ActionBarDrawerToggle(getActivity(),mDrawerLayout,null,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getActivity().invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                getActivity().invalidateOptionsMenu();
//            }
//        };
//        mDrawerLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 * Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.
//                 *
//                 * <p>This should be called from your <code>Activity</code>'s
//                 * {@link Activity#onPostCreate(android.os.Bundle) onPostCreate} method to synchronize after
//                 * the DrawerLayout's instance state has been restored, and any other time when the state
//                 * may have diverged in such a way that the ActionBarDrawerToggle was not notified.
//                 * (For example, if you stop forwarding appropriate drawer events for a period of time.)</p>
//                 */
//                mDrawerToggle.syncState();
//            }
//        });
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    //打开抽屉菜单
    public void openDrawerMenu()
    {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }
    //选择项目表示是首页还是抽屉
    public void selectItem(int position)
    {
        mCurrentSelectedPosition=position;
        if(mDrawerLayout!=null)
        {
          mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if(mCallbacks!=null)
        {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }
  //fragment和activity相关联的是调用
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      try{ mCallbacks=(NavigationDrawerCallbacks)activity;
      } catch (ClassCastException e)
      {
          throw new ClassCastException("Activity must implement NavigationDrawerCallbacs.");
      }
    }
    //解除绑定
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }
    //保存状态
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION,mCurrentSelectedPosition);
    }
    //配置改变

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    //创建菜单使用

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(mDrawerToggle.onOptionsItemSelected(item))
//        {
//            return true;
//        }
        return  super.onOptionsItemSelected(item);
    }

    public ActionBar getActionBar()
    {
        return ((ActionBarActivity)getActivity()).getSupportActionBar();
    }
    //定义回调接口，在该类中可以使用该接口来调用实现该接口的方法
  public interface NavigationDrawerCallbacks{
      void onNavigationDrawerItemSelected(int position);
  }


}