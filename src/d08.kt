import kotlin.time.measureTime

fun main(args: Array<String>) {
    measureTime {
        d08()
    }.let { println("d08 completed in $it") }
}

fun d08() {
    var p1Input = Reader.input("d08")
    var p2Input = Reader.input("d08")
    val antennas = mutableMapOf<Char, MutableList<Position>>()

    for (x in 0..<p1Input[0].count()) {
        for (y in 0..<p1Input.count()) {
            p1Input[y][x].let { if (it != '.') antennas.getOrPut(it) { mutableListOf() }.add(Position(x, y)) }
        }
    }

    for (positions in antennas.values) {
        for (i in positions.indices) {
            for (j in i + 1..<positions.size) {
                val pos1 = positions[i]
                val pos2 = positions[j]
                val dis = Position(pos1.x - pos2.x, pos1.y - pos2.y)
                p1Input = p1Input
                    .placeNode(pos1.x + dis.x, pos1.y + dis.y)
                    .placeNode(pos2.x - dis.x, pos2.y - dis.y)
                repeat(p2Input.count()) {
                    p2Input = p2Input
                        .placeNode(pos1.x + (dis.x * it), pos1.y + (dis.y * it))
                        .placeNode(pos2.x - (dis.x * it), pos2.y - (dis.y * it))
                }
            }
        }
    }

    require(p1Input.countNodes() == 423)
    require(p2Input.countNodes() == 1287)
}

private fun List<String>.countNodes() = joinToString().count { it == '#' }

private fun List<String>.placeNode(x: Int, y: Int): List<String> =
    mapIndexed { col, row ->
        if (x !in row.indices || y > col || col != y) row
        else row.toCharArray().apply { this[x] = '#' }.concatToString()
    }