# User Guide

* [Quick Start](#quick-start)
* [Features](#features)

# Quick start
 
1. Ensure that you have the latest Java version ‘1.8.0_60’ or later installed in your 	Computer.<br>
	> Having any Java 8 version is not enough<br>
	> The application will not work for any earlier Java versions
2. Find the project in the `Project Explorer` or `Package Explorer` (usually located 	at the left side)
3. Right click on the project
4. Click `Run As` > `Java Application` and choose the `Main` class. The GUI should 	appear within split second.
5. Type the command into the command box and press <kbd>Enter</kbd> to execute the 	command. <br>
	List of commands:
	 * **`add`** : adds a task
	 * **`show`** : shows all tasks
	 * **`find`** : searches for a task
	 * **`edit`** : edits a task
	 * **`delete`** : deletes a task
	 * **`complete`** : marks a task as completed
	 * **`undo`** : marks a task as completed
	 * **`help`** : opens instruction

7.  Refer to the [Commands](#commands) section below for details of each command.<br>


# Features

## Descriptions and Usage
### Tasks
The daily planner stores all of the users events as a `Task`.

A `Task` can be viewed, added, searched, edited or deleted with the right command word as can be found in [Commands](#commands).

**Every `Task` must consist of a mandatory `TASKNAME` field and optional `STARTDATE`, `STARTTIME`, `ENDDATE`, `ENDTIME`, `isRECURRING` fields.**

#### `STARTDATE` and `ENDDATE` Field
The `STARTDATE` and `ENDDATE` field, if specified, tells the Daily Planner which date(s) the task is meant for. If no `ENDDATE` is specified, Daily Planner assumes the task is meant to be done today. 

The `STARTDATE` and `ENDDATE` field can accept natural descriptions of dates. The following are all valid dates:
```
1978-01-28
1984/04/02
1/02/1980
2/28/79
The 31st of April in the year 2008
Fri, 21 Nov 1997
Jan 21, '97
Sun, Nov 21
jan 1st
february twenty-eighth
next thursday
last wednesday
today
tomorrow
yesterday
next week
next month
next year
3 days from now
three weeks ago
```

#### `STARTTIME` and `ENDTIME` fields

1. No `STARTTIME` or `ENDTIME` fields have to be entered for tasks with no specific timing
2. For a task that must occur within a fixed time period, `STARTTIME` and `ENDTIME` fields must be given in the following format:
    * `s/STARTTIME e/ENDTIME`
    * Example: `s/2pm e/4pm` 
3. For tasks with only a deadline, only the `ENDTIME` has to be entered after `e/` keyword:
    * `e/ENDTTIME`
    * Example: `e/6pm`

`STARTTIME` and `ENDTIME` fields can also accept various natural desciptions:

```
0600h
06:00 hours
6pm
5:30 a.m.
5
12:59
23:59
8p
noon
afternoon
midnight
```

#### `isRECURRING` field

If a recurring `Task` must be described in a command, (e.g. tasks that must occur every week), the optional recurring field can be entered in the format:

`every CYCLE`,

Where `CYCLE` can be `week` or `month`.

Example Command:

`add cs lecture 2pm to 4pm every week`




## Commands 
### Viewing help : `help`
Format: 

```
help
```

> Help is also shown if you enter an incorrect command e.g. `abcd`

### Adding a task : `add`

Adds a task to the planner <br><br>
Format:<br> 
```
1. add TASKNAME s/STARTDATE(optional)
2. add TASKNAME s/STARTDATE(optional) STARTTIME(optional) e/ENDDATE (optional) ENDTIME (optional)
3. add TASKNAME d/STARTDATE(optional) e/ENDDATE(optional) 
```

Examples:<br>

1. `add gym s/today` <br>
> Task with no specified timing is added to today's schedule<br>
 
2. `add meeting s/tomorrow 2pm e/4pm` <br>
> Fixed task is added from 2pm to 4pm the next day<br>

3. `add math homework e/tomorrow 6pm` <br>
> Task with a deadline is added, no date is specified so today's date is assumed<br>


### Viewing a schedule : `show`

Shows tasks on a particular day

Format: 

```
show STARTDATE(optional)
```

Examples:<br>
1. `show today`
>Shows schedule for today<br>
2. `show next wednesday`
>Shows schedule for next wednesday <br>
2. `show complete`
>Shows completed tasks <br>

<br>

The show function will generate the user's schedule when queried. Starting from the current time, it will consider all tasks in hand for the day and assign them to timeslots. 

Urgent tasks will be scheduled first(tasks with nearing deadlines). Fixed tasks will be scheduled only during their specified timeslot and floating tasks will be inserted to remaining empty timeslots throughout the day. Daily Planner will even account for breaks in between certain hours of consecutive tasks(say, every 3 hours). 

The user can then use this recommended schedule to worryless-ly go about their day. 


### Searching for a task: `find`

Searches for a particular task and displays more information about it.<br>

Format: 

```
find TASKNAME
```

Examples:

```
find cs lecture
```

> * The search is case insensitive.
> * The order of the keywords does not matter. e.g. `math assignment` will match `assignment math`
> * Only the name is searched.
> * Task matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `Hans` will match `Hans Bo`

### Editing a task: `edit`

Edits a particular task's details<br>
Format:
```
edit INDEX 
NEWTASKNAME(optional) NEWDATE(optional) NEWSTARTTIME(optional) to NEWENDTIME(optional)
```
Examples:<br>
`edit 2 tomorrow`<br>
  `s/wednesday 4pm e/6pm`<br>
(only changes date and time)<br>
  
 
#### Deleting a task : `delete`

Description: Deletes a task from the planner. <br>

Format: `delete INDEX` 

> Deletes the task at the specified `INDEX`. 
  The index refers to the index number shown on the list that is currently being viewed<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples: <br>
```delete 5```
>Deletes task 5 of current list being viewed

#### Completing a task : `complete`

Description: Marks a task as completed from the planner. <br>

Format: `complete INDEX` 

> Marks the task at the specified `INDEX` as completed. 
  The index refers to the index number shown on the list that is currently being viewed<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples: <br>
```complete 5```
>Task 5 of current list being viewed is marked as completed

#### Deleting a task : `undo`

Description: Undo the latest command. <br>

Format: `undo` 

> Undo the last command. 
  If the last command was `add`, the task added will be removed if `undo` is invoked<br>
  

Examples: <br>
```undo```
>Undo previous command

### Exiting the program : `exit`
Exits the program.<br>
Format: `exit`  

### Saving the data 
Daily Planner data is saved in the hard disk automatically after any command that changes the data.<br>
There is no need to save manually.
