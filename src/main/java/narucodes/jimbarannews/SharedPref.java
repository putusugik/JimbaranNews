package narucodes.jimbarannews;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 7/30/2018.
 */

public class SharedPref {

    private static final String PREF_NAME = "Jimbaran News";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_ID_USER = "idUser";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setUserID(int idUser)
    {
        editor.putInt(KEY_ID_USER, idUser);
        //editor.putString(NAME_ID_USER, nameUser);
        // commit changes
        editor.commit();
    }

    public int getUserID()
    {
        return pref.getInt(KEY_ID_USER, -1);
    }

}
