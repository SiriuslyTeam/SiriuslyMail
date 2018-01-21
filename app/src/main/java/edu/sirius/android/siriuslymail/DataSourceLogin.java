package edu.sirius.android.siriuslymail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21.01.2018.
 */

public class DataSourceLogin {
    private List<Login> logins = new ArrayList<>();
    private static DataSourceLogin dataSourceLogin = new DataSourceLogin();
    static DataSourceLogin getInstance() {
        return dataSourceLogin;
    }

}
