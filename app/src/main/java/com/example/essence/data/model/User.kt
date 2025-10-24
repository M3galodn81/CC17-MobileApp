package com.example.essence.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

/**
 * Represents the logged-in user.
 * This is the central "source of truth" for employee data.
 */
data class User(
    // --- Core Identity & App Access ---

    /** This is the primary key. It links to `employeeId` in your PayslipData. */
    val id: String,

    /** Log-in credentials */
    val email: String,

    // FOR TESTING PURPOSES
    // REMOVE THIS FOR RELEASE OR PRODUCTION
    val password: String,

    /** * The user's role, used for Role-Based Access Control (RBAC).
     * This determines if they see manager features.
     */
    val role: UserRole,

    // --- Employment Details ---

    /** This user's job title, e.g., "Android Developer" */
    val position: String,

    /** This user's team, e.g., "Mobile" */
    val department: String,

    val hireDate: LocalDate,
    val managerId: String? = null, // The 'id' of this user's manager

    // --- Personal Information ---
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val birthDate: LocalDate? = null,

    // --- Government & Payroll IDs ---
    val tin: String? = null,         // For Tax
    val sssNumber: String? = null,    // For SSS
    val philHealthNumber: String? = null, // For PhilHealth
    val pagIbigNumber: String? = null   // For Pag-Ibig
) {

    val fullName: String
        get() = "$firstName $lastName"


    @RequiresApi(Build.VERSION_CODES.O)
    val payslips = com.example.essence.data.sample.payslips

}

