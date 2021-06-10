package com.finwin.mycart_admin

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.finwin.mycart_admin.login.LoginActivity
import com.finwin.mycart_admin.utils.Constants

class SplashActivity : AppCompatActivity() {
    var sharedPreferences: SharedPreferences? = null
    var edit: Editor? = null
    lateinit var img_splash: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        img_splash = findViewById(R.id.img_splash)

        Glide.with(this).load("http://softadmin.pickcartshopy.com/assets/images/pickcart_App.png")
            .into(img_splash)
        // Picasso.get().load("http://softadmin.pickcartshopy.com/assets/images/pickcart_App.png").into(img_splash);

        // Picasso.get().load("http://softadmin.pickcartshopy.com/assets/images/pickcart_App.png").into(img_splash);
        Handler().postDelayed({
            sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)

            // this condition will do the trick.
//            if (sharedPreferences?.getString("tag", "notok") == "notok") {

                // add tag in SharedPreference here..
//                edit = sharedPreferences?.edit()
//                edit?.putString("tag", "ok")
//                edit?.commit()
//                val i = Intent(this@SplashActivity, WelcomeActivity::class.java)
//                startActivity(i)
//                finish()
//            } else if (sharedPreferences?.getString("tag", null) == "ok") {

            if ((sharedPreferences?.getBoolean(Constants.IS_LOGIN,false) == true) &&
                sharedPreferences?.getString(Constants.USERNAME,"")!="" &&
                sharedPreferences?.getString(Constants.PASSWORD,"")!=""

                    ) {
                    val i = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }else{
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
            //}
            // This method will be executed once the timer is over
        }, 3000)

    }
}