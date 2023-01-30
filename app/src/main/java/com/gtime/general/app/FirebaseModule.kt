package com.gtime.general.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface FirebaseModule {
    companion object {
        @Provides
        @AppScope
        fun provideQueryTop(): Query =
            Firebase.firestore.collection(Constants.TOP_SCORES_COLLECTION)
                .orderBy(Constants.SCORES, Query.Direction.DESCENDING)

        @Provides
        @AppScope
        fun provideAuthModule(): FirebaseAuth = Firebase.auth

        @Provides
        @AppScope
        @Named(Constants.SHOP_COLLECTION)
        fun provideShopCollection(): CollectionReference =
            Firebase.firestore.collection(Constants.SHOP_COLLECTION)

        @Provides
        @AppScope
        @Named(Constants.COINS_STORAGE_COLLECTION)
        fun provideCoinsStorageCollection(): CollectionReference =
            Firebase.firestore.collection(Constants.COINS_STORAGE_COLLECTION)

        @Provides
        @AppScope
        @Named(Constants.PROMO_STORAGE_COLLECTION)
        fun providePromoStorageCollection(): CollectionReference =
            Firebase.firestore.collection(Constants.PROMO_STORAGE_COLLECTION)




    }

}
