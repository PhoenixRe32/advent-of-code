import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val fileContent = Path("src/Day01_input").readText()

    val digitsText = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val digits = listOf(
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    )
    val digitsAll = digits + digitsText

    fun String.toNumber(): String {
        return if (digits.contains(this))
            this
        else
            digits[digitsText.indexOf(this)]
    }

    fileContent.lines().sumOf {
        Integer.parseInt(it.findAnyOf(digitsAll)!!.second.toNumber() + it.findLastAnyOf(digitsAll)!!.second.toNumber())
    }.let(::println)
}