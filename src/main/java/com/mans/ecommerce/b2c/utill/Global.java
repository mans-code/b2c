package com.mans.ecommerce.b2c.utill;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerRequest;

public class Global
{

    private static final int MILLISECONDS_IN_MINUTE = 60000;

    private static final String CUSTOMER_ID = "customerId";

    public static Instant getFuture(int minutes)
    {
        Instant now = Instant.now();
        return Instant.ofEpochMilli(now.toEpochMilli() + minutes * MILLISECONDS_IN_MINUTE);
    }

    public static String getString(String template, Map<String, Object> valuesMap)
    {
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(template);
    }

    public static Optional<String> getIdHeader(ServerHttpRequest request)
    {
        HttpCookie id = request.getCookies()
                               .getFirst(CUSTOMER_ID);
        return getIdHeader(id);
    }

    public static Optional<String> getIdHeader(ServerRequest request)
    {
        HttpCookie id = request.cookies()
                               .getFirst(CUSTOMER_ID);
        return getIdHeader(id);
    }

    private static Optional<String> getIdHeader(HttpCookie id)
    {
        if (id == null)
        {
            return Optional.empty();
        }

        return Optional.of(id.getValue());
    }

    public static void setIdHeader(ServerHttpResponse res, String id)
    {
        ResponseCookie cookie = ResponseCookie.from(CUSTOMER_ID, id).build();
        res.addCookie(cookie);
    }

    public static void getIdOrCreate(ServerHttpRequest req)
    {
    }

    //    private JsonResult json = JsonResult.instance();
    //        json.use(JsonView.with(user).onClass(User.class, Match.match()
    //                    .exclude("password").exclude("yetAnothertopSecretField")));
}
