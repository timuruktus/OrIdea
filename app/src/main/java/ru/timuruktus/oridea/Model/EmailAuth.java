package ru.timuruktus.oridea.Model;


import android.text.TextUtils;

public class EmailAuth {

    private String email, pass;

    public EmailAuth(String email, String pass){
        this.email = email;
        this.pass = pass;
    }

    /**
     * Checks if <field>email</field> and <field>password</field>
     * are correct
     * @return - false, if:
     * 1)password/email is empty
     * 3)email doesn't contain "@" and "."
     * 4)password length < 6 letters
     */
    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) valid = false;
        if (TextUtils.isEmpty(pass)) valid = false;
        if (!email.contains("@")) valid = false;
        if (!email.contains(".")) valid = false;
        if (pass.length()<6) valid = false;
        return valid;
    }

    public void startRegister(){

    }

    public void startAuth(){

    }
}
