import kotlin.time.measureTime

private val possibleDirections = listOf(Dir.N, Dir.E, Dir.S, Dir.W)
private val walls = mutableSetOf<Vector2>()
private lateinit var room: List<String>
private var goal = Vector2(0, 0)

fun main(args: Array<String>) {
    measureTime {
        d16a()
        d16b()
    }.let { println("d16 completed in $it") }
}

fun d16a() {
    room = Reader.example("d16")
    val reindeer = Reindeer()

    for (y in room.indices) {
        for (x in 0..<room[0].length) {
            when (room[y][x]) {
                '#' -> walls.add(Vector2(x, y))
                'S' -> reindeer.p = Vector2(x, y)
                'E' -> goal = Vector2(x, y)
            }
        }
    }

    printRoom(room, reindeer, goal)

}

fun d16b() {
    val lines = Reader.input("d16")
}

private fun printRoom(room: List<String>, reindeer: Reindeer, goal: Vector2) {
    for (y in room.indices) {
        for (x in 0..<room[0].length) {
            if (x == 0 || y == 0 || walls.any { it == Vector2(x, y) }) {
                print('#')
                continue
            }
            if (reindeer.p == Vector2(x, y)) {
                print('S')
                continue
            }
            if (goal == Vector2(x, y)) {
                print('E')
                continue
            }
            print(".")
        }
        println()
    }
}

private fun List<String>.peek(v2: Vector2, dir: Dir): Char? =
    getOrNull(v2.offset(dir).y)?.getOrNull(v2.offset(dir).x)

private fun peek(p: Vector2, f: Dir, char: Char): Boolean {
    val peek = Vector2(p.x + f.x, p.y + f.y)
    return when (char) {
        '#' -> walls.any { it == peek }
        'E' -> goal == peek
        else -> false
    }
}

data class Reindeer(
    var p: Vector2 = Vector2(0, 0),
    var f: Dir = Dir.E,
) {
    private val visited: MutableSet<Pair<Vector2, Dir>> = mutableSetOf()

    fun advance(score: Int, hasOtherOptions: Boolean): Int? {
        if (possibleDirections.all { room.peek(p, f) == '#' || visited.contains(Pair(p.offset(f), f)) }) {
            return null
        }
        if (room.peek(p, f) == 'E') {
            p = p.offset(f)
            return score + 1
        }
        if (room.peek(p, f) == '#' || visited.contains(Pair(p.offset(f), f))) {
            // have to turn

        }

        return null

    }

    private fun turnClockwise(): Dir {
        return mapOf(
            Dir.N to Dir.E,
            Dir.E to Dir.S,
            Dir.S to Dir.W,
            Dir.W to Dir.N
        )[f]!!
    }

    private fun turnCounterClockwise(): Dir {
        return mapOf(
            Dir.N to Dir.W,
            Dir.W to Dir.S,
            Dir.S to Dir.E,
            Dir.E to Dir.N
        )[f]!!
    }
}