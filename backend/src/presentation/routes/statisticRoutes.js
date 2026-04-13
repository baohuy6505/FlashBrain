const express = require('express');
const router = express.Router();
const statisticController = require('../controllers/statisticController');
const authMiddleware = require('../middlewares/authMiddleware');

// GET /api/statistics/me
router.get('/me', authMiddleware, statisticController.getMyStatistics.bind(statisticController));

module.exports = router;