package com.codelog.arke.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelog.arke.api.User

class HomeViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>().apply {
        value = listOf(User("Joe"), User("Jeff"))
    }

   val users: LiveData<List<User>> = _users
}