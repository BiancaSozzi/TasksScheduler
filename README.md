# Tasks Scheduler
We have a set of tasks, each running at least daily, which are scheduled with a simplified cron. 
We want to find when each of them will next run.

### Input
The scheduler config looks like this:

- 30 1 /bin/run_me_daily
- 45 * /bin/run_me_hourly
- \* * /bin/run_me_every_minute
- \* 19 /bin/run_me_sixty_times

The first field is the minutes past the hour, the second field is the hour of the day and 
the third is the command to run. For both cases * means that it should run for all values of 
that field. In the above example run_me_daily has been set to run at 1:30am every day and
run_me_hourly at 45 minutes past the hour every hour. 
The fields are whitespace separated and each entry is on a separate line.

### Output

When the task should fire at the simulated 'current time' then that is the time you should 
output, not the next one.

For example given the above examples as input and the simulated 'current time' command-line argument 16:10 
the output should be

- 1:30 tomorrow - /bin/run_me_daily
- 16:45 today - /bin/run_me_hourly
- 16:10 today - /bin/run_me_every_minute
- 19:00 today - /bin/run_me_sixty_times

## How to run this project
### Requirements
- Kotlin command line compiler: Instructions on how to install it can be found [here](https://kotlinlang.org/docs/command-line.html)

### Compile the project
    kotlinc .\src\main\kotlin\TasksScheduler.kt -include-runtime -d taskScheduler.jar
where: 
- `.\src\main\kotlin\TasksScheduler.kt` is the path to the script
- `-include-runtime` makes the resulting .jar file self-contained and runnable by including the Kotlin runtime library in it
- `-d taskScheduler.jar` is the destination jar file

### Run the project
    cat .\src\main\kotlin\test.txt | java -jar .\taskScheduler.jar 16:10
where: 
- `cat .\src\main\kotlin\test.txt`: I have included a test.txt file to contain the scheduled tasks. It can be modified to test other cases or change the path to your own test file.
- `java -jar .\taskScheduler.jar`: Start execution of the jar file.
- `12:13`: It's the argument for the simulated current time. If no argument is provided it will default to real current time.