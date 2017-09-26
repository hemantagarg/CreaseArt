package com.app.creaseart.activities;


/**
 * Created by Rahul Hooda on 14/7/17.
 */
public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            return "f5vd27Bt";
        }

        @Override
        public String merchant_ID() {
            return "5880661";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "w21LvUpp4y";
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "f5vd27Bt";
        }

        @Override
        public String merchant_ID() {
            return "5880661";
        }

        @Override
        public String furl() {
            return "http://dev.stackmindz.com/creaseart/api/response.php?success=failure";
        }

        @Override
        public String surl() {
            return "http://dev.stackmindz.com/creaseart/api/response.php?success=success";
        }

        @Override
        public String salt() {
            return "w21LvUpp4y";
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
