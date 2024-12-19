import kotlin.time.measureTime

val input = Reader.input("d19", split = "\n\n")
val patterns = input[0].split(", ")
val designs = input[1].split("\n")
val impossibleDesigns = mutableSetOf<String>()

fun main(args: Array<String>) {
    measureTime {
        d19a()
        d19b()
    }.let { println("d19 completed in $it") }
}

fun d19a() {
    val sum = designs.count(::validate)
    require(sum == 280)
}

fun d19b() {
    val lines = Reader.input("d19")
}

private fun validate(design: String): Boolean {
    if (design.isEmpty()) return true
    if (impossibleDesigns.any { it == design }) return false
    for (p in patterns.filter { design.startsWith(it) }) {
        if (validate(design.substring(p.length))) return true
    }
    impossibleDesigns.add(design)
    return false
}