# Q-Less Game Solver

This is a simple web app that solves the Q-less word game written in Kotlin-js
with react. [Q-Less](https://q-lessgame.com/) is a crossword solitaire game
where 12 dice with 6 letters on each side are rolled, and the player must use
all 12 letters to make words that connect. Words must have at least 3 letters,
and no proper nouns or names are allowed.

## Rules

1. Use each letter on the grid exactly once to form words.
2. All words must have at least 3 letters, and cannot be proper nouns or names.
3. All words must be connected either horizontally or vertically.
4. Any sequence of 3 or more letters that appear contiguously on a row or column
   must form a valid word.
5. Plurals are allowed, as long as the singular form of the word is also valid.
   For example, "cat" and "cats" would both be acceptable.
6. The app will recognize alternate spellings or forms of a word, but only if
   they are commonly accepted variants.
7. Both British and American spellings are accepted for all words, as long as
   they are commonly recognized variants.

## Usage

To use the Q-less Game Solver, visit https://qless.mihassan.com/ and enter the
12 letters from the dice into the input field. The app will then generate a
solution grid showing all the words that can be made using the given letters.

## Technologies Used

- HTML
- CSS
- [Kotlin-js](https://kotlinlang.org/docs/js-overview.html)
- [React](https://reactjs.org/)
- [Material UI](https://mui.com/)

## Contributing

If you find any bugs or issues with the Q-less Game Solver, please open a GitHub
issue in this repository.

If you would like to contribute to the project, feel free to fork this
repository and submit a pull request with your changes.

We welcome contributions to improve the q-less word game solver. Here are a few
areas where you can contribute:

1. **Optimize the algorithm:** If you have ideas on how to improve the
   efficiency of the solver algorithm, please feel free to submit a pull
   request.

2. **Write unit tests:** Adding more tests will help ensure the solver code
   works correctly and is more robust.

3. **Improve UX for both desktop and mobile:** Enhancing the user experience for
   both desktop and mobile platforms will make the game more enjoyable to play.

4. **Support dynamically loading arbitrary dictionary:** Currently, the solver
   uses a fixed set of dictionaries. Adding the ability to dynamically load 
   dictionaries would make the game more versatile.

5. **Use React Router to provide input letters via URL:** Adding a feature to
   allow users to input letters via URL would make it easier to share and play
   custom game configurations.

We appreciate any contributions and suggestions to make the q-less word game
solver even better!
