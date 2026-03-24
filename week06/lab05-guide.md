# **Lab Guide: Reflection and Annotations**

## **Objectives**

* Use annotations to customize JSON serialization behavior.
* Apply Kotlin Reflection to inspect annotations at runtime.
* Implement pluggable, type-agnostic custom formatters.

---

## **Introduction**

In the previous lab, you implemented a basic JSON serializer using the **Kotlin
Reflection API**, centered around the function:

```kotlin
memberToJson(...)
```

This lab extends that implementation by introducing **annotations** to:

1. Customize JSON property names.
2. Override how specific values are serialized.

---

## **Part 1: Custom Property Names**

### **Goal**

Allow renaming properties in the generated JSON using annotations.

### **Expected Behavior**

If a property is annotated with `@ToJsonPropName`, its value should be used as
the JSON key instead of the original property name.

### **Example**

```kotlin
data class Person(
   @ToJsonPropName("first_name") val firstName: String,
   @ToJsonPropName("last_name") val lastName: String,
   val age: Int
)
```

### **Expected JSON Output**

```json
{
   "first_name": "John",
   "last_name": "Doe",
   "age": 30
}
```

### **Implementation Notes**

* Use Kotlin Reflection (`KProperty`) to inspect annotations.
* If the annotation is present, then use its value.
* Otherwise, fallback to the property name.

---

## **Part 2: Custom Value Formatters**

### **Goal**

Override how specific property values are serialized.

---

### **Problem**

Consider a `Student` class with an `enrollmentDate` property of type `LocalDate`.
By default, complex objects (e.g., `LocalDate`) are serialized recursively:

```json
{
   ...,
  "enrollmentDate": { "day": 15, "month": 3, "year": 2023 }
}
```

Now the goal is to change this behavior and instead of serializing the
enrollmentDate as an object, we want to serialize it as a string in the format
"yyyy-MM-dd". The JSON output should look like this:

```json
{
   ...,
  "enrollmentDate": "2023-03-15"
}
```

### ⚠️ **Important Design Requirement**

Your solution must be:

* **Generic** → works for any type (**NOT just** `LocalDate`)
* **Pluggable** → formatter is provided via annotation
* **Reflection-based** → formatter instantiated dynamically

---

### **Formatter Contract**

A formatter must implement:

```kotlin
(Any) -> String
```

#### Example

```kotlin
class DateFormatter : (Any) -> String {
    override fun invoke(value: Any): String {
        val date = value as LocalDate
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}
```

---

### **Usage Example**

```kotlin
data class Student(
   val name: String,
   val age: Int,
   @ToJsonFormatter(DateFormatter::class)
   val enrollmentDate: LocalDate
)
```

---

### **Expected Output**

```json
{
  "name": "ZE",
  "age": 20,
  "enrollmentDate": "2023-03-15"
}
```

To that end, you will need to implement a custom annotation `ToJsonFormatter` that
takes a class as a parameter.
This class should implement a function that takes an `Any` type (the value of
the annotated property) and returns a `String` representation of that value in
the desired format.

Members can be annotated with `@ToJsonFormatter`, which specifies a class
implementing a function `(Any) -> String`. This function provides an alternative
JSON representation of the member's value, processing it.

For example, if a property is annotated with
`@ToJsonFormatter(DateFormatter::class)`, the DateFormatter class will be used
to format the property's value into a JSON string.

You should implement a different unit test for a different kind of formatter.