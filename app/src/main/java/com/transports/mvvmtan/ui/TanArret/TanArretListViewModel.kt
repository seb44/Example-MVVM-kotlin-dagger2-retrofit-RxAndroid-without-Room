package com.transports.mvvmtan.ui.TanArret

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.transports.mvvmtan.R
import com.transports.mvvmtan.base.BaseViewModel
import com.transports.mvvmtan.model.TanArret
import com.transports.mvvmtan.network.TanApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TanArretListViewModel : BaseViewModel() {

    @Inject
    lateinit var tanApi: TanApi

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadTanArrets() }

    val listTanArrets: MutableLiveData<List<TanArret>> = MutableLiveData()

    init {
        loadTanArrets()
    }

    private fun loadTanArrets() {

        Log.d("MVVMTAN","TanArretListViewModel - Démarrage de loadTanArrets")

        subscription = tanApi.getTanStops()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveTanArretListStart() }
            .doOnTerminate { onRetrieveTanArretListFinish() }
            .subscribe(
                {listeDesArrets -> onRetrieveTanArretListSuccess(listeDesArrets) },
                { onRetrieveTanArretListError(it) }
            )
    }



    private fun onRetrieveTanArretListStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrieveTanArretListFinish() {
        loadingVisibility.value = View.GONE
    }


    private fun onRetrieveTanArretListSuccess(listeDesArrets: List<TanArret>?) {
        listTanArrets.value = listeDesArrets
        // equivalent à listTanArrets.setValue(listeDesArrets)
    }

    private fun onRetrieveTanArretListError(error: Throwable) {
        errorMessage.value = R.string.tanarret_error
    }


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}