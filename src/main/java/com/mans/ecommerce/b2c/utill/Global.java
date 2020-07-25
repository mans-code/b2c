package com.mans.ecommerce.b2c.utill;

import java.util.Date;

public class Global
{

    private static final int MILLISECONDS_IN_MINUTE = 60000;

    public static Date getFuture(int minutes)
    {
        Date now = new Date();
        return new Date(now.getTime() + minutes * MILLISECONDS_IN_MINUTE);
    }

}
