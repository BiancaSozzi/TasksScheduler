import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IndexOutOfBoundsException
import java.time.DateTimeException
import java.time.LocalTime

internal class TasksSchedulerKtTest {

    @Test
    fun `test nextScheduledRun every minute`() {
        val result = nextScheduledRun("* * /bin/run_me_every_minute", LocalTime.of(16, 10))
        assertEquals("16:10 today - /bin/run_me_every_minute", result)
    }

    @Test
    fun `test nextScheduledRun sixty times in the simulated time`() {
        val result = nextScheduledRun("* 16 /bin/run_me_sixty_times", LocalTime.of(16, 10))
        assertEquals("16:10 today - /bin/run_me_sixty_times", result)
    }

    @Test
    fun `test nextScheduledRun sixty times`() {
        val result = nextScheduledRun("* 19 /bin/run_me_sixty_times", LocalTime.of(16, 10))
        assertEquals("19:00 today - /bin/run_me_sixty_times", result)
    }

    @Test
    fun `test nextScheduledRun N minutes past every hour`() {
        val result = nextScheduledRun("45 * /bin/run_me_hourly", LocalTime.of(16, 10))
        assertEquals("16:45 today - /bin/run_me_hourly", result)
    }

    @Test
    fun `test nextScheduledRun once at specific time`() {
        val result = nextScheduledRun("30 1 /bin/run_me_daily", LocalTime.of(16, 10))
        assertEquals("01:30 tomorrow - /bin/run_me_daily", result)
    }

    @Test
    fun `test nextScheduledRun throws exception when invalid time`() {
        assertThrows(DateTimeException::class.java) {
            nextScheduledRun("20 24 /bin/run_me_daily", LocalTime.of(16, 10))
        }
    }

    @Test
    fun `test nextScheduleRun throws exception when invalid input format`() {
        assertThrows(IndexOutOfBoundsException::class.java) {
            nextScheduledRun("** ** /bin/run_me_daily", LocalTime.of(16, 10))
        }
    }
}