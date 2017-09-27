package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class Suggestion {

    @SerializedName("air")
    public Air air;

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("drsg")
    public Dress dress;

    @SerializedName("flu")
    public Ill ill;

    @SerializedName("sport")
    public Sport sport;

    @SerializedName("trav")
    public Travel travel;

    public class Air{
        @SerializedName("txt")
        public String info;
    }
    public class Comfort{
        @SerializedName("txt")
        public String info;
    }
    public class Dress{
        @SerializedName("txt")
        public String info;
    }
    public class Ill{
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
    public class Travel{
        @SerializedName("txt")
        public String info;
    }


}
