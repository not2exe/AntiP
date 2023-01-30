package com.gtime.general.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
interface FirebaseModule {
    companion object {
        @Provides
        @AppScope
        fun provideQueryTop(): Query =
            Firebase.firestore.collection(Constants.TOP_SCORES)
                .orderBy(Constants.SCORES, Query.Direction.DESCENDING)
        @Provides
        @AppScope
        fun provideAuthModule(): FirebaseAuth = Firebase.auth

    }

}
