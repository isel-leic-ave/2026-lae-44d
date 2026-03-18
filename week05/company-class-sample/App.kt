// Level 3: The individual item
class Employee(
    val id: Int,
    val name: String
)

// Level 2: Contains a reference type AND an Iterable
class Department(
    val code: String,

    val managerName: String,         // Reference type (String)
    val staff: Iterable<Employee>    // Iterable of another class
)

// Level 1: The top-level reference
class Company(
    val name: String,
    val headquarters: String,
    val primaryDept: Department      // Reference to the Department class
)

fun main() {
    val employees = listOf(Employee(101, "Alice"), Employee(102, "Bob"))
    val engineering = Department("ENG-01", "Charlie", employees)
    val techCorp = Company("TechCorp", "Lisbon", engineering)

    println("Company: ${techCorp.name}")
    println("Manager: ${techCorp.primaryDept.managerName}")

    // Accessing the nested Iterable
    techCorp.primaryDept.staff.forEach { println(" - Employee: ${it.name}") }
}