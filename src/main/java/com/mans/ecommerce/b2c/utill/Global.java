package com.mans.ecommerce.b2c.utill;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;

public class Global
{

    private static final int MILLISECONDS_IN_MINUTE = 60000;

    private static final String CUSTOMER_ID = "customerId";

    public static Date getFuture(int minutes)
    {
        Date now = new Date();
        return new Date(now.getTime() + minutes * MILLISECONDS_IN_MINUTE);
    }

    public static String getString(String template, Map<String, Object> valuesMap)
    {
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(template);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name)
    {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
        {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                     .filter(cookie -> cookie.getName().equalsIgnoreCase(name))
                     .findFirst();
    }

    public static void addCookie(String customerId, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(CUSTOMER_ID, customerId);
        response.addCookie(cookie);
    }

    public static void setId(HttpServletResponse res, String id)
    {
        Cookie cookie = new Cookie(CUSTOMER_ID, id);
        res.addCookie(cookie);
    }

    public static Optional<String> getId(HttpServletRequest request)
    {
        Optional<Cookie> cookieOptional = getCookie(request, CUSTOMER_ID);
        if (cookieOptional.isPresent())
        {
            String id = cookieOptional.get().getValue();
            return Optional.of(id);
        }
        return Optional.empty();
    }

    //    private JsonResult json = JsonResult.instance();
    //        json.use(JsonView.with(user).onClass(User.class, Match.match()
    //                    .exclude("password").exclude("yetAnothertopSecretField")));
}
