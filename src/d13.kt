import kotlin.time.measureTime

fun main(args: Array<String>) {
    measureTime {
        d13a()
        d13b()
    }.let { println("d13 completed in $it") }
}

fun d13a() {
    val tokens = Reader.input("d13", split = "\n\r").fold(0) { acc, it ->
        acc + tokensNeededToWinP1(it.split("\n"))
    }
    require(tokens == 37686)
}

fun d13b() {
    val lines = Reader.input("d13")
}

fun tokensNeededToWinP1(machine: List<String>): Int {
    val a = machine[0].parseButton()
    val b = machine[1].parseButton()
    val p = machine[2].parsePrize()
    var o = Vector2(0, 0)
    var aPress = 0
    var bPress = 0
    while (bPress <= 100 && p.x > o.x && p.y > o.y) {
        bPress++
        o = o.offset(b)
    }
    if (o == p) {
        return bPress * 1
    }
    while (aPress <= 100 && (p.x > o.x || p.y > o.y)) {
        aPress++
        o = o.offset(a)
        while (o.x > p.x || o.y > p.y) {
            bPress--
            o = o.offset(Vector2(-b.x, -b.y))
        }

    }
    if (o == p) {
        return (bPress * 1) + (aPress * 3)
    }
    return 0
}

private fun String.parseButton() = Vector2(
    substringAfter("X+").substringBefore(",").toInt(),
    substringAfter("Y+").substringBefore("\r").toInt()
)

private fun String.parsePrize() = Vector2(
    substringAfter("X=").substringBefore(",").toInt(),
    substringAfter("Y=").toInt()
)