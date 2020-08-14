package com.mans.ecommerce.b2c.utill;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

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

    public static Optional<String> getId(ServerHttpRequest request)
    {
        String id = request.getCookies()
                           .getFirst(CUSTOMER_ID)
                           .getValue();
        if (id == null)
        {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    public static void setId(ServerHttpResponse res, String id)
    {
        ResponseCookie cookie =  ResponseCookie.from(CUSTOMER_ID, id).build();
        res.addCookie(cookie);
    }

    //    private JsonResult json = JsonResult.instance();
    //        json.use(JsonView.with(user).onClass(User.class, Match.match()
    //                    .exclude("password").exclude("yetAnothertopSecretField")));
}
