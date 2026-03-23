const express = require('express');
const router = express.Router();

const deckRoutes = require('./deckRoutes'); 
const authRoutes = require('./authRoutes');

// Sử dụng các route con
router.use('/auth', authRoutes);

// Sử dụng các route con
router.use('/decks', deckRoutes);

module.exports = router;