package com.pawan.hirejetpack.data

import com.pawan.hirejetpack.domain.Job

/**
 * [JobRepository] — mock data source, shaped like a real one.
 *
 * Staff note: this function signature is intentionally what a real
 * repository would expose (e.g. `suspend fun getJobs(): List<Job>` backed
 * by Retrofit). Swapping the body for a network call later does not
 * require touching the ViewModel or UI at all — that's the Dependency
 * Inversion Principle (the "D" in SOLID) paying off in practice, not just
 * in theory.
 */
object JobRepository {
    fun getJobs(): List<Job> = listOf(
        Job("1", "Android Engineer", "Zenith Labs", "Bengaluru", "₹18–28 LPA", listOf("Kotlin", "Compose")),
        Job("2", "Staff Mobile Engineer", "Northwind", "Remote", "₹40–55 LPA", listOf("KMP", "Architecture")),
        Job("3", "Kotlin Multiplatform Dev", "OrbitPay", "Hyderabad", "₹22–32 LPA", listOf("KMP", "iOS")),
        Job("4", "Senior Android Developer", "Trailhead", "Pune", "₹25–35 LPA", listOf("Coroutines", "Hilt")),
        Job("5", "Android Team Lead", "Vertex", "Remote", "₹45–60 LPA", listOf("Leadership", "Compose")),
    )
}