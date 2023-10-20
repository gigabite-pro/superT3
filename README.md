# superT3 - the ultimate version of tic-tac-toe
superT3 is the ultimate tic-tac-toe, or to be particular, a nested tic-tac-toe game. Just like the original game, it also has a 3x3 grid of empty squares but with a little twist - all the 9 squares have their own 3x3 grid nested inside them.

### Rules
- To win the game, you have to win three of the larger squares in a row, and to win a large square, you have to win three in a row in the smaller grid inside it. 
- Say X goes first and marks the bottom right square on the small grid present in the upper left square on the large grid. Now, just like in normal tic-tac-toe, O gets to go. But O can’t just go anywhere. The smaller grid O must play in is determined by X’s move. So since X went in the bottom right corner within his small grid, O must go in the bottom right corner of the large grid. So O goes, anywhere in this small grid he wants to.
- If the smaller grid results in a tie, both X and O can use it to complete the large grid and win the game.
- If a player sends another player to a grid that has already been won, then the other player gets to go wherever he wants.

### What will the application do?
The game is a desktop application which has a menu in it. The menu has the following options:
- Play
- Leaderboard
- Quit

The leaderboard will show all the players who have played on that device with decreasing number of scores. The play button will start the game where each player will have to login with their credentials. This has been done to protect the scores of every player and to display them on the leaderboard.

It uses CSV files instead of a database to store user data. This increases the availability of the game across various operating systems. 

### Who will use it?
This game can be played by any two players of any age group. Children of age group(4-12) can play this game with no difficulties and this will help them to improve their logical reasoning skills. Moreover, this game doesn't have any explicit content that would restrict it from any age group.

### Why is this project of interest to you?
There are many reasons why this project interests me:
- I can apply the design recipes that learnt in CPSC110 (Generative Recursion, HTDW, etc).
- I'll get to learn how the games that I've been playing since my childhood actually work on the backend.
- The desktop app version of this game doesn't exist.
- It's fun.

### User Stories

- As a user, I want to be able to save my game data when I quit the game.
- As a user, I want to be able to load my game data when I run the game.
- As a user, I want to be able to play in the grid where I left in the last game.
- As a user, I want to be able to update my progress in a saved game.
- As a user, I want to be able to win/tie/lose the game.
- As a user, I want to be able to see my progress as I go further in the game.
- As a user, I want to be able to select which grid I want to play in, if that grid has already been won.
- As a user, I don't want to download any external resources to play the game.

### Instructions for Grader

- Start the game by running the Main file.
- You will see a Main Menu with two options: **Play** and **Quit**.
- Clicking **Play** will open a new menu with two options: **New Game** and **Load Game**.
- Clicking **Quit** will open a new window asking if you want to save a game or not (if it has been started).
- Clicking **New Game** will open a new game.
- Clicking **Load Game** will open a new window showing the available saved games.
- A table will be shown with Game data: Name, Current Player, Current Grid, and Date & Time.


## Phase 4: Task 2
```
Wed Apr 12 21:43:39 PDT 2023 : Created player X.
Wed Apr 12 21:43:39 PDT 2023 : Created player O.
Wed Apr 12 21:43:45 PDT 2023 : Created large grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:45 PDT 2023 : Created a small grid.
Wed Apr 12 21:43:47 PDT 2023 : Active grid set to 5.
Wed Apr 12 21:43:48 PDT 2023 : Added a X to grid 5
Wed Apr 12 21:43:49 PDT 2023 : Added a O to grid 2
Wed Apr 12 21:43:51 PDT 2023 : Added a X to grid 5
Wed Apr 12 21:43:51 PDT 2023 : Added a O to grid 6
Wed Apr 12 21:43:53 PDT 2023 : Added a X to grid 5
Wed Apr 12 21:43:54 PDT 2023 : Added a O to grid 8
Wed Apr 12 21:44:06 PDT 2023 : Data written to file.
```

## Phase 4: Task 3
If I had more time I would have added an interface called Game which would consist of the function for the GuiGame class and the game class. This is because both of these classes contain the same methods but the GuiGame class is for the GUI version of the code, while, the game class is for the CLI version of the code. I would also have added Exceptions then wherever possible so that the program does not crash if an error occurs. I could also have used some other third-party libraries to create the GUI to make the aesthetics of my game better.