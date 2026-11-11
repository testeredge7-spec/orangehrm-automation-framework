package dataproviders;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {

        return new Object[][] {

                {"WrongUser", "admin123"},
                {"Admin", "WrongPass"},
                {"WrongUser", "WrongPass"}

        };
    }
}