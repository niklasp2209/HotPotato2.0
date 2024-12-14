# HotPotato (Minecraft Minigame, 1.21.x)

## Overview

**HotPotato** is a fun and fast-paced Minecraft minigame where players are given a potato, and they must quickly pass it on to another player. If a player holds the potato for too long, they are eliminated from the game. The goal is to be the last player standing.

This project is modularly programmed to allow easy extensibility, with various game features and modules that can be added or removed.

## Features

- **Modular Design**: The game is built with a modular structure, allowing easy integration and removal of features.
- **HotPotato Game Mechanics**: Players receive the "Hot Potato" and must pass it to others as quickly as possible. Holding the potato for too long results in elimination.
- **Redis**: Redis is used for caching and managing game states (used here as a placeholder for demonstration purposes).
- **SQL**: SQL databases are integrated for storing player data, game statistics, and other persistent data.
- **Game State Management**: The game has different states (Lobby, In-game, Ending), and the state transitions are handled dynamically.
  
## Disclaimer

This project is a portfolio piece and may not be fully functional. Some features may be incomplete or only serve as a demonstration of programming practices and techniques. It is not intended for production use and should be viewed as a reference project only.
