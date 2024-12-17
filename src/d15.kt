import kotlin.time.measureTime

private const val WALL = '#'
private const val BOX = 'O'
private const val ROBOT = '@'
private var room = mutableListOf<String>()
private val walls = mutableSetOf<Vector2>()
private val boxes = mutableSetOf<Vector2>()

fun main(args: Array<String>) {
    measureTime {
        d15a()
        d15b()
    }.let { println("d15 completed in $it") }
}

fun d15a() {
    val lines = Reader.input("d15", split = "\n\n")
    val moves = lines[1].map { mapOf('<' to Dir.W, '^' to Dir.N, '>' to Dir.E, 'v' to Dir.S)[it] }.filterNotNull()
    val robot = Pusher()

    room = lines[0].split("\n").toMutableList()

    for (y in room.indices) {
        for (x in 0..<room[0].length) {
            when (room[y][x]) {
                WALL -> walls.add(Vector2(x, y))
                BOX -> boxes.add(Vector2(x, y))
                ROBOT -> robot.p = Vector2(x, y)
            }
        }
    }

    for (move in moves) {
        robot.advance(move)
    }

    printRoom(robot)

    val sum = boxes.fold(0) { acc, it -> acc + 100 * it.y + it.x }
    require(sum == 1492518)
}

fun d15b() {
    val lines = Reader.input("d15")
}

private fun peek(p: Vector2, f: Dir, char: Char): Boolean {
    val peek = Vector2(p.x + f.x, p.y + f.y)
    return when (char) {
        WALL -> walls.any { it == peek }
        BOX -> boxes.any { it == peek }
        else -> false
    }
}

private fun pushBox(p: Vector2, f: Dir): Boolean {
    fun update() {
        val stone = boxes.first { it == p }
        boxes.remove(stone)
        boxes.add(stone.offset(f))
    }

    when (f) {
        Dir.E, Dir.S, Dir.W, Dir.N -> {
            if (peek(p, f, WALL)) return false
            if (peek(p, f, BOX)) {
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

private fun printRoom(robot: Pusher) {
    for (y in room.indices) {
        for (x in 0..<room[0].length) {
            if (x == 0 || y == 0 || walls.any { it == Vector2(x, y) }) {
                print(WALL)
                continue
            }
            if (boxes.any { it == Vector2(x, y) }) {
                print(BOX)
                continue
            }
            if (robot.p == Vector2(x, y)) {
                print(ROBOT)
                continue
            }
            print(".")
        }
        println()
    }
}

private data class Pusher(var p: Vector2 = Vector2(0, 0)) {
    fun advance(f: Dir) {
        if (peek(p, f, WALL)) return
        if (peek(p, f, BOX)) {
            val pushed = pushBox(p.offset(f), f)
            if (pushed) p = p.offset(f)
            return
        }
        p = p.offset(f)
    }
}