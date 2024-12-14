import kotlin.time.measureTime

private const val W = 101
private const val H = 103
private const val W_MID = (W - 1) / 2
private const val H_MID = (H - 1) / 2

fun main(args: Array<String>) {
    measureTime {
        d14()
    }.let { println("d14 completed in $it") }
}

fun d14() {
    val robots = Reader.input("d14").map(Robot::parse)

    repeat(7037) { repeat ->
        val second = repeat + 1
        robots.forEach {
            it.p = it.p.offset(it.v)
            val x = if (it.p.x >= 0) it.p.x % W else (it.p.x % W + W) % W
            val y = if (it.p.y >= 0) it.p.y % H else (it.p.y % H + H) % H
            it.p = Vector2(x, y)
        }
        when (second) {
            7037 -> printEasterEgg(robots)
            100 -> {
                val result = listOf(
                    robots.count { it.p.x < W_MID && it.p.y < H_MID },
                    robots.count { it.p.x > W_MID && it.p.y < H_MID },
                    robots.count { it.p.x < W_MID && it.p.y > H_MID },
                    robots.count { it.p.x > W_MID && it.p.y > H_MID }
                ).fold(1) { acc, it -> acc * it }
                require(result == 218965032)
            }
        }
    }
}

data class Robot(var p: Vector2, val v: Vector2) {
    companion object {
        fun parse(str: String): Robot {
            val (px, py) = str.substringAfter("p=").substringBefore(" ").split(",").map(String::toInt)
            val (vx, vy) = str.substringAfter("v=").split(",").map(String::toInt)
            return Robot(Vector2(px, py), Vector2(vx, vy))
        }
    }
}

private fun printEasterEgg(robots: List<Robot>) {
    for (y in 0..<H) {
        for (x in 0..<W) {
            print(if (robots.any { it.p == Vector2(x, y) }) "*" else " ")
        }
        println()
    }
}