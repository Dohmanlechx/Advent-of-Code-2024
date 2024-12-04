fun main(args: Array<String>) {
    val letters = Reader.input("d04")
    val rowSize = letters.count()
    val colSize = letters[0].count()
    d04a(letters, rowSize, colSize)
    d04b(letters, rowSize, colSize)
}

fun d04a(letters: List<String>, rowSize: Int, colSize: Int) {
    var sum = 0

    for (x in 0..<rowSize) {
        for (y in 0..<colSize) {
            if (letters[x][y] == 'X') {
                Dir.entries.filter { letters.maybeGet(x, y, it) == 'M' }.forEach { dir ->
                    sum +=
                        if (
                            letters.maybeGet(x, y, dir, 2) == 'A' && letters.maybeGet(x, y, dir, 3) == 'S'
                        ) 1 else 0
                }
            }
        }
    }

    require(sum == 2454)
}

fun d04b(letters: List<String>, rowSize: Int, colSize: Int) {
    var sum = 0

    for (x in 0..<rowSize) {
        for (y in 0..<colSize) {
            fun Dir.adj(c: Char) = letters.maybeGet(x, y, this) == c

            if (letters[x][y] == 'A') {
                sum +=
                    if (
                        Dir.NW.adj('M') && Dir.NE.adj('S') && Dir.SE.adj('S') && Dir.SW.adj('M') ||
                        Dir.NW.adj('S') && Dir.NE.adj('S') && Dir.SE.adj('M') && Dir.SW.adj('M') ||
                        Dir.NW.adj('S') && Dir.NE.adj('M') && Dir.SE.adj('M') && Dir.SW.adj('S') ||
                        Dir.NW.adj('M') && Dir.NE.adj('M') && Dir.SE.adj('S') && Dir.SW.adj('S')
                    ) 1 else 0
            }
        }
    }

    require(sum == 1858)
}

fun List<String>.maybeGet(x: Int, y: Int, dir: Dir, offset: Int = 1): Char? =
    getOrNull(x + dir.x * offset)?.getOrNull(y + dir.y * offset)

enum class Dir(val x: Int, val y: Int) {
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, -1),
    N(0, -1),
    NE(1, -1),
}
