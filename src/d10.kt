import kotlin.time.measureTime

private val possibleDirections = listOf(Dir.N, Dir.E, Dir.S, Dir.W)

fun main(args: Array<String>) {
    measureTime {
        val map = Reader.input("d10")
        val zeroes = mutableSetOf<Vector2>()

        for (x in 0..<map[0].count()) {
            for (y in 0..<map.count()) {
                map[y][x].let { if (it == '0') zeroes.add(Vector2(x, y)) }
            }
        }

        val p1 = zeroes.fold(0) { acc, v2 -> acc + map.trailhead(v2, visited = mutableSetOf()) }
        val p2 = zeroes.fold(0) { acc, v2 -> acc + map.trailhead(v2, visited = null) }

        require(p1 == 482)
        require(p2 == 1094)
    }.let { println("d10 completed in $it") }
}

private fun List<String>.peek(v2: Vector2, dir: Dir): Int? =
    getOrNull(v2.offset(dir).y)?.getOrNull(v2.offset(dir).x)?.digitToIntOrNull()

private fun List<String>.trailhead(
    v2: Vector2,
    visited: MutableSet<Vector2>?,
    score: Int = 0,
    next: Int = 1,
): Int {
    fun roadsTo(target: Int) = possibleDirections.filter { peek(v2, it) == target }

    if (roadsTo(next).isEmpty()) {
        return 0
    }

    if (next != 9) {
        return roadsTo(next).run {
            if (isEmpty()) 0
            else score + fold(score) { acc, it -> acc + trailhead(v2.offset(it), visited, score, next + 1) }
        }
    }

    return roadsTo(9).run {
        if (isEmpty()) {
            0
        } else {
            if (visited == null) {
                count()
            } else {
                count {
                    if (visited.contains(v2.offset(it))) {
                        false
                    } else {
                        visited.add(v2.offset(it))
                        true
                    }
                }
            }
        }
    }
}