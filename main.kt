package minesweeper
import kotlin.random.Random
import kotlin.system.exitProcess
val map = MutableList(9) { MutableList(9) { '.' } }
val field = MutableList(9) { MutableList(9) { '.' } }
fun draw() = println("""
     │123456789│
    —│—————————│
    1│${field[0].joinToString("")}│
    2│${field[1].joinToString("")}│
    3│${field[2].joinToString("")}│
    4│${field[3].joinToString("")}│
    5│${field[4].joinToString("")}│
    6│${field[5].joinToString("")}│
    7│${field[6].joinToString("")}│
    8│${field[7].joinToString("")}│
    9│${field[8].joinToString("")}│
    —│—————————│
    """.trimIndent())
fun place(x: Int, y: Int) {
    map[x][y] = 'X'
    for (i in maxOf(x - 1, 0)..minOf(x + 1, 8)) {
        for (j in maxOf(y - 1, 0)..minOf(y + 1, 8)) {
            when(map[i][j]) {
                '.' -> map[i][j] = '1'
                in '1'..'7' -> map[i][j]++
            }
        }
    }
}
fun set(n: Int) {
    for (k in 1..n) {
        while (map.joinToString("").count { it == 'X' } != n) {
            val x = Random.nextInt(9)
            val y = Random.nextInt(9)
            if (map[x][y] != 'X') place(x, y)
        }
    }
}
fun mark() {
    while (true) {
        print("Set/unset mines marks or claim a cell as free: ")
        val (y, x, a) = readln().split(' ')
        when (a) {
            "free" -> {
                when (map[x.toInt() - 1][y.toInt() - 1]) {
                    'X' -> {
                        draw()
                        println("You stepped on a mine and failed!")
                        exitProcess(0)
                    }
                    else -> {
                        floodFill(x.toInt() - 1, y.toInt() - 1)
                        break
                    }
                }
            }
            "mine" -> {
                when (field[x.toInt() - 1][y.toInt() - 1]) {
                    '.' -> {
                        field[x.toInt() - 1][y.toInt() - 1] = '*'
                        break
                    }
                    'X' -> {
                        field[x.toInt() - 1][y.toInt() - 1] = '*'
                        map[x.toInt() - 1][y.toInt() - 1] = '*'
                        break
                    }
                    '*' -> {
                        field[x.toInt() - 1][y.toInt() - 1] = '.'
                        break
                    }
                    else -> break
                }
            }
        }
    }
}
fun floodFill(x: Int, y: Int) {
    if (x >= 9 || y >= 9 || x < 0 || y < 0) return
    if (map[x][y] == '.') {
        map[x][y] = '/'
        field[x][y] = '/'
        floodFill(x + 1, y)
        floodFill(x - 1, y)
        floodFill(x, y + 1)
        floodFill(x, y - 1)
        floodFill(x + 1, y + 1)
        floodFill(x - 1, y - 1)
        floodFill(x + 1, y - 1)
        floodFill(x - 1, y + 1)
    } else field[x][y] = map[x][y]
}
fun main() {
    print("How many mines do you want on the field? ")
    val n = readln().toInt()
    set(n)
    draw()
    while (map.joinToString("").contains('X') && map.joinToString("").count { it == '*' } != n) {
        draw()
        mark()
    }
    print("Congratulations! You found all the mines!")
}
