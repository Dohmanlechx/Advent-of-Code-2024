const val DO = "do()"
const val DONT = "don't()"

fun main(args: Array<String>) {
    d03a()
    d03b()
}

fun d03a() {
    val sum = Reader.input("d03", split = null)[0].mul()
    require(sum == 157621318)
}

fun d03b() {
    var line = Reader.input("d03", split = null)[0]
    var enabled = ""

    while (line.contains(DONT)) {
        enabled += line.substringBefore(DONT)
        line = line
            .substringAfter(DONT)
            .substringAfter(DO, missingDelimiterValue = "")
    }

    val sum = enabled.mul()
    require(sum == 79845780)
}

private fun String.mul(): Int =
    let { Regex("""mul\(\d+,\d+\)""").findAll(it).map(MatchResult::value) }
        .sumOf {
            val (a, b) = it.substringAfter("(").substringBefore(")").split(",").map(String::toInt)
            a * b
        }