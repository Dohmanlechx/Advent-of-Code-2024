fun main(args: Array<String>) {
    val lab = Reader.input("d06")
    d06a(lab)
    d06b(lab)
}

fun d06a(lab: List<String>) {
    val guard = Guard().apply { setPosition(lab) }
    while (true) guard.peek(lab)?.let { guard.run { if (it != '#') advance() else turn() } } ?: break
    guard.advance()

    val sum = guard.visited.distinctBy(Movement::pos).count()
    require(sum == 5095)
}

fun d06b(lab: List<String>) {

}

private data class Movement(val pos: Pair<Int, Int>, val dir: Dir)

private class Guard {
    private var facing: Dir = Dir.N
    private var y: Int = 0
    private var x: Int = 0
    val visited: MutableSet<Movement> = mutableSetOf()

    fun setPosition(grid: List<String>) {
        for (y in 0..<grid.count()) {
            for (x in 0..<grid[y].count()) {
                if (grid[y][x] == '^') {
                    this.y = y
                    this.x = x
                    break
                }
            }
        }
    }

    fun turn() {
        facing = mapOf(Dir.N to Dir.E, Dir.E to Dir.S, Dir.S to Dir.W, Dir.W to Dir.N)[facing]!!
    }

    fun advance() {
        visited.add(Movement(Pair(x, y), facing))
        y += facing.y
        x += facing.x
    }

    fun peek(lab: List<String>): Char? =
        lab.getOrNull(y + facing.y)?.getOrNull(x + facing.x)
}

