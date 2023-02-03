package com.gtime.online_mode.ui.logic

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@AppScope
class TopScoresRepository @Inject constructor(
    @Named(Constants.TOP_SCORES_COLLECTION) private val topScoresRef: CollectionReference,
    private val auth: FirebaseAuth,
) {
    suspend fun updateForCurrentUser(scores: Int): Boolean {
        val email = auth.currentUser?.email ?: return false
        topScoresRef.document(email).set(
            hashMapOf(Constants.SCORES to scores), SetOptions.merge()
        ).await()
        return true
    }
}