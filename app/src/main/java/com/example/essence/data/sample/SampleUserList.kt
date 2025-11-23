package com.example.essence.data.sample

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.essence.data.model.User
import com.example.essence.data.model.UserRole
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
object SampleData {

    // --- USER 1: A MANAGER ---
    val managerUser = User(
        id = "101",
        email = "user@example.com",
        password = "Password", // For testing
        role = UserRole.MANAGER,
        position = "General Manager",
        department = "Management",
        hireDate = LocalDate.of(2018, 5, 10),
        managerId = null, // Top-level, no manager
        firstName = "Alex",
        lastName = "Reyes",
        middleName = "Cruz",
        phone = "09171234567",
        address = "123 Ayala Ave, Makati City",
        birthDate = LocalDate.of(1985, 3, 15),
        tin = "123-456-789-000",
        sssNumber = "34-1234567-8",
        philHealthNumber = "01-0123456789-0",
        pagIbigNumber = "1234-5678-9012"
    )

    val payrollUser = User(
        id = "110",
        email = "user@example.com",
        password = "Password1", // For testing
        role = UserRole.PAYROLL_OFFICER,
        position = "Payroll Officer",
        department = "Management",
        hireDate = LocalDate.of(2018, 5, 10),
        managerId = null, // Top-level, no manager
        firstName = "Alex",
        lastName = "Reyes",
        middleName = "Cruz",
        phone = "09171234567",
        address = "123 Ayala Ave, Makati City",
        birthDate = LocalDate.of(1985, 3, 15),
        tin = "123-456-789-000",
        sssNumber = "34-1234567-8",
        philHealthNumber = "01-0123456789-0",
        pagIbigNumber = "1234-5678-9012"
    )

    // --- USER 2: AN EMPLOYEE ---
    val employeeUser = User(
        id = "201",
        email = "ben.santos@ess.com",
        password = "employee123", // For testing
        role = UserRole.EMPLOYEE,
        position = "Android Developer",
        department = "Engineering",
        hireDate = LocalDate.of(2022, 8, 20),
        managerId = "101", // Reports to Alex
        firstName = "Ben",
        lastName = "Santos",
        middleName = null, // Test nullability
        phone = "09287654321",
        address = "456 BGC, Taguig City",
        birthDate = LocalDate.of(1995, 11, 2),
        tin = "987-654-321-000",
        sssNumber = "34-8765432-1",
        philHealthNumber = "02-0987654321-0",
        pagIbigNumber = "9012-3456-7890"
    )

    // --- USER 3: AN ADMIN ---
    val adminUser = User(
        id = "301",
        email = "carla.david@ess.com",
        password = "admin123", // For testing
        role = UserRole.ADMIN,
        position = "HR Specialist",
        department = "Human Resources",
        hireDate = LocalDate.of(2020, 2, 14),
        managerId = "101", // Also reports to Alex
        firstName = "Carla",
        lastName = "David",
        middleName = "Perez",
        phone = null, // Test nullability
        address = "789 Ortigas Center, Pasig City",
        birthDate = LocalDate.of(1992, 7, 30),
        tin = "111-222-333-000",
        sssNumber = "34-1112223-4",
        philHealthNumber = "03-0111222333-0",
        pagIbigNumber = "1111-2222-3333"
    )

    // --- A list of all users for convenience ---
    val allUsers = listOf(managerUser, employeeUser, adminUser, payrollUser)

    /**
     * A map to quickly find a user by their email,
     * simulating a database lookup.
     */
    val userMapByEmail = allUsers.associateBy { it.email }
    val userMapById = allUsers.associateBy { it.id }

}