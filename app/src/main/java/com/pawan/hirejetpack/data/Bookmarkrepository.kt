package com.pawan.hirejetpack.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [BookmarkRepository] — single source of truth for which job ids the
 * user has bookmarked.
 *
 * Staff note: this is an `object` (singleton), same shape as
 * [JobRepository]. Both [com.pawan.hirejetpack.presentation.state.HomeViewModel]
 * and [com.pawan.hirejetpack.presentation.state.JobDetailViewModel] read
 * from this ONE [bookmarkedIds] flow — that's what makes a bookmark
 * toggled on the detail screen instantly visible on the Home feed too,
 * with no manual syncing between the two ViewModels. In a real app this
 * object's internals would be backed by Room/DataStore instead of an
 * in-memory Set, but nothing in either ViewModel would need to change —
 * they only depend on this public Flow-based contract.
 */
object BookmarkRepository {
    private val _bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

    fun toggleBookmark(jobId: String) {
        _bookmarkedIds.value = if (jobId in _bookmarkedIds.value) {
            _bookmarkedIds.value - jobId
        } else {
            _bookmarkedIds.value + jobId
        }
    }
}