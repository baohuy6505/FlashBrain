const express = require("express");
const router = express.Router();
const deckRoutes = require("./deckRoutes");
const flashcardRoutes = require("./flashcardRoutes");
const authRoutes = require("./authRoutes");
const statisticRoutes = require("./statisticRoutes");
const userProgressRoutes = require("./userProgressRoutes");
const notificationRoutes = require('./notificationRoutes')

// Sử dụng các route con
router.use("/auth", authRoutes);

// Sử dụng các route con
router.use("/decks", deckRoutes);

router.use("/flashcards", flashcardRoutes);

router.use("/statistics", statisticRoutes);

router.use("/notifications", notificationRoutes);

router.use("/progress", userProgressRoutes);


module.exports = router;
