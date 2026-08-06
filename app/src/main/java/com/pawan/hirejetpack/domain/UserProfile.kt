package com.pawan.hirejetpack.domain

// ============================================================================
// SECTION 1: OOP DATA MODELS & ABSTRACTION (Domain Layer)
// ============================================================================

/**
 * [UserProfile] Data Model.
 *
 * OOP Concept: Encapsulation
 * Groups related user attributes into a single, immutable object.
 */
data class UserProfile(
    val name: String,
    val email: String,
    val role: String
)
