const express = require('express');
const router = express.Router();

const deckRoutes = require('./deckRoutes'); 


// Sử dụng các route con
router.use('/decks', deckRoutes);

module.exports = router;