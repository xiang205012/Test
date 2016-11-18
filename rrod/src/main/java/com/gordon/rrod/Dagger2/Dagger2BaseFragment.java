package com.gordon.rrod.Dagger2;

import android.support.v4.app.Fragment;

/**
 * Created by gordon on 2016/6/15.
 */
public class Dagger2BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
    }



}
