package com.swiftpay.appUtils;

public interface Endpoint {
    String DASHBOARD = "/";
    String SEND = "/send";
    String RECEIVE = "/receive";
    String PAYOUT = "/payout";
    String SEARCH = "/search";
    String TRANSFERS = "/transfers";
    String ADMIN = "/admin";
    String ADMIN_OFFICES = ADMIN + "/offices";
    String ADMIN_AGENCIES = ADMIN + "/agencies";
    String ADMIN_USERS = ADMIN + "/users";
    String ADMIN_TRANSFERS = ADMIN + "/transfers";
}
