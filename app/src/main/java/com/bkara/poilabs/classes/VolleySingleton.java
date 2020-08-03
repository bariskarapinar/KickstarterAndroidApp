package com.bkara.poilabs.classes;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by bkara on 29/7/2020.
 *
 *
 *
 */

public class VolleySingleton {

    static VolleySingleton instance = null;
    private RequestQueue requestQueue;


    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(App.getContext());
    }


    public static VolleySingleton getInstance() {

        if (instance == null)
            instance = new VolleySingleton();

        return instance;
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
