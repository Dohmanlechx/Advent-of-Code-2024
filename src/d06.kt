import kotlin.time.measureTime

fun main(args: Array<String>) {
    measureTime {
        val lab = Reader.input("d06")
        val guardPath = d06a(lab)
        d06b(lab, guardPath)
    }.let { println("d06 completed in $it") }
}

fun d06a(lab: List<String>): List<Movement> {
    val guard = Guard().apply { setPosition(lab) }
    while (true) guard.peek(lab)?.let { guard.run { if (it == '#') turn() else advance() } } ?: break
    val sum = guard.visited.distinctBy(Movement::pos).count()
    require(sum == 5095)
    return guard.visited.drop(1)
}

fun d06b(lab: List<String>, guardPath: List<Movement>) {
    var loops = 0
    guardPath
        .distinctBy(Movement::pos)
        .forEach {
            val guard = Guard().apply { setPosition(lab) }
            val labWithObstacle = lab.withObstacle(it.pos)
            while (true) {
                guard.peek(labWithObstacle)?.let {
                    guard.run {
                        if (it == '#') turn() else advance()
                        if (guard.isStuckInLoop) {
                            loops++
                            return@forEach
                        }
                    }
                } ?: break
            }
        }
    require(loops == 1933)
}

private fun List<String>.withObstacle(obstaclePos: Vector2): List<String> =
    mapIndexed { y, row ->
        if (y != obstaclePos.y) row
        else row.toCharArray().apply { this[obstaclePos.x] = '#' }.concatToString()
    }

data class Vector2(val x: Int, val y: Int) {
    fun offset(dir: Dir) = Vector2(x + dir.x, y + dir.y)
    fun offset(v2: Vector2) = Vector2(x + v2.x, y + v2.y)
}
data class Movement(val pos: Vector2, val dir: Dir)

private class Guard {
    private var facing: Dir = Dir.N
    private var y: Int = 0
    private var x: Int = 0
    var isStuckInLoop = false
    val visited: MutableSet<Movement> = mutableSetOf()

    fun setPosition(grid: List<String>) {
        for (y in 0..<grid.count()) {
            for (x in 0..<grid[y].count()) {
                if (grid[y][x] == '^') {
                    visited.add(Movement(Vector2(x, y), facing))
                    this.y = y
                    this.x = x
                    break
                }
            }
        }
    }

    fun turn() {
        facing = mapOf(Dir.N to Dir.E, Dir.E to Dir.S, Dir.S to Dir.W, Dir.W to Dir.N)[facing]!!
        val movement = Movement(Vector2(x, y), facing)
        isStuckInLoop = movement in visited
        visited.add(movement)
    }

    fun advance() {
        y += facing.y
        x += facing.x
        val movement = Movement(Vector2(x, y), facing)
        isStuckInLoop = movement in visited
        visited.add(movement)
    }

    fun peek(lab: List<String>): Char? =
        lab.getOrNull(y + facing.y)?.getOrNull(x + facing.x)
}
