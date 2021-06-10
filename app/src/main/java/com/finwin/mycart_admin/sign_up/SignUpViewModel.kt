package com.finwin.mycart_admin.sign_up

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import androidx.databinding.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.pedant.SweetAlert.SweetAlertDialog
import com.finwin.mycart_admin.BR
import com.finwin.mycart_admin.retrofit.ApiInterface
import com.finwin.mycart_admin.retrofit.RetrofitClient
import com.finwin.mycart_admin.services.Services
import com.finwin.mycart_admin.sign_up.action.SignUpAction

import com.finwin.mycart_admin.sign_up.pojo.BranchData
import com.finwin.mycart_admin.sign_up.pojo.Data
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.HashMap

class SignUpViewModel(application: Application) : AndroidViewModel(application), Observable {

    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    lateinit var apiInterface: ApiInterface
    var repository: SignUpRepository= SignUpRepository().getInstance()

    var obEmailId = ObservableField("")
    var obName = ObservableField("")
    var obMobile = ObservableField("")
    var obPassword = ObservableField("")
    var obConfirmPassword = ObservableField("")
    var mAction: MutableLiveData<SignUpAction> = MutableLiveData()


    private var selectedBranch = 0

    var loading: SweetAlertDialog? = null
    fun initLoading(context: Context?) {
        loading = Services.showProgressDialog(context)
    }

    fun cancelLoading() {
        if (loading != null) {
            loading!!.cancel()
            loading = null
        }
    }
    var branchId: ObservableField<String> = ObservableField("")
    var firmId: ObservableField<String> = ObservableField("")
    var branchListData: ObservableArrayList<BranchData> = ObservableArrayList()
    var branchList: ObservableArrayList<String> = ObservableArrayList()

    init {
        repository.mAction=mAction

        branchListData.clear()
        branchList.clear()
        branchList.add("--Select Branch--")
        branchListData.add(BranchData("00", "-1","--Select Branch--","","","","",""))
    }
    @Bindable
    private var selectedPostOfficeOnly = 0
    private val registry = PropertyChangeRegistry()

    @Bindable
    fun getSelectedBranch(): Int {
        return selectedBranch
    }

    fun setSelectedBranch(selectedBranch: Int) {
        this.selectedBranch = selectedBranch
        registry.notifyChange(this,BR.selectedBranch)
    }

    fun onSelectedBranch(
        parent: AdapterView<*>?, view: View?, position: Int, id: Long
    ) {
        branchId.set(branchListData[position].BranchId .toString())
        firmId.set(branchListData[position].FirmId .toString())
    }

    public fun getBranches() {

        val jsonParams: MutableMap<String?, Any?> = HashMap()
        jsonParams["Flag"] = "SELECTALL"


        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )

        apiInterface = RetrofitClient().RetrofitClient()?.create(ApiInterface::class.java)!!
        repository.getBranches(apiInterface, body)
    }

    fun clickSignUp(view: View)
    {

        if (obName.get().equals(""))
        {
            showSnakbar("Name cannot be empty",view)
        }else if (obEmailId.get().equals(""))
        {
            showSnakbar("Email cannot be empty",view)
        }else if (obMobile.get().equals(""))
        {
            showSnakbar("Phone number cannot be empty",view)
        }else if (branchId.get().equals("-1"))
        {
            showSnakbar("Please select branch",view)
        }else if (obPassword.get().equals(""))
        {
            showSnakbar("Password cannot be empty",view)
        }else if (obConfirmPassword.get().equals(""))
        {
            showSnakbar("Please confirm password!",view)
        }else if (!obConfirmPassword.get().equals(obConfirmPassword.get()))
        {
            showSnakbar("Passwords do not match",view)
        }else{
            initLoading(view.context)
            signUp()

        }
    }

    private fun signUp() {
        val jsonParams: MutableMap<String?, Any?> = HashMap()
        jsonParams["Flag"] = "INSERT"
        jsonParams["Telephone"] = obMobile.get()
        jsonParams["EmailId"] = obEmailId.get()
        jsonParams["CustName"] = obName.get()
        jsonParams["Cust_LastName"] =""
        jsonParams["FirmId"] =firmId.get()
        jsonParams["BranchId"] =branchId.get()
        jsonParams["CustId"] ="0"
        jsonParams["Password"] =obPassword.get()


        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )

        apiInterface = RetrofitClient().RetrofitClient()?.create(ApiInterface::class.java)!!
        repository.signUp(apiInterface, body)
    }

    public fun login() {

        val jsonParams: MutableMap<String?, Any?> = HashMap()
        jsonParams["Flag"] = "CUST_LOGIN"
        jsonParams["UserName"] = obEmailId.get()
        jsonParams["Password"] = obPassword.get()

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )

        apiInterface = RetrofitClient().RetrofitClient()?.create(ApiInterface::class.java)!!
        repository.login(apiInterface, body)
    }

    fun clickSignIn(view: View)
    {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }

    fun setBranchData(data: Data) {
        if (data.branchData.size>0){
            for (i in data.branchData.indices) {
                branchList.add(data.branchData[i].BranchName)
                branchListData.add(data.branchData[i])
            }
        }

    }

    fun showSnakbar(message: String, view: View) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}