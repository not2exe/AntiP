package com.gtime.online_mode.data


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.TopScoresModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named


class TopScoresSource (
    private val query: Query,
) :
    PagingSource<QuerySnapshot, TopScoresModel>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, TopScoresModel>): QuerySnapshot? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }


    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, TopScoresModel> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleModel = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleModel).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(TopScoresModel::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}