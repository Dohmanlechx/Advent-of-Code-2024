import kotlin.math.abs

fun main(args: Array<String>) {
    d02a()
    d02b()
}

fun d02a() {
    val sum = Reader.input(2).count {
        val line = it.split(" ").map(String::toInt)
        checkLine(line)
    }
    require(sum == 559)
}

fun d02b() {
    val sum = Reader.input(2).count {
        val line = it.split(" ").map(String::toInt)
        (-1..<line.count()).any { i -> checkLine(line, removeAt = i) }
    }
    require(sum == 601)
}

private fun checkLine(line: List<Int>, removeAt: Int = -1): Boolean {
    var increasing: Boolean? = null

    val updatedLine = line.let {
        if (removeAt !in it.indices) it
        else it.take(removeAt) + it.drop(removeAt + 1)
    }

    for (i in 1..<updatedLine.size) {
        val curr = updatedLine[i]
        val prev = updatedLine[i - 1]
        if (curr == prev || abs(curr - prev) > 3) return false
        when {
            increasing == null -> increasing = curr > prev
            increasing == true && curr < prev -> return false
            increasing == false && curr > prev -> return false
        }
    }

    return true
}

/* Alternative solution, heavily inspired by ChatGPT
private fun checkLine(line: List<Int>, removeAt: Int = -1): Boolean {
    val differences = line
        .run {
            if (removeAt !in indices) this
            else take(removeAt) + drop(removeAt + 1)
        }
        .zipWithNext { a, b -> a - b }

    return when {
        differences.any { it == 0 || abs(it) > 3 } -> false
        else -> differences.all { diff -> (diff > 0) == differences.firstOrNull()?.let { it > 0 } }
    }
}
*/
