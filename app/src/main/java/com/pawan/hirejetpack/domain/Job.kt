package com.pawan.hirejetpack.domain

/**
 * [Job] Data Model.
 *
 * Noob note: this is just a plain data holder for one job listing.
 * Staff note: keeping this a pure `data class` with no Android imports means
 * it can live in a `domain`/`core-model` module later without dragging
 * framework dependencies with it (see multi-module dependency rules).
 */
data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val salary: String,
    val tags: List<String>
)