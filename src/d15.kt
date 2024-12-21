import kotlin.time.measureTime

private const val WALL_P1 = '#'
private const val BOX_P1 = 'O'
private const val ROBOT_P1 = '@'
private var room_p1 = mutableListOf<String>()
private val walls_P1 = mutableSetOf<Vector2>()
private val boxes_P1 = mutableSetOf<Vector2>()

private var room_p2 = mutableListOf<String>()
private val walls_P2 = mutableSetOf<Pair<Vector2, Vector2>>()
private val boxes_P2 = mutableSetOf<Pair<Vector2, Vector2>>()

fun main(args: Array<String>) {
    measureTime {
        // d15a()
        d15b()
    }.let { println("d15 completed in $it") }
}

fun d15a() {
    val lines = Reader.input("d15", split = "\n\n")
    val moves = lines[1].map { mapOf('<' to Dir.W, '^' to Dir.N, '>' to Dir.E, 'v' to Dir.S)[it] }.filterNotNull()
    val robot = Pusher()

    room_p1 = lines[0].split("\n").toMutableList()

    for (y in room_p1.indices) {
        for (x in 0..<room_p1[0].length) {
            when (room_p1[y][x]) {
                WALL_P1 -> walls_P1.add(Vector2(x, y))
                BOX_P1 -> boxes_P1.add(Vector2(x, y))
                ROBOT_P1 -> robot.p = Vector2(x, y)
            }
        }
    }

    for (move in moves) {
        robot.advance(move)
    }

    printRoom(robot)

    val sum = boxes_P1.fold(0) { acc, it -> acc + 100 * it.y + it.x }
    require(sum == 1492518)
}

fun d15b() {
    val lines = Reader.example("d15", split = "\n\n")
    val moves = lines[1].map { mapOf('<' to Dir.W, '^' to Dir.N, '>' to Dir.E, 'v' to Dir.S)[it] }.filterNotNull()
    val robot = Pusher2()

    room_p2 = lines[0].split("\n").map {
        it.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.")
    }.toMutableList()

    for (y in room_p2.indices) {
        for (x in 0..<room_p2[0].length step 2) {
            print(room_p2[y][x])
            print(room_p2[y][x + 1])
            when (room_p2[y][x]) {
                '#' -> walls_P2.add(Pair(Vector2(x, y), Vector2(x + 1, y)))
                '[' -> boxes_P2.add(Pair(Vector2(x, y), Vector2(x + 1, y)))
                '@' -> robot.p = Vector2(x, y)
            }
        }
        println()
    }

    for (move in moves.take(1)) {
        robot.advance(move)
    }

    printRoom2(robot)

    //val sum = boxes_P2.fold(0) { acc, it -> acc + 100 * it.y + it.x }
}

private fun peek(p: Vector2, f: Dir, char: Char): Boolean {
    val peek = Vector2(p.x + f.x, p.y + f.y)
    return when (char) {
        WALL_P1 -> walls_P1.any { it == peek }
        BOX_P1 -> boxes_P1.any { it == peek }
        else -> false
    }
}

private fun pushBox(p: Vector2, f: Dir): Boolean {
    fun update() {
        val stone = boxes_P1.first { it == p }
        boxes_P1.remove(stone)
        boxes_P1.add(stone.offset(f))
    }

    when (f) {
        Dir.E, Dir.S, Dir.W, Dir.N -> {
            if (peek(p, f, WALL_P1)) return false
            if (peek(p, f, BOX_P1)) {
                val pushed = pushBox(p.offset(f), f)
                if (pushed) update()
                return pushed
            } else {
                update()
                return true
            }
        }

        else -> throw NotImplementedError()
    }
}

private fun printRoom2(robot: Pusher2) {
    for (y in room_p2.indices) {
        for (x in 0..<room_p2[0].length step 2) {
            if (x == 0 || y == 0 || walls_P2.any { it.first == Vector2(x, y) }) {
                print("##")
                continue
            }
            if (boxes_P2.any { it.first == Vector2(x, y) }) {
                print("[]")
                continue
            }
            if (robot.p == Vector2(x, y)) {
                print("@.")
                continue
            }
            print("..")
        }
        println()
    }
}

