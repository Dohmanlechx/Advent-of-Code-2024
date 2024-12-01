import kotlin.math.abs

fun main(args: Array<String>) {
    val a = mutableListOf<Int>()
    val b = mutableListOf<Int>()
    for (line in Reader.input(1)) {
        a.add(line.split(" ")[0].toInt())
        b.add(line.split(" ")[1].toInt())
    }
    a.sort()
    b.sort()

    d01a(a, b)
    d01b(a, b)
}

fun d01a(a: List<Int>, b: List<Int>) {
    val sum = a.foldIndexed(0) { index, acc, i ->
        acc + abs(i - b[index])
    }
    require(sum == 1341714)
}

fun d01b(a: List<Int>, b: List<Int>) {
    val sum = a.fold(0) { acc, i ->
        acc + i * b.count { it == i }
    }
    require(sum == 27384707)
}