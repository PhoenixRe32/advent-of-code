import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        return input.mapNotNull {
            val gameSections = it.splitGameSections()
            val roundResults = gameSections[1]
                .splitGameRounds()
                .map(String::toResult)
            if (roundResults.all(RGB::isPossible)) gameSections[0]
            else null
        }
            .map { it.getId() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val gameSections = it.splitGameSections()
            gameSections[1]
                .splitGameRounds()
                .map(String::toResult)
                .foldRight(MinRGB(), ::buildMinRequirement)
        }
            .map(MinRGB::power)
            .sum()
    }

    val testInput1 = readInput("Day02_test1")
    check(part1(testInput1).also { println("Test input part 1 result is: $it") } == 8)

    val input = readInput("Day02_input")
    println("Actual Answer Part 1")
    println("===========")
    part1(input).println()

    val testInput2 = readInput("Day02_test2")
    check(part2(testInput2).also { println("Test input part 2 result is: $it") } == 2286)

    println("Actual Answer Part 2")
    println("===========")
    part2(input).println()
}

private fun String.splitGameSections(): List<String> =
    split(':')

private fun String.splitGameRounds(): List<String> =
    split(';')

private fun String.toResult(): RGB =
    split(',')
        .map(String::trim)
        .map(String::lowercase)
        .foldRight(RGB(), ::buildRoundResult)

private fun buildRoundResult(roundResultDescription: String, roundResult: RGB): RGB =
    if (roundResultDescription.endsWith("red"))
        roundResult.copy(red = roundResultDescription.removeSuffix("red").trim().toInt())
    else if (roundResultDescription.endsWith("blue"))
        roundResult.copy(blue = roundResultDescription.removeSuffix("blue").trim().toInt())
    else
        roundResult.copy(green = roundResultDescription.removeSuffix("green").trim().toInt())

private fun buildMinRequirement(roundResult: RGB, minRequirement: MinRGB): MinRGB =
    minRequirement.copy(
        red = max(minRequirement.red, roundResult.red),
        green = max(minRequirement.green, roundResult.green),
        blue = max(minRequirement.blue, roundResult.blue),
    )

private fun String.getId(): Int =
    lowercase().removePrefix("game").trim().toInt()

private data class RGB(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
) {
    val isPossible = red in 0..12
            && green in 0..13
            && blue in 0..14
}

private data class MinRGB(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
) {
    val power = red * green * blue
}