package com.haiking.pojo;

public class Host {
    private Context context;

    public Context getContext() {
        if (context==null){
            context = new Context();
        }
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
