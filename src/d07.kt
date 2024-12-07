fun main(args: Array<String>) {
    d07a()
    d07b()
}

fun d07a() {
    val calibrations = Reader.input("d07")

    val sum = calibrations.fold(0L) { acc, it ->
        val result = it.substringBefore(":").toLong()
        val numbers = it.substringAfter(": ").split(" ").map(String::toLong)
        acc + if (isCalibrationValid(result, numbers)) result else 0L
    }

    println(sum)
    // sum is most likely 945512582195 (3749 for example input data)
    // won't submit as I looked up the solution online - recursion is my weakness
}

fun d07b() {
    val lines = Reader.input("d07")
}

private fun isCalibrationValid(result: Long, numbers: List<Long>): Boolean {
    // Base case
    if (numbers.size == 1) {
        return numbers[0] == result
    }
    // Impossible if more than 1 and already higher than result
    if (numbers[0] > result) {
        return false
    }
    // Create copy of the numbers, and delete the first two numbers for either addition or multiplication
    val numbersCopy = numbers.toMutableList()
    val num1 = numbersCopy.removeAt(0)
    val num2 = numbersCopy.removeAt(0)
    // Try addition
    numbersCopy.add(0, num1 + num2)
    if (isCalibrationValid(result, numbersCopy)) {
        return true
    }
    numbersCopy.removeAt(0)
    // Try multiplication
    numbersCopy.add(0, num1 * num2)
    if (isCalibrationValid(result, numbersCopy)) {
        return true
    }
    // Addition and multiplication didn't work
    return false
}