private fun printRoom(robot: Pusher) {
    for (y in room_p1.indices) {
        for (x in 0..<room_p1[0].length) {
            if (x == 0 || y == 0 || walls_P1.any { it == Vector2(x, y) }) {
                print(WALL_P1)
                continue
            }
            if (boxes_P1.any { it == Vector2(x, y) }) {
                print(BOX_P1)
                continue
            }
            if (robot.p == Vector2(x, y)) {
                print(ROBOT_P1)
                continue
            }
            print(".")
        }
        println()
    }
}

private data class Pusher(var p: Vector2 = Vector2(0, 0)) {
    fun advance(f: Dir) {
        if (peek(p, f, WALL_P1)) return
        if (peek(p, f, BOX_P1)) {
            val pushed = pushBox(p.offset(f), f)
            if (pushed) p = p.offset(f)
            return
        }
        p = p.offset(f)
    }
}

private data class Pusher2(var p: Vector2 = Vector2(0, 0)) {
    fun advance(f: Dir) {
        if (peek2(p, f, WALL_P1)) return
        if (peek2(p, f, '[')) {
            val pushed = pushBox2(p.offset(f), f)
            if (pushed) p = p.offset(f)
            return
        } else if (peek2(p, f, ']')) {
            val pushed = pushBox2(p, f)
            if (pushed) p = p.offset(f)
            return
        }
        p = p.offset(f)
    }
}

private fun pushBox2(p: Vector2, f: Dir): Boolean {
    fun update() {
        val stone = boxes_P2.first { it.first == p || it.second == p }
        boxes_P2.remove(stone)
        boxes_P2.add(stone.offset(f))
    }

    when (f) {
        Dir.E -> {
            if (peek2(p, f, '#')) return false
            if (peek2(p, f, '[')) {
                val pos = p.offset(f)
                val pushed = pushBox2(pos.offset(f), f) && pushBox2(Vector2(pos.offset(f).x + 1, pos.y), f)
                if (pushed) update()
                return pushed
            } else {
                update()
                return true
            }
        }

        Dir.W -> {
            if (peek2(p, f, '#')) return false
            if (peek2(p, f, ']')) {
                val pos = p.offset(f)
                val pushed = pushBox2(pos.offset(f), f)
                if (pushed) update()

                // pushat fint , men update() triggar igen
                return pushed
            } else {
                update()
                return true
            }
        }

        Dir.N -> {
            if (peek2(p, f, '#')) return false
            if (peek2(p, f, ']') || peek2(p, f, '[')) {
                val pos = p.offset(f)
                val pushed = pushBox2(pos.offset(f), f) && pushBox2(Vector2(pos.x, pos.offset(f).y + 1), f)
                if (pushed) update()
                return pushed
            } else {
                update()
                return true
            }
        }

        Dir.S -> {
            if (peek2(p, f, '#')) return false
            if (peek2(p, f, ']') || peek2(p, f, '[')) {
                val pos = p.offset(f)
                val pushed = pushBox2(pos.offset(f), f) && pushBox2(Vector2(pos.x, pos.offset(f).y -1), f)
                if (pushed) update()
                return pushed
            } else {
                update()
                return true
            }
        }

        else -> throw NotImplementedError()
    }
}

private fun peek2(p: Vector2, f: Dir, char: Char): Boolean {
    val peek = Vector2(p.x + f.x, p.y + f.y)
    return when (char) {
        '#' -> walls_P2.any { it.first == peek || it.second == peek }
        '[' -> boxes_P2.any { it.first == peek }
        ']' -> boxes_P2.any { it.second == peek }
        else -> false
    }
}

private fun Pair<Vector2, Vector2>.offset(dir: Dir) = Pair(
    Vector2(first.x + dir.x, first.y + dir.y),
    Vector2(second.x + dir.x, second.y + dir.y),
)