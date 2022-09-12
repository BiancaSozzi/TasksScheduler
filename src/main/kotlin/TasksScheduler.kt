import java.lang.IndexOutOfBoundsException
import java.time.LocalTime

/**
 * Read input data from the terminal
 * @return MutableList<String> list of tasks entries
 */
fun readInputData(): MutableList<String> {
    val tasks = mutableListOf<String>()
    var text = readlnOrNull()
    while(text != null) {
        tasks.add(text)
        text = readlnOrNull()
    }
    return tasks
}

/**
 * Separate input string to each element.
 * Returns a list containing
 *  - complete expression
 *  - minutes
 *  - hour
 *  - command for the task
 */
fun parseData(line: String): List<String> {
    val regex = Regex("^(\\d+|\\*)\\s+(\\d+|\\*)\\s+(.*)")
    runCatching {
        return regex.find(line)!!.groupValues
    }.onFailure {
        throw IndexOutOfBoundsException("Wrong input format. $line could not be parsed")
    }
    return emptyList()
}

/**
 * Prints error message
 * @param error Throwable
 */
fun logError(error: Throwable, message: String = "") {
    println(
        "${ConsoleColors.COLOR_RED} $error \n $message ${ConsoleColors.COLOR_RESET}")
}

/**
 *  Process the input and return next run time
 *  @param schedule input string from the console
 *  @param currentTime simulated current time
 *  @return String indicating time day and command
 */
fun nextScheduledRun(schedule: String, currentTime: LocalTime): String {
    // Parse string into time and task
    val (_, minutes, hour, task) = parseData(schedule)

    // Get next running time
    val time = when {
        // Run every minute
        minutes == "*" && hour == "*" -> LocalTime.of(currentTime.hour, currentTime.minute)
        // Run sixty times
        minutes == "*" && hour != "*" -> {
            if (hour.toInt() == currentTime.hour) {
                LocalTime.of(currentTime.hour, currentTime.minute)
            } else {
                LocalTime.of(hour.toInt(), 0)
            }
        }
        // Run N minutes past every hour
        minutes != "*" && hour == "*" -> {
            val atHour = if (currentTime.minute > minutes.toInt()) currentTime.hour.plus(1) else currentTime.hour
            LocalTime.of(atHour, minutes.toInt())
        }
        // Run once at specific time
        else -> LocalTime.of(hour.toInt(), minutes.toInt())
    }

    // Calculate day of execution
    val day = if (currentTime <= time) "today" else "tomorrow"

    return "$time $day - $task"
}

fun main(args: Array<String>) {
    // Default simulated time to now
    val currentTime = LocalTime.now()
    var simulatedTime: LocalTime = LocalTime.of(currentTime.hour, currentTime.minute)

    runCatching {
        // Get simulated time input parameter
        simulatedTime = LocalTime.parse(args[0])
    }.onFailure {
        logError(it, "Running with simulated time $simulatedTime")
    }

    // Read data and print next execution time for each entry
    readInputData().forEach {
        println(
            nextScheduledRun(it, simulatedTime)
        )
    }
}

/**
    This object is used to format the color of the output text in the console
 */
object ConsoleColors {
    const val COLOR_RED = "\u001B[31m"
    const val COLOR_RESET = "\u001B[0m"
}
