import kotlin.time.measureTime

fun main(args: Array<String>) {
    measureTime {
        d11a()
        d11b()
    }.let { println("d11 completed in $it") }
}

fun d11a() {
    var stones = Reader.input("d11")[0].split(" ").map(String::toLong)

    repeat(25) { repeat ->
        println("Iterating $repeat")
        stones = stones.flatMap { stone ->
            if (stone == 0L) {
                listOf(1L)
            } else if (stone.hasEvenNumberOfDigits()) {
                val digits = stone.toString().split("").filterNot { it.isBlank() }
                val result = listOf(
                    digits.subList(0, digits.size / 2).joinToString(""),
                    digits.subList(digits.size / 2, digits.size).joinToString("")
                ).map { it.toLong() }
                result
            } else {
                val result = listOf(stone * 2024)
                result
            }
        }
    }

    val sum = stones.count()
    require(sum == 217812)
}

fun d11b() {
    val lines = Reader.input("d11")
}

fun Long.hasEvenNumberOfDigits(): Boolean {
    return toString().split("").filterNot(String::isBlank).size % 2 == 0
}