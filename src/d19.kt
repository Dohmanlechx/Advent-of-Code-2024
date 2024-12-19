import kotlin.time.measureTime

private val input = Reader.input("d19", split = "\n\n")
private val patterns = input[0].split(", ")
private val designs = input[1].split("\n")
private val cache = mutableMapOf<String, Long>()

fun main(args: Array<String>) {
    measureTime {
       d19a()
       d19b()
    }.let { println("d19 completed in $it") }
}

fun d19a() {
    val sum = designs.count(::p1)
    require(sum == 280)
}

fun d19b() {
    val sum = designs.fold(0L) { acc, it -> acc + p2(it, 0L) }
    require(sum == 606411968721181)
}

private fun p1(d: String): Boolean {
    cache[d]?.let { return false }
    if (d.isEmpty()) return true
    for (p in patterns.filter { d.startsWith(it) }) {
        if (p1(d.substring(p.length))) return true
    }
    cache.putIfAbsent(d, 0)
    return false
}

private fun p2(d: String, s: Long): Long {
    cache[d]?.let { return it }
    var sum = s
    if (d.isEmpty()) return sum + 1L
    for (p in patterns.filter { d.startsWith(it) }) {
        val temp = d.substring(p.length)
        val res = p2(temp, s)
        cache.putIfAbsent(temp, res)
        sum += res
    }
    return sum
}