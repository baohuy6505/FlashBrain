const express = require('express');
const router = express.Router();

const deckRoutes = require('./deckRoutes'); 
const flashcardRoutes = require('./flashcardRoutes')

// Sử dụng các route con
router.use('/decks', deckRoutes);

router.use('/flashcards', flashcardRoutes);

module.exports = router;