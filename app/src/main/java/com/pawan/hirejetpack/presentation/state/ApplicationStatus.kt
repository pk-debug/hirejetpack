package com.pawan.hirejetpack.presentation.state

/**
 * [ApplicationStatus] — tracks the user's relationship to *this specific*
 * job on the detail screen, separate from the job data itself.
 *
 * Staff note: this is nested inside [JobDetailUiState.Found] rather than
 * being a lone `isApplying: Boolean` flag. A boolean can't cleanly
 * represent "submitting" vs. "applied" as mutually exclusive — you'd need
 * a second flag, and now two booleans can combine into a state that
 * should never exist (`isApplying = true` AND `isApplied = true`). A
 * sealed interface makes that invalid combination impossible to construct
 * in the first place, instead of just "unlikely if everyone remembers the
 * rule."
 */
sealed interface ApplicationStatus {
    object Idle : ApplicationStatus
    object Submitting : ApplicationStatus
    object Applied : ApplicationStatus
}