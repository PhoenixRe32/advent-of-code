package day03

import println
import readInput

private typealias Cord = Pair<Int, Int>
private typealias XY = Pair<Cord, Cord>

private const val numberOrSingleSymbolExceptDot = """\d+|[^.\w]"""
private val numberOrSingleSymbolExceptDotRegex = Regex(numberOrSingleSymbolExceptDot)

fun main() {
    fun part1(input: List<String>): Int {
        val (machineParts, partNumbers) = input.extractSchematicComponents()
        return partNumbers
            .asSequence()
            .filter { partNumber -> machineParts.any { partNumber.isAdjacent(it) } }
            .sumOf(PartNumber::value)
    }

    fun part2(input: List<String>): Int {
        val (machineParts, partNumbers) = input.extractSchematicComponents()
        return machineParts
            .asSequence()
            .filter { it.isProbablyGear }
            .map { probablyGear -> probablyGear to partNumbers.filter { it.isAdjacent(probablyGear) } }
            .filter { (_, partNumbers) -> partNumbers.size == 2 }
            .map { (_, partNumbers) -> partNumbers }
            .sumOf { it.first().value * it.last().value }
    }

    val testInput1 = readInput("day03/test1")
    check(part1(testInput1).also { println("Test input part 1 result is: $it") } == 4361)

    val input = readInput("day03/input")
    print("Actual Answer Part 1: ")
    part1(input).println()

    println()
    println("======")
    println()

    val testInput2 = readInput("day03/test2")
    check(part2(testInput2).also { println("Test input part 2 result is: $it") } == 467835)

    print("Actual Answer Part 2: ")
    part2(input).println()
}

private fun Collection<String>.extractSchematicComponents(): Components =
    mapIndexed(::extractSchematicComponents)
        .reduce(Components::plus)

private fun extractSchematicComponents(y: Int, line: String): Components =
    numberOrSingleSymbolExceptDotRegex
        .findAll(line)
        .map(MatchResult::groups)
        .flatMap(MatchGroupCollection::toList)
        .mapNotNull { it }
        .map {
            when (it.matchedSymbol()) {
                true -> MachinePart(it.value.first(), it.toXY(y))
                else -> PartNumber(it.value.toInt(), it.toXY(y))
            }
        }
        .fold(Components()) { components, component ->
            when (component) {
                is MachinePart -> components.copy(machineParts = components.machineParts + component)
                is PartNumber -> components.copy(partNumbers = components.partNumbers + component)
            }
        }

private fun MatchGroup.matchedSymbol() = value.length == 1 && !value.first().isDigit()
private fun MatchGroup.toXY(y: Int) = (range.first to y) to (range.last to y)

private sealed class SchematicComponent(open val xy: XY)
private data class MachinePart(val value: Char, override val xy: XY) : SchematicComponent(xy) {
    val isProbablyGear = (value == '*')
}

private data class PartNumber(val value: Int, override val xy: XY) : SchematicComponent(xy) {
    fun isAdjacent(machinePart: MachinePart): Boolean {
        val machinePartPointX = machinePart.xy.first.first
        val machinePartPointY = machinePart.xy.first.second
        val partNumberY = xy.first.second // or xy.second.second, we assume part numbers are in same line
        val partNumberXMin = xy.first.first
        val partNumberXMax = xy.second.first
        return when (machinePartPointY) {
            partNumberY -> machinePartPointX == partNumberXMin - 1 || machinePartPointX == partNumberXMax + 1
            partNumberY + 1, partNumberY - 1 -> machinePartPointX in partNumberXMin - 1..partNumberXMax + 1
            else -> false
        }
    }
}

private data class Components(
    val machineParts: Collection<MachinePart> = emptyList(),
    val partNumbers: Collection<PartNumber> = emptyList()
) {
    operator fun plus(other: Components): Components =
        Components(
            machineParts = machineParts + other.machineParts,
            partNumbers = partNumbers + other.partNumbers
        )
}